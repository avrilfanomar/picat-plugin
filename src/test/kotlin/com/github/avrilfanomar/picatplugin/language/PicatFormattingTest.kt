package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.formatter.PicatFormatterService
import com.github.avrilfanomar.picatplugin.language.formatter.PicatFormattingModelBuilder
import com.intellij.lang.LanguageFormatting
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
     */
    private fun doFormatTest(code: String, expected: String) {
        // Normalize the input code by removing leading whitespace
        val normalizedCode = code.trim()

        // Get the custom formatter from the service
        val formatterService = service<PicatFormatterService>()
        val customFormatter = formatterService.getFormatter()

        // Format the code using our custom formatter
        val formattedText = customFormatter.format(normalizedCode)

        // Debug: Print the expected and actual outputs as byte arrays to see the exact differences
        println("[DEBUG_LOG] Expected bytes: ${expected.toByteArray().joinToString()}")
        println("[DEBUG_LOG] Actual bytes: ${formattedText.toByteArray().joinToString()}")

        // Debug: Print the expected and actual outputs with visible newlines
        println("[DEBUG_LOG] Expected with visible newlines: ${expected.replace("\n", "\\n")}")
        println("[DEBUG_LOG] Actual with visible newlines: ${formattedText.replace("\n", "\\n")}")

        // Normalize line endings to ensure consistent comparison
        val normalizedExpected = expected.replace("\r\n", "\n")
        val normalizedFormatted = formattedText.replace("\r\n", "\n")

        // Compare the formatted text with the expected output
        Assertions.assertEquals(normalizedExpected, normalizedFormatted, "Formatting should match expected output")
    }

    @Test
    fun testFormattingModelBuilderRegistration() {
        // Verify that a FormattingModelBuilder is registered for Picat language
        val formattingModelBuilder = LanguageFormatting.INSTANCE.forLanguage(PicatLanguage)

        // Assert that a FormattingModelBuilder is registered
        Assertions.assertNotNull(
            formattingModelBuilder,
            "FormattingModelBuilder should be registered for Picat language"
        )

        // Assert that it's an instance of PicatFormattingModelBuilder
        Assertions.assertTrue(
            formattingModelBuilder is PicatFormattingModelBuilder,
            "FormattingModelBuilder should be an instance of PicatFormattingModelBuilder"
        )
    }

    @Test
    fun testOperatorSpacing() {
        // Test that spaces are added around operators
        val code = """
main=>X=10,Y=20,Z=X+Y*2,println(Z).
        """

        val expected = """
main =>
    X = 10, Y = 20, Z = X + Y * 2, println(Z).
        """.trim()

        doFormatTest(code, expected)
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
        """

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
        """.trim()

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
        """

        val expected = """
% This is a comment
main =>
    % This is another comment
    X = 10, % This is an inline comment
    println(X).
        """.trim()

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
        """

        val expected = """
main =>

    X = 10,

    Y = 20,
    Z = X + Y,
    println(Z).
        """.trim()

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
        """

        val expected = """
% Predicate definition with =>
factorial(0) = 1.
factorial(N) = N * factorial(N - 1) => N > 0.

% Predicate with multiple clauses
fib(0) = 0.
fib(1) = 1.
fib(N) = fib(N - 1) + fib(N - 2) => N > 1.
        """.trim()

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
        """

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
        """.trim()

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
        """

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
        """.trim()

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
        """

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
        """.trim()

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
        """

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
        """.trim()

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
        """

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
        """.trim()

        doFormatTest(code, expected)
    }

    @Test
    fun testSimpleFormatting() {
        // A very simple test to see what the formatter does
        val code = "main=>X=10,Y=20."
        val expected = "main => X = 10, Y = 20."

        // Format the code using PicatCustomFormatter
        val formatterService = service<PicatFormatterService>()
        val customFormatter = formatterService.getFormatter()
        val formattedText = customFormatter.format(code)

        // Assert that the formatted code matches the expected output
        Assertions.assertEquals(expected, formattedText, "Formatting should match expected output")

        // Also test using the CodeStyleManager to ensure it's consistent
        myFixture.configureByText("simple.pi", code)
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val textRange = file.textRange
            val codeStyleManager = com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
            codeStyleManager.reformatText(file, textRange.startOffset, textRange.endOffset)
        }

        // Get the formatted text
        val formatted = myFixture.editor.document.text

        // Assert that the formatted code matches the expected output
        Assertions.assertEquals(expected, formatted, "Formatting with CodeStyleManager should match expected output")

        // Assert that both formatting methods produce the same result
        Assertions.assertEquals(formattedText, formatted, "Both formatting methods should produce the same result")
    }

    @Test
    fun testConstraintRuleOperatorFormatting() {
        // Test formatting of constraint rule operators (#=>, #<=>)
        val code = """
        % Constraint rule operators example
        constraint_rule_example=>
        % Constraint implication
        X#=>Y,
        % Constraint equivalence
        A#<=>B,
        println("Done").
        """

        val expected = """
% Constraint rule operators example
constraint_rule_example =>
    % Constraint implication
    X #=> Y,
    % Constraint equivalence
    A #<=> B,
    println("Done").
        """.trim()

        doFormatTest(code, expected)
    }

    @Test
    fun testComplexSecondReformatting() {
        // Test that formatting already formatted code doesn't change it
        // This verifies that formatting is idempotent for more complex code

        // Start with a more complex piece of code
        val code = """
            % Complex code example
            complex_example=>
            X=10,Y=20,Z=X+Y*2,
            if X>5 then
            println("X is greater than 5")
            else
            println("X is not greater than 5")
            end,
            foreach(I in 1..3)
            println(I)
            end.
        """

        // Format it once
        myFixture.configureByText("complex.pi", code)
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val textRange = file.textRange
            val codeStyleManager = com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
            codeStyleManager.reformatText(file, textRange.startOffset, textRange.endOffset)
        }

        // Get the formatted text
        val formattedOnce = myFixture.editor.document.text

        // Format it again
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val textRange = file.textRange
            val codeStyleManager = com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
            codeStyleManager.reformatText(file, textRange.startOffset, textRange.endOffset)
        }

        // Get the text after second formatting
        val formattedTwice = myFixture.editor.document.text

        Assertions.assertEquals(formattedOnce, formattedTwice)
    }

    @Test
    fun testListComprehensionFormatting() {
        // Test formatting of list comprehensions
        val code = """
            % List comprehension example
            list_comprehension_example=>
            % Simple list comprehension
            L1=[X:X in 1..10],
            % List comprehension with condition
            L2=[X:X in 1..10,X mod 2==0],
            % Nested list comprehension
            L3=[[X,Y]:X in 1..3,Y in 1..3,X!=Y],
            println(L1),println(L2),println(L3).
        """

        val expected = """
% List comprehension example
list_comprehension_example =>
    % Simple list comprehension
    L1 = [X : X in 1..10],
    % List comprehension with condition
    L2 = [X : X in 1..10, X mod 2 == 0],
    % Nested list comprehension
    L3 = [[X, Y] : X in 1..3, Y in 1..3, X != Y],
    println(L1), println(L2), println(L3).
        """.trim()

        doFormatTest(code, expected)
    }

    @Test
    fun testComplexFunctionCallFormatting() {
        // Test formatting of function calls with complex arguments
        val code = """
            % Complex function call example
            complex_function_call_example=>
            % Function call with multiple arguments
            result1=complex_function(10,20,30,40,50),
            % Function call with nested expressions
            result2=another_function(1+2*3,(4+5)*6,7*8+9),
            % Function call with list comprehension
            result3=process_list([X*2:X in 1..5]),
            % Function call with nested function calls
            result4=outer_function(inner_function1(10,20),inner_function2(30,40)),
            println(result1),println(result2),println(result3),println(result4).
        """

        val expected = """
% Complex function call example
complex_function_call_example =>
    % Function call with multiple arguments
    result1 = complex_function(10, 20, 30, 40, 50),
    % Function call with nested expressions
    result2 = another_function(1 + 2 * 3, (4 + 5) * 6, 7 * 8 + 9),
    % Function call with list comprehension
    result3 = process_list([X * 2 : X in 1..5]),
    % Function call with nested function calls
    result4 = outer_function(inner_function1(10, 20), inner_function2(30, 40)),
    println(result1), println(result2), println(result3), println(result4).
        """.trim()

        doFormatTest(code, expected)
    }

    @Test
    fun testCommaFollowedByClosingBracketFormatting() {
        // Test formatting of commas followed by closing brackets
        val code = """
            % Comma followed by closing bracket example
            comma_bracket_example=>
            % List with trailing comma
            L1=[1,2,3,],
            % Nested list with trailing commas
            L2=[[1,2,],[3,4,],],
            % Function call with trailing comma
            result=function(10,20,),
            println(L1),println(L2),println(result).
        """

        val expected = """
% Comma followed by closing bracket example
comma_bracket_example =>
    % List with trailing comma
    L1 = [1, 2, 3,],
    % Nested list with trailing commas
    L2 = [[1, 2,], [3, 4,],],
    % Function call with trailing comma
    result = function(10, 20,),
    println(L1), println(L2), println(result).
        """.trim()

        doFormatTest(code, expected)
    }
}
