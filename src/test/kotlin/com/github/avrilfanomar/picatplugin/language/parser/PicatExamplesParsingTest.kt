package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile // Keep specific import for PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.* // Add wildcard import for PSI interfaces
// Remove specific Impl imports as they will be covered by wildcard or not needed when using interfaces
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

private const val KAKURO_PROGRAM = """
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

        """

private const val KNIGHT_TOUR_PROGRAM = """

            import cp.

            main =>
                asp([""" + "$" + """size(8)]).

            asp(As),
                once(member(""" + "$" + """size(N),As)),
                fillGivenMoves(As,Vars,N),
                M is N*N,
                Vars = new_list(M), 
                computeDomain(Vars,1,N),
                circuit(Vars),  % built-in in sat
                solve([ff],Vars)
            =>
                Vect = to_array(Vars),
                output(Vect,1,0,N,M),
                printf("%nANSWER SET FOUND%n").
            asp(_) =>
                printf("INCONSISTENT%n").

            fillGivenMoves([],_,_) => true.
            fillGivenMoves([givenMove(X1,Y1,X2,Y2)|As],Vars,N) =>
                I1 is (X1-1)*N+Y1,
                I2 is (X2-1)*N+Y2,
                nth(I1,Vars,I2),
                fillGivenMoves(As,Vars,N).
            fillGivenMoves([_|As],Vars,N) =>
                fillGivenMoves(As,Vars,N).    

            computeDomain([],_I,_N) => true.
            computeDomain([V|Vs],I,N) =>
                encode(Row,Col,I,N),
                feasiblePositions(Row,Col,D,N),
                V :: D,
                I1 is I+1,
                computeDomain(Vs,I1,N).

            feasiblePositions(R,C,D,N) =>
                R1 is R+1,  C1 is C+2,
                R2 is R+1,  C2 is C-2,
                R3 is R-1,  C3 is C+2,
                R4 is R-1,  C4 is C-2,
                R5 is R+2,  C5 is C+1,
                R6 is R+2,  C6 is C-1,
                R7 is R-2,  C7 is C+1,
                R8 is R-2,  C8 is C-1,
                addFeasiblePositions([(R1,C1),(R2,C2),(R3,C3),(R4,C4),(R5,C5),(R6,C6),(R7,C7),(R8,C8)],D,N).

            addFeasiblePositions([],D,_N) => D=[].
            addFeasiblePositions([(R,C)|Ps],D,N),
                (R>=1,R=<N,C>=1,C=<N) 
            =>
                encode(R,C,P,N),
                D=[P|D1],
                addFeasiblePositions(Ps,D1,N).
            addFeasiblePositions([_|Ps],D,N) =>
                addFeasiblePositions(Ps,D,N).

            encode(Row,Col,P,N),integer(P) =>
                Row is (P-1)//N+1,
                Col is (P-1) mod N+1.
            encode(Row,Col,P,N) =>
                P is (Row-1)*N+Col.

            output(_Vect,_I,Count,_N,M),Count>=M => true.
            output(Vect,I,Count,N,M) =>
                NI = Vect[I],
                output_move(I,NI,N),
                Count1 is Count+1,
                output(Vect,NI,Count1,N,M).

            output_move(P1,P2,N) =>
                encode(R1,C1,P1,N),
                encode(R2,C2,P2,N),
                print(""" + "$" + """move(R1,C1,R2,C2)),print('. ').
        """

private const val B_QUEENS_PROGRAM = """
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

        """

