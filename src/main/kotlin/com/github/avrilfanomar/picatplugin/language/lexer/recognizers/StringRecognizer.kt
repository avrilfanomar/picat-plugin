package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.TokenSkipper
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for string literals and quoted atoms in the Picat lexer.
 * Handles both double-quoted strings and single-quoted atoms.
 */
class StringRecognizer : TokenRecognizer {
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        // Check for string literal (")
        if (buffer[startOffset] == '"') {
            return true
        }

        // Check for quoted atom (')
        return buffer[startOffset] == '\''
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        val skipper = TokenSkipper(buffer, endOffset)
        
        // Handle string literal (")
        if (buffer[startOffset] == '"') {
            val endPos = skipper.skipString(startOffset)
            return Pair(PicatTokenTypes.STRING, endPos)
        }
        
        // Handle quoted atom (')
        val endPos = skipper.skipQuotedAtom(startOffset)
        return Pair(PicatTokenTypes.QUOTED_ATOM, endPos)
    }
}