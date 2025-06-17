package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions // Consolidated import
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
        Assertions.assertNotNull(functionClauses, "Function clauses should not be null")
        Assertions.assertEquals(1, functionClauses!!.size, "There should be exactly one function clause")

        val functionClause = functionClauses[0]
        Assertions.assertTrue(functionClause is PicatFunctionFact, "Should be a PicatFunctionFact")
        val functionFact = functionClause as PicatFunctionFact

        // Verify that the function definition has the correct name and arity from its head
        val head = functionFact.getHead()
        Assertions.assertNotNull(head, "Function clause should have a head")

        val structure = head.getStructure() // A function head must be a structure
        Assertions.assertNotNull(structure, "Head should contain a structure")

        Assertions.assertEquals("factorial", structure!!.getAtom().text, "Function name should be 'factorial'")
        val argList = structure.getArgumentList()
        Assertions.assertEquals(1, argList?.getExpressionList()?.size ?: 0, "Function arity should be 1")

        // Verify that the function definition has an argument list from its head
        Assertions.assertNotNull(argList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        Assertions.assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        Assertions.assertEquals("0", arguments[0].text, "First argument should be 0")

        // Verify that the function definition has a body (expression for a fact)
        val body = functionFact.getExpression()
        Assertions.assertNotNull(body, "Function fact should have an expression (body)")
        Assertions.assertEquals("1", body?.text, "Function body should be 1")
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
        Assertions.assertNotNull(functionClauses, "Function clauses should not be null")
        Assertions.assertEquals(1, functionClauses!!.size, "There should be exactly one function clause")

        val functionClause = functionClauses[0]
        Assertions.assertTrue(functionClause is PicatFunctionFact, "Should be a PicatFunctionFact")
        val functionFact = functionClause as PicatFunctionFact

        // Verify that the function definition has the correct name and arity from its head
        val head = functionFact.getHead()
        Assertions.assertNotNull(head, "Function clause should have a head")
        val structure = head.getStructure()
        Assertions.assertNotNull(structure, "Head should contain a structure")

        Assertions.assertEquals("custom_sum", structure!!.getAtom().text, "Function name should be 'custom_sum'")
        val argList = structure.getArgumentList()
        Assertions.assertEquals(3, argList?.getExpressionList()?.size ?: 0, "Function arity should be 3")

        // Verify that the function definition has an argument list from its head
        Assertions.assertNotNull(argList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        Assertions.assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        Assertions.assertEquals("1", arguments[0].text, "First argument should be 1")
        Assertions.assertEquals("2", arguments[1].text, "Second argument should be 2")
        Assertions.assertEquals("3", arguments[2].text, "Third argument should be 3")

        // Verify that the function definition has a body (expression for a fact)
        val body = functionFact.getExpression()
        Assertions.assertNotNull(body, "Function fact should have an expression (body)")
        Assertions.assertEquals("6", body?.text, "Function body should be 6")
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
        Assertions.assertNotNull(functionClauses, "Function clauses should not be null")
        Assertions.assertEquals(1, functionClauses!!.size, "There should be exactly one function clause")

        val functionClause = functionClauses[0]
        Assertions.assertTrue(functionClause is PicatFunctionRule, "Should be a PicatFunctionRule")
        val functionRule = functionClause as PicatFunctionRule

        // Verify that the function definition has the correct name and arity from its head
        val head = functionRule.getHead()
        Assertions.assertNotNull(head, "Function clause should have a head")
        val structure = head.getStructure()
        Assertions.assertNotNull(structure, "Head should contain a structure")

        Assertions.assertEquals("factorial", structure!!.getAtom().text, "Function name should be 'factorial'")
        val argList = structure.getArgumentList()
        Assertions.assertEquals(1, argList?.getExpressionList()?.size ?: 0, "Function arity should be 1")

        // Verify that the function definition has an argument list from its head
        Assertions.assertNotNull(argList, "Function should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argList!!.getExpressionList()
        Assertions.assertEquals(1, arguments.size, "Argument list should have 1 argument")

        // Verify that the argument has the correct expression
        Assertions.assertEquals("N", arguments[0].text, "First argument should be N")

        // Verify that the function definition has a body
        val body = functionRule.getFunctionBody() // This should return PicatFunctionBody
        Assertions.assertNotNull(body, "Function rule should have a body")

        // The body should be a complex expression, so we just check that it's not empty
        Assertions.assertTrue(body?.text?.isNotEmpty() ?: false, "Function body should not be empty")
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
        Assertions.assertNotNull(functionClauses, "Function clauses should not be null")
        Assertions.assertEquals(3, functionClauses!!.size, "There should be exactly three function clauses")

        // Verify that each function definition has the correct name
        var headElement = PsiTreeUtil.getChildOfType(functionClauses[0], PicatHead::class.java)
        var structure = headElement as? PicatStructure
        Assertions.assertEquals("factorial", structure?.atom?.text, "First function name should be 'factorial'")
        headElement = PsiTreeUtil.getChildOfType(functionClauses[1], PicatHead::class.java)
        structure = headElement as? PicatStructure
        Assertions.assertEquals("factorial", structure?.atom?.text, "Second function name should be 'factorial'")
        headElement = PsiTreeUtil.getChildOfType(functionClauses[2], PicatHead::class.java)
        structure = headElement as? PicatStructure
        Assertions.assertEquals("custom_sum", structure?.atom?.text, "Third function name should be 'sum'")

        // Verify that each function definition has the correct arity
        headElement = PsiTreeUtil.getChildOfType(functionClauses[0], PicatHead::class.java)
        structure = headElement as? PicatStructure
        Assertions.assertEquals(1, structure?.argumentList?.expressionList?.size ?: 0, "First function arity should be 1")
        headElement = PsiTreeUtil.getChildOfType(functionClauses[1], PicatHead::class.java)
        structure = headElement as? PicatStructure
        Assertions.assertEquals(1, structure?.argumentList?.expressionList?.size ?: 0, "Second function arity should be 1")
        headElement = PsiTreeUtil.getChildOfType(functionClauses[2], PicatHead::class.java)
        structure = headElement as? PicatStructure
        Assertions.assertEquals(2, structure?.argumentList?.expressionList?.size ?: 0, "Third function arity should be 2")
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
        Assertions.assertNotNull(functionClauses, "Function clauses should not be null")
        Assertions.assertEquals(1, functionClauses!!.size, "There should be exactly one function clause")

        val functionClause = functionClauses[0]
        Assertions.assertTrue(functionClause is PicatFunctionRule, "This test expects a rule with a FunctionBody due to ':='")

        val functionRule = functionClause as PicatFunctionRule
        // Verify that the function definition has a body
        val body = functionRule.getFunctionBody() // This should be PicatFunctionBody
        Assertions.assertNotNull(body, "Function rule should have a PicatFunctionBody")

        // Verify that the function body has the correct text (might be the text of the expression within FunctionBody)
        Assertions.assertEquals(
            "1",
            body?.getExpression()?.text,
            "Function body should be '1'"
        ) // Assuming PicatFunctionBody has getExpression()

        // Verify that the function body is of the correct type
        Assertions.assertTrue(body is PicatFunctionBody, "Function body should be a PicatFunctionBody")
    }
}
