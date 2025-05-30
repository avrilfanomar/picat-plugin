package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
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
        val atom = getAtom()
        return atom?.text
    }

    /**
     * Returns the arity of the predicate (number of arguments).
     */
    override fun getArity(): Int {
        val argumentList = getArgumentList()
        return argumentList?.getArguments()?.size ?: 0
    }

    /**
     * Returns the atom of the predicate.
     */
    override fun getAtom(): PicatAtom? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }

    /**
     * Returns the argument list of the predicate, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        return PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
    }


    /**
     * Returns the body of the predicate.
     */
    override fun getBody(): PicatPredicateBody? {
        return PsiTreeUtil.getChildOfType(this, PicatPredicateBody::class.java)
    }
}
