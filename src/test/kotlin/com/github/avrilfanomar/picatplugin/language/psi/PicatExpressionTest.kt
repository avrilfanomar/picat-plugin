@file:Suppress("GrazieInspection")

package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
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
        val assignmentExpressions = PsiTreeUtil.findChildrenOfType(file, PicatAssignment::class.java)
        assertTrue(assignmentExpressions.isNotEmpty())

        val assignment = assignmentExpressions.firstOrNull { it.text == "X = 1 + 2" }
        assertNotNull(assignment)

        // The right-hand side of the assignment should be an additive expression
        // assignment rule is `variable ASSIGN_OP expression`
        val rhsExpression = assignment?.expression
        val additiveExpression = PsiTreeUtil.findChildOfType(rhsExpression, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression)

        // Check for the + operator
        val plusOperator = additiveExpression!!.node.getChildren(null).any {
            it.elementType == PicatTokenTypes.PLUS
        }
        assertTrue(plusOperator)

        // Check for terms (operands)
        // additive_expression ::= multiplicative_expression ((PLUS | MINUS) multiplicative_expression)*
        val terms = additiveExpression.multiplicativeExpressionList
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

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignment::class.java)
            .firstOrNull { it.text == "X = 1 + 2 * 3" }
        assertNotNull(assignment, "Should find the assignment 'X = 1 + 2 * 3'")

        val rhsExpressionOp = assignment?.expression
        val additiveExpression = PsiTreeUtil.findChildOfType(rhsExpressionOp, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression, "Expression '1 + 2 * 3' should be an additive expression")

        // Check for the + operator
        assertTrue(additiveExpression!!.node.getChildren(null).any { it.elementType == PicatTokenTypes.PLUS })

        val firstTerm = additiveExpression.multiplicativeExpressionList.getOrNull(0)
        assertNotNull(firstTerm)
        assertEquals("1", firstTerm!!.text) // This should be a multiplicativeExpression

        val secondTerm = additiveExpression.multiplicativeExpressionList.getOrNull(1)
        assertNotNull(secondTerm, "Additive expression should have a second term for '2 * 3'")
        assertEquals("2 * 3", secondTerm!!.text) // This is a multiplicativeExpression

        // Verify the multiplicative expression "2 * 3"
        // multiplicative_expression ::= power_expression ((MULTIPLY | DIVIDE | ...) power_expression)*
        assertTrue(secondTerm.node.getChildren(null).any { it.elementType == PicatTokenTypes.MULTIPLY })
        val factors = secondTerm.powerExpressionList // Operands of * are power_expression
        assertEquals(2, factors.size, "Multiplicative expression '2 * 3' should have two factors")
        // Each power_expression might wrap a unary_expression, which wraps a primary_expression
        // For simplicity, checking text, but a deeper check might be needed if text is not direct.
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

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignment::class.java)
            .firstOrNull { it.text == "X = (1 + 2) * 3" }
        assertNotNull(assignment, "Should find the assignment 'X = (1 + 2) * 3'")

        // The top-level expression should be a multiplicative expression
        val rhsExpressionParen = assignment?.expression
        val multiplicativeExpression =
            PsiTreeUtil.findChildOfType(rhsExpressionParen, PicatMultiplicativeExpression::class.java)
        assertNotNull(
            multiplicativeExpression,
            "Expression '(1 + 2) * 3' should be a multiplicative expression"
        )

        assertTrue(multiplicativeExpression!!.node.getChildren(null).any { it.elementType == PicatTokenTypes.MULTIPLY })
        // A parenthesized expression is a primary_expression,
        // which is part of power_expression -> unary_expression -> primary_expression
        val factors = multiplicativeExpression.powerExpressionList
        assertEquals(2, factors.size) // Should have two powerExpression children

        // To get to (1+2) which is a primary_expression from power_expression:
        val firstFactorPrimary = getInnermostPrimaryExpression(factors[0])

        assertNotNull(
            firstFactorPrimary,
            "Multiplicative expression should have a first factor (primary)"
        )
        assertEquals("(1 + 2)", firstFactorPrimary!!.text)


        // This primary expression should contain LPAR, RPAR and an additive expression
        assertNotNull(firstFactorPrimary.lpar, "Parenthesized expression should have LPAR")
        assertNotNull(firstFactorPrimary.rpar, "Parenthesized expression should have RPAR")
        // The expression inside LPAR RPAR is a general expression, which can be additive
        val nestedExpression = firstFactorPrimary.expression // primary_expression ::= LPAR expression RPAR
        val nestedAdditiveExpression = PsiTreeUtil.findChildOfType(
            nestedExpression,
            PicatAdditiveExpression::class.java
        )
        assertNotNull(
            nestedAdditiveExpression,
            "Parenthesized expression should contain an additive expression"
        )

        // Verify the nested additive expression "1 + 2"
        assertTrue(nestedAdditiveExpression!!.node.getChildren(null).any { it.elementType == PicatTokenTypes.PLUS })
        val nestedTerms = nestedAdditiveExpression.multiplicativeExpressionList
        assertEquals(2, nestedTerms.size, "Nested additive expression should have two terms")
        assertEquals("1", nestedTerms[0].text)
        assertEquals("2", nestedTerms[1].text)

        // To get to "3" (second factor)
        val secondFactorPrimary = getInnermostPrimaryExpression(factors[1])

        assertNotNull(secondFactorPrimary)
        assertEquals("3", secondFactorPrimary!!.text)
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

        val assignment = PsiTreeUtil.findChildrenOfType(file, PicatAssignment::class.java)
            .firstOrNull { it.text == "Z = X + Y" }
        assertNotNull(assignment, "Should find the assignment 'Z = X + Y'")

        val rhsExpressionVar = assignment?.expression
        val additiveExpression = PsiTreeUtil.findChildOfType(rhsExpressionVar, PicatAdditiveExpression::class.java)
        assertNotNull(additiveExpression, "Expression 'X + Y' should be an additive expression")

        assertTrue(additiveExpression!!.node.getChildren(null).any { it.elementType == PicatTokenTypes.PLUS })

        val terms = additiveExpression.multiplicativeExpressionList
        assertEquals(2, terms.size, "Additive expression 'X + Y' should have two terms")

        // Each term (X, Y) is a multiplicativeExpression.
        val primaryX = getInnermostPrimaryExpression(terms[0])
        assertEquals("X", primaryX?.text)
        assertNotNull(
            primaryX?.atom ?: primaryX?.variable,
            "Term 'X' should contain an atom or variable"
        )

        val primaryY = getInnermostPrimaryExpression(terms[1])
        assertEquals("Y", primaryY?.text)
        assertNotNull(
            primaryY?.atom ?: primaryY?.variable,
            "Term 'Y' should contain an atom or variable"
        )
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
            expr.node.getChildren(null).any { it.elementType == PicatTokenTypes.LESS } // Changed LESS_THAN to LESS
                    && expr.text.contains("X")
                    && expr.text.contains("Y")
        }
        assertNotNull(specificComparison, "Should find comparison 'X < Y'")

        // The rule is: "comparison ::= expression comparison_operator expression {elementType=COMPARISON}"
        // This means PicatComparison PSI element will have an expressionList.
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
        assertTrue(structures.isNotEmpty())

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
        assertTrue(listExpressions.isNotEmpty())

        val list = listExpressions.first() // Assuming it's the primary list in X = [...]
        assertNotNull(list, "Should find the list [1, a, f(X)]")

        // list_expression ::= LBRACKET list_items? RBRACKET
        // list_items ::= expression ((COMMA | SEMICOLON) expression)* (PIPE expression)? {elementType = LIST_ELEMENTS}
        // The PSI class for list_items is PicatListItems (due to psiClassPrefix and rule name)
        // It should have an expressionList.
        val listItems = list.listItems
        assertNotNull(listItems)
        val items = listItems!!.expressionList
        assertEquals(3, items.size, "List should have 3 items")
        assertEquals("1", items[0].text)
        assertEquals("a", items[1].text)
        assertEquals("f(X)", items[2].text)

        // To check if items[2] is a function call, we need to inspect its type.
        // expression -> ... -> primary_expression -> function_call
        val primaryExpression = getInnermostPrimaryExpression(items[2])
        assertNotNull(primaryExpression)
        assertTrue(primaryExpression!!.functionCall != null)
    }

}

