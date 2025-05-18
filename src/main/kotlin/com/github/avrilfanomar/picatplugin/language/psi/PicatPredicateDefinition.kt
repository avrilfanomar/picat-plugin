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
     * Returns the head of the predicate.
     */
    fun getHead(): PicatPredicateHead?
    
    /**
     * Returns the body of the predicate.
     */
    fun getBody(): PicatPredicateBody?
}