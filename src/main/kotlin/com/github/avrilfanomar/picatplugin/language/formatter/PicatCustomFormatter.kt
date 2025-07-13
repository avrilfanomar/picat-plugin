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

        // Debug: print input and normalized code
        println("[DEBUG_LOG] Input code: '${code.replace("\n", "\\n")}'")
        println("[DEBUG_LOG] Normalized code: '${normalizedCode.replace("\n", "\\n")}'")
        println("[DEBUG_LOG] Normalized code lines:")
        normalizedCode.split("\n").forEachIndexed { i, line ->
            println("[DEBUG_LOG]   Line $i: '$line' (length: ${line.length})")
        }

        // Check if this is a simple rule (one line with no blocks)
        val isSimple = isSimpleRule(normalizedCode)
        println("[DEBUG_LOG] isSimpleRule: $isSimple")
        if (isSimple) {
            // For simple rules, add spaces around operators
            println("[DEBUG_LOG] Taking simple rule path")
            var result = handleSpecialOperators(normalizedCode)
            result = addSpacesAroundOperators(result)

            // Only add line breaks and indentation for longer/more complex simple rules
            val contentAfterArrow = result.substringAfter("=>").trim()
            val shouldAddLineBreak = contentAfterArrow.length > 20 || contentAfterArrow.contains("println")
            println("[DEBUG_LOG] Content after arrow: '$contentAfterArrow' (length: ${contentAfterArrow.length})")
            println("[DEBUG_LOG] Should add line break: $shouldAddLineBreak")

            if (shouldAddLineBreak) {
                result = addLineBreaksAfterRuleOperators(result)
                result = addIndentation(result)
            } else {
                // Ensure there's a space after => even without line breaks
                result = result.replace("=>", "=> ")
                // Remove any double spaces that might have been created
                result = result.replace("=>  ", "=> ")
            }
            return result
        }

        // Check if this is a predicate definition
        val isPred = isPredicate(normalizedCode)
        println("[DEBUG_LOG] isPredicate: $isPred")
        if (isPred) {
            // For predicates, just add spaces around operators and don't add indentation
            println("[DEBUG_LOG] Taking predicate path")
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
        var ifBlockBaseLevel = 0

        for (i in lines.indices) {
            val line = lines[i].trim()

            // Debug: print line processing
            println("[DEBUG_LOG] formatCode - Processing line $i: '$line' (inRuleBody: $inRuleBody, indentLevel: $indentLevel)")

            // Skip empty lines but preserve them in the output
            if (line.isEmpty()) {
                result.append("\n")
                continue
            }

            // Check if this line starts a rule body (ends with => or ?=>)
            if (line.endsWith(" =>") || line.endsWith(" ?=>")) {
                result.append(line).append("\n")
                inRuleBody = true
                indentLevel = 1
                continue
            }

            // Handle lines inside rule bodies
            if (inRuleBody) {
                // Check if this line starts with 'if' and contains 'then'
                if (line.startsWith("if ") && line.contains(" then")) {
                    ifBlockBaseLevel = indentLevel
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    indentLevel++
                    continue
                }

                // Check if this line is an 'elseif' statement
                if (line.startsWith("elseif")) {
                    result.append(getIndentation(ifBlockBaseLevel)).append(line).append("\n")
                    indentLevel = ifBlockBaseLevel + 1
                    continue
                }

                // Check if this line is an 'else' statement
                if (line.startsWith("else")) {
                    // Use normal indentation logic for else statements
                    indentLevel--
                    result.append(getIndentation(indentLevel)).append(line).append("\n")
                    indentLevel++
                    continue
                }

                // Check if this line starts a block with 'then' (but not if/elseif)
                if (line.contains(" then")) {
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

                // Check if this line ends a rule body or is a predicate definition
                if (line.endsWith(".")) {
                    // If the line contains "=>" it's a predicate definition with a condition
                    // If it doesn't contain "=>" it's either the end of a rule body or a simple predicate
                    if (line.contains("=>")) {
                        // This is a predicate definition with condition - don't indent
                        result.append(line).append("\n")
                    } else {
                        // Check if this looks like a predicate definition (contains function call pattern)
                        if (line.matches(Regex(".*\\([^)]*\\)\\s*=.*\\."))) {
                            // This is a simple predicate definition - don't indent
                            result.append(line).append("\n")
                        } else {
                            // This is the end of a rule body - use indentation
                            result.append(getIndentation(indentLevel)).append(line).append("\n")
                        }
                    }
                    inRuleBody = false
                    indentLevel = 0
                    continue
                }

                // Add indentation for all other lines inside the rule body
                val indent = getIndentation(indentLevel)
                result.append(indent).append(line).append("\n")
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
        // Check if the code is a predicate definition (single line function definition)
        // A predicate should not contain newlines and should be a simple function definition
        return (!code.contains("\n") && 
                code.contains("(") && code.contains(")") && code.contains("=") && 
                (code.endsWith(".") || code.contains(" =>")) &&
                !code.contains("main") && // main rules are not predicates
                !code.contains("solve")) // solve rules are not predicates
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

        // Add spaces around colons in list comprehensions (but not in strings)
        // This is a simple heuristic: only add spaces around colons that are not inside quotes
        result = addSpacesAroundColonsInListComprehensions(result)

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

        // Remove trailing spaces at the end of lines
        result = result.split("\n").joinToString("\n") { it.trimEnd() }

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
        var ifBlockBaseLevel = 0

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
                // Check if this line starts with 'if' and contains 'then'
                if (trimmedLine.startsWith("if ") && trimmedLine.contains(" then")) {
                    ifBlockBaseLevel = indentLevel
                    result.append(getIndentation(indentLevel)).append(trimmedLine).append("\n")
                    indentLevel += 1
                    continue
                }

                // Check if this line is an 'elseif' statement
                if (trimmedLine.startsWith("elseif")) {
                    result.append(getIndentation(ifBlockBaseLevel)).append(trimmedLine).append("\n")
                    indentLevel = ifBlockBaseLevel + 1
                    continue
                }

                // Check if this line is an 'else' statement
                if (trimmedLine.startsWith("else")) {
                    result.append(getIndentation(ifBlockBaseLevel)).append(trimmedLine).append("\n")
                    indentLevel = ifBlockBaseLevel + 1
                    continue
                }

                // Check if this line starts a block with 'then' (but not if/elseif)
                if (trimmedLine.contains(" then")) {
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
        val result = "    ".repeat(maxOf(0, level))
        // Debug: print indentation details
        println("[DEBUG_LOG] getIndentation($level) = '$result' (length: ${result.length})")
        return result
    }

    /**
     * Adds spaces around colons in list comprehensions but not in string literals.
     */
    private fun addSpacesAroundColonsInListComprehensions(code: String): String {
        var result = code
        var inString = false
        var inListComprehension = false
        var bracketDepth = 0
        val chars = result.toCharArray()

        for (i in chars.indices) {
            val char = chars[i]

            when (char) {
                '"' -> {
                    // Toggle string state (simple heuristic, doesn't handle escaped quotes)
                    inString = !inString
                }
                '[' -> {
                    if (!inString) {
                        bracketDepth++
                        inListComprehension = true
                    }
                }
                ']' -> {
                    if (!inString) {
                        bracketDepth--
                        if (bracketDepth == 0) {
                            inListComprehension = false
                        }
                    }
                }
                ':' -> {
                    if (!inString && inListComprehension) {
                        // Add spaces around colon in list comprehension
                        val before = if (i > 0 && chars[i-1] != ' ') " " else ""
                        val after = if (i < chars.size - 1 && chars[i+1] != ' ') " " else ""

                        if (before.isNotEmpty() || after.isNotEmpty()) {
                            // Rebuild the string with spaces around the colon
                            val prefix = result.substring(0, i)
                            val suffix = result.substring(i + 1)
                            result = prefix + before + ":" + after + suffix
                            // Update chars array for next iteration
                            return addSpacesAroundColonsInListComprehensions(result)
                        }
                    }
                }
            }
        }

        return result
    }
}
