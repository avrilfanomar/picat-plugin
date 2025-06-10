package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatCatchClause : PicatPsiElement {
    fun getPattern(): PicatExpression? // Assuming pattern is parsed as an expression/pattern PSI type
    fun getArrowOperator(): PsiElement?
    fun getBody(): PicatBody?
}
