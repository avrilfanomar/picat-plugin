@file:Suppress("GrazieInspection")

package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat expression PSI implementation.
 * This test verifies that expressions are correctly parsed into PSI elements.
 */
class PicatExpressionTest : BasePlatformTestCase() {

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
        val factorialCall = functionCalls.firstOrNull { it.atom?.text == "factorial" }
        Assertions.assertNotNull(factorialCall, "Should find 'factorial(5)' function call")

        Assertions.assertEquals(
            "factorial",
            factorialCall!!.atom?.text,
            "Function call name should be 'factorial'"
        )

        val arity = factorialCall.argumentList?.expressionList?.size ?: 0
        Assertions.assertEquals(1, arity, "Function call arity should be 1")

        val argumentList = factorialCall.argumentList
        Assertions.assertNotNull(argumentList, "Function call should have an argument list")

        val arguments = argumentList!!.expressionList
        Assertions.assertNotNull(arguments, "Argument list should not be null")
        Assertions.assertEquals(1, arguments.size, "Argument list should have 1 argument")
        Assertions.assertEquals("5", arguments[0].text)
    }

    // Test for PicatStructure (similar to function call but used in facts/rules directly)
    @Test
    fun testStructureExpressionPsi() {
        val code = "point(1, 2)."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val structures = PsiTreeUtil.findChildrenOfType(file, PicatStructure::class.java)
        Assertions.assertTrue(structures.isNotEmpty())

        val pointStructure = structures.firstOrNull { it.atom.text == "point" }
        Assertions.assertNotNull(pointStructure, "Should find structure 'point(1,2)'")

        Assertions.assertEquals(
            "point",
            pointStructure!!.atom.text,
            "Structure name should be 'point'"
        )
        val arity = pointStructure.argumentList?.expressionList?.size ?: 0
        Assertions.assertEquals(2, arity, "Structure arity should be 2")

        val arguments = pointStructure.argumentList?.expressionList
        Assertions.assertNotNull(arguments)
        Assertions.assertEquals(2, arguments!!.size)
        Assertions.assertEquals("1", arguments[0].text)
        Assertions.assertEquals("2", arguments[1].text)
    }

    // Test for List Expression
    @Test
    fun testListExpressionPsi() {
        val code = "L = [1, a, f(X)]."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val listExpressions = PsiTreeUtil.findChildrenOfType(file, PicatListExpression::class.java)
        Assertions.assertTrue(listExpressions.isNotEmpty())

        val list = listExpressions.first() // Assuming it's the primary list in X = [...]
        Assertions.assertNotNull(list, "Should find the list [1, a, f(X)]")

        // list_expression ::= LBRACKET list_items? RBRACKET
        // list_items ::= expression ((COMMA | SEMICOLON) expression)* (PIPE expression)? {elementType = LIST_ELEMENTS}
        // The PSI class for list_items is PicatListItems (due to psiClassPrefix and rule name)
        // It should have an expressionList.
        val listItems = list.listItems
        Assertions.assertNotNull(listItems)
        val items = listItems!!.expressionList
        Assertions.assertEquals(3, items.size, "List should have 3 items")
        Assertions.assertEquals("1", items[0].text)
        Assertions.assertEquals("a", items[1].text)
        Assertions.assertEquals("f(X)", items[2].text)

        // To check if items[2] is a function call, we need to inspect its type.
        // expression -> ... -> primary_expression -> function_call
        val primaryExpression = getInnermostPrimaryExpression(items[2])
        Assertions.assertNotNull(primaryExpression)
        Assertions.assertTrue(primaryExpression!!.functionCall != null)
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
        // Attempt to find the most specific, highest-priority expression type that is a direct child.
        // This order reflects the precedence hierarchy from the BNF.
        val directChild = getDirectChild(current)

        if (directChild != null) {
            current = directChild
        }
        // If no such specific child is found, 'current' remains the original PicatExpression.
        // The subsequent 'if (current is PicatAdditiveExpression)' etc. blocks will then
        // attempt to process it. If 'current' is still just a PicatExpression and not one
        // of the handled specific types, the function will likely (and correctly) return null
        // or current if it happens to be a PicatPrimaryExpression itself.
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
        current = current.unaryExpressionList.first()?.primaryExpression
    }
    if (current is PicatUnaryExpression) {
        current = current.primaryExpression
    }
    return current as? PicatPrimaryExpression
}

private fun getDirectChild(current: PicatExpression): PsiElement? {
    val targetTypes = listOf(
        PicatBiconditionalExpressionLevel::class,
        PicatImplicationExpressionLevel::class,
        PicatConditionalExpression::class,
        PicatLogicalOrExpression::class,
        PicatLogicalAndExpression::class,
        PicatBitwiseOrExpression::class,
        PicatBitwiseXorExpression::class,
        PicatBitwiseAndExpression::class,
        PicatEqualityExpression::class,
        PicatRelationalExpression::class,
        PicatShiftExpression::class,
        PicatAdditiveExpression::class,
        PicatMultiplicativeExpression::class,
        PicatPowerExpression::class,
        PicatUnaryExpression::class,
        PicatPrimaryExpression::class
    )
    return current.children.firstOrNull { child ->
        targetTypes.any { it.isInstance(child) }
    }
}
