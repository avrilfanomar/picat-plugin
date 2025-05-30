package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Test for Picat whitespace handling in PSI parsing.
 * This test verifies that whitespace is correctly handled between operators, braces, etc.
 */
class PicatWhitespaceTest : BasePlatformTestCase() {

    @Test
    @Disabled("Test is not ready to run yet")
    fun testWhitespaceBetweenOperators() {
        // Test that whitespace between operators is correctly handled
        val code = """
            main => 
                X = 1 + 2,
                Y = X * 3,
                Z = Y / 4,
                println(Z).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = PsiTreeUtil.findChildrenOfType(file, PicatExpression::class.java)

        // Find expressions with operators
        val expressionsWithOperators = expressions.filter { expr -> 
            expr.getOperators().isNotEmpty() 
        }

        // Verify that there are expressions with operators
        assertTrue(expressionsWithOperators.isNotEmpty(), "There should be expressions with operators")

        // Check if operators have whitespace around them
        checkOperatorsWhitespace(expressionsWithOperators)
    }

    /**
     * Helper method to check if operators have whitespace around them.
     */
    private fun checkOperatorsWhitespace(expressionsWithOperators: Collection<PicatExpression>) {
        for (expr in expressionsWithOperators) {
            val operators = expr.getOperators()
            for (operator in operators) {
                val operatorText = operator.text
                if (operatorText in listOf("*", "/", "+")) {
                    checkOperatorWhitespace(expr.text, operatorText)
                }
            }
        }
    }

    /**
     * Helper method to check if a specific operator has whitespace around it.
     */
    private fun checkOperatorWhitespace(exprText: String, operatorText: String) {
        val operatorIndex = exprText.indexOf(operatorText)
        if (operatorIndex > 0 && operatorIndex < exprText.length - 1) {
            val hasPrecedingWhitespace = exprText[operatorIndex - 1].isWhitespace()
            val hasFollowingWhitespace = exprText[operatorIndex + 1].isWhitespace()
            assertTrue(hasPrecedingWhitespace, "Operator $operatorText should have whitespace before it")
            assertTrue(hasFollowingWhitespace, "Operator $operatorText should have whitespace after it")
        }
    }

    /**
     * Helper method to print the PSI tree.
     */
    private fun printPsiTree(element: PsiElement, level: Int) {
        element.children.forEach { child ->
            printPsiTree(child, level + 1)
        }
    }

    @Test
    @Disabled("Test is not ready to run yet")
    fun testWhitespaceBetweenBraces() {
        // Test that whitespace between braces is correctly handled
        val code = """
            main => 
                L = [ 1, 2, 3 ],
                println(L).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all lists in the file
        val lists = PsiTreeUtil.findChildrenOfType(file, PicatList::class.java)

        // Verify that there are lists
        assertTrue(lists.isNotEmpty(), "There should be at least one list")

        // Check if lists have whitespace inside them
        for (list in lists) {
            val listText = list.text

            // Check if the list has opening and closing brackets
            assertTrue(listText.contains("["), "List should have opening bracket")
            assertTrue(listText.contains("]"), "List should have closing bracket")

            // Check if there's whitespace after the opening bracket
            val openBracketIndex = listText.indexOf("[")
            if (openBracketIndex >= 0 && openBracketIndex < listText.length - 1) {
                val hasWhitespaceAfterOpenBracket = listText[openBracketIndex + 1].isWhitespace()
                assertTrue(hasWhitespaceAfterOpenBracket, "List should have whitespace after opening bracket")
            }

            // Check if there's whitespace before the closing bracket
            val closeBracketIndex = listText.indexOf("]")
            if (closeBracketIndex > 0) {
                val hasWhitespaceBeforeCloseBracket = listText[closeBracketIndex - 1].isWhitespace()
                assertTrue(hasWhitespaceBeforeCloseBracket, "List should have whitespace before closing bracket")
            }

            // Check if there's whitespace around commas
            val commaIndices = listText.indices.filter { listText[it] == ',' }
            for (commaIndex in commaIndices) {
                if (commaIndex > 0 && commaIndex < listText.length - 1) {
                    val hasWhitespaceAfterComma = listText[commaIndex + 1].isWhitespace()
                    assertTrue(hasWhitespaceAfterComma, "List should have whitespace after comma")
                }
            }
        }
    }

    @Test
    @Disabled("Test is not ready to run yet")
    fun testWhitespaceBetweenParentheses() {
        // Test that whitespace between parentheses is correctly handled
        val code = """
            main => 
                X = (1 + 2) * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = PsiTreeUtil.findChildrenOfType(file, PicatExpression::class.java)

        // Find expressions with terms that have parentheses
        val expressionsWithParentheses = expressions.filter { expr ->
            expr.getTerms().any { term ->
                term.text.contains("(") && term.text.contains(")")
            }
        }

        // Verify that there are expressions with parentheses
        assertTrue(expressionsWithParentheses.isNotEmpty(), "There should be expressions with parentheses")

        // Check if parentheses have whitespace inside them
        checkParenthesesWhitespace(expressionsWithParentheses)
    }

    /**
     * Helper method to check if parentheses have whitespace inside them.
     */
    private fun checkParenthesesWhitespace(expressionsWithParentheses: Collection<PicatExpression>) {
        for (expr in expressionsWithParentheses) {
            val terms = expr.getTerms()
            for (term in terms) {
                val termText = term.text
                if (termText.contains("(") && termText.contains(")")) {
                    checkOpenParenthesisWhitespace(termText)
                    checkCloseParenthesisWhitespace(termText)
                }
            }
        }
    }

    /**
     * Helper method to check if there's whitespace after the opening parenthesis.
     */
    private fun checkOpenParenthesisWhitespace(termText: String) {
        val openParenIndex = termText.indexOf("(")
        if (openParenIndex >= 0 && openParenIndex < termText.length - 1) {
            val hasWhitespaceAfterOpenParen = termText[openParenIndex + 1].isWhitespace()
            assertTrue(hasWhitespaceAfterOpenParen, "Term should have whitespace after opening parenthesis")
        }
    }

    /**
     * Helper method to check if there's whitespace before the closing parenthesis.
     */
    private fun checkCloseParenthesisWhitespace(termText: String) {
        val closeParenIndex = termText.indexOf(")")
        if (closeParenIndex > 0) {
            val hasWhitespaceBeforeCloseParen = termText[closeParenIndex - 1].isWhitespace()
            assertTrue(
                hasWhitespaceBeforeCloseParen, 
                "Term should have whitespace before closing parenthesis"
            )
        }
    }
}
