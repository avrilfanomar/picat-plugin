package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Integration test to verify that stdlib references resolve correctly.
 * Uses actual stdlib files from src/test/resources/lib/ as fixtures.
 */
class PicatCondReferenceIntegrationTest : BasePlatformTestCase() {

    private fun discoverStdlibModules(): List<String> {
        // Use a known file to find the lib directory path, then list all .pi files
        val knownFileUrl = javaClass.getResource("/lib/basic.pi")
            ?: error("Could not find /lib/basic.pi - ensure test resources are set up correctly")

        val libDir = File(knownFileUrl.toURI()).parentFile
        return libDir.listFiles()
            ?.filter { it.isFile && it.extension == "pi" }
            ?.map { it.name }
            ?: emptyList()
    }

    private fun setupStdlibFixture() {
        // Dynamically discover and copy all stdlib files from classpath test resources to fixture
        val stdlibModules = discoverStdlibModules()
        require(stdlibModules.isNotEmpty()) { "No stdlib modules found in /lib resource directory" }

        // Keep reference to first created file to get the picat-home path
        var firstFile: com.intellij.openapi.vfs.VirtualFile? = null

        stdlibModules.forEach { moduleName ->
            val content = javaClass.getResourceAsStream("/lib/$moduleName")
                ?.bufferedReader()
                ?.readText()
                ?: error("Failed to load stdlib module: $moduleName")

            val vFile = myFixture.tempDirFixture.createFile(
                "picat-home/lib/$moduleName",
                content
            )
            if (firstFile == null) {
                firstFile = vFile
            }
        }

        // Get the fixture's picat-home directory from the first created file
        val picatHomeVDir = firstFile?.parent?.parent ?: error("Failed to set up stdlib fixture")

        // Configure settings to point to fixture Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path
    }

    @Test
    fun testCondResolvesToPrimitives() {
        // cond is a built-in primitive, no stdlib setup needed
        val file = myFixture.configureByText(
            "test.pi",
            """
            fibc(N) = cond((N == 0; N == 1), 1, fibc(N - 1) + fibc(N - 2)).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("cond")
        val ref = file.findReferenceAt(caretOffset)

        assertNotNull("Should find reference at cond", ref)
        val resolved = ref?.resolve()

        assertNotNull("cond should resolve to primitives.pi", resolved)
        val fileName = resolved?.containingFile?.name
        assertEquals("primitives.pi", fileName)
    }

    @Test
    fun testAbsResolvesToMathModule() {
        // abs is early in math.pi, before the constant `e` that stops parsing
        setupStdlibFixture()

        val file = myFixture.configureByText(
            "test.pi",
            """
            test(N) = abs(N).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("abs")
        val ref = file.findReferenceAt(caretOffset)

        assertNotNull("Should find reference at abs", ref)
        val resolved = ref?.resolve()

        assertNotNull("abs should resolve to math.pi", resolved)
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to math.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/math.pi") || resolvedPath.endsWith("\\lib\\math.pi")
        )
    }

    @Test
    fun testCeilingResolvesToMathModule() {
        // ceiling is early in math.pi, before the constant `e`
        setupStdlibFixture()

        val file = myFixture.configureByText(
            "test.pi",
            """
            test(N) = ceiling(N).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("ceiling")
        val ref = file.findReferenceAt(caretOffset)

        assertNotNull("Should find reference at ceiling", ref)
        val resolved = ref?.resolve()

        assertNotNull("ceiling should resolve to math.pi", resolved)
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to math.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/math.pi") || resolvedPath.endsWith("\\lib\\math.pi")
        )
    }

    @Test
    fun testPrintlnResolvesToIoModule() {
        // println is in io.pi (not basic.pi)
        setupStdlibFixture()

        val file = myFixture.configureByText(
            "test.pi",
            """
            main => println("Hello").
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("println")
        val ref = file.findReferenceAt(caretOffset)

        assertNotNull("Should find reference at println", ref)
        val resolved = ref?.resolve()

        assertNotNull("println should resolve to io.pi", resolved)
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to io.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/io.pi") || resolvedPath.endsWith("\\lib\\io.pi")
        )
    }

    @Test
    fun testReadFileResolvesToIoModule() {
        setupStdlibFixture()

        val file = myFixture.configureByText(
            "test.pi",
            """
            main => X = read_file_chars("file.txt").
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("read_file_chars")
        val ref = file.findReferenceAt(caretOffset)

        assertNotNull("Should find reference at read_file_chars", ref)
        val resolved = ref?.resolve()

        assertNotNull("read_file_chars should resolve to io.pi", resolved)
        val resolvedPath = resolved?.containingFile?.virtualFile?.path ?: error("Resolution path is null")
        assertTrue(
            "Expected resolution to io.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/io.pi") || resolvedPath.endsWith("\\lib\\io.pi")
        )
    }
}
