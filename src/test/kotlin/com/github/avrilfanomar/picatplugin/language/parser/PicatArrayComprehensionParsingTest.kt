package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatArrayExpr
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatArrayComprehensionParsingTest : BasePlatformTestCase() {

    @Test
    fun testSimpleArrayComprehension() {
        val code = """
            test => X = {I : I in 1..10}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "simple array comprehension")
    }

    @Test
    fun testArrayComprehensionWithCondition() {
        val code = """
            test(L) => X = {X*2 : X in L, X > 0}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "array comprehension with condition")
    }

    @Test
    fun testArrayComprehensionWithPattern() {
        val code = """
            test => X = {R+C : [R,C] in [[1,2],[3,4]]}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "array comprehension with pattern")
    }

    @Test
    fun testArrayComprehensionMultipleIterators() {
        val code = """
            test(N) => X = {I+J : I in 1..N, J in 1..N}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "array comprehension with multiple iterators")
    }

    @Test
    fun testOrdinaryArrayStillWorks() {
        val code = """
            test => X = {1,2,3}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "ordinary array literal")
        val arrays = PsiTreeUtil.findChildrenOfType(file, PicatArrayExpr::class.java)
        Assertions.assertTrue(arrays.isNotEmpty(), "Should find array expression")
    }

    @Test
    fun testEmptyArrayStillWorks() {
        val code = """
            test => X = {}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "empty array literal")
    }
}
