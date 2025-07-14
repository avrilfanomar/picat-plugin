package com.github.avrilfanomar.picatplugin.language.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatCompletionContributor.
 * Verifies that code completion works correctly for Picat language elements.
 */
class PicatCompletionContributorTest : BasePlatformTestCase() {

    fun testKeywordCompletion() {
        // Test completion of keywords
        val text = """
            main => 
                i<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'if' keyword", lookupStrings!!.contains("if"))
        assertTrue("Should contain 'import' keyword", lookupStrings.contains("import"))
        assertTrue("Should contain 'include' keyword", lookupStrings.contains("include"))
        assertTrue("Should contain 'index' keyword", lookupStrings.contains("index"))
        assertTrue("Should contain 'in' keyword", lookupStrings.contains("in"))
    }

    fun testBuiltInFunctionCompletion() {
        // Test completion of built-in functions
        val text = """
            main => 
                L = [1, 2, 3],
                len<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'length' function", lookupStrings!!.contains("length"))
    }

    fun testBuiltInFunctionCompletionWithParentheses() {
        // Test that built-in functions insert parentheses
        val text = """
            main => 
                L = [1, 2, 3],
                length<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        // Select the 'length' completion
        val lookupElements = myFixture.lookupElements
        val lengthElement = lookupElements?.find { it.lookupString == "length" }
        assertNotNull("Should find 'length' completion", lengthElement)

        myFixture.finishLookup('\t')

        val resultText = myFixture.editor.document.text
        assertTrue(
            "Should insert parentheses after function name",
            resultText.contains("length()")
        )
    }

    fun testConstantCompletion() {
        // Test completion of constants
        val text = """
            main => 
                X = tr<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'true' constant", lookupStrings!!.contains("true"))
        assertTrue("Should contain 'try' keyword", lookupStrings.contains("try"))
    }

    fun testControlStructureCompletion() {
        // Test completion of control structures
        val text = """
            main => 
                if X > 0 th<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'then' keyword", lookupStrings!!.contains("then"))
    }

    fun testExceptionHandlingCompletion() {
        // Test completion of exception handling keywords
        val text = """
            main => 
                try
                    X = 1
                cat<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'catch' keyword", lookupStrings!!.contains("catch"))
    }

    fun testMathFunctionCompletion() {
        // Test completion of math functions
        val text = """
            main => 
                X = 5,
                Y = 3,
                ma<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'max' function", lookupStrings!!.contains("max"))
    }

    fun testIOFunctionCompletion() {
        // Test completion of I/O functions
        val text = """
            main => 
                prin<caret>
        """.trimIndent()

        myFixture.configureByText("test.pi", text)
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion should provide suggestions", lookupStrings)
        assertTrue("Should contain 'println' function", lookupStrings!!.contains("println"))
        assertTrue("Should contain 'print' function", lookupStrings.contains("print"))
    }
}