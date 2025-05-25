package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat module name PSI elements.
 * A module name in Picat is the name of a module that can be imported.
 */
interface PicatModuleName : PicatPsiElement {
    /**
     * Returns the name of the module.
     */
    fun getName(): String?

    /**
     * Returns the identifier of the module.
     */
    fun getIdentifier(): PsiElement?
}
