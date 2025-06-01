package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test for Picat rule PSI implementation.
 * This test verifies that rules are correctly parsed into PSI elements.
 */
class PicatRuleTest : BasePlatformTestCase() {

    @Test
    fun testSimpleRulePsi() {
        // Test that a simple rule is correctly parsed
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there is exactly one rule
        assertEquals(1, rules.size, "There should be exactly one rule")

        // Verify that the rule has the correct head, body, and rule operator
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull(head, "Rule should have a head")
        val body = rule.getBody()
        assertNotNull(body, "Rule should have a body")

        // Verify that the rule contains => operator
        assertTrue(rule.text.contains("=>"), "Rule should contain => operator")
    }

    @Test
    fun testRuleWithComplexHeadPsi() {
        // Test that a rule with a complex head is correctly parsed
        val code = """
            append([H|T], L) = [H|append(T, L)] => true.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there is exactly one rule
        assertEquals(1, rules.size, "There should be exactly one rule")

        // Verify that the rule has the correct head, body, and rule operator
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull(head, "Rule should have a head")
        val body = rule.getBody()
        assertNotNull(body, "Rule should have a body")

        // Verify that the rule contains => operator
        assertTrue(rule.text.contains("=>"), "Rule should contain => operator")
    }

    @Test
    fun testRuleWithComplexBodyPsi() {
        // Test that a rule with a complex body is correctly parsed
        val code = """
            factorial(N) = F => N > 0, F = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there is exactly one rule
        assertEquals(1, rules.size, "There should be exactly one rule")

        // Verify that the rule has the correct head and body
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull(head, "Rule should have a head")
        val body = rule.getBody()
        assertNotNull(body, "Rule should have a body")

        // Verify that the body contains multiple goals
        val goals = body?.text?.split(",")
        assertNotNull(goals, "Body should have goals")
        assertTrue(goals?.size ?: 0 > 1, "Body should have multiple goals")
    }

    @Test
    fun testDifferentRuleOperatorsPsi() {
        // Test that rules with different operators are correctly parsed
        val code = """
            factorial(N) = N * factorial(N - 1) => N > 0.

            fibonacci(N) = fibonacci(N - 1) + fibonacci(N - 2) ?=> N > 1.

            reverse(L) = R :- reverse(L, [], R).

            constraint(X, Y) <=> X #= Y.

            backtrack_constraint(X, Y) ?<=> X #= Y.

        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there are exactly five rules
        assertEquals(5, rules.size, "There should be exactly five rules")

        // Verify that each rule contains the correct operator in its text
        assertTrue(rules[0].text.contains("=>"), "First rule should contain => operator")
        assertTrue(rules[1].text.contains("?=>"), "Second rule should contain ?=> operator")
        assertTrue(rules[2].text.contains(":-"), "Third rule should contain :- operator")
        assertTrue(rules[3].text.contains("<=>"), "Fourth rule should contain <=> operator")
        assertTrue(rules[4].text.contains("?<=>"), "Fifth rule should contain ?<=> operator")
    }

}
