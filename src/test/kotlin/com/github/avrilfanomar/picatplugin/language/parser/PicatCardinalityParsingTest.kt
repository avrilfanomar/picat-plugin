package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatCardinalityParsingTest : BasePlatformTestCase() {

    @Test
    fun testCardinalityAsFunction() {
        val code = """
            import cp.
            test(Xs) =>
                cardinality(Xs, [1-2, 2-3]).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "cardinality as function call")
    }

    @Test
    fun testCardinalityInBody() {
        val code = """
            import cp.
            solve(Xs) =>
                Xs = new_list(5),
                Xs :: 1..3,
                cardinality(Xs, [1-2, 2-1, 3-2]).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "cardinality in constraint body")
    }

    @Test
    fun testOtherKeywordsAsAtomStillWork() {
        val code = """
            test => X = once(foo(1,2)).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "once keyword as atom still works")
    }
}
