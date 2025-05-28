package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat comments.
 */
class PicatCommentParser : PicatBaseParser() {
    /**
     * Parses a single-line comment.
     */
    fun parseSingleLineComment(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(PicatTokenTypes.COMMENT)
    }

    /**
     * Parses a multi-line comment.
     */
    fun parseMultiLineComment(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(PicatTokenTypes.MULTI_LINE_COMMENT)
    }

    /**
     * Parses any type of comment based on the current token type.
     * This method handles both single-line and multi-line comments.
     */
    fun parseAnyComment(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.COMMENT -> parseSingleLineComment(builder)
            PicatTokenTypes.MULTI_LINE_COMMENT -> parseMultiLineComment(builder)
            else -> builder.error("Expected comment")
        }
    }
}
