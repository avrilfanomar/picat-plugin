package com.github.avrilfanomar.picatplugin.language.formatter

/**
 * Custom formatter for Picat language.
 * This class provides a custom implementation of the formatting logic.
 */
@Suppress("TooManyFunctions")
class PicatCustomFormatter {

    companion object {
        private const val LINE_BREAK_THRESHOLD = 20
    }

    /**
     * Formats the given code according to the code style settings.
     */
    fun format(code: String): String {
        // Normalize the input code by removing leading whitespace
        val normalizedCode = code.trim()

        val result = when {
            // Check if this is a simple rule (one line with no blocks)
            isSimpleRule(normalizedCode) -> {
                var simpleResult = handleSpecialOperators(normalizedCode)
                simpleResult = addSpacesAroundOperators(simpleResult)

                // Only add line breaks and indentation for longer/more complex simple rules
                val contentAfterArrow = simpleResult.substringAfter("=>").trim()
                val shouldAddLineBreak = contentAfterArrow.length > LINE_BREAK_THRESHOLD || 
                        contentAfterArrow.contains("println")

                if (shouldAddLineBreak) {
                    simpleResult = addLineBreaksAfterRuleOperators(simpleResult)
                    simpleResult = addIndentation(simpleResult)
                } else {
                    // Ensure there's a space after => even without line breaks
                    simpleResult = simpleResult.replace("=>", "=> ")
                    // Remove any double spaces that might have been created
                    simpleResult = simpleResult.replace("=>  ", "=> ")
                }
                simpleResult
            }
            // Check if this is a predicate definition
            isPredicate(normalizedCode) -> {
                var predResult = handleSpecialOperators(normalizedCode)
                predResult = addSpacesAroundOperators(predResult)
                predResult
            }
            // Apply formatting rules for complex code
            else -> {
                var formattedCode = normalizedCode
                // Handle special operators first
                formattedCode = handleSpecialOperators(formattedCode)
                // Add spaces around operators
                formattedCode = addSpacesAroundOperators(formattedCode)
                // Format the code with proper indentation and line breaks
                formatCode(formattedCode)
            }
        }

        return result
    }

