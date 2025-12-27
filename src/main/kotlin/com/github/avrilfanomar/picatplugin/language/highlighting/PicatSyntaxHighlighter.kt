package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.parser._PicatLexer
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.lexer.FlexAdapter

/**
 * Syntax highlighter for Picat language.
 * Defines colors and styles for different token types.
 */
class PicatSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_PicatLexer())
    }

    @Suppress("CyclomaticComplexMethod")
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        // Check for bad characters first
        if (isBadCharacter(tokenType)) {
            return pack(BAD_CHARACTER_ATTR) // Renamed to avoid conflict
        }

        // Check token categories in order
        return when {
            isKeyword(tokenType) -> pack(KEYWORD)
            isBoolean(tokenType) -> pack(KEYWORD) // Highlight boolean literals like keywords
            isComment(tokenType) -> pack(COMMENT)
            isString(tokenType) -> pack(STRING)
            isNumber(tokenType) -> pack(NUMBER)
            isConstraintOperator(tokenType) -> pack(CONSTRAINT_OPERATOR) // Check before regular operators
            isOperator(tokenType) -> pack(OPERATOR)
            isParenthesis(tokenType) -> pack(PARENTHESES)
            isBrace(tokenType) -> pack(BRACES)
            isBracket(tokenType) -> pack(BRACKETS)
            isVariable(tokenType) -> pack(VARIABLE)
            isIdentifier(tokenType) -> pack(IDENTIFIER)
            else -> TextAttributesKey.EMPTY_ARRAY
        }
    }

    // Helper methods to categorize token types
    private fun isBadCharacter(tokenType: IElementType): Boolean =
        tokenType == TokenType.BAD_CHARACTER

    private fun isKeyword(tokenType: IElementType): Boolean =
        tokenType in KEYWORDS_SET

    private fun isBoolean(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.TRUE || tokenType == PicatTokenTypes.FALSE

    private fun isComment(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.COMMENT || tokenType == PicatTokenTypes.MULTILINE_COMMENT

    private fun isString(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.STRING || tokenType == PicatTokenTypes.SINGLE_QUOTED_ATOM

    private fun isNumber(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.INTEGER ||
                tokenType == PicatTokenTypes.FLOAT


    private fun isConstraintOperator(tokenType: IElementType): Boolean =
        tokenType in CONSTRAINT_OPERATORS_SET

    private fun isOperator(tokenType: IElementType): Boolean =
        tokenType in OPERATORS_SET

    private fun isParenthesis(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LPAR || tokenType == PicatTokenTypes.RPAR

    private fun isBrace(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACE || tokenType == PicatTokenTypes.RBRACE

    private fun isBracket(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.LBRACKET || tokenType == PicatTokenTypes.RBRACKET

    private fun isVariable(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.VARIABLE

    private fun isIdentifier(tokenType: IElementType): Boolean =
        tokenType == PicatTokenTypes.IDENTIFIER

    /** Companion object containing text attribute keys and token sets for syntax highlighting. */
    companion object {
        /** Text attributes for Picat keywords. */
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("PICAT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        /** Text attributes for Picat comments. */
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("PICAT_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        /** Text attributes for Picat strings. */
        val STRING = TextAttributesKey.createTextAttributesKey("PICAT_STRING", DefaultLanguageHighlighterColors.STRING)
        /** Text attributes for Picat numbers. */
        val NUMBER = TextAttributesKey.createTextAttributesKey("PICAT_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        /** Text attributes for Picat operators. */
        val OPERATOR =
            TextAttributesKey.createTextAttributesKey("PICAT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        /** Text attributes for Picat parentheses. */
        val PARENTHESES =
            TextAttributesKey.createTextAttributesKey("PICAT_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        /** Text attributes for Picat braces. */
        val BRACES = TextAttributesKey.createTextAttributesKey("PICAT_BRACES", DefaultLanguageHighlighterColors.BRACES)
        /** Text attributes for Picat brackets. */
        val BRACKETS =
            TextAttributesKey.createTextAttributesKey("PICAT_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        /** Text attributes for Picat identifiers. */
        val IDENTIFIER =
            TextAttributesKey.createTextAttributesKey("PICAT_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        /** Text attributes for Picat variables. */
        val VARIABLE =
            TextAttributesKey.createTextAttributesKey("PICAT_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)

        /** Text attributes for bad characters. */
        val BAD_CHARACTER_ATTR =
            TextAttributesKey.createTextAttributesKey("PICAT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        /** Text attributes for Picat constraint operators (CLP/FD). */
        val CONSTRAINT_OPERATOR =
            TextAttributesKey.createTextAttributesKey(
                "PICAT_CONSTRAINT_OPERATOR",
                DefaultLanguageHighlighterColors.KEYWORD
            )

        /** Token set containing all Picat keywords. */
        val KEYWORDS_SET: TokenSet = TokenSet.create(
            PicatTokenTypes.MODULE_KEYWORD,
            PicatTokenTypes.IMPORT_KEYWORD,
            PicatTokenTypes.INCLUDE_KEYWORD,
            PicatTokenTypes.PRIVATE_KEYWORD,
            PicatTokenTypes.TABLE_KEYWORD,
            PicatTokenTypes.INDEX_KEYWORD,
            PicatTokenTypes.IF_KEYWORD,
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.ELSEIF_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD,
            PicatTokenTypes.END_KEYWORD,
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.IN_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD,
            PicatTokenTypes.LOOP_KEYWORD,
            PicatTokenTypes.TRY_KEYWORD,
            PicatTokenTypes.CATCH_KEYWORD,
            PicatTokenTypes.FINALLY_KEYWORD,
            PicatTokenTypes.NOT_KEYWORD,
            PicatTokenTypes.DIV_KEYWORD,
            PicatTokenTypes.MOD_KEYWORD,
            PicatTokenTypes.REM_KEYWORD,
            PicatTokenTypes.LAMBDA_KEYWORD,
            PicatTokenTypes.CARDINALITY_KEYWORD,
            PicatTokenTypes.FAIL_KEYWORD,
            PicatTokenTypes.REPEAT_KEYWORD,
            PicatTokenTypes.UNTIL_KEYWORD,
            PicatTokenTypes.ONCE_KEYWORD,
            PicatTokenTypes.TRUE,
            PicatTokenTypes.FALSE
        )

        /** Token set containing all Picat operators (excluding constraint operators). */
        val OPERATORS_SET: TokenSet = TokenSet.create(
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.ASSIGN_OP,
            PicatTokenTypes.AT,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
            PicatTokenTypes.BICONDITIONAL_OP,
            PicatTokenTypes.COLON,
            PicatTokenTypes.COMMA,
            PicatTokenTypes.CONCAT_OP,
            PicatTokenTypes.DIVIDE,
            PicatTokenTypes.DOT,
            PicatTokenTypes.EQUAL,
            PicatTokenTypes.GREATER,
            PicatTokenTypes.GREATER_EQUAL,
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
            PicatTokenTypes.SEMICOLON,
            PicatTokenTypes.SHIFT_LEFT,
            PicatTokenTypes.SHIFT_RIGHT,
            PicatTokenTypes.AND_AND,
            PicatTokenTypes.OR_OR,
            PicatTokenTypes.UNIV_OP,
            PicatTokenTypes.BACKSLASH_PLUS
            // Note: LPAR, RPAR, LBRACE, RBRACE, LBRACKET, RBRACKET are handled separately
            // Note: Constraint operators are in CONSTRAINT_OPERATORS_SET
        )

        /** Token set containing Picat constraint operators (CLP/FD). */
        val CONSTRAINT_OPERATORS_SET: TokenSet = TokenSet.create(
            // Arithmetic constraint operators
            PicatTokenTypes.HASH_EQUAL_OP,           // #=
            PicatTokenTypes.HASH_NOT_EQUAL_OP,       // #!=
            PicatTokenTypes.HASH_LESS_OP,            // #<
            PicatTokenTypes.HASH_GREATER_OP,         // #>
            PicatTokenTypes.HASH_LESS_EQUAL_OP,      // #=<
            PicatTokenTypes.HASH_LESS_EQUAL_ALT_OP,  // #<=
            PicatTokenTypes.HASH_GREATER_EQUAL_OP,   // #>=
            // Boolean constraint operators
            PicatTokenTypes.HASH_NOT_OP,             // #~
            PicatTokenTypes.HASH_AND_OP,             // #/\
            PicatTokenTypes.HASH_XOR_OP,             // #^
            PicatTokenTypes.HASH_OR_OP,              // #\/
            PicatTokenTypes.HASH_ARROW_OP,           // #=>
            PicatTokenTypes.HASH_BICONDITIONAL_OP,   // #<=>
            // Domain constraint operators
            PicatTokenTypes.DOUBLE_COLON_OP,         // ::
            PicatTokenTypes.NOT_IN_KEYWORD           // notin
        )
    }
}
