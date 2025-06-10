package com.github.avrilfanomar.picatplugin.language.psi

interface PicatRenameList : PicatPsiElement {
    fun getRenameSpecList(): List<PicatRenameSpec>
}
