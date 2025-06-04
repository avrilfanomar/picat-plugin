package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.IElementType

/**
 * Interface for Picat operator PSI elements.
 * An operator in Picat is a symbol that performs an operation on one or more operands.
 */
interface PicatOperator : PicatPsiElement {
    /**
     * Returns the text of the operator.
     */
    fun getOperatorText(): String

    /**
     * Returns the precedence of the operator.
     * Higher precedence operators are evaluated before lower precedence operators.
     */
    fun getPrecedence(): Int
}

/**
 * Checks if the element type is a comparison operator.
 */
fun isComparisonOperator(elementType: IElementType?): Boolean {
    return elementType == PicatTokenTypes.LESS ||
            elementType == PicatTokenTypes.GREATER ||
            elementType == PicatTokenTypes.LESS_EQUAL ||
            elementType == PicatTokenTypes.GREATER_EQUAL
}

/**
 * Checks if the token type is an arithmetic operator.
 */
fun isArithmeticOperator(tokenType: IElementType?): Boolean {
    return tokenType == PicatTokenTypes.PLUS ||
            tokenType == PicatTokenTypes.MINUS ||
            tokenType == PicatTokenTypes.MULTIPLY ||
            tokenType == PicatTokenTypes.DIVIDE ||
            tokenType == PicatTokenTypes.INT_DIVIDE ||
            tokenType == PicatTokenTypes.MOD_KEYWORD
}
