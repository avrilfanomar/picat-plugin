package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionDefinition
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatFunctionDefinition interface.
 */
class PicatFunctionDefinitionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFunctionDefinition {
    /**
     * Returns the name of the function.
     */
    override fun getName(): String? {
        val atom = getAtom()
        return atom?.text
    }

    /**
     * Returns the arity of the function (number of arguments).
     */
    override fun getArity(): Int {
        val argumentList = getArgumentList()
        return argumentList?.getArguments()?.size ?: 0
    }

    /**
     * Returns the atom of the function.
     */
    override fun getAtom(): PicatAtom? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }

    /**
     * Returns the argument list of the function, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        return PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
    }

    /**
     * Returns the body of the function.
     */
    override fun getBody(): PicatFunctionBody? {
        return PsiTreeUtil.getChildOfType(this, PicatFunctionBody::class.java)
    }

    /**
     * Returns the head of the function definition.
     * This is required by the PicatFact interface.
     */
    override fun getHead(): PsiElement? {
        // Create a structure from the atom and argument list
        val atom = getAtom()
        val argumentList = getArgumentList()
        if (atom != null) {
            return atom
        }
        return null
    }
}
