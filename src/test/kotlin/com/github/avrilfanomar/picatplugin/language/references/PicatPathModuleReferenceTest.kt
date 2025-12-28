package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallSimple
import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Tests for PICATPATH module reference resolution.
 *
 * Verifies that modules placed in directories specified by the PICATPATH setting
 * are correctly resolved when imported. Tests multi-module scenarios and priority
 * ordering between different module sources.
 */
class PicatPathModuleReferenceTest : BasePlatformTestCase() {

    @Test
    fun testResolvePredicateFromPicatPathDirectory() {
        // Arrange: create a module in a separate directory (simulating PICATPATH)
        val moduleFile = myFixture.tempDirFixture.createFile(
            "picatpath-lib/myutil.pi",
            """
                module myutil.

                helper(X).
                helper(X, Y).
            """.trimIndent()
        )
        val picatPathDir = moduleFile.parent

        // Configure PICATPATH to point to this directory
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = "" // No stdlib configured
        settings.picatPath = picatPathDir.path

        // Picat file that imports myutil and uses helper/1
        val file = myFixture.configureByText(
            "main.pi",
            """
            import myutil.
            module main.

            run() => helper(42).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("helper(42)")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at helper(42)")
        val resolved = ref.resolve() ?: error("helper/1 did not resolve from PICATPATH")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to picatpath-lib/myutil.pi but got: $path",
            path.endsWith("/picatpath-lib/myutil.pi") || path.endsWith("\\picatpath-lib\\myutil.pi")
        )
    }

    @Test
    fun testResolveFromPicatPathLibSubdirectory() {
        // PICATPATH directories can have lib/ subdirectory (similar to stdlib)
        val moduleFile = myFixture.tempDirFixture.createFile(
            "custom-picat/lib/mathext.pi",
            """
                module mathext.

                square(X) = X * X.
            """.trimIndent()
        )
        // Point PICATPATH to the parent of lib/
        val picatPathDir = moduleFile.parent.parent

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        settings.picatPath = picatPathDir.path

        val file = myFixture.configureByText(
            "calc.pi",
            """
            import mathext.
            module calc.

            compute(N) = square(N).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("square(N)")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at square(N)")
        val resolved = ref.resolve() ?: error("square/1 did not resolve from PICATPATH lib/")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to custom-picat/lib/mathext.pi but got: $path",
            path.contains("mathext.pi")
        )
    }

    @Test
    fun testMultiplePicatPathDirectories() {
        // Create modules in two separate directories
        val module1 = myFixture.tempDirFixture.createFile(
            "path1/module_a.pi",
            """
                module module_a.

                func_a(X).
            """.trimIndent()
        )
        val module2 = myFixture.tempDirFixture.createFile(
            "path2/module_b.pi",
            """
                module module_b.

                func_b(X).
            """.trimIndent()
        )

        val path1 = module1.parent.path
        val path2 = module2.parent.path

        // Configure PICATPATH with both directories (colon-separated on Unix)
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        settings.picatPath = "$path1${java.io.File.pathSeparatorChar}$path2"

        val file = myFixture.configureByText(
            "multi.pi",
            """
            import module_a.
            import module_b.
            module multi.

            test1() => func_a(1).
            test2() => func_b(2).
            """.trimIndent()
        )

        // Verify func_a resolves to path1/module_a.pi
        val refA = file.findReferenceAt(file.text.indexOf("func_a(1)"))
            ?: error("No reference at func_a(1)")
        val resolvedA = refA.resolve() ?: error("func_a/1 did not resolve")
        assertTrue(
            "func_a should resolve to module_a.pi",
            resolvedA.containingFile?.virtualFile?.path?.contains("module_a.pi") == true
        )

        // Verify func_b resolves to path2/module_b.pi
        val refB = file.findReferenceAt(file.text.indexOf("func_b(2)"))
            ?: error("No reference at func_b(2)")
        val resolvedB = refB.resolve() ?: error("func_b/1 did not resolve")
        assertTrue(
            "func_b should resolve to module_b.pi",
            resolvedB.containingFile?.virtualFile?.path?.contains("module_b.pi") == true
        )
    }

