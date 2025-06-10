package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

interface PicatCaseArms : PicatPsiElement {
    fun getArmList(): List<PicatCaseArm>
    // Potentially: fun getSemicolonTokens(): List<PsiElement>
}
