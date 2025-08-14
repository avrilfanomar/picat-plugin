package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Handles structural formatting (rule bodies, blocks, ends). Assumes operators/spaces already handled.
 */
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

    @Suppress("CyclomaticComplexMethod")
    private fun handleRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        when {
            isIfThenStatement(line) -> handleIfThenStatement(line, result, state)
            line.startsWith("elseif") || line.startsWith("else if") -> handleElseIfStatement(line, result, state)
            line.startsWith("else") -> handleElseStatement(line, result, state)
            line.contains(" then") -> handleThenStatement(line, result, state)
            line.startsWith("foreach") || line.startsWith("for ") -> handleForeachStatement(line, result, state)
            line.startsWith("while") || line.startsWith("loop") -> handleLoopStatement(line, result, state)
            line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally") -> handleTryCatchStatement(
                line,
                result,
                state
            )

            isEndStatement(line) -> handleEndStatement(line, result, state)
            line.endsWith(".") -> handleRuleBodyEnd(line, result, state)
            else -> handleRegularRuleBodyLine(line, result, state)
        }
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
    }

    private fun handleRegularRuleBodyLine(line: String, result: StringBuilder, state: FormatState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
    }

    private data class FormatState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        val blockStack: MutableList<String> = mutableListOf(),
        val ifBaseStack: MutableList<Int> = mutableListOf(),
    )
}
