package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatModuleName interface.
 */
class PicatModuleNameImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatModuleName {
    /**
     * Returns the identifier of the module.
     * According to the BNF, a module name is an atom, so we return the atom child element.
     */
    override fun getIdentifier(): PsiElement? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }

    /**
     * Returns the name of the module.
     * According to the BNF, a module name is simply an atom, so we directly use the atom's name.
     */
    override fun getName(): String? {
        // According to the grammar, a module name is an atom
        val atom = PsiTreeUtil.getChildOfType(this, PicatAtom::class.java) ?: return null

        // Try to get the name in order of preference
        return when {
            atom.getIdentifier()?.getName() != null -> atom.getIdentifier()?.getName()
            atom.getQuotedAtom() != null -> {
                val text = atom.getQuotedAtom()?.text ?: ""
                if (text.length >= 2) text.substring(1, text.length - 1) else text
            }
            else -> atom.text
        }
    }
}
