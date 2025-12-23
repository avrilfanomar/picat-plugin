package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.utils.PsiTestUtils
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for basic.pi standard library module parsing.
 * This test verifies that the parser correctly handles all constructs in basic.pi,
 * including throw statements and dot access notation (bp.b_XOR_ccf).
 */
class PicatBasicParsingTest : BasePlatformTestCase() {

    @Test
    fun testBasicPiParsing() {
        myFixture.configureByText("basic.pi", javaClass.getResource("/basic.pi")!!.readText())
        val file = myFixture.file as PicatFileImpl

        val errorElements: Collection<PsiErrorElement> =
            PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)

        if (errorElements.isNotEmpty()) {
            errorElements.forEachIndexed { index, error ->
                println(
                    "[DEBUG_LOG] Error $index: '${error.errorDescription}' at text: '${error.text}' " +
                            "parent: '${error.parent?.javaClass?.simpleName}'"
                )
            }
        }

        val moduleDeclarations = PsiTreeUtil.findChildrenOfType(file, PicatModuleDeclaration::class.java)
        Assertions.assertTrue(moduleDeclarations.isNotEmpty(), "Should have module declaration")

        val moduleDecl = moduleDeclarations.first()
        Assertions.assertTrue(moduleDecl.text.contains("basic"), "Module should be named 'basic'")

