package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatMinimalFibiParsingTest : BasePlatformTestCase() {

    @Test
    fun testSimpleFunctionRuleParsing() {
        val code = """
            /* simple function */
            fibi(N) = F => F = 1.
        """.trimIndent()
        myFixture.configureByText("fibi_simple.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] SIMPLE_FUNCTION PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "simple function")
    }

    @Test
    fun testIfThenElseAsPredicateBody() {
        val code = """
            /* predicate with if */
            p(N) => if N == 0 then true else true end.
        """.trimIndent()
        myFixture.configureByText("pred_if.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] PRED_IF PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "predicate if")
    }
}
