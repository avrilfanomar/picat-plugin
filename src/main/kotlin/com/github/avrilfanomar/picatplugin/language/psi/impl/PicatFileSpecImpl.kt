package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatFileSpec
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

class PicatFileSpecImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFileSpec {
    private val atomTokenSet = TokenSet.create(PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM)

    override fun getStringLiteral(): PsiElement? {
        return findChildByType(PicatTokenTypes.STRING)
    }

    override fun getAtom(): PsiElement? {
        return findChildByType(atomTokenSet)
    }
}
