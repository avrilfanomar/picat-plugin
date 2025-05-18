package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat function body PSI elements.
 */
interface PicatFunctionBody : PsiElement {
    /**
     * Returns the expression of the function body.
     */
    fun getExpression(): PicatExpression?
}