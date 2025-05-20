package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Base interface for all Picat PSI elements.
 * This interface is extended by all specific Picat PSI element interfaces.
 */
interface PicatPsiElement : PsiElement {
    /**
     * Returns the text representation of this element.
     */
    override fun getText(): String
}