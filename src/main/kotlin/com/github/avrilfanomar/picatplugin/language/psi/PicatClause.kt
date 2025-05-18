package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat clause PSI elements.
 */
interface PicatClause : PsiElement {
    /**
     * Returns the expression of the clause.
     */
    fun getExpression(): PicatExpression?
}