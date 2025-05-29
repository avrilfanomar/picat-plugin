package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat clause PSI elements.
 * A clause in Picat consists of a head and an optional body.
 */
interface PicatClause : PicatPsiElement {
    /**
     * Returns the head of the clause.
     */
    fun getHead(): PicatPsiElement?

    /**
     * Returns the body of the clause, if any.
     */
    fun getBody(): PicatPsiElement?
}
