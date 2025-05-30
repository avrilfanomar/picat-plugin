package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Parser component responsible for parsing Picat statements and control structures.
 */
class PicatStatementParser : PicatBaseParser() {

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

        // Control flow statements
        when (tokenType) {
            PicatTokenTypes.IF_KEYWORD -> {
                parseIfThenElse(builder)
                return true
            }
            PicatTokenTypes.FOREACH_KEYWORD -> {
                parseForeachLoop(builder)
                return true
            }
            PicatTokenTypes.WHILE_KEYWORD -> {
                parseWhileLoop(builder)
                return true
            }
            PicatTokenTypes.CASE_KEYWORD -> {
                parseCaseExpression(builder)
                return true
            }
            PicatTokenTypes.TRY_KEYWORD -> {
                parseTryCatch(builder)
                return true
            }

            // Logical statements
            PicatTokenTypes.NOT_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.NOT_KEYWORD, 
                    "Expected 'not'", 
                    PicatTokenTypes.NEGATION,
                    true // Parse expression
                )
                return true
            }
            PicatTokenTypes.FAIL_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.FAIL_KEYWORD, 
                    "Expected 'fail'", 
                    PicatTokenTypes.FAIL_STATEMENT
                )
                return true
            }
            PicatTokenTypes.TRUE_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.TRUE_KEYWORD, 
                    "Expected 'true'", 
                    PicatTokenTypes.TRUE_STATEMENT
                )
                return true
            }
            PicatTokenTypes.FALSE_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.FALSE_KEYWORD, 
                    "Expected 'false'", 
                    PicatTokenTypes.FALSE_STATEMENT
                )
                return true
            }
            PicatTokenTypes.CUT -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.CUT, 
                    "Expected '!'", 
                    PicatTokenTypes.CUT_STATEMENT
                )
                return true
            }

            // Flow control statements
            PicatTokenTypes.RETURN_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.RETURN_KEYWORD, 
                    "Expected 'return'", 
                    PicatTokenTypes.RETURN_STATEMENT,
                    true // Parse expression
                )
                return true
            }
            PicatTokenTypes.CONTINUE_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.CONTINUE_KEYWORD, 
                    "Expected 'continue'", 
                    PicatTokenTypes.CONTINUE_STATEMENT
                )
                return true
            }
            PicatTokenTypes.BREAK_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.BREAK_KEYWORD, 
                    "Expected 'break'", 
                    PicatTokenTypes.BREAK_STATEMENT
                )
                return true
            }
            PicatTokenTypes.THROW_KEYWORD -> {
                parseStatement(
                    builder, 
                    PicatTokenTypes.THROW_KEYWORD, 
                    "Expected 'throw'", 
                    PicatTokenTypes.THROW_STATEMENT,
                    true // Parse expression
                )
                return true
            }
            else -> return false
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
     * Parses a statement that consists of a keyword, optionally followed by an expression.
     * 
     * @param builder The PSI builder
     * @param keyword The keyword token type to expect
     * @param errorMessage The error message if the keyword is not found
     * @param resultType The token type to mark the statement as
     * @param parseExpression Whether to parse an expression after the keyword
     */
    private fun parseStatement(
        builder: PsiBuilder, 
        keyword: IElementType, 
        errorMessage: String,
        resultType: IElementType,
        parseExpression: Boolean = false
    ) {
        val marker = builder.mark()

        // Handle special case for cut operator
        if (keyword == PicatTokenTypes.CUT) {
            PicatParserUtil.expectToken(builder, keyword, errorMessage)
        } else {
            PicatParserUtil.expectKeyword(builder, keyword, errorMessage)
        }

        // Parse expression if needed
        if (parseExpression) {
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            expressionParser.parseExpression(builder)
        }

        marker.done(resultType)
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
