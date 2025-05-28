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

        when (builder.tokenType) {
            PicatTokenTypes.IF_KEYWORD -> parseIfThenElse(builder)
            PicatTokenTypes.FOREACH_KEYWORD -> parseForeachLoop(builder)
            PicatTokenTypes.WHILE_KEYWORD -> parseWhileLoop(builder)
            PicatTokenTypes.CASE_KEYWORD -> parseCaseExpression(builder)
            PicatTokenTypes.TRY_KEYWORD -> parseTryCatch(builder)
            PicatTokenTypes.NOT_KEYWORD -> parseNegation(builder)
            PicatTokenTypes.FAIL_KEYWORD -> parseFail(builder)
            PicatTokenTypes.TRUE_KEYWORD -> parseTrue(builder)
            PicatTokenTypes.FALSE_KEYWORD -> parseFalse(builder)
            PicatTokenTypes.CUT -> parseCut(builder)
            PicatTokenTypes.RETURN_KEYWORD -> parseReturn(builder)
            PicatTokenTypes.CONTINUE_KEYWORD -> parseContinue(builder)
            PicatTokenTypes.BREAK_KEYWORD -> parseBreak(builder)
            PicatTokenTypes.THROW_KEYWORD -> parseThrow(builder)
            else -> {
                if (isAssignment(builder)) {
                    parseAssignment(builder)
                } else {
                    expressionParser.parseExpression(builder)
                }
            }
        }

        marker.done(PicatTokenTypes.GOAL)
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

        expressionParser.parseExpression(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")

        parseBody(builder)

        // Optional elseif clauses
        while (builder.tokenType == PicatTokenTypes.ELSEIF_KEYWORD) {
            builder.advanceLexer()
            expressionParser.parseExpression(builder)
            PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
            parseBody(builder)
        }

        // Optional else clause
        if (builder.tokenType == PicatTokenTypes.ELSE_KEYWORD) {
            builder.advanceLexer()
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

        PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        parseForeachGenerators(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")

        parseBody(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.FOREACH_LOOP)
    }

    /**
     * Parses foreach generators.
     */
    private fun parseForeachGenerators(builder: PsiBuilder) {
        val marker = builder.mark()
        parseForeachGenerator(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseForeachGenerator(builder)
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATORS)
    }

    /**
     * Parses a single foreach generator.
     */
    private fun parseForeachGenerator(builder: PsiBuilder) {
        val marker = builder.mark()
        parseVariable(builder)

        if (builder.tokenType == PicatTokenTypes.IN_KEYWORD) {
            builder.advanceLexer()
            expressionParser.parseExpression(builder)
        } else if (builder.tokenType == PicatTokenTypes.EQUAL) {
            builder.advanceLexer()
            expressionParser.parseExpression(builder)
        } else {
            builder.error("Expected 'in' or '='")
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATOR)
    }

    /**
     * Parses a while loop.
     */
    private fun parseWhileLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.WHILE_KEYWORD, "Expected 'while'")

        expressionParser.parseExpression(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.DO_KEYWORD, "Expected 'do'")

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

        expressionParser.parseExpression(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.OF_KEYWORD, "Expected 'of'")

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
            parseCaseArm(builder)
        }

        marker.done(PicatTokenTypes.CASE_ARMS)
    }

    /**
     * Parses a single case arm.
     */
    private fun parseCaseArm(builder: PsiBuilder) {
        val marker = builder.mark()
        patternParser.parsePattern(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        parseBody(builder)
        marker.done(PicatTokenTypes.CASE_ARM)
    }

    /**
     * Parses a try-catch statement.
     */
    private fun parseTryCatch(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TRY_KEYWORD, "Expected 'try'")

        parseBody(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.CATCH_KEYWORD, "Expected 'catch'")

        parseCatchClauses(builder)

        // Optional finally clause
        if (builder.tokenType == PicatTokenTypes.FINALLY_KEYWORD) {
            builder.advanceLexer()
            parseBody(builder)
        }

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
            parseCatchClause(builder)
        }

        marker.done(PicatTokenTypes.CATCH_CLAUSES)
    }

    /**
     * Parses a single catch clause.
     */
    private fun parseCatchClause(builder: PsiBuilder) {
        val marker = builder.mark()
        patternParser.parsePattern(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        parseBody(builder)
        marker.done(PicatTokenTypes.CATCH_CLAUSE)
    }

    /**
     * Parses a negation statement.
     */
    private fun parseNegation(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.NOT_KEYWORD, "Expected 'not'")
        parseGoal(builder)
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a fail statement.
     */
    private fun parseFail(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.FAIL_KEYWORD, "Expected 'fail'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a true statement.
     */
    private fun parseTrue(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TRUE_KEYWORD, "Expected 'true'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a false statement.
     */
    private fun parseFalse(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.FALSE_KEYWORD, "Expected 'false'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a cut statement.
     */
    private fun parseCut(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.CUT, "Expected '!'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a return statement.
     */
    private fun parseReturn(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.RETURN_KEYWORD, "Expected 'return'")
        expressionParser.parseExpression(builder)
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a continue statement.
     */
    private fun parseContinue(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.CONTINUE_KEYWORD, "Expected 'continue'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a break statement.
     */
    private fun parseBreak(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.BREAK_KEYWORD, "Expected 'break'")
        marker.done(PicatTokenTypes.GOAL)
    }

    /**
     * Parses a throw statement.
     */
    private fun parseThrow(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.THROW_KEYWORD, "Expected 'throw'")
        expressionParser.parseExpression(builder)
        marker.done(PicatTokenTypes.THROW_STATEMENT)
    }

    /**
     * Parses an assignment statement.
     */
    private fun parseAssignment(builder: PsiBuilder) {
        val marker = builder.mark()
        parseVariable(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':='")
        expressionParser.parseExpression(builder)
        marker.done(PicatTokenTypes.GOAL)
    }
}
