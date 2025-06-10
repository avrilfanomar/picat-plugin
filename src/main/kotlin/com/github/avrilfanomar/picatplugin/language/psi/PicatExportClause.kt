package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatExportClause : PicatPsiElement {
    fun getExportKeyword(): PsiElement?
    fun getExportList(): PicatExportList?
}
