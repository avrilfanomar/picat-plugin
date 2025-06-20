package com.github.avrilfanomar.picatplugin.language.parser

// Keep existing imports
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatParserTest : BasePlatformTestCase() {

    @Test
    fun testEmptyFile() {
        // Test parsing an empty file
        val code = ""
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val psiString = DebugUtil.psiToString(file, false, true) // Set showChildren=false for the root, true for content
        // With picatFile ::= item_*, an empty file should result in PicatFileImpl having no children.
        Assertions.assertTrue(file.children.isEmpty(), "Empty file should have no children. PSI:\n$psiString")
    }

    @Test
    fun testFunctionWithComments() {
        // Test parsing comments
        val code = """
            % This is a line comment
            /* This is a
               multi-line comment */
            factorial(0) = 1. % Comment after code
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that comments are parsed correctly
        val functionDefinitions = file.getFunctions()
        Assertions.assertEquals(1, functionDefinitions.size, "Should have one function")
    }

    @Test
    fun testComplexExpression() {
        // Test parsing a complex expression
        val code = "main => println(1)."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val psiString = DebugUtil.psiToString(file, true, true)
        // Verify that the rule is parsed correctly
        val rules = PsiTreeUtil.collectElementsOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 1, "Should have at least one rule. PSI:\n$psiString")
    }

    @Test
    fun testPatternMatching() {
        // Test parsing pattern matching
        val code = """
            custom_length([]) = 0.
            custom_length([_|Xs]) = 1 + custom_length(Xs).

            custom_sum([]) = 0.
            custom_sum([X|Xs]) = X + custom_sum(Xs).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that functions are parsed correctly
        val functions = file.getFunctions()
        Assertions.assertTrue(functions.size >= 2, "Should have at least 2 functions")

        // Find the specific functions
        val customLengthFunctions = functions.filter { it.head.structure?.atom?.text == "custom_length" }
        val customSumFunctions = functions.filter { it.head.structure?.atom?.text == "custom_sum" }

        Assertions.assertTrue(customLengthFunctions.isNotEmpty(), "Should have at least one custom_length function")
        Assertions.assertTrue(customSumFunctions.isNotEmpty(), "Should have at least one custom_sum function")
    }

    @Test
    fun testErrorRecovery() {
        // Test that the parser can recover from syntax errors
        val code = """
            % Missing dot at the end of this fact
            factorial(0) = 1

            % This should still be parsed correctly
            factorial(N) = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        val psiString = DebugUtil.psiToString(file, true, true)

        val functionRules = PsiTreeUtil.collectElementsOfType(file, PicatFunctionRule::class.java)
        Assertions.assertEquals(2, functionRules.size, "Should find 2 PicatFunctionRuleImpl nodes. PSI:\n$psiString")
    }

    @Test
    fun testAtomParsing() {
        // Test parsing different types of atoms
        val code = """
            simple_atom.
            'quoted atom'.
            atom_with_underscore.
            atomWithMixedCase.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val facts = file.getPredicateFacts()
        Assertions.assertEquals(4, facts.size, "Should have four facts")

        facts.forEach { fact ->
            Assertions.assertNotNull(fact.getHead(), "Fact should have a head")
        }
    }

    @Test
    fun testStructureParsing() {
        // Test parsing structures (compound terms)
        val code = """
            point(1, 2).
            person('John', 30, male).
            nested(point(3, 4), circle(5)).
            empty().
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val facts = file.getPredicateFacts()
        Assertions.assertEquals(4, facts.size, "Should have four facts")

        facts.forEach { fact ->
            Assertions.assertNotNull(fact.getHead(), "Fact should have a head")
        }

        val functions = file.getFunctions()
        Assertions.assertEquals(0, functions.size, "Should have no functions")
    }

    @Test
    fun testRuleParsing() {
        // Test parsing different types of rules
        val code = """
            % Explicit rule with arrow operator
            square(X) = X * X => true.
            
            % Explicit rule with condition
            abs(X) = X => X >= 0.
            abs(X) = -X => X < 0.
            
            % Backtrackable rule
            color(red) ?=> true.
            color(blue) ?=> true.
            
            % Rule with traditional Prolog operator
            ancestor(X, Y) :- parent(X, Y).
            ancestor(X, Z) :- parent(X, Y), ancestor(Y, Z).

        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        val psiString = DebugUtil.psiToString(file, true, true)

        val rules = PsiTreeUtil.collectElementsOfType(file, PicatPredicateRule::class.java)
        Assertions.assertEquals(4, rules.size, "Expected 4 predicate rules (3 others are func defs). PSI:\n$psiString")

        if (rules.size == 4) { // This check might be redundant if the above assertEquals passes.
            val ruleTexts = rules.map { it.text }
            val allRuleText = ruleTexts.joinToString(" ")

            Assertions.assertTrue(allRuleText.contains("?=>"), "Should contain '?=>' operator for color rules")
            Assertions.assertTrue(allRuleText.contains(":-"), "Should contain ':-' operator for ancestor rules")
        }
    }

    @Test
    fun testFunctionParsing() {
        // Test parsing function definitions
        val code = """
            % Simple function
            square(X) = X * X.

            % Function with condition
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            % Function with complex expression
            distance(point(X1, Y1), point(X2, Y2)) = sqrt((X2 - X1)*(X2 - X1) + (Y2 - Y1)*(Y2 - Y1)).

            % Function with pattern matching
            custom_sum([]) = 0.
            custom_sum([H|T]) = H + custom_sum(T).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val functions = file.getFunctions()
        Assertions.assertTrue(functions.size >= 3, "Should have at least 3 functions")

        val functionNames = functions.mapNotNull { it.head.structure?.atom?.text }.distinct()
        Assertions.assertTrue(functionNames.size >= 3, "Should have at least 3 distinct function names")
        Assertions.assertTrue(functionNames.contains("square"), "Should contain 'square'")
        Assertions.assertTrue(functionNames.contains("factorial"), "Should contain 'factorial'")
        Assertions.assertTrue(functionNames.contains("custom_sum"), "Should contain 'custom_sum'")
    }

    @Test
    fun testExpressionParsing() {
        // Test parsing different types of expressions
        val code = """
            test_expressions =>
                % Arithmetic expressions
                A = 1 + 2 * 3,
                B = (1 + 2) * 3,
                C = 2 ** 3 + 4,

                % Relational expressions
                D = A > B,
                E = C <= 10,
                F = A == B,

                % Logical expressions
                G = A > 0 and B < 10,
                H = C > 10 or D == true,
                I = not G,

                % Ternary expression
                J = A > B ? "greater" : "less or equal",

                % List expressions
                K = [1, 2, 3],
                L = [A | K],

                % Map expressions
                M = {a:1, b:2, c:3},

                % Function calls
                N = factorial(5),
                O = length([1, 2, 3]),

                println(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.collectElementsOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 1, "Should have at least one rule")

        val testExpressionsRule = rules.find { it.getHead()?.text == "test_expressions" }
        Assertions.assertNotNull(testExpressionsRule, "Should have a rule with head 'test_expressions'")

        val body = testExpressionsRule?.getBody()
        Assertions.assertNotNull(body, "Rule should have a body")
    }

    @Test
    fun testPatternParsing() {
        // Test parsing different types of patterns
        val code = """
            % Variable patterns
            match(X, X) => true.

            % Atom patterns
            match(atom, atom) => true.

            % Structure patterns
            match(point(X, Y), point(X, Y)) => true.

            % List patterns
            match([], []) => true.
            match([H|T], [H|T]) => true.

            % Mixed patterns
            process(person(Name, Age, _), [Name, Age]).

            % Pattern in function definition
            custom_sum([]) = 0.
            custom_sum([H|T]) = H + custom_sum(T).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.collectElementsOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 5, "Should have multiple rules")

        val heads = rules.mapNotNull { it.getHead() }
        Assertions.assertEquals(rules.size, heads.size, "Should have the same number of heads as rules")

        val functions = file.getFunctions()
        Assertions.assertTrue(functions.size >= 2, "Should have at least 2 functions")
    }
}
