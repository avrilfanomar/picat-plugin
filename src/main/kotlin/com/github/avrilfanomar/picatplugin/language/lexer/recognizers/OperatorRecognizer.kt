package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for operators and separators in the Picat lexer.
 */
class OperatorRecognizer : TokenRecognizer {
    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        // Check if the character is an operator or separator
        val c = buffer[startOffset]
        return c in "=+-*/<>!|@$^#~\\,.;:()[]{}?"
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        // Check for four-character operators
        if (startOffset + 3 < endOffset) {
            val fourChars = buffer.substring(startOffset, startOffset + 4)
            when (fourChars) {
                "#<=>" -> {
                    return Pair(PicatTokenTypes.CONSTRAINT_EQUIV, startOffset + 4)
                }
            }
        }

        // Check for three-character operators
        if (startOffset + 2 < endOffset) {
            val threeChars = buffer.substring(startOffset, startOffset + 3)
            when (threeChars) {
                "?=>" -> return Pair(PicatTokenTypes.BACKTRACKABLE_ARROW_OP, startOffset + 3)
                "!==" -> return Pair(PicatTokenTypes.NOT_IDENTICAL, startOffset + 3)
                "===" -> return Pair(PicatTokenTypes.IDENTICAL, startOffset + 3)
                "#=>" -> return Pair(PicatTokenTypes.CONSTRAINT_IMPL, startOffset + 3)
                "#/\\" -> return Pair(PicatTokenTypes.CONSTRAINT_AND, startOffset + 3)
                "#\\/" -> return Pair(PicatTokenTypes.CONSTRAINT_OR, startOffset + 3)
                "#<=" -> return Pair(PicatTokenTypes.CONSTRAINT_LE_ALT, startOffset + 3)
                "#!=" -> return Pair(PicatTokenTypes.CONSTRAINT_NEQ, startOffset + 3)
                "#>=" -> return Pair(PicatTokenTypes.CONSTRAINT_GE, startOffset + 3)
                "#=<" -> return Pair(PicatTokenTypes.CONSTRAINT_LE, startOffset + 3)
                "@=<" -> return Pair(PicatTokenTypes.TERM_LE, startOffset + 3)
                "@<=" -> return Pair(PicatTokenTypes.TERM_LE_ALT, startOffset + 3)
                "@>=" -> return Pair(PicatTokenTypes.TERM_GE, startOffset + 3)
            }
        }

        // Check for two-character operators
        if (startOffset + 1 < endOffset) {
            val twoChars = buffer.substring(startOffset, startOffset + 2)
            when (twoChars) {
                "=>" -> return Pair(PicatTokenTypes.ARROW_OP, startOffset + 2)
                "==" -> return Pair(PicatTokenTypes.EQUAL, startOffset + 2)
                "!=" -> return Pair(PicatTokenTypes.NOT_EQUAL, startOffset + 2)
                "<=" -> return Pair(PicatTokenTypes.LESS_EQUAL, startOffset + 2)
                "=<" -> return Pair(PicatTokenTypes.LESS_EQUAL_ALT, startOffset + 2)
                ">=" -> return Pair(PicatTokenTypes.GREATER_EQUAL, startOffset + 2)
                "&&" -> return Pair(PicatTokenTypes.AND, startOffset + 2)
                "||" -> return Pair(PicatTokenTypes.OR, startOffset + 2)
                "**" -> return Pair(PicatTokenTypes.POWER_OP, startOffset + 2)
                "//" -> return Pair(PicatTokenTypes.INT_DIVIDE, startOffset + 2)
                "/<" -> return Pair(PicatTokenTypes.DIVIDE_LT, startOffset + 2)
                "/>" -> return Pair(PicatTokenTypes.DIVIDE_GT, startOffset + 2)
                ":=" -> return Pair(PicatTokenTypes.ASSIGN_ONCE, startOffset + 2)
                "::" -> return Pair(PicatTokenTypes.TYPE_CONSTRAINT, startOffset + 2)
                ".." -> return Pair(PicatTokenTypes.RANGE, startOffset + 2)
                "++" -> return Pair(PicatTokenTypes.CONCAT, startOffset + 2)
                "<<" -> return Pair(PicatTokenTypes.SHIFT_LEFT, startOffset + 2)
                ">>" -> return Pair(PicatTokenTypes.SHIFT_RIGHT, startOffset + 2)
                "/\\" -> return Pair(PicatTokenTypes.BITWISE_AND, startOffset + 2)
                "\\/" -> return Pair(PicatTokenTypes.BITWISE_OR, startOffset + 2)
                "#=" -> return Pair(PicatTokenTypes.CONSTRAINT_EQ, startOffset + 2)
                "#!" -> return Pair(PicatTokenTypes.BAD_CHARACTER, startOffset + 2) // Not a valid operator by itself
                "#<" -> return Pair(PicatTokenTypes.CONSTRAINT_LT, startOffset + 2)
                "#>" -> return Pair(PicatTokenTypes.CONSTRAINT_GT, startOffset + 2)
                "#~" -> return Pair(PicatTokenTypes.CONSTRAINT_NOT, startOffset + 2)
                "#^" -> return Pair(PicatTokenTypes.CONSTRAINT_XOR, startOffset + 2)
                "@<" -> return Pair(PicatTokenTypes.TERM_LT, startOffset + 2)
                "@>" -> return Pair(PicatTokenTypes.TERM_GT, startOffset + 2)
            }
        }

        // Single character operators and separators
        val tokenType = when (buffer[startOffset]) {
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
            '?' -> PicatTokenTypes.QUESTION
            else -> PicatTokenTypes.BAD_CHARACTER
        }

        return Pair(tokenType, startOffset + 1)
    }
}
