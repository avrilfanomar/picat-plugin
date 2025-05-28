package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat patterns.
 * Patterns are used in case expressions and catch clauses.
 */
class PicatPatternParser : PicatBaseParser() {
    
    /**
     * Parses a Picat pattern.
     */
    fun parsePattern(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            PicatParserUtil.isVariable(builder.tokenType) -> parseVariable(builder)
            PicatParserUtil.isAtom(builder.tokenType) -> parseAtom(builder)
            PicatParserUtil.isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.ANONYMOUS_VARIABLE -> builder.advanceLexer()
            builder.tokenType == PicatTokenTypes.LBRACKET -> parseListPattern(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> parseTuplePattern(builder)
            isStructure(builder) -> parseStructurePattern(builder)
            else -> {
                builder.error("Expected pattern")
                builder.advanceLexer()
            }
        }

        marker.done(PicatTokenTypes.PATTERN)
    }

    /**
     * Parses a structure pattern like f(X, Y, Z).
     */
    private fun parseStructurePattern(builder: PsiBuilder) {
        val marker = builder.mark()
        parseAtom(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parsePatternList(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE_PATTERN)
    }

    /**
     * Parses a list pattern like [X, Y | Z].
     */
    private fun parseListPattern(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LBRACKET, "Expected '['")

        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parsePatternList(builder)

            if (builder.tokenType == PicatTokenTypes.PIPE) {
                builder.advanceLexer()
                parsePattern(builder)
            }
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RBRACKET, "Expected ']'")
        marker.done(PicatTokenTypes.LIST_PATTERN)
    }

    /**
     * Parses a tuple pattern like {X, Y, Z}.
     */
    private fun parseTuplePattern(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LBRACE, "Expected '{'")

        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parsePatternList(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RBRACE, "Expected '}'")
        marker.done(PicatTokenTypes.TUPLE_PATTERN)
    }

    /**
     * Parses a comma-separated list of patterns.
     */
    private fun parsePatternList(builder: PsiBuilder) {
        val marker = builder.mark()
        parsePattern(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parsePattern(builder)
        }

        marker.done(PicatTokenTypes.PATTERN_LIST)
    }
}