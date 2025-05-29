package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat clause list PSI elements.
 * A clause list in Picat is a collection of related clauses.
 */
interface PicatClauseList : PicatPsiElement {
    /**
     * Returns the clauses in this list.
     */
    fun getClauses(): List<PicatClause>
}
