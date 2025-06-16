package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test for Picat whitespace handling in PSI parsing.
 * This test verifies that whitespace is correctly handled between operators, braces, etc.
 */
class PicatWhitespaceTest : BasePlatformTestCase() {

    /**
     * Helper method to print the PSI tree.
     */
    private fun printPsiTree(element: PsiElement, level: Int) {
        element.children.forEach { child ->
            printPsiTree(child, level + 1)
        }
    }

    @Test
    fun testWhitespaceBetweenBraces() {
        // Test that whitespace between braces is correctly handled
        val code = """
            main => 
                L = [ 1, 2, 3 ],
                println(L).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

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
    fun testWhitespaceBetweenParentheses() {
        // Test that whitespace between parentheses is correctly handled
        val code = """
            main => 
                X = (1 + 2) * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all expressions in the file
        val expressions = PsiTreeUtil.findChildrenOfType(file, PicatExpression::class.java)

        // Find expressions with parentheses
        val expressionsWithParentheses = expressions.filter { expr ->
            expr.text.contains("(") && expr.text.contains(")")
        }

        // Verify that there are expressions with parentheses
        assertTrue(expressionsWithParentheses.isNotEmpty(), "There should be expressions with parentheses")
    }

}
