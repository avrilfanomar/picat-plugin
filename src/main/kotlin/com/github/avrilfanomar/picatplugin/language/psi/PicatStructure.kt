package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement // Keep this if methods return raw PsiElement for tokens

/**
 * Interface for Picat structure PSI elements.
 */
interface PicatStructure : PicatPsiElement { // Changed to PicatPsiElement
    /**
     * Returns the identifier (functor) of the structure.
     */
    fun getFunctor(): PicatIdentifier? // Renamed from getIdentifier for clarity

    /**
     * Returns the argument list of the structure, if any.
     */
    fun getArgumentList(): PicatArgumentList?

    /**
     * Returns the name of the structure (functor name).
     */
    fun getName(): String? // Usually from getFunctor().getText()

    /**
     * Returns the arity of the structure (number of arguments).
     */
    fun getArity(): Int
}
