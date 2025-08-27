package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings

class PicatCustomFormatter(
    settings: CodeStyleSettings,
) {
    private val picatSettings: PicatCodeStyleSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

    // Delegated helpers extracted to smaller classes
    private val operatorFormatter = PicatOperatorFormatter(picatSettings)
    private val indentEngine = PicatIndentationEngine(settings)
    private val structureFormatter = PicatStructureFormatter(settings)

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
        val result = when {
            isSimpleRule(code) -> {
                var simpleResult = formatPreservingComments(code)
                val contentAfterArrow = simpleResult.substringAfter("=>").trim()
                val shouldAddLineBreak = if (picatSettings.keepLineBreakAfterRuleOperators) {
                    true
                } else {
                    // Keep previous heuristic when setting is disabled
                    contentAfterArrow.length > LINE_BREAK_THRESHOLD || contentAfterArrow.contains("println")
                }

                if (shouldAddLineBreak) {
                    simpleResult = indentEngine.addLineBreaksAfterRuleOperators(simpleResult)
                    simpleResult = indentEngine.addIndentation(simpleResult)
                } else {
                    simpleResult = simpleResult.replace("=>", "=> ")
                    simpleResult = simpleResult.replace("=>  ", "=> ")
                }
                simpleResult
            }

            isPredicate(code) -> {
                formatPreservingComments(code)
            }

            else -> {
                val preserved = formatPreservingComments(code)
                if (preserved == code) {
                    preserved
                } else {
                    structureFormatter.formatCode(preserved)
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
            var handled = false

            if (inBlockComment) {
                // Preserve multi-line comment lines verbatim (except trailing spaces)
                result.append(line.trimEnd())
                // Only close block comment on an actual, unquoted terminator
                if (line.indexOf("*/") >= 0) {
                    inBlockComment = false
                }
                handled = true
            } else {
                // If the line starts a block comment (outside of strings), preserve it as-is
                val blockStartIndex = PicatCommentTools.findUnquotedToken(line, "/*")
                if (blockStartIndex >= 0) {
                    // Enter a block unless this line also closes it
                    val closesOnSameLine = line.indexOf("*/", blockStartIndex + 2) >= 0
                    inBlockComment = !closesOnSameLine
                    result.append(line.trimEnd())
                    handled = true
                }
            }

            if (!handled) {
                val (codePart, commentPart, trailingWsBeforeComment) = PicatCommentTools.splitCodeAndComment(line)

                // Protect string and char literals from operator processing
                val (maskedCode, literals) = PicatLiteralTools.protectLiterals(codePart)

                var formattedCode = operatorFormatter.handleSpecialOperators(maskedCode)
                formattedCode = operatorFormatter.addSpacesAroundOperators(formattedCode)
                formattedCode = operatorFormatter.cleanupDoubleSpaces(formattedCode)
                formattedCode = operatorFormatter.restoreSpecialOperators(formattedCode)

                // Restore literals before any final whitespace cleanup to avoid touching their content
                formattedCode = PicatLiteralTools.restoreLiterals(formattedCode, literals)

                // Final double-space cleanup outside of literals only
                formattedCode = operatorFormatter.cleanupDoubleSpacesOutsideLiterals(formattedCode)

                // Append code part; if there's a trailing whitespace before an inline comment,
                // preserve exactly that spacing between code and comment
                if (commentPart != null) {
                    result.append(formattedCode.trimEnd())
                    result.append(trailingWsBeforeComment)
                    result.append(commentPart)
                } else {
                    result.append(formattedCode)
                }
            }

            if (index < lines.lastIndex) result.append("\n")
        }
        return result.toString()
    }


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


}
