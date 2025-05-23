package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)

        // Verify that there is exactly one fact
        assertEquals("There should be exactly one fact", 1, facts.size)

        // Verify that the fact has the correct head
        val fact = facts[0]
        val head = fact.getHead()
        assertNotNull("Fact should have a head", head)
    }

    @Test
    fun testRulePsi() {
        // Test that a rule is correctly parsed into a PicatRule PSI element
        val code = """
            factorial(N) = N * factorial(N-1) => N > 0.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)

        // Verify that there is exactly one rule
        assertEquals("There should be exactly one rule", 1, rules.size)

        // Verify that the rule has the correct head, body, and rule operator
        val rule = rules[0]
        val head = rule.getHead()
        assertNotNull("Rule should have a head", head)
        val body = rule.getBody()
        assertNotNull("Rule should have a body", body)
        val ruleOperator = rule.getRuleOperator()
        assertNotNull("Rule should have a rule operator", ruleOperator)
        assertEquals("Rule operator should be =>", "=>", ruleOperator?.text)
    }

    @Test
    fun testExportStatementPsi() {
        // Test that an export statement is correctly parsed into a PicatExportStatement PSI element
        val code = """
            export factorial/1, fibonacci/1.
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all export statements in the file
        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)

        // Verify that there is exactly one export statement
        assertEquals("There should be exactly one export statement", 1, exportStatements.size)

        // Verify that the export statement has the correct predicate indicators
        val exportStatement = exportStatements[0]
        val predicateIndicators = exportStatement.getPredicateIndicators()
        assertEquals("Export statement should have 2 predicate indicators", 2, predicateIndicators.size)
        assertEquals("First predicate indicator should be factorial/1", "factorial", predicateIndicators[0].getName())
        assertEquals("First predicate indicator should have arity 1", 1, predicateIndicators[0].getArity())
        assertEquals("Second predicate indicator should be fibonacci/1", "fibonacci", predicateIndicators[1].getName())
        assertEquals("Second predicate indicator should have arity 1", 1, predicateIndicators[1].getArity())
    }

    @Test
    fun testIncludeStatementPsi() {
        // Test that an include statement is correctly parsed into a PicatIncludeStatement PSI element
        val code = """
            include "utils.pi".
        """.trimIndent()

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all include statements in the file
        val includeStatements = file.findChildrenByClass(PicatIncludeStatement::class.java)

        // Verify that there is exactly one include statement
        assertEquals("There should be exactly one include statement", 1, includeStatements.size)

        // Verify that the include statement has the correct path
        val includeStatement = includeStatements[0]
        assertEquals("Include statement should have path utils.pi", "utils.pi", includeStatement.getIncludePath())
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

        myFixture.configureByText("test.pi", code)
        val file = myFixture.file as PicatFile

        // Find all import statements in the file
        val importStatements = file.findChildrenByClass(PicatImportStatement::class.java)
        assertEquals("There should be exactly one import statement", 1, importStatements.size)

        // Find all export statements in the file
        val exportStatements = file.findChildrenByClass(PicatExportStatement::class.java)
        assertEquals("There should be exactly one export statement", 1, exportStatements.size)

        // Find all include statements in the file
        val includeStatements = file.findChildrenByClass(PicatIncludeStatement::class.java)
        assertEquals("There should be exactly one include statement", 1, includeStatements.size)

        // Find all facts in the file
        val facts = file.findChildrenByClass(PicatFact::class.java)
        assertEquals("There should be exactly 3 facts", 3, facts.size)

        // Find all rules in the file
        val rules = file.findChildrenByClass(PicatRule::class.java)
        assertEquals("There should be exactly 3 rules", 3, rules.size)
    }
}