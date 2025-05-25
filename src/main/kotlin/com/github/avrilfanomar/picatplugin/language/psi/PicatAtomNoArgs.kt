package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat atom_no_args PSI elements.
 * An atom_no_args is an atom that is not followed by a left parenthesis (i.e., an atom without arguments).
 */
interface PicatAtomNoArgs : PsiElement {
    /**
     * Returns the atom of the atom_no_args.
     */
    fun getAtom(): PicatAtom?
}