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

        // Check if this is a simple rule (one line with no blocks)
        if (isSimpleRule(normalizedCode)) {
            // For simple rules, just add spaces around operators
            return addSpacesAroundOperators(handleSpecialOperators(normalizedCode))
        }

        // Check if this is a predicate definition
        if (isPredicate(normalizedCode)) {
            // For predicates, just add spaces around operators and don't add indentation
            var result = handleSpecialOperators(normalizedCode)
            result = addSpacesAroundOperators(result)
            return result
        }

        // Apply formatting rules
        var formattedCode = normalizedCode

        // Handle special operators first
        formattedCode = handleSpecialOperators(formattedCode)

        // Add spaces around operators
        formattedCode = addSpacesAroundOperators(formattedCode)

        // Format the code with proper indentation and line breaks
        formattedCode = formatCode(formattedCode)

        return formattedCode
    }

    /**
     * Formats the code with proper indentation and line breaks.
     */
    private fun formatCode(code: String): String {
        val lines = code.split("\n")
        val result = StringBuilder()
        var indentLevel = 0
        var inRuleBody = false

        for (i in lines.indices) {
            val line = lines[i].trim()

            // Skip empty lines but preserve them in the output
            if (line.isEmpty()) {
                result.append("\n")
                continue
            }

            // Check if this line starts a rule body
            if (line.contains(" =>") || line.contains(" ?=>")) {
                result.append(line).append("\n")
                inRuleBody = true
                indentLevel = 1
                continue
            }

            // Handle lines inside rule bodies
            if (inRuleBody) {
                // Check if this line starts a block with 'then'
                if (line.contains(" then")) {
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    indentLevel++
                    continue
                }

                // Check if this line is an 'else' or 'elseif' statement
                if (line.startsWith("else") || line.startsWith("elseif")) {
                    indentLevel--
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    indentLevel++
                    continue
                }

                // Check if this line starts a foreach loop
                if (line.startsWith("foreach")) {
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    indentLevel++
                    continue
                }

                // Check if this line ends a block
                if (line == "end" || line == "end," || line == "end.") {
                    indentLevel--
                    result.append(getIndentation(indentLevel)).append(line).append("\n")

                    // If this is the end of a rule body, reset the indentation level
                    if (line == "end.") {
                        inRuleBody = false
                        indentLevel = 0
                    }
                    continue
                }

                // Check if this line ends a rule body
                if (line.endsWith(".") && !line.contains("=>")) {
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    inRuleBody = false
                    indentLevel = 0
                    continue
                }

                // Add indentation for all other lines inside the rule body
                result.append(getIndentation(indentLevel)).append(line).append("\n")
                continue
            }

            // For lines outside rule bodies, don't add indentation
            result.append(line).append("\n")
        }

        return result.toString().trim()
    }

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
        // Check if the code is a predicate definition
        return (code.contains("(") && code.contains(")") && code.contains("=") && 
                (code.endsWith(".") || code.contains(" =>")))
    }

    /**
     * Normalizes line endings and removes trailing whitespace.
     */
    private fun normalizeLineEndings(code: String): String {
        // Split the code into lines
        val lines = code.split("\n")

        // Process each line to remove trailing whitespace and leading spaces
        val processedLines = lines.map { it.trim() }

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
        result = result.replace("__ARROW_OP__", " =>")
        result = result.replace("__BACKTRACKABLE_ARROW_OP__", " ?=>")

        // Restore constraint rule operators
        result = result.replace("__HASH_ARROW_OP__", " #=>")
        result = result.replace("__HASH_BICONDITIONAL_OP__", " #<=>")

        // Restore term comparison operators
        result = result.replace("__AT_LESS_OP__", " @<")
        result = result.replace("__AT_GREATER_OP__", " @>")
        result = result.replace("__AT_LESS_EQUAL_OP__", " @=<")
        result = result.replace("__AT_GREATER_EQUAL_OP__", " @>=")

        // Restore equality operators
        result = result.replace("__DOUBLE_EQUAL_OP__", " ==")
        result = result.replace("__NOT_EQUAL_OP__", " !=")

        // Restore type constraint operator
        result = result.replace("__DOUBLE_COLON_OP__", " :: ")

        // Restore concatenation operator
        result = result.replace("__CONCAT_OP__", " ++")

        // Fix double spaces again
        while (result.contains("  ")) {
            result = result.replace("  ", " ")
        }

        return result
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
        var indentLevel = 0
        var inRuleBody = false

        for (i in processedLines.indices) {
            val line = processedLines[i]
            val trimmedLine = line.trim()

            // Handle empty lines
            if (trimmedLine.isEmpty()) {
                result.append("\n")
                continue
            }

            // Check if this line starts a rule body
            if (trimmedLine.contains(" =>") || trimmedLine.contains(" ?=>")) {
                // Don't add indentation to the rule header line
                result.append(trimmedLine).append("\n")
                inRuleBody = true
                indentLevel = 1
                continue
            }

            // Check if this line is a comment outside a rule body
            if (!inRuleBody && trimmedLine.startsWith("%")) {
                result.append(trimmedLine).append("\n")
                continue
            }

            // Check if this line is inside a rule body
            if (inRuleBody) {
                // Check if this line starts a block with 'then'
                if (trimmedLine.contains(" then")) {
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    indentLevel += 1
                    continue
                }

                // Check if this line is an 'else' or 'elseif' statement
                if (trimmedLine.startsWith("else") || trimmedLine.startsWith("elseif")) {
                    indentLevel -= 1
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    indentLevel += 1
                    continue
                }

                // Check if this line starts a foreach loop
                if (trimmedLine.startsWith("foreach")) {
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    indentLevel += 1
                    continue
                }

                // Check if this line ends a block
                if (trimmedLine == "end" || trimmedLine == "end," || trimmedLine == "end.") {
                    indentLevel -= 1
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    continue
                }

                // Check if this line ends a rule body
                if (trimmedLine.endsWith(".")) {
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    inRuleBody = false
                    indentLevel = 0
                    continue
                }

                // Add indentation for all other lines inside the rule body
                result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                continue
            }

            // For lines outside rule bodies, don't add indentation
            result.append(trimmedLine).append("\n")
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
