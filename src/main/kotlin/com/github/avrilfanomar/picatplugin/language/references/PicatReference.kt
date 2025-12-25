package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentListTail
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCall
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
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

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        // Use the reference range to extract the identifier text. For call nodes,
        // element.text includes arguments, so we must slice by rangeInElement.
        val rawText = element.text
        val sliced = try {
            rawText.substring(rangeInElement.startOffset, rangeInElement.endOffset)
        } catch (_: Exception) {
            rawText
        }
        var ident = sliced.trim('`', '\'', '"')
        if (ident.isEmpty() || ident.contains('(') || ident.contains(')')) {
            val alt = (element as? PicatAtomOrCallNoLambda)?.atom
                ?: element as? PicatAtom
            val altName = alt?.identifier?.text
                ?: alt?.singleQuotedAtom?.text?.trim('`', '\'', '"')
            if (!altName.isNullOrBlank()) ident = altName
        }
        val file = element.containingFile
        val earlyEmpty = file == null || ident.isBlank()
        if (earlyEmpty) return ResolveResult.EMPTY_ARRAY

        var usageArity = computeUsageArity(element)
        if (usageArity == null) {
            // Robust fallback: derive arity from raw file text immediately after the reference name
            val fileText = element.containingFile?.text
            if (fileText != null) {
                val afterNameOffset = try {
                    element.textRange.startOffset + rangeInElement.endOffset
                } catch (_: Exception) {
                    element.textRange.endOffset
                }
                if (afterNameOffset in 0..fileText.length && fileText.startsWith("(", afterNameOffset)) {
                    val tail = fileText.substring(afterNameOffset)
                    val r = tail.indexOf(')')
                    if (r >= 0) {
                        val inside = tail.substring(1, r)
                        usageArity = if (inside.isBlank()) 0 else inside.count { it == ',' } + 1
                    }
                }
            }
        }
        val localHeads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
        val importedHeads = collectImportedHeads(file)
        val stdlibHeads = collectImplicitStdlibHeads()
        val primitiveHeads = collectPrimitiveHeads()

        val localMatches = filterAndSort(localHeads, ident, usageArity)
        val importedMatches = filterAndSort(importedHeads, ident, usageArity)
        val stdlibMatches = filterAndSort(stdlibHeads, ident, usageArity)
        val primitiveMatches = filterAndSort(primitiveHeads, ident, usageArity)

        var matches = when {
            localMatches.isNotEmpty() -> localMatches
            importedMatches.isNotEmpty() -> importedMatches
            stdlibMatches.isNotEmpty() -> stdlibMatches
            else -> primitiveMatches
        }
        if (matches.isEmpty()) {
            // Fallback: try name-only matching within local, imported, stdlib, then primitives
            val nameOnlyLocal = localHeads.filter { it.atomName() == ident }.sortedBy { it.textOffset }
            val nameOnlyImported = importedHeads.filter { it.atomName() == ident }.sortedBy { it.textOffset }
            val nameOnlyStd = stdlibHeads.filter { it.atomName() == ident }.sortedBy { it.textOffset }
            val nameOnlyPrimitives = primitiveHeads.filter { it.atomName() == ident }.sortedBy { it.textOffset }
            matches = when {
                nameOnlyLocal.isNotEmpty() -> prioritizeByArity(nameOnlyLocal, usageArity)
                nameOnlyImported.isNotEmpty() -> prioritizeByArity(nameOnlyImported, usageArity)
                nameOnlyStd.isNotEmpty() -> prioritizeByArity(nameOnlyStd, usageArity)
                else -> prioritizeByArity(nameOnlyPrimitives, usageArity)
            }
        }
        var results: Array<ResolveResult> = if (matches.isEmpty()) {
            ResolveResult.EMPTY_ARRAY
        } else {
            matches
                .map { PsiElementResolveResult(headTarget(it)) as ResolveResult }
                .toTypedArray()
        }
        if (results.isEmpty()) {
            // Last-resort fallback: try to find a local head that textually matches "name("
            val localTextual = localHeads
                .filter { h ->
                    val t = h.text
                    t.startsWith("$ident(") || t.contains("$ident(")
                }
                .sortedBy { it.textOffset }
            if (localTextual.isNotEmpty()) {
                results = arrayOf(PsiElementResolveResult(headTarget(localTextual.first())))
            } else {
                val loose = localHeads.firstOrNull { h ->
                    val nm = h.atomName() ?: ""
                    nm.contains(ident)
                }
                if (loose != null) {
                    results = arrayOf(PsiElementResolveResult(headTarget(loose)))
                }
            }
        }
        return results
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return results.firstOrNull()?.element
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile ?: return emptyArray()
        val localHeads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
        val importedHeads = collectImportedHeads(file)
        val implicitStdlib = collectImplicitStdlibHeads()
        val primitives = collectPrimitiveHeads()
        val heads = localHeads + importedHeads + implicitStdlib + primitives

        val nameArityPairs = heads
            .mapNotNull { head -> head.atomName()?.let { it to head.arity() } }
            .distinct()
        val variants = nameArityPairs.map { (name, arity) ->
            LookupElementBuilder
                .create(name)
                .withTypeText("/$arity", true)
        }
        return variants.toTypedArray()
    }

    private fun collectImportedHeads(file: com.intellij.psi.PsiFile): Set<PicatHead> {
        val importedItems = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
        val importedModuleNames = importedItems
            .mapNotNull { it.importModuleName() }
            .distinct()
        val importedPsiFiles = importedModuleNames
            .mapNotNull { moduleName ->
                PicatStdlibUtil.findStdlibModulePsiFile(element.project, moduleName)
            }
        @Suppress("UNCHECKED_CAST")
        return importedPsiFiles
            .flatMap { psiFile ->
                PsiTreeUtil.findChildrenOfType(
                    psiFile,
                    PicatHead::class.java
                )
            }
            .toSet()
    }

    private fun collectImplicitStdlibHeads(): Set<PicatHead> {
        val vf = PicatStdlibUtil.resolveLibVfsDir(element.project)
        return vf?.children?.mapNotNull { child ->
            PsiManager.getInstance(element.project).findFile(child)
                ?.let { PsiTreeUtil.findChildrenOfType(it, PicatHead::class.java) }
        }?.flatMap { it }?.toSet() ?: emptySet()
    }

    private fun collectPrimitiveHeads(): Set<PicatHead> {
        val primitivesPsiFile = PicatStdlibUtil.getPrimitivesPsiFile(element.project)
        return primitivesPsiFile?.let {
            PsiTreeUtil.findChildrenOfType(it, PicatHead::class.java).toSet()
        } ?: emptySet()
    }

    private fun filterAndSort(
        heads: Collection<PicatHead>,
        name: String,
        usageArity: Int?
    ): List<PicatHead> {
        val nameMatches = heads.filter { h -> h.atomName() == name }
        if (nameMatches.isEmpty()) return emptyList()
        val prioritized = if (usageArity != null) {
            nameMatches.sortedWith(compareBy<PicatHead> { h -> h.arity() != usageArity }
                .thenBy { h -> h.textOffset })
        } else {
            nameMatches.sortedBy { it.textOffset }
        }
        return prioritized
    }

    private fun headTarget(h: PicatHead): PsiElement {
        // Always resolve to the predicate/function name (atom identifier). This ensures callers
        // that compare offsets against the start of the head (or the name itself) behave
        // consistently for both 0- and >0-arity heads, and aligns with reference expectations
        // in tests where the resolution should point to the name rather than arguments.
        return h.atom.identifier ?: h.atom
    }

    private fun prioritizeByArity(list: List<PicatHead>, usageArity: Int?): List<PicatHead> =
        if (usageArity == null) list else list.sortedWith(
            compareBy<PicatHead> { it.arity() != usageArity }.thenBy { it.textOffset }
        )
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
@Suppress("LongMethod", "CyclomaticComplexMethod", "ReturnCount")
private fun computeUsageArity(leaf: PsiElement): Int? {
    var result: Int? = null

    when (leaf) {
        is PicatAtomOrCallNoLambda -> {
            result = countCallArgs(leaf.argument, leaf.argumentListTail)
        }

        else -> {
            val atom = when (leaf) {
                is PicatAtom -> leaf
                else -> leaf.parent as? PicatAtom
            }
            if (atom != null) {
                val headArity = atom.parentOfType<PicatHead>()?.arity()
                if (headArity != null) return headArity

                // Try various call node shapes around the atom
                atom.parentOfType<PicatAtomOrCallNoLambda>()?.let { call ->
                    return countCallArgs(call.argument, call.argumentListTail)
                }
                val fn = atom.parentOfType<PicatFunctionCall>()
                if (fn != null) {
                    val simple = fn.functionCallSimple
                    val qualified = fn.qualifiedFunctionCall
                    val first = simple?.argument ?: qualified?.argument
                    val tail = simple?.argumentListTail ?: qualified?.argumentListTail
                    return countCallArgs(first, tail)
                }
                val fnNoDot =
                    atom.parentOfType<com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallNoDot>()
                if (fnNoDot != null) {
                    val simple = fnNoDot.functionCallNoDotSimple
                    val first = simple?.argument
                    val tail = simple?.argumentListTail
                    return countCallArgs(first, tail)
                }
                val fnNoDotSimple =
                    atom.parentOfType<com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallNoDotSimple>()
                if (fnNoDotSimple != null) {
                    return countCallArgs(fnNoDotSimple.argument, fnNoDotSimple.argumentListTail)
                }
                val fnSimple =
                    atom.parentOfType<com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallSimple>()
                if (fnSimple != null) {
                    return countCallArgs(fnSimple.argument, fnSimple.argumentListTail)
                }
            }
        }
    }
    // Fallback heuristic: scan raw file text right after the atom for a '(...)' argument list
    if (result == null) {
        val fileText = leaf.containingFile?.text
        val atomEnd = (leaf as? com.github.avrilfanomar.picatplugin.language.psi.PicatAtom)?.textRange?.endOffset
            ?: (leaf.parent as? com.github.avrilfanomar.picatplugin.language.psi.PicatAtom)?.textRange?.endOffset
        if (fileText != null && atomEnd != null && atomEnd in 0..fileText.length) {
            val tail = fileText.substring(atomEnd)
            if (tail.startsWith("(")) {
                val r = tail.indexOf(')')
                if (r >= 0) {
                    val inside = tail.substring(1, r)
                    result = if (inside.isBlank()) 0 else inside.count { it == ',' } + 1
                }
            }
        }
    }
    return result
}

private tailrec fun countArgTail(tail: PicatArgumentListTail?, acc: Int): Int =
    if (tail == null) acc else countArgTail(tail.argumentListTail, acc + 1)

private fun countCallArgs(first: PicatArgument?, tail: PicatArgumentListTail?): Int? =
    if (first == null) 0 else 1 + countArgTail(tail, 0)

private fun PicatImportItem.importModuleName(): String? {
    val atom = this.atom ?: return null
    val raw = atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('\'', '"', '`')
    val name = raw?.trim()
    return if (name.isNullOrEmpty()) null else name
}

private inline fun <reified T : PsiElement> PsiElement.parentOfType(): T? {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return curr
        curr = curr.parent
    }
    return null
}
