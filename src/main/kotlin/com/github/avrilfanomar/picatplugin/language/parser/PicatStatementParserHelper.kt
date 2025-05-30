package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Helper class for PicatStatementParser that handles parsing of specific statement types.
 */
class PicatStatementParserHelper : PicatBaseParser() {

    /**
     * Parses an if-then-else statement.
     */
    fun parseIfThenElse(builder: PsiBuilder) {
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

        statementParser.parseBody(builder)

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
            statementParser.parseBody(builder)
        }

        // Optional else clause
        if (builder.tokenType == PicatTokenTypes.ELSE_KEYWORD) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            statementParser.parseBody(builder)
        }

        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.IF_THEN_ELSE)
    }

    /**
     * Parses a foreach loop.
     */
    fun parseForeachLoop(builder: PsiBuilder) {
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

        statementParser.parseBody(builder)
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
    fun parseWhileLoop(builder: PsiBuilder) {
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

        statementParser.parseBody(builder)
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.WHILE_LOOP)
    }

    /**
     * Parses a case expression.
     */
    fun parseCaseExpression(builder: PsiBuilder) {
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

        statementParser.parseBody(builder)

        marker.done(PicatTokenTypes.CASE_ARM)
    }

    /**
     * Parses a try-catch statement.
     */
    fun parseTryCatch(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TRY_KEYWORD, "Expected 'try'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        statementParser.parseBody(builder)

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

        statementParser.parseBody(builder)

        marker.done(PicatTokenTypes.CATCH_CLAUSE)
    }

    /**
     * Parses a statement that consists of a keyword, optionally followed by an expression.
     */
    fun parseStatement(
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
}
