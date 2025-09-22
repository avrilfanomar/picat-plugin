package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatStdlibImportReferenceTest : BasePlatformTestCase() {

    @Test
    fun testImportResolvesToStdlibModuleFile() {
        // Arrange: create a fake Picat installation with lib/cp.pi inside the test temp dir (indexed by VFS)
        val vFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/cp.pi",
            """
                module cp.
                % dummy content
            """.trimIndent()
        )
        val picatHomeVDir = vFile.parent.parent

        // Point settings to the fake Picat executable location (directory is supported)
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        // Picat file that imports the stdlib module
        val file = myFixture.configureByText(
            "main.pi",
            """
            import cp.
            module m.
            """.trimIndent()
        )

        val idx = file.text.indexOf("cp")
        assertTrue("Test sanity: 'cp' should be present", idx >= 0)

        val element = file.findElementAt(idx) ?: error("No PSI element at 'cp'")
        val importItem = generateSequence(element) { it.parent }
            .firstOrNull { it is com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem }
            ?: error("No PicatImportItem parent found for 'cp'")

        val itemRefs = importItem.references.toList()
        val atomEl = (importItem as com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem).atom
        val atomRefs = atomEl?.references?.toList() ?: emptyList()
        val allRefs = itemRefs + atomRefs
        val resolvedRef = allRefs.firstOrNull { ref ->
            val resolved = ref.resolve()
            val path = resolved?.containingFile?.virtualFile?.path
            path != null && (path.endsWith("/lib/cp.pi") || path.endsWith("\\lib\\cp.pi"))
        }
        val msg = buildString {
            append("Expected an import reference that resolves to stdlib module file cp.pi. ")
            append("itemRefs=" + itemRefs.size + ", atomRefs=" + atomRefs.size + ". ")
            if (allRefs.isNotEmpty()) {
                append("FirstRefClass=" + allRefs.first()::class.qualifiedName)
            }
        }
        assertNotNull(msg, resolvedRef)
    }
}
