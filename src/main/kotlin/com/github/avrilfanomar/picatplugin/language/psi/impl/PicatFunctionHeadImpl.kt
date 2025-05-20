package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatFunctionHead interface.
 */
class PicatFunctionHeadImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFunctionHead {
    /**
     * Returns the identifier of the function.
     */
    override fun getIdentifier(): PicatIdentifier? {
        return PsiTreeUtil.getChildOfType(this, PicatIdentifier::class.java)
    }
    
    /**
     * Returns the argument list of the function, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        return PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
    }
    
    /**
     * Returns the name of the function.
     */
    override fun getName(): String? {
        return getIdentifier()?.getName()
    }
    
    /**
     * Returns the arity of the function (number of arguments).
     */
    override fun getArity(): Int {
        val argumentList = getArgumentList()
        return argumentList?.getArguments()?.size ?: 0
    }
}