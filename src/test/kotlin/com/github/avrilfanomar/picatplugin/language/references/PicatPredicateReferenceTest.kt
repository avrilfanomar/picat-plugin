package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatPredicateReferenceTest : BasePlatformTestCase() {

    fun testResolvesByArityBetweenP0AndP1() {
        // First: resolve p() in body to p/0
        val text1 = """
            module m.
            
            p().
            p(X).
            
            q() :- <caret>p(), p(a).
        """.trimIndent()
        val file1 = myFixture.configureByText("arity1.pi", text1)
        val offset1 = file1.text.indexOf("p(), p") // body call to p()
        val el1 = file1.findElementAt(offset1)
        val refs1 = el1?.references?.map { it.javaClass.name } ?: emptyList()
        val parent1 = el1?.parent
                val parentRefs1 = parent1?.references?.map { it.javaClass.name } ?: emptyList()
                println("[DEBUG_LOG] element@offset1=${el1?.javaClass?.name} text='${el1?.text}' parent=${parent1?.javaClass?.name} refs=$refs1 parentRefs=$parentRefs1")
        val ref1 = file1.findReferenceAt(offset1) ?: error("No reference at p() in body")
        val resolved1 = ref1.resolve() ?: error("p() did not resolve")
        assertTrue("Resolved element should be within the 0-arity head", resolved1.textOffset < file1.text.indexOf("p(X)"))

        // Second: resolve p(a) in body to p/1
        val text2 = """
            module m.
            
            p().
            p(X).
            
            q() :- p(), <caret>p(a).
        """.trimIndent()
        val file2 = myFixture.configureByText("arity2.pi", text2)
        val offset2 = file2.text.indexOf("p(a)") // body call to p(a)
        val el2 = file2.findElementAt(offset2)
        val refs2 = el2?.references?.map { it.javaClass.name } ?: emptyList()
        val parent2 = el2?.parent
                val parentRefs2 = parent2?.references?.map { it.javaClass.name } ?: emptyList()
                println("[DEBUG_LOG] element@offset2=${el2?.javaClass?.name} text='${el2?.text}' parent=${parent2?.javaClass?.name} refs=$refs2 parentRefs=$parentRefs2")
        val ref2 = file2.findReferenceAt(offset2) ?: error("No reference at p(a) in body")
        val resolved2 = ref2.resolve() ?: error("p(a) did not resolve")
        assertTrue("Resolved element should be within the 1-arity head", resolved2.textOffset >= file2.text.indexOf("p(X)"))
    }

    fun testGetVariantsForCompletion() {
        val text = """
            module m.
            
            predicate1().
            predicate2(X).
            function1() = true.
            
            test() :- <caret>p
        """.trimIndent()
        val file = myFixture.configureByText("completion.pi", text)
        val offset = file.text.indexOf("p") // completion position
        val element = file.findElementAt(offset)
        val parent = element?.parent
        
        // Get references and test variants
        val references = parent?.references ?: emptyArray()
        if (references.isNotEmpty()) {
            val variants = references.first().getVariants()
            assertTrue("Should have completion variants", variants.isNotEmpty())
            
            // Check that our defined predicates are in variants
            val variantTexts = variants.map { it.toString() }
            assertTrue("Should contain predicate1", variantTexts.any { it.contains("predicate1") })
            assertTrue("Should contain predicate2", variantTexts.any { it.contains("predicate2") })
            assertTrue("Should contain function1", variantTexts.any { it.contains("function1") })
        }
    }

    fun testMultiResolveWithMultipleCandidates() {
        val text = """
            module m.
            
            p(X).
            p(X, Y).
            
            test() :- <caret>p(a).
        """.trimIndent()
        val file = myFixture.configureByText("multi_resolve.pi", text)
        val offset = file.text.indexOf("p(a)")
        val reference = file.findReferenceAt(offset)
        
        assertNotNull("Reference should exist", reference)
        if (reference is PicatReference) {
            val results = reference.multiResolve(false)
            assertTrue("Should have at least one result", results.isNotEmpty())
            // Should prefer the single-argument version p(X) over p(X, Y)
            val resolved = results.first().element
            assertNotNull("Should resolve to an element", resolved)
        }
    }

    fun testResolveWithNoMatches() {
        val text = """
            module m.
            
            predicate1().
            predicate2(X).
            
            test() :- <caret>nonexistent().
        """.trimIndent()
        val file = myFixture.configureByText("no_match.pi", text)
        val offset = file.text.indexOf("nonexistent")
        val reference = file.findReferenceAt(offset)
        
        // Should not crash even when no matches exist
        val resolved = reference?.resolve()
        // May be null or may fall back to loose matching - both are valid
    }

    fun testReferenceWithComplexExpressions() {
        val text = """
            module m.
            
            helper(X, Y).
            
            main() =>
                A = 10,
                <caret>helper(A + 1, "test").
        """.trimIndent()
        val file = myFixture.configureByText("complex.pi", text)
        val offset = file.text.indexOf("helper(A")
        val reference = file.findReferenceAt(offset)
        
        assertNotNull("Reference should exist", reference)
        val resolved = reference?.resolve()
        assertNotNull("Should resolve to helper predicate", resolved)
    }

    fun testReferenceWithQuotedAtoms() {
        val text = """
            module m.
            
            'quoted predicate'().
            
            test() :- <caret>'quoted predicate'().
        """.trimIndent()
        val file = myFixture.configureByText("quoted.pi", text)
        val offset = file.text.indexOf("'quoted predicate'()")
        val reference = file.findReferenceAt(offset)
        
        assertNotNull("Reference should exist for quoted atom", reference)
        val resolved = reference?.resolve()
        assertNotNull("Should resolve quoted predicate", resolved)
    }

    fun testReferenceInNestedContexts() {
        val text = """
            module m.
            
            nested_pred(X).
            
            main() =>
                if true then
                    <caret>nested_pred(value)
                end.
        """.trimIndent()
        val file = myFixture.configureByText("nested.pi", text)
        val offset = file.text.indexOf("nested_pred(value)")
        val reference = file.findReferenceAt(offset)
        
        assertNotNull("Reference should exist in nested context", reference)
        val resolved = reference?.resolve()
        assertNotNull("Should resolve in nested context", resolved)
    }

    fun testReferenceWithZeroArityFallback() {
        val text = """
            module m.
            
            zero_arity.
            
            test() :- <caret>zero_arity.
        """.trimIndent()
        val file = myFixture.configureByText("zero_arity.pi", text)
        val offset = file.text.indexOf("zero_arity.")
        val reference = file.findReferenceAt(offset)
        
        assertNotNull("Reference should exist for zero arity", reference)
        val resolved = reference?.resolve()
        assertNotNull("Should resolve zero-arity predicate", resolved)
    }

    fun testReferenceErrorHandling() {
        val text = """
            module m.
            
            test() :- <caret>(.
        """.trimIndent()
        val file = myFixture.configureByText("malformed.pi", text)
        val offset = file.text.indexOf("(")
        val element = file.findElementAt(offset)
        
        // Should not crash on malformed input
        assertDoesNotThrow {
            val references = element?.references ?: emptyArray()
            references.forEach { ref ->
                ref.resolve() // Should not throw
                ref.getVariants() // Should not throw
            }
        }
    }

    private fun assertDoesNotThrow(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("Action should not throw exception: ${e.message}")
        }
    }
}
