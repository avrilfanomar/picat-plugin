package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariable
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
     * Note: This is a read-only PSI element, so this method throws an exception.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Cannot modify variable name")
    }
}
