package com.github.avrilfanomar.picatplugin.language.psi

interface PicatExportList : PicatPsiElement {
    fun getExportSpecList(): List<PicatExportSpec>
}
