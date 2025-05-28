package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatListElements
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
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
     */
    override fun getTailExpression(): PicatExpression? {
        // Check if there's a pipe token in the list
        val pipeElement = node.findChildByType(PicatTokenTypes.PIPE)
        if (pipeElement != null) {
            // The tail expression is the expression after the pipe
            // Find the expression that comes after the pipe in the AST
            val pipeIndex = node.getChildren(null).indexOf(pipeElement)
            if (pipeIndex >= 0 && pipeIndex < node.getChildren(null).size - 1) {
                // Find the next expression after the pipe
                for (i in pipeIndex + 1 until node.getChildren(null).size) {
                    val child = node.getChildren(null)[i]
                    if (child.psi is PicatExpression) {
                        return child.psi as PicatExpression
                    }
                }
            }
        }

        return null
    }
}
