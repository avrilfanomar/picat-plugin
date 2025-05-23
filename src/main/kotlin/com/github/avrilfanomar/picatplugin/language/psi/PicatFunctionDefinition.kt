package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat function definition PSI elements.
 */
interface PicatFunctionDefinition : PicatPsiElement {
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
