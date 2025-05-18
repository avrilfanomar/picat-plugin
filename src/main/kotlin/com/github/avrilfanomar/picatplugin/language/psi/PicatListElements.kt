package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat list elements PSI elements.
 */
interface PicatListElements : PsiElement {
    /**
     * Returns the expressions in the list.
     */
    fun getExpressions(): List<PicatExpression>
    
    /**
     * Returns the tail expression of the list, if any.
     */
    fun getTailExpression(): PicatExpression?
}