package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.language.psi.* // Import all PSI interfaces
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.* // Using wildcard import for assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat PSI implementation.
 * This test verifies that the PSI elements are correctly created for Picat code.
 */
class PicatPsiTest : BasePlatformTestCase() {

    @Test
    fun testFactPsi() {
        // Test that a fact is correctly parsed into a PicatPredicateFact PSI element
        val code = """
            factorial(0).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all predicate facts in the file
        val facts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java)

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts.first()
        val head = fact.head
        assertNotNull(head, "Fact should have a head")

        // Example of checking name and arity based on head structure
        val name: String?
        val arity: Int
        when {
            head.atomNoArgs != null -> {
                name = head.atomNoArgs!!.atom.text
                arity = 0
            }
            head.structure != null -> {
                name = head.structure!!.atom.text
                arity = head.structure!!.argumentList?.expressionList?.size ?: 0
            }
            else -> {
                name = null
                arity = -1 // Should not happen for a valid fact
            }
        }
        assertEquals("factorial", name, "Fact name should be 'factorial'")
        assertEquals(1, arity, "Fact arity should be 1") // factorial(0) has one argument
    }

    @Test
    fun testFactWithMultipleArgumentsPsi() {
        // Test that a fact with multiple arguments is correctly parsed
        val code = """
            custom_sum(1, 2, 3).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all predicate facts in the file
        val facts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java)

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        val fact = facts.first()
        val head = fact.head
        assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure for this case
        val structure = head.structure
        assertNotNull(structure, "Fact head should be a structure")

        // Verify that the structure has the correct name and arity
        assertEquals("custom_sum", structure!!.atom.text, "Structure name should be 'custom_sum'")
        val arity = structure.argumentList?.expressionList?.size ?: 0
        assertEquals(3, arity, "Structure arity should be 3")

        // Verify that the structure has an argument list
        val argumentList = structure.argumentList
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.expressionList
        assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression text
        assertEquals("1", arguments[0].text, "First argument should be 1")
        assertEquals("2", arguments[1].text, "Second argument should be 2")
        assertEquals("3", arguments[2].text, "Third argument should be 3")
    }

    @Test
    fun testExportStatementPsi() {
        // Test that an export statement is correctly parsed into a PicatExportStatement PSI element
        // Note: BNF for PicatExportStatement not provided in this subtask, assuming it's a top-level item.
        val code = """
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all export statements in the file
        val exportStatements = PsiTreeUtil.findChildrenOfType(file, PicatExportStatement::class.java)

        // Verify that there is exactly one export statement
        assertEquals(1, exportStatements.size, "There should be exactly one export statement")
        // Further assertions would depend on PicatExportStatement's internal structure.
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
        val includeStatements = PsiTreeUtil.findChildrenOfType(file, PicatIncludeStatement::class.java)

        // Verify that there is exactly one include statement
        assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements.first()
        var path = includeStatement.fileSpec?.text
        if (includeStatement.fileSpec?.string != null) { // If it's a STRING token
            path = path?.removeSurrounding("\"")
        }
        assertEquals("utils.pi", path, "Include statement should have path utils.pi")
    }

    @Test
    fun testImportStatementMethods() {
        // Test finding import statements and extracting module names
        val code = """
            % This is a sample Picat program with multiple imports

            import util, math, cp.
            import planner.

            main => println("Hello, world!").
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all import statements
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java)
        assertEquals(2, importStatements.size, "There should be exactly two import statements")

        // Verify that the import statements have the correct text (optional, as structure is more important)
        assertTrue(importStatements[0].text.startsWith("import util, math, cp."), "First import statement text check")
        assertTrue(importStatements[1].text.startsWith("import planner."), "Second import statement text check")

        // Test extracting all imported module names from the file
        val allImportedModuleNames = importStatements.flatMap { stmt ->
            stmt.importList?.importItemList?.mapNotNull { it.moduleName?.atom?.text } ?: emptyList()
        }
        assertEquals(4, allImportedModuleNames.size, "There should be four imported module names in total")
        assertTrue(allImportedModuleNames.containsAll(listOf("util", "math", "cp", "planner")), "All expected module names should be present")
    }
}
