package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionHead
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatFunctionDefinition interface.
 */
class PicatFunctionDefinitionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFunctionDefinition {
    /**
     * Returns the name of the function.
     */
    override fun getName(): String? {
        return getHead()?.getName()
    }

    /**
     * Returns the arity of the function (number of arguments).
     */
    override fun getArity(): Int {
        return getHead()?.getArity() ?: 0
    }

    /**
     * Returns the head of the function.
     */
    override fun getHead(): PicatFunctionHead? {
        return PsiTreeUtil.getChildOfType(this, PicatFunctionHead::class.java)
    }

    /**
     * Returns the body of the function.
     */
    override fun getBody(): PicatFunctionBody? {
        return PsiTreeUtil.getChildOfType(this, PicatFunctionBody::class.java)
    }
}