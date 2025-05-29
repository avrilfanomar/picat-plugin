package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

/**
 * Test for the OperatorRecognizer class.
 * This test verifies that the recognizer correctly identifies operators and separators.
 */
class OperatorRecognizerTest {
    private val recognizer = OperatorRecognizer()

    @Test
    fun testCanRecognizeOperators() {
        // Test with various operators
        val operators = "=+-*/<>!|@$^#~\\,.;:()[]{}?"
        for (c in operators) {
            assertTrue(
                recognizer.canRecognize(c.toString(), 0, 1),
                "Should recognize operator '$c'")
        }
    }

    @Test
    fun testCannotRecognizeNonOperators() {
        // Test with non-operators
        assertFalse(recognizer.canRecognize("a", 0, 1))
        assertFalse(recognizer.canRecognize("1", 0, 1))
        assertFalse(recognizer.canRecognize("_", 0, 1))
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeFourCharOperators() {
        // Test recognizing four-character operators
        val text = "#<=>"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.CONSTRAINT_EQUIV, tokenType)
        assertEquals(4, endPos)
    }

    @Test
    fun testRecognizeThreeCharOperators() {
        // Test recognizing three-character operators
        val operatorTests = mapOf(
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

        for ((op, expectedType) in operatorTests) {
            val (tokenType, endPos) = recognizer.recognize(op, 0, op.length)
            assertEquals(
                expectedType, tokenType,
                "Operator '$op' should be recognized as ${expectedType.toString()}")
            assertEquals(3, endPos)
        }
    }

    @Test
    fun testRecognizeTwoCharOperators() {
        // Test recognizing two-character operators
        val operatorTests = mapOf(
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
            "#<" to PicatTokenTypes.CONSTRAINT_LT,
            "#>" to PicatTokenTypes.CONSTRAINT_GT,
            "#~" to PicatTokenTypes.CONSTRAINT_NOT,
            "#^" to PicatTokenTypes.CONSTRAINT_XOR,
            "@<" to PicatTokenTypes.TERM_LT,
            "@>" to PicatTokenTypes.TERM_GT
        )

        for ((op, expectedType) in operatorTests) {
            val (tokenType, endPos) = recognizer.recognize(op, 0, op.length)
            assertEquals(expectedType, tokenType,
                "Operator '$op' should be recognized as ${expectedType.toString()}")
            assertEquals(2, endPos)
        }
    }

    @Test
    fun testRecognizeSingleCharOperators() {
        // Test recognizing single-character operators
        val operatorTests = mapOf(
            "=" to PicatTokenTypes.ASSIGN_OP,
            "+" to PicatTokenTypes.PLUS,
            "-" to PicatTokenTypes.MINUS,
            "*" to PicatTokenTypes.MULTIPLY,
            "/" to PicatTokenTypes.DIVIDE,
            "<" to PicatTokenTypes.LESS,
            ">" to PicatTokenTypes.GREATER,
            "!" to PicatTokenTypes.NOT,
            "|" to PicatTokenTypes.PIPE,
            "@" to PicatTokenTypes.AS_PATTERN,
            "$" to PicatTokenTypes.DATA_CONSTRUCTOR,
            "^" to PicatTokenTypes.POWER,
            "#" to PicatTokenTypes.HASH,
            "~" to PicatTokenTypes.TILDE,
            "\\" to PicatTokenTypes.BACKSLASH,
            "," to PicatTokenTypes.COMMA,
            "." to PicatTokenTypes.DOT,
            ";" to PicatTokenTypes.SEMICOLON,
            ":" to PicatTokenTypes.COLON,
            "(" to PicatTokenTypes.LPAR,
            ")" to PicatTokenTypes.RPAR,
            "[" to PicatTokenTypes.LBRACKET,
            "]" to PicatTokenTypes.RBRACKET,
            "{" to PicatTokenTypes.LBRACE,
            "}" to PicatTokenTypes.RBRACE,
            "?" to PicatTokenTypes.QUESTION
        )

        for ((op, expectedType) in operatorTests) {
            val (tokenType, endPos) = recognizer.recognize(op, 0, op.length)
            assertEquals(expectedType, tokenType,
                "Operator '$op' should be recognized as ${expectedType.toString()}")
            assertEquals(1, endPos)
        }
    }

    @Test
    fun testOperatorPrecedence() {
        // Test that longer operators are recognized before shorter ones
        val text = "===" // Could be parsed as "==" followed by "="
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.IDENTICAL, tokenType)
        assertEquals(3, endPos)

        val text2 = "#<=>" // Could be parsed as "#<=" followed by ">"
        val (tokenType2, endPos2) = recognizer.recognize(text2, 0, text2.length)
        assertEquals(PicatTokenTypes.CONSTRAINT_EQUIV, tokenType2)
        assertEquals(4, endPos2)
    }

    @Test
    fun testRecognizeOperatorWithOffset() {
        // Test recognizing an operator with an offset
        val text = "abc=>"
        val (tokenType, endPos) = recognizer.recognize(text, 3, text.length)
        assertEquals(PicatTokenTypes.ARROW_OP, tokenType)
        assertEquals(5, endPos)
    }
}
