package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatElementType.
 * This test verifies the behavior of custom element types for Picat language PSI elements.
 */
class PicatElementTypeTest : BasePlatformTestCase() {

    fun testElementTypeCreation() {
        // Test that PicatElementType can be created with a debug name
        val debugName = "TEST_ELEMENT"
        val elementType = PicatElementType(debugName)
        
        assertNotNull("Element type should not be null", elementType)
        assertEquals("Language should be PicatLanguage", PicatLanguage, elementType.language)
    }

    fun testToStringMethod() {
        // Test that toString returns the expected format
        val debugName = "PREDICATE_RULE"
        val elementType = PicatElementType(debugName)
        
        val stringRepresentation = elementType.toString()
        assertTrue("toString should start with 'PicatElementType.'", 
                  stringRepresentation.startsWith("PicatElementType."))
        assertTrue("toString should contain the debug name", 
                  stringRepresentation.contains(debugName))
    }

    fun testDifferentDebugNames() {
        // Test various debug names to ensure proper handling
        val debugNames = listOf(
            "ATOM",
            "PREDICATE_FACT", 
            "INCLUDE_DECLARATION",
            "HEAD",
            "BODY",
            "TERM",
            "EXPRESSION",
            "COMMENT"
        )
        
        debugNames.forEach { debugName ->
            val elementType = PicatElementType(debugName)
            
            assertEquals("Language should always be PicatLanguage for: $debugName", 
                        PicatLanguage, elementType.language)
            
            val stringRep = elementType.toString()
            assertTrue("toString should contain debug name '$debugName'", 
                      stringRep.contains(debugName))
            assertTrue("toString should have proper format for '$debugName'",
                      stringRep.startsWith("PicatElementType."))
        }
    }

    fun testEmptyDebugName() {
        // Test behavior with empty debug name
        val elementType = PicatElementType("")
        
        assertEquals("Language should be PicatLanguage even with empty name", 
                    PicatLanguage, elementType.language)
        
        val stringRep = elementType.toString()
        assertTrue("toString should still work with empty debug name",
                  stringRep.startsWith("PicatElementType."))
    }

    fun testSpecialCharactersInDebugName() {
        // Test debug names with special characters
        val specialNames = listOf(
            "TEST_ELEMENT_123",
            "element-with-dashes",
            "element.with.dots",
            "ELEMENT_WITH_UNDERSCORES",
            "MixedCaseElement"
        )
        
        specialNames.forEach { name ->
            val elementType = PicatElementType(name)
            
            assertEquals("Language should be PicatLanguage for special name: $name", 
                        PicatLanguage, elementType.language)
            
            val stringRep = elementType.toString()
            assertTrue("toString should contain special name '$name'", 
                      stringRep.contains(name))
        }
    }

    fun testElementTypeEquality() {
        // Test that element types with same debug name behave consistently
        val debugName = "SAME_NAME"
        val elementType1 = PicatElementType(debugName)
        val elementType2 = PicatElementType(debugName)
        
        // Note: IElementType uses identity equality, not value equality
        assertNotSame("Different instances should not be the same object", 
                     elementType1, elementType2)
        
        assertEquals("Both should have same language", 
                    elementType1.language, elementType2.language)
        
        assertEquals("Both should have same toString format", 
                    elementType1.toString(), elementType2.toString())
    }

    fun testInheritanceFromIElementType() {
        // Test that PicatElementType properly inherits from IElementType
        val elementType = PicatElementType("TEST")
        
        assertTrue("Should be instance of IElementType", 
                  elementType is com.intellij.psi.tree.IElementType)
        
        // Test that parent methods are accessible
        assertNotNull("getLanguage should not be null", elementType.language)
        assertNotNull("toString should not be null", elementType.toString())
    }
}
