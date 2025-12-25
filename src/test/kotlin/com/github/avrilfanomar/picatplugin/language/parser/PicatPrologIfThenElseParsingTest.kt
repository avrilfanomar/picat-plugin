package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatPrologIfThenElseParsingTest : BasePlatformTestCase() {

    @Test
    fun testSimplePrologIfThenElse() {
        val code = """
            test(N) = F =>
                (N == 0 -> F = 1 ; F = 2).
        """.trimIndent()
        myFixture.configureByText("test_prolog_if.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] SIMPLE_PROLOG_IF PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "simple Prolog if-then-else")
    }

    @Test
    fun testChainedPrologIfThenElse() {
        val code = """
            fibg(N) = F =>
                (N == 0 ->
                    F = 1
                ;N == 1 ->
                    F = 1
                ;
                    F = fibg(N-1)+fibg(N-2)
                ).
        """.trimIndent()
        myFixture.configureByText("fibg.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] CHAINED_PROLOG_IF PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "chained Prolog if-then-else (fibg)")
    }

    @Test
    fun testPrologIfThenElseWithoutElse() {
        val code = """
            test(N) = F =>
                (N == 0 -> F = 1).
        """.trimIndent()
        myFixture.configureByText("test_no_else.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] PROLOG_IF_NO_ELSE PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "Prolog if-then without else")
    }

    @Test
    fun testNestedPrologIfThenElse() {
        val code = """
            test(N, M) = F =>
                (N == 0 ->
                    (M == 0 -> F = 1 ; F = 2)
                ;
                    F = 3
                ).
        """.trimIndent()
        myFixture.configureByText("test_nested.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] NESTED_PROLOG_IF PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "nested Prolog if-then-else")
    }
}
