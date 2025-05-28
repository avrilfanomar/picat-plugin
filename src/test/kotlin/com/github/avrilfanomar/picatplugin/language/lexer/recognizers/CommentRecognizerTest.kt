package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.Assert.*
import org.junit.Test

/**
 * Test for the CommentRecognizer class.
 * This test verifies that the recognizer correctly identifies line comments and multi-line comments.
 */
class CommentRecognizerTest {
    private val recognizer = CommentRecognizer()

    @Test
    fun testCanRecognizeLineComment() {
        // Test with line comments (%)
        assertTrue(recognizer.canRecognize("%", 0, 1))
        assertTrue(recognizer.canRecognize("% This is a comment", 0, 19))
        assertTrue(recognizer.canRecognize("code % comment", 5, 14))
    }

    @Test
    fun testCanRecognizeMultiLineComment() {
        // Test with multi-line comments (/* ... */)
        assertTrue(recognizer.canRecognize("/*", 0, 2))
        assertTrue(recognizer.canRecognize("/* This is a comment */", 0, 23))
        assertTrue(recognizer.canRecognize("code /* comment */ code", 5, 20))
    }

    @Test
    fun testCannotRecognizeNonComment() {
        // Test with non-comment characters
        assertFalse(recognizer.canRecognize("a", 0, 1))
        assertFalse(recognizer.canRecognize("/", 0, 1)) // Just a slash
        assertFalse(recognizer.canRecognize("/a", 0, 2)) // Slash followed by non-asterisk
        assertFalse(recognizer.canRecognize("", 0, 0)) // Empty string
    }

    @Test
    fun testRecognizeLineComment() {
        // Test recognizing a line comment
        val text = "% This is a line comment\nNext line"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.COMMENT, tokenType)
        assertEquals(25, endPos) // Should end at the newline
    }

    @Test
    fun testRecognizeLineCommentAtEndOfInput() {
        // Test recognizing a line comment at the end of input (no newline)
        val text = "% This is a line comment"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.COMMENT, tokenType)
        assertEquals(text.length, endPos) // Should end at the end of input
    }

    @Test
    fun testRecognizeMultiLineComment() {
        // Test recognizing a multi-line comment
        val text = "/* This is a\nmulti-line\ncomment */"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.COMMENT, tokenType)
        assertEquals(text.length, endPos) // Should end after */
    }

    @Test
    fun testRecognizeMultiLineCommentWithNestedCommentSyntax() {
        // Test recognizing a multi-line comment with nested comment syntax (which isn't actually nested)
        val text = "/* Outer /* Not actually nested */ still in comment */"
        val (tokenType, endPos) = recognizer.recognize(text, 0, text.length)
        assertEquals(PicatTokenTypes.COMMENT, tokenType)
        assertEquals(text.length, endPos) // Should end after the last */
    }

    @Test
    fun testRecognizeCommentWithOffset() {
        // Test recognizing a comment with an offset
        val text = "code % comment"
        val (tokenType, endPos) = recognizer.recognize(text, 5, text.length)
        assertEquals(PicatTokenTypes.COMMENT, tokenType)
        assertEquals(text.length, endPos) // Should end at the end of input
    }
}