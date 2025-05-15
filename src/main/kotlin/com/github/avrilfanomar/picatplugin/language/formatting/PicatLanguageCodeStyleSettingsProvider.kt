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
        if (settingsType == SettingsType.SPACING_SETTINGS) {
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
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
        } else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showStandardOptions("KEEP_LINE_BREAKS")
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
        """.trimIndent()
    }
}
