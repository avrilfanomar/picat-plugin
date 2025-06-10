package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatCatchClauses : PicatPsiElement {
    fun getCatchClauseList(): List<PicatCatchClause>
    // Potentially: fun getSemicolonTokens(): List<PsiElement>
}
