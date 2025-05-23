package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateIndicator
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException

/**
 * Implementation of the PicatPredicateIndicator interface.
 */
class PicatPredicateIndicatorImpl(node: ASTNode) : ASTWrapperPsiElement(node), PicatPredicateIndicator {
    /**
     * Returns the name of the predicate.
     */
    override fun getName(): String? {
        return getAtom()?.getText()
    }

    /**
     * Sets the name of the predicate.
     * Note: This is a read-only PSI element, so this method throws an exception.
     */
    override fun setName(name: String): PsiElement {
        throw IncorrectOperationException("Cannot modify predicate indicator name")
    }

    /**
     * Returns the arity of the predicate.
     */
    override fun getArity(): Int {
        val arityElement = getArityElement()
        return arityElement?.text?.toIntOrNull() ?: 0
    }

    /**
     * Returns the atom element representing the predicate name.
     */
    override fun getAtom(): PicatAtom? {
        return PsiTreeUtil.getChildOfType(this, PicatAtom::class.java)
    }

    /**
     * Returns the integer element representing the predicate arity.
     */
    override fun getArityElement(): PsiElement? {
        // Find the integer element after the '/' character
        val children = node.getChildren(null)
        var foundSlash = false

        for (child in children) {
            if (foundSlash && child.elementType == PicatTokenTypes.INTEGER) {
                return child.psi
            }

            if (child.text == "/") {
                foundSlash = true
            }
        }

        return null
    }
}
