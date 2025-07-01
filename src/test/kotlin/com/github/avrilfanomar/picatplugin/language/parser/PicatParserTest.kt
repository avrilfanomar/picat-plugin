package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatParserTest : BasePlatformTestCase() {

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

        val allRuleText = rules.joinToString(" ") { it.text }

        Assertions.assertTrue(allRuleText.contains("?=>"), "Should contain '?=>' operator for color rules")
        Assertions.assertTrue(allRuleText.contains(":-"), "Should contain ':-' operator for ancestor rules")
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

        val testExpressionsRule = rules.find { it.getHead().text == "test_expressions" }
        Assertions.assertNotNull(testExpressionsRule, "Should have a rule with head 'test_expressions'")

        val body = testExpressionsRule?.getBody()
        Assertions.assertNotNull(body, "Rule should have a body")
    }

}
