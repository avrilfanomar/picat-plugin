package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement

/**
 * Documentation provider for Picat elements to support Quick Documentation (Ctrl+Q).
 *
 * Minimal, safe implementation:
 * - Shows name and kind for PicatAtom elements.
 * - If invoked on a reference, shows the resolved element's information when available.
 * - Keeps output compact and HTML-safe.
 */
class PicatDocumentationProvider : DocumentationProvider {

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? {
        // Provide a short, one-line description for status bar/quick popup.
        val target = resolveIfReference(element) ?: element
        return buildSummary(target)
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        val target = resolveIfReference(element) ?: element
        val builder = HtmlBuilder()
        val summary = buildSummary(target)
        if (summary != null) builder.append(summary)
        return builder.toString()
    }

    private fun buildSummary(element: PsiElement?): String? {
        return when (element) {
            is PicatAtom -> {
                val name = StringUtil.escapeXmlEntities(element.name ?: "")
                if (name.isNotEmpty()) "<b>Picat symbol:</b> $name" else null
            }
            else -> null
        }
    }

    private fun resolveIfReference(element: PsiElement?): PsiElement? {
        val reference = element?.reference
        return reference?.resolve()
    }
}
