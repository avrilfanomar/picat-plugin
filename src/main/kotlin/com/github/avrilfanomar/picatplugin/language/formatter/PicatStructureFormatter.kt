package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Handles structural formatting (rule bodies, blocks, ends). Assumes operators/spaces already handled.
 */
@Suppress("TooManyFunctions")
internal class PicatStructureFormatter(private val settings: CodeStyleSettings) {

    fun formatCode(code: String): String {
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
            val blockStart = PicatCommentTools.findUnquotedToken(line, "/*")
            if (blockStart >= 0) {
                inBlockComment = line.indexOf("*/", blockStart + 2) < 0
                result.append(line)
                if (idx < lines.lastIndex) result.append("\n")
            } else {
                processLine(line.trim(), result, state)
            }
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

    private fun handleRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        val lineType = classifyLine(line)
        dispatchLineHandler(lineType, line, result, state)
    }

    private fun classifyLine(line: String): LineType =
        classifyControlFlow(line)
            ?: classifyLoopOrException(line)
            ?: classifyEndOrProlog(line)
            ?: LineType.REGULAR

    private fun classifyControlFlow(line: String): LineType? = when {
        isIfThenStatement(line) -> LineType.IF_THEN
        isElseIfStatement(line) -> LineType.ELSE_IF
        line.startsWith("else") -> LineType.ELSE
        line.contains(" then") -> LineType.THEN
        else -> null
    }

    private fun classifyLoopOrException(line: String): LineType? = when {
        isForeachStatement(line) -> LineType.FOREACH
        isLoopStatement(line) -> LineType.LOOP
        isTryCatchStatement(line) -> LineType.TRY_CATCH
        else -> null
    }

    private fun classifyEndOrProlog(line: String): LineType? = when {
        isEndStatement(line) -> LineType.END
        line.endsWith(".") -> LineType.RULE_BODY_END
        isPrologStyleStart(line) -> LineType.PROLOG_START
        isPrologStyleSemicolon(line) -> LineType.PROLOG_SEMICOLON
        isPrologStyleEnd(line) -> LineType.PROLOG_END
        else -> null
    }

    private fun isElseIfStatement(line: String) = line.startsWith("elseif") || line.startsWith("else if")
    private fun isForeachStatement(line: String) = line.startsWith("foreach") || line.startsWith("for ")
    private fun isLoopStatement(line: String) = line.startsWith("while") || line.startsWith("loop")
    private fun isTryCatchStatement(line: String) =
        line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally")

    private fun dispatchLineHandler(type: LineType, line: String, result: StringBuilder, state: FormatState) {
        when (type) {
            LineType.IF_THEN -> handleIfThenStatement(line, result, state)
            LineType.ELSE_IF -> handleElseIfStatement(line, result, state)
            LineType.ELSE -> handleElseStatement(line, result, state)
            LineType.THEN -> handleThenStatement(line, result, state)
            LineType.FOREACH -> handleForeachStatement(line, result, state)
            LineType.LOOP -> handleLoopStatement(line, result, state)
            LineType.TRY_CATCH -> handleTryCatchStatement(line, result, state)
            LineType.END -> handleEndStatement(line, result, state)
            LineType.RULE_BODY_END -> handleRuleBodyEnd(line, result, state)
            LineType.PROLOG_START -> handlePrologStyleStart(line, result, state)
            LineType.PROLOG_SEMICOLON -> handlePrologStyleSemicolon(line, result, state)
            LineType.PROLOG_END -> handlePrologStyleEnd(line, result, state)
            LineType.REGULAR -> handleRegularRuleBodyLine(line, result, state)
        }
    }

    private enum class LineType {
        IF_THEN, ELSE_IF, ELSE, THEN, FOREACH, LOOP, TRY_CATCH, END,
        RULE_BODY_END, PROLOG_START, PROLOG_SEMICOLON, PROLOG_END, REGULAR
    }

    // Prolog-style if-then-else: (cond -> then ; else)

    private fun isPrologStyleStart(line: String): Boolean {
        return line.startsWith("(") && line.endsWith("->")
    }

    private fun isPrologStyleSemicolon(line: String): Boolean {
        return line.startsWith(";")
    }

