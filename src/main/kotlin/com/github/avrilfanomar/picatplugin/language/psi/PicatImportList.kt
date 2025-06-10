package com.github.avrilfanomar.picatplugin.language.psi

interface PicatImportList : PicatPsiElement {
    fun getImportItemList(): List<PicatImportItem>
}
