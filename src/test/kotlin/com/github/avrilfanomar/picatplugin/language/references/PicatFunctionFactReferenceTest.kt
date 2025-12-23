package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Tests that function facts (definitions with = sign) are properly resolved.
 * This addresses the issue where stdlib functions like new_list are not resolved
 * when they use function fact syntax: new_list(N) = new_list(N).
 */
class PicatFunctionFactReferenceTest : BasePlatformTestCase() {

    @Test
    fun testNewListFunctionFactResolution() {
        // Create a fake stdlib with basic.pi that includes new_list as function facts
        val basicFile = myFixture.tempDirFixture.createFile(
            "picat-home/lib/basic.pi",
            """
                module basic.

                new_list(N) = new_list(N).
                new_list(N, InitVal) = new_list(N,InitVal).
            """.trimIndent()
        )
        val picatHomeVDir = basicFile.parent.parent

        // Configure settings to point to fake Picat home
        val settings = PicatSettings.getInstance(project)
        settings.picatExecutablePath = picatHomeVDir.path

        val file = myFixture.configureByText(
            "test.pi",
            """
            main =>
                X = new_list(5).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("new_list")
        val ref = file.findReferenceAt(caretOffset) ?: error("No reference at new_list")
        val resolved = ref.resolve()
        val resolvedPath = resolved?.containingFile?.virtualFile?.path
            ?: error("Resolution path is null for new_list")
        assertTrue(
            "Expected resolution to stdlib basic.pi, but got: $resolvedPath",
            resolvedPath.endsWith("/lib/basic.pi") || resolvedPath.endsWith("\\lib\\basic.pi")
        )
    }

    @Test
    fun testResolveFunctionFactDefinitionSimple() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module m.

            % Function fact definition
            new_list(N) = new_list(N).

            % Usage in another function
            test_list(N) =>
                L = new_list(5).
            """.trimIndent()
        )
        val offset = file.text.indexOf("new_list(5)")
        val ref = file.findReferenceAt(offset) ?: error("No reference at new_list(5)")
        val resolved = ref.resolve()
        assertNotNull("Should resolve to new_list function fact", resolved)
    }

    @Test
    fun testResolveFunctionFactFibonacci() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module m.

            % Fibonacci function facts
            fibfa(0) = 1.
            fibfa(1) = 1.
            fibfa(N) = fibfa(N - 1) + fibfa(N - 2).
            """.trimIndent()
        )
        val offset = file.text.indexOf("fibfa(N - 1)")
        val ref = file.findReferenceAt(offset) ?: error("No reference at fibfa(N - 1)")
        val resolved = ref.resolve()
        assertNotNull("Should resolve to a fibfa definition", resolved)
    }

    @Test
    fun testMultipleArityFunctionFacts() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module m.

            % Different arities
            process() = "arity0".
            process(X) = "arity1".
            process(X, Y) = "arity2".

            test =>
                A = process(),
                B = process(1),
                C = process(1, 2).
            """.trimIndent()
        )

        // Check arity 0
        val offset0 = file.text.indexOf("process()")
        val ref0 = file.findReferenceAt(offset0) ?: error("No reference at process()")
        val resolved0 = ref0.resolve()
        assertNotNull("Should resolve process/0", resolved0)

        // Check arity 1
        val offset1 = file.text.indexOf("process(1)")
        val ref1 = file.findReferenceAt(offset1) ?: error("No reference at process(1)")
        val resolved1 = ref1.resolve()
        assertNotNull("Should resolve process/1", resolved1)

        // Check arity 2
        val offset2 = file.text.indexOf("process(1, 2)")
        val ref2 = file.findReferenceAt(offset2) ?: error("No reference at process(1, 2)")
        val resolved2 = ref2.resolve()
        assertNotNull("Should resolve process/2", resolved2)
    }

    @Test
    fun testFunctionFactVsPredicateFact() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module m.

            % Predicate fact
            is_true().

            % Function fact (same name, different arity)
            is_true(X) = X.

            test =>
                is_true(),
                Y = is_true(1).
            """.trimIndent()
        )

        // First call: is_true() -> arity 0 predicate fact
        val offset1 = file.text.indexOf("is_true()")
        val ref1 = file.findReferenceAt(offset1) ?: error("No reference at is_true()")
        val resolved1 = ref1.resolve()
        assertNotNull("Should resolve is_true/0", resolved1)

        // Second call: is_true(1) -> arity 1 function fact
        val offset2 = file.text.indexOf("is_true(1)")
        val ref2 = file.findReferenceAt(offset2) ?: error("No reference at is_true(1)")
        val resolved2 = ref2.resolve()
        assertNotNull("Should resolve is_true/1", resolved2)
    }
}
