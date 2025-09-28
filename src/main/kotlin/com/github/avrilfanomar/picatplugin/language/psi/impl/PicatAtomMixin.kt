package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiImplUtil
import com.github.avrilfanomar.picatplugin.language.references.PicatImportModuleReference
import com.github.avrilfanomar.picatplugin.language.references.PicatReference
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

/**
 * Abstract mixin class that provides PSI functionality for Picat atom elements.
 * 
 * This class implements the PicatAtom interface and provides:
 * - Name handling (get/set name, name identifier)
 * - Reference resolution for atoms in different contexts (imports vs regular atoms)
 * - Integration with the PSI reference system for navigation and completion
 */
abstract class PicatAtomMixin(node: ASTNode) : ASTWrapperPsiElement(node), PicatAtom {
    override fun getName(): String? = PicatPsiImplUtil.getName(this)
    override fun setName(name: String): PsiElement = PicatPsiImplUtil.setName(this, name)
    override fun getNameIdentifier(): PsiElement? = PicatPsiImplUtil.getNameIdentifier(this)

    override fun getReferences(): Array<PsiReference> {
        val id = nameIdentifier ?: return PsiReference.EMPTY_ARRAY
        val start = id.startOffsetInParent
        val range = TextRange(start, start + id.textLength)

        // Attach a reference for all non-import atoms so findReferenceAt() at the identifier works
        // reliably in tests and in editor. The resolution logic inside PicatReference ensures correct
        // scoping (local > imported > implicit stdlib) and arity filtering.
        val refs: Array<PsiReference> = when {
            this.parent is PicatImportItem -> arrayOf(PicatImportModuleReference(this, range))
            else -> arrayOf(PicatReference(this, range))
        }
        return refs
    }

    override fun getReference(): PsiReference? = getReferences().firstOrNull()
}
