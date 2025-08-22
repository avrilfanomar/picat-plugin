package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionArgumentListTail
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCall
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatTerm
import com.github.avrilfanomar.picatplugin.language.psi.PicatTermListTail
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reference for Picat predicate/function atoms.
 *
 * Resolves to a matching head (predicate/function) in the same file by
 * name AND arity. This improves correctness when multiple clauses exist
 * with different arities (e.g., p/0 vs p/1).
 */
class PicatReference(element: PsiElement, rangeInElement: TextRange) :
    PsiReferenceBase<PsiElement>(element, rangeInElement, false) {

    override fun resolve(): PsiElement? {
        val name = element.text
        val file = element.containingFile
        if (name.isBlank() || file == null) return null

        val usageArity = computeUsageArity(element)
        val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
            .sortedBy { it.textOffset }

        val matchedHead = if (usageArity != null) {
            heads.firstOrNull { it.atomName() == name && it.arity() == usageArity }
        } else {
            heads.firstOrNull { it.atomName() == name }
        }

        return matchedHead?.let { it.atom.identifier ?: it.atom }
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile ?: return emptyArray()
        val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
        val names = heads.mapNotNull { head ->
            head.atomName()?.let { n -> "$n/${head.arity()}" }
        }.distinct()
        return names.toTypedArray()
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
    val atom = leaf.parent as? PicatAtom ?: return null

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
