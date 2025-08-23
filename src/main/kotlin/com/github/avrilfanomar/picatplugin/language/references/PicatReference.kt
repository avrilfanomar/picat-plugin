package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionArgumentListTail
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCall
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallNoDot
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatTerm
import com.github.avrilfanomar.picatplugin.language.psi.PicatTermListTail
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElementResolveResult

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
            val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
                .sortedBy { it.textOffset }
            val matches = heads.filter { h ->
                h.atomName() == name && (usageArity?.let { it == h.arity() } ?: true)
            }
            results = if (matches.isEmpty()) {
                ResolveResult.EMPTY_ARRAY
            } else {
                matches.mapNotNull { h ->
                    val target: PsiElement? =
                        h.argumentList.firstOrNull()?.let { it as PsiElement }
                            ?: h.atom.identifier
                            ?: h.atom
                    target?.let { PsiElementResolveResult(it) }
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
        val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
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

private fun PicatHead.arity(): Int = this.argumentList.size

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
            countCallArgs(call?.term, call?.termListTail)
        }
        atom.parentOfType<PicatFunctionCall>() != null -> {
            val fn = atom.parentOfType<PicatFunctionCall>()
            countFunctionArgs(fn?.functionArgument, fn?.functionArgumentListTail)
        }
        else -> null
    }
    return result
}

private tailrec fun countTermTail(tail: PicatTermListTail?, acc: Int): Int =
    if (tail == null) acc else countTermTail(tail.termListTail, acc + 1)

private fun countCallArgs(first: PicatTerm?, tail: PicatTermListTail?): Int? =
    if (first == null) 0 else 1 + countTermTail(tail, 0)

private tailrec fun countFunctionTail(tail: PicatFunctionArgumentListTail?, acc: Int): Int =
    if (tail == null) acc else countFunctionTail(tail.functionArgumentListTail, acc + 1)

private fun countFunctionArgs(first: PicatFunctionArgument?, tail: PicatFunctionArgumentListTail?): Int? =
    if (first == null) 0 else 1 + countFunctionTail(tail, 0)

private inline fun <reified T : PsiElement> PsiElement.parentOfType(): T? {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return curr
        curr = curr.parent
    }
    return null
}
