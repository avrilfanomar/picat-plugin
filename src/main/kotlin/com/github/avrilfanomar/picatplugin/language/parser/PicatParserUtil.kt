package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Utility object for Picat parser with common methods used by all parser components.
 */
object PicatParserUtil {
    /**
     * Expects a specific keyword token and advances the lexer if found, otherwise reports an error.
     */
    fun expectKeyword(builder: PsiBuilder, type: IElementType, error: String) {
        if (builder.tokenType == type) {
            builder.advanceLexer()
        } else {
            builder.error(error)
        }
    }

    /**
     * Expects a specific token and advances the lexer if found, otherwise reports an error.
     */
    fun expectToken(builder: PsiBuilder, type: IElementType, error: String) {
        if (builder.tokenType == type) {
            builder.advanceLexer()
        } else {
            builder.error(error)
        }
    }

    /**
     * Checks if the token type is an atom.
     */
    fun isAtom(type: IElementType?): Boolean =
        type == PicatTokenTypes.IDENTIFIER || type == PicatTokenTypes.QUOTED_ATOM

    /**
     * Checks if the token type is a variable.
     */
    fun isVariable(type: IElementType?): Boolean =
        type == PicatTokenTypes.VARIABLE || type == PicatTokenTypes.ANONYMOUS_VARIABLE

    /**
     * Checks if the token type is a number.
     */
    fun isNumber(type: IElementType?): Boolean =
        type == PicatTokenTypes.INTEGER || type == PicatTokenTypes.FLOAT ||
                type == PicatTokenTypes.HEX_INTEGER || type == PicatTokenTypes.OCTAL_INTEGER ||
                type == PicatTokenTypes.BINARY_INTEGER

    /**
     * Checks if the token type is a relational operator.
     */
    fun isRelationalOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.LESS || type == PicatTokenTypes.GREATER ||
                type == PicatTokenTypes.LESS_EQUAL || type == PicatTokenTypes.GREATER_EQUAL

    /**
     * Checks if the token type is a multiplicative operator.
     */
    fun isMultiplicativeOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.MULTIPLY || type == PicatTokenTypes.DIVIDE ||
                type == PicatTokenTypes.INT_DIVIDE || type == PicatTokenTypes.MOD_KEYWORD

    /**
     * Checks if the token type is a unary operator.
     */
    fun isUnaryOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.PLUS || type == PicatTokenTypes.MINUS ||
                type == PicatTokenTypes.NOT_KEYWORD || type == PicatTokenTypes.BACKSLASH

    /**
     * Checks if the token type is a rule operator.
     */
    fun isRuleOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.ARROW_OP || type == PicatTokenTypes.BACKTRACKABLE_ARROW_OP ||
                type == PicatTokenTypes.BICONDITIONAL_OP || type == PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP ||
                type == PicatTokenTypes.RULE_OP
}
