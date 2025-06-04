package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isNumber
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isVariable
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.isArithmeticOperator
import com.github.avrilfanomar.picatplugin.language.psi.isComparisonOperator
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
        val exprMarker = builder.mark()

        // Parse the first term
        parseTerm(builder)
        skipWhitespace(builder)

        // Parse binary operators and their right-hand terms
        while (!builder.eof() && (isArithmeticOperator(builder.tokenType) || isComparisonOperator(builder.tokenType))) {

            // Create a marker for the operator
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)

            // Parse the right-hand term
            parseTerm(builder)
            skipWhitespace(builder)
        }

        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseExpression(builder)
            skipWhitespace(builder)
            val colonMarker = builder.mark()
            expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            colonMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseExpression(builder)
        }

        exprMarker.done(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Parses a term in an expression.
     */
    private fun parseTerm(builder: PsiBuilder) {
        val termMarker = builder.mark()
        when {
            builder.tokenType == PicatTokenTypes.LPAR -> {
                // Parse a parenthesized expression
                val openParenMarker = builder.mark()
                builder.advanceLexer()
                openParenMarker.done(PicatTokenTypes.OPERATOR)
                skipWhitespace(builder)

                // Parse the expression inside the parentheses
                parseExpression(builder)

                skipWhitespace(builder)
                val closeParenMarker = builder.mark()
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
                closeParenMarker.done(PicatTokenTypes.OPERATOR)
            }
            else -> {
                // Parse a primary expression without creating a term marker
                // since we've already created one
                when {
                    isStructure(builder) -> parseStructure(builder)
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
                    builder.tokenType == PicatTokenTypes.LBRACKET -> helper.parseList(builder)
                    builder.tokenType == PicatTokenTypes.LBRACE -> helper.parseMap(builder)
                    else -> {
                        builder.error("Expected primary expression")
                        builder.advanceLexer()
                    }
                }
            }
        }
        termMarker.done(PicatTokenTypes.TERM)
    }


    fun parsePrimaryExpression(builder: PsiBuilder) {
        val termMarker = builder.mark()
        when {
            isStructure(builder) -> parseStructure(builder)
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
                // Create a marker for the parenthesized expression
                val openParenMarker = builder.mark()
                builder.advanceLexer()
                openParenMarker.done(PicatTokenTypes.OPERATOR)
                skipWhitespace(builder)

                // Parse the expression inside the parentheses
                // This will create its own expression marker
                binaryHelper.parseLogicalOrExpression(builder)

                skipWhitespace(builder)
                val closeParenMarker = builder.mark()
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
                closeParenMarker.done(PicatTokenTypes.OPERATOR)
            }
            builder.tokenType == PicatTokenTypes.LBRACKET -> helper.parseList(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> helper.parseMap(builder)
            else -> {
                builder.error("Expected primary expression")
                builder.advanceLexer()
            }
        }
        termMarker.done(PicatTokenTypes.TERM)
    }
}
