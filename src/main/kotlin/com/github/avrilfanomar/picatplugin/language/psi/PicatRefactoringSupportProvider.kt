package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

/**
 * Enables in-place rename for Picat elements that have a name identifier
 * (e.g., PicatAtom via grammar implements and mixin).
 */
class PicatRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is PsiNameIdentifierOwner && element.nameIdentifier != null
    }
}
