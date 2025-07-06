package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for parsing specific elements in the Picat language.
 * This test verifies that the parser correctly builds PSI elements for various Picat language constructs.
 */
class PicatElementParserTest : BasePlatformTestCase() {

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

        // Verify that atoms are parsed correctly
        val facts = file.getPredicateFacts()
        val actualSize = facts.size
        Assertions.assertEquals(4, actualSize, "Should have four facts. Actual: $actualSize")
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

        // Verify that structures are parsed correctly
        val facts = file.getPredicateFacts()
        Assertions.assertEquals(4, facts.size, "Should have four facts")

        // Check that the facts have heads
        facts.forEach { fact ->
            Assertions.assertNotNull(fact.head, "Fact should have a head")
        }
    }

    @Test
    fun testRuleParsing() {
        // Test parsing a simple rule
        val code = """
            factorial(0) => 1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a head and body
        val rule = rules[0]
        Assertions.assertNotNull(rule.getHead(), "Rule should have a head")
        Assertions.assertNotNull(rule.getBody(), "Rule should have a body")

        // Check that the rule contains the correct operator
        Assertions.assertTrue(rule.text.contains("=>"), "Rule should contain => operator")
    }

    @Test
    fun testBacktrackableRuleParsing() {
        // Test parsing a backtrackable rule
        val code = """
            fibonacci(0) ?=> 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a head and body
        val rule = rules[0]
        Assertions.assertNotNull(rule.getHead(), "Rule should have a head")
        Assertions.assertNotNull(rule.getBody(), "Rule should have a body")

        // Check that the rule contains the correct operator
        Assertions.assertTrue(rule.text.contains("?=>"), "Rule should contain ?=> operator")
    }

    @Test
    fun testPrologRuleParsing() {
        // Test parsing a rule with traditional Prolog operator
        val code = """
            ancestor(X, Y) :- parent(X, Y).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a head and body
        val rule = rules[0]
        Assertions.assertNotNull(rule.getHead(), "Rule should have a head")
        Assertions.assertNotNull(rule.getBody(), "Rule should have a body")

        // Check that the rule contains the correct operator
        Assertions.assertTrue(rule.text.contains(":-"), "Rule should contain :- operator")
    }

    @Test
    fun testExpressionParsing() {
        // Test parsing a simple expression
        val code = """
            test_expression =>
                X = 1 + 2 * 3.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        Assertions.assertNotNull(body, "Rule should have a body")
    }

    @Test
    fun testListParsing() {
        // Test parsing lists
        val code = """
            test_list =>
                L1 = [],
                L2 = [1, 2, 3],
                L3 = [a, b, c],
                L4 = [1 | [2, 3]].
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        Assertions.assertNotNull(body, "Rule should have a body")
    }

    @Test
    fun testMapParsing() {
        // Test parsing maps
        val code = """
            test_map =>
                M1 = {},
                M2 = {a:1, b:2, c:3},
                M3 = {1:"one", 2:"two", 3:"three"}.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the rule is parsed correctly
        val rules = file.getRules()

        Assertions.assertEquals(1, rules.size, "Should have one rule")

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        Assertions.assertNotNull(body, "Rule should have a body")
    }
}
