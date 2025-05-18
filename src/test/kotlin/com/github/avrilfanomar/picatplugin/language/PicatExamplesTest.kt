package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.highlighting.PicatSyntaxHighlighter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.testFramework.LexerTestCase
import org.junit.Test
import java.io.File

/**
 * Test for Picat syntax highlighting using examples.pi.
 * This test verifies that the lexer correctly identifies tokens in the examples.pi file.
 */
class PicatExamplesTest : LexerTestCase() {

    @Test
    fun testExamplesPi() {
        val highlighter = PicatSyntaxHighlighter()
        val lexer = highlighter.highlightingLexer

        // Read examples.pi file
        val examplesFile = File("examples.pi")
        if (!examplesFile.exists()) {
            return
        }

        val text = examplesFile.readText()

        // Track issues
        val issues = mutableListOf<String>()

        // Process the entire file
        val sampleText = text

        lexer.start(sampleText)

        // Verify that tokens are correctly identified and highlighted
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType
            val tokenText = sampleText.substring(lexer.tokenStart, lexer.tokenEnd)
            val attributes = tokenType?.let { highlighter.getTokenHighlights(it) } ?: emptyArray()

            // Check for BAD_CHARACTER tokens
            if (tokenType == PicatTokenTypes.BAD_CHARACTER) {
                issues.add("Bad character: '$tokenText' at position ${lexer.tokenStart}")
            }

            // Check for unrecognized syntax
            if (tokenText.contains("/*") || tokenText.contains("*/")) {
                if (tokenType != PicatTokenTypes.COMMENT) {
                    issues.add("Multi-line comment not recognized: '$tokenText' at position ${lexer.tokenStart}")
                }
            }

            // Only check for data constructors in non-comment tokens
            if (tokenText.contains("$") && tokenType != PicatTokenTypes.DATA_CONSTRUCTOR && tokenType != PicatTokenTypes.COMMENT) {
                // Skip checking for data constructors in comments or strings
                if (tokenType != PicatTokenTypes.STRING) {
                    issues.add("Data constructor not recognized: '$tokenText' at position ${lexer.tokenStart}")
                }
            }

            if (tokenText.contains("?=>") && tokenType == PicatTokenTypes.BAD_CHARACTER) {
                issues.add("Backtrackable rule operator not recognized: '$tokenText' at position ${lexer.tokenStart}")
            }

            if (tokenText.contains("@") && tokenType == PicatTokenTypes.BAD_CHARACTER) {
                issues.add("As-pattern operator not recognized: '$tokenText' at position ${lexer.tokenStart}")
            }


            lexer.advance()
        }

        // Report issues
        if (issues.isNotEmpty()) {
            // Fail the test with a detailed message
            fail("Found ${issues.size} issues in examples.pi:\n${issues.joinToString("\n")}")
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