/**
 * Helper function to navigate down the PSI tree from a general expression
 * to the innermost PicatPrimaryExpression. This is useful when an expression
 * is wrapped by many layers according to operator precedence rules.
 */
private fun getInnermostPrimaryExpression(element: PsiElement?): PicatPrimaryExpression? {
    var current: PsiElement? = element

    // Descend through expression wrappers if 'element' itself is a high-level expression type
    if (current is PicatExpression) {
        current = PsiTreeUtil.getChildOfType(current, PicatBiconditionalExpressionLevel::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatImplicationExpressionLevel::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatConditionalExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatLogicalOrExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatLogicalAndExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatBitwiseOrExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatBitwiseXorExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatBitwiseAndExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatEqualityExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatRelationalExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatShiftExpression::class.java)
        current = PsiTreeUtil.getChildOfType(current, PicatAdditiveExpression::class.java)
    }

    // Descend through multiplicative and power expressions
    if (current is PicatAdditiveExpression) {
        if (current.multiplicativeExpressionList.isNotEmpty()) {
            current = current.multiplicativeExpressionList[0]
        }
    }
    if (current is PicatMultiplicativeExpression) {
        if (current.powerExpressionList.isNotEmpty()) {
            current = current.powerExpressionList[0]
        }
    }
    if (current is PicatPowerExpression) {
        current = current.unaryExpressionList?.first()?.primaryExpression
    }
    if (current is PicatUnaryExpression) {
        current = current.primaryExpression
    }
    return current as? PicatPrimaryExpression
}
