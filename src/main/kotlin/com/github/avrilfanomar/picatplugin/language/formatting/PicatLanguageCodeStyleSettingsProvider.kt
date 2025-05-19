package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.github.avrilfanomar.picatplugin.language.PicatLanguage

/**
 * Provider for language-specific code style settings for Picat.
 * This class provides language-specific code style settings for Picat in the IDE settings.
 */
class PicatLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language {
        return PicatLanguage
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                    "SPACE_AROUND_LOGICAL_OPERATORS",
                    "SPACE_AROUND_EQUALITY_OPERATORS",
                    "SPACE_AROUND_RELATIONAL_OPERATORS",
                    "SPACE_AROUND_ADDITIVE_OPERATORS",
                    "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
                    "SPACE_WITHIN_PARENTHESES",
                    "SPACE_WITHIN_BRACKETS",
                    "SPACE_WITHIN_BRACES"
                )

                // Add custom options for Picat-specific operators
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_RULE_OPERATORS",
                    "Rule operators (=> and ?=>)",
                    "Picat-specific operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_CONSTRAINT_OPERATORS",
                    "Constraint operators (#=, #!=, etc.)",
                    "Picat-specific operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_TERM_COMPARISON_OPERATORS",
                    "Term comparison operators (@<, @=<, etc.)",
                    "Picat-specific operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_RANGE_OPERATOR",
                    "Range operator (..)",
                    "Picat-specific operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_TYPE_CONSTRAINT_OPERATOR",
                    "Type constraint operator (::)",
                    "Picat-specific operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_BITWISE_OPERATORS",
                    "Bitwise operators (/\\, \\/, <<, >>)",
                    "Picat-specific operators"
                )

                // Add custom options for punctuation
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_BEFORE_COMMA",
                    "Before comma",
                    "Spacing around punctuation"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AFTER_COMMA",
                    "After comma",
                    "Spacing around punctuation"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_COLON",
                    "Around colon",
                    "Spacing around punctuation"
                )

                // Add custom options for keywords
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AFTER_CONTROL_KEYWORDS",
                    "After control keywords (if, foreach, etc.)",
                    "Spacing after keywords"
                )
            }
            SettingsType.BLANK_LINES_SETTINGS -> {
                consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")

                // Add custom options for line breaks
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "KEEP_LINE_BREAKS",
                    "Keep line breaks",
                    "Line breaks"
                )
            }
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions("KEEP_LINE_BREAKS")

                // Add custom options for line breaks
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_RULE_OPERATORS",
                    "Line break after rule operators (=> and ?=>)",
                    "Line breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_BLOCK_KEYWORDS",
                    "Line break after block keywords (then, else)",
                    "Line breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_DOT",
                    "Line break after dot",
                    "Line breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_END_KEYWORD",
                    "Line break after end keyword",
                    "Line breaks"
                )

                // Add custom options for alignment
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "ALIGN_MULTILINE_PARAMETERS",
                    "Align multiline parameters",
                    "Alignment"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "ALIGN_MULTILINE_ARGUMENTS",
                    "Align multiline arguments",
                    "Alignment"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "ALIGN_CONSECUTIVE_ASSIGNMENTS",
                    "Align consecutive assignments",
                    "Alignment"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "ALIGN_LIST_ELEMENTS",
                    "Align list elements",
                    "Alignment"
                )

                // Add custom options for wrapping
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "WRAP_LONG_LINES",
                    "Wrap long lines",
                    "Wrapping"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "MAX_LINE_LENGTH",
                    "Maximum line length",
                    "Wrapping"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "PREFER_PARAMETERS_WRAP",
                    "Prefer parameters wrap",
                    "Wrapping"
                )

                // Add custom options for special formatting
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPECIAL_ELSE_IF_TREATMENT",
                    "Special else-if treatment",
                    "Special formatting"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "INDENT_CASE_FROM_SWITCH",
                    "Indent case from switch",
                    "Special formatting"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPECIAL_LIST_COMPREHENSION_FORMATTING",
                    "Special list comprehension formatting",
                    "Special formatting"
                )
            }
            SettingsType.INDENT_SETTINGS -> {
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "INDENT_SIZE",
                    "Indent size",
                    "Indentation"
                )
            }
            else -> {
                // No customization for other settings types
            }
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            % This is a sample Picat program for code style preview

            import util.
            import cp.

            main => 
                X = 10,
                Y = 20,
                Z = X + Y,
                if Z > 25 then
                    println("Z is greater than 25")
                else
                    println("Z is not greater than 25")
                end,

                % Array example
                Arr = [1, 2, 3, 4, 5],
                foreach(I in 1..5)
                    println(Arr[I])
                end,

                % Function call
                Result = factorial(5),
                println("Factorial of 5 is " ++ Result),

                % Constraint example
                solve_sudoku.

            % Regular rule with => operator
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) => N > 0.

            % Backtrackable rule with ?=> operator
            solve_sudoku ?=>
                Sudoku = new_array(9, 9),
                Sudoku :: 1..9,

                % Constraint operators example
                foreach(I in 1..9)
                    all_different([Sudoku[I,J] : J in 1..9]),
                    all_different([Sudoku[J,I] : J in 1..9])
                end,

                % Term comparison operators example
                foreach(I in 1..9, J in 1..9)
                    if Sudoku[I,J] @> 5 then
                        println("Value at " ++ I ++ "," ++ J ++ " is greater than 5")
                    end
                end,

                % Bitwise operators example
                Bits1 = 0b1010,
                Bits2 = 0b1100,
                BitwiseAnd = Bits1 /\ Bits2,
                BitwiseOr = Bits1 \/ Bits2,
                ShiftLeft = Bits1 << 2,
                ShiftRight = Bits1 >> 1,

                % List comprehension example
                List = [X : X in 1..10, X mod 2 == 0],

                % Multi-line parameters example
                complex_function(
                    Parameter1,
                    Parameter2,
                    Parameter3
                ),

                solve(Sudoku),
                print_sudoku(Sudoku).

            solve_sudoku => println("No solution found").

            print_sudoku(Sudoku) =>
                foreach(I in 1..9)
                    foreach(J in 1..9)
                        printf(" %d", Sudoku[I,J])
                    end,
                    nl
                end.

            % Example of consecutive assignments
            initialize =>
                X1 = 1,
                LongVariableName = 2,
                Y = 300,
                VeryLongVariableName = 4000.
        """.trimIndent()
    }
}
