package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomNoArgs
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatStructure
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException

/**
 * Implementation of the PicatHead interface.
 */
class PicatHeadImpl(node: ASTNode) : ASTWrapperPsiElement(node), PicatHead {
    /**
     * Returns the name of the head.
     */
    override fun getName(): String? {
        val atomNoArgs = getAtomNoArgs()
        val structure = getStructure()

        return when {
            atomNoArgs?.getAtom() != null -> atomNoArgs.getAtom()?.text
            structure != null -> structure.getName()
            else -> null
        }
    }

    /**
     * Sets the name of the head.
     * Note: This is a read-only PSI element, so this method throws an exception.
     */
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Cannot modify head name")
    }

    /**
     * Returns the arity of the head (number of arguments).
     */
    override fun getArity(): Int {
        val structure = getStructure()
        if (structure != null) {
            return structure.getArity()
        }

        return 0
    }

    /**
     * Returns the atom of the head, if it's an atom.
     * @deprecated Use getAtomNoArgs() instead.
     */
    @Deprecated("Use getAtomNoArgs() instead", ReplaceWith("getAtomNoArgs()?.getAtom()"))
    override fun getAtom(): PicatAtom? {
        val atomNoArgs = getAtomNoArgs()
        return atomNoArgs?.getAtom()
    }

    /**
     * Returns the atom_no_args of the head, if it's an atom_no_args.
     */
    override fun getAtomNoArgs(): PicatAtomNoArgs? {
        return PsiTreeUtil.getChildOfType(this, PicatAtomNoArgs::class.java)
    }

    /**
     * Returns the structure of the head, if it's a structure.
     */
    override fun getStructure(): PicatStructure? {
        return PsiTreeUtil.getChildOfType(this, PicatStructure::class.java)
    }
}
