package com.github.avrilfanomar.picatplugin.language.folding

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatFoldingBuilderTest : BasePlatformTestCase() {

    private lateinit var foldingBuilder: PicatFoldingBuilder

    override fun setUp() {
        super.setUp()
        foldingBuilder = PicatFoldingBuilder()
    }

    fun testMultilineBodyFolding() {
        val code = """
            % Test multiline body folding
            test_predicate(X) :-
                X = 1,
                Y = 2,
                Z = X + Y.
        """.trimIndent()

        val file = myFixture.configureByText("test.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        assertTrue("Should have folding descriptors for multiline body", descriptors.isNotEmpty())
        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Should have body folding with ellipsis", bodyFold)
    }

    fun testSingleLineBodyNotFolded() {
        val code = """
            % Single line body should not be folded
            simple_fact(X) :- X = 1.
        """.trimIndent()

        val file = myFixture.configureByText("simple.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val bodyFolds = descriptors.filter { it.placeholderText == "…" }
        assertTrue("Single line bodies should not be folded", bodyFolds.isEmpty())
    }

    fun testFunctionRuleFolding() {
        val code = """
            % Test function rule folding
            calculate_sum(X, Y) = Result =>
                Temp = X + Y,
                Result = Temp * 2,
                println(Result).
        """.trimIndent()

        val file = myFixture.configureByText("function.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        assertTrue("Should have folding for function rule body", descriptors.isNotEmpty())
        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Function rule body should be foldable", bodyFold)
    }

    fun testNonbacktrackablePredicateRuleFolding() {
        val code = """
            % Test nonbacktrackable predicate rule folding
            process_data(X) ?=>
                validate_input(X),
                transform_data(X),
                save_result(X).
        """.trimIndent()

        val file = myFixture.configureByText("nonbacktrack.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        assertTrue("Should have folding for nonbacktrackable rule", descriptors.isNotEmpty())
        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Nonbacktrackable rule body should be foldable", bodyFold)
    }

    fun testActionRuleFolding() {
        val code = """
            % Test action rule folding
            main =>
                read_input(Data),
                process(Data),
                write_output(Data).
        """.trimIndent()

        val file = myFixture.configureByText("action.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        assertTrue("Should have folding for action rule", descriptors.isNotEmpty())
        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Action rule body should be foldable", bodyFold)
    }

    fun testPredicateGroupFolding() {
        val code = """
            % Test predicate group folding - same functor/arity
            process_item(X) :- X = first.
            process_item(X) :- X = second.
            process_item(X) :- X = third.
            
            other_predicate(Y) :- Y = value.
        """.trimIndent()

        val file = myFixture.configureByText("group.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        assertTrue("Should have predicate group folding", groupFolds.isNotEmpty())
        
        val groupFold = groupFolds.first()
        assertTrue("Should show correct clause count", 
            groupFold.placeholderText == "+2 clauses" || groupFold.placeholderText == "+1 clause")
    }

    fun testSinglePredicateNotGrouped() {
        val code = """
            % Single predicate should not be grouped
            single_predicate(X) :- X = value.
            different_predicate(Y) :- Y = other.
        """.trimIndent()

        val file = myFixture.configureByText("single.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        assertTrue("Single predicates should not be grouped", groupFolds.isEmpty())
    }

    fun testMultiplePredicateGroups() {
        val code = """
            % Multiple groups of predicates
            first_group(A) :- A = 1.
            first_group(A) :- A = 2.
            
            second_group(B) :- B = x.
            second_group(B) :- B = y.
            second_group(B) :- B = z.
        """.trimIndent()

        val file = myFixture.configureByText("multiple.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        assertEquals("Should have 2 predicate groups", 2, groupFolds.size)
    }

    fun testMixedFactsAndRules() {
        val code = """
            % Mixed facts and rules with same functor/arity
            process(data1).
            process(X) :- X = data2, validate(X).
            process(data3).
        """.trimIndent()

        val file = myFixture.configureByText("mixed.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        assertTrue("Should fold mixed facts and rules", groupFolds.isNotEmpty())
    }

    fun testPlaceholderTextForBody() {
        val code = """
            test_body(X) :-
                X = 1,
                Y = 2.
        """.trimIndent()

        val file = myFixture.configureByText("placeholder.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Body fold should have ellipsis placeholder", bodyFold)
    }

    fun testIsCollapsedByDefault() {
        // Test that folding is not collapsed by default
        val file = myFixture.configureByText("default.pi", "test :- true.")
        assertFalse("Folding should not be collapsed by default", 
            foldingBuilder.isCollapsedByDefault(file.node))
    }

    fun testEmptyFile() {
        val code = ""
        
        val file = myFixture.configureByText("empty.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)
        
        assertTrue("Empty file should have no folding", descriptors.isEmpty())
    }

    fun testFileWithComments() {
        val code = """
            % This is a comment
            % Another comment
            test_predicate(X) :-
                % Inline comment
                X = value,
                Y = other.
        """.trimIndent()

        val file = myFixture.configureByText("comments.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        // Should still fold the body despite comments
        val bodyFold = descriptors.find { it.placeholderText == "…" }
        assertNotNull("Should fold body even with comments", bodyFold)
    }

    fun testPredicateWithDifferentArities() {
        val code = """
            % Different arities - should not be grouped
            process(X) :- X = first.
            process(X, Y) :- X = second, Y = value.
            process(X) :- X = third.
        """.trimIndent()

        val file = myFixture.configureByText("arities.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        // Different arities should not be grouped together
        assertEquals("Should not group different arity predicates", 0, groupFolds.size)
    }

    fun testQuickBuildMode() {
        val code = """
            test_predicate(X) :-
                X = 1,
                Y = 2.
        """.trimIndent()

        val file = myFixture.configureByText("quick.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        
        // Test both quick and non-quick modes
        val descriptorsQuick = foldingBuilder.buildFoldRegions(file, document, true)
        val descriptorsNormal = foldingBuilder.buildFoldRegions(file, document, false)

        assertEquals("Quick and normal modes should produce same results", 
            descriptorsNormal.size, descriptorsQuick.size)
    }

    fun testFoldingWithSyntaxErrors() {
        val code = """
            % Test with incomplete syntax
            incomplete_predicate(X) :-
                X = value,
                % Missing termination
        """.trimIndent()

        val file = myFixture.configureByText("syntax_error.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        
        // Should not crash with syntax errors
        assertDoesNotThrow {
            foldingBuilder.buildFoldRegions(file, document, false)
        }
    }

    fun testLargePredicateGroup() {
        val code = """
            % Large predicate group
            large_group(1).
            large_group(2).
            large_group(3).
            large_group(4).
            large_group(5).
        """.trimIndent()

        val file = myFixture.configureByText("large.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        val groupFolds = descriptors.filter { it.placeholderText?.contains("clause") == true }
        assertEquals("Should have one large group", 1, groupFolds.size)
        assertEquals("Should show correct count", "+4 clauses", groupFolds.first().placeholderText)
    }

    fun testEdgeCaseTextRanges() {
        val code = """
            % Edge case: very short body
            short(X) :- X.
            
            % Edge case: body at end of file
            end_body(X) :-
                X = final""".trimIndent() // No trailing newline

        val file = myFixture.configureByText("edge.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        
        assertDoesNotThrow {
            val descriptors = foldingBuilder.buildFoldRegions(file, document, false)
            // Verify ranges are valid
            descriptors.forEach { descriptor ->
                val range = descriptor.range
                assertTrue("Range start should be non-negative", range.startOffset >= 0)
                assertTrue("Range end should be after start", range.endOffset >= range.startOffset)
                assertTrue("Range end should not exceed document", range.endOffset <= document.textLength)
            }
        }
    }

    fun testGetPlaceholderTextMethod() {
        val code = """
            test_method(X) :-
                X = value,
                Y = other.
        """.trimIndent()

        val file = myFixture.configureByText("method.pi", code)
        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)!!
        val descriptors = foldingBuilder.buildFoldRegions(file, document, false)

        descriptors.forEach { descriptor ->
            val placeholder = foldingBuilder.getPlaceholderText(descriptor.element)
            assertNotNull("Placeholder text should not be null", placeholder)
            assertTrue("Placeholder should be ellipsis or clause count", 
                placeholder == "…" || placeholder.contains("clause"))
        }
    }

    private fun assertDoesNotThrow(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("Should not throw exception: ${e.message}")
        }
    }
}
