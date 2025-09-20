package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatMinimalFibpParsingTest : BasePlatformTestCase() {

    @Test
    fun testFibpRulesParsing() {
        val code = """
            /* minimal fibp */
            fibp(0, F) => F = 1.
            fibp(1, F) => F = 1.
            fibp(N, F), N > 1 => fibp(N - 1, F1), fibp(N - 2, F2), F = F1 + F2.
        """.trimIndent()
        myFixture.configureByText("fibp.pi", code)
        val file = myFixture.file as PicatFileImpl
        println("[DEBUG_LOG] MINIMAL_FIBP PSI Tree:\n" + DebugUtil.psiToString(file, true))
        PsiTestUtils.assertNoPsiErrorsWithDetailedLogging(file, "minimal fibp")
    }
}
