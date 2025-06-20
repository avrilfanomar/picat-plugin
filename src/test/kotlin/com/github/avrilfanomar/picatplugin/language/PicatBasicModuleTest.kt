package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.highlighting.PicatSyntaxHighlighter
import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat basic module support.
 * This test verifies that the lexer correctly identifies basic module functions and operators.
 */
class PicatBasicModuleTest : LexerTestCase() {

    @Test
    fun testBasicModuleFunctions() {
        val highlighter = PicatSyntaxHighlighter()
        val lexer = highlighter.highlightingLexer

        val text = getBasicModuleFunctionsTestText()
        lexer.start(text)

        // Track basic module functions found and issues
        val basicModuleFunctionsFound = mutableListOf<String>()
        val issues = mutableListOf<String>()

        // Process tokens
        processTokens(lexer, text, basicModuleFunctionsFound, issues)

        // Report issues
        if (issues.isNotEmpty()) {
            // Fail the test with a detailed message
            val value: Any? =
                Assertions.fail(
                    "Found ${issues.size} issues in basic module test:" +
                            "\n${issues.joinToString("\n")}"
                )
            if (value != null) {
                println("it was not null")//whatever
            }
        }

        // Basic module functions found (not logged, but kept for test functionality)
    }

    /**
     * Returns the test text for basic module functions.
     */
    private fun getBasicModuleFunctionsTestText(): String {
        return """
            % This is a test for basic module functions

            main => 
                % Test some basic module functions
                L = [1, 2, 3, 4, 5],
                length(L) = Len,
                println("Length: " ++ Len),
                append(L, [6, 7], L2),
                println(L2),
                sort(L2) = Sorted,
                println(Sorted),
                if member(3, L) then
                    println("3 is in the list")
                else
                    println("3 is not in the list")
                end,

                % Test more basic module functions
                X = 42,
                Y = 10,
                max(X, Y) = Max,
                min(X, Y) = Min,
                println("Max: " ++ Max),
                println("Min: " ++ Min),

                % Test list operations
                reverse(L) = Rev,
                println(Rev),
                flatten([[1, 2], [3, 4]]) = Flat,
                println(Flat),

                % Test map operations
                M = new_map(),
                put(M, "key1", "value1"),
                put(M, "key2", "value2"),
                get(M, "key1") = Val1,
                println(Val1),
                keys(M) = Keys,
                println(Keys),
                values(M) = Vals,
                println(Vals).
        """.trimIndent()
    }

    /**
     * Processes tokens from the lexer and checks for basic module functions.
     */
    private fun processTokens(
        lexer: Lexer,
        text: String,
        basicModuleFunctionsFound: MutableList<String>,
        issues: MutableList<String>
    ) {
        val expectedBasicModuleFunctions = listOf(
            "length", "append", "sort", "member", "max", "min", "reverse",
            "flatten", "new_map", "put", "get", "keys", "values"
        )

        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType
            val tokenText = text.substring(lexer.tokenStart, lexer.tokenEnd)

            // Check if the token is a basic module function
            val isIdentifier = tokenType == PicatTokenTypes.IDENTIFIER
            val isExpectedFunction = expectedBasicModuleFunctions.contains(tokenText)
            if (isIdentifier && isExpectedFunction) { // Changed here
                basicModuleFunctionsFound.add(tokenText)
            }

            // Check for expected basic module functions that are not recognized
            val isNotIdentifier = tokenType != PicatTokenTypes.IDENTIFIER
            if (isExpectedFunction && isNotIdentifier) { // Changed here
                val errorMessage = "Basic module function not recognized as IDENTIFIER: " +
                        "'$tokenText' at position ${lexer.tokenStart}"
                issues.add(errorMessage)
            }

            lexer.advance()
        }
    }


    override fun createLexer(): Lexer {
        return PicatSyntaxHighlighter().highlightingLexer
    }

    override fun getDirPath(): String {
        return ""
    }
}
