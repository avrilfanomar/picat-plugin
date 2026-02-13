package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatPrologOperatorsParsingTest : BasePlatformTestCase() {

    @Test
    fun testNotUnifiable() {
        val code = """
            test(X, Y) => X \= Y.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "not unifiable operator")
    }

    @Test
    fun testNotIdenticalProlog() {
        val code = """
            test(X, Y) => X \== Y.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "not identical prolog operator")
    }

    @Test
    fun testNotUnifiableInCondition() {
        val code = """
            unsafe([F, W, G, _C]), W == G, F \= W => true.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "not unifiable in condition")
    }

    @Test
    fun testNotIdenticalPrologInCondition() {
        val code = """
            unsafe([F, _W, G, C]), G == C, F \== G => true.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "not identical prolog in condition")
    }

    @Test
    fun testExistingOperatorsStillWork() {
        val code = """
            test(X, Y) => X !== Y, X != Y, X == Y.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "existing operators still work")
    }
}
