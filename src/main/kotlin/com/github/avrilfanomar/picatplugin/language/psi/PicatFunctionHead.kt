package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat function head PSI elements.
 */
interface PicatFunctionHead : PsiElement {
    /**
     * Returns the identifier of the function.
     */
    fun getIdentifier(): PicatIdentifier?

    /**
     * Returns the argument list of the function, if any.
     */
    fun getArgumentList(): PicatArgumentList?

    /**
     * Returns the name of the function.
     */
    fun getName(): String?

    /**
     * Returns the arity of the function (number of arguments).
     */
    fun getArity(): Int
}