package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatVisitor
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

class PicatFileImpl(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, PicatLanguage) {

    override fun getFileType(): FileType {
        return PicatFileType
    }

    override fun toString(): String {
        return "Picat"
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is PicatVisitor) {
            visitor.visitFile(this)
        } else {
            super.accept(visitor)
        }
    }

    fun getPredicateFacts(): List<PicatPredicateFact> {
        return PsiTreeUtil.collectElementsOfType(this, PicatPredicateFact::class.java).toList()
    }

    fun getRules(): List<PicatPredicateRule> {
        return PsiTreeUtil.collectElementsOfType(this, PicatPredicateRule::class.java).toList()
    }

}
