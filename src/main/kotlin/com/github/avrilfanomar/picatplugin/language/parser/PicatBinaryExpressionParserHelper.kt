package com.github.avrilfanomar.picatplugin.language.parser

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
        parseLogicalAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.OR_KEYWORD) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseLogicalAndExpression(builder)
        }
    }

    /**
     * Parses a logical AND expression.
     */
    fun parseLogicalAndExpression(builder: PsiBuilder) {
        parseBitwiseOrExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AND_KEYWORD) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseBitwiseOrExpression(builder)
        }
    }

    /**
     * Parses a bitwise OR expression.
     */
    fun parseBitwiseOrExpression(builder: PsiBuilder) {
        parseBitwiseXorExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PIPE) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseBitwiseXorExpression(builder)
        }
    }

    /**
     * Parses a bitwise XOR expression.
     */
    fun parseBitwiseXorExpression(builder: PsiBuilder) {
        parseBitwiseAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.XOR_KEYWORD || builder.tokenType == PicatTokenTypes.CARET) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseBitwiseAndExpression(builder)
        }
    }

    /**
     * Parses a bitwise AND expression.
     */
    fun parseBitwiseAndExpression(builder: PsiBuilder) {
        parseEqualityExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AMPERSAND) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseEqualityExpression(builder)
        }
    }

    /**
     * Parses an equality expression.
     */
    fun parseEqualityExpression(builder: PsiBuilder) {
        parseRelationalExpression(builder)
        while (builder.tokenType == PicatTokenTypes.IDENTICAL || builder.tokenType == PicatTokenTypes.NOT_IDENTICAL) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseRelationalExpression(builder)
        }
    }

    /**
     * Parses a relational expression.
     */
    fun parseRelationalExpression(builder: PsiBuilder) {
        parseShiftExpression(builder)
        while (PicatParserUtil.isRelationalOperator(builder.tokenType)) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseShiftExpression(builder)
        }
    }

    /**
     * Parses a shift expression.
     */
    fun parseShiftExpression(builder: PsiBuilder) {
        parseAdditiveExpression(builder)
        while (builder.tokenType == PicatTokenTypes.SHIFT_LEFT || builder.tokenType == PicatTokenTypes.SHIFT_RIGHT) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseAdditiveExpression(builder)
        }
    }

    /**
     * Parses an additive expression.
     */
    fun parseAdditiveExpression(builder: PsiBuilder) {
        parseMultiplicativeExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PLUS || builder.tokenType == PicatTokenTypes.MINUS) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseMultiplicativeExpression(builder)
        }
    }

    /**
     * Parses a multiplicative expression.
     */
    fun parseMultiplicativeExpression(builder: PsiBuilder) {
        parsePowerExpression(builder)
        while (PicatParserUtil.isMultiplicativeOperator(builder.tokenType)) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parsePowerExpression(builder)
        }
    }

    /**
     * Parses a power expression.
     */
    fun parsePowerExpression(builder: PsiBuilder) {
        parseUnaryExpression(builder)
        while (builder.tokenType == PicatTokenTypes.POWER) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseUnaryExpression(builder)
        }
    }

    /**
     * Parses a unary expression.
     */
    private fun parseUnaryExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        if (PicatParserUtil.isUnaryOperator(builder.tokenType)) {
            builder.advanceLexer()
        }

        context.expressionParser.parsePrimaryExpression(builder)
        marker.done(PicatTokenTypes.UNARY_EXPRESSION)
    }
}
