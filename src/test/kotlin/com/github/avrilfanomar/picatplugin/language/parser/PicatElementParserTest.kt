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

        // Check that the facts have heads
        facts.forEach { fact ->
            Assertions.assertNotNull(fact.head, "Fact should have a head")
            val head = fact.head
            Assertions.assertNotNull("Expected name for atom_no_args", head.atomNoArgs!!.atom.text)
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
    fun testFunctionParsing() {
        // Test parsing a simple function
        val code = """
            square(X) = X * X.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the function is parsed correctly
        val functions = file.getFunctions()
        Assertions.assertEquals(1, functions.size, "Should have one function")

        // Check that the function has the correct name
        val function = functions[0]
        val headPsi = function.head
        var functionName: String? = null
        var arity = 0
        if (headPsi.atomNoArgs != null) {
            functionName = headPsi.atomNoArgs!!.atom.text
            // "atom_no_args" implies arity 0 unless it's a variable, but for func defs it's an atom.
            // Arity for atom_no_args in a function head context is 0.
            arity = 0
        } else if (headPsi.structure != null) {
            functionName = headPsi.structure!!.atom.text
            arity = headPsi.structure!!.argumentList?.expressionList?.size ?: 0
        } else if (headPsi.qualifiedAtom != null) {
            // Handle qualified atom if necessary, though less common for simple function names
            functionName = headPsi.qualifiedAtom!!.text // Or more specific part
        }
        Assertions.assertEquals("square", functionName, "Function should be named 'square'")

        // Check that the function has the correct arity
        Assertions.assertEquals(1, arity, "Function should have arity 1")
    }

    @Test
    fun testFunctionWithConditionParsing() {
        // Test parsing a function with a condition
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the function is parsed correctly
        val functions = file.getFunctions()
        Assertions.assertEquals(1, functions.size, "Should have one function")

        // Check that the function has the correct name
        val function = functions[0]
        val headPsiFact = function.head
        var functionNameFact: String? = null
        var arityFact = 0
        if (headPsiFact.atomNoArgs != null) {
            functionNameFact = headPsiFact.atomNoArgs!!.atom.text
            arityFact = 0
        } else if (headPsiFact.structure != null) {
            functionNameFact = headPsiFact.structure!!.atom.text
            arityFact = headPsiFact.structure!!.argumentList?.expressionList?.size ?: 0
        } else if (headPsiFact.qualifiedAtom != null) {
            functionNameFact = headPsiFact.qualifiedAtom!!.text
        }
        Assertions.assertEquals("factorial", functionNameFact, "Function should be named 'factorial'")

        // Check that the function has the correct arity
        Assertions.assertEquals(1, arityFact, "Function should have arity 1")
    }

    @Test
    fun testPatternMatchingParsing() {
        // Test parsing pattern matching in functions
        val code = """
            custom_sum([]) = 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify that the functions are parsed correctly
        val functions = file.getFunctions() // Reverted to getFunctions()
        Assertions.assertEquals(1, functions.size, "Should have one function")

        // Check that both functions have the same name
        val headPsiSum = functions[0].head // Reverted to functions[0]
        var functionNameSum: String? = null
        if (headPsiSum.atomNoArgs != null) {
            functionNameSum = headPsiSum.atomNoArgs!!.atom.text
        } else if (headPsiSum.structure != null) {
            functionNameSum = headPsiSum.structure!!.atom.text
        } else if (headPsiSum.qualifiedAtom != null) {
            functionNameSum = headPsiSum.qualifiedAtom!!.text
        }
        Assertions.assertEquals("custom_sum", functionNameSum, "Function should be named 'custom_sum'")
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
