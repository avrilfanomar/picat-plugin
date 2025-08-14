package com.github.avrilfanomar.picatplugin.language.formatter

/**
 * Utilities for handling Picat comments in a formatting-safe manner.
 */
internal object PicatCommentTools {
    /**
     * Splits a line into code, optional comment part, and the whitespace between them.
     * Keeps whitespace before inline comments intact to respect user alignment.
     */
    fun splitCodeAndComment(line: String): Triple<String, String?, String> {
        val commentIndex = findUnquotedToken(line, "%")
        return if (commentIndex >= 0) {
            val rawCodePart = line.take(commentIndex)
            val commentPart = line.substring(commentIndex)
            val trailingWs = rawCodePart.takeLastWhile { it == ' ' || it == '\t' }
            val codePart = rawCodePart.dropLast(trailingWs.length)
            Triple(codePart, commentPart, trailingWs)
        } else {
            Triple(line, null, "")
        }
    }

    /**
     * Finds the index of the given token in the line, ignoring occurrences inside string/char literals.
     * Returns -1 if not found.
     */
    fun findUnquotedToken(line: String, token: String): Int {
        var i = 0
        var inQuote = false
        var quoteChar = '\u0000'
        val canMatch = token.isNotEmpty() && token.length <= line.length

        while (i <= line.length - token.length) {
            val ch = line[i]
            var step = 1

            if (inQuote) {
                when (ch) {
                    '\\' -> {
                        // Skip the escaped character (if present)
                        step = if (i + 1 < line.length) 2 else 1
                    }
                    quoteChar -> {
                        inQuote = false
                        quoteChar = '\u0000'
                    }
                }
            } else {
                if (ch == '"' || ch == '\'') {
                    inQuote = true
                    quoteChar = ch
                } else if (canMatch && line.regionMatches(i, token, 0, token.length)) {
                    return i
                }
            }

            i += step
        }

        return -1
    }
}
