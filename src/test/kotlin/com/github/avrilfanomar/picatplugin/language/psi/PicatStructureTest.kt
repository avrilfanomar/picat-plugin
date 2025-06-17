package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionClause // Changed import
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule // Added import
import com.intellij.psi.PsiElement // <<<< ADD THIS IMPORT
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test for Picat structure PSI implementation.
 * This test verifies that structures are correctly parsed into PSI elements.
 */
class PicatStructureTest : BasePlatformTestCase() {

    @Test
    fun testSimpleStructurePsi() {
        // Test that a simple structure is correctly parsed
        val code = """
            main => 
                X = point(1, 2),
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all structures in the file
        val structures = findStructuresInFile(file)

        // Verify that there are at least two structures (point and println)
        assertTrue(structures.size >= 2, "There should be at least two structures")

        // Find the point structure
        val pointStructure = structures.find { it.text.contains("point") }
        assertNotNull(pointStructure, "There should be a point structure")

        // Verify that the point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", pointStructure?.atom?.text)
        assertEquals(2, pointStructure?.argumentList?.expressionList?.size, "Structure arity should be 2")

        // Verify that the point structure has an argument list
        val argumentList = pointStructure?.argumentList // property access
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList?.expressionList // property access
        assertEquals(2, arguments?.size, "Argument list should have 2 arguments")

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments?.get(0)?.text) // Assuming argument itself is the expression, .text is fine
        assertEquals("Second argument should be 2", "2", arguments?.get(1)?.text)
    }

    @Test
    fun testNestedStructuresPsi() {
        // Test that nested structures are correctly parsed
        val code = """
            main => 
                X = point(1, point(2, 3)),
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all structures in the file
        val structures = findStructuresInFile(file)

        // Verify that there are at least three structures (outer point, inner point, and println)
        assertTrue(structures.size >= 3, "There should be at least three structures")

        // Find the outer point structure
        val outerPointStructure = structures.find {
            it.text.contains("point(2, 3)")
        }
        assertNotNull(outerPointStructure, "There should be an outer point structure")

        // Verify that the outer point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", outerPointStructure?.atom?.text)
        assertEquals(2, outerPointStructure?.argumentList?.expressionList?.size, "Structure arity should be 2")

        // Verify that the outer point structure has an argument list
        val outerArgumentList = outerPointStructure?.argumentList // property access
        assertNotNull(outerArgumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val outerArguments = outerArgumentList?.expressionList // Assuming PicatArgumentList has expressionList
        assertEquals(2, outerArguments?.size, "Argument list should have 2 arguments")

        // Verify that the second argument is a structure
        val secondArgExpression = outerArguments?.get(1) // Assuming argument itself is the expression
        assertNotNull(secondArgExpression, "Second argument should have an expression")

        // Find the inner point structure
        val innerPointStructure = structures.find {
            it.atom?.text == "point" && it.text == "point(2, 3)"
        }
        assertNotNull(innerPointStructure, "There should be an inner point structure")

        // Verify that the inner point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", innerPointStructure?.atom?.text)
        assertEquals(2, innerPointStructure?.argumentList?.expressionList?.size ?: 0, "Structure arity should be 2")
    }

    @Test
    fun testStructureWithVariablesPsi() {
        // Test that a structure with variables is correctly parsed
        val code = """
            main => 
                X = 1,
                Y = 2,
                Z = point(X, Y),
                println(Z).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all structures in the file
        val structures = findStructuresInFile(file)

        val pointStructure = structures.find { it.text.contains("point") }
        assertNotNull(pointStructure, "There should be a point structure")

        // Verify that the point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", pointStructure?.atom?.text)
        assertEquals(2, pointStructure?.argumentList?.expressionList?.size ?: 0, "Structure arity should be 2")

        // Verify that the point structure has an argument list
        val argumentList = pointStructure?.argumentList // property access
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList?.expressionList // Assuming PicatArgumentList has expressionList
        assertEquals(2, arguments?.size, "Argument list should have 2 arguments")

        // Verify that each argument has the correct expression
        assertEquals("First argument should be X", "X", arguments?.get(0)?.text) // Assuming argument is expression
        assertEquals("Second argument should be Y", "Y", arguments?.get(1)?.text)
    }

    @Test
    fun testStructureInFunctionCallPsi() {
        // Test that a structure used as an argument in a function call is correctly parsed
        val code = """
            main => 
                println(point(1, 2)).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all structures in the file
        val structures = findStructuresInFile(file)

        // Verify that there are at least two structures (println and point)
        assertTrue(structures.size == 2, "There should be at least two structures")

        // Find the point structure
        val pointStructure = structures.find { it.atom?.text == "point" }
        assertNotNull(pointStructure, "There should be a point structure")

        // Verify that the point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", pointStructure?.atom?.text)
        assertEquals(2, pointStructure?.argumentList?.expressionList?.size ?: 0, "Structure arity should be 2")

        // Find the println structure
        val printlnStructure = structures.find { it.atom?.text == "println" }
        assertNotNull(printlnStructure, "There should be a println structure")

        // Verify that the println structure has the correct name and arity
        assertEquals("Structure name should be 'println'", "println", printlnStructure?.atom?.text)
        assertEquals(1, printlnStructure?.argumentList?.expressionList?.size ?: 0, "Structure arity should be 1")
    }

    /**
     * Helper method to find all structures in a file.
     */
    private fun findStructuresInFile(file: PsiFile): List<PicatStructure> {
        // This is a recursive function to find all structures in the file
        val structures = mutableListOf<PicatStructure>()

        // First, find all direct structure children
        structures.addAll(PsiTreeUtil.getChildrenOfType(file, PicatStructure::class.java)?.toList() ?: emptyList())

        // Then, find all structures in rules
        val rules: Collection<PicatPredicateRule> = PsiTreeUtil.getChildrenOfType(file, PicatPredicateRule::class.java)?.toList() ?: emptyList()
        for (rule: PicatPredicateRule in rules) {
            // Find structures in the rule body
            val body = rule.body // property access
            if (body != null) {
                structures.addAll(PsiTreeUtil.getChildrenOfType(body, PicatStructure::class.java)?.toList() ?: emptyList())
            }

            // Find structures in the rule head
            val head = rule.head // property access
            if (head != null && head is PicatStructure) {
                structures.add(head)
            }
        }

        // Find structures in function definitions
        val functionDefs: Collection<PicatFunctionClause> = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)?.toList() ?: emptyList()
        for (functionDef: PicatFunctionClause in functionDefs) {
            val bodyElement: PsiElement? = when (functionDef) {
                is PicatFunctionRule -> functionDef.functionBody // property access
                is PicatFunctionFact -> functionDef.expression    // property access
                else -> null
            }
            if (bodyElement != null) {
                structures.addAll(PsiTreeUtil.getChildrenOfType(bodyElement, PicatStructure::class.java)?.toList() ?: emptyList())
            }
        }

        return structures
    }
}
