package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatDotAccess
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiUtil
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
 * - PicatAtomOrCallNoLambda nodes (call expressions without a lambda),
 * - PicatImportItem (module import) atoms to stdlib files,
 * - PicatDotAccess (module-qualified calls like bp.predicate(...)),
 * and only over the atom's text range within those elements.
 */
class PicatReferenceContributor : PsiReferenceContributor(), com.intellij.openapi.project.DumbAware {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Atom element itself (standalone atoms only, not inside calls)
        registrar.registerReferenceProvider(
            psiElement(PicatAtom::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    // Disabled atom-level predicate references to avoid ambiguity; call references handle usages.
                    return emptyArray()
                }
            }
        )

        // Call node: PicatAtomOrCallNoLambda
        registrar.registerReferenceProvider(
            psiElement(PicatAtomOrCallNoLambda::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val call = element as PicatAtomOrCallNoLambda
                    val atom = call.atom
                    val id = atom.identifier ?: atom.singleQuotedAtom
                    val result: Array<PsiReference> = if (id != null) {
                        val start = id.textOffset - call.textOffset
                        val range = TextRange(start, start + id.textLength)
                        arrayOf(PicatReference(call, range))
                    } else {
                        PsiReference.EMPTY_ARRAY
                    }
                    return result
                }
            }
        )

        // Import item module reference (e.g., import cp, planner.) -> cp.pi / planner.pi in stdlib
        // Attaches the reference directly to the atom element inside PicatImportItem to ensure visibility
        registrar.registerReferenceProvider(
            psiElement(PicatAtom::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> = buildAtomImportReference(element as PicatAtom)
            }
        )

        // Also, attach a reference to the PicatImportItem element itself so importItem.references includes it
        registrar.registerReferenceProvider(
            psiElement(PicatImportItem::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> = buildImportItemReference(element as PicatImportItem)
            }
        )

        // Ultra-safe fallback: if, for any reason, the above patterns don't trigger, attach module reference
        // by inspecting the element type at runtime. This ensures tests in temp:// VFS also see the reference.
        registrar.registerReferenceProvider(
            psiElement(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    return when (element) {
                        is PicatImportItem -> buildImportItemReference(element)
                        is PicatAtom -> buildAtomImportReference(element)
                        else -> emptyArray()
                    }
                }
            }
        )

        // Dot access (module-qualified calls like bp.predicate(...))
        registrar.registerReferenceProvider(
            psiElement(PicatDotAccess::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> = buildDotAccessReference(element as PicatDotAccess)
            }
        )
    }

    private fun buildDotAccessReference(dotAccess: PicatDotAccess): Array<PsiReference> {
        val predicateName = PicatPsiUtil.getDotAccessName(dotAccess)
        val dotId = dotAccess.dotIdentifier ?: dotAccess.dotSingleQuotedAtom
        if (predicateName == null || dotId == null) return emptyArray()

        val start = dotId.textOffset - dotAccess.textOffset + 1 // +1 to skip leading dot
        return arrayOf(PicatDotAccessReference(dotAccess, TextRange(start, start + predicateName.length)))
    }

    private fun buildImportItemReference(element: PicatImportItem): Array<PsiReference> {
        val id = element.atom?.let { it.identifier ?: it.singleQuotedAtom } ?: return emptyArray()
        val start = id.textOffset - element.textOffset
        return arrayOf(PicatImportModuleReference(element, TextRange(start, start + id.textLength)))
    }

    private fun buildAtomImportReference(element: PicatAtom): Array<PsiReference> {
        val id = (element.identifier ?: element.singleQuotedAtom)
            ?.takeIf { element.parent is PicatImportItem } ?: return emptyArray()
        val range = TextRange(id.startOffsetInParent, id.startOffsetInParent + id.textLength)
        return arrayOf(PicatImportModuleReference(element, range))
    }
}
