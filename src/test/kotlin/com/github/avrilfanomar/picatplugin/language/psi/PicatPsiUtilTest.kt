package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatPsiUtilTest : BasePlatformTestCase() {

    @Test
    fun testStableSignatureForRule() {
        val code = "main(N) => foo(N, 1)."
        myFixture.configureByText("sample.pi", code)
        val file = myFixture.file as PicatFileImpl
        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertEquals(1, rules.size, "Expect one rule in sample")
        val rule = rules.first()
        val name = PicatPsiUtil.getHeadName(rule)
        val arity = PicatPsiUtil.getHeadArity(rule)
        val sig = PicatPsiUtil.getStableSignature(rule)
        Assertions.assertEquals("main", name)
        Assertions.assertEquals(1, arity)
        Assertions.assertEquals("main/1", sig)
    }

    @Test
    fun testArityZero() {
        val code = "hello => true."
        myFixture.configureByText("sample.pi", code)
        val file = myFixture.file as PicatFileImpl
        val rules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        val rule = rules.first()
        val arity = PicatPsiUtil.getHeadArity(rule)
        Assertions.assertEquals(0, arity)
    }
}
