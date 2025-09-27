package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
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
                    @Suppress("UNCHECKED_CAST")
                    return PsiReference.EMPTY_ARRAY as Array<PsiReference>
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
                ): Array<PsiReference> {
                    val atom = element as PicatAtom
                    val result: Array<PsiReference> = if (atom.parent !is PicatImportItem) {
                        @Suppress("UNCHECKED_CAST")
                        PsiReference.EMPTY_ARRAY as Array<PsiReference>
                    } else {
                        val id = atom.identifier ?: atom.singleQuotedAtom
                        if (id == null) {
                            @Suppress("UNCHECKED_CAST")
                            PsiReference.EMPTY_ARRAY as Array<PsiReference>
                        } else {
                            val start = id.startOffsetInParent
                            val range = TextRange(start, start + id.textLength)
                            arrayOf(PicatImportModuleReference(atom, range))
                        }
                    }
                    return result
                }
            }
        )

        // Also, attach a reference to the PicatImportItem element itself so importItem.references includes it
        registrar.registerReferenceProvider(
            psiElement(PicatImportItem::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val importItem = element as PicatImportItem
                    val atom = importItem.atom
                    val result: Array<PsiReference> = if (atom == null) {
                        @Suppress("UNCHECKED_CAST")
                        PsiReference.EMPTY_ARRAY as Array<PsiReference>
                    } else {
                        val id = atom.identifier ?: atom.singleQuotedAtom
                        if (id == null) {
                            @Suppress("UNCHECKED_CAST")
                            PsiReference.EMPTY_ARRAY as Array<PsiReference>
                        } else {
                            val start = id.textOffset - importItem.textOffset
                            val range = TextRange(start, start + id.textLength)
                            arrayOf(PicatImportModuleReference(importItem, range))
                        }
                    }
                    return result
                }
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
                    val refs: Array<PsiReference> = when (element) {
                        is PicatImportItem -> {
                            val atom = element.atom
                            val id = atom?.identifier ?: atom?.singleQuotedAtom
                            if (id == null) {
                                @Suppress("UNCHECKED_CAST")
                                PsiReference.EMPTY_ARRAY as Array<PsiReference>
                            } else {
                                val start = id.textOffset - element.textOffset
                                val range = TextRange(start, start + id.textLength)
                                arrayOf(PicatImportModuleReference(element, range))
                            }
                        }
                        is PicatAtom -> {
                            val parentIsImport = element.parent is PicatImportItem
                            val id = element.identifier ?: element.singleQuotedAtom
                            if (!parentIsImport || id == null) {
                                @Suppress("UNCHECKED_CAST")
                                PsiReference.EMPTY_ARRAY as Array<PsiReference>
                            } else {
                                val start = id.startOffsetInParent
                                val end = start + id.textLength
                                val range = TextRange(start, end)
                                arrayOf(PicatImportModuleReference(element, range))
                            }
                        }
                        else -> {
                            @Suppress("UNCHECKED_CAST")
                            PsiReference.EMPTY_ARRAY as Array<PsiReference>
                        }
                    }
                    return refs
                }
            }
        )
    }
}
