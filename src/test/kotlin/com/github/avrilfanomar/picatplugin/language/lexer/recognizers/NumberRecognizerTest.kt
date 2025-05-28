package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Test for the NumberRecognizer class.
 * This test verifies that the recognizer correctly identifies various numeric literals.
 */
class NumberRecognizerTest {
    private val recognizer = NumberRecognizer()

    @Test
    fun testCanRecognizeDigit() {
        // Test with digits
        assertTrue(recognizer.canRecognize(0, 1, "0"))
        assertTrue(recognizer.canRecognize(0, 1, "1"))
        assertTrue(recognizer.canRecognize(0, 1, "9"))
        assertTrue(recognizer.canRecognize(0, 3, "123"))
    }

    @Test
    fun testCannotRecognizeNonDigit() {
        // Test with non-digits
        assertFalse(recognizer.canRecognize(0, 1, "a"))
        assertFalse(recognizer.canRecognize(0, 1, ".")) // Just a dot
        assertFalse(recognizer.canRecognize(0, 1, "x"))
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeInteger() {
        // Test recognizing integer numbers
        val text = "123"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.INTEGER, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeFloat() {
        // Test recognizing floating-point numbers
        val text = "123.456"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.FLOAT, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeHexNumber() {
        // Test recognizing hexadecimal numbers
        val text = "0x1A3F"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.HEX_INTEGER, tokenType)
        assertEquals(text.length, endPos)

        // Test with uppercase X
        val text2 = "0X1A3F"
        val (tokenType2, endPos2) = recognizer.recognize(text2, 0, text2.length)
        assertEquals(PicatTokenTypes.HEX_INTEGER, tokenType2)
        assertEquals(text2.length, endPos2)
    }

    @Test
    fun testRecognizeOctalNumber() {
        // Test recognizing octal numbers
        val text = "0o1234567"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.OCTAL_INTEGER, tokenType)
        assertEquals(text.length, endPos)

        // Test with uppercase O
        val text2 = "0O1234567"
        val (tokenType2, endPos2) = recognizer.recognize(text2, 0, text2.length)
        assertEquals(PicatTokenTypes.OCTAL_INTEGER, tokenType2)
        assertEquals(text2.length, endPos2)
    }

    @Test
    fun testRecognizeBinaryNumber() {
        // Test recognizing binary numbers
        val text = "0b101010"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.BINARY_INTEGER, tokenType)
        assertEquals(text.length, endPos)

        // Test with uppercase B
        val text2 = "0B101010"
        val (tokenType2, endPos2) = recognizer.recognize(text2, 0, text2.length)
        assertEquals(PicatTokenTypes.BINARY_INTEGER, tokenType2)
        assertEquals(text2.length, endPos2)
    }

    @Test
    fun testRecognizeNumberWithOffset() {
        // Test recognizing a number with an offset
        val text = "abc123.45"
        val (tokenType, endPos) = recognizer.recognize(text, 3, text.length)
        assertEquals(PicatTokenTypes.FLOAT, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeNumberFollowedByNonDigit() {
        // Test recognizing a number followed by a non-digit
        val text = "123abc"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.INTEGER, tokenType)
        assertEquals(3, endPos) // Should stop at 'a'
    }
}