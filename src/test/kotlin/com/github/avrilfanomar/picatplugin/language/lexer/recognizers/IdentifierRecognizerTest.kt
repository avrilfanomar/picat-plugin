package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

/**
 * Test for the IdentifierRecognizer class.
 * This test verifies that the recognizer correctly identifies variables, identifiers, and keywords.
 */
class IdentifierRecognizerTest {
    private val recognizer = IdentifierRecognizer()

    @Test
    fun testCanRecognizeVariable() {
        // Variables start with uppercase letters or underscore
        assertTrue(recognizer.canRecognize("X", 0, 1))
        assertTrue(recognizer.canRecognize("Var", 0, 3))
        assertTrue(recognizer.canRecognize("_var", 0, 4))
        assertTrue(recognizer.canRecognize("_", 0, 1))
    }

    @Test
    fun testCanRecognizeIdentifier() {
        // Identifiers start with lowercase letters
        assertTrue(recognizer.canRecognize("abc", 0, 3))
        assertTrue(recognizer.canRecognize("factorial", 0, 9))
        assertTrue(recognizer.canRecognize("a1", 0, 2))
    }

    @Test
    fun testCannotRecognizeNonIdentifier() {
        // Should not recognize numbers, operators, etc.
        assertFalse(recognizer.canRecognize("123", 0, 3))
        assertFalse(recognizer.canRecognize("+", 0, 1))
        assertFalse(recognizer.canRecognize("=", 0, 1))
        assertFalse(recognizer.canRecognize("", 0, 0))
    }

    @Test
    fun testRecognizeVariable() {
        // Test recognizing variables
        val (tokenType1, endPos1) = recognizer.recognize("X", 0, 1)
        assertEquals(PicatTokenTypes.VARIABLE, tokenType1)
        assertEquals(1, endPos1)

        val (tokenType2, endPos2) = recognizer.recognize("Var123", 0, 6)
        assertEquals(PicatTokenTypes.VARIABLE, tokenType2)
        assertEquals(6, endPos2)

        val (tokenType3, endPos3) = recognizer.recognize("_var", 0, 4)
        assertEquals(PicatTokenTypes.VARIABLE, tokenType3)
        assertEquals(4, endPos3)
    }

    @Test
    fun testRecognizeKeywords() {
        // Test recognizing various keywords
        val keywordTests = listOf(
            "import" to PicatTokenTypes.IMPORT_KEYWORD,
            "export" to PicatTokenTypes.EXPORT_KEYWORD,
            "include" to PicatTokenTypes.INCLUDE_KEYWORD,
            "module" to PicatTokenTypes.MODULE_KEYWORD,
            "if" to PicatTokenTypes.IF_KEYWORD,
            "then" to PicatTokenTypes.THEN_KEYWORD,
            "else" to PicatTokenTypes.ELSE_KEYWORD,
            "true" to PicatTokenTypes.TRUE_KEYWORD,
            "false" to PicatTokenTypes.FALSE_KEYWORD
        )

        for ((keyword, expectedType) in keywordTests) {
            val (tokenType, endPos) = recognizer.recognize(keyword, 0, keyword.length)
            assertEquals("Keyword '$keyword' should be recognized as ${expectedType.toString()}", 
                expectedType, tokenType)
            assertEquals(keyword.length, endPos)
        }
    }

    @Test
    fun testRecognizeBasicModuleFunctions() {
        // Test recognizing functions from the basic module
        val basicFunctions = listOf("length", "member", "sort", "sum", "append")

        for (func in basicFunctions) {
            val (tokenType, endPos) = recognizer.recognize(func, 0, func.length)
            assertEquals("Function '$func' should be recognized as BASIC_MODULE_FUNCTION", 
                PicatTokenTypes.BASIC_MODULE_FUNCTION, tokenType)
            assertEquals(func.length, endPos)
        }
    }

    @Test
    fun testRecognizeRegularIdentifiers() {
        // Test recognizing regular identifiers
        val identifiers = listOf("fibonacci", "myFunc", "custom_predicate", "factorial")

        for (id in identifiers) {
            val (tokenType, endPos) = recognizer.recognize(id, 0, id.length)
            assertEquals("Identifier '$id' should be recognized as IDENTIFIER", 
                PicatTokenTypes.IDENTIFIER, tokenType)
            assertEquals(id.length, endPos)
        }
    }

    @Test
    fun testRecognizeWithOffset() {
        // Test recognizing tokens with an offset
        val text = "abc_Xyz"
        val (tokenType, endPos) = recognizer.recognize(text, 4, text.length)
        assertEquals(PicatTokenTypes.VARIABLE, tokenType)
        assertEquals(7, endPos)
    }
}
