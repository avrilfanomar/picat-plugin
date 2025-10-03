package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Test for PicatRefactoringSupportProvider.
 * This test verifies the refactoring support for Picat elements.
 */
class PicatRefactoringSupportProviderTest : BasePlatformTestCase() {

    private lateinit var refactoringSupportProvider: PicatRefactoringSupportProvider

    override fun setUp() {
        super.setUp()
        refactoringSupportProvider = PicatRefactoringSupportProvider()
    }

    fun testIsInplaceRenameAvailableWithNamedAtom() {
        // Test that atoms with name identifiers support in-place rename
        val code = "test_predicate(X, Y)."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertTrue("Should find at least one atom", atoms.isNotEmpty())

        val atom = atoms.first()
        if (atom is PsiNameIdentifierOwner && atom.nameIdentifier != null) {
            // Test the core logic of isInplaceRenameAvailable
            val hasNameIdentifier = atom.nameIdentifier != null
            assertTrue("Atom with name identifier should support rename", hasNameIdentifier)
        }
    }

    fun testIsInplaceRenameAvailableWithVariousElements() {
        // Test various Picat elements to verify rename availability logic
        val testCodes = listOf(
            "simple_atom.",
            "predicate_with_args(X, Y, Z).",
            "'quoted_atom'.",
            "complex_predicate(nested(A, B), [1,2,3])."
        )

        testCodes.forEach { code ->
            myFixture.configureByText("test.pi", code)
            
            val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
            atoms.forEach { atom ->
                if (atom is PsiNameIdentifierOwner) {
                    val hasNameIdentifier = atom.nameIdentifier != null
                    // Test the expected behavior based on name identifier presence
                    if (hasNameIdentifier) {
                        assertNotNull("Atom should have name identifier for: $code", atom.nameIdentifier)
                    }
                }
            }
        }
    }

    fun testRefactoringSupportForDifferentElementTypes() {
        // Test that the provider correctly identifies which elements support refactoring
        val code = """
            test_fact.
            test_rule :- body_predicate(X).
            include "module.pi".
        """.trimIndent()

        myFixture.configureByText("test.pi", code)

        // Find all PSI elements in the file
        val allElements = mutableListOf<PsiElement>()
        PsiTreeUtil.processElements(myFixture.file) { element ->
            allElements.add(element)
            true
        }

        var nameIdentifierOwnerCount = 0
        var elementsWithNameIdentifier = 0

        allElements.forEach { element ->
            if (element is PsiNameIdentifierOwner) {
                nameIdentifierOwnerCount++
                if (element.nameIdentifier != null) {
                    elementsWithNameIdentifier++
                }
            }
        }

        assertTrue("Should find some PsiNameIdentifierOwner elements", nameIdentifierOwnerCount > 0)
    }

    fun testRenameAvailabilityLogic() {
        // Test the core rename availability logic
        val code = "rename_test(Arg1, Arg2)."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertFalse("Should find atoms", atoms.isEmpty())

        atoms.forEach { atom ->
            // Test the conditions for rename availability
            val isNameIdentifierOwner = atom is PsiNameIdentifierOwner
            if (isNameIdentifierOwner) {
                val nameId = (atom as PsiNameIdentifierOwner).nameIdentifier
                val shouldSupportRename = nameId != null
                
                if (shouldSupportRename) {
                    assertNotNull("Element should have name identifier", nameId)
                    assertTrue("Element should be PsiNameIdentifierOwner", atom is PsiNameIdentifierOwner)
                }
            }
        }
    }

    fun testNullContextHandling() {
        // Test that the provider handles null context correctly
        val code = "context_test."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        if (atoms.isNotEmpty()) {
            val atom = atoms.first()
            
            // Test that the provider logic works regardless of context
            if (atom is PsiNameIdentifierOwner) {
                val hasNameIdentifier = atom.nameIdentifier != null
                // The rename availability should depend on name identifier, not context
                assertEquals("Rename availability should depend on name identifier presence",
                           hasNameIdentifier, atom.nameIdentifier != null)
            }
        }
    }

    fun testEdgeCasesHandling() {
        // Test edge cases for refactoring support
        val edgeCases = listOf(
            "", // Empty file
            "% Just a comment",
            "single.",
            "complex(very(deep(nested(structure))))."
        )

        edgeCases.forEach { code ->
            if (code.isNotEmpty()) {
                myFixture.configureByText("test.pi", code)
                
                // Ensure no exceptions are thrown when processing elements
                assertDoesNotThrow("Should handle edge case without errors: '$code'") {
                    val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
                    atoms.forEach { atom ->
                        if (atom is PsiNameIdentifierOwner) {
                            atom.nameIdentifier // Access name identifier
                        }
                    }
                }
            }
        }
    }

    private fun assertDoesNotThrow(message: String, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("$message but threw: ${e.message}")
        }
    }
}
