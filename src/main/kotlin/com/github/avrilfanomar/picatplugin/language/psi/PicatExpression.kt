package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat expression PSI elements.
 */
interface PicatExpression : PicatPsiElement {
    /**
     * Returns the terms in the expression.
     */
    fun getTerms(): List<PicatTerm>

    /**
     * Returns the operators in the expression, if any.
     */
    fun getOperators(): List<PicatOperator>
}
