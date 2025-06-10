package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat loop-while statement PSI element.
 * Represents: LOOP_KEYWORD body WHILE_KEYWORD expression EOR
 */
interface PicatLoopWhileStatement : PicatPsiElement {
    fun getLoopKeyword(): PsiElement?
    fun getBody(): PicatBody?
    fun getWhileKeyword(): PsiElement? // The one after the body
    fun getCondition(): PicatExpression?
    fun getEorToken(): PsiElement?
}
