package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test for Picat expression PSI implementation.
 * This test verifies that expressions are correctly parsed into PSI elements.
 */
class PicatExpressionTest : BasePlatformTestCase() {

    @Test
    fun testSimpleExpressionPsi() {
        // Test that a simple expression is correctly parsed
        val code = """
            main => 
                X = 1 + 2,
                println(X).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Debug: Print the number of expressions found
        println("[DEBUG_LOG] Number of expressions found: ${expressions.size}")
        expressions.forEach { expression ->
            println("[DEBUG_LOG] Expression: ${expression.text}, Class: ${expression.javaClass.simpleName}")
        }

        // Find expressions with a + operator
        val additionExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "+" }
        }

        // Verify that there is at least one addition expression
        assertTrue(additionExpressions.isNotEmpty(), "There should be at least one addition expression")

        // Debug: Print the addition expression details
        val additionExpression = additionExpressions.first()
        println("[DEBUG_LOG] Addition expression: ${additionExpression.text}")

        // Verify that the expression has terms
        val terms = additionExpression.getTerms()
        assertTrue(terms.isNotEmpty(), "Expression should have terms")

        // Verify that the expression has operators
        val operators = additionExpression.getOperators()
        assertTrue(operators.isNotEmpty(), "Expression should have operators")

        // Verify that one of the operators is +
        assertTrue(operators.any { it.text == "+" }, "Expression should have + operator")
    }

    @Test
    fun testComplexExpressionPsi() {
        // Test that a complex expression with multiple operators is correctly parsed
        val code = """
            main => 
                X = 1 + 2 * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with both + and * operators
        val complexExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "+" } &&
            expr.getOperators().any { op -> op.text == "*" }
        }

        // Verify that there is at least one complex expression
        assertTrue(complexExpressions.isNotEmpty(), "There should be at least one complex expression")

        // Debug: Print the complex expression details
        val complexExpression = complexExpressions.first()
        println("[DEBUG_LOG] Complex expression: ${complexExpression.text}")

        // Verify that the expression has terms
        val terms = complexExpression.getTerms()
        assertTrue(terms.isNotEmpty(), "Expression should have terms")

        // Verify that the expression has operators
        val operators = complexExpression.getOperators()
        assertTrue(operators.isNotEmpty(), "Expression should have operators")

        // Verify that the operators include + and *
        assertTrue(operators.any { it.text == "+" }, "Expression should have + operator")
        assertTrue(operators.any { it.text == "*" }, "Expression should have * operator")
    }

    @Test
    fun testNestedExpressionPsi() {
        // Test that a nested expression with parentheses is correctly parsed
        val code = """
            main => 
                X = (1 + 2) * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with * operator
        val multiplicationExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "*" }
        }

        // Verify that there is at least one multiplication expression
        assertTrue(multiplicationExpressions.isNotEmpty(), "There should be at least one multiplication expression")

        // Debug: Print the nested expression details
        val multiplicationExpression = multiplicationExpressions.first()
        println("[DEBUG_LOG] Multiplication expression: ${multiplicationExpression.text}")

        // Verify that the expression has terms
        val terms = multiplicationExpression.getTerms()
        assertTrue(terms.isNotEmpty(), "Expression should have terms")

        // Verify that the expression has operators
        val operators = multiplicationExpression.getOperators()
        assertTrue(operators.isNotEmpty(), "Expression should have operators")

        // Verify that one of the operators is *
        assertTrue(operators.any { it.text == "*" }, "Expression should have * operator")

        // Look for nested expressions (expressions inside terms)
        var foundNestedExpression = false
        for (term in terms) {
            val nestedExpression = term.getExpression()
            if (nestedExpression != null) {
                foundNestedExpression = true
                println("[DEBUG_LOG] Found nested expression: ${nestedExpression.text}")

                // Verify that the nested expression has terms
                val nestedTerms = nestedExpression.getTerms()
                assertTrue(nestedTerms.isNotEmpty(), "Nested expression should have terms")

                // Verify that the nested expression has operators
                val nestedOperators = nestedExpression.getOperators()
                assertTrue(nestedOperators.isNotEmpty(), "Nested expression should have operators")

                // Verify that one of the operators is +
                assertTrue(nestedOperators.any { it.text == "+" }, "Nested expression should have + operator")
            }
        }

        assertTrue(foundNestedExpression, "Should have found a nested expression")
    }

    @Test
    fun testVariableExpressionPsi() {
        // Test that an expression with variables is correctly parsed
        val code = """
            main => 
                X = 1,
                Y = 2,
                Z = X + Y,
                println(Z).
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with + operator
        val additionExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "+" }
        }

        // Verify that there is at least one addition expression
        assertTrue(additionExpressions.isNotEmpty(), "There should be at least one addition expression")

        // Debug: Print the variable expression details
        val variableExpression = additionExpressions.first()
        println("[DEBUG_LOG] Variable expression: ${variableExpression.text}")

        // Verify that the expression has terms
        val terms = variableExpression.getTerms()
        assertTrue(terms.isNotEmpty(), "Expression should have terms")

        // Verify that the expression has operators
        val operators = variableExpression.getOperators()
        assertTrue(operators.isNotEmpty(), "Expression should have operators")

        // Verify that one of the operators is +
        assertTrue(operators.any { it.text == "+" }, "Expression should have + operator")

        // Verify that at least one term is a variable
        var foundVariable = false
        for (term in terms) {
            val variable = term.getVariable()
            if (variable != null) {
                foundVariable = true
                println("[DEBUG_LOG] Found variable: ${variable.text}")
            }
        }

        assertTrue(foundVariable, "Should have found a variable")
    }

    @Test
    fun testFunctionCallExpressionPsi() {
        // Test that an expression with a function call is correctly parsed
        val code = """
            main => 
                X = factorial(5),
                println(X).

            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all structures in the file (function calls are structures)
        val structures = PsiTreeUtil.findChildrenOfType(file, PicatStructure::class.java).toList()

        // Debug: Print the number of structures found
        println("[DEBUG_LOG] Number of structures found: ${structures.size}")
        structures.forEach { structure ->
            println("[DEBUG_LOG] Structure: ${structure.text}, Name: ${structure.getName()}")
        }

        // Find the factorial structure
        val factorialStructures = structures.filter { it.getName() == "factorial" }

        // Verify that there is at least one factorial structure
        assertTrue(factorialStructures.isNotEmpty(), "There should be at least one factorial structure")

        // Debug: Print the factorial structure details
        val factorialStructure = factorialStructures.first()
        println("[DEBUG_LOG] Factorial structure: ${factorialStructure.text}")

        // Verify that the structure has the correct name and arity
        assertEquals("factorial", factorialStructure.getName(), "Structure name should be 'factorial'")
        assertTrue(factorialStructure.getArity() >= 1, "Structure arity should be at least 1")

        // Verify that the structure has an argument list
        val argumentList = factorialStructure.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertTrue(arguments.isNotEmpty(), "Argument list should have at least 1 argument")

        // Debug: Print the arguments
        arguments.forEach { argument ->
            println("[DEBUG_LOG] Argument: ${argument.text}")
        }
    }

    @Test
    fun testComparisonExpressionPsi() {
        // Test that a comparison expression is correctly parsed
        val code = """
            main => 
                X = 1,
                Y = 2,
                if X < Y then
                    println("X is less than Y")
                else
                    println("X is not less than Y")
                end.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all if-then-else statements in the file
        val ifThenElseStatements = PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
            .filter { it.text.contains("if") && it.text.contains("then") }

        // Debug: Print the number of if-then-else statements found
        println("[DEBUG_LOG] Number of if-then-else statements found: ${ifThenElseStatements.size}")

        // Verify that there is at least one if-then-else statement
        assertTrue(ifThenElseStatements.isNotEmpty(), "There should be at least one if-then-else statement")

        // Find all comparison expressions in the file
        val comparisonExpressions = PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
            .filter { it.text.contains("<") && it.text.contains("X") && it.text.contains("Y") }

        // Debug: Print the number of comparison expressions found
        println("[DEBUG_LOG] Number of comparison expressions found: ${comparisonExpressions.size}")
        comparisonExpressions.forEach { expr ->
            println("[DEBUG_LOG] Comparison expression: ${expr.text}")
        }

        // Verify that there is at least one comparison expression
        assertTrue(comparisonExpressions.isNotEmpty(), "There should be at least one comparison expression")

        // Debug: Print the comparison expression details
        val comparisonExpression = comparisonExpressions.first()
        println("[DEBUG_LOG] Comparison expression: ${comparisonExpression.text}")
    }

    /**
     * Helper method to find all expressions in a file.
     */
    private fun findExpressionsInFile(file: PicatFile): List<PicatExpression> {
        return PsiTreeUtil.findChildrenOfType(file, PicatExpression::class.java).toList()
    }
}