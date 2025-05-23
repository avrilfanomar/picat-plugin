package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatImportStatement interface.
 */
class PicatImportStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatImportStatement {
    /**
     * Returns the module name of the import statement.
     */
    override fun getModuleName(): PicatModuleName? {
        return PsiTreeUtil.getChildOfType(this, PicatModuleName::class.java)
    }
}