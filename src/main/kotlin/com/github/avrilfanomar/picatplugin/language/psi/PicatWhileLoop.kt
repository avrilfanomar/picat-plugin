package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat while loop PSI element.
 * Represents: WHILE_KEYWORD expression DO_KEYWORD body END_KEYWORD
 */
interface PicatWhileLoop : PicatPsiElement {
    fun getWhileKeyword(): PsiElement?
    fun getCondition(): PicatExpression?
    fun getDoKeyword(): PsiElement?
    fun getBody(): PicatBody?
    fun getEndKeyword(): PsiElement?
}
