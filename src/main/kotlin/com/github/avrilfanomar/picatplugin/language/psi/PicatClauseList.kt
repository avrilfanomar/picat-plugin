package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat clause list PSI elements.
 */
interface PicatClauseList : PsiElement {
    /**
     * Returns the clauses in the list.
     */
    fun getClauses(): List<PicatClause>
}