package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportList
import com.github.avrilfanomar.picatplugin.language.psi.PicatExportSpec
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PicatExportListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExportList {
    override fun getExportSpecList(): List<PicatExportSpec> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatExportSpec::class.java)
    }
}
