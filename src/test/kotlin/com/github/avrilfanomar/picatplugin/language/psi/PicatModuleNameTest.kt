package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat module name PSI implementation.
 * This test verifies that module names are correctly parsed into PSI elements.
 */
class PicatModuleNameTest : BasePlatformTestCase() {

    @Test
    fun testSimpleModuleNamePsi() { // Corresponds to testSingleModuleImport
        // Test that a simple module name is correctly parsed
        val code = """
            import util.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all import statements in the file
        val importStatements = PsiTreeUtil.findChildrenOfType(file as PsiElement, PicatImportDeclaration::class.java)

        // Verify that there is exactly one import statement
        Assertions.assertEquals(1, importStatements.size, "There should be exactly one import statement")

        // Get the module names from the import statement
        val importStatement = importStatements.first()
        val moduleNameElements = importStatement.importItemList
        val moduleNames = moduleNameElements.mapNotNull { it.text }


        // Verify that there is exactly one module name
        Assertions.assertEquals(1, moduleNames.size, "There should be exactly one module name")

        // Verify that the module name has the correct text
        val moduleNameText = moduleNames.first()
        Assertions.assertEquals("util", moduleNameText, "Module name should be 'util'")
    }

    @Test
    fun testMultipleModuleNamesPsi() { // Corresponds to testMultipleModuleImport
        // Test that multiple module names are correctly parsed in a single import statement
        val code = """
            import util, math, cp.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all import statements in the file
        val importStatements = PsiTreeUtil.findChildrenOfType(file as PsiElement, PicatImportDeclaration::class.java)

        // Verify that there is exactly one import statement
        Assertions.assertEquals(1, importStatements.size, "There should be exactly one import statement")

        // Get the module names from the import statement
        val importStatement = importStatements.first()
        val moduleNameElements = importStatement.importItemList
        val moduleNames = moduleNameElements.mapNotNull { it.text }

        // Verify that there are exactly three module names
        Assertions.assertEquals(3, moduleNames.size, "There should be exactly three module names")

        // Verify that each module name has the correct text
        Assertions.assertEquals("util", moduleNames[0], "First module name should be 'util'")
        Assertions.assertEquals("math", moduleNames[1], "Second module name should be 'math'")
        Assertions.assertEquals("cp", moduleNames[2], "Third module name should be 'cp'")
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
        val file = myFixture.file as PicatFileImpl

        // Find all import statements in the file
        val importStatements = PsiTreeUtil.findChildrenOfType(file as PsiElement, PicatImportDeclaration::class.java)

        // Verify that there are exactly three import statements
        Assertions.assertEquals(3, importStatements.size, "There should be exactly three import statements")

        // Verify that each import statement has exactly one module name and the correct text
        val expectedModuleNames = listOf("util", "math", "cp")
        importStatements.forEachIndexed { i, importStatement ->
            val moduleNameElements = importStatement.importItemList
            val moduleNames = moduleNameElements.mapNotNull { it.text }

            Assertions.assertEquals(
                1,
                moduleNames.size,
                "Import statement ${i + 1} should have exactly one module name"
            )
            Assertions.assertEquals(
                expectedModuleNames[i],
                moduleNames.first(),
                "Module name for import statement ${i + 1} should be '${expectedModuleNames[i]}'"
            )
        }
    }
}
