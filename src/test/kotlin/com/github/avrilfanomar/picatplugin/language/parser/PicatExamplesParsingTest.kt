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

    @Test
    fun testKakuro() {
        val code = """
            import cp.

            main => go.

            go => 

              problem(P, N, Hints, Blanks),
              writef("Kakuro problem %d\n",P),

              X = new_array(N,N),
              X :: 0..9, 

              % Fill the blanks

              foreach([RR,CC] in Blanks)  X[RR,CC] #= 0 end,


              % The hints
              foreach([Sum|List] in Hints)
                 XLine = [X[R,C] : [R,C] in List, X[R,C] #> 0],
                 sum(XLine) #= Sum,
                 all_different(XLine)
              end,

              solve(X),

              foreach(Row in X)
                 foreach(R in Row) 
                    if R > 0 then
                       writef("%2d",R) 
                    else
                       writef(" _")
                    end
                 end,
                 nl
              end,
              nl.


            %
            % This is the problem cited above.
            %
            % problem(Id, Size, Hints, Blanks).
            problem(Id,Size, Hints, Blanks) =>
              Id = 1,
              Size = 7,
                 % [Sum, [List of indices in X]]
              Hints =  [ 
                  [16, [1,1],[1,2]],
                  [24, [1,5],[1,6],[1,7]],
                  [17, [2,1],[2,2]],
                  [29, [2,4],[2,5],[2,6],[2,7]],
                  [35, [3,1],[3,2],[3,3],[3,4],[3,5]],
                  [ 7, [4,2],[4,3]],
                  [ 8, [4,5],[4,6]],
                  [16, [5,3],[5,4],[5,5],[5,6],[5,7]],
                  [21, [6,1],[6,2],[6,3],[6,4]],
                  [ 5, [6,6],[6,7]],
                  [ 6, [7,1],[7,2],[7,3]],
                  [ 3, [7,6],[7,7]],
                  
                  [23, [1,1],[2,1],[3,1]],
                  [30, [1,2],[2,2],[3,2],[4,2]],
                  [27, [1,5],[2,5],[3,5],[4,5],[5,5]],
                  [12, [1,6],[2,6]],
                  [16, [1,7],[2,7]],
                  [17, [2,4],[3,4]],   
                  [15, [3,3],[4,3],[5,3],[6,3],[7,3]],
                  [12, [4,6],[5,6],[6,6],[7,6]],
                  [ 7, [5,4],[6,4]],   
                  [ 7, [5,7],[6,7],[7,7]],
                  [11, [6,1],[7,1]],
                  [10, [6,2],[7,2]]
              ],

               % indices of blanks
               Blanks = 
                  [
                   [1,3], [1,4],
                   [2,3],
                   [3,6], [3,7],
                   [4,1], [4,4],[4,7],
                   [5,1], [5,2],
                   [6,5],
                   [7,4], [7,5]
               ].

        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatementImpl::class.java)
        assertEquals(1, importStatements.size)
        assertEquals("import cp.", importStatements.first().text)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRuleImpl::class.java)
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)

        // Verify the go rule
        val goRule = PsiTreeUtil.findChildrenOfType(file, PicatRuleImpl::class.java)
            .find { it.text.startsWith("go") }
        assertNotNull("Go rule should exist", goRule)
        assertNotNull("Go rule should have a body", goRule?.getBody())

        // Verify the problem rule
        val problemRule = PsiTreeUtil.findChildrenOfType(file, PicatRuleImpl::class.java)
            .find { it.text.startsWith("problem") }
        assertNotNull("Problem rule should exist", problemRule)
        assertNotNull("Problem rule should have a body", problemRule?.getBody())
    }

}
