package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat function body PSI elements.
 * A function body in Picat contains the implementation of a function.
 */
interface PicatFunctionBody : PicatPsiElement {
    /**
     * Returns the expression that represents the function's return value.
     */
    fun getReturnExpression(): PicatExpression?

    /**
     * Returns the statements in the function body, if any.
     */
    fun getStatements(): List<PicatPsiElement>
}
