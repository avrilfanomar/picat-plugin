package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat predicate head PSI elements.
 */
interface PicatPredicateHead : PsiElement {
    /**
     * Returns the name of the predicate.
     */
    fun getName(): String?
    
    /**
     * Returns the arity of the predicate (number of arguments).
     */
    fun getArity(): Int
    
    /**
     * Returns the argument list of the predicate, if any.
     */
    fun getArgumentList(): PicatArgumentList?
}