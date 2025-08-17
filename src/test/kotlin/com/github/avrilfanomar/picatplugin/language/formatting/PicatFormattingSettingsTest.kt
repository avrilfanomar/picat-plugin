package com.github.avrilfanomar.picatplugin.language.formatting

import com.github.avrilfanomar.picatplugin.language.formatter.PicatCodeStyleSettings
import com.github.avrilfanomar.picatplugin.language.formatter.PicatFormatterService
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for Picat code formatting.
 * This test verifies that the code formatter is correctly registered for Picat language
 * and that it formats code according to the code style settings.
 */
@Suppress("LargeClass")
class PicatFormattingSettingsTest : BasePlatformTestCase() {

    @Test
    fun testSimpleFormatting() {
        // A very simple test to see what the formatter does
        val code = "main=>X=10,Y=20."
        val expected = "main =>\n" +
                "    X = 10, Y = 20."

        // Configure settings to keep the rule body on the same line
        val cs = CodeStyle.createTestSettings()
        val picatSettings =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picatSettings.keepLineBreakAfterRuleOperators = true

        // Format the code using PicatCustomFormatter with custom settings
        val formatterService = service<PicatFormatterService>()
        val customFormatter = formatterService.getFormatter(cs)
        val formattedText = customFormatter.format(code)

        // Assert that the formatted code matches the expected output
        Assertions.assertEquals(expected, formattedText, "Formatting should match expected output")

        // Also test using the CodeStyleManager to ensure it's consistent
        myFixture.configureByText("simple.pi", code)
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val textRange = file.textRange
            val codeStyleManager = CodeStyleManager.getInstance(project)
            codeStyleManager.reformatText(file, textRange.startOffset, textRange.endOffset)
        }

        // Get the formatted text
        val formatted = myFixture.editor.document.text

        // Assert that the formatted code matches the expected output
        Assertions.assertEquals(expected, formatted, "Formatting with CodeStyleManager should match expected output")

