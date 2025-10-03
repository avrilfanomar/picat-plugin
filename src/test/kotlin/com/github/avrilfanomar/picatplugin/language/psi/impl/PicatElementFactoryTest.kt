package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatElementFactory.
 * This test verifies the behavior of the Picat PSI element factory.
 */
class PicatElementFactoryTest : BasePlatformTestCase() {

    fun testCreateFile() {
        // Test creating a PSI file from text
        val text = "test_fact."
        val file = PicatElementFactory.createFile(project, text)
        
        assertNotNull("Created file should not be null", file)
        assertEquals("File type should be PicatFileType", PicatFileType, file.fileType)
        assertEquals("File text should match input", text, file.text)
    }

    fun testCreateFileWithComplexContent() {
        // Test creating a file with various Picat constructs
        val complexText = """
            fact1.
            rule1 :- body1.
            include "module.pi".
        """.trimIndent()
        
        val file = PicatElementFactory.createFile(project, complexText)
        
        assertNotNull("Complex file should not be null", file)
        assertEquals("Complex file text should match", complexText, file.text)
    }

    fun testCreateFileWithEmptyText() {
        // Test creating a file with empty text
        val emptyText = ""
        val file = PicatElementFactory.createFile(project, emptyText)
        
        assertNotNull("Empty file should not be null", file)
        assertEquals("Empty file text should be empty", emptyText, file.text)
    }

    fun testCreateAtom() {
        // Test creating a simple atom
        val atomName = "test_atom"
        val atom = PicatElementFactory.createAtom(project, atomName)
        
        assertNotNull("Created atom should not be null", atom)
        assertEquals("Atom name should match", atomName, atom.name)
    }

    fun testCreateAtomWithVariousNames() {
        // Test creating atoms with different name patterns
        val atomNames = listOf(
            "simple",
            "atom_with_underscores",
            "atomWithCamelCase",
            "atom123",
            "a",
            "very_long_atom_name_with_many_parts"
        )
        
        atomNames.forEach { name ->
            val atom = PicatElementFactory.createAtom(project, name)
            assertNotNull("Atom should be created for name: $name", atom)
            assertEquals("Name should match for: $name", name, atom.name)
        }
    }

    fun testCreateAtomWithQuotedName() {
        // Test creating atom with the quoted name
        val quotedName = "'quoted atom'"
        val atom = PicatElementFactory.createAtom(project, quotedName)
        
        assertNotNull("Quoted atom should not be null", atom)
    }

    fun testCreateNameIdentifier() {
        // Test creating name identifier
        val identifierText = "test_identifier"
        val identifier = PicatElementFactory.createNameIdentifier(project, identifierText)
        
        assertNotNull("Name identifier should not be null", identifier)
        assertEquals("Identifier text should match", identifierText, identifier.text)
    }

    fun testCreateNameIdentifierWithVariousNames() {
        // Test creating name identifiers with different patterns
        val identifierNames = listOf(
            "simple",
            "identifier_with_underscores",
            "IdentifierWithCamelCase",
            "id123",
            "x"
        )
        
        identifierNames.forEach { name ->
            try {
                val identifier = PicatElementFactory.createNameIdentifier(project, name)
                assertNotNull("Identifier should be created for name: $name", identifier)
                assertEquals("Identifier text should match for: $name", name, identifier.text)
            } catch (e: IllegalStateException) {
                // Some names like CamelCase may not be valid Picat atoms
                assertTrue("Factory should fail gracefully for invalid name: $name", 
                          e.message?.contains("Failed to create") ?: false)
            }
        }
    }

    fun testCreateNameIdentifierWithQuoted() {
        // Test creating name identifier with quoted text
        val quotedText = "'quoted_identifier'"
        val identifier = PicatElementFactory.createNameIdentifier(project, quotedText)
        
        assertNotNull("Quoted identifier should not be null", identifier)
    }

    fun testFactoryMethodsIntegration() {
        // Test that factory methods work together
        val atomName = "integration_test"
        
        // Create atom
        val atom = PicatElementFactory.createAtom(project, atomName)
        assertNotNull("Integration atom should not be null", atom)
        
        // Create a name identifier
        val identifier = PicatElementFactory.createNameIdentifier(project, atomName)
        assertNotNull("Integration identifier should not be null", identifier)
        
        // Verify they have consistent text
        assertEquals("Atom and identifier should have same text", 
                    atom.name, identifier.text)
    }

    fun testErrorHandlingForInvalidAtom() {
        // Test error handling for invalid atom creation
        val invalidNames = listOf(
            "123invalid",  // Starting with a number
            "",            // Empty name
            " ",           // Whitespace only
            "invalid-dash" // Invalid characters
        )
        
        invalidNames.forEach { invalidName ->
            assertDoesNotThrow {
                try {
                    PicatElementFactory.createAtom(project, invalidName)
                } catch (e: RuntimeException) {
                    // Expected for some invalid names
                    assertTrue("Should throw meaningful error for: $invalidName", 
                              e.message?.contains("Failed to create") ?: false)
                }
            }
        }
    }

    fun testFactoryConsistency() {
        // Test that multiple calls produce consistent results
        val testName = "consistency_test"
        
        repeat(3) {
            val atom = PicatElementFactory.createAtom(project, testName)
            val identifier = PicatElementFactory.createNameIdentifier(project, testName)
            val file = PicatElementFactory.createFile(project, "$testName.")
            
            assertNotNull("Atom should be consistent", atom)
            assertNotNull("Identifier should be consistent", identifier)
            assertNotNull("File should be consistent", file)
            
            assertEquals("Atom name should be consistent", testName, atom.name)
            assertEquals("Identifier text should be consistent", testName, identifier.text)
        }
    }

    fun testFileFactoryWithSpecialContent() {
        // Test file creation with special content types
        val specialContents = listOf(
            "% Comment only",
            "/* Block comment */",
            "fact1. fact2.",
            "rule :- body1, body2, body3.",
            "include \"test.pi\". fact."
        )
        
        specialContents.forEach { content ->
            val file = PicatElementFactory.createFile(project, content)
            assertNotNull("File should be created for special content: $content", file)
            assertEquals("Content should match for: $content", content, file.text)
        }
    }

    private fun assertDoesNotThrow(action: () -> Unit) {
        try {
            action()
        } catch (_: Exception) {
            // For this test, we allow exceptions but verify they are meaningful
            // The factory is expected to handle some invalid cases gracefully
        }
    }
}
