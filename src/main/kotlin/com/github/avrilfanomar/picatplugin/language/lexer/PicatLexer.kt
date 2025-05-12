package com.github.avrilfanomar.picatplugin.language.lexer

import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.intellij.util.text.CharArrayUtil
import org.jetbrains.annotations.NotNull

/**
 * A simple lexer for Picat language.
 * This lexer tokenizes Picat code into tokens defined in PicatTokenTypes.
 */
class PicatLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var bufferEnd: Int = 0
    private var bufferStart: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: IElementType? = null

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

        // Skip whitespace
        if (isWhitespace(buffer[tokenStart])) {
            tokenEnd = skipWhitespace(tokenStart)
            currentToken = PicatTokenTypes.WHITE_SPACE
            return
        }

        // Handle comments (% to end of line or /* ... */)
        if (buffer[tokenStart] == '%') {
            tokenEnd = skipLineComment(tokenStart)
            currentToken = PicatTokenTypes.COMMENT
            return
        }

        // Handle multi-line comments /* ... */
        if (tokenStart + 1 < bufferEnd && buffer[tokenStart] == '/' && buffer[tokenStart + 1] == '*') {
            tokenEnd = skipMultiLineComment(tokenStart)
            currentToken = PicatTokenTypes.COMMENT
            return
        }

        // Handle string literals
        if (buffer[tokenStart] == '"') {
            tokenEnd = skipString(tokenStart)
            currentToken = PicatTokenTypes.STRING
            return
        }

        // Handle numbers
        if (isDigit(buffer[tokenStart])) {
            tokenEnd = skipNumber(tokenStart)
            currentToken = if (buffer.substring(tokenStart, tokenEnd).contains('.')) {
                PicatTokenTypes.FLOAT
            } else {
                PicatTokenTypes.INTEGER
            }
            return
        }

        // Handle variables (start with uppercase letter or underscore)
        if (isUpperCase(buffer[tokenStart]) || buffer[tokenStart] == '_') {
            tokenEnd = skipIdentifier(tokenStart)
            currentToken = PicatTokenTypes.VARIABLE
            return
        }

        // Handle identifiers and keywords
        if (isLetter(buffer[tokenStart])) {
            tokenEnd = skipIdentifier(tokenStart)
            val text = buffer.substring(tokenStart, tokenEnd)
            currentToken = when (text) {
                "import" -> PicatTokenTypes.IMPORT_KEYWORD
                "module" -> PicatTokenTypes.MODULE_KEYWORD
                "index" -> PicatTokenTypes.INDEX_KEYWORD
                "private" -> PicatTokenTypes.PRIVATE_KEYWORD
                "public" -> PicatTokenTypes.PUBLIC_KEYWORD
                "end" -> PicatTokenTypes.END_KEYWORD
                "if" -> PicatTokenTypes.IF_KEYWORD
                "then" -> PicatTokenTypes.THEN_KEYWORD
                "else" -> PicatTokenTypes.ELSE_KEYWORD
                "elseif" -> PicatTokenTypes.ELSEIF_KEYWORD
                "while" -> PicatTokenTypes.WHILE_KEYWORD
                "do" -> PicatTokenTypes.DO_KEYWORD
                "foreach" -> PicatTokenTypes.FOREACH_KEYWORD
                "for" -> PicatTokenTypes.FOR_KEYWORD
                "return" -> PicatTokenTypes.RETURN_KEYWORD
                "throw" -> PicatTokenTypes.THROW_KEYWORD
                "try" -> PicatTokenTypes.TRY_KEYWORD
                "catch" -> PicatTokenTypes.CATCH_KEYWORD
                "mod" -> PicatTokenTypes.MODULO
                else -> PicatTokenTypes.IDENTIFIER
            }
            return
        }

        // Handle operators and separators
        tokenEnd = tokenStart + 1

        // Check for three-character operators
        if (tokenEnd + 1 < bufferEnd) {
            val threeChars = buffer.substring(tokenStart, tokenStart + 3)
            if (threeChars == "?=>") {
                tokenEnd = tokenStart + 3
                currentToken = PicatTokenTypes.BACKTRACKABLE_ARROW_OP
                return
            }
        }

        // Check for two-character operators
        if (tokenEnd < bufferEnd) {
            val twoChars = buffer.substring(tokenStart, tokenStart + 2)
            if (twoChars == "=>" || twoChars == "==" || twoChars == "!=" || 
                twoChars == "<=" || twoChars == ">=" || twoChars == "&&" || twoChars == "||") {
                tokenEnd = tokenStart + 2
                currentToken = when (twoChars) {
                    "=>" -> PicatTokenTypes.ARROW_OP
                    "==" -> PicatTokenTypes.EQUAL
                    "!=" -> PicatTokenTypes.NOT_EQUAL
                    "<=" -> PicatTokenTypes.LESS_EQUAL
                    ">=" -> PicatTokenTypes.GREATER_EQUAL
                    "&&" -> PicatTokenTypes.AND
                    "||" -> PicatTokenTypes.OR
                    else -> PicatTokenTypes.BAD_CHARACTER
                }
                return
            }
        }

        // Single character operators and separators
        currentToken = when (buffer[tokenStart]) {
            '=' -> PicatTokenTypes.ASSIGN_OP
            '+' -> PicatTokenTypes.PLUS
            '-' -> PicatTokenTypes.MINUS
            '*' -> PicatTokenTypes.MULTIPLY
            '/' -> PicatTokenTypes.DIVIDE
            '<' -> PicatTokenTypes.LESS
            '>' -> PicatTokenTypes.GREATER
            '!' -> PicatTokenTypes.NOT
            '|' -> PicatTokenTypes.PIPE
            '@' -> PicatTokenTypes.AS_PATTERN
            '$' -> PicatTokenTypes.DATA_CONSTRUCTOR
            '^' -> PicatTokenTypes.POWER
            '#' -> PicatTokenTypes.HASH
            '~' -> PicatTokenTypes.TILDE
            '\\' -> PicatTokenTypes.BACKSLASH
            ',' -> PicatTokenTypes.COMMA
            '.' -> PicatTokenTypes.DOT
            ';' -> PicatTokenTypes.SEMICOLON
            ':' -> PicatTokenTypes.COLON
            '(' -> PicatTokenTypes.LPAR
            ')' -> PicatTokenTypes.RPAR
            '[' -> PicatTokenTypes.LBRACKET
            ']' -> PicatTokenTypes.RBRACKET
            '{' -> PicatTokenTypes.LBRACE
            '}' -> PicatTokenTypes.RBRACE
            else -> PicatTokenTypes.BAD_CHARACTER
        }
    }

    private fun skipWhitespace(start: Int): Int {
        var i = start
        while (i < bufferEnd && isWhitespace(buffer[i])) {
            i++
        }
        return i
    }

    private fun skipLineComment(start: Int): Int {
        var i = start
        while (i < bufferEnd && buffer[i] != '\n') {
            i++
        }
        return i
    }

    private fun skipMultiLineComment(start: Int): Int {
        var i = start + 2  // Skip the initial /*
        while (i + 1 < bufferEnd) {
            if (buffer[i] == '*' && buffer[i + 1] == '/') {
                return i + 2  // Skip the closing */
            }
            i++
        }
        return i
    }

    private fun skipString(start: Int): Int {
        var i = start + 1
        while (i < bufferEnd) {
            if (buffer[i] == '"' && buffer[i - 1] != '\\') {
                return i + 1
            }
            i++
        }
        return i
    }

    private fun skipNumber(start: Int): Int {
        var i = start
        var hasDecimalPoint = false

        while (i < bufferEnd) {
            if (buffer[i] == '.' && !hasDecimalPoint) {
                hasDecimalPoint = true
                i++
            } else if (isDigit(buffer[i])) {
                i++
            } else {
                break
            }
        }

        return i
    }

    private fun skipIdentifier(start: Int): Int {
        var i = start
        while (i < bufferEnd && (isLetter(buffer[i]) || isDigit(buffer[i]) || buffer[i] == '_')) {
            i++
        }
        return i
    }

    private fun isWhitespace(c: Char): Boolean = c.isWhitespace()
    private fun isLetter(c: Char): Boolean = c.isLetter()
    private fun isDigit(c: Char): Boolean = c.isDigit()
    private fun isUpperCase(c: Char): Boolean = c.isUpperCase()
}
