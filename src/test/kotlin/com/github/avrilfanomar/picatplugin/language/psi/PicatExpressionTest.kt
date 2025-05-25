package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.junit.Test

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

        myFixture.configureByText("test.pi", code)
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
        assertTrue("There should be at least one addition expression", additionExpressions.isNotEmpty())

        // Debug: Print the addition expression details
        val additionExpression = additionExpressions.first()
        println("[DEBUG_LOG] Addition expression: ${additionExpression.text}")

        // Verify that the expression has terms
        val terms = additionExpression.getTerms()
        assertTrue("Expression should have terms", terms.isNotEmpty())

        // Verify that the expression has operators
        val operators = additionExpression.getOperators()
        assertTrue("Expression should have operators", operators.isNotEmpty())

        // Verify that one of the operators is +
        assertTrue("Expression should have + operator", operators.any { it.text == "+" })
    }

    @Test
    fun testComplexExpressionPsi() {
        // Test that a complex expression with multiple operators is correctly parsed
        val code = """
            main => 
                X = 1 + 2 * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with both + and * operators
        val complexExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "+" } &&
            expr.getOperators().any { op -> op.text == "*" }
        }

        // Verify that there is at least one complex expression
        assertTrue("There should be at least one complex expression", complexExpressions.isNotEmpty())

        // Debug: Print the complex expression details
        val complexExpression = complexExpressions.first()
        println("[DEBUG_LOG] Complex expression: ${complexExpression.text}")

        // Verify that the expression has terms
        val terms = complexExpression.getTerms()
        assertTrue("Expression should have terms", terms.isNotEmpty())

        // Verify that the expression has operators
        val operators = complexExpression.getOperators()
        assertTrue("Expression should have operators", operators.isNotEmpty())

        // Verify that the operators include + and *
        assertTrue("Expression should have + operator", operators.any { it.text == "+" })
        assertTrue("Expression should have * operator", operators.any { it.text == "*" })
    }

    @Test
    fun testNestedExpressionPsi() {
        // Test that a nested expression with parentheses is correctly parsed
        val code = """
            main => 
                X = (1 + 2) * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with * operator
        val multiplicationExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "*" }
        }

        // Verify that there is at least one multiplication expression
        assertTrue("There should be at least one multiplication expression", multiplicationExpressions.isNotEmpty())

        // Debug: Print the nested expression details
        val multiplicationExpression = multiplicationExpressions.first()
        println("[DEBUG_LOG] Multiplication expression: ${multiplicationExpression.text}")

        // Verify that the expression has terms
        val terms = multiplicationExpression.getTerms()
        assertTrue("Expression should have terms", terms.isNotEmpty())

        // Verify that the expression has operators
        val operators = multiplicationExpression.getOperators()
        assertTrue("Expression should have operators", operators.isNotEmpty())

        // Verify that one of the operators is *
        assertTrue("Expression should have * operator", operators.any { it.text == "*" })

        // Look for nested expressions (expressions inside terms)
        var foundNestedExpression = false
        for (term in terms) {
            val nestedExpression = term.getExpression()
            if (nestedExpression != null) {
                foundNestedExpression = true
                println("[DEBUG_LOG] Found nested expression: ${nestedExpression.text}")

                // Verify that the nested expression has terms
                val nestedTerms = nestedExpression.getTerms()
                assertTrue("Nested expression should have terms", nestedTerms.isNotEmpty())

                // Verify that the nested expression has operators
                val nestedOperators = nestedExpression.getOperators()
                assertTrue("Nested expression should have operators", nestedOperators.isNotEmpty())

                // Verify that one of the operators is +
                assertTrue("Nested expression should have + operator", nestedOperators.any { it.text == "+" })
            }
        }

        assertTrue("Should have found a nested expression", foundNestedExpression)
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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all expressions in the file
        val expressions = findExpressionsInFile(file)

        // Find expressions with + operator
        val additionExpressions = expressions.filter { expr ->
            expr.getOperators().any { op -> op.text == "+" }
        }

        // Verify that there is at least one addition expression
        assertTrue("There should be at least one addition expression", additionExpressions.isNotEmpty())

        // Debug: Print the variable expression details
        val variableExpression = additionExpressions.first()
        println("[DEBUG_LOG] Variable expression: ${variableExpression.text}")

        // Verify that the expression has terms
        val terms = variableExpression.getTerms()
        assertTrue("Expression should have terms", terms.isNotEmpty())

        // Verify that the expression has operators
        val operators = variableExpression.getOperators()
        assertTrue("Expression should have operators", operators.isNotEmpty())

        // Verify that one of the operators is +
        assertTrue("Expression should have + operator", operators.any { it.text == "+" })

        // Verify that at least one term is a variable
        var foundVariable = false
        for (term in terms) {
            val variable = term.getVariable()
            if (variable != null) {
                foundVariable = true
                println("[DEBUG_LOG] Found variable: ${variable.text}")
            }
        }

        assertTrue("Should have found a variable", foundVariable)
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

        myFixture.configureByText("test.pi", code)
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
        assertTrue("There should be at least one factorial structure", factorialStructures.isNotEmpty())

        // Debug: Print the factorial structure details
        val factorialStructure = factorialStructures.first()
        println("[DEBUG_LOG] Factorial structure: ${factorialStructure.text}")

        // Verify that the structure has the correct name and arity
        assertEquals("Structure name should be 'factorial'", "factorial", factorialStructure.getName())
        assertTrue("Structure arity should be at least 1", factorialStructure.getArity() >= 1)

        // Verify that the structure has an argument list
        val argumentList = factorialStructure.getArgumentList()
        assertNotNull("Structure should have an argument list", argumentList)

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertTrue("Argument list should have at least 1 argument", arguments.isNotEmpty())

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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all if-then-else statements in the file
        val ifThenElseStatements = PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
            .filter { it.text.contains("if") && it.text.contains("then") }

        // Debug: Print the number of if-then-else statements found
        println("[DEBUG_LOG] Number of if-then-else statements found: ${ifThenElseStatements.size}")

        // Verify that there is at least one if-then-else statement
        assertTrue("There should be at least one if-then-else statement", ifThenElseStatements.isNotEmpty())

        // Find all comparison expressions in the file
        val comparisonExpressions = PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
            .filter { it.text.contains("<") && it.text.contains("X") && it.text.contains("Y") }

        // Debug: Print the number of comparison expressions found
        println("[DEBUG_LOG] Number of comparison expressions found: ${comparisonExpressions.size}")
        comparisonExpressions.forEach { expr ->
            println("[DEBUG_LOG] Comparison expression: ${expr.text}")
        }

        // Verify that there is at least one comparison expression
        assertTrue("There should be at least one comparison expression", comparisonExpressions.isNotEmpty())

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
