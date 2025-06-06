package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat for loop PSI element.
 */
interface PicatForLoop : PicatPsiElement {
    /**
     * Returns the initialization of the for loop.
     */
    fun getInitialization(): PsiElement?

    /**
     * Returns the condition of the for loop.
     */
    fun getCondition(): PsiElement?

    /**
     * Returns the increment of the for loop.
     */
    fun getIncrement(): PsiElement?

    /**
     * Returns the body of the for loop.
     */
    fun getBody(): PsiElement?
}
