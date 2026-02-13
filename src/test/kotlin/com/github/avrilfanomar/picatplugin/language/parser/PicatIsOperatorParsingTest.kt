package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatIsOperatorParsingTest : BasePlatformTestCase() {

    @Test
    fun testSimpleIsExpression() {
        val code = """
            test(N) => M is N*N.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "simple is expression")
    }

    @Test
    fun testIsExpressionInBody() {
        val code = """
            compute(N) =>
                I1 is N+1,
                I2 is I1*2,
                println(I2).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "is expression in body")
    }

    @Test
    fun testIsExpressionWithParentheses() {
        val code = """
            encode(Row, Col, P, N) =>
                P is (Row-1)*N+Col.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "is expression with parentheses")
    }

    @Test
    fun testIsAsAtomInFunctionName() {
        val code = """
            is_sorted([]) => true.
            is_sorted([_]) => true.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "is as prefix in function name")
    }
}
