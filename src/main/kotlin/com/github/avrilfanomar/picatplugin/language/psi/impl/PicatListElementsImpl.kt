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
        val pipeElement = node.findChildByType(PicatTokenTypes.PIPE) ?: return null

        // Find the expression that comes after the pipe in the AST
        return findExpressionAfterPipe(pipeElement)
    }

    /**
     * Finds the first expression after the pipe element.
     */
    private fun findExpressionAfterPipe(pipeElement: ASTNode): PicatExpression? {
        val children = node.getChildren(null)
        val pipeIndex = children.indexOf(pipeElement)

        // Check if pipe is found and not the last element
        if (pipeIndex < 0 || pipeIndex >= children.size - 1) {
            return null
        }

        // Find the next expression after the pipe
        return findFirstExpressionInRange(children, pipeIndex + 1)
    }

    /**
     * Finds the first PicatExpression in the given range of children.
     */
    private fun findFirstExpressionInRange(children: Array<ASTNode>, startIndex: Int): PicatExpression? {
        for (i in startIndex until children.size) {
            val child = children[i]
            if (child.psi is PicatExpression) {
                return child.psi as PicatExpression
            }
        }
        return null
    }
}
