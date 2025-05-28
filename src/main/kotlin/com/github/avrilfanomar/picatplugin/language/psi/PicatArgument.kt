package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat argument PSI elements.
 */
interface PicatArgument : PsiElement {
    /**
     * Returns the expression of the argument.
     */
    fun getExpression(): PicatExpression?
}
