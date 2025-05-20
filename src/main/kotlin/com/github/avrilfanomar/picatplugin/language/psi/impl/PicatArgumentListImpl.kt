package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatArgumentList interface.
 */
class PicatArgumentListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatArgumentList {
    /**
     * Returns the arguments in the list.
     */
    override fun getArguments(): List<PicatArgument> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatArgument::class.java)
    }
}