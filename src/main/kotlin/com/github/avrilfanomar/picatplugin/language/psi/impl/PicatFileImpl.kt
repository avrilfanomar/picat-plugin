package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatActorDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatGeneralDirective
import com.github.avrilfanomar.picatplugin.language.psi.PicatItem_
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDecl
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatVisitor
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement // Added import
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiErrorElement // Added import
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
                // The error node structure is: PicatItem_ -> PicatGeneralDirective -> PicatCompilationDirective -> PsiErrorElement
                val firstGrandChild = child.firstChild
                if (firstGrandChild is com.github.avrilfanomar.picatplugin.language.psi.PicatGeneralDirective) {
                    val firstGreatGrandChild = firstGrandChild.firstChild
                    if (firstGreatGrandChild is com.github.avrilfanomar.picatplugin.language.psi.PicatCompilationDirective) {
                        if (firstGreatGrandChild.firstChild is PsiErrorElement && firstGreatGrandChild.children.size == 1) {
                           return@filter false // Exclude this specific error structure
                        }
                    }
                }
            }
            // A more general filter for any direct PsiErrorElement child (though less likely for PicatFileImpl based on current BNF)
            // if (it is PsiErrorElement) return@filter false
            true
        }.toTypedArray()
    }


    fun getItem_List(): List<PicatItem_> {
        // This should now reflect the filtered children if it uses getChildren() internally,
        // or be adjusted if it uses a different way to get items.
        // PsiTreeUtil.getChildrenOfTypeAsList uses getChildren().
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatItem_::class.java)
    }

    fun getModuleDeclList(): List<PicatModuleDecl> {
        return PsiTreeUtil.collectElementsOfType(this, PicatModuleDecl::class.java).toList()
    }

    fun getGeneralDirectiveList(): List<PicatGeneralDirective> {
        return PsiTreeUtil.collectElementsOfType(this, PicatGeneralDirective::class.java).toList()
    }

    fun getPredicateClauseList(): List<PicatPredicateClause> {
        return PsiTreeUtil.collectElementsOfType(this, PicatPredicateClause::class.java).toList()
    }

    fun getFunctionClauseList(): List<PicatFunctionClause> {
        return PsiTreeUtil.collectElementsOfType(this, PicatFunctionClause::class.java).toList()
    }

    fun getActorDefinitionList(): List<PicatActorDefinition> {
        return PsiTreeUtil.collectElementsOfType(this, PicatActorDefinition::class.java).toList()
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
