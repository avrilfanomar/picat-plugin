package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatLiteral
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatLiteral interface.
 */
class PicatLiteralImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatLiteral {
    /**
     * Returns the integer value of the literal, if it's an integer.
     */
    override fun getInteger(): PsiElement? {
        return findChildByType(PicatTokenTypes.INTEGER)
    }

    /**
     * Returns the float value of the literal, if it's a float.
     */
    override fun getFloat(): PsiElement? {
        return findChildByType(PicatTokenTypes.FLOAT)
    }

    /**
     * Returns the string value of the literal, if it's a string.
     */
    override fun getString(): PsiElement? {
        return findChildByType(PicatTokenTypes.STRING)
    }

    /**
     * Returns the atom of the literal, if it's an atom.
     */
    override fun getAtom(): PicatAtom? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }
}
