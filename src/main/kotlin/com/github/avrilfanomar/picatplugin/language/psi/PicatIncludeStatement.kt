package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat include statement PSI elements.
 * An include statement in Picat includes a file into the current module.
 */
interface PicatIncludeStatement : PicatPsiElement {
    /**
     * Returns the path of the included file.
     */
    fun getIncludePath(): String?
    
    /**
     * Returns the string literal element representing the include path.
     */
    fun getStringLiteral(): PsiElement?
}