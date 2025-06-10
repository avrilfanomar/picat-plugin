package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatRenameSpec : PicatPsiElement {
    fun getOriginalAtom(): PsiElement? // The first atom (IDENTIFIER or QUOTED_ATOM)
    fun getArrowOperator(): PsiElement?
    fun getNewAtom(): PsiElement?      // The second atom (IDENTIFIER or QUOTED_ATOM), if present
}
