package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportList
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PicatImportListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatImportList {
    override fun getImportItemList(): List<PicatImportItem> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatImportItem::class.java)
    }
}
