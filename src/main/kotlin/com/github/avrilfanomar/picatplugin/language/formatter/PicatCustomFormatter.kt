package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Custom formatter for Picat language.
 * This class provides a custom implementation of the formatting logic.
 */
class PicatCustomFormatter(
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder
) {
    /**
     * Formats the given code according to the code style settings.
     */
    fun format(code: String): String {
        // Normalize the input code by removing leading whitespace
        val normalizedCode = code.trim()

        // Apply formatting rules
        var formattedCode = normalizedCode

        // Handle special operators first
        formattedCode = handleSpecialOperators(formattedCode)

        // Add spaces around operators
        formattedCode = addSpacesAroundOperators(formattedCode)

        // Add line breaks after rule operators
        formattedCode = addLineBreaksAfterRuleOperators(formattedCode)

        // Add indentation
        formattedCode = addIndentation(formattedCode)

        // Normalize line endings and remove trailing whitespace
        formattedCode = normalizeLineEndings(formattedCode)

        return formattedCode
    }

    /**
     * Normalizes line endings and removes trailing whitespace.
     */
    private fun normalizeLineEndings(code: String): String {
        // Split the code into lines
        val lines = code.split("\n")

        // Process each line to remove trailing whitespace
        val processedLines = lines.map { it.trimEnd() }

        // Join the lines back together with LF line endings
        return processedLines.joinToString("\n")
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

        // First, normalize all spaces to ensure idempotency
        // Remove all spaces around operators
        result = result.replace(" = ", "=")
        result = result.replace(" + ", "+")
        result = result.replace(" - ", "-")
        result = result.replace(" * ", "*")
        result = result.replace(" / ", "/")
        result = result.replace(" > ", ">")
        result = result.replace(" < ", "<")
        result = result.replace(" >= ", ">=")
        result = result.replace(" <= ", "<=")
        result = result.replace(" == ", "==")
        result = result.replace(" != ", "!=")
        result = result.replace(" && ", "&&")
        result = result.replace(" || ", "||")
        result = result.replace(" & ", "&")
        result = result.replace(" | ", "|")
        result = result.replace(" ^ ", "^")
        result = result.replace(" << ", "<<")
        result = result.replace(" >> ", ">>")
        result = result.replace(" : ", ":")
        result = result.replace(", ", ",")

        // Now add spaces around operators consistently
        // Add spaces around assignment operators
        result = result.replace("=", " = ")

        // Add spaces around arithmetic operators
        result = result.replace("+", " + ")
        result = result.replace("-", " - ")
        result = result.replace("*", " * ")
        result = result.replace("/", " / ")

        // Add spaces around relational operators
        result = result.replace(">", " > ")
        result = result.replace("<", " < ")
        result = result.replace(">=", " >= ")
        result = result.replace("<=", " <= ")
        result = result.replace("==", " == ")
        result = result.replace("!=", " != ")

        // Add spaces around logical operators
        result = result.replace("&&", " && ")
        result = result.replace("||", " || ")

        // Add spaces around bitwise operators
        result = result.replace("&", " & ")
        result = result.replace("|", " | ")
        result = result.replace("^", " ^ ")
        result = result.replace("<<", " << ")
        result = result.replace(">>", " >> ")

        // Add spaces around colon
        result = result.replace(":", " : ")

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

        // Fix double spaces
        while (result.contains("  ")) {
            result = result.replace("  ", " ")
        }

        // Restore special operators with proper spacing

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

        // Fix double spaces again
        while (result.contains("  ")) {
            result = result.replace("  ", " ")
        }

        return result
    }

    /**
     * Adds line breaks after rule operators.
     * Note: We don't actually add line breaks here because the addIndentation method
     * will add them. This method is kept for clarity and potential future use.
     */
    private fun addLineBreaksAfterRuleOperators(code: String): String {
        // Just return the code as is, since addIndentation will handle the line breaks
        return code
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
        var indentLevel = 0
        var inRuleBody = false
        var lastLineWasEmpty = false

        for (i in processedLines.indices) {
            val line = processedLines[i]
            val trimmedLine = line.trim()

            // Handle empty lines
            if (trimmedLine.isEmpty()) {
                // Only add a newline if the last line wasn't empty
                // This prevents consecutive empty lines
                if (!lastLineWasEmpty || i == 0) {
                    result.append("\n")
                }
                lastLineWasEmpty = true
                continue
            }
            lastLineWasEmpty = false

            // Check if this line starts a rule body
            if (trimmedLine.contains(" =>") || trimmedLine.contains(" ?=>")) {
                inRuleBody = true
                result.append(trimmedLine).append("\n")
                indentLevel = 1
                continue
            }

            // Check if this line ends a rule body
            if (trimmedLine.endsWith(".")) {
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                inRuleBody = false
                indentLevel = 0
                continue
            }

            // Check if this line starts a block with 'then'
            if (trimmedLine.contains(" then")) {
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                indentLevel++
                continue
            }

            // Check if this line is an 'else' statement
            if (trimmedLine.startsWith("else ") || trimmedLine == "else") {
                // Decrease indent for the 'else' line itself
                indentLevel--
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                // Increase indent for the block that follows
                indentLevel++
                continue
            }

            // Check if this line is an 'elseif' statement
            if (trimmedLine.startsWith("elseif ")) {
                // Decrease indent for the 'elseif' line itself
                indentLevel--
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                // Increase indent for the block that follows
                indentLevel++
                continue
            }

            // Check if this line is a simple rule (not a rule body)
            if (!inRuleBody && (trimmedLine.contains(" = ") || trimmedLine.contains("(") && trimmedLine.contains(")"))) {
                result.append(trimmedLine).append("\n")
                continue
            }

            // Check if this line starts a foreach loop
            if (trimmedLine.startsWith("foreach")) {
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                indentLevel++
                continue
            }

            // Check if this line ends a block
            if (trimmedLine == "end" || trimmedLine == "end," || trimmedLine == "end.") {
                // Decrease indent for the 'end' line itself
                indentLevel--
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                continue
            }

            // Add indentation to the line
            if (inRuleBody) {
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
            } else {
                result.append(trimmedLine).append("\n")
            }
        }

        return result.toString().trim()
    }

    /**
     * Returns the indentation string for the given level.
     */
    private fun getIndentation(level: Int): String {
        return "    ".repeat(maxOf(0, level))
    }
}
