package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.intentions.CreatePredicateIntentionAction
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiUtil
import com.github.avrilfanomar.picatplugin.language.references.PicatReference
import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Basic annotator for Picat.
 * - Flags unresolved predicate/function references (error)
 * - Flags simple style issues: unnecessary quotes around atom names (weak warning)
 */
class PicatAnnotator : Annotator, DumbAware {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val project = element.project
        val settings = PicatSettings.getInstance(project)
        if (!settings.enableAnnotations) return

        when (element) {
            is PicatAtomOrCallNoLambda -> annotateReferences(element, holder)
            is PicatAtom -> {
                annotateReferences(element, holder)
                annotateStyle(element, holder)
            }
        }
    }

    private fun annotateReferences(element: PsiElement, holder: AnnotationHolder) {
        val refs = element.references
        for (ref in refs) {
            if (ref !is PicatReference) continue
            val targets = ref.multiResolve(false)
            if (targets.isEmpty()) {
                val start = element.textRange.startOffset + ref.rangeInElement.startOffset
                val end = element.textRange.startOffset + ref.rangeInElement.endOffset
                val range = TextRange(start, end)
                val annotation = holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference")
                    .range(range)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                
                // Add quick-fix to create predicate
                val quickFix = CreatePredicateIntentionAction.createIfApplicable(element)
                if (quickFix != null) {
                    annotation.withFix(quickFix)
                }
                
                annotation.create()
            } else {
                // Check arity mismatch for resolved references
                checkArityMismatch(element, targets, holder)
            }
        }
    }

    private fun checkArityMismatch(
        element: PsiElement,
        targets: Array<out com.intellij.psi.ResolveResult>,
        holder: AnnotationHolder
    ) {
        // Get the called arity from the element
        val calledArity = estimateCallArity(element)
        if (calledArity == null) return

        // Check each target for arity mismatch
        val mismatchedTarget = targets
            .mapNotNull { it.element }
            .firstOrNull { target ->
                val definedArity = PicatPsiUtil.getHeadArity(target)
                definedArity != null && definedArity != calledArity
            }
            
        if (mismatchedTarget != null) {
            val definedArity = PicatPsiUtil.getHeadArity(mismatchedTarget)
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Arity mismatch: expected $definedArity arguments, got $calledArity"
            )
                .range(element.textRange)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)
                .create()
        }
    }

    private fun estimateCallArity(element: PsiElement): Int? {
        // Try to count arguments in a call-like structure
        val text = element.text
        val openParen = text.indexOf('(')
        val closeParen = text.lastIndexOf(')')
        
        return if (openParen >= 0 && closeParen > openParen) {
            val argsText = text.substring(openParen + 1, closeParen).trim()
            if (argsText.isEmpty()) 0
            else argsText.count { it == ',' } + 1
        } else {
            0 // No parentheses means arity 0
        }
    }

    private fun annotateStyle(atom: PicatAtom, holder: AnnotationHolder) {
        val quoted = atom.singleQuotedAtom ?: return
        val text = quoted.text
        // strip quotes: can be '...' or "..." or `...`
        val unquoted = text.trim('"', '\'', '`')
        val identifierRegex = Regex("^[a-z][a-zA-Z0-9_]*$")
        if (identifierRegex.matches(unquoted)) {
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "Unnecessary quotes around atom name")
                .range(quoted.textRange)
                .create()
        }
    }
}
