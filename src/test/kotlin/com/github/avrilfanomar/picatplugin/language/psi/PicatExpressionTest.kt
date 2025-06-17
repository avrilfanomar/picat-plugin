package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test

/**
 * Test for Picat expression PSI implementation.
 * This test verifies that expressions are correctly parsed into PSI elements.
 */
class PicatExpressionTest : BasePlatformTestCase() {

    @Test
    fun testSimpleExpressionPsi() {
        // Test that a simple expression (e.g., 1 + 2) is correctly parsed
        val code = """
            main => 
                X = 1 + 2,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find assignment expressions
        val assignmentExpressions = PsiTreeUtil.findChildrenOfType(file, PicatAssignmentExpression::class.java)
        assertTrue(assignmentExpressions.isNotEmpty(), "Should find at least one assignment expression")

        val assignment = assignmentExpressions.firstOrNull { it.text == "X = 1 + 2" }
        assertNotNull(assignment, "Should find the assignment 'X = 1 + 2'")

        // The right-hand side of the assignment should be an additive expression
        val additiveExpression = PsiTreeUtil.findChildOfType(assignment, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression, "Assignment 'X = 1 + 2' should contain an additive expression")

        // Check for the + operator
        val plusOperator = additiveExpression!!.children.any { it.node.elementType == PicatTokenTypes.PLUS }
        assertTrue(plusOperator, "Additive expression should have a '+' operator")

        // Check for terms (operands)
        val terms = additiveExpression.children.filterIsInstance<PicatMultiplicativeExpression>()
        assertEquals(2, terms.size, "Additive expression should have two terms")
        assertEquals("1", terms[0].text)
        assertEquals("2", terms[1].text)
    }

    @Test
    fun testOperatorPrecedencePsi() {
        // Test that operator precedence (e.g., 1 + 2 * 3) is correctly parsed
        val code = """
            main => 
                X = 1 + 2 * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignmentExpression::class.java)
            .firstOrNull { it.text == "X = 1 + 2 * 3" }
        assertNotNull(assignment, "Should find the assignment 'X = 1 + 2 * 3'")

        val additiveExpression = PsiTreeUtil.findChildOfType(assignment, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression, "Expression '1 + 2 * 3' should be an additive expression")

        // Check for the + operator
        assertTrue(
            additiveExpression!!.children.any { it.node.elementType == PicatTokenTypes.PLUS },
            "Additive expression should have a '+' operator"
        )

        val firstTerm = additiveExpression.multiplicativeExpressionList.getOrNull(0)
        assertNotNull(firstTerm)
        assertEquals("1", firstTerm!!.text)

        val secondTerm = additiveExpression.multiplicativeExpressionList.getOrNull(1)
        assertNotNull(secondTerm, "Additive expression should have a second term for '2 * 3'")
        assertEquals("2 * 3", secondTerm!!.text)