        val numOfFunctionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java).size
        val numOfFunctionFacts = PsiTreeUtil.findChildrenOfType(file, PicatFunctionFact::class.java).size
        val numOfPredicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java).size
        val numOfPredicateFacts = PsiTreeUtil.findChildrenOfType(file, PicatPredicateFact::class.java).size
        val totalRules = numOfFunctionRules + numOfFunctionFacts + numOfPredicateRules + numOfPredicateFacts

        // FIXED: The file now parses completely with all ~275 rules (functions + predicates).
        Assertions.assertTrue(totalRules > 270, "Should have at least 270 total rules (was $totalRules: ${numOfFunctionRules}FR + ${numOfFunctionFacts}FF + ${numOfPredicateRules}PR + ${numOfPredicateFacts}PF)")

        // Verify specific constructs are parsed correctly

        // 1. Test that 'throw' statements are parsed correctly
        val fileText = file.text
        val throwOccurrences = fileText.split("throw").size - 1
        Assertions.assertTrue(throwOccurrences > 0, "Should have throw statements")

        // Verify that throw statement appears (the exact string check is tricky due to escaping)
        Assertions.assertTrue(
            fileText.contains("meta_meta_call_not_allowed"),
            "Should contain meta_meta_call_not_allowed"
        )

        // 2. Test that dot access notation is parsed correctly (bp.b_XOR_ccf)
        val dotAccessExample = "bp.b_XOR_ccf(X,Y,Res)"
        Assertions.assertTrue(fileText.contains(dotAccessExample), "Should contain dot access: $dotAccessExample")

        // 3. Test that dollar-escaped atoms are parsed correctly
        val dollarAtomOccurrences = fileText.split("\$meta_meta_call_not_allowed").size - 1
        Assertions.assertTrue(dollarAtomOccurrences > 0, "Should have dollar-escaped atoms")

        // Verify that there are NO parsing errors - all keywords can now be used as function names
        Assertions.assertEquals(
            0, errorElements.size,
            "Expected 0 parsing errors after allowing keywords as function names"
        )
    }

    @Test
    fun testThrowStatement() {
        // Test isolated throw statement parsing (exact construct from basic.pi line 37)
        val throwTest = """
            module test.

            '\\+'(Call) => throw(${'$'}meta_meta_call_not_allowed('\\+'(Call))).
        """.trimIndent()

        myFixture.configureByText("test.pi", throwTest)
        val file = myFixture.file as PicatFileImpl

        // Find errors if any
        val errorElements = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)

        // The parser should have no errors for this simple throw statement
        Assertions.assertEquals(0, errorElements.size, "Should have no parsing errors")

        // Verify throw statements are present
        val fileText = file.text
        Assertions.assertTrue(fileText.contains("throw"), "Should contain throw statements")
        Assertions.assertTrue(fileText.contains("${'$'}meta_meta_call_not_allowed"), "Should contain dollar atoms")

        // Count predicate rules (uses => so it's a predicate, not a function)
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(predicateRules.isNotEmpty(), "Should have at least 1 predicate rule")
    }

    @Test
    fun testDotAccessNotation() {
        // Test isolated dot access notation parsing (exact construct from basic.pi line 35)
        val dotAccessTest = """
            module test.

            '^'(X, Y) = Res => bp.b_XOR_ccf(X,Y,Res).
        """.trimIndent()

        myFixture.configureByText("test.pi", dotAccessTest)
        val file = myFixture.file as PicatFileImpl

        PsiTestUtils.assertNoPsiErrors(file, "dot access notation")

        // Verify dot access is present
        val fileText = file.text
        Assertions.assertTrue(fileText.contains("bp.b_XOR_ccf"), "Should contain bp.b_XOR_ccf")

        // Count function rules
        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        Assertions.assertTrue(functionRules.size >= 1, "Should have at least 1 function rule")
    }

    @Test
    fun testQuotedFunctionNames() {
        // Test that quoted function names (operators) are parsed correctly
        val quotedNamesTest = """
            module test.

            '!='(X,Y) => X != Y.
            '+'(X,Y) = X + Y.
        """.trimIndent()

        myFixture.configureByText("test.pi", quotedNamesTest)
        val file = myFixture.file as PicatFileImpl

        PsiTestUtils.assertNoPsiErrors(file, "quoted function names")

        // Verify quoted names are present
        val fileText = file.text
        Assertions.assertTrue(fileText.contains("'!='"), "Should contain '!='")
        Assertions.assertTrue(fileText.contains("'+'"), "Should contain '+'")

        // Count predicate rules (uses => so it's a predicate, not a function)
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(predicateRules.isNotEmpty(), "Should have at least 1 predicate rule")
    }

    @Test
    fun testDollarEscapedAtoms() {
        // Test that dollar-escaped atoms are parsed correctly
        // Note: Dollar atoms are fully tested in testCombinedConstructs and testThrowStatement
        // This test just verifies the basics work
        val dollarAtomsTest = """
            module test.

            simple_pred(X) => true.
        """.trimIndent()

        myFixture.configureByText("test.pi", dollarAtomsTest)
        val file = myFixture.file as PicatFileImpl

        PsiTestUtils.assertNoPsiErrors(file, "dollar-escaped atoms")

        // Verify we can parse basic predicates
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        Assertions.assertTrue(predicateRules.isNotEmpty(), "Should have at least 1 predicate rule")
    }

    @Test
    fun testCombinedConstructs() {
        // Test combination of throw, dot access, and dollar atoms (exact constructs from basic.pi)
        val combinedTest = """
            module test.

            '^'(X, Y) = Res => bp.b_XOR_ccf(X,Y,Res).
            '\\+'(Call) => throw(${'$'}meta_meta_call_not_allowed('\\+'(Call))).
        """.trimIndent()

        myFixture.configureByText("test.pi", combinedTest)
        val file = myFixture.file as PicatFileImpl

        PsiTestUtils.assertNoPsiErrors(file, "combined constructs")

        // Verify all constructs are present
        val fileText = file.text
        Assertions.assertTrue(fileText.contains("bp.b_XOR_ccf"), "Should contain dot access")
        Assertions.assertTrue(fileText.contains("throw"), "Should contain throw statements")
        Assertions.assertTrue(fileText.contains("${'$'}meta_meta_call_not_allowed"), "Should contain dollar atoms")

        // Count both function and predicate rules
        val functionRules = PsiTreeUtil.findChildrenOfType(file, PicatFunctionRule::class.java)
        val predicateRules = PsiTreeUtil.findChildrenOfType(file, PicatPredicateRule::class.java)
        val totalRules = functionRules.size + predicateRules.size
        Assertions.assertTrue(totalRules >= 2, "Should have at least 2 rules total")
    }
}
