package com.github.avrilfanomar.picatplugin.language.navigation

import com.github.avrilfanomar.picatplugin.language.navigation.PicatGotoSymbolContributor
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatGotoSymbolContributorTest : BasePlatformTestCase() {

    fun testNamesContainPredicateAndArity() {
        myFixture.configureByText(
            "a.pi",
            """
            module a.
            
            p().
            q(X).
            r(X,Y).
            """.trimIndent()
        )
        val contrib = PicatGotoSymbolContributor()
        val names = contrib.getNames(project, /* includeNonProjectItems = */ true).toSet()
        assertTrue("Should contain simple name p", names.contains("p"))
        assertTrue("Should contain name with arity p/0", names.contains("p/0"))
        assertTrue("Should contain name with arity r/2", names.contains("r/2"))
    }

    fun testItemsByNameResolvesAcrossFiles() {
        myFixture.configureByText(
            "a.pi",
            """
            module a.
            
            p().
            q(X) :- p().
            """.trimIndent()
        )
        myFixture.configureByText(
            "b.pi",
            """
            module b.
            
            p(X).
            other() :- p(1).
            """.trimIndent()
        )
        val contrib = PicatGotoSymbolContributor()

        // Lookup by simple name should find both p/0 and p/1 heads
        val itemsByName = contrib.getItemsByName("p", "p", project, true)
        assertTrue("Expected at least 2 items for 'p'", itemsByName.size >= 2)

        // Lookup by name/arity should narrow to exactly one
        val itemsArity0 = contrib.getItemsByName("p/0", "p/0", project, true)
        val itemsArity1 = contrib.getItemsByName("p/1", "p/1", project, true)
        assertEquals("Expected 1 match for p/0", 1, itemsArity0.size)
        assertEquals("Expected 1 match for p/1", 1, itemsArity1.size)
    }
}
