package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatParserTest : BasePlatformTestCase() {

    @Test
    fun testEmptyFile() {
        // Test parsing an empty file
        val code = ""
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the file is empty
        assertEquals("Empty file should have no children", 0, file.children.size)
    }

    @Test
    fun testComments() {
        // Test parsing comments
        val code = """
            % This is a line comment
            /* This is a
               multi-line comment */
            factorial(0) = 1. % Comment after code
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that comments are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have one fact", 1, facts.size)
    }

    @Test
    fun testModuleDeclaration() {
        // Test parsing a module declaration
        val code = """
            module example.

            import util.
            export factorial/1, fibonacci/1.

            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the module declaration is parsed correctly
        val moduleDeclarations = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration::class.java)
        assertEquals("Should have one module declaration", 1, moduleDeclarations.size)
        assertEquals("Module name should be 'example'", "example", moduleDeclarations[0].getName())

        // Verify that import and export statements are parsed correctly
        val importStatements = file.getImportStatements()
        assertEquals("Should have one import statement", 1, importStatements.size)

        val exportStatements = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement::class.java)
        assertEquals("Should have one export statement", 1, exportStatements.size)

        // Verify that facts are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have two facts", 2, facts.size)
    }

    @Test
    fun testComplexExpression() {
        // Test parsing a complex expression
        val code = """
            main =>
                X = 10,
                Y = 20,
                Z = X + Y * (2 - 1) / 3,
                println(Z).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatRule::class.java)
        assertEquals("Should have one rule", 1, rules.size)

        // Verify that the rule has the correct head
        val head = rules[0].getHead()
        assertNotNull("Rule should have a head", head)
        assertEquals("Head should be 'main'", "main", head?.text)

        // Verify that the rule has a body
        val body = rules[0].getBody()
        assertNotNull("Rule should have a body", body)
    }

    @Test
    fun testPatternMatching() {
        // Test parsing pattern matching
        val code = """
            length([]) = 0.
            length([_|Xs]) = 1 + length(Xs).

            sum([]) = 0.
            sum([X|Xs]) = X + sum(Xs).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the facts are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have four facts", 4, facts.size)
    }

    @Test
    fun testControlStructures() {
        // Test parsing control structures
        val code = """
            factorial(N) = Fact =>
                if N == 0 then
                    Fact = 1
                else
                    Fact = N * factorial(N-1)
                end.

            process_list(List) =>
                foreach X in List
                    println(X)
                end.

            count_to(N) =>
                I = 1,
                while I <= N do
                    println(I),
                    I := I + 1
                end.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the rules are parsed correctly
        val rules = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatRule::class.java)
        assertEquals("Should have three rules", 3, rules.size)
    }

    @Test
    fun testErrorRecovery() {
        // Test that the parser can recover from syntax errors
        val code = """
            % Missing dot at the end of this fact
            factorial(0) = 1

            % This should still be parsed correctly
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that at least the second fact is parsed correctly
        val facts = file.getAllFacts()
        assertTrue("Should have at least one fact", facts.size >= 1)
    }
}
