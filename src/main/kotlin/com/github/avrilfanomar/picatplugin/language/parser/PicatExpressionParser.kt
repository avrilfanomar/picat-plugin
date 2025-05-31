package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isNumber
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isVariable
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat expressions.
 */
class PicatExpressionParser : PicatBaseParser() {
    // Helper for parsing complex data structures
    private lateinit var helper: PicatExpressionParserHelper
    // Helper for parsing binary expressions
    private lateinit var binaryHelper: PicatBinaryExpressionParserHelper

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        super.initialize(parserContext)
        helper = PicatExpressionParserHelper()
        helper.initialize(parserContext)
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
        skipWhitespace(builder)
        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            builder.advanceLexer()
            skipWhitespace(builder)
            parseExpression(builder)
            skipWhitespace(builder)
            expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            skipWhitespace(builder)
            parseExpression(builder)
        }

        marker.done(PicatTokenTypes.EXPRESSION)
    }


    fun parsePrimaryExpression(builder: PsiBuilder) {
        when {
            isAtom(builder.tokenType) -> {
                if (isAtom(builder.tokenType)) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected atom")
                }
            }
            isVariable(builder.tokenType) -> parseVariable(builder)
            isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.STRING -> parseString(builder)
            builder.tokenType == PicatTokenTypes.LPAR -> {
                builder.advanceLexer()
                skipWhitespace(builder)
                parseExpression(builder)
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
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
