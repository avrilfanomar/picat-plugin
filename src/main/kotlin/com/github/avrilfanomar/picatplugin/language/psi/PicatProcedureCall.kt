package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat procedure call PSI elements.
 * A procedure call can be a simple atom (e.g., `nl.`) or a structure (e.g., `write(X).`).
 */
interface PicatProcedureCall : PicatPsiElement, PsiNamedElement { // PsiNamedElement for the call's name
    /**
     * Returns the core expression of the call, which should be an atom or a structure.
     * This is the element that holds the name and arguments.
     */
    fun getCoreExpression(): PicatExpression? // Could be PicatAtom, PicatStructure, or PicatQualifiedAtom

    /**
     * Helper to get the name identifier (atom/functor) of the call.
     */
    fun getNameIdentifier(): PsiElement?

    /**
     * Helper to get the argument list if this call is a structure.
     */
    fun getArgumentList(): PicatArgumentList?

    // getName() is inherited from PsiNamedElement
}
