package com.github.avrilfanomar.picatplugin.language.lexer

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType
import org.junit.Assert.*
import org.junit.Test

/**
 * Test for the PicatLexer class.
 * This test verifies that the lexer correctly tokenizes Picat code.
 */
class PicatLexerTest {

    /**
     * Helper function to tokenize a string and return the list of tokens.
     */
    private fun tokenize(text: String): List<Pair<IElementType, String>> {
        val lexer = PicatLexer()
        lexer.start(text)

        val tokens = mutableListOf<Pair<IElementType, String>>()
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType!!
            val tokenText = text.substring(lexer.tokenStart, lexer.tokenEnd)
            tokens.add(Pair(tokenType, tokenText))
            lexer.advance()
        }

        return tokens
    }

    @Test
    fun testEmptyInput() {
        val tokens = tokenize("")
        assertTrue("Empty input should produce no tokens", tokens.isEmpty())
    }

    @Test
    fun testWhitespace() {
        val tokens = tokenize("  \t\n")
        assertEquals("Should have one whitespace token", 1, tokens.size)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[0].first)
        assertEquals("  \t\n", tokens[0].second)
    }

    @Test
    fun testIdentifiers() {
        val tokens = tokenize("factorial fibonacci")
        assertEquals("Should have 3 tokens (2 identifiers and 1 whitespace)", 3, tokens.size)
        assertEquals(PicatTokenTypes.IDENTIFIER, tokens[0].first)
        assertEquals("factorial", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.IDENTIFIER, tokens[2].first)
        assertEquals("fibonacci", tokens[2].second)
    }

    @Test
    fun testVariables() {
        val tokens = tokenize("X Y _var")
        assertEquals("Should have 5 tokens (3 variables and 2 whitespaces)", 5, tokens.size)
        assertEquals(PicatTokenTypes.VARIABLE, tokens[0].first)
        assertEquals("X", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.VARIABLE, tokens[2].first)
        assertEquals("Y", tokens[2].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[3].first)
        assertEquals(" ", tokens[3].second)
        assertEquals(PicatTokenTypes.VARIABLE, tokens[4].first)
        assertEquals("_var", tokens[4].second)
    }

    @Test
    fun testKeywords() {
        val tokens = tokenize("import module if then else")
        assertEquals("Should have 9 tokens (5 keywords and 4 whitespaces)", 9, tokens.size)
        assertEquals(PicatTokenTypes.IMPORT_KEYWORD, tokens[0].first)
        assertEquals("import", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.MODULE_KEYWORD, tokens[2].first)
        assertEquals("module", tokens[2].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[3].first)
        assertEquals(" ", tokens[3].second)
        assertEquals(PicatTokenTypes.IF_KEYWORD, tokens[4].first)
        assertEquals("if", tokens[4].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[5].first)
        assertEquals(" ", tokens[5].second)
        assertEquals(PicatTokenTypes.THEN_KEYWORD, tokens[6].first)
        assertEquals("then", tokens[6].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[7].first)
        assertEquals(" ", tokens[7].second)
        assertEquals(PicatTokenTypes.ELSE_KEYWORD, tokens[8].first)
        assertEquals("else", tokens[8].second)
    }

    @Test
    fun testNumbers() {
        val tokens = tokenize("123 3.14 0xFF 0b101 0o777")
        assertEquals("Should have 9 tokens (5 numbers and 4 whitespaces)", 9, tokens.size)
        assertEquals(PicatTokenTypes.INTEGER, tokens[0].first)
        assertEquals("123", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.FLOAT, tokens[2].first)
        assertEquals("3.14", tokens[2].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[3].first)
        assertEquals(" ", tokens[3].second)
        assertEquals(PicatTokenTypes.HEX_INTEGER, tokens[4].first)
        assertEquals("0xFF", tokens[4].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[5].first)
        assertEquals(" ", tokens[5].second)
        assertEquals(PicatTokenTypes.BINARY_INTEGER, tokens[6].first)
        assertEquals("0b101", tokens[6].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[7].first)
        assertEquals(" ", tokens[7].second)
        assertEquals(PicatTokenTypes.OCTAL_INTEGER, tokens[8].first)
        assertEquals("0o777", tokens[8].second)
    }

    @Test
    fun testStrings() {
        val tokens = tokenize("\"hello\" 'atom'")
        assertEquals("Should have 3 tokens (2 strings and 1 whitespace)", 3, tokens.size)
        assertEquals(PicatTokenTypes.STRING, tokens[0].first)
        assertEquals("\"hello\"", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.QUOTED_ATOM, tokens[2].first)
        assertEquals("'atom'", tokens[2].second)
    }

    @Test
    fun testOperators() {
        val tokens = tokenize("+ - * / =")
        assertEquals("Should have 9 tokens (5 operators and 4 whitespaces)", 9, tokens.size)
        assertEquals(PicatTokenTypes.PLUS, tokens[0].first)
        assertEquals("+", tokens[0].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)
        assertEquals(" ", tokens[1].second)
        assertEquals(PicatTokenTypes.MINUS, tokens[2].first)
        assertEquals("-", tokens[2].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[3].first)
        assertEquals(" ", tokens[3].second)
        assertEquals(PicatTokenTypes.MULTIPLY, tokens[4].first)
        assertEquals("*", tokens[4].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[5].first)
        assertEquals(" ", tokens[5].second)
        assertEquals(PicatTokenTypes.DIVIDE, tokens[6].first)
        assertEquals("/", tokens[6].second)
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[7].first)
        assertEquals(" ", tokens[7].second)
        assertEquals(PicatTokenTypes.ASSIGN_OP, tokens[8].first)
        assertEquals("=", tokens[8].second)
    }

    @Test
    fun testCompoundOperators() {
        // Test compound operators like ==, !=, =>, ?=>
        val tokens = tokenize("== != => ?=>")

        // Verify that the compound operators are tokenized correctly
        // == should be tokenized as EQUAL
        assertEquals(PicatTokenTypes.EQUAL, tokens[0].first)
        assertEquals("==", tokens[0].second)

        // Space
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[1].first)

        // != should be tokenized as NOT_EQUAL
        assertEquals(PicatTokenTypes.NOT_EQUAL, tokens[2].first)
        assertEquals("!=", tokens[2].second)

        // Space
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[3].first)

        // => should be tokenized as ARROW_OP
        assertEquals(PicatTokenTypes.ARROW_OP, tokens[4].first)
        assertEquals("=>", tokens[4].second)

        // Space
        assertEquals(PicatTokenTypes.WHITE_SPACE, tokens[5].first)

        // ?=> should be tokenized as BACKTRACKABLE_ARROW_OP
        assertEquals(PicatTokenTypes.BACKTRACKABLE_ARROW_OP, tokens[6].first)
        assertEquals("?=>", tokens[6].second)
    }

    @Test
    fun testComments() {
        val tokens = tokenize("% This is a comment\ncode /* Multi-line\ncomment */ code")
        assertTrue("Should tokenize comments correctly", tokens.size > 0)
        assertEquals(PicatTokenTypes.COMMENT, tokens[0].first)
        assertEquals("% This is a comment", tokens[0].second)
    }

    @Test
    fun testComplexCode() {
        val code = """
            % Factorial function in Picat
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            main =>
                X = 5,
                F = factorial(X),
                println("Factorial of " ++ X ++ " is " ++ F).
        """.trimIndent()

        val tokens = tokenize(code)
        assertTrue("Should tokenize complex code correctly", tokens.size > 0)

        // Verify some key tokens
        val tokenTypes = tokens.map { it.first }
        assertTrue("Should contain COMMENT token", tokenTypes.contains(PicatTokenTypes.COMMENT))
        assertTrue("Should contain IDENTIFIER tokens", tokenTypes.contains(PicatTokenTypes.IDENTIFIER))
        assertTrue("Should contain INTEGER tokens", tokenTypes.contains(PicatTokenTypes.INTEGER))
        assertTrue("Should contain VARIABLE tokens", tokenTypes.contains(PicatTokenTypes.VARIABLE))
        assertTrue("Should contain STRING tokens", tokenTypes.contains(PicatTokenTypes.STRING))
        assertTrue("Should contain operator tokens", 
            tokenTypes.contains(PicatTokenTypes.ASSIGN_OP) || 
            tokenTypes.contains(PicatTokenTypes.MULTIPLY) ||
            tokenTypes.contains(PicatTokenTypes.ARROW_OP))
    }
}
