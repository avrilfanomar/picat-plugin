package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatCharLiteralParsingTest : BasePlatformTestCase() {

    @Test
    fun testCharacterLiteralInExpression() {
        val code = """
            test => X = 0'a + 1.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "character literal in expression")
    }

    @Test
    fun testCharacterLiteralComparison() {
        val code = """
            is_digit(C) => C >= 0'0, C =< 0'9.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "character literal comparison")
    }

    @Test
    fun testCharacterLiteralEscaped() {
        val code = """
            test => X = 0'\n, Y = 0'\t, Z = 0'\\.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "escaped character literal")
    }

    @Test
    fun testCharacterLiteralSpace() {
        val code = """
            test => X = 0' .
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "space character literal")
    }

    @Test
    fun testRegularIntegersStillWork() {
        val code = """
            test => X = 42, Y = 0xFF, Z = 0b1010, W = 0o77.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "regular integers")
    }
}
