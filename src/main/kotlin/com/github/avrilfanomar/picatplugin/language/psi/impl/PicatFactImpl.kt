package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * Implementation of the PicatFact interface.
 */
class PicatFactImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFact {
    /**
     * Returns the head of the fact.
     */
    override fun getHead(): PsiElement? {
        return findChildByType(PicatTokenTypes.HEAD) 
            ?: findChildByType(PicatTokenTypes.STRUCTURE) 
            ?: findChildByType(PicatTokenTypes.ATOM_NO_ARGS)
    }
}
