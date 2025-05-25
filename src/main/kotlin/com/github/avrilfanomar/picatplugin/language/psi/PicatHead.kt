package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat head PSI elements.
 * A head in Picat is a structure or an atom without arguments that appears at the beginning of a rule or fact.
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
     * @deprecated Use getAtomNoArgs() instead.
     */
    @Deprecated("Use getAtomNoArgs() instead", ReplaceWith("getAtomNoArgs()?.getAtom()"))
    fun getAtom(): PicatAtom?

    /**
     * Returns the atom_no_args of the head, if it's an atom_no_args.
     */
    fun getAtomNoArgs(): PicatAtomNoArgs?

    /**
     * Returns the structure of the head, if it's a structure.
     */
    fun getStructure(): PicatStructure?
}
