package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
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
        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that the file is empty
        assertEquals("Empty file should have no children", 0, file.children.size)
    }

    @Test
    fun testFunctionWithComments() {
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
        val functionDefinitions = file.getFunctions()
        assertEquals("Should have one function", 1, functionDefinitions.size)
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
        val moduleDeclarations = file.findChildrenByClass(PicatModuleDeclaration::class.java)
        assertEquals("Should have one module declaration", 1, moduleDeclarations.size)
        assertEquals("Module name should be 'example'", "example", moduleDeclarations[0].getName())

        // Verify that import and export statements are parsed correctly
        val importStatements = file.getImportStatements()
        assertEquals("Should have one import statement", 1, importStatements.size)

        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)
        assertEquals("Should have one export statement", 1, exportStatements.size)

        // Verify that facts are parsed correctly
        val functionDefinitions = file.getFunctions()
        assertEquals("Should have two functions", 2, functionDefinitions.size)
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
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertTrue("Should have at least one rule", rules.size >= 1)

        // Find the main rule
        val mainRule = rules.find { it.getHead()?.text == "main" }
        assertNotNull("Should have a rule with head 'main'", mainRule)

        // Verify that the rule has a body
        val body = mainRule?.getBody()
        assertNotNull("Rule should have a body", body)
    }

    @Test
    fun testPatternMatching() {
        // Test parsing pattern matching
        val code = """
            custom_length([]) = 0.
            custom_length([_|Xs]) = 1 + custom_length(Xs).

            custom_sum([]) = 0.
            custom_sum([X|Xs]) = X + custom_sum(Xs).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that functions are parsed correctly
        val functions = file.getFunctions()
        assertTrue("Should have at least 2 functions", functions.size >= 2)

        // Find the specific functions
        val customLengthFunctions = functions.filter { it.getName() == "custom_length" }
        val customSumFunctions = functions.filter { it.getName() == "custom_sum" }

        assertTrue("Should have at least one custom_length function", customLengthFunctions.isNotEmpty())
        assertTrue("Should have at least one custom_sum function", customSumFunctions.isNotEmpty())
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
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertTrue("Should have at least three rules", rules.size >= 3)

        // Find the specific rules
        val factorialRule = rules.find { it.getHead()?.text?.startsWith("factorial") == true }
        val processListRule = rules.find { it.getHead()?.text?.startsWith("process_list") == true }
        val countToRule = rules.find { it.getHead()?.text?.startsWith("count_to") == true }

        assertNotNull("Should have a rule for factorial", factorialRule)
        assertNotNull("Should have a rule for process_list", processListRule)
        assertNotNull("Should have a rule for count_to", countToRule)
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

    @Test
    fun testAtomParsing() {
        // Test parsing different types of atoms
        val code = """
            simple_atom.
            'quoted atom'.
            atom_with_underscore.
            atomWithMixedCase.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that atoms are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have four facts", 4, facts.size)

        // Check that the facts have heads
        facts.forEach { fact ->
            assertNotNull("Fact should have a head", fact.getHead())
        }
    }

    @Test
    fun testStructureParsing() {
        // Test parsing structures (compound terms)
        val code = """
            point(1, 2).
            person('John', 30, male).
            nested(point(3, 4), circle(5)).
            empty().
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that structures are parsed correctly
        val facts = file.getAllFacts()
        assertEquals("Should have four facts", 4, facts.size)

        // Check that the facts have heads
        facts.forEach { fact ->
            assertNotNull("Fact should have a head", fact.getHead())
        }

        // Check for function definitions
        val functions = file.getFunctions()
        assertEquals("Should have no functions", 0, functions.size)
    }

    @Test
    fun testRuleParsing() {
        // Test parsing different types of rules
        val code = """
            % Explicit rule with arrow operator
            factorial(0) => 1.

            % Explicit rule with condition
            factorial(N) => N * factorial(N-1) => N > 0.

            % Backtrackable rule
            fibonacci(0) ?=> 0.
            fibonacci(1) ?=> 1.

            % Rule with biconditional operator
            even(N) <=> N mod 2 == 0.

            % Rule with traditional Prolog operator
            ancestor(X, Y) :- parent(X, Y).
            ancestor(X, Z) :- parent(X, Y), ancestor(Y, Z).

            % Implicit rule (no explicit operator)
            main
                println("Hello, world!").
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that rules are parsed correctly
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertTrue("Should have at least 7 rules", rules.size >= 7)

        // Check for different rule operators
        val ruleTypes = rules.mapNotNull { it.getRuleType() }

        assertTrue("Should contain '=>' operator", ruleTypes.contains("=>"))
        assertTrue("Should contain '?=>' operator", ruleTypes.contains("?=>"))
        assertTrue("Should contain '<=>' operator", ruleTypes.contains("<=>"))
        assertTrue("Should contain ':-' operator", ruleTypes.contains(":-"))
    }

    @Test
    fun testFunctionParsing() {
        // Test parsing function definitions
        val code = """
            % Simple function
            square(X) = X * X.

            % Function with condition
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            % Function with complex expression
            distance(point(X1, Y1), point(X2, Y2)) = sqrt((X2 - X1)*(X2 - X1) + (Y2 - Y1)*(Y2 - Y1)).

            % Function with pattern matching
            sum([]) = 0.
            sum([H|T]) = H + sum(T).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that functions are parsed correctly
        val functions = file.getFunctions()
        assertTrue("Should have at least 3 functions", functions.size >= 3)

        // Check function names
        val functionNames = functions.mapNotNull { it.getName() }.distinct()
        assertTrue("Should have at least 3 distinct function names", functionNames.size >= 3)
        assertTrue("Should contain 'square'", functionNames.contains("square"))
        assertTrue("Should contain 'factorial'", functionNames.contains("factorial"))
        assertTrue("Should contain 'sum'", functionNames.contains("sum"))
    }

    @Test
    fun testExpressionParsing() {
        // Test parsing different types of expressions
        val code = """
            test_expressions =>
                % Arithmetic expressions
                A = 1 + 2 * 3,
                B = (1 + 2) * 3,
                C = 2 ** 3 + 4,

                % Relational expressions
                D = A > B,
                E = C <= 10,
                F = A == B,

                % Logical expressions
                G = A > 0 and B < 10,
                H = C > 10 or D == true,
                I = not G,

                % Ternary expression
                J = A > B ? "greater" : "less or equal",

                % List expressions
                K = [1, 2, 3],
                L = [A | K],

                % Map expressions
                M = {a:1, b:2, c:3},

                % Function calls
                N = factorial(5),
                O = length([1, 2, 3]),

                println(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that expressions are parsed correctly
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertTrue("Should have at least one rule", rules.size >= 1)

        // Find the test_expressions rule
        val testExpressionsRule = rules.find { it.getHead()?.text == "test_expressions" }
        assertNotNull("Should have a rule with head 'test_expressions'", testExpressionsRule)

        // Check that the rule has a body
        val body = testExpressionsRule?.getBody()
        assertNotNull("Rule should have a body", body)
    }

    @Test
    fun testPatternParsing() {
        // Test parsing different types of patterns
        val code = """
            % Variable patterns
            match(X, X) => true.

            % Atom patterns
            match(atom, atom) => true.

            % Structure patterns
            match(point(X, Y), point(X, Y)) => true.

            % List patterns
            match([], []) => true.
            match([H|T], [H|T]) => true.

            % Mixed patterns
            process(person(Name, Age, _), [Name, Age]).

            % Pattern in function definition
            sum([]) = 0.
            sum([H|T]) = H + sum(T).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Verify that patterns are parsed correctly
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertTrue("Should have multiple rules", rules.size >= 5)

        // Check for patterns in rule heads
        val heads = rules.mapNotNull { it.getHead() }
        assertEquals("Should have the same number of heads as rules", rules.size, heads.size)

        // Check for function definitions with patterns
        val functions = file.getFunctions()
        assertTrue("Should have at least 2 functions", functions.size >= 2)
    }
}
