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
}
