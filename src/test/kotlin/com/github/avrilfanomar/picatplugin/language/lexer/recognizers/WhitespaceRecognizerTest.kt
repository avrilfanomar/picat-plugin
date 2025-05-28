package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.Assert.*
import org.junit.Test

/**
 * Test for the WhitespaceRecognizer class.
 * This test verifies that the recognizer correctly identifies whitespace tokens.
 */
class WhitespaceRecognizerTest {
    private val recognizer = WhitespaceRecognizer()

    @Test
    fun testCanRecognizeWhitespace() {
        // Test with various whitespace characters
        assertTrue(recognizer.canRecognize(" ", 0, 1))
        assertTrue(recognizer.canRecognize("\t", 0, 1))
        assertTrue(recognizer.canRecognize("\n", 0, 1))
        assertTrue(recognizer.canRecognize("\r", 0, 1))
        assertTrue(recognizer.canRecognize(" \t\n\r", 0, 4))
    }

    @Test
    fun testCannotRecognizeNonWhitespace() {
        // Test with non-whitespace characters
        assertFalse(recognizer.canRecognize("a", 0, 1))
        assertFalse(recognizer.canRecognize("1", 0, 1))
        assertFalse(recognizer.canRecognize("_", 0, 1))
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeSingleWhitespace() {
        // Test recognizing a single whitespace character
        val (tokenType, endPos) = recognizer.recognize(" ", 0, 1)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokenType)
        assertEquals(1, endPos)
    }

    @Test
    fun testRecognizeMultipleWhitespace() {
        // Test recognizing multiple consecutive whitespace characters
        val (tokenType, endPos) = recognizer.recognize("   \t\n", 0, 5)
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