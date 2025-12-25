package com.github.avrilfanomar.picatplugin.language.formatter

/**
 * Handles operator protection/restoration and spacing rules according to Picat code style settings.
 */
internal class PicatOperatorFormatter(private val settings: PicatCodeStyleSettings) {
    val equalityOps = listOf(
        "__SPACELESS_EQUIV_OP__",
        "__TRIPLE_EQUAL_OP__",
        "__STRICT_NOT_EQUAL_OP__",
        "__DOUBLE_EQUAL_OP__",
        "__NOT_EQUAL_OP__",
        "__EQUAL_NUM_OP__",
        "__NOT_EQUAL_NUM_OP__"
    )
    val relationalOps = listOf(
        "__LE_OP__",
        "__GE_OP__",
        "__LE_PROLOG_OP__",
        "__DIV_LEFT_OP__",
        "__DIV_RIGHT_OP__",
        "<",
        ">"
    )
    val bitwiseOps = listOf(
        "__URSHIFT_OP__",
        "__RSHIFT_OP__",
        "__LSHIFT_OP__",
        "__BIT_AND_OP__",
        "__BIT_OR_OP__",
        "\\^",
        "~"
    )
    val ruleOps = listOf(
        "__BACKTRACKABLE_ARROW_OP__",
        "__ARROW_OP__",
        "__PROLOG_RULE_OP__",
        "__HASH_ARROW_OP__",
        "__HASH_BICONDITIONAL_OP__"
    )
    val controlFlowOps = listOf(
        "__IF_THEN_OP__"
    )
    val constraintOps = listOf(
        "__HASH_EQUAL_OP__",
        "__HASH_NOT_EQUAL_OP__",
        "__HASH_GE_OP__",
        "__HASH_LE_OP__",
        "__HASH_LE_ALT_OP__",
        "__HASH_GT_OP__",
        "__HASH_LT_OP__",
        "__HASH_BICONDITIONAL_OP__",
        "__HASH_OR_OP__",
        "__HASH_XOR_OP__",
        "__HASH_AND_OP__",
        "__HASH_NOT_OP__"
    )
    val termOps = listOf(
        "__AT_LESS_EQUAL_PROLOG_OP__",
        "__AT_LESS_EQUAL_OP__",
        "__AT_GREATER_EQUAL_OP__",
        "__AT_LESS_OP__",
        "__AT_GREATER_OP__"
    )
    
    fun handleSpecialOperators(code: String): String {
        var result = code
        result = result.replace("#<=>", "__HASH_BICONDITIONAL_OP__")
        result = result.replace("#=>", "__HASH_ARROW_OP__")
        result = result.replace("#\\/", "__HASH_OR_OP__")
        result = result.replace("#^", "__HASH_XOR_OP__")
        result = result.replace("#\\", "__HASH_AND_OP__")
        result = result.replace("#~", "__HASH_NOT_OP__")
        result = result.replace("#!=", "__HASH_NOT_EQUAL_OP__")
        result = result.replace("#>=", "__HASH_GE_OP__")
        result = result.replace("#=<", "__HASH_LE_OP__")
        result = result.replace("#<=", "__HASH_LE_ALT_OP__")
        result = result.replace("#>", "__HASH_GT_OP__")
        result = result.replace("#<", "__HASH_LT_OP__")
        result = result.replace("#=", "__HASH_EQUAL_OP__")
        result = result.replace("@<=", "__AT_LESS_EQUAL_OP__")
        result = result.replace("@=<", "__AT_LESS_EQUAL_PROLOG_OP__")
        result = result.replace("@>=", "__AT_GREATER_EQUAL_OP__")
        result = result.replace("@<", "__AT_LESS_OP__")
        result = result.replace("@>", "__AT_GREATER_OP__")
        result = result.replace("=\\=", "__NOT_EQUAL_NUM_OP__")
        result = result.replace("=:=", "__EQUAL_NUM_OP__")
        result = result.replace("//", "__INT_DIV_OP__")
        result = result.replace("/>", "__DIV_RIGHT_OP__")
        result = result.replace("/<", "__DIV_LEFT_OP__")
        result = result.replace("\\/", "__BIT_OR_OP__")
        result = result.replace("/\\", "__BIT_AND_OP__")
        result = result.replace("<=>", "__SPACELESS_EQUIV_OP__")
        result = result.replace("<=", "__LE_OP__")
        result = result.replace("=<", "__LE_PROLOG_OP__")
        result = result.replace(">=", "__GE_OP__")
        result = result.replace("=..", "__UNIV_OP__")
        result = result.replace("!==", "__STRICT_NOT_EQUAL_OP__")
        result = result.replace("!=", "__NOT_EQUAL_OP__")
        result = result.replace("===", "__TRIPLE_EQUAL_OP__")
        result = result.replace("==", "__DOUBLE_EQUAL_OP__")
        result = result.replace("<<", "__LSHIFT_OP__")
        result = result.replace(">>>", "__URSHIFT_OP__")
        result = result.replace(">>", "__RSHIFT_OP__")
        result = result.replace("++", "__CONCAT_OP__")
        result = result.replace("--", "__DECREMENT_OP__")
        result = result.replace("**", "__POWER_OP__")
        result = result.replace("&&", "__AND_OP__")
        result = result.replace("||", "__OR_OP__")
        result = result.replace("::", "__DOUBLE_COLON_OP__")
        result = result.replace(":=", "__ASSIGN_OP__")
        result = result.replace("?:", "__TERNARY_COLON_OP__")
        result = result.replace("?=>", "__BACKTRACKABLE_ARROW_OP__")
        result = result.replace("->", "__IF_THEN_OP__")  // Prolog-style if-then-else
        result = result.replace("=>", "__ARROW_OP__")
        result = result.replace(":-", "__PROLOG_RULE_OP__")
        result = result.replace("#", "__HASH_OP__")
        return result
    }

