package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatFileSpec : PicatPsiElement {
    fun getStringLiteral(): PsiElement?
    fun getAtom(): PsiElement? // IDENTIFIER or QUOTED_ATOM
}
