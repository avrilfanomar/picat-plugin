package com.github.avrilfanomar.picatplugin.language.lexer

/**
 * Utility class for skipping tokens in the Picat lexer.
 * Provides methods to skip various token types.
 */
class TokenSkipper(private val buffer: CharSequence, private val bufferEnd: Int) {

    /**
     * Skips whitespace characters starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the whitespace.
     */
    fun skipWhitespace(start: Int): Int {
        var i = start
        while (i < bufferEnd && CharacterClassifier.isWhitespace(buffer[i])) {
            i++
        }
        return i
    }

    /**
     * Skips a line comment (% to end of line) starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the comment.
     */
    fun skipLineComment(start: Int): Int {
        var i = start
        while (i < bufferEnd && buffer[i] != '\n') {
            i++
        }
        return i
    }

    /**
     * Skips a multi-line comment (/* ... */) starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the comment.
     */
    fun skipMultiLineComment(start: Int): Int {
        var i = start + 2  // Skip the initial /*
        while (i + 1 < bufferEnd) {
            if (buffer[i] == '*' && buffer[i + 1] == '/') {
                return i + 2  // Skip the closing */
            }
            i++
        }
        return i
    }

    /**
     * Skips a string literal starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the string.
     */
    fun skipString(start: Int): Int {
        var i = start + 1
        while (i < bufferEnd) {
            if (buffer[i] == '"' && buffer[i - 1] != '\\') {
                return i + 1
            }
            i++
        }
        return i
    }

    /**
     * Skips a quoted atom starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the quoted atom.
     */
    fun skipQuotedAtom(start: Int): Int {
        var i = start + 1
        while (i < bufferEnd) {
            if (buffer[i] == '\'' && buffer[i - 1] != '\\') {
                return i + 1
            }
            i++
        }
        return i
    }

    /**
     * Skips a number (integer or float) starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the number.
     */
    fun skipNumber(start: Int): Int {
        var i = start
        var hasDecimalPoint = false

        while (i < bufferEnd) {
            if (buffer[i] == '.' && !hasDecimalPoint) {
                hasDecimalPoint = true
                i++
            } else if (CharacterClassifier.isDigit(buffer[i])) {
                i++
            } else if (buffer[i] == '_') {
                // Allow underscores in numbers for readability (e.g., 1_000_000)
                i++
            } else if (i + 1 < bufferEnd && (buffer[i] == 'e' || buffer[i] == 'E') &&
                (CharacterClassifier.isDigit(buffer[i + 1]) || buffer[i + 1] == '+' || buffer[i + 1] == '-')
            ) {
                // Handle exponent notation (e.g., 1.0e10, 1.0e+10, 1.0e-10)
                i++
                if (buffer[i] == '+' || buffer[i] == '-') {
                    i++
                }
                // Skip the digits in the exponent
                while (i < bufferEnd && CharacterClassifier.isDigit(buffer[i])) {
                    i++
                }
                break
            } else {
                break
            }
        }

        return i
    }

    /**
     * Skips a hexadecimal number starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the number.
     */
    fun skipHexNumber(start: Int): Int {
        // Skip the '0x' or '0X' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (CharacterClassifier.isHexDigit(buffer[i])) {
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

    /**
     * Skips an octal number starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the number.
     */
    fun skipOctalNumber(start: Int): Int {
        // Skip the '0o' or '0O' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (CharacterClassifier.isOctalDigit(buffer[i])) {
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

    /**
     * Skips a binary number starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the number.
     */
    fun skipBinaryNumber(start: Int): Int {
        // Skip the '0b' or '0B' prefix
        var i = start + 2

        while (i < bufferEnd) {
            if (CharacterClassifier.isBinaryDigit(buffer[i])) {
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

    /**
     * Skips an identifier starting from the given position.
     * @param start The starting position in the buffer.
     * @return The position after the identifier.
     */
    fun skipIdentifier(start: Int): Int {
        var i = start
        while (i < bufferEnd && (CharacterClassifier.isLetter(buffer[i]) || 
                                CharacterClassifier.isDigit(buffer[i]) || 
                                buffer[i] == '_')) {
            i++
        }
        return i
    }
}
