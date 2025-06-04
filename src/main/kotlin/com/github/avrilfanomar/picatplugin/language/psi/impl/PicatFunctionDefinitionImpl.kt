package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentList
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatStructure
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
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
        return findAtomInHead()
    }

    /**
     * Helper method to find atom in the head element.
     */
    private fun findAtomInHead(): PicatAtom? {
        val head = getHead() ?: return null

        // Try to find the atom directly in the head
        val headAtom = PsiTreeUtil.getChildOfType(head, PicatAtom::class.java)

        // If found, return it, otherwise try to find it in a structure
        return headAtom ?: run {
            // Try to find it in a structure in the head
            val structure = PsiTreeUtil.getChildOfType(head, PicatStructure::class.java)
            structure?.let { PsiTreeUtil.getChildOfType(it, PicatAtom::class.java) }
        }
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
        return findArgumentListInHead()
    }

    /**
     * Helper method to find argument list in the head element.
     */
    private fun findArgumentListInHead(): PicatArgumentList? {
        val head = getHead() ?: return null

        // Try to find the argument list directly in the head
        val headArgumentList = PsiTreeUtil.getChildOfType(head, PicatArgumentList::class.java)

        // If found, return it, otherwise try to find it in a structure
        return headArgumentList ?: run {
            // Try to find it in a structure in the head
            val structure = PsiTreeUtil.getChildOfType(head, PicatStructure::class.java)
            structure?.let { PsiTreeUtil.getChildOfType(it, PicatArgumentList::class.java) }
        }
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
        val head = node.findChildByType(PicatTokenTypes.HEAD)
        if (head != null) {
            return head.psi
        }

        // If no HEAD element, look for a STRUCTURE or ATOM directly
        return findStructureOrAtom()
    }

    /**
     * Helper method to find STRUCTURE or ATOM element.
     */
    private fun findStructureOrAtom(): PsiElement? {
        val structure = node.findChildByType(PicatTokenTypes.STRUCTURE)
        return structure?.psi ?: node.findChildByType(PicatTokenTypes.ATOM)?.psi
    }
}
