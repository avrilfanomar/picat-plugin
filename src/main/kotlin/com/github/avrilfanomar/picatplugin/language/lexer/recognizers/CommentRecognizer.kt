package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.TokenSkipper
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for comment tokens in the Picat lexer.
 * Handles both line comments (% to end of line) and multi-line comments (/* ... */).
 */
class CommentRecognizer : TokenRecognizer {
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        // Check for line comment (%)
        if (buffer[startOffset] == '%') {
            return true
        }

        // Check for multi-line comment (/* ... */)
        return startOffset + 1 < endOffset && 
               buffer[startOffset] == '/' && 
               buffer[startOffset + 1] == '*'
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        val skipper = TokenSkipper(buffer, endOffset)
        
        // Handle line comment (%)
        if (buffer[startOffset] == '%') {
            val endPos = skipper.skipLineComment(startOffset)
            return Pair(PicatTokenTypes.COMMENT, endPos)
        }
        
        // Handle multi-line comment (/* ... */)
        val endPos = skipper.skipMultiLineComment(startOffset)
        return Pair(PicatTokenTypes.COMMENT, endPos)
    }
}