package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Applies line breaks after rule operators and adds indentation for rule bodies.
 */
@Suppress("TooManyFunctions")
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

    private fun handleIndentationRuleBodyLine(line: String, result: StringBuilder, state: IndentationState) {
        val lineType = classifyLine(line)
        dispatchLineHandler(lineType, line, result, state)
    }

    private fun classifyLine(line: String): LineType =
        classifyControlFlow(line)
            ?: classifyLoopOrException(line)
            ?: classifyTerminator(line)
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

    private fun classifyTerminator(line: String): LineType? = when {
        isEndStatement(line) -> LineType.END
        line.endsWith(".") -> LineType.RULE_BODY_END
        else -> null
    }

    private fun isElseIfStatement(line: String) = line.startsWith("elseif") || line.startsWith("else if")
    private fun isForeachStatement(line: String) = line.startsWith("foreach") || line.startsWith("for ")
    private fun isLoopStatement(line: String) = line.startsWith("while") || line.startsWith("loop")
    private fun isTryCatchStatement(line: String) =
        line.startsWith("try") || line.startsWith("catch") || line.startsWith("finally")

    private fun dispatchLineHandler(type: LineType, line: String, result: StringBuilder, state: IndentationState) {
        when (type) {
            LineType.IF_THEN -> handleIndentationIfThen(line, result, state)
            LineType.ELSE_IF -> handleIndentationElseIf(line, result, state)
            LineType.ELSE -> handleIndentationElse(line, result, state)
            LineType.THEN -> handleIndentationThen(line, result, state)
            LineType.FOREACH -> handleIndentationForeach(line, result, state)
            LineType.LOOP -> handleIndentationForeach(line, result, state)
            LineType.TRY_CATCH -> handleIndentationTryCatch(line, result, state)
            LineType.END -> handleIndentationEnd(line, result, state)
            LineType.RULE_BODY_END -> handleIndentationRuleBodyEnd(line, result, state)
            LineType.REGULAR -> handleIndentationRegularLine(line, result, state)
        }
    }

    private enum class LineType {
        IF_THEN, ELSE_IF, ELSE, THEN, FOREACH, LOOP, TRY_CATCH, END, RULE_BODY_END, REGULAR
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
