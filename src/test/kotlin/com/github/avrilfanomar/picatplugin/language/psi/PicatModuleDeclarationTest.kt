package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.language.psi.* // Import all PSI interfaces
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.*
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
        val file = myFixture.file as PicatFileImpl

        // Find all module declarations in the file
        val moduleDeclarations = PsiTreeUtil.findChildrenOfType(file, PicatModuleDeclaration::class.java)

        // Verify that there is exactly one module declaration
        assertEquals(1, moduleDeclarations.size, "There should be exactly one module declaration")

        // Verify that the module declaration has the correct name
        val moduleDeclaration = moduleDeclarations.first()
        val moduleName = moduleDeclaration.moduleDeclarationContent?.moduleName?.atom?.text
        assertEquals("example", moduleName, "Module name should be 'example'")
    }

    @Test
    fun testModuleDeclarationWithImportExportPsi() {
        // Test that a module declaration with import and export statements is correctly parsed
        // Assuming PicatExportStatement is a valid PSI class based on the original test.
        // If not, the PicatExportStatement related lines would also need adjustment based on its actual BNF and PSI class.
        val code = """
            module example.

            import util.
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all module declarations in the file
        val moduleDeclarations = PsiTreeUtil.findChildrenOfType(file, PicatModuleDeclaration::class.java)

        // Verify that there is exactly one module declaration
        assertEquals(1, moduleDeclarations.size, "There should be exactly one module declaration")

        // Verify that the module declaration has the correct name
        val moduleDeclaration = moduleDeclarations.first()
        val moduleName = moduleDeclaration.moduleDeclarationContent?.moduleName?.atom?.text
        assertEquals("example", moduleName, "Module name should be 'example'")

        // Verify that import statements are parsed correctly
        val importStatements = PsiTreeUtil.findChildrenOfType(file, PicatImportStatement::class.java)
        assertEquals(1, importStatements.size, "Should have one import statement")

        val firstImport = importStatements.first()
        val importedModuleName = firstImport.importList?.importItemList?.firstOrNull()?.moduleName?.atom?.text
        assertEquals("util", importedModuleName, "Imported module should be 'utils'")


        // Verify export statements (assuming PicatExportStatement exists and is a direct child of the file)
        // The BNF for export_clause was not given, so this is a best guess based on original test structure
        val exportStatements = PsiTreeUtil.findChildrenOfType(file, PicatExportStatement::class.java)
        assertEquals(1, exportStatements.size, "Should have one export statement")
        // Further assertions on exportStatements content would depend on PicatExportStatement's PSI structure
    }
}