    private fun isPrologStyleEnd(line: String): Boolean {
        return (line == ")" || line == ")." || line == "),") && prologDepth > 0
    }

    private var prologDepth = 0
    private var prologExpectingConsequence = false

    private fun handlePrologStyleStart(line: String, result: StringBuilder, state: FormatState) {
        // Add extra indentation if we're already inside a Prolog construct
        val extraIndent = if (prologDepth > 0) prologDepth else 0
        val effectiveLevel = state.indentLevel + extraIndent
        prologDepth++
        prologExpectingConsequence = true
        result.append(PicatIndentUtil.getIndentation(settings, effectiveLevel)).append(line).append("\n")
    }

    private fun handlePrologStyleSemicolon(line: String, result: StringBuilder, state: FormatState) {
        prologExpectingConsequence = true
        // Normalize spacing: ensure space after ; (unless it's just ";" alone)
        val normalizedLine = if (line.length > 1 && line[1] != ' ') {
            "; " + line.substring(1)
        } else {
            line
        }
        // Semicolons in nested constructs need extra indentation (depth - 1)
        val extraIndent = if (prologDepth > 1) prologDepth - 1 else 0
        val effectiveLevel = state.indentLevel + extraIndent
        result.append(PicatIndentUtil.getIndentation(settings, effectiveLevel)).append(normalizedLine).append("\n")
    }

    private fun handlePrologStyleEnd(line: String, result: StringBuilder, state: FormatState) {
        prologDepth--
        prologExpectingConsequence = false
        // Closing parens need extra indentation based on remaining depth
        val extraIndent = if (prologDepth > 0) prologDepth else 0
        val effectiveLevel = state.indentLevel + extraIndent
        result.append(PicatIndentUtil.getIndentation(settings, effectiveLevel)).append(line).append("\n")
    }

    private fun isIfThenStatement(line: String) =
        (line.startsWith("if ") || line.startsWith("if(")) && line.contains(" then")

    private fun handleIfThenStatement(line: String, result: StringBuilder, state: FormatState) {
        val base = state.indentLevel
        state.blockStack.add("if")
        state.ifBaseStack.add(base)
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleElseIfStatement(line: String, result: StringBuilder, state: FormatState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(PicatIndentUtil.getIndentation(settings, base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleElseStatement(line: String, result: StringBuilder, state: FormatState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(PicatIndentUtil.getIndentation(settings, base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleThenStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleForeachStatement(line: String, result: StringBuilder, state: FormatState) {
        state.blockStack.add("foreach")
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleLoopStatement(line: String, result: StringBuilder, state: FormatState) {
        state.blockStack.add("loop")
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleTryCatchStatement(line: String, result: StringBuilder, state: FormatState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
    }

    private fun isEndStatement(line: String) =
        line == "end" || line == "end," || line == "end." || line == "endif" || line == "endloop"

    private fun handleEndStatement(line: String, result: StringBuilder, state: FormatState) {
        if (state.blockStack.isNotEmpty()) {
            val last = state.blockStack.removeAt(state.blockStack.lastIndex)
            if (last == "if" && state.ifBaseStack.isNotEmpty()) {
                state.ifBaseStack.removeAt(state.ifBaseStack.lastIndex)
            }
        }
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
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
            else -> result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        }
        state.inRuleBody = false
        state.indentLevel = 0
        // Reset Prolog-style state
        prologDepth = 0
        prologExpectingConsequence = false
    }

    private fun handleRegularRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        // When inside a Prolog-style construct, add extra indentation for consequence/alternative lines
        val extraIndent = if (prologDepth > 0) prologDepth else 0
        val effectiveLevel = state.indentLevel + extraIndent
        result.append(PicatIndentUtil.getIndentation(settings, effectiveLevel)).append(line).append("\n")
        // Reset the expecting flag after processing a consequence line
        if (prologExpectingConsequence) {
            prologExpectingConsequence = false
        }
    }

    private data class FormatState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        val blockStack: MutableList<String> = mutableListOf(),
        val ifBaseStack: MutableList<Int> = mutableListOf(),
    )
}
