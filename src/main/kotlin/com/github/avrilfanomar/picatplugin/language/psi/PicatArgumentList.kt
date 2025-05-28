package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat argument list PSI elements.
 */
interface PicatArgumentList : PsiElement {
    /**
     * Returns the arguments in the list.
     */
    fun getArguments(): List<PicatArgument>
}
