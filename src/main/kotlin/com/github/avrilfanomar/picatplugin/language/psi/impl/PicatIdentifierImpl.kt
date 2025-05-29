package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
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
     * Note: This is a read-only PSI element, so this method throws an exception.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Cannot modify identifier name")
    }
}
