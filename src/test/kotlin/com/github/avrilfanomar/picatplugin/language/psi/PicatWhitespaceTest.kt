package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.junit.Ignore

/**
 * Test for Picat whitespace handling in PSI parsing.
 * This test verifies that whitespace is correctly handled between operators, braces, etc.
 */
class PicatWhitespaceTest : BasePlatformTestCase() {

    @Test
    @Ignore("Test is not ready to run yet")
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
        assertTrue("There should be expressions with operators", expressionsWithOperators.isNotEmpty())

        // Check if operators have whitespace around them
        for (expr in expressionsWithOperators) {
            val operators = expr.getOperators()
            for (operator in operators) {
                val operatorText = operator.text
                if (operatorText in listOf("+", "*", "/")) {
                    val exprText = expr.text
                    val operatorIndex = exprText.indexOf(operatorText)
                    if (operatorIndex > 0 && operatorIndex < exprText.length - 1) {
                        val hasPrecedingWhitespace = exprText[operatorIndex - 1].isWhitespace()
                        val hasFollowingWhitespace = exprText[operatorIndex + 1].isWhitespace()
                        assertTrue("Operator $operatorText should have whitespace before it", hasPrecedingWhitespace)
                        assertTrue("Operator $operatorText should have whitespace after it", hasFollowingWhitespace)
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
    @Ignore("Test is not ready to run yet")
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
        assertTrue("There should be at least one list", lists.isNotEmpty())

        // Check if lists have whitespace inside them
        for (list in lists) {
            val listText = list.text

            // Check if the list has opening and closing brackets
            assertTrue("List should have opening bracket", listText.contains("["))
            assertTrue("List should have closing bracket", listText.contains("]"))

            // Check if there's whitespace after the opening bracket
            val openBracketIndex = listText.indexOf("[")
            if (openBracketIndex >= 0 && openBracketIndex < listText.length - 1) {
                val hasWhitespaceAfterOpenBracket = listText[openBracketIndex + 1].isWhitespace()
                assertTrue("There should be whitespace after the opening bracket", hasWhitespaceAfterOpenBracket)
            }

            // Check if there's whitespace before the closing bracket
            val closeBracketIndex = listText.lastIndexOf("]")
            if (closeBracketIndex > 0) {
                val hasWhitespaceBeforeCloseBracket = listText[closeBracketIndex - 1].isWhitespace()
                assertTrue("There should be whitespace before the closing bracket", hasWhitespaceBeforeCloseBracket)
            }

            // Check if the list has elements
            val listElements = list.getListElements()
            if (listElements != null) {
                val expressions = listElements.getExpressions()
                assertTrue("List should have elements", expressions.isNotEmpty())

                // Check if there's whitespace around commas
                if (expressions.size > 1) {
                    val listElementsText = listElements.text
                    val commaIndices = listElementsText.indices.filter { listElementsText[it] == ',' }

                    for (commaIndex in commaIndices) {
                        if (commaIndex < listElementsText.length - 1) {
                            val hasWhitespaceAfterComma = listElementsText[commaIndex + 1].isWhitespace()
                            assertTrue("There should be whitespace after commas", hasWhitespaceAfterComma)
                        }
                    }
                }
            }
        }
    }

    @Test
    @Ignore("Test is not ready to run yet")
    fun testWhitespaceBetweenParentheses() {
        // Test that whitespace between parentheses is correctly handled
        val code = """
            main => 
                X = ( 1 + 2 ) * 3,
                println( X ).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all terms in the file
        val terms = PsiTreeUtil.findChildrenOfType(file, PicatTerm::class.java)

        // Find terms with parenthesized expressions
        val termsWithParenthesizedExpressions = terms.filter { term -> 
            term.getExpression() != null 
        }

        // Verify that there are terms with parenthesized expressions
        assertTrue("There should be terms with parenthesized expressions", termsWithParenthesizedExpressions.isNotEmpty())

        // Check if parenthesized expressions have whitespace inside them
        for (term in termsWithParenthesizedExpressions) {
            val termText = term.text

            // Check if the term has opening and closing parentheses
            assertTrue("Term should have opening parenthesis", termText.contains("("))
            assertTrue("Term should have closing parenthesis", termText.contains(")"))

            // Check if there's whitespace after the opening parenthesis
            val openParenIndex = termText.indexOf("(")
            if (openParenIndex >= 0 && openParenIndex < termText.length - 1) {
                val hasWhitespaceAfterOpenParen = termText[openParenIndex + 1].isWhitespace()
                assertTrue("There should be whitespace after the opening parenthesis", hasWhitespaceAfterOpenParen)
            }

            // Check if there's whitespace before the closing parenthesis
            val closeParenIndex = termText.lastIndexOf(")")
            if (closeParenIndex > 0) {
                val hasWhitespaceBeforeCloseParen = termText[closeParenIndex - 1].isWhitespace()
                assertTrue("There should be whitespace before the closing parenthesis", hasWhitespaceBeforeCloseParen)
            }
        }

        // Find all structures (function calls) in the file
        val structures = PsiTreeUtil.findChildrenOfType(file, PicatStructure::class.java)

        // Verify that there are structures
        assertTrue("There should be structures (function calls)", structures.isNotEmpty())

        // Check if structures have whitespace inside their parentheses
        for (structure in structures) {
            val structureText = structure.text

            // Check if the structure has opening and closing parentheses
            assertTrue("Structure should have opening parenthesis", structureText.contains("("))
            assertTrue("Structure should have closing parenthesis", structureText.contains(")"))

            // Check if there's whitespace after the opening parenthesis
            val openParenIndex = structureText.indexOf("(")
            if (openParenIndex >= 0 && openParenIndex < structureText.length - 1) {
                val hasWhitespaceAfterOpenParen = structureText[openParenIndex + 1].isWhitespace()
                assertTrue("There should be whitespace after the opening parenthesis in function call", hasWhitespaceAfterOpenParen)
            }

            // Check if there's whitespace before the closing parenthesis
            val closeParenIndex = structureText.lastIndexOf(")")
            if (closeParenIndex > 0) {
                val hasWhitespaceBeforeCloseParen = structureText[closeParenIndex - 1].isWhitespace()
                assertTrue("There should be whitespace before the closing parenthesis in function call", hasWhitespaceBeforeCloseParen)
            }
        }
    }

}
