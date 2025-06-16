package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile // Keep specific for clarity, wildcard will cover others
import com.github.avrilfanomar.picatplugin.language.psi.PicatFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.intellij.testFramework.fixtures.BasePlatformTestCase
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
        val file = myFixture.file as PicatFile

        // Verify that atoms are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have four facts", 4, facts.size)

        // Check that the facts have heads
        facts.forEach { fact ->
            assertNotNull("Fact should have a head", fact.getHead())
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
        val file = myFixture.file as PicatFile

        // Verify that structures are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have four facts", 4, facts.size)

        // Check that the facts have heads
        facts.forEach { fact ->
            assertNotNull("Fact should have a head", fact.getHead())
        }
    }

    @Test
    fun testRuleParsing() {
        // Test parsing a simple rule
        val code = """
            factorial(0) => 1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a head and body
        val rule = rules[0]
        assertNotNull("Rule should have a head", rule.getHead())
        assertNotNull("Rule should have a body", rule.getBody())

        // Check that the rule contains the correct operator
        assertTrue("Rule should contain => operator", rule.text.contains("=>"))
    }

    @Test
    fun testBacktrackableRuleParsing() {
        // Test parsing a backtrackable rule
        val code = """
            fibonacci(0) ?=> 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a head and body
        val rule = rules[0]
        assertNotNull("Rule should have a head", rule.getHead())
        assertNotNull("Rule should have a body", rule.getBody())

        // Check that the rule contains the correct operator
        assertTrue("Rule should contain ?=> operator", rule.text.contains("?=>"))
    }

    @Test
    fun testPrologRuleParsing() {
        // Test parsing a rule with traditional Prolog operator
        val code = """
            ancestor(X, Y) :- parent(X, Y).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a head and body
        val rule = rules[0]
        assertNotNull("Rule should have a head", rule.getHead())
        assertNotNull("Rule should have a body", rule.getBody())

        // Check that the rule contains the correct operator
        assertTrue("Rule should contain :- operator", rule.text.contains(":-"))
    }

    @Test
    fun testFunctionParsing() {
        // Test parsing a simple function
        val code = """
            square(X) = X * X.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the function is parsed correctly
        val functions = file.getFunctions()
        assertEquals("Should have one function", 1, functions.size)

        // Check that the function has the correct name
        val function = functions[0]
        assertEquals("Function should be named 'square'", "square", function.getName())

        // Check that the function has the correct arity
        assertEquals("Function should have arity 1", 1, function.getArity())
    }

    @Test
    fun testFunctionWithConditionParsing() {
        // Test parsing a function with a condition
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the function is parsed correctly
        val functions = file.getFunctions()
        assertEquals("Should have one function", 1, functions.size)

        // Check that the function has the correct name
        val function = functions[0]
        assertEquals("Function should be named 'factorial'", "factorial", function.getName())

        // Check that the function has the correct arity
        assertEquals("Function should have arity 1", 1, function.getArity())
    }

    @Test
    fun testPatternMatchingParsing() {
        // Test parsing pattern matching in functions
        val code = """
            custom_sum([]) = 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the functions are parsed correctly
        val functions = file.getFunctions()
        assertEquals("Should have one function", 1, functions.size)

        // Check that both functions have the same name
        assertEquals("Function should be named 'custom_sum'", "custom_sum", functions[0].getName())
    }

    @Test
    fun testExpressionParsing() {
        // Test parsing a simple expression
        val code = """
            test_expression =>
                X = 1 + 2 * 3.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        assertNotNull("Rule should have a body", body)
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
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()
        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        assertNotNull("Rule should have a body", body)
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
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.getRules()

        assertEquals("Should have one rule", 1, rules.size)

        // Check that the rule has a body
        val rule = rules[0]
        val body = rule.getBody()
        assertNotNull("Rule should have a body", body)
    }
}
