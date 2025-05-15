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
                println("Factorial of 5 is " ++ Result).
                
            factorial(0) = 1.
            factorial(N) = N * factorial(N-1) :- N > 0.
        """.trimIndent()
    }
}