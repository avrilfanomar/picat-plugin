package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil

/**
 * Basic reference for Picat identifiers.
 *
 * Resolves an IDENTIFIER under a PicatAtom to the matching definition head (predicate/function)
 * within the same file by name equality.
 *
 * This keeps implementation intentionally simple and local-file scoped for now.
 */
class PicatReference(element: PsiElement, rangeInElement: TextRange) :
    PsiReferenceBase<PsiElement>(element, rangeInElement, /* soft = */ false) {

    override fun resolve(): PsiElement? {
        val name = element.text
        if (!name.isBlank() && element.containingFile != null) {
            // Find all heads (predicate/function) and match by atom identifier text.
            // Sort by position to ensure deterministic resolution to the first declared clause.
            val heads = PsiTreeUtil.findChildrenOfType(element.containingFile, PicatHead::class.java)
                .sortedBy { it.textOffset }
            for (head in heads) {
                val atom = head.atom
                val id: PsiElement? = atom.identifier
                val singleQuoted: PsiElement? = atom.singleQuotedAtom
                val candidateText = id?.text ?: singleQuoted?.text?.trim('"', '\'', '`')
                if (candidateText == name) {
                    // Prefer to resolve to the identifier token if present, otherwise atom element
                    return id ?: atom
                }
            }
        }
        return null
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile ?: return emptyArray()
        val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
        val names = heads.mapNotNull { head ->
            head.atom.let { atom -> atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('"', '\'', '`') }
        }.distinct()
        return names.toTypedArray()
    }
}
