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
     */
    override fun getIdentifier(): PsiElement? {
        return firstChild
    }

    /**
     * Returns the name of the module.
     */
    override fun getName(): String? {
        // According to the grammar, a module name is an atom
        val atom = PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)

        // Try to get the name from the identifier
        val identifier = atom?.getIdentifier()
        if (identifier != null) {
            return identifier.getName()
        }

        // If there's no identifier, try to get the name from the quoted atom
        val quotedAtom = atom?.getQuotedAtom()
        if (quotedAtom != null) {
            // Remove the quotes from the quoted atom
            val text = quotedAtom.text
            if (text.length >= 2) {
                return text.substring(1, text.length - 1)
            }
        }

        return null
    }
}
