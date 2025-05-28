package com.github.avrilfanomar.picatplugin.language.lexer

/**
 * Utility class for character classification in the Picat lexer.
 * Provides methods to check character types.
 */
object CharacterClassifier {
    /**
     * Checks if the character is a whitespace.
     */
    fun isWhitespace(c: Char): Boolean = c.isWhitespace()

    /**
     * Checks if the character is a letter.
     */
    fun isLetter(c: Char): Boolean = c.isLetter()

    /**
     * Checks if the character is a digit.
     */
    fun isDigit(c: Char): Boolean = c.isDigit()

    /**
     * Checks if the character is an uppercase letter.
     */
    fun isUpperCase(c: Char): Boolean = c.isUpperCase()

    /**
     * Checks if the character is a hexadecimal digit.
     */
    fun isHexDigit(c: Char): Boolean = c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'

    /**
     * Checks if the character is an octal digit.
     */
    fun isOctalDigit(c: Char): Boolean = c in '0'..'7'

    /**
     * Checks if the character is a binary digit.
     */
    fun isBinaryDigit(c: Char): Boolean = c == '0' || c == '1'
}