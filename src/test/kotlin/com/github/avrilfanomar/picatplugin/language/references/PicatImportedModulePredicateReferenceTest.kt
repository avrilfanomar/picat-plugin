package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatImportedModulePredicateReferenceTest : BasePlatformTestCase() {

    @Test
    fun testResolvePredicateFromProjectSourceRoots() {
        // Arrange: module in project lib/ directory (no stdlib configured)
        // This tests the fallback mechanism when Picat executable is not set
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = "" // No stdlib configured

        // Create planner module in lib/ subdirectory
        myFixture.tempDirFixture.createFile(
            "lib/planner.pi",
            """
                module planner.
                
                plan(S, Plan).
                plan(S, Limit, Plan).
            """.trimIndent()
        )

        // Picat file that imports planner and uses plan/2
        val file = myFixture.configureByText(
            "main.pi",
            """
            import planner.
            module m.
            
            farmer() => p<caret>lan(state, result).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("plan(state")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at plan(state, result)")
        val resolved = ref.resolve() ?: error("plan/2 did not resolve from project source roots")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to lib/planner.pi but got: $path",
            path.endsWith("/lib/planner.pi") || path.endsWith("\\lib\\planner.pi")
        )
    }

    @Test
    fun testResolvePredicateFromImportedStdlibModule() {
        // Arrange: fake stdlib module cp.pi with foo/1 predicate
        val vFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/cp.pi",
            """
                module cp.
                
                foo(X).
            """.trimIndent()
        )
        val picatHomeVDir = vFile.parent.parent

        // Point settings to fake Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        // Picat file that imports cp and uses foo/1
        val file = myFixture.configureByText(
            "main.pi",
            """
            import cp.
            module m.
            
            main() => f<caret>oo(42).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("foo(42)")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at foo(42)")
        val resolved = ref.resolve() ?: error("foo/1 did not resolve")
        val path = resolved.containingFile?.virtualFile?.path ?: ""
        assertTrue(
            "Expected resolution to stdlib cp.pi but got: $path",
            path.endsWith("/lib/cp.pi") || path.endsWith("\\lib\\cp.pi")
        )
    }

    @Test
    fun testLocalDefinitionOverridesImported() {
        // Arrange: fake stdlib module lib/math.pi with max/2
        val vFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/math.pi",
            """
                module math.
                
                max(X,Y).
            """.trimIndent()
        )
        val picatHomeVDir = vFile.parent.parent
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        // File that imports math and defines its own max/2
        val text = """
            import math.
            module m.
            
            max(A,B).
            
            main() => max(1,2).
        """.trimIndent()
        val file = myFixture.configureByText("main.pi", text)
        val calls = com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(file, com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallSimple::class.java)
        val call = calls.firstOrNull { it.text.trim().startsWith("max(1,2)") } ?: error("Call node for max(1,2) not found")
        val ref = call.atom.references.firstOrNull() ?: error("No reference at max(1,2)")
        val resolved = ref.resolve() ?: error("max/2 did not resolve")
        // Should resolve to the local head, which appears before the call, not to stdlib
        val localHeadOffset = text.indexOf("max(A,B)")
        assertTrue("Expected resolution to local max/2", resolved.textOffset <= localHeadOffset + "max".length)
    }
}
