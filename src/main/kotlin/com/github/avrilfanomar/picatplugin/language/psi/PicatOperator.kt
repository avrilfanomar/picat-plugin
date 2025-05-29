package com.github.avrilfanomar.picatplugin.language.psi

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
