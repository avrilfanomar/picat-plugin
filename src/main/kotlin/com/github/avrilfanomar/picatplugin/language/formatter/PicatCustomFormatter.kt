package com.github.avrilfanomar.picatplugin.language.formatter

@Suppress("TooManyFunctions")
class PicatCustomFormatter {

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
        val normalizedCode = code.trim()

        val result = when {
            isSimpleRule(normalizedCode) -> {
                var simpleResult = formatPreservingComments(normalizedCode)
                val contentAfterArrow = simpleResult.substringAfter("=>").trim()
                val shouldAddLineBreak = contentAfterArrow.length > LINE_BREAK_THRESHOLD ||
                        contentAfterArrow.contains("println")

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
                val formattedCode = formatPreservingComments(normalizedCode)
                formatCode(formattedCode)
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
                if (line.contains("*/")) {
                    inBlockComment = false
                }
                if (index < lines.lastIndex) result.append("\n")
                continue
            }

            // If the line starts a block comment, preserve it and the rest until closing
            if (line.contains("/*")) {
                inBlockComment = !line.contains("*/") // enter block unless this line also closes it
                result.append(line.trimEnd())
                if (index < lines.lastIndex) result.append("\n")
                continue
            }

            val (codePart, commentPart) = splitCodeAndComment(line)
            var formattedCode = handleSpecialOperators(codePart)
            formattedCode = addSpacesAroundOperators(formattedCode)
            formattedCode = cleanupDoubleSpaces(formattedCode)
            formattedCode = restoreSpecialOperators(formattedCode)
            formattedCode = cleanupDoubleSpaces(formattedCode)
            formattedCode = removeTrailingSpaces(formattedCode)

            result.append(formattedCode.trimEnd())
            if (commentPart != null) {
                if (formattedCode.isNotEmpty()) result.append(" ")
                result.append(commentPart)
            }
            if (index < lines.lastIndex) result.append("\n")
        }
        return result.toString()
    }

    private fun splitCodeAndComment(line: String): Pair<String, String?> {
        val commentIndex = line.indexOf('%')
        return if (commentIndex >= 0) {
            val codePart = line.substring(0, commentIndex)
            val commentPart = line.substring(commentIndex)
            codePart to commentPart
        } else {
            line to null
        }
    }

    /* ========== Structural formatting (indentation / block handling) ========== */

    private fun formatCode(code: String): String {
        val lines = code.split("\n")
        val result = StringBuilder()
        val state = FormatState()

        for (line in lines) {
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
        line.endsWith(" =>") || line.endsWith(" ?=>") || line.endsWith(" #=>") || line.endsWith(" #<=>")

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
        state.ifBlockBaseLevel = state.indentLevel
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleElseIfStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.ifBlockBaseLevel)).append(line).append("\n")
        state.indentLevel = state.ifBlockBaseLevel + 1
    }

    private fun handleElseStatement(line: String, result: StringBuilder, state: FormatState) {
        // place else at the same level as the matching if
        result.append(getIndentation(state.ifBlockBaseLevel)).append(line).append("\n")
        state.indentLevel = state.ifBlockBaseLevel + 1
    }

    private fun handleThenStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleForeachStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleLoopStatement(line: String, result: StringBuilder, state: FormatState) {
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
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")

        if (line == "end." || line == "end") {
            state.inRuleBody = false
            state.indentLevel = 0
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
        var ifBlockBaseLevel: Int = 0
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
        result = result.replace("#/\\", "__HASH_OR_ESC__") // placeholder if present in grammar; harmless no-op
        result = result.replace("#\\/", "__HASH_OR_OP__")
        result = result.replace("#\\^", "__HASH_XOR_OP__")
        result = result.replace("#\\'", "__HASH_SLASH_OP__")
        result = result.replace("#~", "__HASH_NOT_OP__")
        result = result.replace("#!=", "__HASH_NOT_EQUAL_OP__")
        result = result.replace("#>=", "__HASH_GE_OP__")
        result = result.replace("#=<", "__HASH_LE_OP__")
        result = result.replace("#=", "__HASH_EQUAL_OP__")

        result = result.replace("@=<", "__AT_LESS_EQUAL_OP__")
        result = result.replace("@>=", "__AT_GREATER_EQUAL_OP__")
        result = result.replace("@<", "__AT_LESS_OP__")
        result = result.replace("@>", "__AT_GREATER_OP__")

        // Arithmetic / comparison / equality operators
        result = result.replace("=\\=", "__NOT_EQUAL_NUM_OP__")
        result = result.replace("=:=", "__EQUAL_NUM_OP__")

        // Standard multi-char ops
        result = result.replace("=\\=", "__NOT_EQUAL_NUM_OP__")

        result = result.replace("<=>", "__SPACELESS_EQUIV_OP__")
        result = result.replace("<=", "__LE_OP__")
        result = result.replace(">=", "__GE_OP__")
        result = result.replace("=..", "__UNIV_OP__") // univ operator in some Prolog-like languages
        result = result.replace("===", "__TRIPLE_EQUAL_OP__")
        result = result.replace("==", "__DOUBLE_EQUAL_OP__")
        result = result.replace("!==", "__STRICT_NOT_EQUAL_OP__")
        result = result.replace("!=", "__NOT_EQUAL_OP__")
        result = result.replace("<<", "__LSHIFT_OP__")
        result = result.replace(">>>", "__URSHIFT_OP__")
        result = result.replace(">>", "__RSHIFT_OP__")
        result = result.replace("++", "__CONCAT_OP__")
        result = result.replace("--", "__DECREMENT_OP__")
        result = result.replace("&&", "__AND_OP__")
        result = result.replace("||", "__OR_OP__")
        result = result.replace("::", "__DOUBLE_COLON_OP__")
        result = result.replace(":=", "__ASSIGN_OP__")
        result = result.replace("?:", "__TERNARY_COLON_OP__") // defensive placeholder

        // @-prefixed comparison ops used in Picat BNF

        // Rule operators (backtrackable and normal)
        result = result.replace("?=>", "__BACKTRACKABLE_ARROW_OP__")
        result = result.replace("=>", "__ARROW_OP__")

        // '#' alone as operator (ensure it's protected after the multi-char # ops above)
        result = result.replace("#", "__HASH_OP__")

        return result
    }

    private fun restoreSpecialOperators(code: String): String {
        var result = code

        // restore in reverse/normalized form (with a single spaying around them)
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", " ?=> ")
        result = result.replace("__ARROW_OP__", " => ")

        // @ ops
        result = result.replace("__AT_LESS_EQUAL_OP__", " @=< ")
        result = result.replace("__AT_GREATER_EQUAL_OP__", " @>= ")
        result = result.replace("__AT_LESS_OP__", " @< ")
        result = result.replace("__AT_GREATER_OP__", " @> ")

        // Hash ops
        result = result.replace("__HASH_BICONDITIONAL_OP__", " #<=> ")
        result = result.replace("__HASH_ARROW_OP__", " #=> ")
        result = result.replace("__HASH_EQUAL_OP__", " #= ")
        result = result.replace("__HASH_NOT_EQUAL_OP__", " #!= ")
        result = result.replace("__HASH_GE_OP__", " #>= ")
        result = result.replace("__HASH_LE_OP__", " #=< ")
        result = result.replace("__HASH_OP__", " # ")
        result = result.replace("__HASH_OR_OP__", " #\\/ ")
        result = result.replace("__HASH_XOR_OP__", " #^ ")
        result = result.replace("__HASH_NOT_OP__", " #~ ")

        // common comparisons
        result = result.replace("__DOUBLE_EQUAL_OP__", " == ")
        result = result.replace("__NOT_EQUAL_OP__", " != ")
        result = result.replace("__STRICT_NOT_EQUAL_OP__", " !== ")
        result = result.replace("__TRIPLE_EQUAL_OP__", " === ")

        result = result.replace("__LE_OP__", " <= ")
        result = result.replace("__GE_OP__", " >= ")
        result = result.replace("__LSHIFT_OP__", " << ")
        result = result.replace("__RSHIFT_OP__", " >> ")
        result = result.replace("__URSHIFT_OP__", " >>> ")
        result = result.replace("__CONCAT_OP__", " ++ ")
        result = result.replace("__DOUBLE_COLON_OP__", " :: ")
        result = result.replace("__AND_OP__", " && ")
        result = result.replace("__OR_OP__", " || ")
        result = result.replace("__UNIV_OP__", " =.. ")
        result = result.replace("__NOT_EQUAL_NUM_OP__", " =\\= ")
        result = result.replace("__EQUAL_NUM_OP__", " =:= ")
        result = result.replace("__ASSIGN_OP__", " := ")

        // defensive placeholders
        result = result.replace("__SPACELESS_EQUIV_OP__", " <=> ")
        result = result.replace("__DECREMENT_OP__", " -- ")
        result = result.replace("__TERNARY_COLON_OP__", " ?: ")

        // generic cleanup of multiple spaces introduced above
        return result
    }

    /* ========== Spacing algorithm for operators (applied to the protected code) ========== */
    private fun addSpacesAroundOperators(code: String): String {
        var result = code

        // A prioritized list: multi-char operators first (longer first), then single-char
        val operators = arrayOf(
            ">>>", "?=>", "<=>", "#<=>", "#!=", "#>=", "#=<", "#<=", "@>=", "@<=", "@=<", "=:=", "=\\=", "!==",
            "#\\/", "#\\^", "#'", "=>", "#~", "#=", "==", "!=", "=<", ">=", "<=", "<", ">", "\\+", "-", "\\*", "/", "%",
            "=", "!", "\\?", "\\^", "\\|", "&", "~"
        )

        // create a regex-safe list and apply spacing in a way that avoids splitting already protected tokens
        for (op in operators) {
            val escaped = Regex.escape(op.trim())
            // place spaces around operator occurrences that don't already have spaces
            // use lookbehind/lookahead to avoid touching string boundaries that already have spaces
            result = result.replace(Regex("(?<!\\s)$escaped(?!\\s)"), " $op ")
            result = result.replace(Regex("(?<!\\s)$escaped(?=\\s)"), " $op ")
            result = result.replace(Regex("(?<=\\s)$escaped(?!\\s)"), " $op ")
        }

        // Single-char common operators (space if not already spaced)
        val singleCharOps = listOf("=", "+", "-", "*", "/", ">", "<", "&", "|", "^", "~", "%", ",")
        for (op in singleCharOps) {
            val escaped = Regex.escape(op)
            // avoid touching parentheses and commas spacing normalized later by fixSpacingInStructures
            result = result.replace(Regex("(?<!\\s)$escaped(?!\\s)"), " $op ")
        }

        result = addSpacesAroundColonsInListComprehensions(result)
        result = fixSpacingInStructures(result)
        return result
    }

    private fun fixSpacingInStructures(code: String): String {
        var result = code
        // normalize common structure spacing
        result = result.replace("[ ", "[")
        result = result.replace(" ]", "]")
        result = result.replace("( ", "(")
        result = result.replace(" )", ")")
        result = result.replace(" ,", ",")
        result = result.replace(",", ", ")
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

    private fun removeTrailingSpaces(code: String) =
        code.split("\n").joinToString("\n") { it.trimEnd() }

    /* ========== Helpers for splitting long rule lines into a body with indentation ========== */

    private fun addLineBreaksAfterRuleOperators(code: String): String {
        var result = code
        // ensure arrow operators are followed by newline for multi-line rule bodies
        result = result.replace(" =>", " =>\n")
        result = result.replace(" ?=>", " ?=>\n")
        result = result.replace(" #=>", " #=>\n")
        result = result.replace(" #<=>", " #<=>\n")
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
        line.contains(" =>") || line.contains(" ?=>") || line.contains(" #=>") || line.contains(" #<=>")

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
        state.ifBlockBaseLevel = state.indentLevel
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationElseIf(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.ifBlockBaseLevel)).append(line).append("\n")
        state.indentLevel = state.ifBlockBaseLevel + 1
    }

    private fun handleIndentationElse(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.ifBlockBaseLevel)).append(line).append("\n")
        state.indentLevel = state.ifBlockBaseLevel + 1
    }

    private fun handleIndentationThen(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationForeach(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationTryCatch(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationEnd(line: String, result: StringBuilder, state: IndentationState) {
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationRuleBodyEnd(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.inRuleBody = false
        state.indentLevel = 0
    }

    private fun handleIndentationRegularLine(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
    }

    private data class IndentationState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        var ifBlockBaseLevel: Int = 0
    )

    private fun getIndentation(level: Int) = "    ".repeat(maxOf(0, level))

    /* ========== Colon spacing inside list comprehensions / brackets (kept from previous logic) ========== */

    private fun addSpacesAroundColonsInListComprehensions(code: String): String {
        val state = ColonSpacingState()
        val chars = code.toCharArray()
        for (i in chars.indices) {
            val char = chars[i]
            updateColonSpacingState(char, state)
            if (shouldAddSpacesAroundColon(char, i, chars, state)) {
                return processColonSpacing(code, i, chars)
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

    private fun shouldAddSpacesAroundColon(char: Char, index: Int, chars: CharArray, state: ColonSpacingState) =
        char == ':' && !state.inString && state.inListComprehension && needsSpacing(index, chars)

    private fun needsSpacing(index: Int, chars: CharArray): Boolean {
        val needsBefore = index > 0 && chars[index - 1] != ' '
        val needsAfter = index < chars.size - 1 && chars[index + 1] != ' '
        return needsBefore || needsAfter
    }

    private fun processColonSpacing(code: String, index: Int, chars: CharArray): String {
        val before = if (index > 0 && chars[index - 1] != ' ') " " else ""
        val after = if (index < chars.size - 1 && chars[index + 1] != ' ') " " else ""
        val prefix = code.substring(0, index)
        val suffix = code.substring(index + 1)
        val result = "$prefix$before:$after$suffix"
        return addSpacesAroundColonsInListComprehensions(result)
    }

    private data class ColonSpacingState(
        var inString: Boolean = false,
        var inListComprehension: Boolean = false,
        var bracketDepth: Int = 0
    )
}
