package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test for Picat PSI implementation.
 * This test verifies that the PSI elements are correctly created for Picat code.
 */
class PicatPsiTest : BasePlatformTestCase() {

    @Test
    fun testFactPsi() {
        // Test that a fact is correctly parsed into a PicatFact PSI element
        val code = """
            factorial(0).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all facts in the file
        val facts = file.getAllFacts()

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")
    }

    @Test
    fun testFactWithMultipleArgumentsPsi() {
        // Test that a fact with multiple arguments is correctly parsed
        val code = """
            custom_sum(1, 2, 3).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all facts in the file
        val facts = file.getAllFacts()

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        assertTrue(head is PicatHead, "Head should be a PicatHead")
        val structure = head?.children?.first() as PicatStructure

        // Verify that the structure has the correct name and arity
        assertNotNull(structure, "Fact should have a structure")
        assertEquals("Structure should have name sum", "custom_sum", structure.getName())
        assertEquals("Structure should have arity 3", 3, structure.getArity())

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments[0].getExpression()?.text)
        assertEquals("Second argument should be 2", "2", arguments[1].getExpression()?.text)
        assertEquals("Third argument should be 3", "3", arguments[2].getExpression()?.text)
    }

    @Test
    fun testExportStatementPsi() {
        // Test that an export statement is correctly parsed into a PicatExportStatement PSI element
        val code = """
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all export statements in the file
        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)

        // Verify that there is exactly one export statement
        assertEquals(1, exportStatements.size, "There should be exactly one export statement")
    }

    @Test
    fun testIncludeStatementPsi() {
        // Test that an include statement is correctly parsed into a PicatIncludeStatement PSI element
        val code = """
            include "utils.pi".
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all include statements in the file
        val includeStatements = file.findChildrenByClass(PicatIncludeStatement::class.java)

        // Verify that there is exactly one include statement
        assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements[0]
        assertEquals("Include statement should have path utils.pi", "utils.pi", includeStatement.getIncludePath())
    }

    @Test
    fun testImportStatementMethods() {
        // Test that the getImportStatements method works correctly
        val code = """
            % This is a sample Picat program with multiple imports

            import util, math, cp.
            import planner.

            main => println("Hello, world!").
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Test getImportStatements method
        val importStatements = file.getImportStatements()
        assertEquals(2, importStatements.size, "There should be exactly two import statements")

        // Verify that the import statements have the correct text
        assertTrue(importStatements[0].text.startsWith("import"), "First import statement should start with 'import'")
        assertTrue(importStatements[1].text.startsWith("import"), "Second import statement should start with 'import'")

        // Test getImportedModuleNames method
        assertEquals(4, file.getImportedModuleNames().size, "There should be no module names")
    }
}
