package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatRenameSpec
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

class PicatRenameSpecImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatRenameSpec {
    private val atomTokenSet = TokenSet.create(PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM)

    override fun getOriginalAtom(): PsiElement? {
        val atoms = findChildrenByType(atomTokenSet)
        return atoms.firstOrNull()
    }

    override fun getArrowOperator(): PsiElement? {
        return findChildByType(PicatTokenTypes.ARROW_OP)
    }

    override fun getNewAtom(): PsiElement? {
        val atoms = findChildrenByType(atomTokenSet)
        return if (atoms.size > 1 && getArrowOperator() != null) {
            atoms.getOrNull(1)
        } else {
            null
        }
    }
}
