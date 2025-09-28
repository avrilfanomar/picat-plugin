package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.TokenType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatSyntaxHighlighterTest : BasePlatformTestCase() {

    private lateinit var highlighter: PicatSyntaxHighlighter

    override fun setUp() {
        super.setUp()
        highlighter = PicatSyntaxHighlighter()
    }

    fun testHighlighterCreation() {
        assertDoesNotThrow("Highlighter should be created without exceptions") {
            val syntaxHighlighter = PicatSyntaxHighlighter()
            assertNotNull("Highlighter should not be null", syntaxHighlighter)
        }
    }

    fun testGetHighlightingLexer() {
        assertDoesNotThrow("Should get highlighting lexer") {
            val lexer = highlighter.getHighlightingLexer()
            assertNotNull("Lexer should not be null", lexer)
        }
    }

    fun testKeywordHighlighting() {
        assertDoesNotThrow("Should highlight keywords") {
            // Test various keyword tokens
            val keywordTokens = listOf(
                PicatTokenTypes.MODULE_KEYWORD,
                PicatTokenTypes.IMPORT_KEYWORD,
                PicatTokenTypes.IF_KEYWORD,
                PicatTokenTypes.THEN_KEYWORD,
                PicatTokenTypes.ELSE_KEYWORD,
                PicatTokenTypes.END_KEYWORD,
                PicatTokenTypes.FOREACH_KEYWORD,
                PicatTokenTypes.WHILE_KEYWORD,
                PicatTokenTypes.TRUE,
                PicatTokenTypes.FALSE
            )
            
            keywordTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have keyword highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testCommentHighlighting() {
        assertDoesNotThrow("Should highlight comments") {
            val commentTokens = listOf(
                PicatTokenTypes.COMMENT,
                PicatTokenTypes.MULTILINE_COMMENT
            )
            
            commentTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have comment highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testStringHighlighting() {
        assertDoesNotThrow("Should highlight strings") {
            val stringTokens = listOf(
                PicatTokenTypes.STRING,
                PicatTokenTypes.SINGLE_QUOTED_ATOM
            )
            
            stringTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have string highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testNumberHighlighting() {
        assertDoesNotThrow("Should highlight numbers") {
            val numberTokens = listOf(
                PicatTokenTypes.INTEGER,
                PicatTokenTypes.FLOAT
            )
            
            numberTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have number highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testOperatorHighlighting() {
        assertDoesNotThrow("Should highlight operators") {
            val operatorTokens = listOf(
                PicatTokenTypes.ARROW_OP,
                PicatTokenTypes.ASSIGN_OP,
                PicatTokenTypes.PLUS,
                PicatTokenTypes.MINUS,
                PicatTokenTypes.MULTIPLY,
                PicatTokenTypes.DIVIDE,
                PicatTokenTypes.EQUAL,
                PicatTokenTypes.NOT_EQUAL,
                PicatTokenTypes.GREATER,
                PicatTokenTypes.LESS,
                PicatTokenTypes.AND_AND,
                PicatTokenTypes.OR_OR
            )
            
            operatorTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have operator highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testParenthesesHighlighting() {
        assertDoesNotThrow("Should highlight parentheses") {
            val parenthesesTokens = listOf(
                PicatTokenTypes.LPAR,
                PicatTokenTypes.RPAR
            )
            
            parenthesesTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have parentheses highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testBracesHighlighting() {
        assertDoesNotThrow("Should highlight braces") {
            val braceTokens = listOf(
                PicatTokenTypes.LBRACE,
                PicatTokenTypes.RBRACE
            )
            
            braceTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have brace highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testBracketsHighlighting() {
        assertDoesNotThrow("Should highlight brackets") {
            val bracketTokens = listOf(
                PicatTokenTypes.LBRACKET,
                PicatTokenTypes.RBRACKET
            )
            
            bracketTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                assertTrue("Should have bracket highlighting for $tokenType", highlights.isNotEmpty())
            }
        }
    }

    fun testVariableHighlighting() {
        assertDoesNotThrow("Should highlight variables") {
            val highlights = highlighter.getTokenHighlights(PicatTokenTypes.VARIABLE)
            assertNotNull("Highlights should not be null for variables", highlights)
            assertTrue("Should have variable highlighting", highlights.isNotEmpty())
        }
    }

    fun testIdentifierHighlighting() {
        assertDoesNotThrow("Should highlight identifiers") {
            val highlights = highlighter.getTokenHighlights(PicatTokenTypes.IDENTIFIER)
            assertNotNull("Highlights should not be null for identifiers", highlights)
            assertTrue("Should have identifier highlighting", highlights.isNotEmpty())
        }
    }

    fun testBadCharacterHighlighting() {
        assertDoesNotThrow("Should highlight bad characters") {
            val highlights = highlighter.getTokenHighlights(TokenType.BAD_CHARACTER)
            assertNotNull("Highlights should not be null for bad characters", highlights)
            assertTrue("Should have bad character highlighting", highlights.isNotEmpty())
        }
    }

    fun testUnknownTokenHighlighting() {
        assertDoesNotThrow("Should handle unknown tokens") {
            // Test with a token type that doesn't have specific highlighting
            val highlights = highlighter.getTokenHighlights(TokenType.WHITE_SPACE)
            assertNotNull("Highlights should not be null for unknown tokens", highlights)
            // Unknown tokens should return empty array
            assertTrue("Unknown tokens should have empty highlighting", highlights.isEmpty())
        }
    }

    fun testTextAttributeKeysCreation() {
        assertDoesNotThrow("Should create text attribute keys") {
            // Test that all text attribute keys are properly created
            assertNotNull("KEYWORD key should not be null", PicatSyntaxHighlighter.KEYWORD)
            assertNotNull("COMMENT key should not be null", PicatSyntaxHighlighter.COMMENT)
            assertNotNull("STRING key should not be null", PicatSyntaxHighlighter.STRING)
            assertNotNull("NUMBER key should not be null", PicatSyntaxHighlighter.NUMBER)
            assertNotNull("OPERATOR key should not be null", PicatSyntaxHighlighter.OPERATOR)
            assertNotNull("PARENTHESES key should not be null", PicatSyntaxHighlighter.PARENTHESES)
            assertNotNull("BRACES key should not be null", PicatSyntaxHighlighter.BRACES)
            assertNotNull("BRACKETS key should not be null", PicatSyntaxHighlighter.BRACKETS)
            assertNotNull("IDENTIFIER key should not be null", PicatSyntaxHighlighter.IDENTIFIER)
            assertNotNull("VARIABLE key should not be null", PicatSyntaxHighlighter.VARIABLE)
            assertNotNull("BAD_CHARACTER_ATTR key should not be null", PicatSyntaxHighlighter.BAD_CHARACTER_ATTR)
        }
    }

    fun testTokenSetsCreation() {
        assertDoesNotThrow("Should create token sets") {
            // Test that token sets are properly created
            assertNotNull("KEYWORDS_SET should not be null", PicatSyntaxHighlighter.KEYWORDS_SET)
            assertNotNull("OPERATORS_SET should not be null", PicatSyntaxHighlighter.OPERATORS_SET)
            
            assertTrue("KEYWORDS_SET should not be empty", PicatSyntaxHighlighter.KEYWORDS_SET.types.isNotEmpty())
            assertTrue("OPERATORS_SET should not be empty", PicatSyntaxHighlighter.OPERATORS_SET.types.isNotEmpty())
        }
    }

    fun testComprehensiveTokenHighlighting() {
        assertDoesNotThrow("Should handle comprehensive token highlighting") {
            // Test a variety of tokens to ensure robustness
            val testTokens = listOf(
                PicatTokenTypes.MODULE_KEYWORD,
                PicatTokenTypes.COMMENT,
                PicatTokenTypes.STRING,
                PicatTokenTypes.INTEGER,
                PicatTokenTypes.PLUS,
                PicatTokenTypes.LPAR,
                PicatTokenTypes.LBRACE,
                PicatTokenTypes.LBRACKET,
                PicatTokenTypes.VARIABLE,
                PicatTokenTypes.IDENTIFIER,
                TokenType.BAD_CHARACTER,
                TokenType.WHITE_SPACE
            )
            
            testTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Highlights should not be null for $tokenType", highlights)
                // Highlights array should always be non-null (but may be empty)
            }
        }
    }

    fun testSpecialTokenHighlighting() {
        assertDoesNotThrow("Should handle special tokens") {
            // Test boolean tokens (TRUE/FALSE) which are treated as keywords
            val booleanTokens = listOf(PicatTokenTypes.TRUE, PicatTokenTypes.FALSE)
            
            booleanTokens.forEach { tokenType ->
                val highlights = highlighter.getTokenHighlights(tokenType)
                assertNotNull("Boolean highlights should not be null for $tokenType", highlights)
                assertTrue("Boolean tokens should have highlighting", highlights.isNotEmpty())
            }
        }
    }

    private fun assertDoesNotThrow(message: String, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("$message - Exception: ${e.message}")
        }
    }
}
