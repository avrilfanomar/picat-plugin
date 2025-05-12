package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.PicatTokenType
import com.intellij.lexer.Lexer
import com.intellij.lexer.EmptyLexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * Syntax highlighter for Picat language.
 * Defines colors and styles for different token types.
 */
class PicatSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        // Using EmptyLexer as a placeholder
        // In a real implementation, we would create a proper lexer for Picat
        return EmptyLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        if (tokenType == TokenType.BAD_CHARACTER) {
            return pack(BAD_CHARACTER)
        }
        
        // This is a placeholder implementation
        // In a real implementation, we would handle different token types
        return TextAttributesKey.EMPTY_ARRAY
    }

    companion object {
        // Define text attribute keys for different token types
        val KEYWORD = TextAttributesKey.createTextAttributesKey("PICAT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val COMMENT = TextAttributesKey.createTextAttributesKey("PICAT_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val STRING = TextAttributesKey.createTextAttributesKey("PICAT_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = TextAttributesKey.createTextAttributesKey("PICAT_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val OPERATOR = TextAttributesKey.createTextAttributesKey("PICAT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PARENTHESES = TextAttributesKey.createTextAttributesKey("PICAT_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("PICAT_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = TextAttributesKey.createTextAttributesKey("PICAT_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey("PICAT_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val PREDICATE = TextAttributesKey.createTextAttributesKey("PICAT_PREDICATE", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val VARIABLE = TextAttributesKey.createTextAttributesKey("PICAT_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("PICAT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}