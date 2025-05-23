package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat term PSI elements.
 */
interface PicatTerm : PicatPsiElement {
    /**
     * Returns the literal of the term, if it's a literal.
     */
    fun getLiteral(): PicatLiteral?

    /**
     * Returns the variable of the term, if it's a variable.
     */
    fun getVariable(): PicatVariable?

    /**
     * Returns the list of the term, if it's a list.
     */
    fun getList(): PicatList?

    /**
     * Returns the structure of the term, if it's a structure.
     */
    fun getStructure(): PicatStructure?

    /**
     * Returns the expression of the term, if it's a parenthesized expression.
     */
    fun getExpression(): PicatExpression?
}
