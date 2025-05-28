package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Debug: Print the number of rules found
        println("[DEBUG_LOG] Number of rules found: ${rules.size}")
        rules.forEach { rule ->
            println("[DEBUG_LOG] Rule: ${rule.text}, Class: ${rule.javaClass.simpleName}")
        }

        // Verify that there is exactly one rule
        assertEquals(1, rules.size, "There should be exactly one rule")

        // Verify that the rule has the correct head, body, and rule operator
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull(head, "Rule should have a head")
        val body = rule.getBody()
        assertNotNull(body, "Rule should have a body")
        val ruleOperator = rule.getRuleOperator()
        assertNotNull(ruleOperator, "Rule should have a rule operator")
        
        // Verify that the rule operator is =>
        assertEquals(ruleOperator?.text, "Rule operator should be =>", "=>")
        
        // Debug: Print the head and body text
        println("[DEBUG_LOG] Rule head: ${head?.text}")
        println("[DEBUG_LOG] Rule body: ${body?.text}")
    }

    @Test
    fun testRuleWithComplexHeadPsi() {
        // Test that a rule with a complex head is correctly parsed
        val code = """
            append([H|T], L) = [H|append(T, L)] => true.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
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
        val ruleOperator = rule.getRuleOperator()
        assertNotNull(ruleOperator, "Rule should have a rule operator")
        
        // Verify that the rule operator is =>
        assertEquals(ruleOperator?.text, "Rule operator should be =>", "=>")
        
        // Debug: Print the head and body text
        println("[DEBUG_LOG] Rule head: ${head?.text}")
        println("[DEBUG_LOG] Rule body: ${body?.text}")
    }

    @Test
    fun testRuleWithComplexBodyPsi() {
        // Test that a rule with a complex body is correctly parsed
        val code = """
            factorial(N) = F => N > 0, F = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
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
        val ruleOperator = rule.getRuleOperator()
        assertNotNull(ruleOperator, "Rule should have a rule operator")
        
        // Verify that the rule operator is =>
        assertEquals(ruleOperator?.text, "Rule operator should be =>", "=>")
        
        // Debug: Print the head and body text
        println("[DEBUG_LOG] Rule head: ${head?.text}")
        println("[DEBUG_LOG] Rule body: ${body?.text}")
        
        // Verify that the body contains multiple goals
        val goals = body?.text?.split(",")
        assertNotNull(goals, "Body should have goals")
        assertTrue(goals?.size ?: 0 > 1, "Body should have multiple goals")
    }

    @Test
    fun testDifferentRuleOperatorsPsi() {
        // Test that rules with different operators are correctly parsed
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
            fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) ?=> N > 1.
            reverse(L) = R :- reverse(L, [], R).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there are exactly three rules
        assertEquals(3, rules.size, "There should be exactly three rules")

        // Verify that each rule has the correct rule operator
        assertEquals(rules[0].getRuleOperator(, "First rule operator should be =>", "=>")?.text)
        assertEquals(rules[1].getRuleOperator(, "Second rule operator should be ?=>", "?=>")?.text)
        assertEquals(rules[2].getRuleOperator(, "Third rule operator should be :-", ":-")?.text)
    }

    @Test
    fun testMultipleRulesPsi() {
        // Test that multiple rules are correctly parsed
        val code = """
            factorial(0) = 1 => true.
            factorial(N) = N * factorial(N-1) => N > 0.
            
            fibonacci(0) = 0 => true.
            fibonacci(1) = 1 => true.
            fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) => N > 1.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there are exactly five rules
        assertEquals(5, rules.size, "There should be exactly five rules")

        // Verify that each rule has the correct head name
        assertEquals(rules[0].getHead(, "First rule head name should be factorial", "factorial")?.text?.substringBefore("("))
        assertEquals(rules[1].getHead(, "Second rule head name should be factorial", "factorial")?.text?.substringBefore("("))
        assertEquals(rules[2].getHead(, "Third rule head name should be fibonacci", "fibonacci")?.text?.substringBefore("("))
        assertEquals(rules[3].getHead(, "Fourth rule head name should be fibonacci", "fibonacci")?.text?.substringBefore("("))
        assertEquals(rules[4].getHead(, "Fifth rule head name should be fibonacci", "fibonacci")?.text?.substringBefore("("))
    }
}
