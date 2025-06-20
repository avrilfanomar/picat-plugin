package com.github.avrilfanomar.picatplugin.language.lexer

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.Reader

/**
 * Test for the PicatLexer class.
 * This test verifies that the lexer correctly tokenizes Picat code.
 */
class PicatLexerTest {

    /**
     * Helper function to tokenize a string and return the list of tokens.
     */
    private fun tokenize(text: String): List<Pair<IElementType, String>> {
        val lexer = FlexAdapter(_PicatLexer(null as Reader?))
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
        Assertions.assertTrue(tokens.isEmpty(), "Empty input should produce no tokens")
    }

    @Test
    fun testWhitespace() {
        val tokens = tokenize("  \t\n")
        Assertions.assertEquals(1, tokens.size, "Should have one whitespace token")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[0].first) // Changed here
        Assertions.assertEquals(tokens[0].second, "  \t\n")
    }

    @Test
    fun testIdentifiers() {
        val tokens = tokenize("factorial fibonacci")
        Assertions.assertEquals(3, tokens.size, "Should have 3 tokens (2 identifiers and 1 whitespace)")
        Assertions.assertEquals(PicatTokenTypes.IDENTIFIER, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "factorial")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.IDENTIFIER, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "fibonacci")
    }

    @Test
    fun testVariables() {
        val tokens = tokenize("X Y _var")
        Assertions.assertEquals(5, tokens.size, "Should have 5 tokens (3 variables and 2 whitespaces)")
        Assertions.assertEquals(PicatTokenTypes.VARIABLE, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "X")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.VARIABLE, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "Y")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first) // Changed here
        Assertions.assertEquals(tokens[3].second, " ")
        Assertions.assertEquals(PicatTokenTypes.VARIABLE, tokens[4].first)
        Assertions.assertEquals(tokens[4].second, "_var")
    }

    @Test
    fun testKeywords() {
        val tokens = tokenize("import module if then else")
        Assertions.assertEquals(9, tokens.size, "Should have 9 tokens (5 keywords and 4 whitespaces)")
        Assertions.assertEquals(PicatTokenTypes.IMPORT_KEYWORD, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "import")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.MODULE_KEYWORD, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "module")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first) // Changed here
        Assertions.assertEquals(tokens[3].second, " ")
        Assertions.assertEquals(PicatTokenTypes.IF_KEYWORD, tokens[4].first)
        Assertions.assertEquals(tokens[4].second, "if")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[5].first) // Changed here
        Assertions.assertEquals(tokens[5].second, " ")
        Assertions.assertEquals(PicatTokenTypes.THEN_KEYWORD, tokens[6].first)
        Assertions.assertEquals(tokens[6].second, "then")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[7].first) // Changed here
        Assertions.assertEquals(tokens[7].second, " ")
        Assertions.assertEquals(PicatTokenTypes.ELSE_KEYWORD, tokens[8].first)
        Assertions.assertEquals(tokens[8].second, "else")
    }

    @Test
    fun testNumbers() {
        val tokens = tokenize("123 3.14 0xFF 0b101 0o777")
        Assertions.assertEquals(9, tokens.size, "Should have 9 tokens (5 numbers and 4 whitespaces)")
        Assertions.assertEquals(PicatTokenTypes.INTEGER, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "123")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.FLOAT, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "3.14")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first) // Changed here
        Assertions.assertEquals(tokens[3].second, " ")
        Assertions.assertEquals(PicatTokenTypes.HEX_INTEGER, tokens[4].first)
        Assertions.assertEquals(tokens[4].second, "0xFF")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[5].first) // Changed here
        Assertions.assertEquals(tokens[5].second, " ")
        Assertions.assertEquals(PicatTokenTypes.BINARY_INTEGER, tokens[6].first)
        Assertions.assertEquals(tokens[6].second, "0b101")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[7].first) // Changed here
        Assertions.assertEquals(tokens[7].second, " ")
        Assertions.assertEquals(PicatTokenTypes.OCTAL_INTEGER, tokens[8].first)
        Assertions.assertEquals(tokens[8].second, "0o777")
    }

    @Test
    fun testStrings() {
        val tokens = tokenize("\"hello\" 'atom'")
        Assertions.assertEquals(3, tokens.size, "Should have 3 tokens (2 strings and 1 whitespace)")
        Assertions.assertEquals(PicatTokenTypes.STRING, tokens[0].first)
        Assertions.assertEquals("\"hello\"", tokens[0].second)
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.QUOTED_ATOM, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "'atom'")
    }

    @Test
    fun testOperators() {
        val tokens = tokenize("+ - * / =")
        Assertions.assertEquals(9, tokens.size, "Should have 9 tokens (5 operators and 4 whitespaces)")
        Assertions.assertEquals(PicatTokenTypes.PLUS, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "+")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first) // Changed here
        Assertions.assertEquals(tokens[1].second, " ")
        Assertions.assertEquals(PicatTokenTypes.MINUS, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "-")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first) // Changed here
        Assertions.assertEquals(tokens[3].second, " ")
        Assertions.assertEquals(PicatTokenTypes.MULTIPLY, tokens[4].first)
        Assertions.assertEquals(tokens[4].second, "*")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[5].first) // Changed here
        Assertions.assertEquals(tokens[5].second, " ")
        Assertions.assertEquals(PicatTokenTypes.DIVIDE, tokens[6].first)
        Assertions.assertEquals(tokens[6].second, "/")
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[7].first) // Changed here
        Assertions.assertEquals(tokens[7].second, " ")
        Assertions.assertEquals(PicatTokenTypes.ASSIGN_OP, tokens[8].first)
        Assertions.assertEquals(tokens[8].second, "=")
    }

    @Test
    fun testSelectedOperators() { // Renamed for clarity or keep as testCompoundOperators
        val tokens = tokenize("!= => ?=>") // Input string changed

        // Verify that the compound operators are tokenized correctly
        // != should be tokenized as NOT_EQUAL
        Assertions.assertEquals(PicatTokenTypes.NOT_EQUAL, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "!=")

        // Space
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first)

        // => should be tokenized as ARROW_OP
        Assertions.assertEquals(PicatTokenTypes.ARROW_OP, tokens[2].first)
        Assertions.assertEquals(tokens[2].second, "=>")

        // Space
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first)

        // ?=> should be tokenized as BACKTRACKABLE_ARROW_OP
        Assertions.assertEquals(PicatTokenTypes.BACKTRACKABLE_ARROW_OP, tokens[4].first)
        Assertions.assertEquals(tokens[4].second, "?=>")
    }

    @Test
    fun testComments() {
        val tokens = tokenize("% This is a comment\ncode /* Multi-line\ncomment */ code")
        Assertions.assertTrue(tokens.size > 0, "Should tokenize comments correctly")
        Assertions.assertEquals(PicatTokenTypes.COMMENT, tokens[0].first)
        Assertions.assertEquals(tokens[0].second, "% This is a comment")
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
        Assertions.assertTrue(tokens.size > 0, "Should tokenize complex code correctly")

        // Verify some key tokens
        val tokenTypes = tokens.map { it.first }
        Assertions.assertTrue(tokenTypes.contains(PicatTokenTypes.COMMENT), "Should contain COMMENT token")
        Assertions.assertTrue(tokenTypes.contains(PicatTokenTypes.IDENTIFIER), "Should contain IDENTIFIER tokens")
        Assertions.assertTrue(tokenTypes.contains(PicatTokenTypes.INTEGER), "Should contain INTEGER tokens")
        Assertions.assertTrue(tokenTypes.contains(PicatTokenTypes.VARIABLE), "Should contain VARIABLE tokens")
        Assertions.assertTrue(tokenTypes.contains(PicatTokenTypes.STRING), "Should contain STRING tokens")
        Assertions.assertTrue(
            tokenTypes.contains(PicatTokenTypes.ASSIGN_OP) &&
                    tokenTypes.contains(PicatTokenTypes.MULTIPLY) &&
                    tokenTypes.contains(PicatTokenTypes.ARROW_OP),
            "Should contain operator tokens"
        )
    }

    @Test
    fun testDoubleColonOperator() {
        val tokens = tokenize("::")
        Assertions.assertEquals(1, tokens.size, "Should have 1 token")
        Assertions.assertEquals(PicatTokenTypes.DOUBLE_COLON_OP, tokens[0].first)
        Assertions.assertEquals("::", tokens[0].second)
    }

    @Test
    fun testMixedOperatorsIncludingDoubleColon() {
        val tokens = tokenize(":: + :=")
        Assertions.assertEquals(5, tokens.size, "Should correctly tokenize mixed operators")
        Assertions.assertEquals(PicatTokenTypes.DOUBLE_COLON_OP, tokens[0].first)
        Assertions.assertEquals("::", tokens[0].second)
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first)
        Assertions.assertEquals(" ", tokens[1].second)
        Assertions.assertEquals(PicatTokenTypes.PLUS, tokens[2].first)
        Assertions.assertEquals("+", tokens[2].second)
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first)
        Assertions.assertEquals(" ", tokens[3].second)
        Assertions.assertEquals(PicatTokenTypes.ASSIGN_OP, tokens[4].first)
        Assertions.assertEquals(":=", tokens[4].second)
    }

    @Test
    fun testColonAndDoubleColon() {
        val tokens = tokenize(": :: :")
        Assertions.assertEquals(5, tokens.size)
        Assertions.assertEquals(PicatTokenTypes.COLON, tokens[0].first)
        Assertions.assertEquals(":", tokens[0].second)
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[1].first)
        Assertions.assertEquals(" ", tokens[1].second)
        Assertions.assertEquals(PicatTokenTypes.DOUBLE_COLON_OP, tokens[2].first)
        Assertions.assertEquals("::", tokens[2].second)
        Assertions.assertEquals(TokenType.WHITE_SPACE, tokens[3].first)
        Assertions.assertEquals(" ", tokens[3].second)
        Assertions.assertEquals(PicatTokenTypes.COLON, tokens[4].first)
        Assertions.assertEquals(":", tokens[4].second)
    }
}
