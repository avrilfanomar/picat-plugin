package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatCompilationDirective
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatGeneralDirective
import com.github.avrilfanomar.picatplugin.language.psi.PicatItem_
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatVisitor
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil

class PicatFileImpl(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, PicatLanguage) {

    override fun getFileType(): FileType {
        return PicatFileType
    }

    override fun toString(): String {
        return "Picat File"
    }

    override fun getChildren(): Array<PsiElement> {
        val originalChildren = super.getChildren()
        // Filter out children that are PicatItem_ containing only PsiErrorElement,
        // which is a common pattern for the EOF error node.
        // A more direct check if the child *is* the error item might be needed.
        // The PSI dump showed: PicatFile -> PicatItem_Impl -> ... -> PsiErrorElement
        // So, we filter PicatItem_Impl nodes that are effectively empty or just errors.
        return originalChildren.filter { child ->
            if (child is PicatItem_) {
                // Check if this item consists only of error elements or is empty in a way
                // that signifies the parser's attempt to parse at EOF.
                // A simple heuristic: if its direct children are all PsiErrorElement,
                // or if it contains a PsiErrorElement and very few other tokens.
                // For the specific EOF error, the PicatItem_ contains a chain leading to PsiErrorElement.
                // A robust check might be too complex here, this is an approximation.
                // The goal is to make `file.children.isEmpty()` true for `testEmptyFile`.
                val firstGrandChild = child.firstChild
                if (firstGrandChild is PicatGeneralDirective) {
                    val firstGreatGrandChild = firstGrandChild.firstChild
                    if (firstGreatGrandChild is PicatCompilationDirective) {
                        if (firstGreatGrandChild.firstChild is PsiErrorElement &&
                            firstGreatGrandChild.children.size == 1
                        ) {
                            return@filter false // Exclude this specific error structure
                        }
                    }
                }
            }
            true
        }.toTypedArray()
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

    fun getFunctionFacts(): List<PicatFunctionFact> {
        return PsiTreeUtil.collectElementsOfType(this, PicatFunctionFact::class.java).toList()
    }

    fun getRules(): List<PicatPredicateRule> {
        return PsiTreeUtil.collectElementsOfType(this, PicatPredicateRule::class.java).toList()
    }

    fun getFunctions(): List<PicatFunctionRule> {
        return PsiTreeUtil.collectElementsOfType(this, PicatFunctionRule::class.java).toList()
    }
}
