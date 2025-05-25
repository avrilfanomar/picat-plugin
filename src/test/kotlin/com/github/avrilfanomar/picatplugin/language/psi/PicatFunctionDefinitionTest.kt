package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

/**
 * Test for Picat function definition PSI implementation.
 * This test verifies that function definitions are correctly parsed into PSI elements.
 */
class PicatFunctionDefinitionTest : BasePlatformTestCase() {

    @Test
    fun testSimpleFunctionDefinitionPsi() {
        // Test that a simple function definition is correctly parsed
        val code = """
            factorial(0) = 1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Debug: Print the number of function definitions found
        println("[DEBUG_LOG] Number of function definitions found: ${functionDefinitions.size}")
        functionDefinitions.forEach { functionDef ->
            println("[DEBUG_LOG] Function definition: ${functionDef.text}, Class: ${functionDef.javaClass.simpleName}")
        }

        // Verify that there is exactly one function definition
        assertEquals("There should be exactly one function definition", 1, functionDefinitions.size)

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals("Function name should be 'factorial'", "factorial", functionDef.getName())
        assertEquals("Function arity should be 1", 1, functionDef.getArity())

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull("Function should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals("Argument list should have 1 argument", 1, arguments.size)

        // Verify that the argument has the correct expression
        assertEquals("First argument should be 0", "0", arguments[0].getExpression()?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull("Function should have a body", body)
        assertEquals("Function body should be 1", "1", body?.text)
    }

    @Test
    fun testFunctionDefinitionWithMultipleArgumentsPsi() {
        // Test that a function definition with multiple arguments is correctly parsed
        val code = """
            sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there is exactly one function definition
        assertEquals("There should be exactly one function definition", 1, functionDefinitions.size)

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals("Function name should be 'sum'", "sum", functionDef.getName())
        assertEquals("Function arity should be 3", 3, functionDef.getArity())

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull("Function should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals("Argument list should have 3 arguments", 3, arguments.size)

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments[0].getExpression()?.text)
        assertEquals("Second argument should be 2", "2", arguments[1].getExpression()?.text)
        assertEquals("Third argument should be 3", "3", arguments[2].getExpression()?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull("Function should have a body", body)
        assertEquals("Function body should be 6", "6", body?.text)
    }

    @Test
    fun testFunctionDefinitionWithComplexBodyPsi() {
        // Test that a function definition with a complex body is correctly parsed
        val code = """
            factorial(N) = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there is exactly one function definition
        assertEquals("There should be exactly one function definition", 1, functionDefinitions.size)

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals("Function name should be 'factorial'", "factorial", functionDef.getName())
        assertEquals("Function arity should be 1", 1, functionDef.getArity())

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull("Function should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals("Argument list should have 1 argument", 1, arguments.size)

        // Verify that the argument has the correct expression
        assertEquals("First argument should be N", "N", arguments[0].getExpression()?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull("Function should have a body", body)
        
        // The body should be a complex expression, so we just check that it's not empty
        assertTrue("Function body should not be empty", body?.text?.isNotEmpty() ?: false)
        
        // Debug: Print the body text
        println("[DEBUG_LOG] Function body: ${body?.text}")
    }

    @Test
    fun testMultipleFunctionDefinitionsPsi() {
        // Test that multiple function definitions are correctly parsed
        val code = """
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1).
            sum(X, Y) = X + Y.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there are exactly three function definitions
        assertEquals("There should be exactly three function definitions", 3, functionDefinitions.size)

        // Verify that each function definition has the correct name
        assertEquals("First function name should be 'factorial'", "factorial", functionDefinitions[0].getName())
        assertEquals("Second function name should be 'factorial'", "factorial", functionDefinitions[1].getName())
        assertEquals("Third function name should be 'sum'", "sum", functionDefinitions[2].getName())

        // Verify that each function definition has the correct arity
        assertEquals("First function arity should be 1", 1, functionDefinitions[0].getArity())
        assertEquals("Second function arity should be 1", 1, functionDefinitions[1].getArity())
        assertEquals("Third function arity should be 2", 2, functionDefinitions[2].getArity())
    }
}