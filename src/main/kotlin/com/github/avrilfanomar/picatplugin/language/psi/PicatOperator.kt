package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat operator PSI elements.
 */
interface PicatOperator : PsiElement {
    /**
     * Returns the text of the operator.
     */
    fun getOperatorText(): String
}