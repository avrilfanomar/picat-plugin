package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatFact interface.
 */
class PicatFactImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFact {
    /**
     * Returns the head of the fact.
     */
    override fun getHead(): PsiElement? {
        return findChildByType(PicatTokenTypes.STRUCTURE) ?: findChildByType(PicatTokenTypes.ATOM)
    }
}