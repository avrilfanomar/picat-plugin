package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.intellij.grammar.parser.BnfLexer

/**
 * Syntax highlighter for Picat language.
 * Defines colors and styles for different token types.
 */
class PicatSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return BnfLexer()//TODO
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        // Check for bad characters first
        if (isBadCharacter(tokenType)) {
            return pack(BAD_CHARACTER_ATTR) // Renamed to avoid conflict
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
            // isBasicModuleFunction(tokenType) -> pack(BASIC_MODULE_FUNCTION) // Removed for now
            isIdentifier(tokenType) -> pack(IDENTIFIER)
            else -> TextAttributesKey.EMPTY_ARRAY
        }
    }

    // Helper methods to categorize token types
    private fun isBadCharacter(tokenType: IElementType): Boolean =
        tokenType == TokenType.BAD_CHARACTER

    private fun isKeyword(tokenType: IElementType): Boolean =
        tokenType in KEYWORDS_SET

    private fun isComment(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.COMMENT ||
                tokenType == PicatTokenTypes.MULTI_LINE_COMMENT

    private fun isString(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.STRING

    private fun isNumber(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.INTEGER ||
                tokenType == PicatTokenTypes.FLOAT ||
                tokenType == PicatTokenTypes.HEX_INTEGER ||
                tokenType == PicatTokenTypes.OCTAL_INTEGER ||
                tokenType == PicatTokenTypes.BINARY_INTEGER


    private fun isOperator(tokenType: IElementType): Boolean =
        tokenType in OPERATORS_SET

    private fun isParenthesis(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LPAR || tokenType == PicatTokenTypes.RPAR

    private fun isBrace(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACE || tokenType == PicatTokenTypes.RBRACE

    private fun isBracket(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACKET || tokenType == PicatTokenTypes.RBRACKET

    private fun isVariable(tokenType: IElementType): Boolean = // Commenting out due to VARIABLE ambiguity for now
        tokenType == PicatTokenTypes.VARIABLE || tokenType == PicatTokenTypes.ANONYMOUS_VARIABLE

    private fun isIdentifier(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.IDENTIFIER || tokenType == PicatTokenTypes.QUOTED_ATOM

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

        val BAD_CHARACTER_ATTR =
            TextAttributesKey.createTextAttributesKey("PICAT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        val KEYWORDS_SET: TokenSet = TokenSet.create(
            PicatTokenTypes.AND_KEYWORD,
            PicatTokenTypes.BREAK_KEYWORD,
            PicatTokenTypes.CASE_KEYWORD,
            PicatTokenTypes.CATCH_KEYWORD,
            PicatTokenTypes.CONTINUE_KEYWORD,
            PicatTokenTypes.DIV_KEYWORD,
            PicatTokenTypes.DO_KEYWORD,
            PicatTokenTypes.ELSEIF_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD,
            PicatTokenTypes.END_KEYWORD,
            PicatTokenTypes.END_MODULE_KEYWORD,
            PicatTokenTypes.EXPORT_KEYWORD,
            PicatTokenTypes.FAIL_KEYWORD,
            PicatTokenTypes.FALSE_KEYWORD,
            PicatTokenTypes.FINALLY_KEYWORD,
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.IF_KEYWORD,
            PicatTokenTypes.IMPORT_KEYWORD,
            PicatTokenTypes.INCLUDE_KEYWORD,
            PicatTokenTypes.INDEX_KEYWORD,
            PicatTokenTypes.IN_KEYWORD,
            PicatTokenTypes.IS_KEYWORD,
            PicatTokenTypes.LOOP_KEYWORD,
            PicatTokenTypes.MODULE_KEYWORD,
            PicatTokenTypes.MOD_KEYWORD,
            PicatTokenTypes.NOT_KEYWORD,
            PicatTokenTypes.OF_KEYWORD,
            PicatTokenTypes.OR_KEYWORD,
            PicatTokenTypes.PASS_KEYWORD,
            PicatTokenTypes.PRIVATE_KEYWORD,
            PicatTokenTypes.REM_KEYWORD,
            PicatTokenTypes.RETURN_KEYWORD,
            PicatTokenTypes.TABLE_KEYWORD,
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.THROW_KEYWORD,
            PicatTokenTypes.TRUE_KEYWORD,
            PicatTokenTypes.TRY_KEYWORD,
            PicatTokenTypes.USING_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD,
            PicatTokenTypes.XOR_KEYWORD
        )

        val OPERATORS_SET: TokenSet = TokenSet.create(
            PicatTokenTypes.AMPERSAND,
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.ASSIGN_OP,
            PicatTokenTypes.AT,
            PicatTokenTypes.BACKSLASH,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP,
            PicatTokenTypes.BICONDITIONAL_OP,
            PicatTokenTypes.CARET,
            PicatTokenTypes.COLON,
            PicatTokenTypes.COMMA,
            PicatTokenTypes.CONCAT_OP,
            PicatTokenTypes.CUT,
            PicatTokenTypes.DIVIDE,
            PicatTokenTypes.DOT,
            PicatTokenTypes.EQUAL,
            PicatTokenTypes.GREATER,
            PicatTokenTypes.GREATER_EQUAL,
            PicatTokenTypes.HASH_AND_OP,
            PicatTokenTypes.HASH_ARROW_OP,
            PicatTokenTypes.HASH_BICONDITIONAL_OP,
            PicatTokenTypes.HASH_CARET_OP,
            PicatTokenTypes.HASH_OR_OP,
            PicatTokenTypes.HASH_TILDE_OP,
            PicatTokenTypes.IDENTICAL,
            PicatTokenTypes.INT_DIVIDE,
            PicatTokenTypes.LESS,
            PicatTokenTypes.LESS_EQUAL,
            PicatTokenTypes.MINUS,
            PicatTokenTypes.MULTIPLY,
            PicatTokenTypes.NOT_EQUAL,
            PicatTokenTypes.NOT_IDENTICAL,
            PicatTokenTypes.PIPE,
            PicatTokenTypes.PLUS,
            PicatTokenTypes.POWER,
            PicatTokenTypes.RANGE_OP,
            PicatTokenTypes.RULE_OP,
            PicatTokenTypes.SEMICOLON,
            PicatTokenTypes.SHIFT_LEFT,
            PicatTokenTypes.SHIFT_RIGHT,
            PicatTokenTypes.SHIFT_RIGHT_TRIPLE_OP
            // Note: LPAR, RPAR, LBRACE, RBRACE, LBRACKET, RBRACKET are handled separately
        )
    }
}
