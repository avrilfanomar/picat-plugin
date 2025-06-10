package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatRenameList
import com.github.avrilfanomar.picatplugin.language.psi.PicatRenameSpec
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PicatRenameListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatRenameList {
    override fun getRenameSpecList(): List<PicatRenameSpec> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatRenameSpec::class.java)
    }
}
