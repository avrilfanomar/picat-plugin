package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

class PicatDcgParsingTest : BasePlatformTestCase() {

    @Test
    fun testSimpleDcgRule() {
        val code = """
            sentence --> noun_phrase, verb_phrase.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "simple DCG rule")
    }

    @Test
    fun testDcgRuleWithTerminal() {
        val code = """
            greeting --> [hello], name.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "DCG rule with terminal")
    }

    @Test
    fun testDcgRuleWithArgs() {
        val code = """
            expr(X+Y) --> expr(X), ['+'], expr(Y).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "DCG rule with arguments")
    }

    @Test
    fun testDcgRuleWithAlternatives() {
        val code = """
            digit(D) --> [D], {D >= 0, D =< 9}.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "DCG rule with pushback/goals")
    }

    @Test
    fun testMultipleDcgRules() {
        val code = """
            sentence --> noun_phrase, verb_phrase.
            noun_phrase --> determiner, noun.
            verb_phrase --> verb, noun_phrase.
            determiner --> [the].
            determiner --> [a].
            noun --> [cat].
            noun --> [dog].
            verb --> [chases].
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "multiple DCG rules")
    }

    @Test
    fun testIfThenArrowStillWorks() {
        val code = """
            test(X) => (X > 0 -> println(positive) ; println(negative)).
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl
        PsiTestUtils.assertNoPsiErrors(file, "if-then arrow still works")
    }
}
