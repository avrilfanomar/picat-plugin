package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatForeachLoop
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatForeachLoop interface.
 */
class PicatForeachLoopImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatForeachLoop {
    /**
     * Returns the generators of the foreach loop.
     */
    override fun getGenerators(): PsiElement? {
        return findChildByType(PicatTokenTypes.FOREACH_GENERATORS)
    }

    /**
     * Returns the body of the foreach loop.
     */
    override fun getBody(): PsiElement? {
        return findChildByType(PicatTokenTypes.BODY)
    }
}
