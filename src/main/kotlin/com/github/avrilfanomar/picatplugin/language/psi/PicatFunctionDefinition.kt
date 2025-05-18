package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat function definition PSI elements.
 */
interface PicatFunctionDefinition : PsiElement {
    /**
     * Returns the name of the function.
     */
    fun getName(): String?
    
    /**
     * Returns the arity of the function (number of arguments).
     */
    fun getArity(): Int
    
    /**
     * Returns the head of the function.
     */
    fun getHead(): PicatFunctionHead?
    
    /**
     * Returns the body of the function.
     */
    fun getBody(): PicatFunctionBody?
}