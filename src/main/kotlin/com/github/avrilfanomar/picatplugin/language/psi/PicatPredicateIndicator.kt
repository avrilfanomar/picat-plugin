package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat predicate indicator PSI elements.
 * A predicate indicator in Picat is a name/arity pair that identifies a predicate.
 */
interface PicatPredicateIndicator : PsiNamedElement {
    /**
     * Returns the name of the predicate.
     */
    override fun getName(): String?

    /**
     * Returns the arity of the predicate.
     */
    fun getArity(): Int

    /**
     * Returns the atom element representing the predicate name.
     */
    fun getAtom(): PicatAtom?

    /**
     * Returns the integer element representing the predicate arity.
     */
    fun getArityElement(): PsiElement?
}