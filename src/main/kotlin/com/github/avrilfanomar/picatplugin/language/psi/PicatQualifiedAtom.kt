package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat qualified atom PSI element.
 * Represents: module_atom DOT_OP name_atom
 */
interface PicatQualifiedAtom : PicatPsiElement {
    fun getModuleAtom(): PsiElement? // This would be an IDENTIFIER or QUOTED_ATOM token
    fun getDotOperator(): PsiElement?
    fun getNameAtom(): PsiElement?   // This would be an IDENTIFIER or QUOTED_ATOM token

    // Optional: Helper to get string names
    // fun getModuleName(): String?
    // fun getAtomName(): String?
}
