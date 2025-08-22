package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatPredicateReferenceTest : BasePlatformTestCase() {

    fun testResolvesByArityBetweenP0AndP1() {
        // First: resolve p() in body to p/0
        val text1 = """
            module m.
            
            p().
            p(X).
            
            q() :- <caret>p(), p(a).
        """.trimIndent()
        val file1 = myFixture.configureByText("arity1.pi", text1)
        val offset1 = file1.text.indexOf("p(), p") // body call to p()
        myFixture.editor.caretModel.moveToOffset(offset1)
        val el1 = file1.findElementAt(offset1)
        val refs1 = el1?.references?.map { it.javaClass.name } ?: emptyList()
        val parent1 = el1?.parent
                val parentRefs1 = parent1?.references?.map { it.javaClass.name } ?: emptyList()
                println("[DEBUG_LOG] element@offset1=${el1?.javaClass?.name} text='${el1?.text}' parent=${parent1?.javaClass?.name} refs=$refs1 parentRefs=$parentRefs1")
        val ref1 = file1.findReferenceAt(offset1) ?: error("No reference at p() in body")
        val resolved1 = ref1.resolve() ?: error("p() did not resolve")
        assertTrue("Resolved element should be within the 0-arity head", resolved1.textOffset < file1.text.indexOf("p(X)"))

        // Second: resolve p(a) in body to p/1
        val text2 = """
            module m.
            
            p().
            p(X).
            
            q() :- p(), <caret>p(a).
        """.trimIndent()
        val file2 = myFixture.configureByText("arity2.pi", text2)
        val offset2 = file2.text.indexOf("p(a)") // body call to p(a)
        myFixture.editor.caretModel.moveToOffset(offset2)
        val el2 = file2.findElementAt(offset2)
        val refs2 = el2?.references?.map { it.javaClass.name } ?: emptyList()
        val parent2 = el2?.parent
                val parentRefs2 = parent2?.references?.map { it.javaClass.name } ?: emptyList()
                println("[DEBUG_LOG] element@offset2=${el2?.javaClass?.name} text='${el2?.text}' parent=${parent2?.javaClass?.name} refs=$refs2 parentRefs=$parentRefs2")
        val ref2 = file2.findReferenceAt(offset2) ?: error("No reference at p(a) in body")
        val resolved2 = ref2.resolve() ?: error("p(a) did not resolve")
        assertTrue("Resolved element should be within the 1-arity head", resolved2.textOffset > file2.text.indexOf("p(X)"))
    }
}
