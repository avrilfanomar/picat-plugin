package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for Picat module declaration PSI implementation.
 * This test verifies that module declarations are correctly parsed into PSI elements.
 */
class PicatModuleDeclarationTest : BasePlatformTestCase() {

    @Test
    fun testSimpleModuleDeclarationPsi() {
        // Test that a simple module declaration is correctly parsed
        val code = """
            module example.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all module declarations in the file
        val moduleDeclarations = file.findChildrenByClass(PicatModuleDeclaration::class.java)

        // Verify that there is exactly one module declaration
        assertEquals("There should be exactly one module declaration", 1, moduleDeclarations.size)

        // Verify that the module declaration has the correct name
        val moduleName = moduleDeclarations[0].getName()
        assertEquals("Module name should be 'example'", "example", moduleName)
    }

    @Test
    fun testModuleDeclarationWithImportExportPsi() {
        // Test that a module declaration with import and export statements is correctly parsed
        val code = """
            module example.

            import util.
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all module declarations in the file
        val moduleDeclarations = file.findChildrenByClass(PicatModuleDeclaration::class.java)

        // Verify that there is exactly one module declaration
        assertEquals("There should be exactly one module declaration", 1, moduleDeclarations.size)

        // Verify that the module declaration has the correct name
        val moduleName = moduleDeclarations[0].getName()
        assertEquals("Module name should be 'example'", "example", moduleName)

        // Verify that import and export statements are parsed correctly
        val importStatements = file.getImportStatements()
        assertEquals("Should have one import statement", 1, importStatements.size)

        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)
        assertEquals("Should have one export statement", 1, exportStatements.size)
    }
}
