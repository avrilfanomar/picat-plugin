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
        // First try to find the atom directly
        val directAtom = PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
        if (directAtom != null) {
            return directAtom
        }

        // If not found, try to find it in the head
        val head = getHead()
        if (head != null) {
            // Try to find the atom directly in the head
            val headAtom = PsiTreeUtil.getChildOfType(head, PicatAtom::class.java)
            if (headAtom != null) {
                return headAtom
            }

            // If not found, try to find it in a structure in the head
            val structure = PsiTreeUtil.getChildOfType(head, com.github.avrilfanomar.picatplugin.language.psi.PicatStructure::class.java)
            if (structure != null) {
                return PsiTreeUtil.getChildOfType(structure, PicatAtom::class.java)
            }
        }

        return null
    }

    /**
     * Returns the argument list of the function, if any.
     */
    override fun getArgumentList(): PicatArgumentList? {
        // First try to find the argument list directly
        val directArgumentList = PsiTreeUtil.getChildOfType(this, PicatArgumentList::class.java)
        if (directArgumentList != null) {
            return directArgumentList
        }

        // If not found, try to find it in the head
        val head = getHead()
        if (head != null) {
            // Try to find the argument list directly in the head
            val headArgumentList = PsiTreeUtil.getChildOfType(head, PicatArgumentList::class.java)
            if (headArgumentList != null) {
                return headArgumentList
            }

            // If not found, try to find it in a structure in the head
            val structure = PsiTreeUtil.getChildOfType(head, com.github.avrilfanomar.picatplugin.language.psi.PicatStructure::class.java)
            if (structure != null) {
                return PsiTreeUtil.getChildOfType(structure, PicatArgumentList::class.java)
            }
        }

        return null
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
        // Look for a HEAD element first
        val head = node.findChildByType(com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.HEAD)
        if (head != null) {
            return head.psi
        }

        // If no HEAD element, look for a STRUCTURE or ATOM directly
        val structure = node.findChildByType(com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.STRUCTURE)
        if (structure != null) {
            return structure.psi
        }

        val atom = node.findChildByType(com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.ATOM)
        return atom?.psi
    }
}
