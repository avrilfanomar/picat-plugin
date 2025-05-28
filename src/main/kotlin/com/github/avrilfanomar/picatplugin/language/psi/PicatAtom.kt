package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat atom PSI elements.
 */
interface PicatAtom : PsiElement {
    /**
     * Returns the identifier of the atom, if it's an identifier.
     */
    fun getIdentifier(): PicatIdentifier?

    /**
     * Returns the quoted atom, if it's a quoted atom.
     */
    fun getQuotedAtom(): PsiElement?
}
