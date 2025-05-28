package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Test for the StringRecognizer class.
 * This test verifies that the recognizer correctly identifies string literals and quoted atoms.
 */
class StringRecognizerTest {
    private val recognizer = StringRecognizer()

    @Test
    fun testCanRecognizeStringLiteral() {
        // Test with double-quoted strings
        assertTrue(recognizer.canRecognize("\"", 0, 1))
        assertTrue(recognizer.canRecognize("\"hello\"", 0, 7))
        assertTrue(recognizer.canRecognize("code \"string\" code", 5, 13))
    }

    @Test
    fun testCanRecognizeQuotedAtom() {
        // Test with single-quoted atoms
        assertTrue(recognizer.canRecognize(0, 1, "'"))
        assertTrue(recognizer.canRecognize(0, 6, "'atom'"))
        assertTrue(recognizer.canRecognize(5, 11, "code 'atom' code"))
    }

    @Test
    fun testCannotRecognizeNonString() {
        // Test with non-string characters
        assertFalse(recognizer.canRecognize(0, 1, "a"))
        assertFalse(recognizer.canRecognize(0, 1, "1"))
        assertFalse(recognizer.canRecognize(0, 1, "_"))
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeSimpleStringLiteral() {
        // Test recognizing a simple string literal
        val text = "\"hello\""
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.STRING, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeStringLiteralWithEscapes() {
        // Test recognizing a string literal with escape sequences
        val text = "\"hello\\\"world\""
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.STRING, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeSimpleQuotedAtom() {
        // Test recognizing a simple quoted atom
        val text = "'atom'"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.QUOTED_ATOM, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeQuotedAtomWithEscapes() {
        // Test recognizing a quoted atom with escape sequences
        val text = "'atom\\'s'"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.QUOTED_ATOM, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeMultilineString() {
        // Test recognizing a multi-line string
        val text = "\"This is a\nmulti-line\nstring\""
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.STRING, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeStringWithOffset() {
        // Test recognizing a string with an offset
        val text = "code \"string\""
        val (tokenType, endPos) = recognizer.recognize(text, 5, text.length)
        assertEquals(PicatTokenTypes.STRING, tokenType)
        assertEquals(text.length, endPos)
    }

    @Test
    fun testRecognizeQuotedAtomWithOffset() {
        // Test recognizing a quoted atom with an offset
        val text = "code 'atom'"
        val (tokenType, endPos) = recognizer.recognize(text, 5, text.length)
        assertEquals(PicatTokenTypes.QUOTED_ATOM, tokenType)
        assertEquals(text.length, endPos)
    }
}