package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatAtom interface.
 */
class PicatAtomImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatAtom {
    /**
     * Returns the identifier of the atom, if it's an identifier.
     */
    override fun getIdentifier(): PicatIdentifier? {
        return PsiTreeUtil.getChildOfType(this, PicatIdentifier::class.java)
    }

    /**
     * Returns the quoted atom, if it's a quoted atom.
     */
    override fun getQuotedAtom(): PsiElement? {
        return findChildByType(PicatTokenTypes.QUOTED_ATOM)
    }
}
