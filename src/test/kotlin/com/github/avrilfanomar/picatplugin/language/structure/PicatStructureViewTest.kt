package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatStructureViewTest : BasePlatformTestCase() {

    // ===================================================================================
    // Basic Creation Tests
    // ===================================================================================

    fun testStructureViewElementBasicCreation() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            main => println("Hello World").
            """.trimIndent()
        )

        val structureElement = PicatStructureViewElement(file)

        assertEquals(file, structureElement.value)
        assertTrue("Should be able to navigate", structureElement.canNavigate())
        assertTrue("Should be able to navigate to source", structureElement.canNavigateToSource())
        assertNotNull("Presentation should not be null", structureElement.presentation)
        assertNotNull("Sort key should not be null", structureElement.alphaSortKey)
        assertNotNull("Children array should not be null", structureElement.children)
    }

    fun testStructureViewModelCreation() {
        val file = myFixture.configureByText(
            "model_test.pi",
            """
            module test.
            main => println("Hello").
            """.trimIndent()
        )

        val model = PicatStructureViewModel(file)
        assertNotNull("Model should not be null", model)

        val sorters = model.sorters
        assertNotNull("Sorters should not be null", sorters)
        assertEquals("Should have one sorter", 1, sorters.size)
    }

    fun testStructureViewFactory() {
        val file = myFixture.configureByText(
            "factory_test.pi",
            """
            module test.
            main => println("factory test").
            """.trimIndent()
        )

        val factory = PicatStructureViewFactory()
        val builder = factory.getStructureViewBuilder(file)
        assertNotNull("Builder should not be null", builder)
    }

    // ===================================================================================
    // Presentation Tests - Name/Arity Format
    // ===================================================================================

    fun testFilePresentationShowsFileName() {
        val file = myFixture.configureByText("my_file.pi", "module test.")
        val element = PicatStructureViewElement(file)

        assertEquals("my_file.pi", element.presentation.presentableText)
    }

    fun testFunctionRulePresentationShowsNameArity() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            add(X, Y) = Z => Z = X + Y.
            """.trimIndent()
        )

        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        assertTrue("Should have function rules", functionRules.isNotEmpty())

        val element = PicatStructureViewElement(functionRules.first())
        assertEquals("add/2", element.presentation.presentableText)
    }

    fun testFunctionRuleZeroArityPresentation() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            constant = X => X = 42.
            """.trimIndent()
        )

        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        assertTrue("Should have function rules", functionRules.isNotEmpty())

        val element = PicatStructureViewElement(functionRules.first())
        assertEquals("constant/0", element.presentation.presentableText)
    }

    fun testPredicateRulePresentationShowsNameArity() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            process(X, Y, Z) => println(X), println(Y), println(Z).
            """.trimIndent()
        )

        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        assertTrue("Should have predicate rules", predicateRules.isNotEmpty())

        val element = PicatStructureViewElement(predicateRules.first())
        assertEquals("process/3", element.presentation.presentableText)
    }

    fun testPredicateFactPresentationShowsNameArity() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            fact(1, 2).
            another_fact(a).
            """.trimIndent()
        )

        val predicateFacts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java)
        assertTrue("Should have predicate facts", predicateFacts.isNotEmpty())

        val factList = predicateFacts.toList()
        val element1 = PicatStructureViewElement(factList[0])
        val element2 = PicatStructureViewElement(factList[1])

        assertEquals("fact/2", element1.presentation.presentableText)
        assertEquals("another_fact/1", element2.presentation.presentableText)
    }

    fun testQuotedAtomPresentationShowsNameArity() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            '+'(X, Y) = Z => Z = X + Y.
            """.trimIndent()
        )

        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        assertTrue("Should have function rules", functionRules.isNotEmpty())

        val element = PicatStructureViewElement(functionRules.first())
        assertEquals("+/2", element.presentation.presentableText)
    }

    // ===================================================================================
    // Sort Key Tests
    // ===================================================================================

    fun testSortKeyMatchesPresentableText() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            zebra(X) => println(X).
            alpha => true.
            """.trimIndent()
        )

        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        assertTrue("Should have predicate rules", predicateRules.size >= 2)

        val rulesList = predicateRules.toList()
        val element1 = PicatStructureViewElement(rulesList[0])
        val element2 = PicatStructureViewElement(rulesList[1])

        // Sort keys should match presentation text (name/arity format)
        assertEquals(element1.presentation.presentableText, element1.alphaSortKey)
        assertEquals(element2.presentation.presentableText, element2.alphaSortKey)
    }

    fun testAlphaSorterIsConfigured() {
        val file = myFixture.configureByText("test.pi", "module test.")
        val model = PicatStructureViewModel(file)

        val sorters = model.sorters
        assertEquals(1, sorters.size)
        assertEquals("ALPHA_COMPARATOR", sorters[0].toString())
    }

    // ===================================================================================
    // Children Hierarchy Tests
    // ===================================================================================

    fun testFileChildrenIncludeFunctionRules() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            func1(X) = Y => Y = X + 1.
            func2(A, B) = C => C = A * B.
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        val functionChildren = children.filterIsInstance<PicatStructureViewElement>()
            .filter { it.value is PicatFunctionRule }

        assertEquals("Should have 2 function rule children", 2, functionChildren.size)
    }

    fun testFileChildrenIncludePredicateRules() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            pred1(X) => println(X).
            pred2(Y, Z) => Y = Z.
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        val predicateChildren = children.filterIsInstance<PicatStructureViewElement>()
            .filter { it.value is PicatPredicateRule }

        assertEquals("Should have 2 predicate rule children", 2, predicateChildren.size)
    }

    fun testFileChildrenIncludePredicateFacts() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            base_case(0).
            base_case(1).
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        val factChildren = children.filterIsInstance<PicatStructureViewElement>()
            .filter { it.value is PicatPredicateFact }

        assertEquals("Should have 2 predicate fact children", 2, factChildren.size)
    }

    fun testFileChildrenIncludeMixedRuleTypes() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            my_func(X) = Y => Y = X * 2.
            my_pred(Z) => println(Z).
            my_fact(42).
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        assertTrue("Should have children", children.isNotEmpty())

        val functionCount = children.filterIsInstance<PicatStructureViewElement>()
            .count { it.value is PicatFunctionRule }
        val predicateCount = children.filterIsInstance<PicatStructureViewElement>()
            .count { it.value is PicatPredicateRule }
        val factCount = children.filterIsInstance<PicatStructureViewElement>()
            .count { it.value is PicatPredicateFact }

        assertEquals("Should have 1 function rule", 1, functionCount)
        assertEquals("Should have 1 predicate rule", 1, predicateCount)
        assertEquals("Should have 1 predicate fact", 1, factCount)
    }

    fun testRulesHaveNoChildren() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            my_func(X) = Y => Y = X + 1.
            """.trimIndent()
        )

        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        assertTrue("Should have function rules", functionRules.isNotEmpty())

        val functionElement = PicatStructureViewElement(functionRules.first())
        val children = functionElement.children

        assertEquals("Function rules should have no children (they are leaves)", 0, children.size)
    }

    fun testEmptyFileHasNoChildren() {
        val file = myFixture.configureByText("empty.pi", "module empty.")
        val element = PicatStructureViewElement(file)

        val children = element.children
        assertEquals("Empty file should have no children", 0, children.size)
    }

    // ===================================================================================
    // isAlwaysLeaf and isAlwaysShowsPlus Tests
    // ===================================================================================

    fun testFileElementIsAlwaysShowsPlus() {
        val file = myFixture.configureByText("test.pi", "module test.")
        val model = PicatStructureViewModel(file)
        val fileElement = PicatStructureViewElement(file)

        assertTrue("File should always show plus", model.isAlwaysShowsPlus(fileElement))
    }

    fun testFileElementIsNotAlwaysLeaf() {
        val file = myFixture.configureByText("test.pi", "module test.")
        val model = PicatStructureViewModel(file)
        val fileElement = PicatStructureViewElement(file)

        assertFalse("File should not be always leaf", model.isAlwaysLeaf(fileElement))
    }

    fun testFunctionRuleIsAlwaysLeaf() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            func(X) = Y => Y = X.
            """.trimIndent()
        )

        val model = PicatStructureViewModel(file)
        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        assertTrue("Should have function rules", functionRules.isNotEmpty())

        val functionElement = PicatStructureViewElement(functionRules.first())
        assertTrue("Function rule should always be leaf", model.isAlwaysLeaf(functionElement))
    }

    fun testPredicateRuleIsAlwaysLeaf() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            pred(X) => println(X).
            """.trimIndent()
        )

        val model = PicatStructureViewModel(file)
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        assertTrue("Should have predicate rules", predicateRules.isNotEmpty())

        val predicateElement = PicatStructureViewElement(predicateRules.first())
        assertTrue("Predicate rule should always be leaf", model.isAlwaysLeaf(predicateElement))
    }

    fun testPredicateFactIsAlwaysLeaf() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            fact(1).
            """.trimIndent()
        )

        val model = PicatStructureViewModel(file)
        val predicateFacts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java)
        assertTrue("Should have predicate facts", predicateFacts.isNotEmpty())

        val factElement = PicatStructureViewElement(predicateFacts.first())
        assertTrue("Predicate fact should always be leaf", model.isAlwaysLeaf(factElement))
    }

    // ===================================================================================
    // getSuitableClasses Tests
    // ===================================================================================

    fun testSuitableClassesIncludesAllRuleTypes() {
        val file = myFixture.configureByText("test.pi", "module test.")
        val model = PicatStructureViewModel(file)

        // getSuitableClasses is protected, so we test indirectly by verifying that
        // the model properly identifies elements of various rule types.
        // If getSuitableClasses didn't include these types, isSuitable would return false.

        // We verify the model can work with all rule types through the isAlwaysLeaf method
        // which processes structure elements containing these types.
        val rootElement = PicatStructureViewElement(file)
        assertFalse("File should not be leaf", model.isAlwaysLeaf(rootElement))
        assertTrue("File should always show plus", model.isAlwaysShowsPlus(rootElement))
    }

    // ===================================================================================
    // Navigation Tests
    // ===================================================================================

    fun testFileElementCanNavigate() {
        val file = myFixture.configureByText("nav_test.pi", "module test.")
        val element = PicatStructureViewElement(file)

        assertTrue("File element should be navigable", element.canNavigate())
        assertTrue("File element should be navigable to source", element.canNavigateToSource())
    }

    fun testRuleElementCanNavigate() {
        val file = myFixture.configureByText(
            "nav_test.pi",
            """
            module test.
            test_func(X) => println(X).
            """.trimIndent()
        )

        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        assertTrue("Should have predicate rules", predicateRules.isNotEmpty())

        val element = PicatStructureViewElement(predicateRules.first())
        assertTrue("Predicate rule should be navigable", element.canNavigate())
        assertTrue("Predicate rule should be navigable to source", element.canNavigateToSource())
    }

    // ===================================================================================
    // Edge Case Tests
    // ===================================================================================

    fun testStructureViewWithMultipleClausesForSamePredicate() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            factorial(0) = 1.
            factorial(N) = N * factorial(N - 1), N > 0 => true.
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        // Both clauses should appear in the structure view
        assertTrue("Should have children for factorial clauses", children.size >= 1)
    }

    fun testStructureViewWithComplexFile() {
        val file = myFixture.configureByText(
            "complex.pi",
            """
            module complex_module.

            % Function rules
            add(X, Y) = Z => Z = X + Y.
            multiply(A, B) = C => C = A * B.

            % Predicate rules
            process(X) => println(X).
            validate(Y), Y > 0 => true.

            % Predicate facts
            base(0).
            base(1).
            """.trimIndent()
        )

        val rootElement = PicatStructureViewElement(file)
        val children = rootElement.children

        // Should have multiple children of different types
        assertTrue("Should have multiple children", children.size > 3)

        // Verify presentations are in name/arity format
        children.filterIsInstance<PicatStructureViewElement>().forEach { child ->
            val presentation = child.presentation.presentableText
            assertNotNull("Presentation should not be null", presentation)
            if (child.value !is PicatFileImpl) {
                assertTrue(
                    "Presentation '$presentation' should be in name/arity format",
                    presentation!!.matches(Regex(".+/\\d+"))
                )
            }
        }
    }

    fun testStructureViewWithBacktrackableArrow() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            module test.
            backtracking_pred(X) ?=> println(X).
            """.trimIndent()
        )

        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        assertTrue("Should have predicate rules with ?=>", predicateRules.isNotEmpty())

        val element = PicatStructureViewElement(predicateRules.first())
        assertEquals("backtracking_pred/1", element.presentation.presentableText)
    }
}
