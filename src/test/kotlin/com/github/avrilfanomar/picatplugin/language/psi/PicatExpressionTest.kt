@file:Suppress("GrazieInspection")

package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.impl.DebugUtil
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

    @Test
    fun testListExpressionPsi() {
        val code = "main => L = [1, a, f(X)]."
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val listExpressions = PsiTreeUtil.findChildrenOfType(file, PicatListExpression::class.java)
        Assertions.assertTrue(listExpressions.isNotEmpty(), DebugUtil.psiToString(file, false, true))

        val list = listExpressions.first() // Assuming it's the primary list in X = [...]
        Assertions.assertNotNull(list, "Should find the list [1, a, f(X)]")

        val listItems = list.listItems
        Assertions.assertNotNull(listItems)
        val items = listItems!!.expressionList
        Assertions.assertEquals(3, items.size, "List should have 3 items")
        Assertions.assertEquals("1", items[0].text)
        Assertions.assertEquals("a", items[1].text)
        Assertions.assertEquals("f(X)", items[2].text)
    }

}

