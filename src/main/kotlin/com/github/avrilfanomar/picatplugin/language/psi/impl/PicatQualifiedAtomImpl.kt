package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatQualifiedAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

class PicatQualifiedAtomImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatQualifiedAtom {

    private val atomTokenSet = TokenSet.create(PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM)

    override fun getModuleAtom(): PsiElement? {
        // The first child that is an atom token should be the module atom.
        val children = node.getChildren(atomTokenSet)
        return children.getOrNull(0)?.psi
    }

    override fun getDotOperator(): PsiElement? {
        return findChildByType(PicatTokenTypes.DOT_OP)
    }

    override fun getNameAtom(): PsiElement? {
        // The second child that is an atom token should be the name atom.
        val children = node.getChildren(atomTokenSet)
        return children.getOrNull(1)?.psi
    }
}
