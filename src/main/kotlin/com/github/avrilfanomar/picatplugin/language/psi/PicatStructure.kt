package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat structure PSI elements.
 */
interface PicatStructure : PsiElement {
    /**
     * Returns the identifier of the structure.
     */
    fun getIdentifier(): PicatIdentifier?

    /**
     * Returns the argument list of the structure, if any.
     */
    fun getArgumentList(): PicatArgumentList?

    /**
     * Returns the name of the structure.
     */
    fun getName(): String?

    /**
     * Returns the arity of the structure (number of arguments).
     */
    fun getArity(): Int
}
