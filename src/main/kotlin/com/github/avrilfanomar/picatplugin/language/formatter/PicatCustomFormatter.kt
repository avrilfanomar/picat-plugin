package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings

class PicatCustomFormatter(
    private val settings: CodeStyleSettings,
) {
    private val picatSettings: PicatCodeStyleSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

    companion object {
        private const val LINE_BREAK_THRESHOLD = 20
    }

    /**
     * Top-level entry.
     * Normalizes input, preserves comments while spacing operators, and then applies
     * structural formatting (indentation of rule bodies, if/then/else, foreach, try/catch, etc.)
     * The implementation below was updated to match the Picat BNF's operator set and common control keywords.
     */
    fun format(code: String): String {
        val normalizedCode = code

        val result = when {
            isSimpleRule(normalizedCode) -> {
                var simpleResult = formatPreservingComments(normalizedCode)
                val contentAfterArrow = simpleResult.substringAfter("=>").trim()
                val shouldAddLineBreak = if (picatSettings.keepLineBreakAfterRuleOperators) {
                    true
                } else {
                    // Keep previous heuristic when setting is disabled
                    contentAfterArrow.length > LINE_BREAK_THRESHOLD || contentAfterArrow.contains("println")
                }

                if (shouldAddLineBreak) {
                    simpleResult = addLineBreaksAfterRuleOperators(simpleResult)
                    simpleResult = addIndentation(simpleResult)
                } else {
                    simpleResult = simpleResult.replace("=>", "=> ")
                    simpleResult = simpleResult.replace("=>  ", "=> ")
                }
                simpleResult
            }

            isPredicate(normalizedCode) -> {
                formatPreservingComments(normalizedCode)
            }

            else -> {
                val preserved = formatPreservingComments(normalizedCode)
                if (preserved == normalizedCode) {
                    preserved
                } else {
                    formatCode(preserved)
                }
            }
        }

        return result
    }

    /**
     * Protect comments, protect multi-char/special operators, add spaces around operators,
     * then restore special operators and re-attach comments.
     */
    private fun formatPreservingComments(input: String): String {
        val lines = input.split("\n")
        val result = StringBuilder()
        var inBlockComment = false
        for ((index, rawLine) in lines.withIndex()) {
            val line = rawLine
            if (inBlockComment) {
                // Preserve multi-line comment lines verbatim (except trailing spaces)
                result.append(line.trimEnd())
                // Only close block comment on an actual, unquoted terminator
                if (line.indexOf("*/") >= 0) {
                    inBlockComment = false
                }
                if (index < lines.lastIndex) result.append("\n")
                continue
            }

            // If the line starts a block comment (outside of strings), preserve it as-is
            val blockStartIndex = findUnquotedToken(line, "/*")
            if (blockStartIndex >= 0) {
                inBlockComment =
                    line.indexOf("*/", blockStartIndex + 2) < 0 // enter block unless this line also closes it
                result.append(line.trimEnd())
                if (index < lines.lastIndex) result.append("\n")
                continue
            }

            val split = splitCodeAndComment(line)
            val codePart = split.first
            val commentPart = split.second
            val trailingWsBeforeComment = split.third

            // Protect string and char literals from operator processing
            val (maskedCode, literals) = protectLiterals(codePart)

            var formattedCode = handleSpecialOperators(maskedCode)
            formattedCode = addSpacesAroundOperators(formattedCode)
            formattedCode = cleanupDoubleSpaces(formattedCode)
            formattedCode = restoreSpecialOperators(formattedCode)

            // Restore literals before any final whitespace cleanup to avoid touching their content
            formattedCode = restoreLiterals(formattedCode, literals)

            // Final double-space cleanup outside of literals only
            formattedCode = cleanupDoubleSpacesOutsideLiterals(formattedCode)

            // Append code part; if there's a trailing whitespace before an inline comment,
            // preserve exactly that spacing between code and comment
            if (commentPart != null) {
                result.append(formattedCode.trimEnd())
                result.append(trailingWsBeforeComment)
                result.append(commentPart)
            } else {
                result.append(formattedCode)
            }

            if (index < lines.lastIndex) result.append("\n")
        }
        return result.toString()
    }

    private fun splitCodeAndComment(line: String): Triple<String, String?, String> {
        val commentIndex = findUnquotedToken(line, "%")
        return if (commentIndex >= 0) {
            val rawCodePart = line.substring(0, commentIndex)
            val commentPart = line.substring(commentIndex)
            val trailingWs = rawCodePart.takeLastWhile { it == ' ' || it == '\t' }
            val codePart = rawCodePart.dropLast(trailingWs.length)
            Triple(codePart, commentPart, trailingWs)
        } else {
            Triple(line, null, "")
        }
    }

    /**
     * Finds the index of the given token in the line, ignoring occurrences inside string/char literals.
     * Returns -1 if not found.
     */
    private fun findUnquotedToken(line: String, token: String): Int {
        if (token.isEmpty()) return -1
        var i = 0
        var inString = false
        var quote: Char? = null
        while (i <= line.length - token.length) {
            val ch = line[i]
            if (inString) {
                if (ch == '\\') {
                    // skip escaped char
                    i += if (i + 1 < line.length) 2 else 1
                    continue
                }
                if (ch == quote) {
                    inString = false
                    quote = null
                    i++
                    continue
                }
                i++
                continue
            } else {
                if (ch == '"' || ch == '\'') {
                    inString = true
                    quote = ch
                    i++
                    continue
                }
                // check token at current position
                if (line.regionMatches(i, token, 0, token.length)) {
                    return i
                }
                i++
            }
        }
        return -1
    }

    /* ========== Literal masking to prevent formatting inside strings/atoms ========== */
    private fun protectLiterals(code: String): Pair<String, List<String>> {
        if (code.isEmpty()) return code to emptyList()
        val saved = mutableListOf<String>()
        val out = StringBuilder()
        var i = 0
        while (i < code.length) {
            val ch = code[i]
            if (ch == '"' || ch == '\'') {
                val quote = ch
                val j = findJ(i, code, quote)
                val literal = code.substring(i, minOf(j, code.length))
                val placeholder = "__LIT_${'$'}${saved.size}__"
                saved += literal
                out.append(placeholder)
                i = j
            } else {
                out.append(ch)
                i++
            }
        }
        return out.toString() to saved
    }

    private fun findJ(i: Int, code: String, quote: Char): Int {
        var j = i + 1
        // scan until matching quote, honoring backslash escapes
        while (j < code.length) {
            val cj = code[j]
            if (cj == '\\') {
                // skip escaped next char if any
                j += if (j + 1 < code.length) 2 else 1
                continue
            }
            if (cj == quote) {
                j++ // include closing quote
                break
            }
            j++
        }
        if (j > code.length) j = code.length
        return j
    }

    private fun restoreLiterals(code: String, literals: List<String>): String {
        var result = code
        for ((idx, lit) in literals.withIndex()) {
            result = result.replace("__LIT_${'$'}${idx}__", lit)
        }
        return result
    }

    /* ========== Structural formatting (indentation / block handling) ========== */
    private fun formatCode(code: String): String {
        val lines = code.split("\n")
        val result = StringBuilder()
        val state = FormatState()
        var inBlockComment = false

        for ((idx, raw) in lines.withIndex()) {
            val line = raw
            if (inBlockComment) {
                result.append(line)
                if (line.indexOf("*/") >= 0) {
                    inBlockComment = false
                }
                if (idx < lines.lastIndex) result.append("\n")
                continue
            }
            val blockStart = findUnquotedToken(line, "/*")
            if (blockStart >= 0) {
                inBlockComment = line.indexOf("*/", blockStart + 2) < 0
                result.append(line)
                if (idx < lines.lastIndex) result.append("\n")
                continue
            }

            processLine(line.trim(), result, state)
        }

        return result.toString().trim()
    }

    private fun processLine(line: String, result: StringBuilder, state: FormatState) {
        when {
            line.isEmpty() -> result.append("\n")
            isRuleBodyStart(line) -> handleRuleBodyStart(line, result, state)
            state.inRuleBody -> handleRuleBodyLine(line, result, state)
            else -> result.append(line).append("\n")
        }
    }

    private fun isRuleBodyStart(line: String) =
        line.endsWith("=>") || line.endsWith(" ?=>") || line.endsWith("#=>") || line.endsWith("#<=>")

    private fun handleRuleBodyStart(line: String, result: StringBuilder, state: FormatState) {
        result.append(line).append("\n")
        state.inRuleBody = true
        state.indentLevel = 1
    }

    @Suppress("CyclomaticComplexMethod")//kinda false-positive
    private fun handleRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        when {
            isIfThenStatement(line) -> handleIfThenStatement(line, result, state)
            line.startsWith("elseif") || line.startsWith("else if") -> handleElseIfStatement(line, result, state)
            line.startsWith("else") -> handleElseStatement(line, result, state)
            line.contains(" then") -> handleThenStatement(line, result, state)
            line.startsWith("foreach") || line.startsWith("for ") -> handleForeachStatement(line, result, state)
            line.startsWith("while") || line.startsWith("loop") -> handleLoopStatement(line, result, state)
            line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally")
                -> handleTryCatchStatement(line, result, state)

            isEndStatement(line) -> handleEndStatement(line, result, state)
            line.endsWith(".") -> handleRuleBodyEnd(line, result, state)
            else -> handleRegularRuleBodyLine(line, result, state)
        }
    }

    private fun isIfThenStatement(line: String) =
        (line.startsWith("if ") || line.startsWith("if(")) && line.contains(" then")

    private fun handleIfThenStatement(line: String, result: StringBuilder, state: FormatState) {
        val base = state.indentLevel
        // entering an if-block: record block type and its base level for proper else/elseif alignment
        state.blockStack.add("if")
        state.ifBaseStack.add(base)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleElseIfStatement(line: String, result: StringBuilder, state: FormatState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(getIndentation(base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleElseStatement(line: String, result: StringBuilder, state: FormatState) {
        // place else at the same level as the matching if
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(getIndentation(base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleThenStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleForeachStatement(line: String, result: StringBuilder, state: FormatState) {
        state.blockStack.add("foreach")
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleLoopStatement(line: String, result: StringBuilder, state: FormatState) {
        state.blockStack.add("loop")
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleTryCatchStatement(line: String, result: StringBuilder, state: FormatState) {
        // Align try/catch/finally at the base level of the block
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private fun isEndStatement(line: String) =
        line == "end" || line == "end," || line == "end." || line == "endif" || line == "endloop"

    private fun handleEndStatement(line: String, result: StringBuilder, state: FormatState) {
        // close the last opened block (if any) and adjust stacks
        if (state.blockStack.isNotEmpty()) {
            val last = state.blockStack.removeAt(state.blockStack.lastIndex)
            if (last == "if" && state.ifBaseStack.isNotEmpty()) {
                state.ifBaseStack.removeAt(state.ifBaseStack.lastIndex)
            }
        }
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")

        // Only terminate the rule body when we hit a final 'end.' at the outermost level.
        // A bare 'end' closes an inner block (if/foreach/while) and must NOT leave the rule body.
        if (line == "end.") {
            state.inRuleBody = false
            state.indentLevel = 0
            state.blockStack.clear()
            state.ifBaseStack.clear()
        }
    }

    private fun handleRuleBodyEnd(line: String, result: StringBuilder, state: FormatState) {
        when {
            line.contains("=>") -> result.append(line).append("\n")
            line.matches(Regex(".*\\([^)]*\\)\\s*=.*\\.")) -> result.append(line).append("\n")
            else -> result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        }
        state.inRuleBody = false
        state.indentLevel = 0
    }

    private fun handleRegularRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private data class FormatState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        // Maintain stacks to properly handle nested blocks and align else/elseif
        val blockStack: MutableList<String> = mutableListOf(),
        val ifBaseStack: MutableList<Int> = mutableListOf()
    )

    /* ========== Heuristics that determine "kind" of single-line snippets ========== */

    private fun isSimpleRule(code: String) =
        code.contains("=>") && !code.contains("then") && !code.contains("else") &&
                !code.contains("foreach") && !code.contains("end") && !code.contains("\n") &&
                !code.contains("if") && !code.contains("try") && !code.contains("catch")

    private fun isPredicate(code: String) =
        !code.contains("\n") &&
                code.contains("(") && code.contains(")") &&
                // predicates frequently contain = or == or #= etc. Check for common assignment/equality tokens
                (code.contains("=") || code.contains("==") || code.contains("#=")) &&
                (code.trim().endsWith(".") || code.contains("=>") || code.contains("?=>"))

    /* ========== Special operators protection&restoration (expanded to match BNF) ========== */

    private fun handleSpecialOperators(code: String): String {
        var result = code

        // Protect the longest / most specific tokens first
        // Logical / constraint ops with # prefix
        result = result.replace("#<=>", "__HASH_BICONDITIONAL_OP__")
        result = result.replace("#=>", "__HASH_ARROW_OP__")
        // Hash-based boolean/bitwise ops
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

        // @-prefixed term comparison ops
        result = result.replace("@<=", "__AT_LESS_EQUAL_OP__")
        result = result.replace("@=<", "__AT_LESS_EQUAL_PROLOG_OP__")
        result = result.replace("@>=", "__AT_GREATER_EQUAL_OP__")
        result = result.replace("@<", "__AT_LESS_OP__")
        result = result.replace("@>", "__AT_GREATER_OP__")

        // Arithmetic / comparison / equality operators
        result = result.replace("=\\=", "__NOT_EQUAL_NUM_OP__")
        result = result.replace("=:=", "__EQUAL_NUM_OP__")

        // Standard multi-char ops (order matters: protect ops starting with '!' before plain equality)
        // Division-like multi-char (protect before single '/'): int divide and relational variations
        result = result.replace("//", "__INT_DIV_OP__")
        result = result.replace("/>", "__DIV_RIGHT_OP__")
        result = result.replace("/<", "__DIV_LEFT_OP__")
        // Bitwise or/and in Picat syntax (protect before single '/')
        result = result.replace("\\/", "__BIT_OR_OP__")
        result = result.replace("/\\", "__BIT_AND_OP__")

        result = result.replace("<=>", "__SPACELESS_EQUIV_OP__")
        result = result.replace("<=", "__LE_OP__")
        result = result.replace("=<", "__LE_PROLOG_OP__")
        result = result.replace(">=", "__GE_OP__")
        result = result.replace("=..", "__UNIV_OP__") // univ operator in some Prolog-like languages
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
        result = result.replace("?:", "__TERNARY_COLON_OP__") // defensive placeholder

        // Rule operators (backtrackable and normal)
        result = result.replace("?=>", "__BACKTRACKABLE_ARROW_OP__")
        result = result.replace("=>", "__ARROW_OP__")
        result = result.replace(":-", "__PROLOG_RULE_OP__")

        // '#' alone as operator (ensure it's protected after the multi-char # ops above)
        result = result.replace("#", "__HASH_OP__")

        return result
    }

    private fun restoreSpecialOperators(code: String): String {
        var result = code

        // Restore tokens without forcing spaces; spacing will be applied according to settings later.
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", "?=>")
        result = result.replace("__ARROW_OP__", "=>")

        // @ ops
        result = result.replace("__AT_LESS_EQUAL_OP__", "@<=")
        result = result.replace("__AT_LESS_EQUAL_PROLOG_OP__", "@=<")
        result = result.replace("__AT_GREATER_EQUAL_OP__", "@>=")
        result = result.replace("__AT_LESS_OP__", "@<")
        result = result.replace("__AT_GREATER_OP__", "@>")

        // Hash ops
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

        // common comparisons
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

        // defensive placeholders
        result = result.replace("__SPACELESS_EQUIV_OP__", "<=>")
        result = result.replace("__DECREMENT_OP__", "--")
        result = result.replace("__POWER_OP__", "**")
        result = result.replace("__TERNARY_COLON_OP__", "?:")

        return result
    }

    /* ========== Spacing algorithm for operators (applied to the protected code) ========== */
    private fun addSpacesAroundOperators(code: String): String {
        var result = code

        fun applyForOps(text: String, ops: List<String>, space: Boolean): String {
            var t = text
            // Sort longer first to avoid partial overlaps
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

        // Operator groups
        val assignmentOps = listOf("__ASSIGN_OP__", "=")
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
        val additiveOps = listOf("__CONCAT_OP__", "\\+", "-")
        val multiplicativeOps = listOf("__INT_DIV_OP__", "\\*", "/", "%")
        val logicalOps = listOf("__AND_OP__", "__OR_OP__")
        val bitwiseOps = listOf(
            "__URSHIFT_OP__",
            "__RSHIFT_OP__",
            "__LSHIFT_OP__",
            "__BIT_AND_OP__",
            "__BIT_OR_OP__",
            "\\^",
            "~"
        )
        // Use placeholders because operators are protected before spacing is applied
        val ruleOps =
            listOf("__BACKTRACKABLE_ARROW_OP__", "__ARROW_OP__", "__PROLOG_RULE_OP__", "__HASH_ARROW_OP__", "__HASH_BICONDITIONAL_OP__")
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
        val typeConstraintOps = listOf("__DOUBLE_COLON_OP__")
        val rangeOps = listOf("\\.\\.")

        // Apply per-setting spacing
        result = applyForOps(result, assignmentOps, picatSettings.spaceAroundAssignmentOperators)
        result = applyForOps(result, equalityOps, picatSettings.spaceAroundEqualityOperators)
        result = applyForOps(result, relationalOps, picatSettings.spaceAroundRelationalOperators)
        result = applyForOps(result, additiveOps, picatSettings.spaceAroundAdditiveOperators)
        result = applyForOps(result, multiplicativeOps, picatSettings.spaceAroundMultiplicativeOperators)
        result = applyForOps(result, logicalOps, picatSettings.spaceAroundLogicalOperators)
        result = applyForOps(result, bitwiseOps, picatSettings.spaceAroundBitwiseOperators)

        // Rule operators: interpret setting as spacing around rule operators
        result = applyForOps(result, ruleOps, picatSettings.spaceBeforeRuleOperators)
        result = applyForOps(result, constraintOps, picatSettings.spaceAroundConstraintOperators)
        result = applyForOps(result, termOps, picatSettings.spaceAroundTermComparisonOperators)
        result = applyForOps(result, typeConstraintOps, picatSettings.spaceAroundTypeConstraintOperator)
        result = applyForOps(result, rangeOps, picatSettings.spaceAroundRangeOperator)

        // Colon in list comprehensions handled separately
        result = addSpacesAroundColonsInListComprehensions(result)
        result = fixSpacingInStructures(result)
        return result
    }

    private fun fixSpacingInStructures(code: String): String {
        var result = code
        // normalize brackets and parentheses spacing
        result = result.replace("[ ", "[")
        result = result.replace(" ]", "]")
        result = result.replace("( ", "(")
        result = result.replace(" )", ")")

        // comma spacing based on settings
        val before = if (picatSettings.spaceBeforeComma) " " else ""
        val after = if (picatSettings.spaceAfterComma) " " else ""
        result = result.replace(Regex("\\s*,\\s*"), "$before,$after")
        // but do not keep space before closing bracket/paren or before newline
        result = result.replace(", ]", ",]")
        result = result.replace(", )", ",)")
        result = result.replace(", \n", ",\n")

        return result
    }

    private fun cleanupDoubleSpaces(code: String): String {
        var result = code
        while (result.contains("  ")) {
            result = result.replace("  ", " ")
        }
        return result
    }

    private fun cleanupDoubleSpacesOutsideLiterals(code: String): String {
        val (masked, lits) = protectLiterals(code)
        val cleaned = cleanupDoubleSpaces(masked)
        return restoreLiterals(cleaned, lits)
    }


    /* ========== Helpers for splitting long rule lines into a body with indentation ========== */

    private fun addLineBreaksAfterRuleOperators(code: String): String {
        var result = code
        // ensure arrow operators are followed by newline for multi-line rule bodies
        result = result.replace(" =>", " =>\n")
        result = result.replace(" ?=>", " ?=>\n")
        result = result.replace(" #=>", " #=>\n")
        result = result.replace(" #<=>", " #<=>\n")
        result = result.replace(" :-", " :-\n")
        // remove accidental double blank lines
        result = result.replace("\n\n", "\n")
        return result
    }

    private fun addIndentation(code: String): String {
        val lines = code.split("\n").map { it.trim() }
        val result = StringBuilder()
        val state = IndentationState()
        for (line in lines) {
            processIndentationLine(line, result, state)
        }
        return result.toString().trim()
    }

    private fun processIndentationLine(line: String, result: StringBuilder, state: IndentationState) {
        when {
            line.isEmpty() -> result.append("\n")
            isRuleBodyStartLine(line) -> handleIndentationRuleBodyStart(line, result, state)
            isCommentOutsideRuleBody(line, state) -> result.append(line).append("\n")
            state.inRuleBody -> handleIndentationRuleBodyLine(line, result, state)
            else -> result.append(line).append("\n")
        }
    }

    private fun isRuleBodyStartLine(line: String) =
        line.contains("=>") || line.contains("?=>") || line.contains("#=>") || line.contains("#<=>") || line.contains(":-")

    private fun isCommentOutsideRuleBody(line: String, state: IndentationState) =
        !state.inRuleBody && line.startsWith("%")

    private fun handleIndentationRuleBodyStart(line: String, result: StringBuilder, state: IndentationState) {
        result.append(line).append("\n")
        state.inRuleBody = true
        state.indentLevel = 1
    }

    @Suppress("CyclomaticComplexMethod")//kinda false-positive
    private fun handleIndentationRuleBodyLine(line: String, result: StringBuilder, state: IndentationState) {
        when {
            isIfThenStatement(line) -> handleIndentationIfThen(line, result, state)
            line.startsWith("elseif") || line.startsWith("else if") -> handleIndentationElseIf(line, result, state)
            line.startsWith("else") -> handleIndentationElse(line, result, state)
            line.contains(" then") -> handleIndentationThen(line, result, state)
            line.startsWith("foreach") || line.startsWith("for ") -> handleIndentationForeach(line, result, state)
            line.startsWith("while") || line.startsWith("loop") -> handleIndentationForeach(line, result, state)
            line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally")
                -> handleIndentationTryCatch(line, result, state)

            isEndStatement(line) -> handleIndentationEnd(line, result, state)
            line.endsWith(".") -> handleIndentationRuleBodyEnd(line, result, state)
            else -> handleIndentationRegularLine(line, result, state)
        }
    }

    private fun handleIndentationIfThen(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.indentLevel
        state.blockStack.add("if")
        state.ifBaseStack.add(base)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationElseIf(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(getIndentation(base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleIndentationElse(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(getIndentation(base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleIndentationThen(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationForeach(line: String, result: StringBuilder, state: IndentationState) {
        state.blockStack.add("foreach")
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationTryCatch(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationEnd(line: String, result: StringBuilder, state: IndentationState) {
        if (state.blockStack.isNotEmpty()) {
            val last = state.blockStack.removeAt(state.blockStack.lastIndex)
            if (last == "if" && state.ifBaseStack.isNotEmpty()) {
                state.ifBaseStack.removeAt(state.ifBaseStack.lastIndex)
            }
        }
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationRuleBodyEnd(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.inRuleBody = false
        state.indentLevel = 0
        state.blockStack.clear()
        state.ifBaseStack.clear()
    }

    private fun handleIndentationRegularLine(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private data class IndentationState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        // Stacks to track nested blocks and else alignment
        val blockStack: MutableList<String> = mutableListOf(),
        val ifBaseStack: MutableList<Int> = mutableListOf()
    )

    private fun getIndentation(level: Int): String {
        val common = settings.getCommonSettings(PicatLanguage)
        val size = common.indentOptions?.INDENT_SIZE ?: 4
        return " ".repeat(size * maxOf(0, level))
    }

    /* ========== Colon spacing inside list comprehensions / brackets (kept from previous logic) ========== */

    private fun addSpacesAroundColonsInListComprehensions(code: String): String {
        val state = ColonSpacingState()
        val chars = code.toCharArray()
        for (i in chars.indices) {
            val char = chars[i]
            updateColonSpacingState(char, state)
            if (char == ':' && !state.inString && state.inListComprehension) {
                return if (picatSettings.spaceAroundColon) {
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

    private data class ColonSpacingState(
        var inString: Boolean = false,
        var inListComprehension: Boolean = false,
        var bracketDepth: Int = 0
    )
}
