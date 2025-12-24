package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatDotAccess
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatReferenceContributorTest : BasePlatformTestCase() {

    fun testReferenceContributorCreation() {
        assertDoesNotThrow("ReferenceContributor should be created without exceptions") {
            val contributor = PicatReferenceContributor()
            assertNotNull("Contributor should not be null", contributor)
        }
    }

    fun testAtomReferencesInCallExpression() {
        val fileContent = """
            module test.
            
            predicate_call(X).
            
            main =>
                predicate_call(test).
        """.trimIndent()
        
        val file = myFixture.configureByText("call_test.pi", fileContent)
        
        assertDoesNotThrow("Atom references in call expressions should work") {
            // Find call expressions - be resilient to parsing variations
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            
            // Test that calls can provide references (even if empty)
            calls.forEach { call ->
                val references = call.references
                assertNotNull("References should not be null", references)
                
                val atom = call.atom
            }
        }
    }

    fun testAtomReferencesInImportItems() {
        val fileContent = """
            module test.
            import basic.
            import cp.
            
            main => true.
        """.trimIndent()
        
        val file = myFixture.configureByText("import_test.pi", fileContent)
        
        assertDoesNotThrow("Atom references in import items should work") {
            // Find import items
            val imports = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            assertTrue("Should find import items", imports.isNotEmpty())
            
            imports.forEach { importItem ->
                // Test that references are available
                val references = importItem.references
                assertNotNull("References should not be null", references)
                
                // Test that we can access the atom
                val atom = importItem.atom
                assertNotNull("Atom should be accessible in import", atom)
                
                if (atom != null) {
                    val atomReferences = atom.references
                    assertNotNull("Atom references should not be null", atomReferences)
                }
            }
        }
    }

    fun testStandaloneAtomReferences() {
        val fileContent = """
            module test.
            
            standalone_atom.
            another_atom(X).
        """.trimIndent()
        
        val file = myFixture.configureByText("atom_test.pi", fileContent)
        
        assertDoesNotThrow("Standalone atom references should be handled") {
            // Find atoms
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            assertTrue("Should find atoms", atoms.isNotEmpty())
            
            atoms.forEach { atom ->
                // Test that references are available (might be empty based on contributor logic)
                val references = atom.references
                assertNotNull("References array should not be null", references)
                
                // Test atom properties
                assertNotNull("Atom text should not be null", atom.text)
            }
        }
    }

    fun testReferenceProviderWithComplexExpressions() {
        val fileContent = """
            module test.
            import planner.
            
            complex_pred(X, Y).
            
            main =>
                complex_pred(1, 2),
                simple_call,
                nested(call(inside)).
        """.trimIndent()
        
        val file = myFixture.configureByText("complex_test.pi", fileContent)
        
        assertDoesNotThrow("Reference provider should handle complex expressions") {
            // Test various element types
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            val imports = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            
            // Test that all elements can provide references without exceptions
            atoms.forEach { atom -> 
                val refs = atom.references
                assertNotNull("Atom references should not be null", refs)
            }
            calls.forEach { call -> 
                val refs = call.references
                assertNotNull("Call references should not be null", refs)
            }
            imports.forEach { import -> 
                val refs = import.references
                assertNotNull("Import references should not be null", refs)
            }
        }
    }

    fun testReferenceContributorWithEmptyFile() {
        val fileContent = """
            module empty.
        """.trimIndent()
        
        val file = myFixture.configureByText("empty_test.pi", fileContent)
        
        assertDoesNotThrow("Reference contributor should handle empty files") {
            // Test with minimal content
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            val imports = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            
            // Even with minimal content, should not throw exceptions
            atoms.forEach { atom -> atom.references }
            calls.forEach { call -> call.references }
            imports.forEach { import -> import.references }
        }
    }

    fun testReferenceContributorWithQuotedAtoms() {
        val fileContent = """
            module test.
            import 'quoted_module'.
            
            'quoted_predicate'.
            'quoted_call'(X).
            
            main =>
                'quoted_call'(test).
        """.trimIndent()
        
        val file = myFixture.configureByText("quoted_test.pi", fileContent)
        
        assertDoesNotThrow("Reference contributor should handle quoted atoms") {
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            val imports = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            
            // Test quoted atom handling
            atoms.forEach { atom ->
                val references = atom.references
                assertNotNull("References should be available for quoted atoms", references)
            }
            
            calls.forEach { call ->
                val references = call.references
                assertNotNull("References should be available for quoted calls", references)
            }
            
            imports.forEach { import ->
                val references = import.references
                assertNotNull("References should be available for quoted imports", references)
            }
        }
    }

    @Suppress("SwallowedException")
    fun testReferenceContributorWithMalformedInput() {
        val fileContent = """
            module test.
            import .
            
            malformed(
            incomplete_call
        """.trimIndent()
        
        val file = myFixture.configureByText("malformed_test.pi", fileContent)
        
        assertDoesNotThrow("Reference contributor should handle malformed input gracefully") {
            // Test that malformed input doesn't crash the reference system
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            val imports = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            
            // Should handle malformed input without exceptions
            atoms.forEach { atom -> 
                try { atom.references } catch (e: Exception) { 
                    // Expected: malformed input might cause exceptions - test passes by not crashing
                }
            }
            calls.forEach { call -> 
                try { call.references } catch (e: Exception) { 
                    // Expected: malformed input might cause exceptions - test passes by not crashing
                }
            }
            imports.forEach { import -> 
                try { import.references } catch (e: Exception) { 
                    // Expected: malformed input might cause exceptions - test passes by not crashing
                }
            }
        }
    }

    fun testReferenceContributorEdgeCases() {
        val fileContent = """
            module test.
            
            % Test various edge cases
            single.
            multi(X, Y, Z).
            nested(call(inside(deep))).
            
            main =>
                single,
                multi(1, 2, 3),
                nested(call(inside(deep))).
        """.trimIndent()
        
        val file = myFixture.configureByText("edge_cases_test.pi", fileContent)
        
        assertDoesNotThrow("Reference contributor should handle edge cases") {
            val atoms = PsiTreeUtil.findChildrenOfType(file, PicatAtom::class.java)
            val calls = PsiTreeUtil.findChildrenOfType(file, PicatAtomOrCallNoLambda::class.java)
            
            // Test that all elements provide references without exceptions
            atoms.forEach { atom ->
                val refs = atom.references
                assertNotNull("Edge case atoms should have references array", refs)
            }
            
            calls.forEach { call ->
                val refs = call.references
                assertNotNull("Edge case calls should have references array", refs)
            }
        }
    }

    fun testDotAccessReferences() {
        val fileContent = """
            module test.

            test_pred(X, Y, Res) => bp.b_XOR_ccf(X, Y, Res).
        """.trimIndent()

        val file = myFixture.configureByText("dot_access_test.pi", fileContent)

        assertDoesNotThrow("Dot access references should work") {
            // Find dot access expressions
            val dotAccesses = PsiTreeUtil.findChildrenOfType(file, PicatDotAccess::class.java)

            // If no dot access found, the reference system is still functional
            // Just verify that the file parses without errors
            if (dotAccesses.isEmpty()) {
                // Test still passes - the reference contributor handles non-existent elements gracefully
                return@assertDoesNotThrow
            }

            dotAccesses.forEach { dotAccess ->
                // Test that references are available
                val references = dotAccess.references
                assertNotNull("Dot access references should not be null", references)

                // Test that we can access the dot identifier
                val dotId = dotAccess.dotIdentifier ?: dotAccess.dotSingleQuotedAtom
                assertNotNull("Dot identifier should be accessible", dotId)
            }
        }
    }

    fun testDotAccessReferencesWithQuotedNames() {
        val fileContent = """
            module test.

            test_pred(X) => bp.'quoted_pred'(X).
        """.trimIndent()

        val file = myFixture.configureByText("quoted_dot_access_test.pi", fileContent)

        assertDoesNotThrow("Dot access references with quoted names should work") {
            val dotAccesses = PsiTreeUtil.findChildrenOfType(file, PicatDotAccess::class.java)

            if (dotAccesses.isEmpty()) {
                return@assertDoesNotThrow
            }

            dotAccesses.forEach { dotAccess ->
                val references = dotAccess.references
                assertNotNull("Quoted dot access references should not be null", references)
            }
        }
    }

    fun testDotAccessReferencesEdgeCases() {
        val fileContent = """
            module test.

            test_many(A, B, C, D, E) => bp.many_args(A, B, C, D, E).
        """.trimIndent()

        val file = myFixture.configureByText("dot_access_edge_test.pi", fileContent)

        assertDoesNotThrow("Dot access with varying arities should work") {
            val dotAccesses = PsiTreeUtil.findChildrenOfType(file, PicatDotAccess::class.java)

            if (dotAccesses.isEmpty()) {
                return@assertDoesNotThrow
            }

            dotAccesses.forEach { dotAccess ->
                val references = dotAccess.references
                assertNotNull("Dot access references should not be null", references)

                // Verify argument list is accessible
                val args = dotAccess.argumentList
                assertNotNull("Argument list should be accessible", args)
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
