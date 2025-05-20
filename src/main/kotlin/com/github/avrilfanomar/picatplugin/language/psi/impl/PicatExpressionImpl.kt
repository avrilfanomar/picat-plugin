package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatOperator
import com.github.avrilfanomar.picatplugin.language.psi.PicatTerm
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatExpression interface.
 */
class PicatExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExpression {
    /**
     * Returns the terms in the expression.
     */
    override fun getTerms(): List<PicatTerm> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatTerm::class.java)
    }
    
    /**
     * Returns the operators in the expression, if any.
     */
    override fun getOperators(): List<PicatOperator> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatOperator::class.java)
    }
}