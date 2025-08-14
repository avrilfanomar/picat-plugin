package com.github.avrilfanomar.picatplugin.language.formatter

/**
 * Utilities to protect and restore string/char literals so that formatter
 * does not alter content inside quotes.
 */
internal object PicatLiteralTools {
    fun protectLiterals(code: String): Pair<String, List<String>> {
        if (code.isEmpty()) return code to emptyList()
        val saved = mutableListOf<String>()
        val out = StringBuilder()
        var i = 0
        while (i < code.length) {
            val ch = code[i]
            if (ch == '"' || ch == '\'') {
                val j = findClosing(i, code, ch)
                val literal = code.substring(i, minOf(j, code.length))
                val placeholder = $$"__LIT_$$${saved.size}__"
                saved += literal
                out.append(placeholder)
                i = j
            } else {
                out.append(ch)
                i++
            }
        }
        return out.toString() to saved
    }

    private fun findClosing(i: Int, code: String, quote: Char): Int {
        var j = i + 1
        // scan until matching quote, honoring backslash escapes
        while (j < code.length) {
            val cj = code[j]
            if (cj == quote) {
                j++ // include closing quote
                break
            }
            if (cj == '\\') {
                // skip escaped next char if any
                j += if (j + 1 < code.length) 2 else 1
            } else {
                j++
            }
        }
        if (j > code.length) j = code.length
        return j
    }

    fun restoreLiterals(code: String, literals: List<String>): String {
        var result = code
        for ((idx, lit) in literals.withIndex()) {
            result = result.replace($$"__LIT_$$${idx}__", lit)
        }
        return result
    }
}
