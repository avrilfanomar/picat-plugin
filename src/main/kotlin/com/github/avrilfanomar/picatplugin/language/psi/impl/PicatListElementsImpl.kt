package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatListElements
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatListElements interface.
 */
class PicatListElementsImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatListElements {
    /**
     * Returns the expressions in the list.
     */
    override fun getExpressions(): List<PicatExpression> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatExpression::class.java)
    }

    /**
     * Returns the tail expression of the list, if any.
     * The tail expression is the expression after the pipe (|) in a list.
     *
     * Note: This is a simplified implementation. In a real implementation,
     * we would need to identify which expression is the tail expression.
     */
    override fun getTailExpression(): PicatExpression? {
        // For now, we'll just return null
        // In a real implementation, we would need to identify which expression is the tail expression
        return null
    }
}