package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all function clauses in the file
        val functionClauses = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)
        assertNotNull(functionClauses, "Function clauses should not be null")
        assertEquals("There should be exactly one function clause", 1, functionClauses!!.size)

        val functionClause = functionClauses[0]
        assertTrue(functionClause is PicatFunctionFact, "Should be a PicatFunctionFact")
        val functionFact = functionClause as PicatFunctionFact

        // Verify that the function definition has the correct name and arity from its head
        val head = functionFact.getHead()
        assertNotNull(head, "Function clause should have a head")

        val structure = head.getStructure() // A function head must be a structure
        assertNotNull(structure, "Head should contain a structure")

        assertEquals("Function name should be 'factorial'", "factorial", structure!!.getAtom().text)
        val argList = structure.getArgumentList()
        assertEquals("Function arity should be 1", 1, argList?.getExpressionList()?.size ?: 0)

        // Verify that the function definition has an argument list from its head
        assertNotNull("Function should have an argument list", argList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        assertEquals("Argument list should have 1 argument", 1, arguments.size)

        // Verify that the argument has the correct expression
        assertEquals("First argument should be 0", "0", arguments[0].text)

        // Verify that the function definition has a body (expression for a fact)
        val body = functionFact.getExpression()
        assertNotNull("Function fact should have an expression (body)", body)
        assertEquals("Function body should be 1", "1", body?.text)
    }

    @Test
    fun testFunctionDefinitionWithMultipleArgumentsPsi() {
        // Test that a function definition with multiple arguments is correctly parsed
        val code = """
            custom_sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all function clauses in the file
        val functionClauses = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)
        assertNotNull(functionClauses, "Function clauses should not be null")
        assertEquals("There should be exactly one function clause", 1, functionClauses!!.size)

        val functionClause = functionClauses[0]
        assertTrue(functionClause is PicatFunctionFact, "Should be a PicatFunctionFact")
        val functionFact = functionClause as PicatFunctionFact

        // Verify that the function definition has the correct name and arity from its head
        val head = functionFact.getHead()
        assertNotNull(head, "Function clause should have a head")
        val structure = head.getStructure()
        assertNotNull(structure, "Head should contain a structure")

        assertEquals("Function name should be 'custom_sum'", "custom_sum", structure!!.getAtom().text)
        val argList = structure.getArgumentList()
        assertEquals("Function arity should be 3", 3, argList?.getExpressionList()?.size ?: 0)

        // Verify that the function definition has an argument list from its head
        assertNotNull("Function should have an argument list", argList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        assertEquals("Argument list should have 3 arguments", 3, arguments.size)

        // Verify that each argument has the correct expression
        assertEquals("First argument should be 1", "1", arguments[0].text)
        assertEquals("Second argument should be 2", "2", arguments[1].text)
        assertEquals("Third argument should be 3", "3", arguments[2].text)

        // Verify that the function definition has a body (expression for a fact)
        val body = functionFact.getExpression()
        assertNotNull("Function fact should have an expression (body)", body)
        assertEquals("Function body should be 6", "6", body?.text)
    }

    @Test
    fun testFunctionDefinitionWithComplexBodyPsi() {
        // Test that a function definition with a complex body is correctly parsed
        val code = """
            factorial(N) = N * factorial(N-1).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all function clauses in the file
        val functionClauses = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)
        assertNotNull(functionClauses, "Function clauses should not be null")
        assertEquals("There should be exactly one function clause", 1, functionClauses!!.size)

        val functionClause = functionClauses[0]
        assertTrue(functionClause is PicatFunctionRule, "Should be a PicatFunctionRule")
        val functionRule = functionClause as PicatFunctionRule

        // Verify that the function definition has the correct name and arity from its head
        val head = functionRule.getHead()
        assertNotNull(head, "Function clause should have a head")
        val structure = head.getStructure()
        assertNotNull(structure, "Head should contain a structure")

        assertEquals("Function name should be 'factorial'", "factorial", structure!!.getAtom().text)
        val argList = structure.getArgumentList()
        assertEquals("Function arity should be 1", 1, argList?.getExpressionList()?.size ?: 0)

        // Verify that the function definition has an argument list from its head
        assertNotNull("Function should have an argument list", argList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        assertEquals("Argument list should have 1 argument", 1, arguments.size)

        // Verify that the argument has the correct expression
        assertEquals("First argument should be N", "N", arguments[0].text)

        // Verify that the function definition has a body
        val body = functionRule.getFunctionBody() // This should return PicatFunctionBody
        assertNotNull("Function rule should have a body", body)

        // The body should be a complex expression, so we just check that it's not empty
        assertTrue("Function body should not be empty", body?.text?.isNotEmpty() ?: false)
    }

    @Test
    fun testMultipleFunctionDefinitionsPsi() {
        // Test that multiple function definitions are correctly parsed
        val code = """
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1).
            custom_sum(X, Y) = X + Y.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all function clauses in the file
        val functionClauses = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)
        assertNotNull(functionClauses, "Function clauses should not be null")
        assertEquals("There should be exactly three function clauses", 3, functionClauses!!.size)

        // Verify that each function definition has the correct name
        var headElement = PsiTreeUtil.getChildOfType(functionClauses[0], PicatHead::class.java)
        var structure = headElement as? PicatStructure
        assertEquals("First function name should be 'factorial'", "factorial", structure?.atom?.text)
        headElement = PsiTreeUtil.getChildOfType(functionClauses[1], PicatHead::class.java)
        structure = headElement as? PicatStructure
        assertEquals("Second function name should be 'factorial'", "factorial", structure?.atom?.text)
        headElement = PsiTreeUtil.getChildOfType(functionClauses[2], PicatHead::class.java)
        structure = headElement as? PicatStructure
        assertEquals("Third function name should be 'sum'", "custom_sum", structure?.atom?.text)

        // Verify that each function definition has the correct arity
        headElement = PsiTreeUtil.getChildOfType(functionClauses[0], PicatHead::class.java)
        structure = headElement as? PicatStructure
        assertEquals("First function arity should be 1", 1, structure?.argumentList?.expressionList?.size ?: 0)
        headElement = PsiTreeUtil.getChildOfType(functionClauses[1], PicatHead::class.java)
        structure = headElement as? PicatStructure
        assertEquals("Second function arity should be 1", 1, structure?.argumentList?.expressionList?.size ?: 0)
        headElement = PsiTreeUtil.getChildOfType(functionClauses[2], PicatHead::class.java)
        structure = headElement as? PicatStructure
        assertEquals("Third function arity should be 2", 2, structure?.argumentList?.expressionList?.size ?: 0)
    }

    @Test
    fun testFunctionBodyPsi() {
        // Test that the function body is correctly marked with the FUNCTION_BODY token type
        // Changed to use ASSIGN_OP (:=) to ensure it's a FunctionRule with a FunctionBody
        val code = """
            factorial(A) := 1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all function clauses in the file
        val functionClauses = PsiTreeUtil.getChildrenOfType(file, PicatFunctionClause::class.java)
        assertNotNull(functionClauses, "Function clauses should not be null")
        assertEquals("There should be exactly one function clause", 1, functionClauses!!.size)

        val functionClause = functionClauses[0]
        assertTrue(functionClause is PicatFunctionRule, "This test expects a rule with a FunctionBody due to ':='")

        val functionRule = functionClause as PicatFunctionRule
        // Verify that the function definition has a body
        val body = functionRule.getFunctionBody() // This should be PicatFunctionBody
        assertNotNull("Function rule should have a PicatFunctionBody", body)

        // Verify that the function body has the correct text (might be the text of the expression within FunctionBody)
        assertEquals(
            "Function body should be '1'",
            "1",
            body?.getExpression()?.text
        ) // Assuming PicatFunctionBody has getExpression()

        // Verify that the function body is of the correct type
        assertTrue("Function body should be a PicatFunctionBody", body is PicatFunctionBody)
    }
}
