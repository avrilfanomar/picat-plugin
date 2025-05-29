package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

/**
 * Test for Picat module name PSI implementation.
 * This test verifies that module names are correctly parsed into PSI elements.
 */
class PicatModuleNameTest : BasePlatformTestCase() {

    @Test
    fun testSimpleModuleNamePsi() {
        // Test that a simple module name is correctly parsed
        val code = """
            import util.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all import statements in the file
        val importStatements = file.findChildrenByClass(PicatImportStatement::class.java)

        // Verify that there is exactly one import statement
        assertEquals(1, importStatements.size, "There should be exactly one import statement")

        // Get the module names from the import statement
        val moduleNames = importStatements[0].getModuleNames()

        // Verify that there is exactly one module name
        assertEquals(1, moduleNames.size, "There should be exactly one module name")

        // Verify that the module name has the correct text
        val moduleName = moduleNames[0]
        assertEquals("util", moduleName.text, "Module name should be 'util'")

        // Verify that the module name has the correct identifier
        val identifier = moduleName.getIdentifier()
        assertNotNull(identifier, "Module name should have an identifier")
        assertEquals("util", identifier?.text, "Identifier should be 'util'")

        // Test the getName method (which might be missing in the implementation)
        try {
            val name = moduleName.getName()
            assertEquals("util", name, "Module name should be 'util'")
        } catch (e: Exception) {
            // This test might fail if getName() is not implemented
            fail("getName() method threw an exception: ${e.message}")
        }
    }

    @Test
    fun testMultipleModuleNamesPsi() {
        // Test that multiple module names are correctly parsed
        val code = """
            import util, math, cp.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all import statements in the file
        val importStatements = file.findChildrenByClass(PicatImportStatement::class.java)

        // Verify that there is exactly one import statement
        assertEquals(1, importStatements.size, "There should be exactly one import statement")

        // Get the module names from the import statement
        val moduleNames = importStatements[0].getModuleNames()

        // Verify that there are exactly three module names
        assertEquals(3, moduleNames.size, "There should be exactly three module names")

        // Verify that each module name has the correct text
        assertEquals("util", moduleNames[0].text, "First module name should be 'util'")
        assertEquals("math", moduleNames[1].text, "Second module name should be 'math'")
        assertEquals("cp", moduleNames[2].text, "Third module name should be 'cp'")

        // Verify that each module name has the correct identifier
        assertEquals("util", moduleNames[0].getIdentifier()?.text, "First identifier should be 'util'")
        assertEquals("math", moduleNames[1].getIdentifier()?.text, "Second identifier should be 'math'")
        assertEquals("cp", moduleNames[2].getIdentifier()?.text, "Third identifier should be 'cp'")

        // Test the getName method for each module name
        try {
            assertEquals("util", moduleNames[0].getName(), "First module name should be 'util'")
            assertEquals("math", moduleNames[1].getName(), "Second module name should be 'math'")
            assertEquals("cp", moduleNames[2].getName(), "Third module name should be 'cp'")
        } catch (e: Exception) {
            // This test might fail if getName() is not implemented
            fail("getName() method threw an exception: ${e.message}")
        }
    }

    @Test
    fun testMultipleImportStatementsPsi() {
        // Test that multiple import statements are correctly parsed
        val code = """
            import util.
            import math.
            import cp.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all import statements in the file
        val importStatements = file.findChildrenByClass(PicatImportStatement::class.java)

        // Verify that there are exactly three import statements
        assertEquals(3, importStatements.size, "There should be exactly three import statements")

        // Verify that each import statement has exactly one module name
        for (i in 0..2) {
            val moduleNames = importStatements[i].getModuleNames()
            assertEquals(1, moduleNames.size, "Import statement ${i+1} should have exactly one module name")
        }

        // Verify that each module name has the correct text
        assertEquals("util", importStatements[0].getModuleNames()[0].text, "First module name should be 'util'")
        assertEquals("math", importStatements[1].getModuleNames()[0].text, "Second module name should be 'math'")
        assertEquals("cp", importStatements[2].getModuleNames()[0].text, "Third module name should be 'cp'")
    }
}