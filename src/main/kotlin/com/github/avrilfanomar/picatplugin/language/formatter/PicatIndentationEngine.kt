package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Applies line breaks after rule operators and adds indentation for rule bodies.
 */
internal class PicatIndentationEngine(private val settings: CodeStyleSettings) {

    fun addLineBreaksAfterRuleOperators(code: String): String {
        return PicatWrappingRules().addLineBreaksAfterRuleOperators(code)
    }

    fun addIndentation(code: String): String {
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
        line.contains("=>")
                || line.contains("?=>")
                || line.contains("#=>")
                || line.contains("#<=>")
                || line.contains(":-")

    private fun isCommentOutsideRuleBody(line: String, state: IndentationState) =
        !state.inRuleBody && line.startsWith("%")

    private fun handleIndentationRuleBodyStart(line: String, result: StringBuilder, state: IndentationState) {
        result.append(line).append("\n")
        state.inRuleBody = true
        state.indentLevel = 1
    }

    @Suppress("CyclomaticComplexMethod")
    private fun handleIndentationRuleBodyLine(line: String, result: StringBuilder, state: IndentationState) {
        when {
            isIfThenStatement(line) -> handleIndentationIfThen(line, result, state)
            line.startsWith("elseif") || line.startsWith("else if") -> handleIndentationElseIf(line, result, state)
            line.startsWith("else") -> handleIndentationElse(line, result, state)
            line.contains(" then") -> handleIndentationThen(line, result, state)
            line.startsWith("foreach") || line.startsWith("for ") -> handleIndentationForeach(line, result, state)
            line.startsWith("while") || line.startsWith("loop") -> handleIndentationForeach(line, result, state)
            line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally") ->
                handleIndentationTryCatch(line, result, state)

            isEndStatement(line) -> handleIndentationEnd(line, result, state)
            line.endsWith(".") -> handleIndentationRuleBodyEnd(line, result, state)
            else -> handleIndentationRegularLine(line, result, state)
        }
    }

    private fun isEndStatement(line: String) =
        line == "end" || line == "end," || line == "end." || line == "endif" || line == "endloop"

    private fun isIfThenStatement(line: String) =
        (line.startsWith("if ") || line.startsWith("if(")) && line.contains(" then")

    private fun handleIndentationIfThen(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.indentLevel
        state.blockStack.add("if")
        state.ifBaseStack.add(base)
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationElseIf(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(PicatIndentUtil.getIndentation(settings, base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleIndentationElse(line: String, result: StringBuilder, state: IndentationState) {
        val base = state.ifBaseStack.lastOrNull() ?: 0
        result.append(PicatIndentUtil.getIndentation(settings, base)).append(line).append("\n")
        state.indentLevel = base + 1
    }

    private fun handleIndentationThen(line: String, result: StringBuilder, state: IndentationState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationForeach(line: String, result: StringBuilder, state: IndentationState) {
        state.blockStack.add("foreach")
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.indentLevel++
    }

    private fun handleIndentationTryCatch(line: String, result: StringBuilder, state: IndentationState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationEnd(line: String, result: StringBuilder, state: IndentationState) {
        if (state.blockStack.isNotEmpty()) {
            val last = state.blockStack.removeAt(state.blockStack.lastIndex)
            if (last == "if" && state.ifBaseStack.isNotEmpty()) {
                state.ifBaseStack.removeAt(state.ifBaseStack.lastIndex)
            }
        }
        state.indentLevel = maxOf(0, state.indentLevel - 1)
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
    }

    private fun handleIndentationRuleBodyEnd(line: String, result: StringBuilder, state: IndentationState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
        state.inRuleBody = false
        state.indentLevel = 0
        state.blockStack.clear()
        state.ifBaseStack.clear()
    }

    private fun handleIndentationRegularLine(line: String, result: StringBuilder, state: IndentationState) {
        result.append(PicatIndentUtil.getIndentation(settings, state.indentLevel)).append(line).append("\n")
    }

    private data class IndentationState(
        var indentLevel: Int = 0,
        var inRuleBody: Boolean = false,
        val blockStack: MutableList<String> = mutableListOf(),
        val ifBaseStack: MutableList<Int> = mutableListOf(),
    )
}