private const val LOOPS_PROGRAM = """
        import util.

        main => go.

        go =>

            % While loop example
            J = 1,
            while(J <= 10)
                println(J),
                J := J + 1
            end,

            % Foreach loop example
            foreach(K in 1..10)
                println(K)
            end.
        """

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatExamplesParsingTest : BasePlatformTestCase() {

    @Test
    fun testBQueens() {
        myFixture.configureByText("test.pi", B_QUEENS_PROGRAM.trimIndent())
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java) // Use interface
        assertEquals(1, importStatements.size)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)

        // Verify queens rule
        val queensRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("queens") }
        assertNotNull("Queens rule should exist", queensRule)

        // Get the body of the queens rule
        val queensBody = queensRule!!.getBody()
        assertNotNull("Queens rule should have a body", queensBody)
        val bodyText = queensBody!!.text
        assertTrue("Queens rule body should contain foreach", bodyText.contains("foreach"))
        assertTrue("Queens rule body should contain solve", bodyText.contains("solve"))

        // Verify foreach loops
        val foreachLoops = PsiTreeUtil.findChildrenOfType(queensBody, PicatForeachLoop::class.java) // Use interface
        assertNotNull("Foreach loops should exist", foreachLoops)
        assertEquals("Should have 5 foreach loops", 5, foreachLoops.size)

    }

    @Test
    fun testKakuro() {
        myFixture.configureByText("test.pi", KAKURO_PROGRAM.trimIndent())
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java) // Use interface
        assertEquals(1, importStatements.size)
        assertEquals("import cp.", importStatements.first().text)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)

        // Verify the go rule
        val goRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("go") }
        assertNotNull("Go rule should exist", goRule)
        assertNotNull("Go rule should have a body", goRule?.getBody())

        // Verify the problem rule
        val problemRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("problem") }
        assertNotNull("Problem rule should exist", problemRule)
        assertNotNull("Problem rule should have a body", problemRule?.getBody())
    }

    @Test
    fun testKnightTour() {
        myFixture.configureByText("test.pi", KNIGHT_TOUR_PROGRAM.trimIndent())
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java) // Use interface
        assertEquals(1, importStatements.size)
        assertEquals("import cp.", importStatements.first().text)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)
        assertNotNull("Main rule should have a body", mainRule?.getBody())

        // Verify asp rules
        val aspRules = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .filter { it.text.startsWith("asp") }
        assertTrue("Should have asp rules", aspRules.isNotEmpty())
        aspRules.forEach { rule ->
            assertNotNull("Asp rule should have a body", rule.getBody())
        }

        // Verify helper functions
        val helperRules = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .filter { rule ->
                listOf(
                    "fillGivenMoves", "computeDomain", "feasiblePositions",
                    "addFeasiblePositions", "encode", "output", "output_move"
                )
                    .any { rule.text.startsWith(it) }
            }
        assertTrue("Should have helper functions", helperRules.isNotEmpty())
        helperRules.forEach { rule ->
            assertNotNull("Helper rule should have a body", rule.getBody())
        }
    }

    @Test
    fun testLoops() {
        myFixture.configureByText("test.pi", LOOPS_PROGRAM.trimIndent())
        val file = myFixture.file as PicatFile

        // Verify import statement
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java) // Use interface
        assertEquals(1, importStatements.size)
        assertEquals("import util.", importStatements.first().text)

        // Verify the main rule
        val mainRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("main") }
        assertNotNull("Main rule should exist", mainRule)

        // Verify the go rule
        val goRule = PsiTreeUtil.findChildrenOfType(file, PicatRule::class.java) // Use interface
            .find { it.text.startsWith("go") }
        assertNotNull("Go rule should exist", goRule)

        // Get the body of the go rule
        val goBody = goRule!!.getBody()
        assertNotNull("Go rule should have a body", goBody)

        // Verify while loops
        val whileLoops = PsiTreeUtil.findChildrenOfType(goBody, PicatWhileLoop::class.java) // Use interface
        assertNotNull("While loops should exist", whileLoops)
        assertEquals("Should have 1 while loop", 1, whileLoops.size)

        // Verify foreach loops
        val foreachLoops = PsiTreeUtil.findChildrenOfType(goBody, PicatForeachLoop::class.java) // Use interface
        assertNotNull("Foreach loops should exist", foreachLoops)
        assertEquals("Should have 1 foreach loop", 1, foreachLoops.size)
    }
}
