package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for operators and separators in the Picat lexer.
 */
class OperatorRecognizer : TokenRecognizer {
    companion object {
        // Maps for operators of different lengths
        private val THREE_CHAR_OPERATORS = mapOf(
            "?=>" to PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
            "!==" to PicatTokenTypes.NOT_IDENTICAL,
            "===" to PicatTokenTypes.IDENTICAL,
            "#=>" to PicatTokenTypes.CONSTRAINT_IMPL,
            "#/\\" to PicatTokenTypes.CONSTRAINT_AND,
            "#\\/" to PicatTokenTypes.CONSTRAINT_OR,
            "#<=" to PicatTokenTypes.CONSTRAINT_LE_ALT,
            "#!=" to PicatTokenTypes.CONSTRAINT_NEQ,
            "#>=" to PicatTokenTypes.CONSTRAINT_GE,
            "#=<" to PicatTokenTypes.CONSTRAINT_LE,
            "@=<" to PicatTokenTypes.TERM_LE,
            "@<=" to PicatTokenTypes.TERM_LE_ALT,
            "@>=" to PicatTokenTypes.TERM_GE
        )

        private val TWO_CHAR_OPERATORS = mapOf(
            "=>" to PicatTokenTypes.ARROW_OP,
            "==" to PicatTokenTypes.EQUAL,
            "!=" to PicatTokenTypes.NOT_EQUAL,
            "<=" to PicatTokenTypes.LESS_EQUAL,
            "=<" to PicatTokenTypes.LESS_EQUAL_ALT,
            ">=" to PicatTokenTypes.GREATER_EQUAL,
            "&&" to PicatTokenTypes.AND,
            "||" to PicatTokenTypes.OR,
            "**" to PicatTokenTypes.POWER_OP,
            "//" to PicatTokenTypes.INT_DIVIDE,
            "/<" to PicatTokenTypes.DIVIDE_LT,
            "/>" to PicatTokenTypes.DIVIDE_GT,
            ":=" to PicatTokenTypes.ASSIGN_ONCE,
            "::" to PicatTokenTypes.TYPE_CONSTRAINT,
            ".." to PicatTokenTypes.RANGE,
            "++" to PicatTokenTypes.CONCAT,
            "<<" to PicatTokenTypes.SHIFT_LEFT,
            ">>" to PicatTokenTypes.SHIFT_RIGHT,
            "/\\" to PicatTokenTypes.BITWISE_AND,
            "\\/" to PicatTokenTypes.BITWISE_OR,
            "#=" to PicatTokenTypes.CONSTRAINT_EQ,
            "#!" to PicatTokenTypes.BAD_CHARACTER, // Not a valid operator by itself
            "#<" to PicatTokenTypes.CONSTRAINT_LT,
            "#>" to PicatTokenTypes.CONSTRAINT_GT,
            "#~" to PicatTokenTypes.CONSTRAINT_NOT,
            "#^" to PicatTokenTypes.CONSTRAINT_XOR,
            "@<" to PicatTokenTypes.TERM_LT,
            "@>" to PicatTokenTypes.TERM_GT
        )

        private val SINGLE_CHAR_OPERATORS = mapOf(
            '=' to PicatTokenTypes.ASSIGN_OP,
            '+' to PicatTokenTypes.PLUS,
            '-' to PicatTokenTypes.MINUS,
            '*' to PicatTokenTypes.MULTIPLY,
            '/' to PicatTokenTypes.DIVIDE,
            '<' to PicatTokenTypes.LESS,
            '>' to PicatTokenTypes.GREATER,
            '!' to PicatTokenTypes.NOT,
            '|' to PicatTokenTypes.PIPE,
            '@' to PicatTokenTypes.AS_PATTERN,
            '$' to PicatTokenTypes.DATA_CONSTRUCTOR,
            '^' to PicatTokenTypes.POWER,
            '#' to PicatTokenTypes.HASH,
            '~' to PicatTokenTypes.TILDE,
            '\\' to PicatTokenTypes.BACKSLASH,
            ',' to PicatTokenTypes.COMMA,
            '.' to PicatTokenTypes.DOT,
            ';' to PicatTokenTypes.SEMICOLON,
            ':' to PicatTokenTypes.COLON,
            '(' to PicatTokenTypes.LPAR,
            ')' to PicatTokenTypes.RPAR,
            '[' to PicatTokenTypes.LBRACKET,
            ']' to PicatTokenTypes.RBRACKET,
            '{' to PicatTokenTypes.LBRACE,
            '}' to PicatTokenTypes.RBRACE,
            '?' to PicatTokenTypes.QUESTION
        )

        private val FOUR_CHAR_OPERATORS = mapOf(
            "#<=>" to PicatTokenTypes.CONSTRAINT_EQUIV
        )
    }
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        // Check if the character is an operator or separator
        val c = buffer[startOffset]
        return c in "=+-*/<>!|@$^#~\\,.;:()[]{}?"
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        // Try to recognize operators in order of length (longest first)
        val fourCharResult = recognizeFourCharOperator(buffer, startOffset, endOffset)
        val threeCharResult = if (fourCharResult == null) 
            recognizeThreeCharOperator(buffer, startOffset, endOffset) 
        else 
            null
        val twoCharResult = if (threeCharResult == null) 
            recognizeTwoCharOperator(buffer, startOffset, endOffset) 
        else 
            null

        // Return the first non-null result, or fall back to single character
        return fourCharResult ?: threeCharResult ?: twoCharResult ?: recognizeSingleCharOperator(buffer, startOffset)
    }

    /**
     * Recognizes four-character operators.
     */
    private fun recognizeFourCharOperator(
        buffer: CharSequence, 
        startOffset: Int, 
        endOffset: Int
    ): Pair<IElementType, Int>? {
        if (startOffset + 3 < endOffset) {
            val fourChars = buffer.substring(startOffset, startOffset + 4)
            FOUR_CHAR_OPERATORS[fourChars]?.let {
                return Pair(it, startOffset + 4)
            }
        }
        return null
    }

    /**
     * Recognizes three-character operators.
     */
    private fun recognizeThreeCharOperator(
        buffer: CharSequence, 
        startOffset: Int, 
        endOffset: Int
    ): Pair<IElementType, Int>? {
        if (startOffset + 2 < endOffset) {
            val threeChars = buffer.substring(startOffset, startOffset + 3)
            THREE_CHAR_OPERATORS[threeChars]?.let {
                return Pair(it, startOffset + 3)
            }
        }
        return null
    }

    /**
     * Recognizes two-character operators.
     */
    private fun recognizeTwoCharOperator(
        buffer: CharSequence, 
        startOffset: Int, 
        endOffset: Int
    ): Pair<IElementType, Int>? {
        if (startOffset + 1 < endOffset) {
            val twoChars = buffer.substring(startOffset, startOffset + 2)
            TWO_CHAR_OPERATORS[twoChars]?.let {
                return Pair(it, startOffset + 2)
            }
        }
        return null
    }

    /**
     * Recognizes single-character operators and separators.
     */
    private fun recognizeSingleCharOperator(buffer: CharSequence, startOffset: Int): Pair<IElementType, Int> {
        val c = buffer[startOffset]
        val tokenType = SINGLE_CHAR_OPERATORS[c] ?: PicatTokenTypes.BAD_CHARACTER
        return Pair(tokenType, startOffset + 1)
    }
}
