package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat import statement PSI elements.
 * An import statement in Picat imports modules to be used by the current module.
 */
interface PicatImportStatement : PicatPsiElement {
    /**
     * Returns the list of module names in the import statement.
     */
    fun getModuleNames(): List<PicatModuleName>

    /**
     * Returns the module list.
     */
    fun getModuleList(): PsiElement?
}
