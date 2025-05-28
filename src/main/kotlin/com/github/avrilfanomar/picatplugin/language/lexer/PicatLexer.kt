package com.github.avrilfanomar.picatplugin.language.lexer

import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.WhitespaceRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.CommentRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.StringRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.NumberRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.IdentifierRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.recognizers.OperatorRecognizer
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NotNull

/**
 * A lexer for Picat language following SOLID principles.
 * This lexer tokenizes Picat code into tokens defined in PicatTokenTypes.
 * It delegates token recognition to specialized recognizer classes.
 */
class PicatLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var bufferEnd: Int = 0
    private var bufferStart: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: IElementType? = null

    // Token recognizers
    private val recognizers = listOf(
        WhitespaceRecognizer(),
        CommentRecognizer(),
        StringRecognizer(),
        NumberRecognizer(),
        IdentifierRecognizer(),
        OperatorRecognizer()
    )

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.bufferStart = startOffset
        this.bufferEnd = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentToken

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    @NotNull
    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = bufferEnd

    override fun advance() {
        tokenStart = tokenEnd

        if (tokenStart >= bufferEnd) {
            currentToken = null
            return
        }

        // Try each recognizer in order
        for (recognizer in recognizers) {
            if (recognizer.canRecognize(buffer, tokenStart, bufferEnd)) {
                val (tokenType, endPos) = recognizer.recognize(buffer, tokenStart, bufferEnd)
                currentToken = tokenType
                tokenEnd = endPos
                return
            }
        }

        // If no recognizer matched, mark as bad character
        tokenEnd = tokenStart + 1
        currentToken = PicatTokenTypes.BAD_CHARACTER
    }
}
