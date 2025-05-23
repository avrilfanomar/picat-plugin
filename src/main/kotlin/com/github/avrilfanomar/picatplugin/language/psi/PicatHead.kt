package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat head PSI elements.
 * A head in Picat is an atom or a structure that appears at the beginning of a rule or fact.
 */
interface PicatHead : PsiNamedElement {
    /**
     * Returns the name of the head.
     */
    override fun getName(): String?

    /**
     * Returns the arity of the head (number of arguments).
     */
    fun getArity(): Int

    /**
     * Returns the atom of the head, if it's an atom.
     */
    fun getAtom(): PicatAtom?

    /**
     * Returns the structure of the head, if it's a structure.
     */
    fun getStructure(): PicatStructure?
}