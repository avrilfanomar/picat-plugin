package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for PicatPsiImplUtil delegation methods.
 * This test verifies that the delegation methods correctly forward to PicatPsiUtil.
 */
class PicatPsiImplUtilTest : BasePlatformTestCase() {

    @Test
    fun testGetNameDelegation() {
        // Test that getName correctly delegates to PicatPsiUtil
        val code = "test_atom(X, Y)."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertTrue("Should find at least one atom", atoms.isNotEmpty())
        
        val atom = atoms.first()
        val nameFromImpl = PicatPsiImplUtil.getName(atom)
        val nameFromUtil = PicatPsiUtil.getName(atom)
        
        assertEquals("PicatPsiImplUtil.getName should delegate to PicatPsiUtil", nameFromUtil, nameFromImpl)
        assertNotNull("Name should not be null", nameFromImpl)
    }

    @Test
    fun testGetNameWithNullAtom() {
        // Test null handling in getName
        val result = PicatPsiImplUtil.getName(null)
        assertNull("getName with null atom should return null", result)
    }

    @Test
    fun testSetNameDelegation() {
        // Test that setName correctly delegates to PicatPsiUtil
        val code = "test_atom(X, Y)."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertTrue("Should find at least one atom", atoms.isNotEmpty())
        
        val atom = atoms.first()
        val resultFromImpl = PicatPsiImplUtil.setName(atom, "new_name")
        val resultFromUtil = PicatPsiUtil.setName(atom, "new_name")
        
        assertEquals("PicatPsiImplUtil.setName should delegate to PicatPsiUtil", resultFromUtil, resultFromImpl)
        assertEquals("Both should return the same element", atom, resultFromImpl)
    }

    @Test
    fun testGetNameIdentifierDelegation() {
        // Test that getNameIdentifier correctly delegates to PicatPsiUtil
        val code = "test_atom(X, Y)."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertTrue("Should find at least one atom", atoms.isNotEmpty())
        
        val atom = atoms.first()
        val identifierFromImpl = PicatPsiImplUtil.getNameIdentifier(atom)
        val identifierFromUtil = PicatPsiUtil.getNameIdentifier(atom)
        
        assertEquals("PicatPsiImplUtil.getNameIdentifier should delegate to PicatPsiUtil", 
                    identifierFromUtil, identifierFromImpl)
    }

    @Test
    fun testDelegationConsistency() {
        // Test that all three methods consistently delegate for various atom types
        val testCodes = listOf(
            "simple_atom.",
            "atom_with_args(X, Y, Z).",
            "'quoted_atom'.",
            "min(X, Y).",  // keyword atom
            "max(A, B)."   // keyword atom
        )

        testCodes.forEach { code ->
            myFixture.configureByText("test.pi", code)
            val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
            
            if (atoms.isNotEmpty()) {
                val atom = atoms.first()
                
                // Test consistency between impl and util for all methods
                assertEquals("getName delegation should be consistent for: $code",
                           PicatPsiUtil.getName(atom), PicatPsiImplUtil.getName(atom))
                
                assertEquals("getNameIdentifier delegation should be consistent for: $code",
                           PicatPsiUtil.getNameIdentifier(atom), PicatPsiImplUtil.getNameIdentifier(atom))
                
                assertEquals("setName delegation should be consistent for: $code",
                           PicatPsiUtil.setName(atom, "test"), PicatPsiImplUtil.setName(atom, "test"))
            }
        }
    }

    @Test
    fun testEdgeCasesHandling() {
        // Test edge cases to ensure proper delegation
        val code = "complex_atom(nested(X, Y), [1,2,3], {a: b})."
        myFixture.configureByText("test.pi", code)

        val atoms = PsiTreeUtil.findChildrenOfType(myFixture.file, PicatAtom::class.java)
        assertTrue("Should find atoms in complex expression", atoms.isNotEmpty())
        
        atoms.forEach { atom ->
            // Ensure no exceptions are thrown and delegation works
            assertDoesNotThrow("getName should not throw for complex atoms") {
                PicatPsiImplUtil.getName(atom)
            }
            
            assertDoesNotThrow("getNameIdentifier should not throw for complex atoms") {
                PicatPsiImplUtil.getNameIdentifier(atom)
            }
            
            assertDoesNotThrow("setName should not throw for complex atoms") {
                PicatPsiImplUtil.setName(atom, "test_name")
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
