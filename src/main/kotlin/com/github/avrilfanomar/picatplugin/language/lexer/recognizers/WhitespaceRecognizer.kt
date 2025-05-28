package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.CharacterClassifier
import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.TokenSkipper
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for whitespace tokens in the Picat lexer.
 */
class WhitespaceRecognizer : TokenRecognizer {
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        return startOffset < endOffset && CharacterClassifier.isWhitespace(buffer[startOffset])
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        val skipper = TokenSkipper(buffer, endOffset)
        val endPos = skipper.skipWhitespace(startOffset)
        return Pair(PicatTokenTypes.WHITE_SPACE, endPos)
    }
}