package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatFindUsagesProviderTest : BasePlatformTestCase() {

    @Test
    fun testProviderBasics() {
        val provider = PicatFindUsagesProvider()
        Assertions.assertNotNull(provider.wordsScanner, "Words scanner should be provided")
    }

    @Test
    fun testCanFindUsagesForHeadIdentifier() {
        val code = """
            fact(X) => X > 0.
        """.trimIndent()
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFileImpl

        val head = PsiTreeUtil.findChildOfType(file, PicatHead::class.java)
        Assertions.assertNotNull(head, "There should be a rule head in the PSI")
        val id = head!!.atom.identifier
        Assertions.assertNotNull(id, "Head should contain an identifier element")

        val provider = PicatFindUsagesProvider()
        Assertions.assertTrue(provider.canFindUsagesFor(id!!), "Provider should allow find usages for head identifier")
        Assertions.assertNotNull(provider.wordsScanner, "Words scanner should be provided")

        // Basic descriptor texts
        Assertions.assertEquals("identifier", provider.getType(id))
        Assertions.assertEquals("fact", provider.getDescriptiveName(id))
        Assertions.assertEquals("fact", provider.getNodeText(id, false))
    }
}
