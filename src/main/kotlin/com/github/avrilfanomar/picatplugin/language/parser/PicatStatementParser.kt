package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat statements and control structures.
 */
class PicatStatementParser : PicatBaseParser() {

    /**
     * Parses a Picat goal (statement).
     */
    fun parseGoal(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            isControlFlowStatement(builder) -> parseControlFlowStatement(builder)
            isLogicalStatement(builder) -> parseLogicalStatement(builder)
            isFlowControlStatement(builder) -> parseFlowControlStatement(builder)
            isAssignment(builder) -> parseAssignment(builder)
            else -> expressionParser.parseExpression(builder)
        }

        marker.done(PicatTokenTypes.GOAL)
    }
    
    /**
     * Checks if the current token is a control flow statement.
     */
    private fun isControlFlowStatement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType
        return tokenType == PicatTokenTypes.IF_KEYWORD ||
               tokenType == PicatTokenTypes.FOREACH_KEYWORD ||
               tokenType == PicatTokenTypes.WHILE_KEYWORD ||
               tokenType == PicatTokenTypes.CASE_KEYWORD ||
               tokenType == PicatTokenTypes.TRY_KEYWORD
    }
    
    /**
     * Parses a control flow statement.
     */
    private fun parseControlFlowStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.IF_KEYWORD -> parseIfThenElse(builder)
            PicatTokenTypes.FOREACH_KEYWORD -> parseForeachLoop(builder)
            PicatTokenTypes.WHILE_KEYWORD -> parseWhileLoop(builder)
            PicatTokenTypes.CASE_KEYWORD -> parseCaseExpression(builder)
            PicatTokenTypes.TRY_KEYWORD -> parseTryCatch(builder)
        }
    }
    
    /**
     * Checks if the current token is a logical statement.
     */
    private fun isLogicalStatement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType
        return tokenType == PicatTokenTypes.NOT_KEYWORD ||
               tokenType == PicatTokenTypes.FAIL_KEYWORD ||
               tokenType == PicatTokenTypes.TRUE_KEYWORD ||
               tokenType == PicatTokenTypes.FALSE_KEYWORD ||
               tokenType == PicatTokenTypes.CUT
    }
    
    /**
     * Parses a logical statement.
     */
    private fun parseLogicalStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.NOT_KEYWORD -> parseNegation(builder)
            PicatTokenTypes.FAIL_KEYWORD -> parseFail(builder)
            PicatTokenTypes.TRUE_KEYWORD -> parseTrue(builder)
            PicatTokenTypes.FALSE_KEYWORD -> parseFalse(builder)
            PicatTokenTypes.CUT -> parseCut(builder)
        }
    }
    
    /**
     * Checks if the current token is a flow control statement.
     */
    private fun isFlowControlStatement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType
        return tokenType == PicatTokenTypes.RETURN_KEYWORD ||
               tokenType == PicatTokenTypes.CONTINUE_KEYWORD ||
               tokenType == PicatTokenTypes.BREAK_KEYWORD ||
               tokenType == PicatTokenTypes.THROW_KEYWORD
    }
    
    /**
     * Parses a flow control statement.
     */
    private fun parseFlowControlStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.RETURN_KEYWORD -> parseReturn(builder)
            PicatTokenTypes.CONTINUE_KEYWORD -> parseContinue(builder)
            PicatTokenTypes.BREAK_KEYWORD -> parseBreak(builder)
            PicatTokenTypes.THROW_KEYWORD -> parseThrow(builder)
        }
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
     * Parses an if-then-else statement.
     */
    private fun parseIfThenElse(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.IF_KEYWORD, "Expected 'if'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        expressionParser.parseExpression(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)

        // Optional elseif clauses
        while (builder.tokenType == PicatTokenTypes.ELSEIF_KEYWORD) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            expressionParser.parseExpression(builder)
            PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseBody(builder)
        }

        // Optional else clause
        if (builder.tokenType == PicatTokenTypes.ELSE_KEYWORD) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseBody(builder)
        }

        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.IF_THEN_ELSE)
    }

    /**
     * Parses a foreach loop.
     */
    private fun parseForeachLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.FOREACH_KEYWORD, "Expected 'foreach'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        parseForeachGenerators(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.FOREACH_LOOP)
    }

    /**
     * Parses foreach generators (var in expr, var in expr, ...).
     */
    private fun parseForeachGenerators(builder: PsiBuilder) {
        val marker = builder.mark()

        parseForeachGenerator(builder)
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseForeachGenerator(builder)
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATORS)
    }

    /**
     * Parses a single foreach generator (var in expr).
     */
    private fun parseForeachGenerator(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse variable or pattern
        if (builder.tokenType == PicatTokenTypes.VARIABLE) {
            builder.advanceLexer()
        } else {
            expressionParser.parsePattern(builder)
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Expect 'in' keyword
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.IN_KEYWORD, "Expected 'in'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse expression
        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.FOREACH_GENERATOR)
    }

    /**
     * Parses a while loop.
     */
    private fun parseWhileLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.WHILE_KEYWORD, "Expected 'while'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        expressionParser.parseExpression(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.WHILE_LOOP)
    }

    /**
     * Parses a case expression.
     */
    private fun parseCaseExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.CASE_KEYWORD, "Expected 'case'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        expressionParser.parseExpression(builder)
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.OF_KEYWORD, "Expected 'of'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseCaseArms(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.CASE_EXPRESSION)
    }

    /**
     * Parses case arms.
     */
    private fun parseCaseArms(builder: PsiBuilder) {
        val marker = builder.mark()

        parseCaseArm(builder)
        while (builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseCaseArm(builder)
        }

        marker.done(PicatTokenTypes.CASE_ARMS)
    }

    /**
     * Parses a single case arm.
     */
    private fun parseCaseArm(builder: PsiBuilder) {
        val marker = builder.mark()

        expressionParser.parsePattern(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)

        marker.done(PicatTokenTypes.CASE_ARM)
    }

    /**
     * Parses a try-catch statement.
     */
    private fun parseTryCatch(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TRY_KEYWORD, "Expected 'try'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)

        // Parse catch clauses
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.CATCH_KEYWORD, "Expected 'catch'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseCatchClauses(builder)

        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.TRY_CATCH)
    }

    /**
     * Parses catch clauses.
     */
    private fun parseCatchClauses(builder: PsiBuilder) {
        val marker = builder.mark()

        parseCatchClause(builder)
        while (builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseCatchClause(builder)
        }

        marker.done(PicatTokenTypes.CATCH_CLAUSES)
    }

    /**
     * Parses a single catch clause.
     */
    private fun parseCatchClause(builder: PsiBuilder) {
        val marker = builder.mark()

        expressionParser.parsePattern(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseBody(builder)

        marker.done(PicatTokenTypes.CATCH_CLAUSE)
    }

    /**
     * Parses a negation (not) expression.
     */
    private fun parseNegation(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.NOT_KEYWORD, "Expected 'not'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.NEGATION)
    }

    /**
     * Parses a fail statement.
     */
    private fun parseFail(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.FAIL_KEYWORD, "Expected 'fail'")
        marker.done(PicatTokenTypes.FAIL_STATEMENT)
    }

    /**
     * Parses a true statement.
     */
    private fun parseTrue(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TRUE_KEYWORD, "Expected 'true'")
        marker.done(PicatTokenTypes.TRUE_STATEMENT)
    }

    /**
     * Parses a false statement.
     */
    private fun parseFalse(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.FALSE_KEYWORD, "Expected 'false'")
        marker.done(PicatTokenTypes.FALSE_STATEMENT)
    }

    /**
     * Parses a cut (!) statement.
     */
    private fun parseCut(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.CUT, "Expected '!'")
        marker.done(PicatTokenTypes.CUT_STATEMENT)
    }

    /**
     * Parses a return statement.
     */
    private fun parseReturn(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.RETURN_KEYWORD, "Expected 'return'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.RETURN_STATEMENT)
    }

    /**
     * Parses a continue statement.
     */
    private fun parseContinue(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.CONTINUE_KEYWORD, "Expected 'continue'")
        marker.done(PicatTokenTypes.CONTINUE_STATEMENT)
    }

    /**
     * Parses a break statement.
     */
    private fun parseBreak(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.BREAK_KEYWORD, "Expected 'break'")
        marker.done(PicatTokenTypes.BREAK_STATEMENT)
    }

    /**
     * Parses a throw statement.
     */
    private fun parseThrow(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THROW_KEYWORD, "Expected 'throw'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.THROW_STATEMENT)
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