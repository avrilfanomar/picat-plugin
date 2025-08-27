package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
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
                holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference")
                    .range(range)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create()
            }
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
