package com.github.avrilfanomar.picatplugin.language.parser

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
     * Checks and parses control flow statements (if, foreach, while, case, try).
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
            else -> parsed = false
        }

        return parsed
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
    fun parseBody(builder: PsiBuilder) {
        val marker = builder.mark()

        parseGoal(builder)
        while (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseGoal(builder)
        }

        marker.done(PicatTokenTypes.BODY)
    }

    /**
     * Parses an assignment statement.
     */
    private fun parseAssignment(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse left-hand side (variable or pattern)
        expressionParser.parsePattern(builder)

        // Parse assignment operator
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected '='")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse right-hand side (expression)
        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.ASSIGNMENT)
    }
}
