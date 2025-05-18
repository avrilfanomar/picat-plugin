package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat import statement PSI elements.
 */
interface PicatImportStatement : PsiElement {
    /**
     * Returns the module name of the import statement.
     */
    fun getModuleName(): PicatModuleName?
}