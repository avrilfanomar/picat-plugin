package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatRule interface.
 */
class PicatRuleImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatRule {
    /**
     * Returns the head of the rule.
     */
    override fun getHead(): PsiElement? {
        return findChildByType(PicatTokenTypes.HEAD)
            ?: findChildByType(PicatTokenTypes.STRUCTURE) 
            ?: findChildByType(PicatTokenTypes.ATOM)
            ?: firstChild // Fallback to first child if no specific head element is found
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
        return findChildByType(PicatTokenTypes.RULE_OPERATOR)
            ?: findChildByType(PicatTokenTypes.ARROW_OP) 
            ?: findChildByType(PicatTokenTypes.BACKTRACKABLE_ARROW_OP)
            ?: findChildByType(PicatTokenTypes.BICONDITIONAL_OP)
            ?: findChildByType(PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP)
            ?: findChildByType(PicatTokenTypes.RULE_OP)
    }

}
