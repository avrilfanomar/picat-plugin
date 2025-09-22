package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCall
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentListTail
import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reference for Picat predicate/function atoms.
 *
 * Resolves to matching heads (predicate/function) in the same file by
 * name AND arity. multiResolve() returns all candidates; resolve() prefers
 * a single candidate when available.
 */
class PicatReference(element: PsiElement, rangeInElement: TextRange) :
    PsiPolyVariantReferenceBase<PsiElement>(element, rangeInElement, false) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results: Array<ResolveResult>
        val name = element.text
        val file = element.containingFile
        if (name.isBlank() || file == null) {
            results = ResolveResult.EMPTY_ARRAY
        } else {
            val usageArity = computeUsageArity(element)
            // Local heads in the current file
            val localHeads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
            // Heads from implicit stdlib modules: basic and io (if available)
            val basicPsi = PicatStdlibUtil.findStdlibModulePsiFile(element.project, "basic")
            val ioPsi = PicatStdlibUtil.findStdlibModulePsiFile(element.project, "io")
            val basicHeads = if (basicPsi != null) PsiTreeUtil.findChildrenOfType(basicPsi, PicatHead::class.java) else emptySet()
            val ioHeads = if (ioPsi != null) PsiTreeUtil.findChildrenOfType(ioPsi, PicatHead::class.java) else emptySet()

            val localMatches = localHeads.filter { h ->
                h.atomName() == name && (usageArity?.let { it == h.arity() } ?: true)
            }.sortedBy { it.textOffset }
            val basicMatches = basicHeads.filter { h ->
                h.atomName() == name && (usageArity?.let { it == h.arity() } ?: true)
            }.sortedBy { it.textOffset }
            val ioMatches = ioHeads.filter { h ->
                h.atomName() == name && (usageArity?.let { it == h.arity() } ?: true)
            }.sortedBy { it.textOffset }
            val stdlibMatches = basicMatches + ioMatches
            val matches = if (localMatches.isNotEmpty()) localMatches else stdlibMatches
            results = if (matches.isEmpty()) {
                ResolveResult.EMPTY_ARRAY
            } else {
                matches.mapNotNull { h ->
                    val target: PsiElement =
                        h.headArgs?.argumentList?.firstOrNull()?.let { it as PsiElement }
                            ?: h.atom.identifier
                            ?: h.atom
                    PsiElementResolveResult(target)
                }.toTypedArray()
            }
        }
        return results
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return if (results.size == 1) results[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile ?: return emptyArray()
        val localHeads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
        val basicPsi = PicatStdlibUtil.findStdlibModulePsiFile(element.project, "basic")
        val ioPsi = PicatStdlibUtil.findStdlibModulePsiFile(element.project, "io")
        val basicHeads = if (basicPsi != null) PsiTreeUtil.findChildrenOfType(basicPsi, PicatHead::class.java) else emptySet()
        val ioHeads = if (ioPsi != null) PsiTreeUtil.findChildrenOfType(ioPsi, PicatHead::class.java) else emptySet()
        val heads = localHeads + basicHeads + ioHeads
        val nameArityPairs = heads.mapNotNull { head ->
            val name = head.atomName() ?: return@mapNotNull null
            name to head.arity()
        }.distinct()
        val variants = nameArityPairs.map { (name, arity) ->
            LookupElementBuilder.create(name)
                .withTypeText("/$arity", true)
        }
        return variants.toTypedArray()
    }
}

private fun PicatHead.arity(): Int = this.headArgs?.argumentList?.size ?: 0

private fun PicatHead.atomName(): String? {
    val a = this.atom
    return a.identifier?.text ?: a.singleQuotedAtom?.text?.trim('\'', '"', '`')
}

/**
 * Compute the arity at the usage site (where the reference is placed),
 * if it can be determined from the PSI context. Returns null if unknown
 * (e.g., standalone atom that could be 0-arity or part of a different construct).
 */
private fun computeUsageArity(leaf: PsiElement): Int? {
    val atom = when (leaf) {
        is PicatAtom -> leaf
        else -> leaf.parent as? PicatAtom
    } ?: return null

    val headArity = atom.parentOfType<PicatHead>()?.arity()
    val result = when {
        headArity != null -> headArity
        atom.parentOfType<PicatAtomOrCallNoLambda>() != null -> {
            val call = atom.parentOfType<PicatAtomOrCallNoLambda>()
            countCallArgs(call?.argument, call?.argumentListTail)
        }

        atom.parentOfType<PicatFunctionCall>() != null -> {
            val fn = atom.parentOfType<PicatFunctionCall>()
            val simple = fn?.functionCallSimple
            val qualified = fn?.qualifiedFunctionCall
            val first = simple?.argument ?: qualified?.argument
            val tail = simple?.argumentListTail ?: qualified?.argumentListTail
            countCallArgs(first, tail)
        }

        else -> null
    }
    return result
}

private tailrec fun countArgTail(tail: PicatArgumentListTail?, acc: Int): Int =
    if (tail == null) acc else countArgTail(tail.argumentListTail, acc + 1)

private fun countCallArgs(first: PicatArgument?, tail: PicatArgumentListTail?): Int? =
    if (first == null) 0 else 1 + countArgTail(tail, 0)


private inline fun <reified T : PsiElement> PsiElement.parentOfType(): T? {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return curr
        curr = curr.parent
    }
    return null
}
