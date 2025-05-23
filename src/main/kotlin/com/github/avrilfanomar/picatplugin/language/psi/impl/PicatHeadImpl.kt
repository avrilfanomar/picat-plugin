package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
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
        val atom = getAtom()
        if (atom != null) {
            return atom.getText()
        }
        
        val structure = getStructure()
        if (structure != null) {
            return structure.getName()
        }
        
        return null
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
     */
    override fun getAtom(): PicatAtom? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }
    
    /**
     * Returns the structure of the head, if it's a structure.
     */
    override fun getStructure(): PicatStructure? {
        return PsiTreeUtil.getChildOfType(this, PicatStructure::class.java)
    }
}