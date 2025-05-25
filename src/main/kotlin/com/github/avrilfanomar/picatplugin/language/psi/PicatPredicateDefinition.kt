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
     * Returns the head of the predicate.
     * @deprecated The predicate_head rule has been removed from the grammar.
     */
    @Deprecated("The predicate_head rule has been removed from the grammar")
    fun getHead(): PicatPredicateHead?

    /**
     * Returns the body of the predicate.
     */
    fun getBody(): PicatPredicateBody?
}
