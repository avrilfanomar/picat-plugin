package com.github.avrilfanomar.picatplugin.language.formatter

@Suppress("TooManyFunctions")
class PicatCustomFormatter {

    companion object {
        private const val LINE_BREAK_THRESHOLD = 20
    }

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
     * Core safe formatting step: split into code/comment, protect operators, then space.
     */
    private fun formatPreservingComments(input: String): String {
        val lines = input.split("\n")
        return lines.joinToString("\n") { line ->
            val (codePart, commentPart) = splitCodeAndComment(line)
            var formattedCode = handleSpecialOperators(codePart)
            formattedCode = addSpacesAroundOperators(formattedCode)
            formattedCode = cleanupDoubleSpaces(formattedCode)
            formattedCode = restoreSpecialOperators(formattedCode)
            formattedCode = cleanupDoubleSpaces(formattedCode)
            formattedCode = removeTrailingSpaces(formattedCode)
            buildString {
                append(formattedCode.trimEnd())
                if (commentPart != null) {
                    if (formattedCode.isNotEmpty()) append(" ")
                    append(commentPart)
                }
            }
        }
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
        line.endsWith(" =>") || line.endsWith(" ?=>")

    private fun handleRuleBodyStart(line: String, result: StringBuilder, state: FormatState) {
        result.append(line).append("\n")
        state.inRuleBody = true
        state.indentLevel = 1
    }

    private fun handleRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        when {
            isIfThenStatement(line) -> handleIfThenStatement(line, result, state)
            line.startsWith("elseif") -> handleElseIfStatement(line, result, state)
            line.startsWith("else") -> handleElseStatement(line, result, state)
            line.contains(" then") -> handleThenStatement(line, result, state)
            line.startsWith("foreach") -> handleForeachStatement(line, result, state)
            isEndStatement(line) -> handleEndStatement(line, result, state)
            line.endsWith(".") -> handleRuleBodyEnd(line, result, state)
            else -> handleRegularRuleBodyLine(line, result, state)
        }
    }

    private fun isIfThenStatement(line: String) =
        line.startsWith("if ") && line.contains(" then")

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
        state.indentLevel--
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleThenStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleForeachStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun isEndStatement(line: String) =
        line == "end" || line == "end," || line == "end."

    private fun handleEndStatement(line: String, result: StringBuilder, state: FormatState) {
        state.indentLevel--
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")

        if (line == "end.") {
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

    private fun isSimpleRule(code: String) =
        code.contains("=>") && !code.contains("then") && !code.contains("else") &&
                !code.contains("foreach") && !code.contains("end") && !code.contains("\n")

    private fun isPredicate(code: String) =
        !code.contains("\n") &&
                code.contains("(") && code.contains(")") && code.contains("=") &&
                (code.endsWith(".") || code.contains("=>"))

    private fun handleSpecialOperators(code: String): String {
        var result = code

        // Term comparison
        result = result.replace("@=<", "__AT_LESS_EQUAL_OP__")
        result = result.replace("@>=", "__AT_GREATER_EQUAL_OP__")
        result = result.replace("@<", "__AT_LESS_OP__")
        result = result.replace("@>", "__AT_GREATER_OP__")

        // Constraint rule
        result = result.replace("#<=>", "__HASH_BICONDITIONAL_OP__")
        result = result.replace("#=>", "__HASH_ARROW_OP__")
        result = result.replace("#=", "__HASH_EQUAL_OP__")
        result = result.replace("#", "__HASH_OP__")

        // Equality
        result = result.replace("==", "__DOUBLE_EQUAL_OP__")
        result = result.replace("!=", "__NOT_EQUAL_OP__")

        // Type constraint
        result = result.replace("::", "__DOUBLE_COLON_OP__")

        // Concatenation
        result = result.replace("++", "__CONCAT_OP__")

        // Rule
        result = result.replace("?=>", "__BACKTRACKABLE_ARROW_OP__")
        result = result.replace("=>", "__ARROW_OP__")

        return result
    }

    private fun addSpacesAroundOperators(code: String): String {
        var result = code

        fun spaceOperator(op: String) {
            val escaped = Regex.escape(op)
            result = result.replace(Regex("(?<=[^\\s])$escaped(?=[^\\s])"), " $op ")
        }

        listOf(
            "=", "+", "-", "*", "/", ">=", "<=", ">", "<", "==", "!=", "&&", "||",
            "&", "|", "^", "<<", ">>"
        ).forEach { spaceOperator(it) }

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

    private fun restoreSpecialOperators(code: String): String {
        var result = code
        result = result.replace("__ARROW_OP__", " => ")
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", " ?=> ")
        result = result.replace("__HASH_ARROW_OP__", " #=> ")
        result = result.replace("__HASH_BICONDITIONAL_OP__", " #<=> ")
        result = result.replace("__HASH_EQUAL_OP__", " #= ")
        result = result.replace("__HASH_OP__", " # ")
        result = result.replace("__AT_LESS_OP__", " @< ")
        result = result.replace("__AT_GREATER_OP__", " @> ")
        result = result.replace("__AT_LESS_EQUAL_OP__", " @=< ")
        result = result.replace("__AT_GREATER_EQUAL_OP__", " @>= ")
        result = result.replace("__DOUBLE_EQUAL_OP__", " == ")
        result = result.replace("__NOT_EQUAL_OP__", " != ")
        result = result.replace("__DOUBLE_COLON_OP__", " :: ")
        result = result.replace("__CONCAT_OP__", " ++ ")
        return result
    }

    private fun removeTrailingSpaces(code: String) =
        code.split("\n").joinToString("\n") { it.trimEnd() }

    private fun addLineBreaksAfterRuleOperators(code: String): String {
        var result = code
        result = result.replace(" =>", " =>\n")
        result = result.replace(" ?=>", " ?=>\n")
        result = result.replace(" =>\n\n", " =>\n")
        result = result.replace(" ?=>\n\n", " ?=>\n")
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
        line.contains(" =>") || line.contains(" ?=>")

    private fun isCommentOutsideRuleBody(line: String, state: IndentationState) =
        !state.inRuleBody && line.startsWith("%")

    private fun handleIndentationRuleBodyStart(line: String, result: StringBuilder, state: IndentationState) {
        result.append(line).append("\n")
        state.inRuleBody = true
        state.indentLevel = 1
    }

    private fun handleIndentationRuleBodyLine(line: String, result: StringBuilder, state: IndentationState) {
        when {
            isIfThenStatement(line) -> handleIndentationIfThen(line, result, state)
            line.startsWith("elseif") -> handleIndentationElseIf(line, result, state)
            line.startsWith("else") -> handleIndentationElse(line, result, state)
            line.contains(" then") -> handleIndentationThen(line, result, state)
            line.startsWith("foreach") -> handleIndentationForeach(line, result, state)
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

    private fun handleIndentationEnd(line: String, result: StringBuilder, state: IndentationState) {
        state.indentLevel--
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
