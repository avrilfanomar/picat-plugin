package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatFileImpl.
 * This test verifies the basic behavior of Picat file PSI implementation.
 */
class PicatFileImplTest : BasePlatformTestCase() {

    fun testFileCreation() {
        // Test that PicatFileImpl is created correctly
        val code = "simple_fact."
        myFixture.configureByText("test.pi", code)

        val psiFile = myFixture.file
        assertTrue("File should be instance of PicatFileImpl", psiFile is PicatFileImpl)
    }

    fun testGetFileType() {
        // Test the getFileType method
        val code = "test_predicate."
        myFixture.configureByText("test.pi", code)

        val picatFile = myFixture.file as PicatFileImpl
        val fileType = picatFile.getFileType()

        assertEquals("getFileType should return PicatFileType", PicatFileType, fileType)
        assertNotNull("File type should not be null", fileType)
    }

    fun testToString() {
        // Test the toString method
        val code = "test_atom."
        myFixture.configureByText("test.pi", code)

        val picatFile = myFixture.file as PicatFileImpl
        assertEquals("toString should return 'Picat'", "Picat", picatFile.toString())
    }

    fun testFileTypeConsistency() {
        // Test that file type is consistent across different content
        val codes = listOf("fact1.", "rule :- body.", "include \"test.pi\".")
        
        codes.forEach { code ->
            myFixture.configureByText("test.pi", code)
            val picatFile = myFixture.file as PicatFileImpl
            
            assertEquals("File type should be consistent for: $code", 
                        PicatFileType, picatFile.getFileType())
            assertEquals("toString should be consistent for: $code", 
                        "Picat", picatFile.toString())
        }
    }

    fun testEmptyFile() {
        // Test with empty file
        myFixture.configureByText("test.pi", "")
        val picatFile = myFixture.file as PicatFileImpl
        
        assertEquals("Empty file should have correct type", PicatFileType, picatFile.getFileType())
        assertEquals("Empty file toString should work", "Picat", picatFile.toString())
    }
}
