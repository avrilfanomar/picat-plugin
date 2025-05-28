package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Debug: Print the number of function definitions found
        println("[DEBUG_LOG] Number of function definitions found: ${functionDefinitions.size}")
        functionDefinitions.forEach { functionDef ->
            println("[DEBUG_LOG] Function definition: ${functionDef.text}, Class: ${functionDef.javaClass.simpleName}")
        }

        // Verify that there is exactly one function definition
        assertEquals(1, functionDefinitions.size, "There should be exactly one function definition")

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals(functionDef.getName(, "Function name should be 'factorial'", "factorial"))
        assertEquals(1, functionDef.getArity(, "Function arity should be 1"))

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull(argumentList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be 0", "0")?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull(body, "Function should have a body")
        assertEquals(body?.text, "Function body should be 1", "1")
    }

    @Test
    fun testFunctionDefinitionWithMultipleArgumentsPsi() {
        // Test that a function definition with multiple arguments is correctly parsed
        val code = """
            sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there is exactly one function definition
        assertEquals(1, functionDefinitions.size, "There should be exactly one function definition")

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals(functionDef.getName(, "Function name should be 'sum'", "sum"))
        assertEquals(3, functionDef.getArity(, "Function arity should be 3"))

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull(argumentList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be 1", "1")?.text)
        assertEquals(arguments[1].getExpression(, "Second argument should be 2", "2")?.text)
        assertEquals(arguments[2].getExpression(, "Third argument should be 3", "3")?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull(body, "Function should have a body")
        assertEquals(body?.text, "Function body should be 6", "6")
    }

    @Test
    fun testFunctionDefinitionWithComplexBodyPsi() {
        // Test that a function definition with a complex body is correctly parsed
        val code = """
            factorial(N) = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there is exactly one function definition
        assertEquals(1, functionDefinitions.size, "There should be exactly one function definition")

        // Verify that the function definition has the correct name and arity
        val functionDef = functionDefinitions[0]
        assertEquals(functionDef.getName(, "Function name should be 'factorial'", "factorial"))
        assertEquals(1, functionDef.getArity(, "Function arity should be 1"))

        // Verify that the function definition has an argument list
        val argumentList = functionDef.getArgumentList()
        assertNotNull(argumentList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be N", "N")?.text)

        // Verify that the function definition has a body
        val body = functionDef.getBody()
        assertNotNull(body, "Function should have a body")
        
        // The body should be a complex expression, so we just check that it's not empty
        assertTrue(body?.text?.isNotEmpty(, "Function body should not be empty") ?: false)
        
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all function definitions in the file
        val functionDefinitions = file.findChildrenByClass(PicatFunctionDefinition::class.java)

        // Verify that there are exactly three function definitions
        assertEquals(3, functionDefinitions.size, "There should be exactly three function definitions")

        // Verify that each function definition has the correct name
        assertEquals(functionDefinitions[0].getName(, "First function name should be 'factorial'", "factorial"))
        assertEquals(functionDefinitions[1].getName(, "Second function name should be 'factorial'", "factorial"))
        assertEquals(functionDefinitions[2].getName(, "Third function name should be 'sum'", "sum"))

        // Verify that each function definition has the correct arity
        assertEquals(1, functionDefinitions[0].getArity(, "First function arity should be 1"))
        assertEquals(1, functionDefinitions[1].getArity(, "Second function arity should be 1"))
        assertEquals(2, functionDefinitions[2].getArity(, "Third function arity should be 2"))
    }
}