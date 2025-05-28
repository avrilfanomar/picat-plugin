package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.github.avrilfanomar.picatplugin.language.psi.PicatStructure
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatStructure interface.
 */
class PicatStructureImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatStructure {
    /**
     * Returns the identifier of the structure.
     */
    override fun getIdentifier(): PicatIdentifier? {
        return PsiTreeUtil.getChildOfType(this, PicatIdentifier::class.java)
    }

    /**
     * Returns the argument list of the structure, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        return PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
    }

    /**
     * Returns the name of the structure.
     */
    override fun getName(): String? {
        return getIdentifier()?.getName()
    }

    /**
     * Returns the arity of the structure (number of arguments).
     */
    override fun getArity(): Int {
        val argumentList = getArgumentList()
        return argumentList?.getArguments()?.size ?: 0
    }
}
