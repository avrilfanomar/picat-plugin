package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatExportStatement interface.
 */
class PicatExportStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExportStatement {
    /**
     * Returns the export list.
     */
    override fun getExportList(): PsiElement? {
        return findChildByType(PicatTokenTypes.EXPORT_LIST)
    }
}
