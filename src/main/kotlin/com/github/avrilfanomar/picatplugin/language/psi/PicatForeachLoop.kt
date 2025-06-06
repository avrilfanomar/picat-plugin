package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat foreach loop PSI element.
 */
interface PicatForeachLoop : PicatPsiElement {
    /**
     * Returns the generators of the foreach loop.
     */
    fun getGenerators(): PsiElement?

    /**
     * Returns the body of the foreach loop.
     */
    fun getBody(): PsiElement?
}
