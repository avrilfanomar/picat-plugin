package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions // Added import
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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all facts in the file
        val facts = file.getFunctionFacts()

        // Verify that there is exactly one fact
        Assertions.assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        Assertions.assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        Assertions.assertTrue(head is PicatStructure, "Head should be a structure")
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        Assertions.assertEquals("factorial", structure.atom.text, "Structure should have name 'factorial'")
        Assertions.assertEquals(1, structure.argumentList?.expressionList?.size, "Structure should have arity 1")

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        Assertions.assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.expressionList
        Assertions.assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        Assertions.assertEquals("0", arguments[0]?.text, "First argument should be 0")
    }

    @Test
    fun testMultipleArgumentsPsi() {
        // Test that a function definition with comma-separated arguments is correctly parsed
        val code = """
            custom_sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all facts in the file
        val facts = file.getFunctionFacts()

        // Verify that there is exactly one fact
        Assertions.assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        Assertions.assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        Assertions.assertTrue(head is PicatStructure, "Head should be a structure")
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        Assertions.assertEquals("custom_sum", structure.atom.text, "Structure should have name custom_sum")
        Assertions.assertEquals(3, structure.argumentList?.expressionList?.size, "Structure should have arity 3")

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        Assertions.assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.expressionList
        Assertions.assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        Assertions.assertEquals("1", arguments[0]?.text, "First argument should be 1")
        Assertions.assertEquals("2", arguments[1]?.text, "Second argument should be 2")
        Assertions.assertEquals("3", arguments[2]?.text, "Third argument should be 3")
    }
}
