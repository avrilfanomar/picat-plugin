package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Verifies that the basic module is treated as implicitly imported:
 * references to its predicates/functions resolve without an explicit import.
 */
class PicatImplicitBasicReferenceTest : BasePlatformTestCase() {

    @Test
    fun testResolveToBasicModuleWithoutExplicitImport() {
        // Arrange: create fake stdlib with basic.pi
        val vFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/basic.pi",
            """
                module basic.
                
                println(X).
            """.trimIndent()
        )
        val picatHomeVDir = vFile.parent.parent

        // Configure settings to point to fake Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        // Picat file that uses println/1 without importing basic
        val file = myFixture.configureByText(
            "main.pi",
            """
            module m.
            
            main() => pri<caret>ntln(123).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("println")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at println")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib basic.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/basic.pi") || resolvedPath.endsWith("\\lib\\basic.pi")
        )
    }
}
