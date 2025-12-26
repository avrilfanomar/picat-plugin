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
    /**
     * Gets the name of a Picat atom element.
     * @param element the atom element to get the name from
     * @return the name text of the atom, or null if not available
     */
    @JvmStatic
    fun getName(element: PicatAtom?): String? = element?.nameIdentifier?.text

    /**
     * Sets the name of a Picat atom element.
     * Currently unsupported - returns the element unchanged to maintain PSI stability.
     * @param element the atom element to rename
     * @param newName the new name to set (currently ignored)
     * @return the unchanged PSI element
     */
    @JvmStatic
    fun setName(element: PicatAtom, @Suppress("unused") newName: String?): PsiElement {
        // Name changes are currently unsupported; return an element unchanged to keep PSI stable.
        // Future: implement element factory rename when grammar token types are finalized.
        return element
    }

    /**
     * Gets the name identifier element of a Picat atom.
     * Searches for specific token types like IDENTIFIER, SINGLE_QUOTED_ATOM, MIN, MAX.
     * @param element the atom element to get the identifier from
     * @return the name identifier PSI element, or null if not found
     */
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

    /**
     * Extract the module qualifier from a dot access expression (e.g., "bp" from "bp.predicate(...)").
     * @param dotAccess the PicatDotAccess element
     * @return the module name if the dot access is qualified, null otherwise
     */
    @JvmStatic
    fun getDotAccessModuleQualifier(dotAccess: PicatDotAccess): String? {
        // Navigate up the PSI tree: primary_expr -> base_expr (atom_no_args) + postfix_ops
        val atom = (dotAccess.parent?.parent?.parent as? PicatPrimaryExpr)
            ?.baseExpr?.atomNoArgs?.atom ?: return null
        return atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('\'', '"', '`')
    }

    /**
     * Extract the predicate/function name from a dot access expression (e.g., "b_XOR_ccf" from "bp.b_XOR_ccf(...)").
     * @param dotAccess the PicatDotAccess element
     * @return the predicate/function name, or null if not available
     */
    @JvmStatic
    fun getDotAccessName(dotAccess: PicatDotAccess): String? {
        val rawText = dotAccess.dotIdentifier?.text ?: dotAccess.dotSingleQuotedAtom?.text ?: return null
        val withoutDot = if (rawText.startsWith(".")) rawText.substring(1) else rawText
        return withoutDot.trim('\'', '"', '`')
    }

    /**
     * Get the arity of a dot access call (number of arguments).
     * @param dotAccess the PicatDotAccess element
     * @return the arity, or 0 if no arguments
     */
    @JvmStatic
    fun getDotAccessArity(dotAccess: PicatDotAccess): Int {
        return dotAccess.argumentList.size
    }

    private val IDENT_REGEX = Regex("[A-Za-z_][A-Za-z0-9_]*")
}
