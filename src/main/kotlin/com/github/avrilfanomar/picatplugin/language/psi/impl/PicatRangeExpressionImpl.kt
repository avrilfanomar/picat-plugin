package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatRangeExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatTerm
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatRangeExpression interface.
 */
class PicatRangeExpressionImpl(node: ASTNode) : ASTWrapperPsiElement(node), PicatRangeExpression {
    /**
     * Returns the left term of the range expression.
     */
    override fun getLeft(): PicatTerm? {
        // The first term in the range expression
        val terms = PsiTreeUtil.getChildrenOfType(this, PicatTerm::class.java)
        return terms?.firstOrNull()
    }

    /**
     * Returns the right term of the range expression, if any.
     * If this is not a range expression (i.e., there is no ".." operator),
     * this method returns null.
     */
    override fun getRight(): PicatTerm? {
        // Check if there's a range operator in the expression
        val rangeOp = node.findChildByType(PicatTokenTypes.RANGE)
        if (rangeOp != null) {
            // If there is a range operator, the right term is the second term
            val terms = PsiTreeUtil.getChildrenOfType(this, PicatTerm::class.java)
            return if (terms != null && terms.size > 1) terms[1] else null
        }
        return null
    }
}