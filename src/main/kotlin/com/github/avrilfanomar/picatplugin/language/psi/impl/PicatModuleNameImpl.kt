package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatModuleName interface.
 */
class PicatModuleNameImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatModuleName {
    /**
     * Returns the identifier of the module.
     */
    override fun getIdentifier(): PsiElement? {
        return firstChild
    }
}
