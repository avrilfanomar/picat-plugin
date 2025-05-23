package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatList
import com.github.avrilfanomar.picatplugin.language.psi.PicatListElements
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatList interface.
 */
class PicatListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatList {
    /**
     * Returns the list elements, if any.
     */
    override fun getListElements(): PicatListElements? {
        return PsiTreeUtil.getChildOfType(this, PicatListElements::class.java)
    }
}