package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectKeyword
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
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
        expectKeyword(builder, PicatTokenTypes.IF_KEYWORD, "Expected 'if'")
        skipWhitespace(builder)

        expressionParser.parseExpression(builder)
        expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
        skipWhitespace(builder)

        statementParser.parseBody(builder)

        // Optional elseif clauses
        while (builder.tokenType == PicatTokenTypes.ELSEIF_KEYWORD) {
            builder.advanceLexer()
            skipWhitespace(builder)
            expressionParser.parseExpression(builder)
            expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
            skipWhitespace(builder)
            statementParser.parseBody(builder)
        }

        // Optional else clause
        if (builder.tokenType == PicatTokenTypes.ELSE_KEYWORD) {
            builder.advanceLexer()
            skipWhitespace(builder)
            statementParser.parseBody(builder)
        }

        skipWhitespace(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.IF_THEN_ELSE)
    }

    /**
     * Parses a foreach loop.
     */
    fun parseForeachLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.FOREACH_KEYWORD, "Expected 'foreach'")
        skipWhitespace(builder)

        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        skipWhitespace(builder)
        parseForeachGenerators(builder)
        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        skipWhitespace(builder)

        statementParser.parseBody(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
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
            skipWhitespace(builder)
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

        skipWhitespace(builder)

        // Expect 'in' keyword
        expectKeyword(builder, PicatTokenTypes.IN_KEYWORD, "Expected 'in'")
        skipWhitespace(builder)

        // Parse expression
        expressionParser.parseExpression(builder)

        marker.done(PicatTokenTypes.FOREACH_GENERATOR)
    }

    /**
     * Parses a while loop.
     */
    fun parseWhileLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.WHILE_KEYWORD, "Expected 'while'")
        skipWhitespace(builder)

        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        skipWhitespace(builder)
        expressionParser.parseExpression(builder)
        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        skipWhitespace(builder)

        statementParser.parseBody(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.WHILE_LOOP)
    }

    /**
     * Parses a case expression.
     */
    fun parseCaseExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.CASE_KEYWORD, "Expected 'case'")
        skipWhitespace(builder)

        expressionParser.parseExpression(builder)
        skipWhitespace(builder)

        expectKeyword(builder, PicatTokenTypes.OF_KEYWORD, "Expected 'of'")
        skipWhitespace(builder)

        parseCaseArms(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
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
            skipWhitespace(builder)
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
        expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        skipWhitespace(builder)

        statementParser.parseBody(builder)

        marker.done(PicatTokenTypes.CASE_ARM)
    }

    /**
     * Parses a try-catch statement.
     */
    fun parseTryCatch(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.TRY_KEYWORD, "Expected 'try'")
        skipWhitespace(builder)

        statementParser.parseBody(builder)

        // Parse catch clauses
        expectKeyword(builder, PicatTokenTypes.CATCH_KEYWORD, "Expected 'catch'")
        skipWhitespace(builder)

        parseCatchClauses(builder)

        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
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
            skipWhitespace(builder)
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
        expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        skipWhitespace(builder)

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
            expectToken(builder, keyword, errorMessage)
        } else {
            expectKeyword(builder, keyword, errorMessage)
        }

        // Parse expression if needed
        if (parseExpression) {
            skipWhitespace(builder)
            expressionParser.parseExpression(builder)
        }

        marker.done(resultType)
    }
}
