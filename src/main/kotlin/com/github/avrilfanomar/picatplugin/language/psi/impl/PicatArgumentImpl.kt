package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatArgument interface.
 */
class PicatArgumentImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatArgument {
    /**
     * Returns the expression of the argument.
     */
    override fun getExpression(): PicatExpression? {
        return PsiTreeUtil.getChildOfType(this, PicatExpression::class.java)
    }
}