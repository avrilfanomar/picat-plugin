package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for Picat argument list PSI implementation.
 * This test verifies that comma-separated arguments are correctly parsed into PSI elements.
 */
class PicatArgumentListTest : BasePlatformTestCase() {

    @Test
    fun testSingleArgumentPsi() {
        // Test that a function definition with a single argument is correctly parsed
        val code = """
            factorial(0) = 1.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        assertTrue(head is PicatStructure, "Head should be a structure")
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        assertEquals(structure.getName(, "Structure should have name factorial", "factorial"))
        assertEquals(1, structure.getArity(, "Structure should have arity 1"))

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be 0", "0")?.text)
    }

    @Test
    fun testMultipleArgumentsPsi() {
        // Test that a function definition with comma-separated arguments is correctly parsed
        val code = """
            sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        assertTrue(head is PicatStructure, "Head should be a structure")
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        assertEquals(structure.getName(, "Structure should have name sum", "sum"))
        assertEquals(3, structure.getArity(, "Structure should have arity 3"))

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be 1", "1")?.text)
        assertEquals(arguments[1].getExpression(, "Second argument should be 2", "2")?.text)
        assertEquals(arguments[2].getExpression(, "Third argument should be 3", "3")?.text)
    }
}
