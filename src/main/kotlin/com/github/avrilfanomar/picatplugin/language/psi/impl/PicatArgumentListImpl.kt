package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // Import PicatExpression
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatArgumentList interface.
 */
class PicatArgumentListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatArgumentList {
    /**
     * Returns the arguments (expressions) in the list.
     */
    override fun getArguments(): List<PicatExpression> {
        // The parser now makes EXPRESSION elements direct children of ARGUMENT_LIST.
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatExpression::class.java)
    }
}
