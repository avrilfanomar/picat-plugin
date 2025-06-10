package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportSpec
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

class PicatExportSpecImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExportSpec {
    private val atomTokenSet = TokenSet.create(PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM)

    override fun getAtom(): PsiElement? {
        // The parser for ExportSpec directly consumes an atom.
        return findChildByType(atomTokenSet)
    }
}
