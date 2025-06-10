package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat end_module declaration PSI elements.
 * Represents: END_MODULE_KEYWORD (module_name)? EOR
 */
interface PicatEndModuleDeclaration : PicatPsiElement {
    fun getEndModuleKeyword(): PsiElement?
    fun getModuleNameElement(): PicatModuleName? // Optional
    fun getEorToken(): PsiElement?
}
