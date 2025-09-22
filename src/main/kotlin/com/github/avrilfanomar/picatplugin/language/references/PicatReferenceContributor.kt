package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
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
 * - PicatImportItem (module import) atoms to stdlib files,
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
                    // Skip atoms that are part of an import item; they have a dedicated module reference
                    if (atom.parent is com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem) {
                        return PsiReference.EMPTY_ARRAY
                    }
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

        // Import item module reference (e.g., import cp, planner.) -> cp.pi / planner.pi in stdlib
        // Attach the reference directly to the atom element inside PicatImportItem to ensure visibility
        registrar.registerReferenceProvider(
            psiElement(PicatAtom::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val atom = element as PicatAtom
                    if (atom.parent !is PicatImportItem) return PsiReference.EMPTY_ARRAY
                    val id = atom.identifier ?: atom.singleQuotedAtom ?: return PsiReference.EMPTY_ARRAY
                    val startInParent = id.startOffsetInParent
                    val range = TextRange(startInParent, startInParent + id.textLength)
                    return arrayOf(PicatImportModuleReference(atom, range))
                }
            }
        )

        // Also attach a reference at the PicatImportItem element itself so importItem.references includes it
        registrar.registerReferenceProvider(
            psiElement(PicatImportItem::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val importItem = element as PicatImportItem
                    val atom = importItem.atom ?: return PsiReference.EMPTY_ARRAY
                    val id = atom.identifier ?: atom.singleQuotedAtom ?: return PsiReference.EMPTY_ARRAY
                    val startInParent = id.textOffset - importItem.textOffset
                    val range = TextRange(startInParent, startInParent + id.textLength)
                    return arrayOf(PicatImportModuleReference(importItem, range))
                }
            }
        )

        // Ultra-safe fallback: if, for any reason, the above patterns don't trigger, attach module reference
        // by inspecting the element type at runtime. This ensures tests in temp:// VFS also see the reference.
        registrar.registerReferenceProvider(
            psiElement(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return when (element) {
                        is PicatImportItem -> {
                            val atom = element.atom ?: return PsiReference.EMPTY_ARRAY
                            val id = atom.identifier ?: atom.singleQuotedAtom ?: return PsiReference.EMPTY_ARRAY
                            val startInParent = id.textOffset - element.textOffset
                            val range = TextRange(startInParent, startInParent + id.textLength)
                            arrayOf(PicatImportModuleReference(element, range))
                        }
                        is PicatAtom -> {
                            if (element.parent !is PicatImportItem) return PsiReference.EMPTY_ARRAY
                            val id = element.identifier ?: element.singleQuotedAtom ?: return PsiReference.EMPTY_ARRAY
                            val range = TextRange(id.startOffsetInParent, id.startOffsetInParent + id.textLength)
                            arrayOf(PicatImportModuleReference(element, range))
                        }
                        else -> PsiReference.EMPTY_ARRAY
                    }
                }
            }
        )
    }
}
