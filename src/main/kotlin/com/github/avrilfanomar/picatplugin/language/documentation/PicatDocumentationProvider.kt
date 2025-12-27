package com.github.avrilfanomar.picatplugin.language.documentation

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiUtil
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Documentation provider for Picat elements to support Quick Documentation (Ctrl+Q).
 *
 * Provides documentation for:
 * - Constraint predicates and functions (from PicatConstraintDocs registry)
 * - Solver options (context-aware, when inside solve() calls)
 * - Regular Picat atoms and symbols
 */
class PicatDocumentationProvider : DocumentationProvider {

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? {
        val target = resolveIfReference(element) ?: element
        return buildQuickInfo(target)
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String {
        val target = resolveIfReference(element) ?: element

        // Try constraint documentation first, then fall back to default
        return tryGetConstraintDoc(target, originalElement) ?: buildDefaultDoc(target) ?: ""
    }

    private fun buildQuickInfo(element: PsiElement?): String? {
        val name = extractSymbolName(element) ?: return null

        // Check for constraint documentation first, otherwise use default
        val docEntry = PicatConstraintDocs.getDocumentation(name)
        return docEntry?.let { "${it.signature} (${it.module})" }
            ?: (element as? PicatAtom)?.name
                ?.let { StringUtil.escapeXmlEntities(it) }
                ?.takeIf { it.isNotEmpty() }
                ?.let { "Picat symbol: $it" }
    }

    private fun tryGetConstraintDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val name = extractSymbolName(element) ?: extractSymbolName(originalElement)
        if (name == null) return null

        val arity = extractArity(element)
        val docEntry = PicatConstraintDocs.getDocumentation(name, arity)

        return docEntry?.let { formatDocEntry(it) }
    }

    private fun buildDefaultDoc(element: PsiElement?): String? {
        return when (element) {
            is PicatAtom -> {
                val name = StringUtil.escapeXmlEntities(element.name ?: "")
                if (name.isNotEmpty()) {
                    val builder = HtmlBuilder()
                    builder.append("<b>Picat symbol:</b> $name")
                    builder.toString()
                } else null
            }
            else -> null
        }
    }

    private fun extractSymbolName(element: PsiElement?): String? {
        return when (element) {
            is PicatAtom -> element.name
            else -> {
                // Try to find parent atom
                val atom = PsiTreeUtil.getParentOfType(element, PicatAtom::class.java)
                atom?.name ?: element?.text?.takeIf { it.isNotBlank() && it.length < MAX_SYMBOL_LENGTH }
            }
        }
    }

    /** Constants for documentation provider. */
    companion object {
        private const val MAX_SYMBOL_LENGTH = 50
    }

    private fun extractArity(element: PsiElement?): Int? {
        if (element == null) return null
        return PicatPsiUtil.getHeadArity(element)
    }

    private fun formatDocEntry(entry: PicatDocEntry): String {
        val builder = StringBuilder()

        // Header with signature
        builder.append("<div class='definition'>")
        builder.append("<b>${StringUtil.escapeXmlEntities(entry.signature)}</b>")
        builder.append("</div>")

        // Module and category
        builder.append("<div class='section'>")
        builder.append("<i>Module: ${entry.module}</i> | ")
        builder.append("<i>${formatCategory(entry.category)}</i>")
        builder.append("</div><br/>")

        // Description
        builder.append("<p>${StringUtil.escapeXmlEntities(entry.description)}</p>")

        // Parameters
        if (entry.parameters.isNotEmpty()) {
            builder.append("<br/><b>Parameters:</b><ul>")
            entry.parameters.forEach { param ->
                builder.append("<li><code>${StringUtil.escapeXmlEntities(param.name)}</code>: ")
                builder.append("${StringUtil.escapeXmlEntities(param.description)}</li>")
            }
            builder.append("</ul>")
        }

        // Examples
        if (entry.examples.isNotEmpty()) {
            builder.append("<br/><b>Examples:</b><pre>")
            entry.examples.forEach { ex ->
                builder.append("${StringUtil.escapeXmlEntities(ex)}\n")
            }
            builder.append("</pre>")
        }

        // See also
        if (entry.seeAlso.isNotEmpty()) {
            builder.append("<br/><b>See also:</b> ")
            builder.append(entry.seeAlso.joinToString(", ") { StringUtil.escapeXmlEntities(it) })
        }

        return builder.toString()
    }

    private fun formatCategory(category: DocCategory): String {
        return when (category) {
            DocCategory.DOMAIN_FUNCTION -> "Domain Function"
            DocCategory.TABLE_CONSTRAINT -> "Table Constraint"
            DocCategory.GLOBAL_CONSTRAINT -> "Global Constraint"
            DocCategory.SOLVER_FUNCTION -> "Solver Function"
            DocCategory.ARITHMETIC_FUNCTION -> "Arithmetic Function"
            DocCategory.SOLVER_OPTION -> "Solver Option"
        }
    }

    private fun resolveIfReference(element: PsiElement?): PsiElement? {
        val reference = element?.reference
        return reference?.resolve()
    }
}
