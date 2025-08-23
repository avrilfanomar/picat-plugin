package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiImplUtil
import com.github.avrilfanomar.picatplugin.language.references.PicatReference
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

abstract class PicatAtomMixin(node: ASTNode) : ASTWrapperPsiElement(node), PicatAtom {
    override fun getName(): String? = PicatPsiImplUtil.getName(this)
    override fun setName(name: String): PsiElement = PicatPsiImplUtil.setName(this, name)
    override fun getNameIdentifier(): PsiElement? = PicatPsiImplUtil.getNameIdentifier(this)

    override fun getReferences(): Array<PsiReference> {
        val id = nameIdentifier ?: return PsiReference.EMPTY_ARRAY
        val start = id.startOffsetInParent
        val range = TextRange(start, start + id.textLength)
        return arrayOf(PicatReference(this, range))
    }

    override fun getReference(): PsiReference? = getReferences().firstOrNull()
}
