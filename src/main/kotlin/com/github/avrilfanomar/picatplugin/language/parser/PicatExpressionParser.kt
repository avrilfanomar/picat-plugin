package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat expressions.
 */
class PicatExpressionParser : PicatBaseParser() {
    // Helper for parsing complex data structures
    private val helper = PicatExpressionParserHelper()
    // Helper for parsing binary expressions
    private lateinit var binaryHelper: PicatBinaryExpressionParserHelper

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        super.initialize(parserContext)
        binaryHelper = PicatBinaryExpressionParserHelper()
        binaryHelper.initialize(parserContext)
    }

    /**
     * Parses a Picat pattern.
     * Delegates to the pattern parser for actual pattern parsing.
     */
    fun parsePattern(builder: PsiBuilder) {
        patternParser.parsePattern(builder)
    }

    /**
     * Parses a Picat expression.
     */
    fun parseExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        binaryHelper.parseLogicalOrExpression(builder)

        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseExpression(builder)
            PicatParserUtil.expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseExpression(builder)
        }

        marker.done(PicatTokenTypes.EXPRESSION)
    }


    fun parsePrimaryExpression(builder: PsiBuilder) {
        when {
            PicatParserUtil.isAtom(builder.tokenType) -> parseAtom(builder)
            PicatParserUtil.isVariable(builder.tokenType) -> parseVariable(builder)
            PicatParserUtil.isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.STRING -> parseString(builder)
            builder.tokenType == PicatTokenTypes.LPAR -> {
                builder.advanceLexer()
                while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                    builder.advanceLexer()
                }
                parseExpression(builder)
                PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
            }
            builder.tokenType == PicatTokenTypes.LBRACKET -> helper.parseList(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> helper.parseMap(builder)
            else -> {
                builder.error("Expected primary expression")
                builder.advanceLexer()
            }
        }
    }
}
