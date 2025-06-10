package com.github.avrilfanomar.picatplugin.language.psi

interface PicatImportItem : PicatPsiElement {
    fun getModuleNameElement(): PicatModuleName?
    fun getRenameList(): PicatRenameList?
}
