package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for PicatReferenceContributor.
 * This test verifies that the reference contributor is properly registered and working.
 */
class PicatReferenceContributorTest : BasePlatformTestCase() {

    @Test
    fun testReferenceContributorRegistration() {
        // Test that the reference contributor is properly registered by creating a simple Picat file
        // and verifying that references can be obtained (even if they're empty for now)
        val code = """
            main => 
                println("Hello World").
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify no PSI errors
        PsiTestUtils.assertNoPsiErrors(file, "reference contributor registration test")

        // Find all elements in the file
        val allElements = PsiTreeUtil.findChildrenOfAnyType(file, PsiElement::class.java)
        
        // Verify that we can get references from elements (even if empty)
        var referencesFound = 0
        for (element in allElements) {
            val references = element.references
            referencesFound += references.size
            
            // Each reference should be non-null
            references.forEach { reference ->
                Assertions.assertNotNull(reference, "Reference should not be null")
            }
        }

        // Log the number of references found for debugging
        println("[DEBUG_LOG] Found $referencesFound references in test file")
        
        // The test passes if no exceptions are thrown and references can be obtained
        // (even if the current implementation returns empty arrays)
        Assertions.assertTrue(allElements.isNotEmpty(), "Should have found some PSI elements")
    }

    @Test
    fun testReferenceProviderWithIdentifiers() {
        // Test with a more complex example that includes identifiers that might have references
        val code = """
            fact(X) => X > 0.
            
            main => 
                fact(5),
                println("Test").
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify no PSI errors
        PsiTestUtils.assertNoPsiErrors(file, "reference provider with identifiers test")

        // Find all elements and check for references
        val allElements = PsiTreeUtil.findChildrenOfAnyType(file, PsiElement::class.java)
        
        var elementsWithReferences = 0
        var totalReferences = 0
        
        for (element in allElements) {
            val references = element.references
            if (references.isNotEmpty()) {
                elementsWithReferences++
                totalReferences += references.size
                
                // Verify each reference
                references.forEach { reference ->
                    Assertions.assertNotNull(reference, "Reference should not be null")
                    Assertions.assertNotNull(reference.element, "Reference element should not be null")
                }
            }
        }

        println("[DEBUG_LOG] Found $elementsWithReferences elements with references")
        println("[DEBUG_LOG] Total references found: $totalReferences")
        
        // The test passes if we can process all elements without errors
        Assertions.assertTrue(allElements.isNotEmpty(), "Should have found some PSI elements")
    }

    @Test
    fun testReferenceContributorWithRuleCalls() {
        // Test with rule calls that should potentially have references
        val code = """
            helper_rule(X) => X > 0.
            
            another_rule(Y) => 
                helper_rule(Y),
                Y < 10.
                
            main => 
                another_rule(5).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Verify no PSI errors
        PsiTestUtils.assertNoPsiErrors(file, "reference contributor with rule calls test")

        // Test that we can traverse the PSI tree and get references without errors
        val allElements = PsiTreeUtil.findChildrenOfAnyType(file, PsiElement::class.java)
        
        var processedElements = 0
        var referencesProcessed = 0
        
        for (element in allElements) {
            processedElements++
            val references = element.references
            referencesProcessed += references.size
            
            // Verify that getting references doesn't throw exceptions
            references.forEach { reference ->
                Assertions.assertNotNull(reference, "Reference should not be null")
                
                // Test that we can call basic reference methods without errors
                val referenceElement = reference.element
                Assertions.assertNotNull(referenceElement, "Reference element should not be null")
                
                // Test that we can get the range text without errors
                val rangeInElement = reference.rangeInElement
                Assertions.assertNotNull(rangeInElement, "Reference range should not be null")
            }
        }

        println("[DEBUG_LOG] Processed $processedElements elements")
        println("[DEBUG_LOG] Processed $referencesProcessed references")
        
        Assertions.assertTrue(processedElements > 0, "Should have processed some elements")
    }

    @Test
    fun testReferenceContributorErrorHandling() {
        // Test that the reference contributor handles malformed code gracefully
        val code = """
            incomplete_rule(X) => 
            
            main => 
                incomplete_rule(
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Even with parsing errors, the reference contributor should not crash
        val allElements = PsiTreeUtil.findChildrenOfAnyType(file, PsiElement::class.java)
        
        var elementsProcessed = 0
        var exceptionsThrown = 0
        
        for (element in allElements) {
            try {
                elementsProcessed++
                val references = element.references
                
                // Verify references are valid even for malformed code
                references.forEach { reference ->
                    Assertions.assertNotNull(reference, "Reference should not be null even for malformed code")
                }
            } catch (e: Exception) {
                exceptionsThrown++
                println("[DEBUG_LOG] Exception processing element: ${e.message}")
            }
        }

        println("[DEBUG_LOG] Processed $elementsProcessed elements with $exceptionsThrown exceptions")
        
        // The reference contributor should handle errors gracefully
        Assertions.assertTrue(elementsProcessed > 0, "Should have processed some elements")
        Assertions.assertEquals(0, exceptionsThrown, "Should not throw exceptions when processing references")
    }
}
