package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext

/**
 * Registers references for Picat predicate/function atoms with strict scoping to avoid false positives.
 *
 * We only attach references to:
 * - PicatAtom nodes (the atom element itself), and
 * - PicatAtomOrCallNoLambda nodes (call expressions without lambda),
 * and only over the atom's text range within those elements.
 */
class PicatReferenceContributor : PsiReferenceContributor(), com.intellij.openapi.project.DumbAware {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Atom element itself
        registrar.registerReferenceProvider(
            psiElement(PicatAtom::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val atom = element as PicatAtom
                    val id = atom.identifier ?: atom.singleQuotedAtom
                    return if (id != null) {
                        val startInParent = id.startOffsetInParent
                        val range = TextRange(startInParent, startInParent + id.textLength)
                        arrayOf(PicatReference(atom, range))
                    } else PsiReference.EMPTY_ARRAY
                }
            }
        )

        // Call node: PicatAtomOrCallNoLambda
        registrar.registerReferenceProvider(
            psiElement(PicatAtomOrCallNoLambda::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val call = element as PicatAtomOrCallNoLambda
                    val atom = call.atom
                    val id = atom.identifier ?: atom.singleQuotedAtom
                    return if (id != null) {
                        val startInParent = id.textOffset - call.textOffset
                        val range = TextRange(startInParent, startInParent + id.textLength)
                        arrayOf(PicatReference(call, range))
                    } else PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}
