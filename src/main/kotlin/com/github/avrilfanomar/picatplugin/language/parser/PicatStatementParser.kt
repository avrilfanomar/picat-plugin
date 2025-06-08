package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Parser component responsible for parsing Picat statements and control structures.
 */
class PicatStatementParser : PicatBaseParser() {
    // Helper for parsing specific statement types
    private lateinit var helper: PicatStatementParserHelper

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        super.initialize(parserContext)
        helper = PicatStatementParserHelper()
        helper.initialize(parserContext)
    }

    /**
     * Parses a Picat goal (statement).
     */
    fun parseGoal(builder: PsiBuilder) {
        val marker = builder.mark()

        if (!checkAndParseSpecialStatement(builder)) {
            if (isAssignment(builder)) {
                parseAssignment(builder)
            } else {
                expressionParser.parseExpression(builder)
            }
        }

        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Checks if the current token is a special statement and parses it.
     * Returns true if a special statement was parsed, false otherwise.
     */
    private fun checkAndParseSpecialStatement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType

        // Check each category of statements
        return checkAndParseControlFlowStatement(builder, tokenType) ||
                checkAndParseLogicalStatement(builder, tokenType) ||
                checkAndParseFlowControlStatement(builder, tokenType)
    }

    /**
     * Checks and parses control flow statements (if, foreach, while, for, case, try).
     * Returns true if a control flow statement was parsed, false otherwise.
     */
    private fun checkAndParseControlFlowStatement(builder: PsiBuilder, tokenType: IElementType?): Boolean {
        var parsed = true

        when (tokenType) {
            PicatTokenTypes.IF_KEYWORD -> helper.parseIfThenElse(builder)
            PicatTokenTypes.FOREACH_KEYWORD -> helper.parseForeachLoop(builder)
            PicatTokenTypes.WHILE_KEYWORD -> helper.parseWhileLoop(builder)
            PicatTokenTypes.CASE_KEYWORD -> helper.parseCaseExpression(builder)
            PicatTokenTypes.TRY_KEYWORD -> helper.parseTryCatch(builder)
            PicatTokenTypes.LOOP_KEYWORD -> parseLoopWhileStatement(builder, 0) // Assuming level 0 for direct calls from dispatcher
            else -> parsed = false
        }

        return parsed
    }

    fun parseLoopWhileStatement(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "LoopWhileStatement")) return false
        if (builder.tokenType != PicatTokenTypes.LOOP_KEYWORD) return false

        val marker = builder.mark()
        var result = PicatParserUtil.expectKeyword(builder, PicatTokenTypes.LOOP_KEYWORD, "Expected 'loop'")

        // Pinning the LOOP_KEYWORD (pin=1 in BNF) means if LOOP_KEYWORD is present,
        // this rule must succeed or partial tree is kept.
        // For manual parsing, this means we don't easily rollback past LOOP_KEYWORD if body parsing fails.
        // However, marker.done() or error marking will handle this.

        result = result && parseBody(builder, level + 1) // Re-use existing parseBody
        result = result && PicatParserUtil.expectKeyword(builder, PicatTokenTypes.WHILE_KEYWORD, "Expected 'while' after loop body")
        result = result && expressionParser.parseExpression(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after loop-while statement")

        if (result) {
            marker.done(PicatTokenTypes.LOOP_WHILE_STATEMENT)
        } else {
            marker.rollbackTo() // Or marker.error("Syntax error in loop-while") and marker.drop() depending on how much was consumed
        }
        return result
    }

    /**
     * Checks and parses logical statements (not, fail, true, false, cut).
     * Returns true if a logical statement was parsed, false otherwise.
     */
    private fun checkAndParseLogicalStatement(builder: PsiBuilder, tokenType: IElementType?): Boolean {
        var parsed = true

        when (tokenType) {
            PicatTokenTypes.NOT_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.NOT_KEYWORD,
                    "Expected 'not'",
                    PicatTokenTypes.NEGATION,
                    true // Parse expression
                )
            }

            PicatTokenTypes.FAIL_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.FAIL_KEYWORD,
                    "Expected 'fail'",
                    PicatTokenTypes.FAIL_STATEMENT
                )
            }

            PicatTokenTypes.TRUE_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.TRUE_KEYWORD,
                    "Expected 'true'",
                    PicatTokenTypes.TRUE_STATEMENT
                )
            }

            PicatTokenTypes.FALSE_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.FALSE_KEYWORD,
                    "Expected 'false'",
                    PicatTokenTypes.FALSE_STATEMENT
                )
            }

            PicatTokenTypes.CUT -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.CUT,
                    "Expected '!'",
                    PicatTokenTypes.CUT_STATEMENT
                )
            }

            else -> parsed = false
        }

        return parsed
    }

    /**
     * Checks and parses flow control statements (return, continue, break, throw).
     * Returns true if a flow control statement was parsed, false otherwise.
     */
    private fun checkAndParseFlowControlStatement(builder: PsiBuilder, tokenType: IElementType?): Boolean {
        var parsed = true

        when (tokenType) {
            PicatTokenTypes.RETURN_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.RETURN_KEYWORD,
                    "Expected 'return'",
                    PicatTokenTypes.RETURN_STATEMENT,
                    true // Parse expression
                )
            }

            PicatTokenTypes.CONTINUE_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.CONTINUE_KEYWORD,
                    "Expected 'continue'",
                    PicatTokenTypes.CONTINUE_STATEMENT
                )
            }

            PicatTokenTypes.BREAK_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.BREAK_KEYWORD,
                    "Expected 'break'",
                    PicatTokenTypes.BREAK_STATEMENT
                )
            }

            PicatTokenTypes.THROW_KEYWORD -> {
                helper.parseStatement(
                    builder,
                    PicatTokenTypes.THROW_KEYWORD,
                    "Expected 'throw'",
                    PicatTokenTypes.THROW_STATEMENT,
                    true // Parse expression
                )
            }

            else -> parsed = false
        }

        return parsed
    }

    /**
     * Parses a body consisting of one or more goals separated by commas or semicolons.
     */
    fun parseBody(builder: PsiBuilder, level: Int = 0) { // Added level parameter for consistency if called from other parsers
        val marker = builder.mark()

        skipWhitespace(builder)
        // Parse the first goal
        parseGoal(builder) // parseGoal itself doesn't take level in this existing code
        skipWhitespace(builder)

        // Parse additional goals separated by commas or semicolons
        while (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer() // Consume comma or semicolon
            skipWhitespace(builder)
            parseGoal(builder)
            skipWhitespace(builder)
        }
        // Optional cut
        if (builder.tokenType == PicatTokenTypes.CUT) {
            val cutMarker = builder.mark()
            builder.advanceLexer()
            cutMarker.done(PicatTokenTypes.CUT_GOAL) // Assuming CUT_GOAL is the elementType for a cut
        }

        marker.done(PicatTokenTypes.BODY)
    }

    /**
     * Parses an assignment statement.
     */
    private fun parseAssignment(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the left-hand side (variable or pattern)
        expressionParser.parsePattern(builder)
        skipWhitespace(builder)
        // Parse assignment operator (= or :=)
        if (builder.tokenType == PicatTokenTypes.ASSIGN_OP || builder.tokenType == PicatTokenTypes.EQUAL) {
            builder.advanceLexer()
            skipWhitespace(builder)
        } else {
            builder.error("Expected '=' or ':='")
        }

        // Parse right-hand side (expression)
        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.ASSIGNMENT)
    }
}
