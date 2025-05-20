package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatVariable
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException

/**
 * Implementation of the PicatVariable interface.
 */
class PicatVariableImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatVariable {
    /**
     * Returns the name of the variable.
     */
    override fun getName(): String? {
        val variableElement = node.findChildByType(PicatTokenTypes.VARIABLE)
        return variableElement?.text
    }

    /**
     * Sets the name of the variable.
     * This is required by PsiNamedElement but not implemented for now.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Rename not supported")
    }
}