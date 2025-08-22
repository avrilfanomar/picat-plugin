package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext

/**
 * Registers references for Picat predicate/function atoms.
 *
 * We attach a reference only to atom leaf tokens (IDENTIFIER or SINGLE_QUOTED_ATOM)
 * whose parent PSI element is PicatAtom. This avoids creating references for
 * unrelated identifiers (e.g., variables).
 */
class PicatReferenceContributor : PsiReferenceContributor(), com.intellij.openapi.project.DumbAware {
    companion object {
        init { println("[DEBUG_LOG] PicatReferenceContributor loaded") }
    }
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val atomLeafTokens = TokenSet.create(
            PicatTokenTypes.IDENTIFIER,
            PicatTokenTypes.SINGLE_QUOTED_ATOM,
            PicatTokenTypes.DOT_IDENTIFIER,
            PicatTokenTypes.DOT_SINGLE_QUOTED_ATOM
        )
        // Leaf tokens: IDENTIFIER, quoted atoms (including dotted)
        registrar.registerReferenceProvider(
            psiElement().withElementType(atomLeafTokens).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    println("[DEBUG_LOG] leafProvider for '${'$'}{element.text}' type='${'$'}{element.node?.elementType}'")
                    return arrayOf(PicatReference(element, TextRange(0, element.textLength)))
                }
            }
        )

        // Fallback registration for any leaf that is an atom identifier, in case elementType filter misses.
        registrar.registerReferenceProvider(
            psiElement(),
            object : PsiReferenceProvider(), com.intellij.openapi.project.DumbAware {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val type = element.node?.elementType
                    val isAtomLeaf = (type == PicatTokenTypes.IDENTIFIER || type == PicatTokenTypes.SINGLE_QUOTED_ATOM)
                    if (isAtomLeaf) println("[DEBUG_LOG] fallbackLeafProvider for '${'$'}{element.text}' type='${'$'}type'")
                    return if (isAtomLeaf) {
                        arrayOf(PicatReference(element, TextRange(0, element.textLength)))
                    } else PsiReference.EMPTY_ARRAY
                }
            }
        )

        // Atom element itself
        registrar.registerReferenceProvider(
            psiElement(PicatAtom::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val atom = element as PicatAtom
                    val id = atom.identifier ?: atom.singleQuotedAtom
                    return if (id != null) {
                        val startInParent = id.startOffsetInParent
                        val range = TextRange(startInParent, startInParent + id.textLength)
                        println("[DEBUG_LOG] atomProvider for '${'$'}{id.text}' at range ${'$'}range")
                        arrayOf(PicatReference(atom, range))
                    } else PsiReference.EMPTY_ARRAY
                }
            }
        )

        // Call node: PicatAtomOrCallNoLambda
        registrar.registerReferenceProvider(
            psiElement(PicatAtomOrCallNoLambda::class.java).withLanguage(PicatLanguage),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val call = element as PicatAtomOrCallNoLambda
                    val atom = call.atom
                    val id = atom.identifier ?: atom.singleQuotedAtom
                    return if (id != null) {
                        val startInParent = id.textOffset - call.textOffset
                        val range = TextRange(startInParent, startInParent + id.textLength)
                        println("[DEBUG_LOG] callProvider for '${'$'}{id.text}' at range ${'$'}range")
                        arrayOf(PicatReference(call, range))
                    } else PsiReference.EMPTY_ARRAY
                }
            }
        )

        // Extra-wide fallback: if element is inside an Atom or a Call, attach a reference covering the atom text.
        registrar.registerReferenceProvider(
            psiElement(),
            object : PsiReferenceProvider(), com.intellij.openapi.project.DumbAware {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val atomAncestor = element.parentOfTypeLocal<PicatAtom>()
                    if (atomAncestor != null) {
                        val id = atomAncestor.identifier ?: atomAncestor.singleQuotedAtom
                        if (id != null) {
                            val startInParent = id.startOffsetInParent
                            val range = TextRange(startInParent, startInParent + id.textLength)
                            println("[DEBUG_LOG] fallbackAtomAncestor for '${'$'}{id.text}' at range ${'$'}range")
                            return arrayOf(PicatReference(atomAncestor, range))
                        }
                    }
                    val callAncestor = element.parentOfTypeLocal<PicatAtomOrCallNoLambda>()
                    if (callAncestor != null) {
                        val id = callAncestor.atom.identifier ?: callAncestor.atom.singleQuotedAtom
                        if (id != null) {
                            val startInParent = id.textOffset - callAncestor.textOffset
                            val range = TextRange(startInParent, startInParent + id.textLength)
                            println("[DEBUG_LOG] fallbackCallAncestor for '${'$'}{id.text}' at range ${'$'}range")
                            return arrayOf(PicatReference(callAncestor, range))
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}


private inline fun <reified T : PsiElement> PsiElement.hasAncestor(): Boolean {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return true
        curr = curr.parent
    }
    return false
}

private inline fun <reified T : PsiElement> PsiElement.parentOfTypeLocal(): T? {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return curr
        curr = curr.parent
    }
    return null
}
