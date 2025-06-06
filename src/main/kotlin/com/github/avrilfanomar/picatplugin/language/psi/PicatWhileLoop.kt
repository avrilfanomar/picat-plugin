package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat while loop PSI element.
 */
interface PicatWhileLoop : PicatPsiElement {
    /**
     * Returns the condition of the while loop.
     */
    fun getCondition(): PsiElement?

    /**
     * Returns the body of the while loop.
     */
    fun getBody(): PsiElement?
}
