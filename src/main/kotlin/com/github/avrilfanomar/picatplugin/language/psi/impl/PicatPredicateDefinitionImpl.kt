package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateHead
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatPredicateDefinition interface.
 */
class PicatPredicateDefinitionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatPredicateDefinition {
    /**
     * Returns the name of the predicate.
     */
    override fun getName(): String? {
        return getHead()?.getName()
    }

    /**
     * Returns the arity of the predicate (number of arguments).
     */
    override fun getArity(): Int {
        return getHead()?.getArity() ?: 0
    }

    /**
     * Returns the head of the predicate.
     */
    override fun getHead(): PicatPredicateHead? {
        return PsiTreeUtil.getChildOfType(this, PicatPredicateHead::class.java)
    }

    /**
     * Returns the body of the predicate.
     */
    override fun getBody(): PicatPredicateBody? {
        return PsiTreeUtil.getChildOfType(this, PicatPredicateBody::class.java)
    }
}