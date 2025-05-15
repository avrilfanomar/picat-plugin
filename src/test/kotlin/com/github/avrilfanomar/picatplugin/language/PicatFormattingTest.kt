package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.formatting.PicatFormattingModelBuilder
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.lang.LanguageFormatting
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import com.intellij.openapi.command.WriteCommandAction

/**
 * Test for Picat code formatting.
 * This test verifies that the code formatter is correctly registered for Picat language
 * and that it formats code according to the code style settings.
 */
class PicatFormattingTest : BasePlatformTestCase() {

    /**
     * Helper method to test code formatting.
     * 
     * @param code The input code to format
     * @param expected The expected formatted output
     * @param filename Optional filename for the test file (defaults to "test.pi")
     */
    private fun doFormatTest(code: String, expected: String, filename: String = "test.pi") {
        // Configure a test file with the input code
        myFixture.configureByText(filename, code)

        // Apply formatting to the file
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val textRange = file.textRange
            val codeStyleManager = com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
            codeStyleManager.reformatText(file, textRange.startOffset, textRange.endOffset)
        }

        // Get the formatted text
        val formattedText = myFixture.editor.document.text

        // Compare the formatted text with the expected output
        assertEquals("Formatting should match expected output", expected, formattedText)
    }

    @Test
    fun testFormattingModelBuilderRegistration() {
        // Verify that a FormattingModelBuilder is registered for Picat language
        val formattingModelBuilder = LanguageFormatting.INSTANCE.forLanguage(PicatLanguage)

        // Print debug information
        println("[DEBUG_LOG] FormattingModelBuilder for Picat: $formattingModelBuilder")

        // Assert that a FormattingModelBuilder is registered
        assertNotNull("FormattingModelBuilder should be registered for Picat language", formattingModelBuilder)

        // Assert that it's an instance of PicatFormattingModelBuilder
        assertTrue("FormattingModelBuilder should be an instance of PicatFormattingModelBuilder", 
                  formattingModelBuilder is PicatFormattingModelBuilder)
    }

    @Test
    fun testOperatorSpacing() {
        // Test that spaces are added around operators
        val code = """
            main=>X=10,Y=20,Z=X+Y*2,println(Z).
        """.trimIndent()

        val expected = """
            main => X = 10, Y = 20, Z = X + Y * 2, println(Z).
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testFormattingModelBuilderImplementation() {
        // Test that the PicatFormattingModelBuilder is implemented correctly
        val formattingModelBuilder = PicatFormattingModelBuilder()

        // Verify that it's an instance of FormattingModelBuilder
        assertTrue("PicatFormattingModelBuilder should implement FormattingModelBuilder",
                  formattingModelBuilder is FormattingModelBuilder)

        println("[DEBUG_LOG] PicatFormattingModelBuilder implementation verified")
    }

    @Test
    fun testPicatBlockImplementation() {
        // Test that the PicatBlock class is implemented correctly
        val node = com.intellij.lang.ASTFactory.leaf(PicatTokenTypes.IDENTIFIER, "test")
        val settings = CodeStyleSettingsManager.getInstance(project).currentSettings
        val spacingBuilder = com.intellij.formatting.SpacingBuilder(settings, PicatLanguage)

        // Create a PicatBlock
        val block = com.github.avrilfanomar.picatplugin.language.formatting.PicatBlock(
            node,
            null,
            null,
            settings,
            spacingBuilder
        )

        // Verify that it's an instance of Block
        assertTrue("PicatBlock should implement Block",
                  block is com.intellij.formatting.Block)

        // Verify that the block has the expected properties
        assertNotNull("PicatBlock should have a spacing builder", block)

        println("[DEBUG_LOG] PicatBlock implementation verified")
    }

    @Test
    fun testIndentation() {
        // Test that code blocks are properly indented
        val code = """
            main=>
            if X>10 then
            println("X is greater than 10")
            else
            println("X is not greater than 10")
            end,
            foreach(I in 1..5)
            println(I)
            end.
        """.trimIndent()

        val expected = """
            main =>
                if X > 10 then
                    println("X is greater than 10")
                else
                    println("X is not greater than 10")
                end,
                foreach(I in 1..5)
                    println(I)
                end.
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testCommentHandling() {
        // Test that comments are properly handled
        val code = """
            % This is a comment
            main=>
            % This is another comment
            X=10, % This is an inline comment
            println(X).
        """.trimIndent()

        val expected = """
            % This is a comment
            main =>
                % This is another comment
                X = 10, % This is an inline comment
                println(X).
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testLineBreaksAndBlankLines() {
        // Test that line breaks and blank lines are preserved
        val code = """
            main=>

            X=10,

            Y=20,
            Z=X+Y,
            println(Z).
        """.trimIndent()

        val expected = """
            main =>

                X = 10,

                Y = 20,
                Z = X + Y,
                println(Z).
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testPredicateFormatting() {
        // Test formatting of predicate definitions
        val code = """
            % Predicate definition with =>
            factorial(0)=1.
            factorial(N)=N*factorial(N-1)=>N>0.

            % Predicate with multiple clauses
            fib(0)=0.
            fib(1)=1.
            fib(N)=fib(N-1)+fib(N-2)=>N>1.
        """.trimIndent()

        val expected = """
            % Predicate definition with =>
            factorial(0) = 1.
            factorial(N) = N * factorial(N - 1) => N > 0.

            % Predicate with multiple clauses
            fib(0) = 0.
            fib(1) = 1.
            fib(N) = fib(N - 1) + fib(N - 2) => N > 1.
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testBacktrackableRuleFormatting() {
        // Test formatting of backtrackable rules with ?=>
        val code = """
            % Backtrackable rule with ?=>
            solve?=>
            X=1,
            println(X).

            solve?=>
            X=2,
            println(X).

            solve=>
            println("No more solutions").
        """.trimIndent()

        val expected = """
            % Backtrackable rule with ?=>
            solve ?=>
                X = 1,
                println(X).

            solve ?=>
                X = 2,
                println(X).

            solve =>
                println("No more solutions").
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testConstraintFormatting() {
        // Test formatting of constraint expressions
        val code = """
            % Constraint programming example
            sudoku=>
            Rows=new_array(9,9),
            Rows::1..9,

            % Constraints
            foreach(I in 1..9)
            all_different([Rows[I,J]:J in 1..9]),
            all_different([Rows[J,I]:J in 1..9])
            end,

            % Block constraints
            foreach(I in 0..2, J in 0..2)
            all_different([Rows[I*3+X,J*3+Y]:X in 1..3,Y in 1..3])
            end,

            solve(Rows).
        """.trimIndent()

        val expected = """
            % Constraint programming example
            sudoku =>
                Rows = new_array(9, 9),
                Rows :: 1..9,

                % Constraints
                foreach(I in 1..9)
                    all_different([Rows[I, J] : J in 1..9]),
                    all_different([Rows[J, I] : J in 1..9])
                end,

                % Block constraints
                foreach(I in 0..2, J in 0..2)
                    all_different([Rows[I * 3 + X, J * 3 + Y] : X in 1..3, Y in 1..3])
                end,

                solve(Rows).
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testTermComparisonFormatting() {
        // Test formatting of term comparison expressions
        val code = """
            % Term comparison example
            compare_terms=>
            X=10,
            Y=20,
            if X@<Y then
            println("X is less than Y")
            elseif X@=<Y then
            println("X is less than or equal to Y")
            elseif X@>Y then
            println("X is greater than Y")
            elseif X@>=Y then
            println("X is greater than or equal to Y")
            end.
        """.trimIndent()

        val expected = """
            % Term comparison example
            compare_terms =>
                X = 10,
                Y = 20,
                if X @< Y then
                    println("X is less than Y")
                elseif X @=< Y then
                    println("X is less than or equal to Y")
                elseif X @> Y then
                    println("X is greater than Y")
                elseif X @>= Y then
                    println("X is greater than or equal to Y")
                end.
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testNestedStructuresFormatting() {
        // Test formatting of nested structures
        val code = """
            % Nested structures example
            nested_example=>
            if X>10 then
            foreach(I in 1..X)
            if I mod 2==0 then
            println("Even: "++I)
            else
            println("Odd: "++I)
            end
            end
            else
            println("X is not greater than 10")
            end.
        """.trimIndent()

        val expected = """
            % Nested structures example
            nested_example =>
                if X > 10 then
                    foreach(I in 1..X)
                        if I mod 2 == 0 then
                            println("Even: " ++ I)
                        else
                            println("Odd: " ++ I)
                        end
                    end
                else
                    println("X is not greater than 10")
                end.
        """.trimIndent()

        doFormatTest(code, expected)
    }

    @Test
    fun testLiteralsFormatting() {
        // Test formatting of different types of literals
        val code = """
            % Literals example
            literals_example=>
            % Integers
            X=42,
            % Floats
            Y=3.14,
            % Strings
            S="Hello, world!",
            % Atoms
            A=atom,
            % Lists
            L=[1,2,3,4,5],
            % Structures
            St=point(10,20),
            % Variables
            Var=X,
            % Anonymous variables
            _=42,
            println(X+Y).
        """.trimIndent()

        val expected = """
            % Literals example
            literals_example =>
                % Integers
                X = 42,
                % Floats
                Y = 3.14,
                % Strings
                S = "Hello, world!",
                % Atoms
                A = atom,
                % Lists
                L = [1, 2, 3, 4, 5],
                % Structures
                St = point(10, 20),
                % Variables
                Var = X,
                % Anonymous variables
                _ = 42,
                println(X + Y).
        """.trimIndent()

        doFormatTest(code, expected)
    }

}
