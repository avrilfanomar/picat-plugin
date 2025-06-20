package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isMultiplicativeOperator
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isRelationalOperator
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isUnaryOperator
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Helper class for parsing binary expressions in Picat.
 * Contains methods for parsing different types of binary expressions.
 */
class PicatBinaryExpressionParserHelper : PicatBaseParser() {
    /**
     * Parses a logical OR expression.
     */
    fun parseLogicalOrExpression(builder: PsiBuilder) {
        val exprMarker = builder.mark()
        parseLogicalAndExpression(builder)

        while (builder.tokenType == PicatTokenTypes.OR_KEYWORD) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseLogicalAndExpression(builder)
        }

        exprMarker.done(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Parses a logical AND expression.
     */
    fun parseLogicalAndExpression(builder: PsiBuilder) {
        parseBitwiseOrExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AND_KEYWORD) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseBitwiseOrExpression(builder)
        }
    }

    /**
     * Parses a bitwise OR expression.
     */
    fun parseBitwiseOrExpression(builder: PsiBuilder) {
        parseBitwiseXorExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PIPE) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseBitwiseXorExpression(builder)
        }
    }

    /**
     * Parses a bitwise XOR expression.
     */
    fun parseBitwiseXorExpression(builder: PsiBuilder) {
        parseBitwiseAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.XOR_KEYWORD || builder.tokenType == PicatTokenTypes.CARET) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseBitwiseAndExpression(builder)
        }
    }

    /**
     * Parses a bitwise AND expression.
     */
    fun parseBitwiseAndExpression(builder: PsiBuilder) {
        parseEqualityExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AMPERSAND) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseEqualityExpression(builder)
        }
    }

    /**
     * Parses an equality expression.
     */
    fun parseEqualityExpression(builder: PsiBuilder) {
        parseRelationalExpression(builder)
        while (builder.tokenType == PicatTokenTypes.IDENTICAL || builder.tokenType == PicatTokenTypes.NOT_IDENTICAL) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseRelationalExpression(builder)
        }
    }

    /**
     * Parses a relational expression.
     */
    fun parseRelationalExpression(builder: PsiBuilder) {
        parseShiftExpression(builder)
        while (isRelationalOperator(builder.tokenType)) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseShiftExpression(builder)
        }
    }

    /**
     * Parses a shift expression.
     */
    fun parseShiftExpression(builder: PsiBuilder) {
        parseAdditiveExpression(builder)
        while (builder.tokenType == PicatTokenTypes.SHIFT_LEFT || builder.tokenType == PicatTokenTypes.SHIFT_RIGHT) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseAdditiveExpression(builder)
        }
    }

    /**
     * Parses an additive expression.
     */
    fun parseAdditiveExpression(builder: PsiBuilder) {
        parseMultiplicativeExpression(builder)

        while (builder.tokenType == PicatTokenTypes.PLUS || builder.tokenType == PicatTokenTypes.MINUS) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseMultiplicativeExpression(builder)
        }
    }

    /**
     * Parses a multiplicative expression.
     */
    fun parseMultiplicativeExpression(builder: PsiBuilder) {
        val exprMarker = builder.mark()
        parsePowerExpression(builder)

        while (isMultiplicativeOperator(builder.tokenType)) {
            val operatorMarker = builder.mark()
            // Advance the lexer for any multiplicative operator
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parsePowerExpression(builder)
        }

        exprMarker.done(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Parses a power expression.
     */
    fun parsePowerExpression(builder: PsiBuilder) {
        parseUnaryExpression(builder)
        while (builder.tokenType == PicatTokenTypes.POWER) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseUnaryExpression(builder)
        }
    }

    /**
     * Parses a unary expression.
     */
    private fun parseUnaryExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        if (isUnaryOperator(builder.tokenType)) {
            builder.advanceLexer()
        }

        context.expressionParser.parsePrimaryExpression(builder)
        marker.done(PicatTokenTypes.UNARY_EXPRESSION)
    }
}