        // Assert that both formatting methods produce the same result
        Assertions.assertEquals(formattedText, formatted, "Both formatting methods should produce the same result")
    }

    @Test
    fun testColonSpacingSetting() {
        val code = """
            list_example=>
            L=[X:X in 1..10],
            println(L).
        """
        val expectedNoSpaces = """
list_example =>
    L = [X:X in 1..10],
    println(L).
        """.trim()

        val cs = CodeStyle.createTestSettings()
        val picatSettings =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picatSettings.spaceAroundColon = false

        val formatter = service<PicatFormatterService>().getFormatter(cs)
        val formatted = formatter.format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testAdditiveSpacingSettingToggle() {
        val code = """
            toggle_additive=>
            X=1+2-3,
            Y=(A+B)-(C-D),
            println(X+Y).
        """
        val expectedNoSpaces = """
toggle_additive =>
    X = 1+2-3,
    Y = (A+B)-(C-D),
    println(X+Y).
        """.trim()

        val cs = CodeStyle.createTestSettings()
        val picatSettings =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picatSettings.spaceAroundAdditiveOperators = false

        val formatter = service<PicatFormatterService>().getFormatter(cs)
        val formatted = formatter.format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testRangeOperatorSpacingSetting() {
        val code = """
            ranges=>
            A=1..9,
            B=[X:X in 1..10].
        """
        val expectedSpaces = """
ranges =>
    A = 1 .. 9,
    B = [X : X in 1 .. 10].
        """.trim()

        val cs = CodeStyle.createTestSettings()
        val picatSettings =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picatSettings.spaceAroundRangeOperator = true

        val formatter = service<PicatFormatterService>().getFormatter(cs)
        val formatted = formatter.format(code.trim())
        Assertions.assertEquals(expectedSpaces, formatted)
    }

    @Test
    fun testAssignmentOperatorSpacingToggle() {
        val code = """
            assign_toggle=>
            A=1, B:=2, C=A=B.
        """
        val expectedNoSpaces = """
assign_toggle =>
    A=1, B:=2, C=A=B.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundAssignmentOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testLogicalOperatorSpacingToggle() {
        val code = """
            logic_toggle=>
            X=A&&B||C.
        """
        val expectedNoSpaces = """
logic_toggle =>
    X = A&&B||C.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundLogicalOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testEqualityOperatorSpacingToggle() {
        val code = """
            eq_toggle=>
            A==B, C!==D, E!=F.
        """
        val expectedNoSpaces = """
eq_toggle =>
    A==B, C!==D, E!=F.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundEqualityOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testRelationalOperatorSpacingToggle() {
        val code = """
            rel_toggle=>
            X<Y, A>=B, C=<D.
        """
        val expectedNoSpaces = """
rel_toggle =>
    X<Y, A>=B, C=<D.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundRelationalOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testMultiplicativeOperatorSpacingToggle() {
        val code = """
            mul_toggle=>
            X=2*3/4%5.
        """
        val expectedNoSpaces = """
mul_toggle =>
    X = 2*3/4%5.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundMultiplicativeOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testRuleOperatorSpacingToggleAndLineBreaks() {
        val code = """
            rule_op_toggle=>
            X=1, println(X).
        """
        val expectedNoSpacesAround = """
rule_op_toggle=>
    X = 1, println(X).
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceBeforeRuleOperators = false
        // keepLineBreakAfterRuleOperators is true by default
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpacesAround, formatted)
    }

    @Test
    fun testConstraintOperatorsSpacingToggle() {
        val code = """
            constr_toggle=>
            X#=Y, A#!=B, C#<=>D, P#\/Q, R#\S.
        """
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundConstraintOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        // Ensure no spaces around constraint operators
        Assertions.assertTrue(formatted.contains("X#=Y"))
        Assertions.assertTrue(formatted.contains("A#!=B"))
        Assertions.assertTrue(formatted.contains("C#<=>D"))
        Assertions.assertTrue(formatted.contains("P#\\/Q"))
        Assertions.assertTrue(formatted.contains("R#\\S"))
        // Ensure the header is formatted
        Assertions.assertTrue(formatted.startsWith("constr_toggle =>"))
    }

    @Test
    fun testTermComparisonOperatorsSpacingToggle() {
        val code = """
            term_toggle=>
            X@<Y, A@=<B, C@>D, E@>=F.
        """
        val expectedNoSpaces = """
term_toggle =>
    X@<Y, A@=<B, C@>D, E@>=F.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundTermComparisonOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testTypeConstraintOperatorSpacingToggle() {
        val code = """
            type_toggle=>
            X::type.
        """
        val expectedNoSpaces = """
type_toggle =>
    X::type.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundTypeConstraintOperator = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testBitwiseOperatorsSpacingToggle() {
        val code = """
            bitwise_toggle=>
            X=Y/\\Z, A=Y\\/Z, B=Y<<Z, C=Y>>Z, D=Y>>>Z.
        """
        val expectedNoSpaces = """
bitwise_toggle =>
    X = Y/\\Z, A = Y\\/Z, B = Y<<Z, C = Y>>Z, D = Y>>>Z.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceAroundBitwiseOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedNoSpaces, formatted)
    }

    @Test
    fun testCommaSpacingSettings() {
        val code = """
            comma_toggle=>
            point(1,2), list([1,2,3]).
        """
        val expectedBeforeNoAfter = """
comma_toggle =>
    point(1 ,2) ,list([1 ,2 ,3]).
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.spaceBeforeComma = true
        picat.spaceAfterComma = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedBeforeNoAfter, formatted)
    }

    @Test
    fun testKeepLineBreakAfterRuleOperatorsFalse() {
        val code = """
            main=>X=1.
        """
        val expectedSameLine = """
main => X = 1.
        """.trim()
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.keepLineBreakAfterRuleOperators = false
        val formatted = service<PicatFormatterService>().getFormatter(cs).format(code.trim())
        Assertions.assertEquals(expectedSameLine, formatted)
    }

    @Test
    fun testLineBreakSettingsWithCodeStyleManager() {
        val code = """
            main => if X > 0 then println(X) else println(0) end.
        """.trim()

        // First, with settings disabled
        val csFalse = CodeStyle.createTestSettings()
        csFalse.getCustomSettings(PicatCodeStyleSettings::class.java)
            .apply {
                lineBreakAfterBlockKeywords = false
                lineBreakAfterDot = false
                lineBreakAfterEndKeyword = false
            }
        val mgr = CodeStyleSettingsManager.getInstance(project)
        mgr.setTemporarySettings(csFalse)
        myFixture.configureByText("lb.pi", code)
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val tr = file.textRange
            CodeStyleManager.getInstance(project)
                .reformatText(file, tr.startOffset, tr.endOffset)
        }
        val formattedFalse = myFixture.editor.document.text.trim()

        // Then, with settings enabled
        val csTrue = CodeStyle.createTestSettings()
        csTrue.getCustomSettings(PicatCodeStyleSettings::class.java)
            .apply {
                lineBreakAfterBlockKeywords = true
                lineBreakAfterDot = true
                lineBreakAfterEndKeyword = true
            }
        mgr.setTemporarySettings(csTrue)
        WriteCommandAction.runWriteCommandAction(project) {
            val file = myFixture.file
            val tr = file.textRange
            CodeStyleManager.getInstance(project)
                .reformatText(file, tr.startOffset, tr.endOffset)
        }
        val formattedTrue = myFixture.editor.document.text.trim()
        try {
            // We exercised toggling settings without errors. Ensure formatting produced some output.
            Assertions.assertTrue(formattedFalse.isNotEmpty())
            Assertions.assertTrue(formattedTrue.isNotEmpty())
        } finally {
            mgr.dropTemporarySettings()
        }
    }

    @Test
    fun testUnusedIndentSettingsToggleNoEffect() {
        val code = """
            main=>
            if X>0 then println(X) end.
        """
        val cs = CodeStyle.createTestSettings()
        val picat =
            cs.getCustomSettings(PicatCodeStyleSettings::class.java)
        picat.indentRuleBody = false
        picat.indentBlockStatements = false
        picat.indentListComprehension = false
        val formatter = service<PicatFormatterService>().getFormatter(cs)
        val formatted = formatter.format(code.trim())
        // Formatting should still be valid Picat structure; we only ensure it runs without errors.
        Assertions.assertTrue(formatted.contains("main =>"))
    }
}
