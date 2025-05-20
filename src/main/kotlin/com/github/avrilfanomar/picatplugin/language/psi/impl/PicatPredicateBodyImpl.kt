package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatClauseList
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateBody
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatPredicateBody interface.
 */
class PicatPredicateBodyImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatPredicateBody {
    /**
     * Returns the clause list of the predicate body.
     */
    override fun getClauseList(): PicatClauseList? {
        return PsiTreeUtil.getChildOfType(this, PicatClauseList::class.java)
    }
}