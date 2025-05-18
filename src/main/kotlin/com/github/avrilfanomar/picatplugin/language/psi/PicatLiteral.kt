package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat literal PSI elements.
 */
interface PicatLiteral : PsiElement {
    /**
     * Returns the integer value of the literal, if it's an integer.
     */
    fun getInteger(): PsiElement?
    
    /**
     * Returns the float value of the literal, if it's a float.
     */
    fun getFloat(): PsiElement?
    
    /**
     * Returns the string value of the literal, if it's a string.
     */
    fun getString(): PsiElement?
    
    /**
     * Returns the atom of the literal, if it's an atom.
     */
    fun getAtom(): PicatAtom?
}