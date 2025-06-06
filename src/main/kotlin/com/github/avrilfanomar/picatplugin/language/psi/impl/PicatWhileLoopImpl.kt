package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.PicatWhileLoop
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatWhileLoop interface.
 */
class PicatWhileLoopImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatWhileLoop {
    /**
     * Returns the condition of the while loop.
     */
    override fun getCondition(): PsiElement? {
        return findChildByType(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Returns the body of the while loop.
     */
    override fun getBody(): PsiElement? {
        return findChildByType(PicatTokenTypes.BODY)
    }
}