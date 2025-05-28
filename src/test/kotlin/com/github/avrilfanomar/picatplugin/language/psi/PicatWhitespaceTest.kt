package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Disabled

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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = PsiTreeUtil.findChildrenOfType(file, PicatExpression::class.java)

        // Find expressions with operators
        val expressionsWithOperators = expressions.filter { expr -> 
            expr.getOperators().isNotEmpty() 
        }

        // Verify that there are expressions with operators
        assertTrue(expressionsWithOperators.isNotEmpty(, "There should be expressions with operators"))

        // Check if operators have whitespace around them
        for (expr in expressionsWithOperators) {
            val operators = expr.getOperators()
            for (operator in operators) {
                val operatorText = operator.text
                if (operatorText in listOf("*", "/", "+")) {
                    val exprText = expr.text
                    val operatorIndex = exprText.indexOf(operatorText)
                    if (operatorIndex > 0 && operatorIndex < exprText.length - 1) {
                        val hasPrecedingWhitespace = exprText[operatorIndex - 1].isWhitespace()
                        val hasFollowingWhitespace = exprText[operatorIndex + 1].isWhitespace()
                        assertTrue(hasPrecedingWhitespace, "Operator $operatorText should have whitespace before it")
                        assertTrue(hasFollowingWhitespace, "Operator $operatorText should have whitespace after it")
                    }
                }
            }
        }
    }

    /**
     * Helper method to print the PSI tree.
     */
    private fun printPsiTree(element: PsiElement, level: Int) {
        val indent = "  ".repeat(level)
        println("[DEBUG_LOG] $indent${element.javaClass.simpleName}: ${element.text}")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all lists in the file
        val lists = PsiTreeUtil.findChildrenOfType(file, PicatList::class.java)

        // Verify that there are lists
        assertTrue(lists.isNotEmpty(, "There should be at least one list"))

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
                assertTrue(hasWhitespaceAfterOpenBracket, "There should be whitespace after the opening bracket")
            }

            // Check if there's whitespace before the closing bracket
            val closeBracketIndex = listText.lastIndexOf("]")
            if (closeBracketIndex > 0) {
                val hasWhitespaceBeforeCloseBracket = listText[closeBracketIndex - 1].isWhitespace()
                assertTrue(hasWhitespaceBeforeCloseBracket, "There should be whitespace before the closing bracket")
            }

            // Check if the list has elements
            val listElements = list.getListElements()
            if (listElements != null) {
                val expressions = listElements.getExpressions()
                assertTrue(expressions.isNotEmpty(, "List should have elements"))

                // Check if there's whitespace around commas
                if (expressions.size > 1) {
                    val listElementsText = listElements.text
                    val commaIndices = listElementsText.indices.filter { listElementsText[it] == ',' }

                    for (commaIndex in commaIndices) {
                        if (commaIndex < listElementsText.length - 1) {
                            val hasWhitespaceAfterComma = listElementsText[commaIndex + 1].isWhitespace()
                            assertTrue(hasWhitespaceAfterComma, "There should be whitespace after commas")
                        }
                    }
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
                X = ( 1 + 2 ) * 3,
                println( X ).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all terms in the file
        val terms = PsiTreeUtil.findChildrenOfType(file, PicatTerm::class.java)

        // Find terms with parenthesized expressions
        val termsWithParenthesizedExpressions = terms.filter { term -> 
            term.getExpression() != null 
        }

        // Verify that there are terms with parenthesized expressions
        assertTrue(termsWithParenthesizedExpressions.isNotEmpty(, "There should be terms with parenthesized expressions"))

        // Check if parenthesized expressions have whitespace inside them
        for (term in termsWithParenthesizedExpressions) {
            val termText = term.text

            // Check if the term has opening and closing parentheses
            assertTrue(termText.contains("("), "Term should have opening parenthesis")
            assertTrue(termText.contains("), "Term should have closing parenthesis""))

            // Check if there's whitespace after the opening parenthesis
            val openParenIndex = termText.indexOf("(")
            if (openParenIndex >= 0 && openParenIndex < termText.length - 1) {
                val hasWhitespaceAfterOpenParen = termText[openParenIndex + 1].isWhitespace()
                assertTrue(hasWhitespaceAfterOpenParen, "There should be whitespace after the opening parenthesis")
            }

            // Check if there's whitespace before the closing parenthesis
            val closeParenIndex = termText.lastIndexOf(")")
            if (closeParenIndex > 0) {
                val hasWhitespaceBeforeCloseParen = termText[closeParenIndex - 1].isWhitespace()
                assertTrue(hasWhitespaceBeforeCloseParen, "There should be whitespace before the closing parenthesis")
            }
        }

        // Find all structures (function calls) in the file
        val structures = PsiTreeUtil.findChildrenOfType(file, PicatStructure::class.java)

        // Verify that there are structures
        assertTrue(structures.isNotEmpty(, "There should be structures (function calls)"))

        // Check if structures have whitespace inside their parentheses
        for (structure in structures) {
            val structureText = structure.text

            // Check if the structure has opening and closing parentheses
            assertTrue(structureText.contains("("), "Structure should have opening parenthesis")
            assertTrue(structureText.contains("), "Structure should have closing parenthesis""))

            // Check if there's whitespace after the opening parenthesis
            val openParenIndex = structureText.indexOf("(")
            if (openParenIndex >= 0 && openParenIndex < structureText.length - 1) {
                val hasWhitespaceAfterOpenParen = structureText[openParenIndex + 1].isWhitespace()
                assertTrue(hasWhitespaceAfterOpenParen, "There should be whitespace after the opening parenthesis in function call")
            }

            // Check if there's whitespace before the closing parenthesis
            val closeParenIndex = structureText.lastIndexOf(")")
            if (closeParenIndex > 0) {
                val hasWhitespaceBeforeCloseParen = structureText[closeParenIndex - 1].isWhitespace()
                assertTrue(hasWhitespaceBeforeCloseParen, "There should be whitespace before the closing parenthesis in function call")
            }
        }
    }

}
