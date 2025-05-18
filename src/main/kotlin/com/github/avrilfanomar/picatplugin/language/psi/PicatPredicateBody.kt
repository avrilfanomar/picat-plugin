package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat predicate body PSI elements.
 */
interface PicatPredicateBody : PsiElement {
    /**
     * Returns the clause list of the predicate body.
     */
    fun getClauseList(): PicatClauseList?
}