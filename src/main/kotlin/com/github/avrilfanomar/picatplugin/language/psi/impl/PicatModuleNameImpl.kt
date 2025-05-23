package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatModuleName interface.
 */
class PicatModuleNameImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatModuleName {
    /**
     * Returns the identifier of the module name.
     */
    override fun getIdentifier(): PicatIdentifier? {
        return PsiTreeUtil.getChildOfType(this, PicatIdentifier::class.java)
    }
}