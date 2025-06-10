package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatExportList
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatExportClauseImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExportClause {
    override fun getExportKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.EXPORT_KEYWORD)
    }

    override fun getExportList(): PicatExportList? {
        return findChildByType(PicatTokenTypes.EXPORT_LIST)
    }
}
