package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiElement
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
        val facts = PsiTreeUtil.findChildrenOfType(file as PsiElement, PicatPredicateFact::class.java)

        // Verify that there is exactly one fact
        Assertions.assertEquals(
            1,
            facts.size,
            "There should be exactly one fact"
        )

        val fact = facts.first()
        val head = fact.head
        Assertions.assertNotNull(head, "Fact should have a head")
    }


    @Test
    fun testIncludeStatementPsi() {
        // Test that an include statement is correctly parsed into a PicatIncludeDeclaration PSI element
        val code = """
            include "utils.pi".
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all include statements in the file
        val includeStatements = PsiTreeUtil.findChildrenOfType(file as PsiElement, PicatIncludeDeclaration::class.java)

        // Verify that there is exactly one include statement
        Assertions.assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements.first()
        val path = includeStatement.text
        Assertions.assertEquals("include \"utils.pi\".", path, "Include statement should have path utils.pi")
    }

}