    @Test
    fun testPicatPathOverridesProjectRoots() {
        // Create same module in both PICATPATH and project lib/
        val picatPathModule = myFixture.tempDirFixture.createFile(
            "external/shared.pi",
            """
                module shared.

                from_picatpath(X).
            """.trimIndent()
        )
        myFixture.tempDirFixture.createFile(
            "lib/shared.pi",
            """
                module shared.

                from_project(X).
            """.trimIndent()
        )

        // Configure PICATPATH to point to external/
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        settings.picatPath = picatPathModule.parent.path

        val file = myFixture.configureByText(
            "priority.pi",
            """
            import shared.
            module priority.

            test() => from_picatpath(1).
            """.trimIndent()
        )

        // Should resolve to PICATPATH version (external/shared.pi)
        val ref = file.findReferenceAt(file.text.indexOf("from_picatpath(1)"))
            ?: error("No reference at from_picatpath(1)")
        val resolved = ref.resolve() ?: error("from_picatpath/1 did not resolve")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to external/shared.pi (PICATPATH) but got: $path",
            path.contains("external")
        )
    }

    @Test
    fun testStdlibOverridesPicatPath() {
        // Create a fake stdlib module
        val stdlibModule = myFixture.tempDirFixture.createFile(
            "picat-home/lib/cp.pi",
            """
                module cp.

                stdlib_func(X).
            """.trimIndent()
        )
        // Create same module in PICATPATH
        val picatPathModule = myFixture.tempDirFixture.createFile(
            "my-modules/cp.pi",
            """
                module cp.

                custom_func(X).
            """.trimIndent()
        )

        val settings = PicatSettings.getInstance(project)
        // Configure both stdlib (via executable path) and PICATPATH
        settings.picatExecutablePath = stdlibModule.parent.parent.path
        settings.picatPath = picatPathModule.parent.path

        val file = myFixture.configureByText(
            "stdlib_priority.pi",
            """
            import cp.
            module stdlib_priority.

            test() => stdlib_func(1).
            """.trimIndent()
        )

        // Should resolve to stdlib version (picat-home/lib/cp.pi), not PICATPATH
        val ref = file.findReferenceAt(file.text.indexOf("stdlib_func(1)"))
            ?: error("No reference at stdlib_func(1)")
        val resolved = ref.resolve() ?: error("stdlib_func/1 did not resolve")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to picat-home/lib/cp.pi (stdlib) but got: $path",
            path.contains("picat-home")
        )
    }

    @Test
    fun testLocalDefinitionOverridesPicatPath() {
        // Create a module in PICATPATH
        val picatPathModule = myFixture.tempDirFixture.createFile(
            "external-lib/utils.pi",
            """
                module utils.

                compute(X) = X.
            """.trimIndent()
        )

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        settings.picatPath = picatPathModule.parent.path

        // File that imports utils but also defines its own compute/1
        val text = """
            import utils.
            module local.

            compute(X) = X * 2.

            test() => compute(5).
        """.trimIndent()
        val file = myFixture.configureByText("local.pi", text)

        // Find the call to compute(5)
        val calls = PsiTreeUtil.findChildrenOfType(file, PicatFunctionCallSimple::class.java)
        val call = calls.firstOrNull { it.text.trim().startsWith("compute(5)") }
            ?: error("Call node for compute(5) not found")
        val ref = call.atom.references.firstOrNull() ?: error("No reference at compute(5)")
        val resolved = ref.resolve() ?: error("compute/1 did not resolve")

        // Should resolve to local definition, not PICATPATH
        val localDefOffset = text.indexOf("compute(X) = X * 2")
        assertTrue(
            "Expected resolution to local compute/1, not PICATPATH",
            resolved.textOffset <= localDefOffset + "compute".length
        )
    }

    @Test
    fun testEmptyPicatPathFallsBackToProjectRoots() {
        // Create module only in project lib/
        myFixture.tempDirFixture.createFile(
            "lib/fallback.pi",
            """
                module fallback.

                fallback_func(X).
            """.trimIndent()
        )

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        settings.picatPath = "" // Empty PICATPATH

        val file = myFixture.configureByText(
            "test_fallback.pi",
            """
            import fallback.
            module test_fallback.

            run() => fallback_func(1).
            """.trimIndent()
        )

        val ref = file.findReferenceAt(file.text.indexOf("fallback_func(1)"))
            ?: error("No reference at fallback_func(1)")
        val resolved = ref.resolve() ?: error("fallback_func/1 did not resolve from project roots")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to lib/fallback.pi but got: $path",
            path.contains("fallback.pi")
        )
    }

    @Test
    fun testResolveFromRelativePicatPath() {
        // Create a module in a subdirectory (relative to project root)
        myFixture.tempDirFixture.createFile(
            "modules/relmod.pi",
            """
                module relmod.

                relative_func(X).
            """.trimIndent()
        )

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        // Use relative path instead of absolute path
        settings.picatPath = "modules"

        val file = myFixture.configureByText(
            "use_relative.pi",
            """
            import relmod.
            module use_relative.

            run() => relative_func(42).
            """.trimIndent()
        )

        val ref = file.findReferenceAt(file.text.indexOf("relative_func(42)"))
            ?: error("No reference at relative_func(42)")
        val resolved = ref.resolve() ?: error("relative_func/1 did not resolve from relative PICATPATH")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to modules/relmod.pi but got: $path",
            path.contains("modules") && path.contains("relmod.pi")
        )
    }

    @Test
    fun testMixedAbsoluteAndRelativePicatPaths() {
        // Create modules in two directories - one for absolute path, one for relative
        val absModule = myFixture.tempDirFixture.createFile(
            "absolute-dir/absmod.pi",
            """
                module absmod.

                abs_func(X).
            """.trimIndent()
        )
        myFixture.tempDirFixture.createFile(
            "relative-dir/relmod.pi",
            """
                module relmod.

                rel_func(X).
            """.trimIndent()
        )

        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = ""
        // Mix absolute and relative paths
        val absolutePath = absModule.parent.path
        settings.picatPath = "$absolutePath${java.io.File.pathSeparatorChar}relative-dir"

        val file = myFixture.configureByText(
            "mixed_paths.pi",
            """
            import absmod.
            import relmod.
            module mixed_paths.

            test1() => abs_func(1).
            test2() => rel_func(2).
            """.trimIndent()
        )

        // Verify abs_func resolves (from absolute path)
        val refAbs = file.findReferenceAt(file.text.indexOf("abs_func(1)"))
            ?: error("No reference at abs_func(1)")
        val resolvedAbs = refAbs.resolve() ?: error("abs_func/1 did not resolve")
        assertTrue(
            "abs_func should resolve to absmod.pi",
            resolvedAbs.containingFile?.virtualFile?.path?.contains("absmod.pi") == true
        )

        // Verify rel_func resolves (from relative path)
        val refRel = file.findReferenceAt(file.text.indexOf("rel_func(2)"))
            ?: error("No reference at rel_func(2)")
        val resolvedRel = refRel.resolve() ?: error("rel_func/1 did not resolve")
        assertTrue(
            "rel_func should resolve to relmod.pi",
            resolvedRel.containingFile?.virtualFile?.path?.contains("relmod.pi") == true
        )
    }
}
