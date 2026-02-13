package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatConstraintBooleanParsingTest : BasePlatformTestCase() {

    @Test
    fun testConstraintAnd() {
        val code = """
            import cp.
            test(X, Y) => X #> 0 #/\ Y #> 0.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "constraint AND expression")
    }

    @Test
    fun testConstraintAndChained() {
        val code = """
            import cp.
            test(X, Y, Z) => X #> 0 #/\ Y #> 0 #/\ Z #> 0.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "chained constraint AND")
    }

    @Test
    fun testConstraintNotBackslash() {
        val code = """
            import cp.
            test(X) => #\ (X #> 0).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "constraint NOT (backslash)")
    }

    @Test
    fun testMixedConstraintBooleans() {
        val code = """
            import cp.
            test(X, Y, Z) => (X #> 0 #/\ Y #> 0) #\/ Z #> 0.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "mixed constraint boolean operators")
    }

    @Test
    fun testConstraintNotTildeStillWorks() {
        val code = """
            import cp.
            bcolor(NV, NC) =>
                A = new_array(NV, NC),
                A :: [0, 1],
                foreach(K in 1..NC)
                    #~ A[1, K] #\/ #~ A[2, K]
                end.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "constraint NOT tilde still works")
    }

    @Test
    fun testConstraintOrStillWorks() {
        val code = """
            import cp.
            test(X, Y) => X #!= 1 #\/ Y #!= 1.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "constraint OR still works")
    }
}
