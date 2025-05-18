package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat module declaration PSI elements.
 */
interface PicatModuleDeclaration : PsiNamedElement {
    /**
     * Returns the name of the module.
     */
    override fun getName(): String?
    
    /**
     * Returns the module name element.
     */
    fun getModuleName(): PicatModuleName?
}