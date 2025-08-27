package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Utilities for common PSI queries over a Picat syntax tree.
 *
 * Rationale:
 * - Provide stable and centralized helpers for name/arity and qualifiers, to be reused by indices,
 *   navigation, completion, and inspections. This reduces duplication and ensures consistent logic.
 */
object PicatPsiUtil {
    // NameIdentifierOwner support for PicatAtom
    @JvmStatic
    fun getName(element: PicatAtom?): String? = element?.nameIdentifier?.text

    @JvmStatic
    fun setName(element: PicatAtom, @Suppress("UNUSED_PARAMETER") newName: String?): PsiElement {
        // Name changes are currently unsupported; return an element unchanged to keep PSI stable.
        // Future: implement element factory rename when grammar token types are finalized.
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: PicatAtom): PsiElement? {
        // Prefer specific token types to ensure stability across atoms:
        // IDENTIFIER, SINGLE_QUOTED_ATOM, and keyword-atoms MIN/MAX
        val node = element.node
        node.getChildren(null).forEach { child ->
            val t = child.elementType
            when (t) {
                PicatTokenTypes.IDENTIFIER,
                PicatTokenTypes.SINGLE_QUOTED_ATOM,
                PicatTokenTypes.MIN,
                PicatTokenTypes.MAX -> return child.psi
            }
        }
        // Fallback: first identifier-like token by text
        return node.getChildren(null).map { it.psi }.firstOrNull { psi ->
            val txt = psi.text
            txt.isNotBlank() && IDENT_REGEX.matches(txt)
        }
    }

    /** Returns the predicate/function name at the head of a rule/definition, or null if not resolvable. */
    @JvmStatic
    fun getHeadName(element: PsiElement): String? {
        val head = when (element) {
            is PicatPredicateRule -> element.head
            is PicatPredicateClause -> element.predicateRule?.head
            is PicatPredicateDefinition -> element.predicateClause.predicateRule?.head
            else -> null
        } ?: return null
        return extractNameFromHead(head)
    }

    /** Returns the arity of the head (number of arguments), or null if unknown. */
    @JvmStatic
    fun getHeadArity(element: PsiElement): Int? {
        val head = when (element) {
            is PicatPredicateRule -> element.head
            is PicatPredicateClause -> element.predicateRule?.head
            is PicatPredicateDefinition -> element.predicateClause.predicateRule?.head
            else -> null
        } ?: return null
        return extractArityFromHead(head)
    }

    /** Attempt to get a stable identifying string for a declaration: name/arity form. */
    @JvmStatic
    fun getStableSignature(element: PsiElement): String? {
        val name = getHeadName(element)
        val arity = getHeadArity(element)
        return if (name != null && arity != null) "$name/$arity" else null
    }

    /** Extract module qualifier if present for a qualified call or atom like module::name/arity. */
    @JvmStatic
    @Suppress("unused")
    fun getModuleQualifier(element: PsiElement): String? {
        // Depending on grammar, qualified names may appear in head or in call expressions.
        // Keep this conservative and nullable.
        val head = when (element) {
            is PicatPredicateRule -> element.head
            is PicatPredicateClause -> element.predicateRule?.head
            is PicatPredicateDefinition -> element.predicateClause.predicateRule?.head
            else -> null
        } ?: return null
        return extractModuleQualifier(head)
    }

    // --- Internal helpers over generated PSI ---
    private fun extractNameFromHead(head: PicatHead): String? {
        // The PicatHead likely contains a functor/atom and arguments.
        // Generated PSI names vary; try common shapes conservatively.
        // Prefer atom if present.
        head.node.getChildren(null).forEach { child ->
            when (val psi = child.psi) {
                is PicatAtom -> return psi.text
            }
        }
        // Fallback to the first identifier-like token in head text
        val m = IDENT_REGEX.find(head.text)
        return m?.value
    }

    private fun extractArityFromHead(head: PicatHead): Int {
        // Count top-level arguments within parentheses after functor.
        val text = head.text
        val l = text.indexOf('(')
        val r = if (l >= 0) text.lastIndexOf(')') else -1
        val arity = if (l < 0 || r < 0 || r <= l) {
            0 // no args -> arity 0
        } else {
            val inside = text.substring(l + 1, r)
            if (inside.isBlank()) 0
            else inside.count { it == ',' } + 1 // Count commas at the top level
        }
        return arity
    }

    private fun extractModuleQualifier(head: PicatHead): String? {
        // Look for pattern module::name
        val text = head.text
        return if ("::" in text) text.substringBefore("::").takeIf { it.isNotBlank() } else null
    }

    private val IDENT_REGEX = Regex("[A-Za-z_][A-Za-z0-9_]*")
}
