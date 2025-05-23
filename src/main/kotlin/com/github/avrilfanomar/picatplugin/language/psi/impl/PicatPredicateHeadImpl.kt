package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateHead
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatPredicateHead interface.
 */
class PicatPredicateHeadImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatPredicateHead {
    /**
     * Returns the name of the predicate.
     */
    override fun getName(): String? {
        // The name is typically stored in the first child element
        // This is a simplified implementation
        return node.firstChildNode?.text
    }

    /**
     * Returns the arity of the predicate (number of arguments).
     */
    override fun getArity(): Int {
        val argumentList = getArgumentList()
        return argumentList?.getArguments()?.size ?: 0
    }

    /**
     * Returns the argument list of the predicate, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        return PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
    }
}