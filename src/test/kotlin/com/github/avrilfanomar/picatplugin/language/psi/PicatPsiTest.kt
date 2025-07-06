package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat PSI implementation.
 * This test verifies that the PSI elements are correctly created for Picat code.
 */
class PicatPsiTest : BasePlatformTestCase() {

    @Test
    fun testFactWithMultipleArgumentsPsi() {
        // Test that a fact with multiple arguments is correctly parsed
        val code = "custom_sum(1, 2, 3).".trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all predicate facts in the file
        val facts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java)

        // Verify that there is exactly one fact
        Assertions.assertEquals(
            1,
            facts.size,
            "There should be exactly one fact, psi:" + DebugUtil.psiToString(file, false)
        )

        val fact = facts.first()
        val head = fact.head
        Assertions.assertNotNull(head, "Fact should have a head")
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
        Assertions.assertEquals(1, exportStatements.size, "There should be exactly one export statement")
        // Further Assertions.assertions would depend on PicatExportStatement's internal structure.
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
        Assertions.assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements.first()
        var path = includeStatement.fileSpec?.text
        if (includeStatement.fileSpec?.string != null) { // If it's a STRING token
            path = path?.removeSurrounding("\"")
        }
        Assertions.assertEquals("utils.pi", path, "Include statement should have path utils.pi")
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
        Assertions.assertEquals(2, importStatements.size, "There should be exactly two import statements, PSI: " +
                DebugUtil.psiToString(file, false))

        // Test extracting all imported module names from the file
        val allImportedModuleNames = importStatements.flatMap { stmt ->
            stmt.importList?.importItemList?.mapNotNull { it.moduleName.atom.text } ?: emptyList()
        }
        Assertions.assertEquals(4, allImportedModuleNames.size, "There should be four imported module names in total")
        Assertions.assertTrue(
            allImportedModuleNames.containsAll(listOf("util", "math", "cp", "planner")),
            "All expected module names should be present"
        )
    }
}
