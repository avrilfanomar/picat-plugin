package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatCaseExpression : PicatPsiElement {
    fun getCaseKeyword(): PsiElement?
    fun getExpression(): PicatExpression?
    fun getOfKeyword(): PsiElement?
    fun getArms(): PicatCaseArms?
    fun getEndKeyword(): PsiElement?
}
