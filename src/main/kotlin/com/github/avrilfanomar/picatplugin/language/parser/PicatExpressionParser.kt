package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat expressions.
 */
class PicatExpressionParser : PicatBaseParser() {
    
    /**
     * Parses a Picat expression.
     */
    fun parseExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        parseLogicalOrExpression(builder)

        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            builder.advanceLexer()
            parseExpression(builder)
            PicatParserUtil.expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            parseExpression(builder)
        }

        marker.done(PicatTokenTypes.EXPRESSION)
    }

    private fun parseLogicalOrExpression(builder: PsiBuilder) {
        parseLogicalAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.OR_KEYWORD) {
            builder.advanceLexer()
            parseLogicalAndExpression(builder)
        }
    }

    private fun parseLogicalAndExpression(builder: PsiBuilder) {
        parseBitwiseOrExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AND_KEYWORD) {
            builder.advanceLexer()
            parseBitwiseOrExpression(builder)
        }
    }

    private fun parseBitwiseOrExpression(builder: PsiBuilder) {
        parseBitwiseXorExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PIPE) {
            builder.advanceLexer()
            parseBitwiseXorExpression(builder)
        }
    }

    private fun parseBitwiseXorExpression(builder: PsiBuilder) {
        parseBitwiseAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.XOR_KEYWORD || builder.tokenType == PicatTokenTypes.CARET) {
            builder.advanceLexer()
            parseBitwiseAndExpression(builder)
        }
    }

    private fun parseBitwiseAndExpression(builder: PsiBuilder) {
        parseEqualityExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AMPERSAND) {
            builder.advanceLexer()
            parseEqualityExpression(builder)
        }
    }

    private fun parseEqualityExpression(builder: PsiBuilder) {
        parseRelationalExpression(builder)
        while (builder.tokenType == PicatTokenTypes.IDENTICAL || builder.tokenType == PicatTokenTypes.NOT_IDENTICAL) {
            builder.advanceLexer()
            parseRelationalExpression(builder)
        }
    }

    private fun parseRelationalExpression(builder: PsiBuilder) {
        parseShiftExpression(builder)
        while (PicatParserUtil.isRelationalOperator(builder.tokenType)) {
            builder.advanceLexer()
            parseShiftExpression(builder)
        }
    }

    private fun parseShiftExpression(builder: PsiBuilder) {
        parseAdditiveExpression(builder)
        while (builder.tokenType == PicatTokenTypes.SHIFT_LEFT || builder.tokenType == PicatTokenTypes.SHIFT_RIGHT) {
            builder.advanceLexer()
            parseAdditiveExpression(builder)
        }
    }

    private fun parseAdditiveExpression(builder: PsiBuilder) {
        parseMultiplicativeExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PLUS || builder.tokenType == PicatTokenTypes.MINUS) {
            builder.advanceLexer()
            parseMultiplicativeExpression(builder)
        }
    }

    private fun parseMultiplicativeExpression(builder: PsiBuilder) {
        parsePowerExpression(builder)
        while (PicatParserUtil.isMultiplicativeOperator(builder.tokenType)) {
            builder.advanceLexer()
            parsePowerExpression(builder)
        }
    }

    private fun parsePowerExpression(builder: PsiBuilder) {
        parseUnaryExpression(builder)
        while (builder.tokenType == PicatTokenTypes.POWER) {
            builder.advanceLexer()
            parseUnaryExpression(builder)
        }
    }

    private fun parseUnaryExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        if (PicatParserUtil.isUnaryOperator(builder.tokenType)) {
            builder.advanceLexer()
        }

        parsePrimaryExpression(builder)
        marker.done(PicatTokenTypes.UNARY_EXPRESSION)
    }

    private fun parsePrimaryExpression(builder: PsiBuilder) {
        when {
            PicatParserUtil.isAtom(builder.tokenType) -> parseAtom(builder)
            PicatParserUtil.isVariable(builder.tokenType) -> parseVariable(builder)
            PicatParserUtil.isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.STRING -> parseString(builder)
            builder.tokenType == PicatTokenTypes.LPAR -> {
                builder.advanceLexer()
                parseExpression(builder)
                PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
            }
            builder.tokenType == PicatTokenTypes.LBRACKET -> parseList(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> parseMap(builder)
            else -> {
                builder.error("Expected primary expression")
                builder.advanceLexer()
            }
        }
    }

    private fun parseList(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LBRACKET, "Expected '['")

        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parseListItems(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RBRACKET, "Expected ']'")
        marker.done(PicatTokenTypes.LIST)
    }

    private fun parseListItems(builder: PsiBuilder) {
        parseExpression(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseExpression(builder)
        }

        if (builder.tokenType == PicatTokenTypes.PIPE) {
            builder.advanceLexer()
            parseExpression(builder)
        }
    }

    private fun parseMap(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LBRACE, "Expected '{'")

        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parseMapEntries(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RBRACE, "Expected '}'")
        marker.done(PicatTokenTypes.MAP)
    }

    private fun parseMapEntries(builder: PsiBuilder) {
        parseMapEntry(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseMapEntry(builder)
        }
    }

    private fun parseMapEntry(builder: PsiBuilder) {
        parseExpression(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.COLON, "Expected ':'")
        parseExpression(builder)
    }
}
