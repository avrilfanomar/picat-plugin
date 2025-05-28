package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.highlighting.PicatSyntaxHighlighter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.testFramework.LexerTestCase
import org.junit.Test

/**
 * Test for Picat syntax highlighting.
 */
class PicatSyntaxHighlightingTest : LexerTestCase() {


    @Test
    fun testHighlighter() {
        val highlighter = PicatSyntaxHighlighter()
        val lexer = highlighter.highlightingLexer

        val text = """
            % This is a sample Picat program

            import util.

            main => 
                println("Hello, world!"),
                X = 42,
                Y = X + 10,
                println(Y),
                if true then
                    println("This is true")
                else
                    fail
                end,
                repeat
                    Z = Z + 1
                until Z > 10,
                false.

            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            fibonacci(0) = 0.
            fibonacci(1) = 1.
            fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) => N > 1.
        """.trimIndent()

        lexer.start(text)

        // Map to store token text to expected attribute
        val expectedHighlights = mapOf(
            "%" to "PICAT_COMMENT",
            "import" to "PICAT_KEYWORD",
            "util" to "PICAT_IDENTIFIER",
            "main" to "PICAT_IDENTIFIER",
            "=>" to "PICAT_OPERATOR",
            "println" to "PICAT_IDENTIFIER",
            "\"Hello, world!\"" to "PICAT_STRING",
            "X" to "PICAT_VARIABLE",
            "=" to "PICAT_OPERATOR",
            "42" to "PICAT_NUMBER",
            "+" to "PICAT_OPERATOR",
            "10" to "PICAT_NUMBER",
            "if" to "PICAT_KEYWORD",
            "true" to "PICAT_KEYWORD",
            "then" to "PICAT_KEYWORD",
            "else" to "PICAT_KEYWORD",
            "fail" to "PICAT_KEYWORD",
            "end" to "PICAT_KEYWORD",
            "repeat" to "PICAT_KEYWORD",
            "until" to "PICAT_KEYWORD",
            ">" to "PICAT_OPERATOR",
            "false" to "PICAT_KEYWORD",
            "factorial" to "PICAT_IDENTIFIER",
            "0" to "PICAT_NUMBER",
            "1" to "PICAT_NUMBER",
            "N" to "PICAT_VARIABLE",
            "*" to "PICAT_OPERATOR",
            "-" to "PICAT_OPERATOR",
            "fibonacci" to "PICAT_IDENTIFIER"
        )

        // Verify that tokens are correctly identified and highlighted
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType
            val tokenText = text.substring(lexer.tokenStart, lexer.tokenEnd)
            val attributes = tokenType?.let { highlighter.getTokenHighlights(it) } ?: emptyArray()

            // If this token is in our expected highlights map, verify its attribute
            if (expectedHighlights.containsKey(tokenText)) {
                val expectedAttribute = expectedHighlights[tokenText]
                val attributeNames = attributesToString(attributes)

                assertTrue(
                    "Token '$tokenText' should have attribute '$expectedAttribute' but has '$attributeNames'",
                    attributeNames.contains(expectedAttribute ?: "")
                )
            }

            lexer.advance()
        }
    }

    private fun attributesToString(attributes: Array<TextAttributesKey>): String {
        return attributes.joinToString(", ") { it.externalName }
    }

    override fun createLexer(): Lexer {
        return PicatSyntaxHighlighter().highlightingLexer
    }

    override fun getDirPath(): String {
        return ""
    }
}
