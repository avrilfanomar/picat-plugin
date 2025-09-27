package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.language.references.PicatImportModuleReference
import com.github.avrilfanomar.picatplugin.language.references.PicatReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatAtomMixinTest : BasePlatformTestCase() {

    fun testGetNameDelegatesCorrectly() {
        val fileContent = """
            module test.
            simple_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("getName should work") {
            val name = atom!!.getName()
            assertNotNull("Name should not be null", name)
            assertTrue("Name should not be blank", name!!.isNotBlank())
        }
    }

    fun testSetNameDelegatesCorrectly() {
        val fileContent = """
            module test.
            test_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("setName should work") {
            val result = atom!!.setName("new_name")
            assertEquals("setName should return the atom", atom, result)
        }
    }

    fun testGetNameIdentifierDelegatesCorrectly() {
        val fileContent = """
            module test.
            identifier_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("getNameIdentifier should work") {
            atom!!.getNameIdentifier()
            // May be null for some atom types, but should not throw
        }
    }

    fun testGetReferencesForRegularAtom() {
        val fileContent = """
            module test.
            regular_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("getReferences should work for regular atom") {
            val references = atom!!.getReferences()
            assertNotNull("References should not be null", references)
            
            if (references.isNotEmpty()) {
                // Should be PicatReference for non-import atoms
                val firstRef = references.first()
                assertTrue("Should be PicatReference for regular atom", 
                    firstRef is PicatReference)
            }
        }
    }

    fun testGetReferencesForImportAtom() {
        val fileContent = """
            module test.
            import basic.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find import item and then its atom
        val importItem = PsiTreeUtil.findChildOfType(file, PicatImportItem::class.java)
        if (importItem != null) {
            val atom = PsiTreeUtil.findChildOfType(importItem, PicatAtom::class.java)
            if (atom != null) {
                assertDoesNotThrow("getReferences should work for import atom") {
                    val references = atom.getReferences()
                    assertNotNull("References should not be null", references)
                    
                    if (references.isNotEmpty()) {
                        val firstRef = references.first()
                        assertTrue("Should be PicatImportModuleReference for import atom", 
                            firstRef is PicatImportModuleReference)
                    }
                }
            }
        }
    }

    fun testGetReferenceReturnsFirstReference() {
        val fileContent = """
            module test.
            some_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("getReference should work") {
            val reference = atom!!.getReference()
            val references = atom.getReferences()
            
            if (references.isNotEmpty()) {
                // Should return same type as first reference (may not be same instance)
                assertNotNull("getReference should not be null when references exist", reference)
                assertEquals("getReference should have same class as first reference", 
                    references.first().javaClass, reference!!.javaClass)
            } else {
                assertNull("getReference should be null when no references", reference)
            }
        }
    }

    fun testGetReferencesWithNoNameIdentifier() {
        val fileContent = """
            module test.
            test_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        
        if (atom != null) {
            assertDoesNotThrow("getReferences should handle atoms gracefully") {
                val references = atom.getReferences()
                assertNotNull("References should not be null", references)
                // References array may be empty if no name identifier
            }
        }
    }

    fun testMultipleAtomsHaveIndependentReferences() {
        val fileContent = """
            module test.
            atom1.
            atom2.
            import basic.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
        
        assertTrue("Should find multiple atoms", atoms.size >= 2)
        
        assertDoesNotThrow("Multiple atoms should have independent references") {
            val atomsWithRefs = atoms.filter { it.getReferences().isNotEmpty() }
            
            // Each atom should have its own reference instances
            if (atomsWithRefs.size >= 2) {
                val ref1 = atomsWithRefs[0].getReferences().first()
                val ref2 = atomsWithRefs[1].getReferences().first()
                assertNotSame("References should be different instances", ref1, ref2)
            }
        }
    }

    fun testReferenceTextRangeCalculation() {
        val fileContent = """
            module test.
            test_identifier.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("Reference text range should be calculated") {
            val references = atom!!.getReferences()
            
            if (references.isNotEmpty()) {
                val reference = references.first()
                val range = reference.getRangeInElement()
                assertNotNull("Range should not be null", range)
                assertTrue("Range should have valid start", range.startOffset >= 0)
                assertTrue("Range should have valid end", range.endOffset >= range.startOffset)
            }
        }
    }

    fun testAtomMixinInheritance() {
        val fileContent = """
            module test.
            inheritance_test.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("Atom should inherit from PicatAtom interface") {
            assertTrue("Atom should implement PicatAtom", atom is PicatAtom)
            
            // Test that all interface methods are accessible
            atom!!.getName()
            atom.getNameIdentifier()
            atom.getReferences()
            atom.getReference()
        }
    }

    private fun assertDoesNotThrow(message: String, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("$message - Exception: ${e.message}")
        }
    }
}
