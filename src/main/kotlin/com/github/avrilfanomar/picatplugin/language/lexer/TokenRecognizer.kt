package com.github.avrilfanomar.picatplugin.language.lexer

import com.intellij.psi.tree.IElementType

/**
 * Interface for token recognizers in the Picat lexer.
 * Each recognizer is responsible for recognizing a specific type of token.
 */
interface TokenRecognizer {
    /**
     * Checks if the recognizer can recognize a token at the given position.
     * @param buffer The character buffer.
     * @param startOffset The starting position in the buffer.
     * @param endOffset The ending position in the buffer.
     * @return True if the recognizer can recognize a token at the given position, false otherwise.
     */
    fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean

    /**
     * Recognizes a token at the given position.
     * @param buffer The character buffer.
     * @param startOffset The starting position in the buffer.
     * @param endOffset The ending position in the buffer.
     * @return A pair containing the token type and the ending position of the token.
     */
    fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int>
}