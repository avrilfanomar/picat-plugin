package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for the PicatParser class.
 * This test verifies that the parser correctly builds a PSI tree for various Picat code snippets.
 */
class PicatParserTest : BasePlatformTestCase() {

    @Test
    fun testEmptyFile() {
        // Test parsing an empty file
        val code = ""
        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that the file is empty
        assertEquals(0, file.children.size, "Empty file should have no children")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that comments are parsed correctly
        val facts = file.getAllFacts()
        assertEquals(1, facts.size, "Should have one fact")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that the module declaration is parsed correctly
        val moduleDeclarations = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration::class.java)
        assertEquals(1, moduleDeclarations.size, "Should have one module declaration")
        assertEquals(moduleDeclarations[0].getName(, "Module name should be 'example'", "example"))

        // Verify that import and export statements are parsed correctly
        val importStatements = file.getImportStatements()
        assertEquals(1, importStatements.size, "Should have one import statement")

        val exportStatements = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement::class.java)
        assertEquals(1, exportStatements.size, "Should have one export statement")

        // Verify that facts are parsed correctly
        val facts = file.getAllFacts()
        assertEquals(2, facts.size, "Should have two facts")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that the rule is parsed correctly
        val rules = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatRule::class.java)
        assertEquals(1, rules.size, "Should have one rule")

        // Verify that the rule has the correct head
        val head = rules[0].getHead()
        assertNotNull(head, "Rule should have a head")
        assertEquals(head?.text, "Head should be 'main'", "main")

        // Verify that the rule has a body
        val body = rules[0].getBody()
        assertNotNull(body, "Rule should have a body")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that the facts are parsed correctly
        val facts = file.getAllFacts()
        assertEquals(4, facts.size, "Should have four facts")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that the rules are parsed correctly
        val rules = file.findChildrenByClass(com.github.avrilfanomar.picatplugin.language.psi.PicatRule::class.java)
        assertEquals(3, rules.size, "Should have three rules")
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

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Verify that at least the second fact is parsed correctly
        val facts = file.getAllFacts()
        assertTrue(facts.size >= 1, "Should have at least one fact")
    }
}
