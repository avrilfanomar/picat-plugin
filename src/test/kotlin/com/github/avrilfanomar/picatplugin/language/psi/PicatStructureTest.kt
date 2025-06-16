package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
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
        assertEquals("Structure name should be 'point'", "point", pointStructure?.getName())
        assertEquals(2, pointStructure?.getArity(), "Structure arity should be 2")

        // Verify that the point structure has an argument list
        val argumentList = pointStructure?.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(2, arguments.size, "Argument list should have 2 arguments")

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments[0].getExpression()?.text)
        assertEquals("Second argument should be 2", "2", arguments[1].getExpression()?.text)
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
        assertEquals("Structure name should be 'point'", "point", outerPointStructure?.getName())
        assertEquals(2, outerPointStructure?.getArity(), "Structure arity should be 2")

        // Verify that the outer point structure has an argument list
        val outerArgumentList = outerPointStructure?.getArgumentList()
        assertNotNull(outerArgumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val outerArguments = outerArgumentList!!.getArguments()
        assertEquals(2, outerArguments.size, "Argument list should have 2 arguments")

        // Verify that the second argument is a structure
        val secondArgExpression = outerArguments[1].getExpression()
        assertNotNull(secondArgExpression, "Second argument should have an expression")

        // Find the inner point structure
        val innerPointStructure = structures.find {
            it.getName() == "point" && it.text == "point(2, 3)"
        }
        assertNotNull(innerPointStructure, "There should be an inner point structure")

        // Verify that the inner point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", innerPointStructure?.getName())
        assertEquals(2, innerPointStructure?.getArity(), "Structure arity should be 2")
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
        assertEquals("Structure name should be 'point'", "point", pointStructure?.getName())
        assertEquals(2, pointStructure?.getArity(), "Structure arity should be 2")

        // Verify that the point structure has an argument list
        val argumentList = pointStructure?.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(2, arguments.size, "Argument list should have 2 arguments")

        // Verify that each argument has the correct expression
        assertEquals("First argument should be X", "X", arguments[0].getExpression()?.text)
        assertEquals("Second argument should be Y", "Y", arguments[1].getExpression()?.text)
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
        val pointStructure = structures.find { it.getName() == "point" }
        assertNotNull(pointStructure, "There should be a point structure")

        // Verify that the point structure has the correct name and arity
        assertEquals("Structure name should be 'point'", "point", pointStructure?.getName())
        assertEquals(2, pointStructure?.getArity(), "Structure arity should be 2")

        // Find the println structure
        val printlnStructure = structures.find { it.getName() == "println" }
        assertNotNull(printlnStructure, "There should be a println structure")

        // Verify that the println structure has the correct name and arity
        assertEquals("Structure name should be 'println'", "println", printlnStructure?.getName())
        assertEquals(1, printlnStructure?.getArity(), "Structure arity should be 1")
    }

    /**
     * Helper method to find all structures in a file.
     */
    private fun findStructuresInFile(file: PsiFile): List<PicatStructure> {
        // This is a recursive function to find all structures in the file
        val structures = mutableListOf<PicatStructure>()

        // First, find all direct structure children
        structures.addAll(file.findChildrenByClass(PicatStructure::class.java))

        // Then, find all structures in rules
        val rules = file.findChildrenByClass(PicatRule::class.java)
        for (rule in rules) {
            // Find structures in the rule body
            val body = rule.getBody()
            if (body != null) {
                structures.addAll(PsiTreeUtil.findChildrenOfType(body, PicatStructure::class.java))
            }

            // Find structures in the rule head
            val head = rule.getHead()
            if (head != null && head is PicatStructure) {
                structures.add(head)
            }
        }

        // Find structures in function definitions
        val functionDefs = file.findChildrenByClass(PicatFunctionDefinition::class.java)
        for (functionDef in functionDefs) {
            // Find structures in the function body
            val body = functionDef.getBody()
            if (body != null) {
                structures.addAll(PsiTreeUtil.findChildrenOfType(body, PicatStructure::class.java))
            }
        }

        return structures
    }
}
