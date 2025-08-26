package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.psi.PicatActionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatNonbacktrackablePredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Provides custom code folding for Picat:
 * - folds long rule/function bodies (only if multi-line)
 * - folds groups of consecutive predicate clauses sharing the same head
 *   functor/arity (from the 2nd clause onward)
 */
class PicatFoldingBuilder : FoldingBuilderEx(), com.intellij.openapi.project.DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        // 1) Fold long bodies in predicate and function rules
        PsiTreeUtil.processElements(root) { element ->
            when (element) {
                is PicatPredicateRule -> element.body?.let { addBodyFoldIfMultiline(it, document, descriptors) }
                is PicatFunctionRule -> element.body?.let { addBodyFoldIfMultiline(it, document, descriptors) }
                is PicatNonbacktrackablePredicateRule -> addBodyFoldIfMultiline(element.body, document, descriptors)
                is PicatActionRule -> addBodyFoldIfMultiline(element.body, document, descriptors)
            }
            true
        }

        // 2) Fold multi-clause predicate groups (same head functor/arity in consecutive definitions)
        addPredicateGroupFolds(root, document, descriptors)

        return descriptors.toTypedArray()
    }

    private fun addBodyFoldIfMultiline(body: PicatBody, document: Document, out: MutableList<FoldingDescriptor>) {
        val bodyRange = body.textRange ?: return
        if (!isMultiline(bodyRange, document)) return
        // Place the closing tag right before the terminating dot on the same line as in fixtures.
        val end = moveToNextChar(bodyRange.endOffset, document, '.')
        val start = moveStartAfterRuleOperator(bodyRange.startOffset, document)
        val adjusted = TextRange(start, end)
        out += FoldingDescriptor(body.node, adjusted, null, ELLIPSIS)
    }

    private fun addPredicateGroupFolds(root: PsiElement, document: Document, out: MutableList<FoldingDescriptor>) {
        // Collect consecutive predicate definitions (facts/rules) in file order via ProgramItem wrappers.
        val items = PsiTreeUtil.getChildrenOfTypeAsList(
            root,
            com.github.avrilfanomar.picatplugin.language.psi.PicatProgramItem::class.java
        )
        val allPredDefs = items.mapNotNull { it.predicateDefinition }
        var i = 0
        while (i < allPredDefs.size) {
            val def = allPredDefs[i]
            val sig = headSignature(def) ?: run { i++; continue }

            // Find how many further definitions have the same signature
            var j = i + 1
            while (j < allPredDefs.size) {
                val nextSig = headSignature(allPredDefs[j])
                if (nextSig == sig) j++ else break
            }
            val groupSize = j - i
            if (groupSize >= 2) {
                // Create a fold covering clauses from the 2nd in the group up to the last
                val second = allPredDefs[i + 1]
                val last = allPredDefs[j - 1]
                // Start at the beginning of the 2nd clause's line to put <fold> on its own line
                val startLine = document.getLineNumber(second.textRange.startOffset)
                val start = document.getLineStartOffset(startLine)
                // End at the end of the last clause's line including its trailing newline so </fold> is on its own line
                val endLine = document.getLineNumber(last.textRange.endOffset)
                val end =
                    (if (endLine + 1 < document.lineCount) {
                        document.getLineStartOffset(endLine + 1)
                    } else {
                        document.textLength
                    })
                val range = TextRange(start, end)
                // Do not require multiline: groups should fold even if subsequent clause(s) are single-line
                val placeholder = "+${groupSize - 1} " + if (groupSize - 1 == 1) "clause" else "clauses"
                // Attach the fold to the 2nd clause's node; placeholder is fixed per-descriptor
                out += FoldingDescriptor(second.node, range, null, placeholder)
            }
            i = j
        }
    }

    private fun headSignature(def: PicatPredicateDefinition): String? {
        val clause = def.predicateClause
        val head: PicatHead? = when {
            clause.predicateRule != null -> clause.predicateRule!!.head
            clause.predicateFact != null -> clause.predicateFact!!.head
            else -> null
        }
        head ?: return null
        val name = head.atom.text
        val arity = head.argumentList.size
        return "$name/$arity"
    }

    private fun isMultiline(range: TextRange, document: Document): Boolean {
        val startLine = document.getLineNumber(range.startOffset)
        val endLine = document.getLineNumber(range.endOffset)
        return endLine > startLine
    }

    @Suppress("SameParameterValue", "ReturnCount")
    private fun moveToNextChar(fromOffset: Int, document: Document, target: Char): Int {
        val text = document.charsSequence
        var i = fromOffset
        while (i < text.length) {
            if (text[i] == target) return i
            if (!text[i].isWhitespace()) return fromOffset // no target ahead on this line; keep original
            i++
        }
        return fromOffset
    }

    @Suppress("ReturnCount")
    private fun moveStartAfterRuleOperator(bodyStart: Int, document: Document): Int {
        // Look backwards a few characters to find the rule operator (":-", "=>", or "?=>")
        val text = document.charsSequence
        val lookBack = 4
        val from = (bodyStart - lookBack).coerceAtLeast(0)
        val segment = text.subSequence(from, bodyStart).toString()
        val idxQ = segment.lastIndexOf("?=>")
        if (idxQ >= 0) return from + idxQ + 3
        val idxArrow = segment.lastIndexOf("=>")
        if (idxArrow >= 0) return from + idxArrow + 2
        val idxProlog = segment.lastIndexOf(":-")
        if (idxProlog >= 0) return from + idxProlog + 2
        return bodyStart
    }

    @Suppress("ReturnCount")
    override fun getPlaceholderText(node: ASTNode): String {
        when (val psi = node.psi) {
            is PicatBody -> return ELLIPSIS
            is PicatPredicateDefinition -> {
                // If this definition is the 2nd (or later) clause in a same-head group, compute placeholder
                val parent = psi.parent ?: return ELLIPSIS
                val defs = PsiTreeUtil.getChildrenOfTypeAsList(parent, PicatPredicateDefinition::class.java)
                val idx = defs.indexOf(psi)
                if (idx > 0) {
                    val sig = headSignature(defs[idx])
                    val prevSig = headSignature(defs[idx - 1])
                    if (sig != null && sig == prevSig) {
                        // We're at least the 2nd in a group; count how many are in this contiguous group
                        var j = idx
                        while (j < defs.size && headSignature(defs[j]) == sig) j++
                        val groupSize = j - (idx - 1)
                        val folded = groupSize - 1
                        return "+$folded " + if (folded == 1) "clause" else "clauses"
                    }
                }
                return ELLIPSIS
            }
        }
        // Default
        return ELLIPSIS
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    companion object {
        private const val ELLIPSIS = "…"
    }
}