    /**
     * Formats the code with proper indentation and line breaks.
     */
    private fun formatCode(code: String): String {
        val lines = code.split("\n")
        val result = StringBuilder()
        val state = FormatState()

        for (i in lines.indices) {
            val line = lines[i].trim()
            processLine(line, result, state)
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

    private fun isRuleBodyStart(line: String): Boolean {
        return line.endsWith(" =>") || line.endsWith(" ?=>")
    }

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

    private fun isIfThenStatement(line: String): Boolean {
        return line.startsWith("if ") && line.contains(" then")
    }

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

    private fun isEndStatement(line: String): Boolean {
        return line == "end" || line == "end," || line == "end."
    }

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

    /**
     * Checks if the given code is a simple rule (one line with no blocks).
     */
    private fun isSimpleRule(code: String): Boolean {
        // Check if the code is a simple rule (one line with no blocks)
        return code.contains("=>") && !code.contains("then") && !code.contains("else") && 
               !code.contains("foreach") && !code.contains("end") && !code.contains("\n")
    }

    /**
     * Checks if the given code is a predicate definition.
     */
    private fun isPredicate(code: String): Boolean {
        // Check if the code is a predicate definition (single line function definition)
        // A predicate should not contain newlines and should be a simple function definition
        return (!code.contains("\n") &&
                code.contains("(") && code.contains(")") && code.contains("=") &&
                (code.endsWith(".") || code.contains("=>")))
    }


    /**
     * Handles special operators like => and ?=> to ensure they're not broken up.
     */
    private fun handleSpecialOperators(code: String): String {
        var result = code

        // Handle multi-character operators first (to avoid conflicts)

        // Term comparison operators
        result = result.replace("@=<", "__AT_LESS_EQUAL_OP__")
        result = result.replace("@>=", "__AT_GREATER_EQUAL_OP__")
        result = result.replace("@<", "__AT_LESS_OP__")
        result = result.replace("@>", "__AT_GREATER_OP__")

        // Constraint rule operators
        result = result.replace("#<=>", "__HASH_BICONDITIONAL_OP__")
        result = result.replace("#=>", "__HASH_ARROW_OP__")

        // Equality operators
        result = result.replace("==", "__DOUBLE_EQUAL_OP__")
        result = result.replace("!=", "__NOT_EQUAL_OP__")

        // Type constraint operator
        result = result.replace("::", "__DOUBLE_COLON_OP__")

        // Concatenation operator
        result = result.replace("++", "__CONCAT_OP__")

        // Rule operators
        result = result.replace("?=>", "__BACKTRACKABLE_ARROW_OP__")
        result = result.replace("=>", "__ARROW_OP__")

        return result
    }

    /**
     * Adds spaces around operators.
     */
    private fun addSpacesAroundOperators(code: String): String {
        var result = code

        result = normalizeOperatorSpaces(result)
        result = addOperatorSpaces(result)
        result = addSpacesAroundColonsInListComprehensions(result)
        result = fixSpacingInStructures(result)
        result = cleanupDoubleSpaces(result)
        result = restoreSpecialOperators(result)
        result = cleanupDoubleSpaces(result)
        result = removeTrailingSpaces(result)

        return result
    }

    private fun normalizeOperatorSpaces(code: String): String {
        var result = code
        val operators = listOf(
            " = ", " + ", " - ", " * ", " / ", " > ", " < ", " >= ", " <= ", 
            " == ", " != ", " && ", " || ", " & ", " | ", " ^ ", " << ", " >> ", " : ", ", "
        )
        val normalized = listOf(
            "=", "+", "-", "*", "/", ">", "<", ">=", "<=", 
            "==", "!=", "&&", "||", "&", "|", "^", "<<", ">>", ":", ","
        )

        operators.zip(normalized).forEach { (spaced, clean) ->
            result = result.replace(spaced, clean)
        }

        return result
    }

    private fun addOperatorSpaces(code: String): String {
        var result = code

        // Assignment operators
        result = result.replace("=", " = ")

        // Arithmetic operators
        result = result.replace("+", " + ")
        result = result.replace("-", " - ")
        result = result.replace("*", " * ")
        result = result.replace("/", " / ")

        // Relational operators
        result = result.replace(">", " > ")
        result = result.replace("<", " < ")
        result = result.replace(">=", " >= ")
        result = result.replace("<=", " <= ")
        result = result.replace("==", " == ")
        result = result.replace("!=", " != ")

        // Logical operators
        result = result.replace("&&", " && ")
        result = result.replace("||", " || ")

        // Bitwise operators
        result = result.replace("&", " & ")
        result = result.replace("|", " | ")
        result = result.replace("^", " ^ ")
        result = result.replace("<<", " << ")
        result = result.replace(">>", " >> ")

        return result
    }

    private fun fixSpacingInStructures(code: String): String {
        var result = code

        // Fix spaces in list comprehensions
        result = result.replace("[ ", "[")
        result = result.replace(" ]", "]")

        // Fix spaces in function calls
        result = result.replace("( ", "(")
        result = result.replace(" )", ")")

        // Fix spaces around commas
        result = result.replace(" ,", ",")
        result = result.replace(",", ", ")
        result = result.replace(", ]", ",]")
        result = result.replace(", )", ",)")

        // Remove trailing spaces after commas at end of lines
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

        // Restore rule operators
        result = result.replace("__ARROW_OP__", " => ")
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", " ?=> ")

        // Restore constraint rule operators
        result = result.replace("__HASH_ARROW_OP__", " #=> ")
        result = result.replace("__HASH_BICONDITIONAL_OP__", " #<=> ")

        // Restore term comparison operators
        result = result.replace("__AT_LESS_OP__", " @< ")
        result = result.replace("__AT_GREATER_OP__", " @> ")
        result = result.replace("__AT_LESS_EQUAL_OP__", " @=< ")
        result = result.replace("__AT_GREATER_EQUAL_OP__", " @>= ")

        // Restore equality operators
        result = result.replace("__DOUBLE_EQUAL_OP__", " == ")
        result = result.replace("__NOT_EQUAL_OP__", " != ")

        // Restore type constraint operator
        result = result.replace("__DOUBLE_COLON_OP__", " :: ")

        // Restore concatenation operator
        result = result.replace("__CONCAT_OP__", " ++ ")

        return result
    }

    private fun removeTrailingSpaces(code: String): String {
        return code.split("\n").joinToString("\n") { it.trimEnd() }
    }

    /**
     * Adds line breaks after rule operators.
     */
    private fun addLineBreaksAfterRuleOperators(code: String): String {
        var result = code

        // Add line breaks after rule operators if they don't already have one
        result = result.replace(" =>", " =>\n")
        result = result.replace(" ?=>", " ?=>\n")

        // Remove any double newlines that might have been created
        result = result.replace(" =>\n\n", " =>\n")
        result = result.replace(" ?=>\n\n", " ?=>\n")

        return result
    }

    /**
     * Adds indentation to the code.
     */
    private fun addIndentation(code: String): String {
        // First, remove any existing indentation to ensure idempotency
        val lines = code.split("\n")
        val cleanedLines = lines.map { it.trim() }
        val cleanedCode = cleanedLines.joinToString("\n")

        // Now process the cleaned code
        val processedLines = cleanedCode.split("\n")
        val result = StringBuilder()
        val state = IndentationState()

        for (line in processedLines) {
            val trimmedLine = line.trim()
            processIndentationLine(trimmedLine, result, state)
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

    private fun isRuleBodyStartLine(line: String): Boolean {
        return line.contains(" =>") || line.contains(" ?=>")
    }

    private fun isCommentOutsideRuleBody(line: String, state: IndentationState): Boolean {
        return !state.inRuleBody && line.startsWith("%")
    }

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
        state.indentLevel += 1
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
        state.indentLevel += 1
    }

    private fun handleIndentationForeach(line: String, result: StringBuilder, state: IndentationState) {
        result.append(getIndentation(state.indentLevel)).append(line).append("\n")
        state.indentLevel += 1
    }

    private fun handleIndentationEnd(line: String, result: StringBuilder, state: IndentationState) {
        state.indentLevel -= 1
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

    /**
     * Returns the indentation string for the given level.
     */
    private fun getIndentation(level: Int): String {
        return "    ".repeat(maxOf(0, level))
    }

    /**
     * Adds spaces around colons in list comprehensions but not in string literals.
     */
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
                if (state.bracketDepth == 0) {
                    state.inListComprehension = false
                }
            }
        }
    }

    private fun shouldAddSpacesAroundColon(
        char: Char, 
        index: Int, 
        chars: CharArray, 
        state: ColonSpacingState
    ): Boolean {
        return char == ':' && !state.inString && state.inListComprehension && needsSpacing(index, chars)
    }

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
