package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatTokenType.
 * This test verifies the behavior of custom token types for Picat language lexical analysis.
 */
class PicatTokenTypeTest : BasePlatformTestCase() {

    fun testTokenTypeCreation() {
        // Test that PicatTokenType can be created with a debug name
        val debugName = "TEST_TOKEN"
        val tokenType = PicatTokenType(debugName)
        
        assertNotNull("Token type should not be null", tokenType)
        assertEquals("Language should be PicatLanguage", PicatLanguage, tokenType.language)
    }

    fun testTokenTypeWithLexicalTokenNames() {
        // Test various token names that would be used in lexical analysis
        val tokenNames = listOf(
            "IDENTIFIER",
            "INTEGER_LITERAL",
            "STRING_LITERAL",
            "SINGLE_QUOTED_ATOM",
            "DOT",
            "COMMA",
            "LPAREN",
            "RPAREN",
            "LBRACKET",
            "RBRACKET",
            "PIPE",
            "RULE_OP",
            "COMMENT",
            "WHITESPACE"
        )
        
        tokenNames.forEach { tokenName ->
            val tokenType = PicatTokenType(tokenName)
            
            assertEquals("Language should always be PicatLanguage for token: $tokenName", 
                        PicatLanguage, tokenType.language)
            
            assertNotNull("Token type should not be null for: $tokenName", tokenType)
        }
    }

    fun testTokenTypeEquality() {
        // Test that token types with same debug name behave consistently
        val debugName = "SAME_TOKEN"
        val tokenType1 = PicatTokenType(debugName)
        val tokenType2 = PicatTokenType(debugName)
        
        // Note: IElementType uses identity equality, not value equality
        assertNotSame("Different instances should not be the same object", 
                     tokenType1, tokenType2)
        
        assertEquals("Both should have same language", 
                    tokenType1.language, tokenType2.language)
    }

    fun testEmptyTokenName() {
        // Test behavior with empty token name
        val tokenType = PicatTokenType("")
        
        assertEquals("Language should be PicatLanguage even with empty name", 
                    PicatLanguage, tokenType.language)
        
        assertNotNull("Token type should not be null with empty name", tokenType)
    }

    fun testSpecialCharactersInTokenName() {
        // Test token names with special characters commonly used in lexer definitions
        val specialNames = listOf(
            "PLUS_OP",
            "MINUS_OP", 
            "MULTIPLY_OP",
            "DIVIDE_OP",
            "EQUALS_OP",
            "NOT_EQUALS_OP",
            "LESS_THAN_OP",
            "GREATER_THAN_OP",
            "ASSIGNMENT_OP",
            "MODULE_QUALIFIER_OP"
        )
        
        specialNames.forEach { name ->
            val tokenType = PicatTokenType(name)
            
            assertEquals("Language should be PicatLanguage for token name: $name", 
                        PicatLanguage, tokenType.language)
            
            assertNotNull("Token type should not be null for: $name", tokenType)
        }
    }

    fun testInheritanceFromIElementType() {
        // Test that PicatTokenType properly inherits from IElementType
        val tokenType = PicatTokenType("TEST")
        
        // Test that parent methods are accessible
        assertNotNull("getLanguage should not be null", tokenType.language)
        assertNotNull("toString should not be null", tokenType.toString())
    }

    fun testTokenTypeForKeywords() {
        // Test token types for Picat language keywords
        val keywords = listOf(
            "IF",
            "THEN", 
            "ELSE",
            "ENDIF",
            "FOREACH",
            "IN",
            "DO",
            "END",
            "WHILE",
            "TRUE",
            "FALSE",
            "MIN",
            "MAX",
            "IMPORT",
            "MODULE"
        )
        
        keywords.forEach { keyword ->
            val tokenType = PicatTokenType(keyword)
            
            assertEquals("Language should be PicatLanguage for keyword: $keyword", 
                        PicatLanguage, tokenType.language)
            
            assertNotNull("Token type should not be null for keyword: $keyword", tokenType)
        }
    }

    fun testTokenTypeConsistency() {
        // Test that token type creation is consistent across multiple calls
        val testTokens = listOf("ATOM", "INTEGER", "STRING", "OPERATOR", "DELIMITER")
        
        testTokens.forEach { token ->
            repeat(3) {
                val tokenType = PicatTokenType(token)
                assertEquals("Language should be consistent for $token", 
                            PicatLanguage, tokenType.language)
                assertNotNull("Token should be created consistently for $token", tokenType)
            }
        }
    }
}
