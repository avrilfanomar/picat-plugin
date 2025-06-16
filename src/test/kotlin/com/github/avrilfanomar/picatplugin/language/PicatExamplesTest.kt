package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.highlighting.PicatSyntaxHighlighter
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.* // Added wildcard import
import com.intellij.lexer.Lexer
import com.intellij.psi.TokenType // Keep one TokenType import
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.LexerTestCase
import org.junit.jupiter.api.Test
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
            val position = lexer.tokenStart

            checkForBadCharacters(tokenType, tokenText, position, issues)
            checkForComments(tokenType, tokenText, position, issues)
            checkForDataConstructors(tokenType, tokenText, position, issues)
            checkForBacktrackableRuleOperator(tokenType, tokenText, position, issues)
            checkForAsPatternOperator(tokenType, tokenText, position, issues)

            lexer.advance()
        }

        // Report issues
        if (issues.isNotEmpty()) {
            // Fail the test with a detailed message
            fail("Found ${issues.size} issues in examples.pi:\n${issues.joinToString("\n")}")
        }
    }

    /**
     * Checks for BAD_CHARACTER tokens.
     */
    private fun checkForBadCharacters(
        tokenType: IElementType?, 
        tokenText: String, 
        position: Int, 
        issues: MutableList<String>
    ) {
        if (tokenType == TokenType.BAD_CHARACTER) { // Changed here
            issues.add("Bad character: '$tokenText' at position $position")
        }
    }

    /**
     * Checks for unrecognized comments.
     */
    private fun checkForComments(
        tokenType: IElementType?, 
        tokenText: String, 
        position: Int, 
        issues: MutableList<String>
    ) {
        if ((tokenText.contains("/*") || tokenText.contains("*/")) && 
            tokenType != PicatTokenTypes.COMMENT) {
            issues.add("Multi-line comment not recognized: '$tokenText' at position $position")
        }
    }

    /**
     * Checks for unrecognized data constructors.
     */
    private fun checkForDataConstructors(
        tokenType: IElementType?, 
        tokenText: String, 
        position: Int, 
        issues: MutableList<String>
    ) {
        // Skip checking if token doesn't contain '$'
        if (!tokenText.contains("$")) {
            return
        }

        // Skip checking for known token types that can contain '$'
        // Temporarily changed PicatTokenTypes.DATA_CONSTRUCTOR to false to allow compilation
        val isAllowedTokenType = false /* tokenType == PicatTokenTypes.DATA_CONSTRUCTOR */ ||
                                 tokenType == PicatTokenTypes.COMMENT ||
                                 tokenType == PicatTokenTypes.STRING

        if (!isAllowedTokenType) {
            issues.add("Data constructor not recognized: '$tokenText' at position $position")
        }
    }

    /**
     * Checks for unrecognized backtrackable rule operators.
     */
    private fun checkForBacktrackableRuleOperator(
        tokenType: IElementType?, 
        tokenText: String, 
        position: Int, 
        issues: MutableList<String>
    ) {
        if (tokenText.contains("?=>") && tokenType == TokenType.BAD_CHARACTER) { // Changed here
            issues.add("Backtrackable rule operator not recognized: '$tokenText' at position $position")
        }
    }

    /**
     * Checks for unrecognized as-pattern operators.
     */
    private fun checkForAsPatternOperator(
        tokenType: IElementType?, 
        tokenText: String, 
        position: Int, 
        issues: MutableList<String>
    ) {
        if (tokenType == TokenType.BAD_CHARACTER) { // Changed here
            issues.add("As-pattern operator not recognized: '$tokenText' at position $position")
        }
    }


    override fun createLexer(): Lexer {
        return PicatSyntaxHighlighter().highlightingLexer
    }

    override fun getDirPath(): String {
        return ""
    }
}
