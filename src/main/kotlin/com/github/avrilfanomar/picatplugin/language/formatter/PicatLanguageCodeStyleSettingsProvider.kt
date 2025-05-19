package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.OptionAnchor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.SpacingOption
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/**
 * Provides code style settings for Picat language to the IDE.
 * This class defines which settings are available for Picat and provides a code sample for the settings preview.
 */
class PicatLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language = PicatLanguage

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return PicatCodeStyleSettings(settings)
    }

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, "Picat") {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return PicatCodeStyleMainPanel(currentSettings, settings)
            }
        }
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
                    "SPACE_BEFORE_COMMA",
                    "SPACE_AFTER_COMMA"
                )

                // Picat-specific operators
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_RULE_OPERATORS",
                    "Around rule operators (=>, ?=>)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_CONSTRAINT_OPERATORS",
                    "Around constraint operators (#=, #!=, etc.)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_TERM_COMPARISON_OPERATORS",
                    "Around term comparison operators (@<, @=<, etc.)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_RANGE_OPERATOR",
                    "Around range operator (..)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_TYPE_CONSTRAINT_OPERATOR",
                    "Around type constraint operator (::)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_BITWISE_OPERATORS",
                    "Around bitwise operators (/\\, \\/, <<, >>)",
                    "Operators"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "SPACE_AROUND_COLON",
                    "Around colon (:)",
                    "Punctuation"
                )
            }
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_RULE_OPERATORS",
                    "After rule operators (=> and ?=>)",
                    "Line Breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_BLOCK_KEYWORDS",
                    "After block keywords (then, else)",
                    "Line Breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_DOT",
                    "After dot (.)",
                    "Line Breaks"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "LINE_BREAK_AFTER_END_KEYWORD",
                    "After end keyword",
                    "Line Breaks"
                )
            }
            SettingsType.INDENT_SETTINGS -> {
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "INDENT_RULE_BODY",
                    "Indent rule body",
                    "Indentation"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "INDENT_BLOCK_STATEMENTS",
                    "Indent block statements",
                    "Indentation"
                )
                consumer.showCustomOption(
                    PicatCodeStyleSettings::class.java,
                    "INDENT_LIST_COMPREHENSION",
                    "Indent list comprehension",
                    "Indentation"
                )
            }
            else -> {}
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            % Fibonacci function with if-then-else
            fibi(N) = F => 
                if N==0 then
                    F=1
                elseif N==1 then
                    F=1
                else
                    F= fibi(N-1)+fibi(N-2)
                end.

            % A nondeterministic predicate with a backtrackable rule
            my_member(Y,[X|_]) ?=> Y=X.
            my_member(Y,[_|L]) => my_member(Y,L).

            % List comprehension
            qsort([])=[].
            qsort([H|T])=qsort([E : E in T, E=<H])++[H]++qsort([E : E in T, E>H]).

            % Constraint programming
            queens(N) =>
                Qs=new_array(N),
                Qs :: 1..N,
                foreach (I in 1..N-1, J in I+1..N)
                    Qs[I] #!= Qs[J],
                    abs(Qs[I]-Qs[J]) #!= J-I
                end,
                solve([ff],Qs),
                writeln(Qs).
        """.trimIndent()
    }

    private class PicatCodeStyleMainPanel(
        currentSettings: CodeStyleSettings,
        settings: CodeStyleSettings
    ) : TabbedLanguageCodeStylePanel(PicatLanguage, currentSettings, settings) {
        override fun initTabs(settings: CodeStyleSettings) {
            addIndentOptionsTab(settings)
            addSpacesTab(settings)
            addWrappingAndBracesTab(settings)
        }
    }
}
