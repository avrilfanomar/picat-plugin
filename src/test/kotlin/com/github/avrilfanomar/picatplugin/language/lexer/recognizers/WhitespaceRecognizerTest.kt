package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Test for the WhitespaceRecognizer class.
 * This test verifies that the recognizer correctly identifies whitespace tokens.
 */
class WhitespaceRecognizerTest {
    private val recognizer = WhitespaceRecognizer()

    @Test
    fun testCanRecognizeWhitespace() {
        // Test with various whitespace characters
        assertTrue(recognizer.canRecognize(0, 1, " "))
        assertTrue(recognizer.canRecognize(0, 1, "\t"))
        assertTrue(recognizer.canRecognize(0, 1, "\n"))
        assertTrue(recognizer.canRecognize(0, 1, "\r"))
        assertTrue(recognizer.canRecognize(0, 4, " \t\n\r"))
    }

    @Test
    fun testCannotRecognizeNonWhitespace() {
        // Test with non-whitespace characters
        assertFalse(recognizer.canRecognize(0, 1, "a"))
        assertFalse(recognizer.canRecognize(0, 1, "1"))
        assertFalse(recognizer.canRecognize(0, 1, "_"))
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeSingleWhitespace() {
        // Test recognizing a single whitespace character
        val (tokenType, endPos) = recognizer.recognize(0, 1, " ")
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokenType)
        assertEquals(1, endPos)
    }

    @Test
    fun testRecognizeMultipleWhitespace() {
        // Test recognizing multiple consecutive whitespace characters
        val (tokenType, endPos) = recognizer.recognize(0, 5, "   \t\n")
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokenType)
        assertEquals(5, endPos)
    }

    @Test
    fun testRecognizeWhitespaceWithOffset() {
        // Test recognizing whitespace with an offset
        val text = "abc   \t"
        val (tokenType, endPos) = recognizer.recognize(text, 3, text.length)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokenType)
        assertEquals(7, endPos)
    }

    @Test
    fun testRecognizeWhitespaceFollowedByNonWhitespace() {
        // Test recognizing whitespace followed by non-whitespace
        val text = "  \tabc"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokenType)
        assertEquals(3, endPos) // Should stop at the 'a'
    }
}