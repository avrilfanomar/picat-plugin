package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatTryCatch : PicatPsiElement {
    fun getTryKeyword(): PsiElement?
    fun getTryBody(): PicatBody?
    fun getCatchKeyword(): PsiElement?
    fun getCatchClauses(): PicatCatchClauses?
    fun getFinallyKeyword(): PsiElement?
    fun getFinallyBody(): PicatBody? // There can be only one finally body
    fun getEndKeyword(): PsiElement?
}
