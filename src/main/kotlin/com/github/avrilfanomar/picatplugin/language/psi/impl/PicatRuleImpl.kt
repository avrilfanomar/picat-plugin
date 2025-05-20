package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatRule interface.
 */
class PicatRuleImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatRule {
    /**
     * Returns the head of the rule.
     */
    override fun getHead(): PsiElement? {
        return findChildByType(PicatTokenTypes.STRUCTURE) ?: findChildByType(PicatTokenTypes.ATOM)
    }

    /**
     * Returns the body of the rule.
     */
    override fun getBody(): PsiElement? {
        return findChildByType(PicatTokenTypes.BODY)
    }

    /**
     * Returns the rule operator.
     */
    override fun getRuleOperator(): PsiElement? {
        return findChildByType(PicatTokenTypes.ARROW_OP) ?: 
               findChildByType(PicatTokenTypes.BACKTRACKABLE_ARROW_OP)
    }
}
