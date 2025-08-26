package com.github.avrilfanomar.picatplugin.language.navigation

import com.github.avrilfanomar.picatplugin.language.PicatStructureAwareNavbar
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatNavbarContributorTest : BasePlatformTestCase() {

    fun testBreadcrumbsInsideRuleBody() {
        val psiFile = myFixture.configureByText(
            "a.pi",
            """
            module a.
            
            p().
            r(X,Y) :- p(), X = 1, <caret>Y = 2.
            """.trimIndent()
        )

        val caretOffset = myFixture.caretOffset
        val leaf = psiFile.findElementAt(caretOffset)!!

        val ext = PicatStructureAwareNavbar()
        val chain = buildChain(ext, leaf)
        val texts = chain.map { ext.getPresentableText(it) }

        // Expect breadcrumbs to include file and the function rule r/2
        assertTrue("Breadcrumbs should include file name", texts.firstOrNull()?.endsWith("a.pi") == true)
        val lastRule = texts.lastOrNull { it != null && it.contains("/") }
        assertEquals("r/2", lastRule)
    }

    fun testBreadcrumbsOnHead() {
        val psiFile = myFixture.configureByText(
            "a.pi",
            """
            module a.
            
            r(X)<caret>.
            """.trimIndent()
        )

        val caretOffset = myFixture.caretOffset
        val leaf = psiFile.findElementAt(caretOffset)!!

        val ext = PicatStructureAwareNavbar()
        val chain = buildChain(ext, leaf)
        val texts = chain.map { ext.getPresentableText(it) }

        assertTrue(texts.firstOrNull()?.endsWith("a.pi") == true)
        val lastRule = texts.lastOrNull { it != null && it.contains("/") }
        assertEquals("r/1", lastRule)
    }

    private fun buildChain(
        ext: PicatStructureAwareNavbar,
        start: com.intellij.psi.PsiElement
    ): List<com.intellij.psi.PsiElement> {
        val out = ArrayList<com.intellij.psi.PsiElement>()
        val file = start.containingFile
        if (file != null) out.add(file)

        generateSequence(start) { prev -> ext.getParent(prev) }
            .drop(1) // skip the starting element itself
            .takeWhile { it != file }
            .distinct()
            .forEach { out.add(it) }

        return out
    }
}