    fun restoreSpecialOperators(code: String): String {
        var result = code
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", "?=>")
        result = result.replace("__IF_THEN_OP__", "->")  // Prolog-style if-then-else
        result = result.replace("__ARROW_OP__", "=>")
        result = result.replace("__AT_LESS_EQUAL_OP__", "@<=")
        result = result.replace("__AT_LESS_EQUAL_PROLOG_OP__", "@=<")
        result = result.replace("__AT_GREATER_EQUAL_OP__", "@>=")
        result = result.replace("__AT_LESS_OP__", "@<")
        result = result.replace("__AT_GREATER_OP__", "@>")
        result = result.replace("__HASH_BICONDITIONAL_OP__", "#<=>")
        result = result.replace("__HASH_ARROW_OP__", "#=>")
        result = result.replace("__HASH_EQUAL_OP__", "#=")
        result = result.replace("__HASH_NOT_EQUAL_OP__", "#!=")
        result = result.replace("__HASH_GE_OP__", "#>=")
        result = result.replace("__HASH_LE_OP__", "#=<")
        result = result.replace("__HASH_LE_ALT_OP__", "#<=")
        result = result.replace("__HASH_GT_OP__", "#>")
        result = result.replace("__HASH_LT_OP__", "#<")
        result = result.replace("__HASH_OP__", "#")
        result = result.replace("__HASH_OR_OP__", "#\\/")
        result = result.replace("__HASH_XOR_OP__", "#^")
        result = result.replace("__HASH_AND_OP__", "#\\")
        result = result.replace("__HASH_NOT_OP__", "#~")
        result = result.replace("__DOUBLE_EQUAL_OP__", "==")
        result = result.replace("__NOT_EQUAL_OP__", "!=")
        result = result.replace("__STRICT_NOT_EQUAL_OP__", "!==")
        result = result.replace("__TRIPLE_EQUAL_OP__", "===")
        result = result.replace("__LE_OP__", "<=")
        result = result.replace("__LE_PROLOG_OP__", "=<")
        result = result.replace("__GE_OP__", ">=")
        result = result.replace("__LSHIFT_OP__", "<<")
        result = result.replace("__RSHIFT_OP__", ">>")
        result = result.replace("__URSHIFT_OP__", ">>>")
        result = result.replace("__CONCAT_OP__", "++")
        result = result.replace("__DOUBLE_COLON_OP__", "::")
        result = result.replace("__AND_OP__", "&&")
        result = result.replace("__OR_OP__", "||")
        result = result.replace("__UNIV_OP__", "=..")
        result = result.replace("__NOT_EQUAL_NUM_OP__", "=\\=")
        result = result.replace("__EQUAL_NUM_OP__", "=:=")
        result = result.replace("__ASSIGN_OP__", ":=")
        result = result.replace("__INT_DIV_OP__", "//")
        result = result.replace("__DIV_RIGHT_OP__", "/>")
        result = result.replace("__DIV_LEFT_OP__", "/<")
        result = result.replace("__BIT_OR_OP__", "\\/")
        result = result.replace("__BIT_AND_OP__", "/\\")
        result = result.replace("__SPACELESS_EQUIV_OP__", "<=>")
        result = result.replace("__DECREMENT_OP__", "--")
        result = result.replace("__POWER_OP__", "**")
        result = result.replace("__TERNARY_COLON_OP__", "?:")
        return result
    }

