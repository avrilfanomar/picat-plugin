package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Verifies that the io module is treated as implicitly imported:
 * references to its predicates/functions resolve without an explicit import.
 */
class PicatImplicitIoReferenceTest : BasePlatformTestCase() {

    @Test
    fun testResolveToIoModuleWithoutExplicitImport() {
        // Arrange: create fake stdlib with io.pi
        val vFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/io.pi",
            """
                module io.
                
                print(X).
            """.trimIndent()
        )
        val picatHomeVDir = vFile.parent.parent

        // Configure settings to point to fake Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        // Picat file that uses print/1 without importing io
        val file = myFixture.configureByText(
            "main.pi",
            """
            module m.
            
            main() => pr<caret>int(123).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("print")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at print")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib io.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/io.pi") || resolvedPath.endsWith("\\lib\\io.pi")
        )
    }
}
