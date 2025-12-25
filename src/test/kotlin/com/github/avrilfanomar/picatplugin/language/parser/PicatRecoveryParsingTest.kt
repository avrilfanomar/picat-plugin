package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatRecoveryParsingTest : BasePlatformTestCase() {

    @Test
    fun testListItemsRecover() {
        val code = """
            import cp.
            
            main => go.
            
            go =>
              X = [1,2,],
              Y = 3.
        """.trimIndent()
        myFixture.configureByText("recover_list_items.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        // main and go rules should both parse
        Assertions.assertTrue(rules.size >= 2, "Should parse at least main and go rules despite trailing comma in list")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("[1,2"), "Go body should contain list literal text")
        Assertions.assertTrue(goRule.text.contains("Y = 3"), "Parsing should continue after the malformed list to the next statement")
    }

    @Test
    fun testListComprehensionTailRecover() {
        val code = """
            import cp.
            
            main => go.
            
            go =>
              L = [X : X in 1..3,],
              Z = 1.
        """.trimIndent()
        myFixture.configureByText("recover_list_comp.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse at least main and go rules despite trailing comma in list comprehension tail")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        val goText = goRule!!.text
        Assertions.assertTrue(goText.contains("[X : X in 1..3"), "Comprehension head should be parsed")
        Assertions.assertTrue(goText.contains("Z = 1"), "Parsing should recover and continue after comprehension to next statement")
    }

    @Test
    fun testConjunctionRecover() {
        val code = """
            import cp.

            main => go.

            go =>
              true, , true.
        """.trimIndent()
        myFixture.configureByText("recover_conjunction.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse at least main and go rules despite malformed conjunction")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        val goText = goRule!!.text
        // Ensure both 'true' occurrences are still in the parsed body text
        Assertions.assertTrue(goText.contains("true"), "Go body should contain 'true' goals")
    }

    @Test
    fun testHeadArgsRecover() {
        // Tests recovery when there's a malformed argument in predicate head
        val code = """
            main => go.

            foo(X, , Y) => true.

            go => foo(1, 2, 3).
        """.trimIndent()
        myFixture.configureByText("recover_head_args.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        // Despite malformed head args, we should still parse main and go rules
        Assertions.assertTrue(rules.size >= 2, "Should parse main and go rules despite malformed head args")
        Assertions.assertTrue(rules.any { it.text.startsWith("go") }, "Go rule should be present")
    }

    @Test
    fun testIfConditionRecover() {
        // Tests recovery when there's a malformed if condition
        val code = """
            main => go.

            go =>
              if , then
                X = 1
              end,
              Y = 2.
        """.trimIndent()
        myFixture.configureByText("recover_if_condition.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed if condition")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Y = 2"), "Should continue parsing after malformed if")
    }

    @Test
    fun testWhileConditionRecover() {
        // Tests recovery when there's a malformed while condition
        val code = """
            main => go.

            go =>
              X = 0,
              while (, )
                X := X + 1
              end,
              Y = X.
        """.trimIndent()
        myFixture.configureByText("recover_while_condition.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed while condition")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Y = X"), "Should continue parsing after malformed while")
    }

    @Test
    fun testTryCatchRecover() {
        // Tests recovery when there's a malformed try-catch
        val code = """
            main => go.

            go =>
              try
                X = , 1
              catch(E)
                println(E)
              end,
              Y = 2.
        """.trimIndent()
        myFixture.configureByText("recover_try_catch.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed try body")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Y = 2"), "Should continue parsing after malformed try")
    }

    @Test
    fun testThrowRecover() {
        // Tests recovery when there's a malformed throw statement
        val code = """
            main => go.

            go =>
              throw(, error),
              X = 1.
        """.trimIndent()
        myFixture.configureByText("recover_throw.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed throw")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("X = 1"), "Should continue parsing after malformed throw")
    }

    @Test
    fun testIndexAccessRecover() {
        // Tests recovery when there's a malformed index access (trailing comma)
        val code = """
            main => go.

            go =>
              A = new_array(10),
              X = A[1, ],
              Y = 2.
        """.trimIndent()
        myFixture.configureByText("recover_index_access.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed index access")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Y = 2"), "Should continue parsing after malformed index access")
    }

    @Test
    fun testParenthesizedGoalRecover() {
        // Tests recovery when there's a malformed parenthesized goal
        val code = """
            main => go.

            go =>
              ( , true ),
              X = 1.
        """.trimIndent()
        myFixture.configureByText("recover_paren_goal.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed parenthesized goal")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("X = 1"), "Should continue parsing after malformed parens")
    }

    @Test
    fun testFunctionRuleTailRecover() {
        // Tests recovery when there's a malformed function rule tail
        val code = """
            main => go.

            foo(X) = Y, X > 0 => Y = , X.

            go => println(foo(5)).
        """.trimIndent()
        myFixture.configureByText("recover_func_rule_tail.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        // Should still parse the go rule despite malformed function
        Assertions.assertTrue(rules.any { it.text.startsWith("go") }, "Go rule should be present")
    }

    @Test
    fun testMultipleRecoveryScenarios() {
        // Tests that recovery works across multiple malformed constructs
        val code = """
            main => go.

            go =>
              X = [1, , 2],
              if , then
                Y = 1
              end,
              Z = 3.
        """.trimIndent()
        myFixture.configureByText("recover_multiple.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite multiple malformed constructs")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Z = 3"), "Should continue parsing after multiple errors")
    }

    @Test
    fun testLoopWhileRecover() {
        // Tests recovery when there's a malformed loop-while condition
        val code = """
            main => go.

            go =>
              X = 0,
              loop
                X := X + 1
              while (, ),
              Y = X.
        """.trimIndent()
        myFixture.configureByText("recover_loop_while.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed loop-while condition")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
    }

    @Test
    fun testCatchPatternRecover() {
        // Tests recovery when there's a malformed catch pattern
        val code = """
            main => go.

            go =>
              try
                X = 1
              catch(, E)
                println(E)
              end,
              Y = 2.
        """.trimIndent()
        myFixture.configureByText("recover_catch_pattern.pi", code)
        val file = myFixture.file as PicatFileImpl

        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.size >= 2, "Should parse rules despite malformed catch pattern")

        val goRule = rules.find { it.text.startsWith("go") }
        Assertions.assertNotNull(goRule, "Go rule should be present")
        Assertions.assertTrue(goRule!!.text.contains("Y = 2"), "Should continue parsing after malformed catch")
    }
}
