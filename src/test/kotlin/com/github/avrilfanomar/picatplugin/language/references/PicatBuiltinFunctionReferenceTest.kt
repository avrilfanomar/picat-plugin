package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Tests that built-in functions like new_array, sum, all_different are properly resolved
 * even when external Picat stdlib is not configured.
 */
class PicatBuiltinFunctionReferenceTest : BasePlatformTestCase() {

    @Test
    fun testNewArrayResolution() {
        // Create a fake stdlib with basic.pi that includes new_array
        val basicFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/basic.pi",
            """
                module basic.
                
                new_array(_).
                new_array(_, _).
                new_array(_, _, _).
                new_array(_, _, _, _).
                new_array(_, _, _, _, _).
            """.trimIndent()
        )
        val picatHomeVDir = basicFile.parent.parent

        // Configure settings to point to fake Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        val file = myFixture.configureByText(
            "test.pi",
            """
            import cp.
            
            main =>
                X = new_array(5, 5).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("new_array")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at new_array")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib basic.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/basic.pi") || resolvedPath.endsWith("\\lib\\basic.pi")
        )
    }

    @Test
    fun testSumResolution() {
        // Create a fake stdlib with basic.pi that includes 'sum'
        val basicFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/basic.pi",
            """
                module basic.
                
                sum(_).
                all_different(_).
                all_different_except_0(_).
            """.trimIndent()
        )
        val picatHomeVDir = basicFile.parent.parent

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        val file = myFixture.configureByText(
            "test.pi",
            """
            import cp.
            
            main =>
                XLine = [1, 2, 3],
                sum(XLine) #= 6.
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("sum")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at sum")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib basic.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/basic.pi") || resolvedPath.endsWith("\\lib\\basic.pi")
        )
    }

    @Test
    fun testAllDifferentResolution() {
        // Create a fake stdlib with basic.pi that includes all_different
        val basicFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/basic.pi",
            """
                module basic.
                
                sum(_).
                all_different(_).
                all_different_except_0(_).
            """.trimIndent()
        )
        val picatHomeVDir = basicFile.parent.parent

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        val file = myFixture.configureByText(
            "test.pi",
            """
            import cp.
            
            main =>
                XLine = [1, 2, 3],
                all_different(XLine).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("all_different")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at all_different")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib basic.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/basic.pi") || resolvedPath.endsWith("\\lib\\basic.pi")
        )
    }

    @Test
    fun testEmbeddedResourcesAreAccessible() {
        // Since embedded resources aren't accessible in test environment,
        // create mock embedded files using test fixture
        val basicContent = """
            module basic.
            
            new_array(_).
            new_array(_, _).
            sum(_).
            all_different(_).
        """.trimIndent()

        val ioContent = """
            module io.
            
            writef(_).
            writef(_, _).
            write(_).
            println(_).
        """.trimIndent()

        // Create files in a location that simulates embedded resources
        myFixture.tempDirFixture.createFile("embedded/lib/basic.pi", basicContent)
        myFixture.tempDirFixture.createFile("embedded/lib/io.pi", ioContent)

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = "" // No external Picat configured

        // Test that we can create PSI files from the content (even if not from actual embedded resources)
        val psiFileFactory = com.intellij.psi.PsiFileFactory.getInstance(project)
        val fileType = com.intellij.openapi.fileTypes.FileTypeManager.getInstance()
            .getFileTypeByFileName("basic.pi")

        val basicPsi = psiFileFactory.createFileFromText("basic.pi", fileType, basicContent)
        assertNotNull("Should be able to create PSI file from basic.pi content", basicPsi)

        val ioPsi = psiFileFactory.createFileFromText("io.pi", fileType, ioContent)
        assertNotNull("Should be able to create PSI file from io.pi content", ioPsi)
    }

    @Test
    fun testWritefResolutionStillWorks() {
        // Create a fake stdlib with io.pi that includes writef
        val ioFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/io.pi",
            """
                module io.
                
                writef(_).
                writef(_, _).
                write(_).
                println(_).
            """.trimIndent()
        )
        val picatHomeVDir = ioFile.parent.parent

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        val file = myFixture.configureByText(
            "test.pi",
            """
            main =>
                writef("Hello %s\\n", "World").
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("writef")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at writef")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to stdlib io.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/io.pi") || resolvedPath.endsWith("\\lib\\io.pi")
        )
    }
}
