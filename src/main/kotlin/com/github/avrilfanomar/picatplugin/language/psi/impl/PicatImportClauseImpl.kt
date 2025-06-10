package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportList
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatImportClauseImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatImportClause {
    override fun getImportKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.IMPORT_KEYWORD)
    }

    override fun getImportList(): PicatImportList? {
        return findChildByType(PicatTokenTypes.IMPORT_LIST)
    }
}
