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
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatItem_::class.java)
    }

    fun getModuleDeclList(): List<PicatModuleDecl> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatModuleDecl::class.java)
    }

    fun getGeneralDirectiveList(): List<PicatGeneralDirective> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatGeneralDirective::class.java)
    }

    fun getPredicateClauseList(): List<PicatPredicateClause> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPredicateClause::class.java)
    }

    fun getFunctionClauseList(): List<PicatFunctionClause> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatFunctionClause::class.java)
    }

    fun getActorDefinitionList(): List<PicatActorDefinition> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatActorDefinition::class.java)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is PicatVisitor) {
            // PicatVisitor should have visitPicatFileContent(PicatPicatFileContent o)
            // as picat_file_content is a public rule defining this structure.
            visitor.visitPicatFileContent(this)
        } else {
            super.accept(visitor)
        }
    }

    fun getPredicateFacts(): List<PicatPredicateFact> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPredicateFact::class.java)
    }

    fun getFunctionFacts(): List<PicatFunctionFact> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatFunctionFact::class.java)
    }

    fun getRules(): List<PicatPredicateRule> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPredicateRule::class.java)
    }

    fun getFunctions(): List<PicatFunctionRule> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatFunctionRule::class.java)
    }
}
