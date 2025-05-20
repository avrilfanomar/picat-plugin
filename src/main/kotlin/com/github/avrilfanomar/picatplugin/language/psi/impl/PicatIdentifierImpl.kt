package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException

/**
 * Implementation of the PicatIdentifier interface.
 */
class PicatIdentifierImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatIdentifier {
    /**
     * Returns the name of the identifier.
     */
    override fun getName(): String? {
        return node.text
    }

    /**
     * Sets the name of the identifier.
     * This is required by PsiNamedElement but not implemented for now.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Rename not supported")
    }
}