package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat module name PSI elements.
 */
interface PicatModuleName : PsiElement {
    /**
     * Returns the identifier of the module name.
     */
    fun getIdentifier(): PicatIdentifier?
}