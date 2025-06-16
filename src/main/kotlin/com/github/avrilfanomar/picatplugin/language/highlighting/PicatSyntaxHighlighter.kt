package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexerAdapter
// import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes // Potentially ambiguous
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

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
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.COMMENT || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.MULTI_LINE_COMMENT

    private fun isString(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.STRING

    private fun isNumber(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.INTEGER || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.FLOAT ||
            tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HEX_INTEGER || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.OCTAL_INTEGER ||
            tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BINARY_INTEGER


    private fun isOperator(tokenType: IElementType): Boolean =
        tokenType in OPERATORS_SET

    private fun isParenthesis(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LPAR || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RPAR

    private fun isBrace(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LBRACE || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RBRACE

    private fun isBracket(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LBRACKET || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RBRACKET

    private fun isVariable(tokenType: IElementType): Boolean = // Commenting out due to VARIABLE ambiguity for now
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.VARIABLE || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ANONYMOUS_VARIABLE
        // false // Temporarily disable to avoid ambiguity

//    private fun isBasicModuleFunction(tokenType: IElementType): Boolean = // Removed for now
//        tokenType == PicatTokenTypes.BASIC_MODULE_FUNCTION

    private fun isIdentifier(tokenType: IElementType): Boolean =
        tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IDENTIFIER || tokenType == com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.QUOTED_ATOM

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
//        val BASIC_MODULE_FUNCTION = TextAttributesKey.createTextAttributesKey( // Removed for now
//            "PICAT_BASIC_MODULE_FUNCTION",
//            DefaultLanguageHighlighterColors.INSTANCE_METHOD
//        )
        // Renamed BAD_CHARACTER to BAD_CHARACTER_ATTR to avoid conflict with potential class name
        val BAD_CHARACTER_ATTR =
            TextAttributesKey.createTextAttributesKey("PICAT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        val KEYWORDS_SET: TokenSet = TokenSet.create(
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.AND_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BREAK_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CASE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CATCH_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CONTINUE_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.DIV_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.DO_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ELSEIF_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ELSE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.END_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.END_MODULE_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.EXPORT_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.FAIL_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.FALSE_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.FINALLY_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.FOREACH_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IF_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IMPORT_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.INCLUDE_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.INDEX_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IN_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IS_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LOOP_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.MODULE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.MOD_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.NOT_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.OF_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.OR_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.PASS_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.PRIVATE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.REM_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RETURN_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.TABLE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.THEN_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.THROW_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.TRUE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.TRY_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.USING_KEYWORD, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.WHILE_KEYWORD,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.XOR_KEYWORD
        )

        val OPERATORS_SET: TokenSet = TokenSet.create(
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.AMPERSAND, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ARROW_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ASSIGN_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.AT, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BACKSLASH, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.BICONDITIONAL_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CARET, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.COLON, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.COMMA, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CONCAT_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.CUT, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.DIVIDE, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.DOT, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.EQUAL,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.GREATER, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.GREATER_EQUAL, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_AND_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_ARROW_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_BICONDITIONAL_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_CARET_OP,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_OR_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HASH_TILDE_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.IDENTICAL,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.INT_DIVIDE, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LESS, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.LESS_EQUAL,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.MINUS, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.MULTIPLY, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.NOT_EQUAL, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.NOT_IDENTICAL,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.PIPE, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.PIPE_CHOICE, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.PLUS, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.POWER,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RANGE_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.RULE_OP, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.SEMICOLON,
            com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.SHIFT_LEFT, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.SHIFT_RIGHT, com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.SHIFT_RIGHT_TRIPLE_OP
            // Note: LPAR, RPAR, LBRACE, RBRACE, LBRACKET, RBRACKET are handled separately
        )
    }
}
