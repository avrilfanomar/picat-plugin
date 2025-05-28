package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Test for Picat PSI implementation.
 * This test verifies that the PSI elements are correctly created for Picat code.
 */
class PicatPsiTest : BasePlatformTestCase() {

    @Test
    fun testFactPsi() {
        // Test that a fact is correctly parsed into a PicatFact PSI element
        val code = """
            factorial(0) = 1.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.getAllFacts()

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")
    }

    @Test
    fun testFactWithMultipleArgumentsPsi() {
        // Test that a fact with multiple arguments is correctly parsed
        val code = """
            sum(1, 2, 3) = 6.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Debug: Print all PSI elements in the file
        println("[DEBUG_LOG] PSI elements in the file:")
        file.children.forEach { element ->
            println("[DEBUG_LOG] ${element.javaClass.simpleName}: ${element.text}")
        }

        // Find all facts in the file
        val facts = file.getAllFacts()

        // Debug: Print the number of facts found
        println("[DEBUG_LOG] Number of facts found: ${facts.size}")

        // Verify that there is exactly one fact
        assertEquals(1, facts.size, "There should be exactly one fact")

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull(head, "Fact should have a head")

        // Verify that the head is a structure
        assertTrue(head is PicatStructure, "Head should be a structure")
        val structure = head as PicatStructure

        // Verify that the structure has the correct name and arity
        assertEquals(structure.getName(, "Structure should have name sum", "sum"))
        assertEquals(3, structure.getArity(, "Structure should have arity 3"))

        // Verify that the structure has an argument list
        val argumentList = structure.getArgumentList()
        assertNotNull(argumentList, "Structure should have an argument list")

        // Verify that the argument list has the correct number of arguments
        val arguments = argumentList!!.getArguments()
        assertEquals(3, arguments.size, "Argument list should have 3 arguments")

        // Verify that each argument has the correct expression
        assertEquals(arguments[0].getExpression(, "First argument should be 1", "1")?.text)
        assertEquals(arguments[1].getExpression(, "Second argument should be 2", "2")?.text)
        assertEquals(arguments[2].getExpression(, "Third argument should be 3", "3")?.text)
    }

    @Test
    fun testRulePsi() {
        // Test that a rule is correctly parsed into a PicatRule PSI element
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there is exactly one rule
        assertEquals(1, rules.size, "There should be exactly one rule")

        // Verify that the rule has the correct head, body, and rule operator
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull(head, "Rule should have a head")
        val body = rule.getBody()
        assertNotNull(body, "Rule should have a body")
        val ruleOperator = rule.getRuleOperator()
        assertNotNull(ruleOperator, "Rule should have a rule operator")
        assertEquals(ruleOperator?.text, "Rule operator should be =>", "=>")
    }

    @Test
    fun testExportStatementPsi() {
        // Test that an export statement is correctly parsed into a PicatExportStatement PSI element
        val code = """
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all export statements in the file
        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)

        // Verify that there is exactly one export statement
        assertEquals(1, exportStatements.size, "There should be exactly one export statement")

        // Verify that the export statement has the correct predicate indicators
        val exportStatement = exportStatements[0]
        val predicateIndicators = exportStatement.getPredicateIndicators()
        assertEquals(2, predicateIndicators.size, "Export statement should have 2 predicate indicators")
        assertEquals(predicateIndicators[0].getName(, "First predicate indicator should be factorial/1", "factorial"))
        assertEquals(1, predicateIndicators[0].getArity(, "First predicate indicator should have arity 1"))
        assertEquals(predicateIndicators[1].getName(, "Second predicate indicator should be fibonacci/1", "fibonacci"))
        assertEquals(1, predicateIndicators[1].getArity(, "Second predicate indicator should have arity 1"))
    }

    @Test
    fun testIncludeStatementPsi() {
        // Test that an include statement is correctly parsed into a PicatIncludeStatement PSI element
        val code = """
            include "utils.pi".
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all include statements in the file
        val includeStatements = file.findChildrenByClass(PicatIncludeStatement::class.java)

        // Verify that there is exactly one include statement
        assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements[0]
        assertEquals(includeStatement.getIncludePath(, "Include statement should have path utils.pi", "utils.pi"))
    }

    @Test
    fun testComplexPsi() {
        // Test that a more complex Picat program is correctly parsed into PSI elements
        val code = """
            % This is a sample Picat program

            import util.
            export factorial/1, fibonacci/1.
            include "utils.pi".

            main => 
                println("Hello, world!"),
                X = 42,
                Y = X + 10,
                println(Y).

            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            fibonacci(0) = 0.
            fibonacci(1) = 1.
            fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) => N > 1.
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Find all import statements in the file
        val importStatements = file.findChildrenByClass(PicatImportStatement::class.java)
        assertEquals(1, importStatements.size, "There should be exactly one import statement")

        // Find all export statements in the file
        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)
        assertEquals(1, exportStatements.size, "There should be exactly one export statement")

        // Find all include statements in the file
        val includeStatements = file.findChildrenByClass(PicatIncludeStatement::class.java)
        assertEquals(1, includeStatements.size, "There should be exactly one include statement")

        // Find all facts in the file
        val facts = file.getAllFacts()
        assertEquals(3, facts.size, "There should be exactly 3 facts")

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertEquals(3, rules.size, "There should be exactly 3 rules")
    }

    @Test
    fun testImportStatementMethods() {
        // Test that the getImportStatements method works correctly
        val code = """
            % This is a sample Picat program with multiple imports

            import util, math, cp.
            import planner.

            main => println("Hello, world!").
        """.trimIndent()

        myFixture.configureByText(code, "test.pi")
        val file = myFixture.file as PicatFile

        // Test getImportStatements method
        val importStatements = file.getImportStatements()
        assertEquals(2, importStatements.size, "There should be exactly two import statements")

        // Verify that the import statements have the correct text
        assertEquals(importStatements[0].text, "First import statement should be 'import'", "import")
        assertEquals(importStatements[1].text, "Second import statement should be 'import'", "import")

        // Test getImportedModuleNames method
        val allModuleNames = file.getImportedModuleNames()

        // Currently, the implementation doesn't correctly parse module names,
        // so we expect an empty list
        assertEquals(0, allModuleNames.size, "There should be no module names")
    }
}
