package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil // Already used

class PicatModuleDeclarationImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatModuleDeclaration {

    override fun getModuleKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.MODULE_KEYWORD)
    }

    override fun getModuleNameElement(): PicatModuleName? {
        return findChildByType(PicatTokenTypes.MODULE_NAME)
        // Or using PsiTreeUtil if preferred for class-based search:
        // return PsiTreeUtil.getChildOfType(this, PicatModuleName::class.java)
    }

    override fun getExportClause(): PicatExportClause? {
        return findChildByType(PicatTokenTypes.EXPORT_CLAUSE)
    }

    override fun getImportClause(): PicatImportClause? {
        return findChildByType(PicatTokenTypes.IMPORT_CLAUSE)
    }

    override fun getEorToken(): PsiElement? {
        return findChildByType(PicatTokenTypes.EOR)
    }

    override fun getName(): String? {
        return getModuleNameElement()?.name
    }

    override fun setName(name: String): PsiElement {
        // For refactoring support, this would involve changing the underlying ModuleName element.
        // This is often handled by PicatPsiImplUtil.setName or similar.
        // For now, keeping it simple as per original or deferring full implementation.
        getModuleNameElement()?.setName(name) // Assuming PicatModuleName handles its own renaming
        return this
    }

    // Add other methods from PicatPsiElement if needed, or ensure they are in PicatPsiElementImpl
}
