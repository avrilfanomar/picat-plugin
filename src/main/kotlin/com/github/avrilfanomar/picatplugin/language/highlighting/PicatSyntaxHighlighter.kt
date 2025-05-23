package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexer
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lexer.Lexer
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
        return PicatLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        if (tokenType == TokenType.BAD_CHARACTER || tokenType == PicatTokenTypes.BAD_CHARACTER) {
            return pack(BAD_CHARACTER)
        }

        // Handle different token types
        return when (tokenType) {
            // Keywords
            in PicatTokenTypes.KEYWORDS -> pack(KEYWORD)

            // Comments
            PicatTokenTypes.COMMENT -> pack(COMMENT)

            // Strings
            PicatTokenTypes.STRING -> pack(STRING)

            // Numbers
            PicatTokenTypes.INTEGER, PicatTokenTypes.FLOAT -> pack(NUMBER)

            // Operators
            in PicatTokenTypes.OPERATORS -> pack(OPERATOR)

            // Parentheses, braces, brackets
            PicatTokenTypes.LPAR, PicatTokenTypes.RPAR -> pack(PARENTHESES)
            PicatTokenTypes.LBRACE, PicatTokenTypes.RBRACE -> pack(BRACES)
            PicatTokenTypes.LBRACKET, PicatTokenTypes.RBRACKET -> pack(BRACKETS)

            // Variables
            PicatTokenTypes.VARIABLE -> pack(VARIABLE)

            // Basic module functions and operators
            PicatTokenTypes.BASIC_MODULE_FUNCTION -> pack(BASIC_MODULE_FUNCTION)

            // Identifiers (including predicates)
            PicatTokenTypes.IDENTIFIER -> pack(IDENTIFIER)

            // Default case
            else -> TextAttributesKey.EMPTY_ARRAY
        }
    }

    companion object {
        // Define text attribute keys for different token types
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("PICAT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("PICAT_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val STRING = TextAttributesKey.createTextAttributesKey("PICAT_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = TextAttributesKey.createTextAttributesKey("PICAT_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val OPERATOR =
            TextAttributesKey.createTextAttributesKey("PICAT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PARENTHESES =
            TextAttributesKey.createTextAttributesKey("PICAT_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("PICAT_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS =
            TextAttributesKey.createTextAttributesKey("PICAT_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val IDENTIFIER =
            TextAttributesKey.createTextAttributesKey("PICAT_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val PREDICATE = TextAttributesKey.createTextAttributesKey(
            "PICAT_PREDICATE",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
        val VARIABLE =
            TextAttributesKey.createTextAttributesKey("PICAT_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
        val BASIC_MODULE_FUNCTION = TextAttributesKey.createTextAttributesKey(
            "PICAT_BASIC_MODULE_FUNCTION",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )
        val BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("PICAT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}
