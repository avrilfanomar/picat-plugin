package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatFindUsagesProviderTest : BasePlatformTestCase() {

    fun testProviderRegisteredAndCanFindUsagesForIdentifier() {
        val text = """
            module test.
            
            p().
            p().
            
            q() :- p().
        """.trimIndent()

        val file = myFixture.configureByText("test.pi", text)
        // Place the caret at the first occurrence of p
        val offset = file.text.indexOf("p()")
        myFixture.editor.caretModel.moveToOffset(offset)
        val element = file.findElementAt(offset) ?: error("No element at caret")

        val provider = PicatFindUsagesProvider()
        // Sanity: provider constructed
        assertTrue("Provider should allow finding usages for identifier", provider.canFindUsagesFor(element))

        val usages = myFixture.findUsages(element)
        assertTrue("Expected at least one usage of 'p'", usages.isNotEmpty())
    }
}
