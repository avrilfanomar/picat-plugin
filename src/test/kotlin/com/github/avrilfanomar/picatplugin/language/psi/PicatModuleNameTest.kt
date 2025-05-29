package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
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
        assertEquals("There should be exactly one import statement", 1, importStatements.size)

        // Get the module names from the import statement
        val moduleNames = importStatements[0].getModuleNames()

        // Verify that there is exactly one module name
        assertEquals("There should be exactly one module name", 1, moduleNames.size)

        // Verify that the module name has the correct text
        val moduleName = moduleNames[0]
        assertEquals("Module name should be 'util'", "util", moduleName.text)

        // Verify that the module name has the correct identifier
        val name = moduleName.text
        assertNotNull("Module name should have an identifier", name)
        assertEquals("Identifier should be 'util'", "util", name)

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
        assertEquals("There should be exactly one import statement", 1, importStatements.size)

        // Get the module names from the import statement
        val moduleNames = importStatements[0].getModuleNames()

        // Verify that there are exactly three module names
        assertEquals("There should be exactly three module names", 3, moduleNames.size)

        // Verify that each module name has the correct text
        assertEquals("First module name should be 'util'", "util", moduleNames[0].text)
        assertEquals("Second module name should be 'math'", "math", moduleNames[1].text)
        assertEquals("Third module name should be 'cp'", "cp", moduleNames[2].text)
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
        assertEquals("There should be exactly three import statements", 3, importStatements.size)

        // Verify that each import statement has exactly one module name
        for (i in 0..2) {
            val moduleNames = importStatements[i].getModuleNames()
            assertEquals("Import statement ${i + 1} should have exactly one module name", 1, moduleNames.size)
        }

        // Verify that each module name has the correct text
        assertEquals("First module name should be 'util'", "util", importStatements[0].getModuleNames()[0].text)
        assertEquals("Second module name should be 'math'", "math", importStatements[1].getModuleNames()[0].text)
        assertEquals("Third module name should be 'cp'", "cp", importStatements[2].getModuleNames()[0].text)
    }
}
