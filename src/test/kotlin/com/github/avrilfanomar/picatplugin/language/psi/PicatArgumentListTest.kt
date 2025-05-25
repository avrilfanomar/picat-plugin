package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)

        // Verify that there is exactly one fact
        assertEquals("There should be exactly one fact", 1, facts.size)

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull("Fact should have a head", head)

        // Verify that the head is a structure
        assertTrue("Head should be a structure", head is PicatStructure)
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        assertEquals("Structure should have name factorial", "factorial", structure.getName())
        assertEquals("Structure should have arity 1", 1, structure.getArity())

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull("Structure should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals("Argument list should have 1 argument", 1, arguments.size)

        // Verify that the argument has the correct expression
        assertEquals("First argument should be 0", "0", arguments[0].getExpression()?.text)
    }

    @Test
    fun testMultipleArgumentsPsi() {
        // Test that a function definition with comma-separated arguments is correctly parsed
        val code = """
            sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)

        // Verify that there is exactly one fact
        assertEquals("There should be exactly one fact", 1, facts.size)

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull("Fact should have a head", head)

        // Verify that the head is a structure
        assertTrue("Head should be a structure", head is PicatStructure)
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        assertEquals("Structure should have name sum", "sum", structure.getName())
        assertEquals("Structure should have arity 3", 3, structure.getArity())

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull("Structure should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals("Argument list should have 3 arguments", 3, arguments.size)

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments[0].getExpression()?.text)
        assertEquals("Second argument should be 2", "2", arguments[1].getExpression()?.text)
        assertEquals("Third argument should be 3", "3", arguments[2].getExpression()?.text)
    }
}
