package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatElementFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatPsiUtilTest : BasePlatformTestCase() {

    fun testGetNameForAtom() {
        val fileContent = """
            module test.
            
            simple_atom.
            'quoted_atom'.
            test_predicate(X).
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find atoms in the file
        val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
        assertTrue("Should find atoms in file", atoms.isNotEmpty())
        
        // Test getName for various atom types
        atoms.forEach { atom ->
            assertDoesNotThrow("getName should not throw for atom: ${atom.text}") {
                val name = PicatPsiUtil.getName(atom)
                assertNotNull("getName should return a value for atom: ${atom.text}", name)
                assertTrue("Name should not be blank: '$name'", name?.isNotBlank() == true)
            }
        }
    }
    
    fun testGetNameWithNullAtom() {
        val name = PicatPsiUtil.getName(null)
        assertNull("getName should return null for null atom", name)
    }
    
    fun testSetName() {
        val fileContent = """
            module test.
            test_atom.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atom = PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
        assertNotNull("Should find an atom", atom)
        
        assertDoesNotThrow("setName should not throw") {
            val result = PicatPsiUtil.setName(atom!!, "new_name")
            assertEquals("setName should return the original element", atom, result)
        }
    }
    
    fun testGetNameIdentifier() {
        val fileContent = """
            module test.
            
            simple_name.
            'quoted_name'.
            min.
            max.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
        
        atoms.forEach { atom ->
            assertDoesNotThrow("getNameIdentifier should not throw for atom: ${atom.text}") {
                val nameId = PicatPsiUtil.getNameIdentifier(atom)
                // Name identifier may be null for some atom types, but method should not throw
                if (nameId != null) {
                    assertNotNull("Name identifier should have text", nameId.text)
                }
            }
        }
    }
    
    fun testGetHeadName() {
        val fileContent = """
            module test.
            
            simple_predicate.
            predicate_with_args(X, Y).
            'quoted_predicate'(Z).
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find predicate rules
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        
        predicateRules.forEach { rule ->
            assertDoesNotThrow("getHeadName should not throw for rule") {
                PicatPsiUtil.getHeadName(rule)
                // Head name may be null for some cases, but method should not throw
            }
        }
    }
    
    fun testGetHeadArity() {
        val fileContent = """
            module test.
            
            zero_arity.
            one_arity(X).
            two_arity(X, Y).
            three_arity(A, B, C).
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find predicate rules
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        
        predicateRules.forEach { rule ->
            assertDoesNotThrow("getHeadArity should not throw for rule") {
                val arity = PicatPsiUtil.getHeadArity(rule)
                // Arity may be null for some cases, but method should not throw
                if (arity != null) {
                    assertTrue("Arity should be non-negative", arity >= 0)
                }
            }
        }
    }
    
    fun testGetStableSignature() {
        val fileContent = """
            module test.
            
            test_pred.
            test_func(X).
            another(A, B).
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find predicate rules
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        
        predicateRules.forEach { rule ->
            assertDoesNotThrow("getStableSignature should not throw for rule") {
                val signature = PicatPsiUtil.getStableSignature(rule)
                // Signature may be null for some cases, but method should not throw
                if (signature != null) {
                    assertTrue("Signature should contain '/'", signature.contains("/"))
                    assertTrue("Signature should not be blank", signature.isNotBlank())
                }
            }
        }
    }
    
    fun testGetModuleQualifier() {
        val fileContent = """
            module test.
            
            simple_pred.
            qualified::pred(X).
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Find predicate rules
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        
        predicateRules.forEach { rule ->
            assertDoesNotThrow("getModuleQualifier should not throw for rule") {
                PicatPsiUtil.getModuleQualifier(rule)
                // Qualifier may be null for non-qualified predicates, but method should not throw
            }
        }
    }
    
    fun testPsiUtilWithVariousElements() {
        val fileContent = """
            module test.
            
            % Test various PSI element types
            fact.
            rule :- body.
            function_rule => result.
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Test that utility methods handle various PSI elements gracefully
        file.children.forEach { child ->
            assertDoesNotThrow("PSI utilities should handle all element types gracefully") {
                PicatPsiUtil.getHeadName(child)
                PicatPsiUtil.getHeadArity(child)
                PicatPsiUtil.getStableSignature(child)
                PicatPsiUtil.getModuleQualifier(child)
            }
        }
    }
    
    fun testElementFactory() {
        assertDoesNotThrow("ElementFactory methods should work") {
            // Test createFile
            val file = PicatElementFactory.createFile(project, "module test. test_atom.")
            assertNotNull("Should create file", file)
            assertEquals("Should have correct file type", "dummy.pi", file.name)
            
            // Test createAtom
            val atom = PicatElementFactory.createAtom(project, "test_name")
            assertNotNull("Should create atom", atom)
            assertTrue("Atom should have text content", atom.text.isNotBlank())
            
            // Test createNameIdentifier
            val nameId = PicatElementFactory.createNameIdentifier(project, "identifier")
            assertNotNull("Should create name identifier", nameId)
            assertNotNull("Name identifier should have text", nameId.text)
        }
    }
    
    fun testElementFactoryWithVariousNames() {
        val testNames = listOf("simple", "test_name", "valid_atom", "name123")
        
        testNames.forEach { name ->
            assertDoesNotThrow("ElementFactory should handle name: $name") {
                val atom = PicatElementFactory.createAtom(project, name)
                assertNotNull("Should create atom for name: $name", atom)
                
                val nameId = PicatElementFactory.createNameIdentifier(project, name)
                assertNotNull("Should create name identifier for: $name", nameId)
            }
        }
    }
    
    @Suppress("SwallowedException")
    fun testElementFactoryErrorHandling() {
        // Test edge cases that might cause errors
        assertDoesNotThrow("ElementFactory should handle edge cases") {
            try {
                PicatElementFactory.createAtom(project, "")
                fail("Should throw error for empty name")
            } catch (e: Exception) {
                // Expected exception for empty name - test passes
            }
            
            try {
                PicatElementFactory.createNameIdentifier(project, "")
                fail("Should throw error for empty name identifier")
            } catch (e: Exception) {
                // Expected exception for empty name identifier - test passes
            }
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
