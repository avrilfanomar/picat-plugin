package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat predicate definition PSI elements.
 */
interface PicatPredicateDefinition : PsiElement {
    /**
     * Returns the name of the predicate.
     */
    fun getName(): String?

    /**
     * Returns the arity of the predicate (number of arguments).
     */
    fun getArity(): Int

    /**
     * Returns the atom of the predicate.
     */
    fun getAtom(): PicatAtom?

    /**
     * Returns the argument list of the predicate, if any.
     */
    fun getArgumentList(): PicatArgumentList?


    /**
     * Returns the body of the predicate.
     */
    fun getBody(): PicatPredicateBody?
}