        // Verify the multiplicative expression "2 * 3"
        assertTrue(
            secondTerm.children.any { it.node.elementType == PicatTokenTypes.MUL_OP },
            "Multiplicative expression '2 * 3' should have a '*' operator"
        )
        val factors = secondTerm.primaryExpressionList // Operands of * are primary_expression
        assertEquals(2, factors.size, "Multiplicative expression '2 * 3' should have two factors")
        assertEquals("2", factors[0].text)
        assertEquals("3", factors[1].text)
    }

    @Test
    fun testParenthesizedExpressionPsi() {
        // Test that parenthesized expressions (e.g., (1 + 2) * 3) are correctly parsed
        val code = """
            main => 
                X = (1 + 2) * 3,
                println(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignmentExpression::class.java)
            .firstOrNull { it.text == "X = (1 + 2) * 3" }
        assertNotNull(assignment, "Should find the assignment 'X = (1 + 2) * 3'")

        // The top-level expression should be a multiplicative expression
        val multiplicativeExpression =
            PsiTreeUtil.findChildOfType(assignment, PicatMultiplicativeExpression::class.java)
        assertNotNull(multiplicativeExpression, "Expression '(1 + 2) * 3' should be a multiplicative expression")

        assertTrue(
            multiplicativeExpression!!.children.any { it.node.elementType == PicatTokenTypes.MUL_OP },
            "Multiplicative expression should have '*' operator"
        )

        val firstFactor = multiplicativeExpression.primaryExpressionList.getOrNull(0)
        assertNotNull(firstFactor, "Multiplicative expression should have a first factor")
        assertEquals("(1 + 2)", firstFactor!!.text)

        // This primary expression should contain LPAR, RPAR and an additive expression
        assertNotNull(firstFactor.lpar, "Parenthesized expression should have LPAR")
        assertNotNull(firstFactor.rpar, "Parenthesized expression should have RPAR")
        val nestedAdditiveExpression = PsiTreeUtil.findChildOfType(firstFactor, PicatAdditiveExpression::class.java)
        assertNotNull(nestedAdditiveExpression, "Parenthesized expression should contain an additive expression")

        // Verify the nested additive expression "1 + 2"
        assertTrue(
            nestedAdditiveExpression!!.children.any { it.node.elementType == PicatTokenTypes.PLUS },
            "Nested additive expression should have '+' operator"
        )
        val nestedTerms = nestedAdditiveExpression.multiplicativeExpressionList
        assertEquals(2, nestedTerms.size, "Nested additive expression should have two terms")
        assertEquals("1", nestedTerms[0].text)
        assertEquals("2", nestedTerms[1].text)

        val secondFactor = multiplicativeExpression.primaryExpressionList.getOrNull(1)
        assertNotNull(secondFactor)
        assertEquals("3", secondFactor!!.text)
    }

    @Test
    fun testVariableExpressionPsi() {
        // Test that an expression with variables (e.g., Z = X + Y) is correctly parsed
        val code = """
            main => 
                X = 1, Y = 2, Z = X + Y,
                println(Z).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignmentExpression::class.java)
            .firstOrNull { it.text == "Z = X + Y" }
        assertNotNull(assignment, "Should find the assignment 'Z = X + Y'")

        val additiveExpression = PsiTreeUtil.findChildOfType(assignment, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression, "Expression 'X + Y' should be an additive expression")

        assertTrue(
            additiveExpression!!.children.any { it.node.elementType == PicatTokenTypes.PLUS },
            "Additive expression should have a '+' operator"
        )

        val terms = additiveExpression.multiplicativeExpressionList
        assertEquals(2, terms.size, "Additive expression 'X + Y' should have two terms")
        // Each term (X, Y) would be a PicatPrimaryExpression containing a PicatIdentifier (atom)
        assertEquals("X", terms[0].text)
        assertNotNull(PsiTreeUtil.findChildOfType(terms[0], PicatAtom::class.java), "Term 'X' should contain an atom")
        assertEquals("Y", terms[1].text)
        assertNotNull(PsiTreeUtil.findChildOfType(terms[1], PicatAtom::class.java), "Term 'Y' should contain an atom")
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
        val file = myFixture.file as PicatFileImpl

        // Find all function calls
        val functionCalls = PsiTreeUtil.findChildrenOfType(file, PicatFunctionCall::class.java)

        // Find the factorial function call
        val factorialCall = functionCalls.firstOrNull { it.atom.text == "factorial" }
        assertNotNull(factorialCall, "Should find 'factorial(5)' function call")

        assertEquals("factorial", factorialCall!!.atom.text, "Function call name should be 'factorial'")

        val arity = factorialCall.argumentList?.expressionList?.size ?: 0
        assertEquals(1, arity, "Function call arity should be 1")

        val argumentList = factorialCall.argumentList
        assertNotNull(argumentList, "Function call should have an argument list")

        val arguments = argumentList!!.expressionList
        assertNotNull(arguments, "Argument list should not be null")
        assertEquals(1, arguments.size, "Argument list should have 1 argument")
        assertEquals("5", arguments[0].text)
    }

    @Test
    fun testComparisonExpressionPsi() {
        // Test that a comparison expression is correctly parsed
        val code = """
            main => 
                X = 1, Y = 2,
                if X < Y then
                    println("X is less than Y")
                else
                    println("X is not less than Y")
                end.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        // Find all comparison expressions
        val comparisonExpressions = PsiTreeUtil.findChildrenOfType(file, PicatComparison::class.java)

        val specificComparison = comparisonExpressions.firstOrNull { expr ->
            expr.children.any { it.node.elementType == PicatTokenTypes.LESS_THAN }
                    && expr.text.contains("X")
                    && expr.text.contains("Y")
        }
        assertNotNull(specificComparison, "Should find comparison 'X < Y'")

        // A PicatComparison has two PicatExpression children and an operator token
        val operands = specificComparison!!.expressionList
        assertEquals(2, operands.size, "Comparison X < Y should have two operands")
        assertEquals("X", operands[0].text)
        assertEquals("Y", operands[1].text)
    }

    // Test for PicatStructure (similar to function call but used in facts/rules directly)
    @Test
    fun testStructureExpressionPsi() {
        val code = "point(1, 2)."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val structures = PsiTreeUtil.findChildrenOfType(file, PicatStructure::class.java)
        assertTrue(structures.isNotEmpty(), "Should find at least one structure")

        val pointStructure = structures.firstOrNull { it.atom.text == "point" }
        assertNotNull(pointStructure, "Should find structure 'point(1,2)'")

        assertEquals("point", pointStructure!!.atom.text, "Structure name should be 'point'")
        val arity = pointStructure.argumentList?.expressionList?.size ?: 0
        assertEquals(2, arity, "Structure arity should be 2")

        val arguments = pointStructure.argumentList?.expressionList
        assertNotNull(arguments)
        assertEquals(2, arguments!!.size)
        assertEquals("1", arguments[0].text)
        assertEquals("2", arguments[1].text)
    }

    // Test for List Expression
    @Test
    fun testListExpressionPsi() {
        val code = "L = [1, a, f(X)]."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val listExpressions = PsiTreeUtil.findChildrenOfType(file, PicatListExpression::class.java)
        assertTrue(listExpressions.isNotEmpty(), "Should find at least one list expression")

        val list = listExpressions.first() // Assuming it's the primary list in X = [...]
        assertNotNull(list, "Should find the list [1, a, f(X)]")

        val items = list.listItems.expressionList
        assertEquals(3, items.size, "List should have 3 items")
        assertEquals("1", items[0].text)
        assertEquals("a", items[1].text)
        assertEquals("f(X)", items[2].text)
        assertTrue(items[2] is PicatFunctionCall, "Third item should be a function call")
    }

}
