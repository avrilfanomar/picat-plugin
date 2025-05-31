package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isNumber
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isVariable
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
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
            isVariable(builder.tokenType) -> parseVariable(builder)
            isAtom(builder.tokenType) -> {
                if (isAtom(builder.tokenType)) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected atom")
                }
            }
            isNumber(builder.tokenType) -> parseNumber(builder)
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
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        skipWhitespace(builder)

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parsePatternList(builder)
        }

        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE_PATTERN)
    }

    /**
     * Parses a list pattern like [X, Y | Z].
     */
    private fun parseListPattern(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACKET, "Expected '['")
        skipWhitespace(builder)

        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parsePatternList(builder)

            if (builder.tokenType == PicatTokenTypes.PIPE) {
                builder.advanceLexer()
                skipWhitespace(builder)
                parsePattern(builder)
            }
        }

        expectToken(builder, PicatTokenTypes.RBRACKET, "Expected ']'")
        marker.done(PicatTokenTypes.LIST_PATTERN)
    }

    /**
     * Parses a tuple pattern like {X, Y, Z}.
     */
    private fun parseTuplePattern(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACE, "Expected '{'")
        skipWhitespace(builder)

        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parsePatternList(builder)
        }

        expectToken(builder, PicatTokenTypes.RBRACE, "Expected '}'")
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
            skipWhitespace(builder)
            parsePattern(builder)
        }

        marker.done(PicatTokenTypes.PATTERN_LIST)
    }
}
