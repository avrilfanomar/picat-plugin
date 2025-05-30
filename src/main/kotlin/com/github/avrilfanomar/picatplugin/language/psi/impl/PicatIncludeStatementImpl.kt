package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatIncludeStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatIncludeStatement interface.
 */
class PicatIncludeStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatIncludeStatement {
    /**
     * Returns the path of the included file.
     */
    override fun getIncludePath(): String? {
        val stringLiteral = getStringLiteral()
        if (stringLiteral != null) {
            // Remove the quotes from the string literal
            val text = stringLiteral.text
            if (text.length >= 2) {
                return text.substring(1, text.length - 1)
            }
        }
        return null
    }

    /**
     * Returns the string literal element representing the include path.
     */
    override fun getStringLiteral(): PsiElement? {
        // Try to find the string literal directly
        val directChild = findChildByType<PsiElement>(PicatTokenTypes.STRING)
        if (directChild != null) {
            return directChild
        }

        // If not found directly, try to find it in all children
        return children.find { it.node.elementType == PicatTokenTypes.STRING }
    }
}
