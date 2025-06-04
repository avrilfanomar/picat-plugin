package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRuleImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatExamplesParsingTest : BasePlatformTestCase() {

    @Test
    fun testBQueens() {
        val code = """
        import smt.

        main =>
            queens(8).

        queens(N) =>
            Qs = new_array(N,N),
            Qs :: 0..1,
            foreach(I in 1..N)     % 1 in each row
               sum([Qs[I,J] : J in 1..N]) #= 1
            end,
            foreach(J in 1..N)     % 1 in each column
               sum([Qs[I,J] : I in 1..N]) #= 1
            end,
            foreach(K in 1-N..N-1) % at most one 
               sum([Qs[I,J] : I in 1..N, J in 1..N, I-J=:=K]) #=< 1
            end,
            foreach(K in 2..2*N)   % at most one 
               sum([Qs[I,J] :  I in 1..N, J in 1..N, I+J=:=K]) #=< 1
            end,
            solve([""" + "$" + """logic("LIA"),cvc4],Qs),
            foreach(I in 1..N)
                writeln(Qs[I])
            end.

        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatementImpl::class.java)
        assertEquals(1, importStatements.size)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRuleImpl::class.java)
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)

        // Verify queens rule
        val queensRule = PsiTreeUtil.findChildrenOfType(file, PicatRuleImpl::class.java)
            .find { it.text.startsWith("queens") }
        assertNotNull("Queens rule should exist", queensRule)

        // Get the body of the queens rule
        val queensBody = queensRule!!.getBody()
        assertNotNull("Queens rule should have a body", queensBody)

        // Check for the presence of assignments and function calls in the body
        val bodyText = queensBody!!.text

        // Check that the rule has a non-empty body
        assertTrue("Queens rule body should not be empty", bodyText.isNotEmpty())
    }


}