    fun addSpacesAroundOperators(code: String): String {
        var result = code

        fun applyForOps(text: String, ops: List<String>, space: Boolean): String {
            var t = text
            val sorted = ops.sortedByDescending { it.replace("\\\\", "").length }
            for (raw in sorted) {
                val op = raw
                val pattern = Regex("\\s*(" + op + ")\\s*")
                t = if (space) {
                    t.replace(pattern, " $1 ")
                } else {
                    t.replace(pattern, "$1")
                }
            }
            return t
        }

        val assignmentOps = listOf("__ASSIGN_OP__", "=")

        val additiveOps = listOf("__CONCAT_OP__", "\\+", "-")
        val multiplicativeOps = listOf("__INT_DIV_OP__", "\\*", "/", "%")
        val logicalOps = listOf("__AND_OP__", "__OR_OP__")

        val typeConstraintOps = listOf("__DOUBLE_COLON_OP__")
        val rangeOps = listOf("\\.\\.")

        result = applyForOps(result, assignmentOps, settings.spaceAroundAssignmentOperators)
        result = applyForOps(result, equalityOps, settings.spaceAroundEqualityOperators)
        result = applyForOps(result, relationalOps, settings.spaceAroundRelationalOperators)
        result = applyForOps(result, additiveOps, settings.spaceAroundAdditiveOperators)
        result = applyForOps(result, multiplicativeOps, settings.spaceAroundMultiplicativeOperators)
        result = applyForOps(result, logicalOps, settings.spaceAroundLogicalOperators)
        result = applyForOps(result, bitwiseOps, settings.spaceAroundBitwiseOperators)
        result = applyForOps(result, ruleOps, settings.spaceBeforeRuleOperators)
        result = applyForOps(result, controlFlowOps, true)  // Always add spaces around ->
        result = applyForOps(result, constraintOps, settings.spaceAroundConstraintOperators)
        result = applyForOps(result, termOps, settings.spaceAroundTermComparisonOperators)
        result = applyForOps(result, typeConstraintOps, settings.spaceAroundTypeConstraintOperator)
        result = applyForOps(result, rangeOps, settings.spaceAroundRangeOperator)

        result = addSpacesAroundColonsInListComprehensions(result)
        result = fixSpacingInStructures(result)
        return result
    }

    private fun fixSpacingInStructures(code: String): String {
        var result = code
        result = result.replace("[ ", "[")
        result = result.replace(" ]", "]")
        result = result.replace("( ", "(")
        result = result.replace(" )", ")")
        val before = if (settings.spaceBeforeComma) " " else ""
        val after = if (settings.spaceAfterComma) " " else ""
        result = result.replace(Regex("\\s*,\\s*"), "$before,$after")
        result = result.replace(", ]", ",]")
        result = result.replace(", )", ",)")
        result = result.replace(", \n", ",\n")
        return result
    }

    fun cleanupDoubleSpaces(code: String): String {
        var result = code
        while (result.contains("  ")) {
            result = result.replace("  ", " ")
        }
        return result
    }

    fun cleanupDoubleSpacesOutsideLiterals(code: String): String {
        val (masked, lits) = PicatLiteralTools.protectLiterals(code)
        val cleaned = cleanupDoubleSpaces(masked)
        return PicatLiteralTools.restoreLiterals(cleaned, lits)
    }

    // Colon spacing inside list comprehensions / brackets
    private data class ColonSpacingState(
        var inString: Boolean = false,
        var inListComprehension: Boolean = false,
        var bracketDepth: Int = 0,
    )

    private fun addSpacesAroundColonsInListComprehensions(code: String): String {
        val state = ColonSpacingState()
        val chars = code.toCharArray()
        for (i in chars.indices) {
            val char = chars[i]
            updateColonSpacingState(char, state)
            if (char == ':' && !state.inString && state.inListComprehension) {
                return if (settings.spaceAroundColon) {
                    if (needsSpacing(i, chars)) processColonSpacingAdd(code, i, chars) else code
                } else {
                    if (hasSpacesAroundColon(i, chars)) processColonSpacingRemove(code, i, chars) else code
                }
            }
        }
        return code
    }

    private fun updateColonSpacingState(char: Char, state: ColonSpacingState) {
        when (char) {
            '"' -> state.inString = !state.inString
            '\'' -> state.inString = !state.inString
            '[' -> if (!state.inString) {
                state.bracketDepth++
                state.inListComprehension = true
            }
            ']' -> if (!state.inString) {
                state.bracketDepth--
                if (state.bracketDepth == 0) state.inListComprehension = false
            }
        }
    }

    private fun needsSpacing(index: Int, chars: CharArray): Boolean {
        val needsBefore = index > 0 && chars[index - 1] != ' '
        val needsAfter = index < chars.size - 1 && chars[index + 1] != ' '
        return needsBefore || needsAfter
    }

    private fun hasSpacesAroundColon(index: Int, chars: CharArray): Boolean {
        val hasBefore = index > 0 && chars[index - 1] == ' '
        val hasAfter = index < chars.size - 1 && chars[index + 1] == ' '
        return hasBefore || hasAfter
    }

    private fun processColonSpacingAdd(code: String, index: Int, chars: CharArray): String {
        val before = if (index > 0 && chars[index - 1] != ' ') " " else ""
        val after = if (index < chars.size - 1 && chars[index + 1] != ' ') " " else ""
        val prefix = code.substring(0, index)
        val suffix = code.substring(index + 1)
        val result = "$prefix$before:$after$suffix"
        return addSpacesAroundColonsInListComprehensions(result)
    }

    private fun processColonSpacingRemove(code: String, index: Int, chars: CharArray): String {
        var start = index - 1
        while (start >= 0 && chars[start] == ' ') start--
        var end = index + 1
        while (end < chars.size && chars[end] == ' ') end++
        val prefix = code.substring(0, start + 1)
        val suffix = code.substring(end)
        val result = "$prefix:$suffix"
        return addSpacesAroundColonsInListComprehensions(result)
    }
}
