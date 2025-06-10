package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatImportClause : PicatPsiElement {
    fun getImportKeyword(): PsiElement?
    fun getImportList(): PicatImportList?
}
