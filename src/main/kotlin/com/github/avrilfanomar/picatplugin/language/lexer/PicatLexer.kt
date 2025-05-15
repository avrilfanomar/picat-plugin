package com.github.avrilfanomar.picatplugin.language.lexer

import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
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

        // Handle single-quoted atoms
        if (buffer[tokenStart] == '\'') {
            tokenEnd = skipQuotedAtom(tokenStart)
            currentToken = PicatTokenTypes.QUOTED_ATOM
            return
        }

        // Handle numbers
        if (isDigit(buffer[tokenStart])) {
            // Check for hex, octal, or binary numbers
            if (tokenStart + 2 < bufferEnd && buffer[tokenStart] == '0') {
                when (buffer[tokenStart + 1]) {
                    'x', 'X' -> {
                        tokenEnd = skipHexNumber(tokenStart)
                        currentToken = PicatTokenTypes.HEX_INTEGER
                        return
                    }

                    'o', 'O' -> {
                        tokenEnd = skipOctalNumber(tokenStart)
                        currentToken = PicatTokenTypes.OCTAL_INTEGER
                        return
                    }

                    'b', 'B' -> {
                        tokenEnd = skipBinaryNumber(tokenStart)
                        currentToken = PicatTokenTypes.BINARY_INTEGER
                        return
                    }
                }
            }

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
                "table" -> PicatTokenTypes.TABLE_KEYWORD
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
                "not" -> PicatTokenTypes.NOT_KEYWORD
                "once" -> PicatTokenTypes.ONCE_KEYWORD
                "div" -> PicatTokenTypes.DIV_KEYWORD
                "mod" -> PicatTokenTypes.MOD_KEYWORD
                "rem" -> PicatTokenTypes.REM_KEYWORD
                "in" -> PicatTokenTypes.IN_KEYWORD
                "notin" -> PicatTokenTypes.NOTIN_KEYWORD
                "writef" -> PicatTokenTypes.WRITEF_KEYWORD
                "true" -> PicatTokenTypes.TRUE_KEYWORD
                "false" -> PicatTokenTypes.FALSE_KEYWORD
                "fail" -> PicatTokenTypes.FAIL_KEYWORD
                "repeat" -> PicatTokenTypes.REPEAT_KEYWORD
                else -> PicatTokenTypes.IDENTIFIER
            }
            return
        }

        // Handle operators and separators
        tokenEnd = tokenStart + 1

        // Check for four-character operators
        if (tokenStart + 3 < bufferEnd) {
            val fourChars = buffer.substring(tokenStart, tokenStart + 4)
            when (fourChars) {
                "#<=>" -> {
                    tokenEnd = tokenStart + 4
                    currentToken = PicatTokenTypes.CONSTRAINT_EQUIV
                    return
                }
            }
        }

        // Check for three-character operators
        if (tokenStart + 2 < bufferEnd) {
            val threeChars = buffer.substring(tokenStart, tokenStart + 3)
            when (threeChars) {
                "?=>" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.BACKTRACKABLE_ARROW_OP
                    return
                }

                "!==" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.NOT_IDENTICAL
                    return
                }

                "===" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.IDENTICAL
                    return
                }

                "#=>" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_IMPL
                    return
                }

                "#/\\" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_AND
                    return
                }

                "#\\/" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_OR
                    return
                }

                "#<=" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_LE_ALT
                    return
                }

                "#!=" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_NEQ
                    return
                }

                "#>=" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_GE
                    return
                }

                "#=<" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.CONSTRAINT_LE
                    return
                }

                "@=<" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.TERM_LE
                    return
                }

                "@<=" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.TERM_LE_ALT
                    return
                }

                "@>=" -> {
                    tokenEnd = tokenStart + 3
                    currentToken = PicatTokenTypes.TERM_GE
                    return
                }
            }
        }

        // Check for two-character operators
        if (tokenStart + 1 < bufferEnd) {
            val twoChars = buffer.substring(tokenStart, tokenStart + 2)
            when (twoChars) {
                "=>" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.ARROW_OP
                    return
                }

                "==" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.EQUAL
                    return
                }

                "!=" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.NOT_EQUAL
                    return
                }

                "<=" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.LESS_EQUAL
                    return
                }

                "=<" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.LESS_EQUAL_ALT
                    return
                }

                ">=" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.GREATER_EQUAL
                    return
                }

                "&&" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.AND
                    return
                }

                "||" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.OR
                    return
                }

                "**" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.POWER_OP
                    return
                }

                "//" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.INT_DIVIDE
                    return
                }

                "/<" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.DIVIDE_LT
                    return
                }

                "/>" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.DIVIDE_GT
                    return
                }

                ":=" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.ASSIGN_ONCE
                    return
                }

                "::" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.TYPE_CONSTRAINT
                    return
                }

                ".." -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.RANGE
                    return
                }

                "++" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONCAT
                    return
                }

                "<<" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.SHIFT_LEFT
                    return
                }

                ">>" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.SHIFT_RIGHT
                    return
                }

                "/\\" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.BITWISE_AND
                    return
                }

                "\\/" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.BITWISE_OR
                    return
                }

                "#=" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONSTRAINT_EQ
                    return
                }

                "#!" -> {
                    // Skip this case, as "#!=" is handled by three-character operators
                    // and "#!" by itself is not a valid operator
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.BAD_CHARACTER
                    return
                }

                "#<" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONSTRAINT_LT
                    return
                }

                "#>" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONSTRAINT_GT
                    return
                }

                "#~" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONSTRAINT_NOT
                    return
                }

                "#^" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.CONSTRAINT_XOR
                    return
                }

                "@<" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.TERM_LT
                    return
                }

                "@>" -> {
                    tokenEnd = tokenStart + 2
                    currentToken = PicatTokenTypes.TERM_GT
                    return
                }
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

    private fun skipQuotedAtom(start: Int): Int {
        var i = start + 1
        while (i < bufferEnd) {
            if (buffer[i] == '\'' && buffer[i - 1] != '\\') {
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
            } else if (buffer[i] == '_') {
                // Allow underscores in numbers for readability (e.g., 1_000_000)
                i++
            } else if (i + 1 < bufferEnd && (buffer[i] == 'e' || buffer[i] == 'E') &&
                (isDigit(buffer[i + 1]) || buffer[i + 1] == '+' || buffer[i + 1] == '-')
            ) {
                // Handle exponent notation (e.g., 1.0e10, 1.0e+10, 1.0e-10)
                i++
                if (buffer[i] == '+' || buffer[i] == '-') {
                    i++
                }
                // Skip the digits in the exponent
                while (i < bufferEnd && isDigit(buffer[i])) {
                    i++
                }
                break
            } else {
                break
            }
        }

        return i
    }

    private fun skipHexNumber(start: Int): Int {
        // Skip the '0x' or '0X' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (isHexDigit(buffer[i])) {
                i++
            } else if (buffer[i] == '_') {
                // Allow underscores in numbers for readability
                i++
            } else {
                break
            }
        }

        return i
    }

    private fun skipOctalNumber(start: Int): Int {
        // Skip the '0o' or '0O' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (isOctalDigit(buffer[i])) {
                i++
            } else if (buffer[i] == '_') {
                // Allow underscores in numbers for readability
                i++
            } else {
                break
            }
        }

        return i
    }

    private fun skipBinaryNumber(start: Int): Int {
        // Skip the '0b' or '0B' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (isBinaryDigit(buffer[i])) {
                i++
            } else if (buffer[i] == '_') {
                // Allow underscores in numbers for readability
                i++
            } else {
                break
            }
        }

        return i
    }

    private fun isHexDigit(c: Char): Boolean = c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'
    private fun isOctalDigit(c: Char): Boolean = c in '0'..'7'
    private fun isBinaryDigit(c: Char): Boolean = c == '0' || c == '1'

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
