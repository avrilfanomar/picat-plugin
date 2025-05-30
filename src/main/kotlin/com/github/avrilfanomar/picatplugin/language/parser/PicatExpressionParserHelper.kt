package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Helper class for PicatExpressionParser.
 * Contains methods for parsing specific expression types.
 */
class PicatExpressionParserHelper : PicatBaseParser() {
    /**
     * Parses a list in Picat syntax.
     */
    fun parseList(builder: PsiBuilder) {
        val listMarker = builder.mark()

        // Consume the opening bracket
        builder.advanceLexer()

        // Parse list items if the list is not empty
        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parseListItems(builder)
        }

        // Expect closing bracket
        if (builder.tokenType == PicatTokenTypes.RBRACKET) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ']'")
        }

        listMarker.done(PicatTokenTypes.LIST)
    }

    /**
     * Parses the items in a list, including the tail.
     */
    fun parseListItems(builder: PsiBuilder) {
        val listItemsMarker = builder.mark()

        // Parse the first expression
        expressionParser.parseExpression(builder)

        // Parse additional expressions or list tail
        var shouldContinue = true
        while (shouldContinue && builder.tokenType != PicatTokenTypes.RBRACKET && !builder.eof()) {
            when (builder.tokenType) {
                PicatTokenTypes.PIPE -> {
                    // List tail (e.g., [1, 2 | Rest])
                    builder.advanceLexer()
                    PicatParserUtil.skipWhitespace(builder)
                    expressionParser.parseExpression(builder)
                    shouldContinue = false
                }
                PicatTokenTypes.COMMA -> {
                    // Another list item
                    builder.advanceLexer()
                    PicatParserUtil.skipWhitespace(builder)
                    expressionParser.parseExpression(builder)
                }
                else -> {
                    builder.error("Expected ',' or '|' or ']'")
                    shouldContinue = false
                }
            }
        }

        listItemsMarker.done(PicatTokenTypes.LIST_ELEMENTS)
    }

    /**
     * Parses a map in Picat syntax.
     */
    fun parseMap(builder: PsiBuilder) {
        val mapMarker = builder.mark()

        // Consume the opening brace
        builder.advanceLexer()

        // Parse map entries if the map is not empty
        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parseMapEntries(builder)
        }

        // Expect closing brace
        if (builder.tokenType == PicatTokenTypes.RBRACE) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '}'")
        }

        mapMarker.done(PicatTokenTypes.MAP)
    }

    /**
     * Parses the entries in a map.
     */
    fun parseMapEntries(builder: PsiBuilder) {
        val mapEntriesMarker = builder.mark()

        // Parse the first key-value pair
        parseMapEntry(builder)

        // Parse additional key-value pairs
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            PicatParserUtil.skipWhitespace(builder)
            parseMapEntry(builder)
        }

        mapEntriesMarker.done(PicatTokenTypes.MAP_ENTRIES)
    }

    /**
     * Parses a single key-value pair in a map.
     */
    private fun parseMapEntry(builder: PsiBuilder) {
        val entryMarker = builder.mark()

        // Parse the key
        expressionParser.parseExpression(builder)

        // Expect the map association operator
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            PicatParserUtil.skipWhitespace(builder)
        } else {
            builder.error("Expected '=>'")
        }

        // Parse the value
        expressionParser.parseExpression(builder)

        entryMarker.done(PicatTokenTypes.MAP_ENTRY)
    }
}
