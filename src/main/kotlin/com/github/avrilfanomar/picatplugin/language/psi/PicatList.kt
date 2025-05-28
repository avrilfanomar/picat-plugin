package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat list PSI elements.
 */
interface PicatList : PsiElement {
    /**
     * Returns the list elements, if any.
     */
    fun getListElements(): PicatListElements?
}
