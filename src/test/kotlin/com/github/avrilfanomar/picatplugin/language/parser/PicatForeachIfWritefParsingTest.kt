package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatForeachIfWritefParsingTest : BasePlatformTestCase() {

    @Test
    fun testForeachIfWritefParses() {
        val code = """
            main =>
              foreach(R in Row)
                if R > 0 then
                  writef("%2d", R)
                else
                  writef(" _")
                end
              end.
        """.trimIndent()

        myFixture.configureByText("foreach_if_writef.pi", code)
        val file = myFixture.file as PicatFileImpl

        // There should be at least one predicate rule (main)
        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(rules.isNotEmpty(), "Should have at least one predicate rule")

        // And there should be no PSI errors
        PsiTestUtils.assertNoPsiErrors(file, "foreach_if_writef")
    }
}
