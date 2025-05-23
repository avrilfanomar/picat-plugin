package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat fact PSI elements.
 * A fact in Picat is a head followed by a dot.
 */
interface PicatFact : PicatPsiElement {
    /**
     * Returns the head of the fact.
     */
    fun getHead(): PsiElement?
}