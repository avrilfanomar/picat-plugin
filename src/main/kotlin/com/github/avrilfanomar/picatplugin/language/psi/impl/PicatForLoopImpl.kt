package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatForLoop
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatForLoop interface.
 */
class PicatForLoopImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatForLoop {
    /**
     * Returns the initialization of the for loop.
     */
    override fun getInitialization(): PsiElement? {
        // Find the first expression child
        val expressions = findChildrenByType<PsiElement>(PicatTokenTypes.EXPRESSION)
        return if (expressions.isNotEmpty()) expressions[0] else null
    }

    /**
     * Returns the condition of the for loop.
     */
    override fun getCondition(): PsiElement? {
        // Find the second expression child
        val expressions = findChildrenByType<PsiElement>(PicatTokenTypes.EXPRESSION)
        return if (expressions.size > 1) expressions[1] else null
    }

    /**
     * Returns the increment of the for loop.
     */
    override fun getIncrement(): PsiElement? {
        // Find the third expression child
        val expressions = findChildrenByType<PsiElement>(PicatTokenTypes.EXPRESSION)
        return if (expressions.size > 2) expressions[2] else null
    }

    /**
     * Returns the body of the for loop.
     */
    override fun getBody(): PsiElement? {
        return findChildByType(PicatTokenTypes.BODY)
    }
}
