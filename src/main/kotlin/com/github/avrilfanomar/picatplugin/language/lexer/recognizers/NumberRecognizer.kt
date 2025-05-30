package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.CharacterClassifier
import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.TokenSkipper
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for numeric literals in the Picat lexer.
 * Handles integers, floats, hexadecimal, octal, and binary numbers.
 */
class NumberRecognizer : TokenRecognizer {
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        // Check if the character is a digit
        return CharacterClassifier.isDigit(buffer[startOffset])
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        val skipper = TokenSkipper(buffer, endOffset)

        // Check for special number formats (hex, octal, binary)
        val specialFormat = recognizeSpecialNumberFormat(buffer, startOffset, endOffset, skipper)
        if (specialFormat != null) {
            return specialFormat
        }

        // Handle regular numbers (integer or float)
        val endPos = skipper.skipNumber(startOffset)
        val tokenText = buffer.substring(startOffset, endPos)

        return if (tokenText.contains('.')) {
            Pair(PicatTokenTypes.FLOAT, endPos)
        } else {
            Pair(PicatTokenTypes.INTEGER, endPos)
        }
    }

    /**
     * Recognizes special number formats (hex, octal, binary).
     * Returns null if the number is not in a special format.
     */
    private fun recognizeSpecialNumberFormat(
        buffer: CharSequence, 
        startOffset: Int, 
        endOffset: Int,
        skipper: TokenSkipper
    ): Pair<IElementType, Int>? {
        var result: Pair<IElementType, Int>? = null

        if (startOffset + 2 < endOffset && buffer[startOffset] == '0') {
            when (buffer[startOffset + 1]) {
                'x', 'X' -> {
                    val endPos = skipper.skipHexNumber(startOffset)
                    result = Pair(PicatTokenTypes.HEX_INTEGER, endPos)
                }
                'o', 'O' -> {
                    val endPos = skipper.skipOctalNumber(startOffset)
                    result = Pair(PicatTokenTypes.OCTAL_INTEGER, endPos)
                }
                'b', 'B' -> {
                    val endPos = skipper.skipBinaryNumber(startOffset)
                    result = Pair(PicatTokenTypes.BINARY_INTEGER, endPos)
                }
            }
        }

        return result
    }
}
