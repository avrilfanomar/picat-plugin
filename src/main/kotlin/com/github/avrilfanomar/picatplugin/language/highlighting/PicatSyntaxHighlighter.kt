package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexerAdapter // New import
// import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexer // Remove this
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
        return PicatLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        // Check for bad characters first
        if (isBadCharacter(tokenType)) {
            return pack(BAD_CHARACTER)
        }

        // Check token categories in order
        return when {
            isKeyword(tokenType) -> pack(KEYWORD)
            isComment(tokenType) -> pack(COMMENT)
            isString(tokenType) -> pack(STRING)
            isNumber(tokenType) -> pack(NUMBER)
            isOperator(tokenType) -> pack(OPERATOR)
            isParenthesis(tokenType) -> pack(PARENTHESES)
            isBrace(tokenType) -> pack(BRACES)
            isBracket(tokenType) -> pack(BRACKETS)
            isVariable(tokenType) -> pack(VARIABLE)
            isBasicModuleFunction(tokenType) -> pack(BASIC_MODULE_FUNCTION)
            isIdentifier(tokenType) -> pack(IDENTIFIER)
            else -> TextAttributesKey.EMPTY_ARRAY
        }
    }

    // Helper methods to categorize token types
    private fun isBadCharacter(tokenType: IElementType): Boolean =
        tokenType == TokenType.BAD_CHARACTER || tokenType == PicatTokenTypes.BAD_CHARACTER

    private fun isKeyword(tokenType: IElementType): Boolean =
        tokenType in PicatTokenTypes.KEYWORDS

    private fun isComment(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.COMMENT

    private fun isString(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.STRING

    private fun isNumber(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.INTEGER || tokenType == PicatTokenTypes.FLOAT

    private fun isOperator(tokenType: IElementType): Boolean =
        tokenType in PicatTokenTypes.OPERATORS

    private fun isParenthesis(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LPAR || tokenType == PicatTokenTypes.RPAR

    private fun isBrace(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACE || tokenType == PicatTokenTypes.RBRACE

    private fun isBracket(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACKET || tokenType == PicatTokenTypes.RBRACKET

    private fun isVariable(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.VARIABLE

    private fun isBasicModuleFunction(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.BASIC_MODULE_FUNCTION

    private fun isIdentifier(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.IDENTIFIER

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
