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
import com.github.avrilfanomar.picatplugin.language.psi.PicatPicatFileContent
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatVisitor
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

// This class is the root of the PSI tree for a Picat file.
// It implements PicatPicatFileContent, which is the interface generated from the `picat_file_content` rule in the BNF.
class PicatFileImpl(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, PicatLanguage),
    PicatPicatFileContent {

    override fun getFileType(): FileType {
        return PicatFileType
    }

    override fun toString(): String {
        return "Picat File"
    }

    // Implementation for PicatPicatFileContent interface (from 'picat_file_content ::= item_*')
    override fun getItem_List(): List<PicatItem_> {
        // Children should be PicatItem_ if item_ has an elementType=ITEM.
        // If item_ is transparent, children might be PicatPredicateClause, etc.
        // This method's correctness depends on how item_ is defined and if PicatItem_ PSI nodes are created.
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
            // PicatVisitor should have visitPicatFileContent(PicatPicatFileContent o)
            // as picat_file_content is a public rule defining this structure.
            visitor.visitPicatFileContent(this) // Reverted to visitPicatFileContent
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
