package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet

/**
 * Defines spacing rules for Picat language elements.
 * This class uses IntelliJ's SpacingBuilder to create a set of rules that determine
 * the spacing between different elements in the code.
 */
class PicatSpacingBuilder(settings: CodeStyleSettings) {
    private val spacingBuilder: SpacingBuilder

    init {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val commonSettings = settings.getCommonSettings(PicatLanguage)

        val ruleOperators = TokenSet.create(
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )
        spacingBuilder = SpacingBuilder(settings, PicatLanguage)
            // Assignment operators (=, :=)
            .around(
                TokenSet.create(
                    PicatTokenTypes.ASSIGN_OP,
                    PicatTokenTypes.ASSIGN_ONCE
                )
            )
            .spaceIf(picatSettings.spaceAroundAssignmentOperators)

            // Logical operators (&&, ||, !)
            .around(
                TokenSet.create(
                    PicatTokenTypes.AND,
                    PicatTokenTypes.OR,
                    PicatTokenTypes.NOT
                )
            )
            .spaceIf(picatSettings.spaceAroundLogicalOperators)

            // Equality operators (==, !=, ===, !==)
            .around(
                TokenSet.create(
                    PicatTokenTypes.EQUAL,
                    PicatTokenTypes.NOT_EQUAL,
                    PicatTokenTypes.IDENTICAL,
                    PicatTokenTypes.NOT_IDENTICAL
                )
            )
            .spaceIf(picatSettings.spaceAroundEqualityOperators)

            // Relational operators (<, >, <=, >=)
            .around(
                TokenSet.create(
                    PicatTokenTypes.LESS,
                    PicatTokenTypes.GREATER,
                    PicatTokenTypes.LESS_EQUAL,
                    PicatTokenTypes.LESS_EQUAL_ALT,
                    PicatTokenTypes.GREATER_EQUAL
                )
            )
            .spaceIf(picatSettings.spaceAroundRelationalOperators)

            // Additive operators (+, -)
            .around(
                TokenSet.create(
                    PicatTokenTypes.PLUS,
                    PicatTokenTypes.MINUS
                )
            )
            .spaceIf(picatSettings.spaceAroundAdditiveOperators)

            // Multiplicative operators (*, /, //, mod)
            .around(
                TokenSet.create(
                    PicatTokenTypes.MULTIPLY,
                    PicatTokenTypes.DIVIDE,
                    PicatTokenTypes.INT_DIVIDE,
                    PicatTokenTypes.MODULO
                )
            )
            .spaceIf(picatSettings.spaceAroundMultiplicativeOperators)

            // Rule operators (=>, ?=>)
            .before(ruleOperators)
            .spaceIf(picatSettings.spaceBeforeRuleOperators)
            .after(ruleOperators)
            .spacing(0, 0, 1, picatSettings.keepLineBreakAfterRuleOperators, 1)

            // Add indentation for the rule body (between rule operator and end dot)
            .between(ruleOperators, PicatTokenTypes.DOT)
            .spacing(1, Integer.MAX_VALUE, 1, true, 1)

            // Constraint operators (#=, #!=, etc.)
            .around(
                TokenSet.create(
                    PicatTokenTypes.CONSTRAINT_EQ,
                    PicatTokenTypes.CONSTRAINT_NEQ,
                    PicatTokenTypes.CONSTRAINT_LT,
                    PicatTokenTypes.CONSTRAINT_LE,
                    PicatTokenTypes.CONSTRAINT_LE_ALT,
                    PicatTokenTypes.CONSTRAINT_GT,
                    PicatTokenTypes.CONSTRAINT_GE,
                    PicatTokenTypes.CONSTRAINT_NOT,
                    PicatTokenTypes.CONSTRAINT_AND,
                    PicatTokenTypes.CONSTRAINT_OR,
                    PicatTokenTypes.CONSTRAINT_XOR
                )
            )
            .spaceIf(picatSettings.spaceAroundConstraintOperators)

            // Constraint rule operators (#=>, #<=>)
            .around(
                TokenSet.create(
                    PicatTokenTypes.CONSTRAINT_IMPL,
                    PicatTokenTypes.CONSTRAINT_EQUIV
                )
            )
            .spaceIf(picatSettings.spaceAroundConstraintOperators)
            .after(
                TokenSet.create(
                    PicatTokenTypes.CONSTRAINT_IMPL,
                    PicatTokenTypes.CONSTRAINT_EQUIV
                )
            )
            .spacing(1, 1, 0, true, 1)

            // Term comparison operators (@<, @=<, etc.)
            .around(
                TokenSet.create(
                    PicatTokenTypes.TERM_LT,
                    PicatTokenTypes.TERM_LE,
                    PicatTokenTypes.TERM_LE_ALT,
                    PicatTokenTypes.TERM_GT,
                    PicatTokenTypes.TERM_GE
                )
            )
            .spaceIf(picatSettings.spaceAroundTermComparisonOperators)

            // Range operator (..)
            .around(PicatTokenTypes.RANGE)
            .spaceIf(picatSettings.spaceAroundRangeOperator)

            // Type constraint operator (::)
            .around(PicatTokenTypes.TYPE_CONSTRAINT)
            .spaceIf(picatSettings.spaceAroundTypeConstraintOperator)

            // Bitwise operators (/\, \/, <<, >>)
            .around(
                TokenSet.create(
                    PicatTokenTypes.BITWISE_AND,
                    PicatTokenTypes.BITWISE_OR,
                    PicatTokenTypes.SHIFT_LEFT,
                    PicatTokenTypes.SHIFT_RIGHT
                )
            )
            .spaceIf(picatSettings.spaceAroundBitwiseOperators)

            // Punctuation
            .before(PicatTokenTypes.COMMA)
            .spaceIf(picatSettings.spaceBeforeComma)
            .after(PicatTokenTypes.COMMA)
            .spacing(
                if (picatSettings.spaceAfterComma) 0 else 0,
                if (picatSettings.spaceAfterComma) 1 else 0,
                0,  // minLineFeeds
                true,  // keepLineBreaks
                0  // keepBlankLines
            )

            // No space after comma when followed by closing brackets/parentheses/braces
            .between(
                PicatTokenTypes.COMMA,
                TokenSet.create(
                    PicatTokenTypes.RBRACKET,
                    PicatTokenTypes.RPAR,
                    PicatTokenTypes.RBRACE
                )
            )
            .spaces(0)

            .around(PicatTokenTypes.COLON)
            .spaceIf(picatSettings.spaceAroundColon)

            // Parentheses, brackets, braces
            .withinPair(PicatTokenTypes.LPAR, PicatTokenTypes.RPAR)
            .spaceIf(commonSettings.SPACE_WITHIN_PARENTHESES)
            .withinPair(PicatTokenTypes.LBRACKET, PicatTokenTypes.RBRACKET)
            .spaceIf(commonSettings.SPACE_WITHIN_BRACKETS)
            .withinPair(PicatTokenTypes.LBRACE, PicatTokenTypes.RBRACE)
            .spaceIf(commonSettings.SPACE_WITHIN_BRACES)

            // After keywords
            .after(
                TokenSet.create(
                    PicatTokenTypes.IF_KEYWORD,
                    PicatTokenTypes.THEN_KEYWORD,
                    PicatTokenTypes.ELSE_KEYWORD,
                    PicatTokenTypes.FOREACH_KEYWORD,
                    PicatTokenTypes.WHILE_KEYWORD,
                    PicatTokenTypes.FOR_KEYWORD,
                    PicatTokenTypes.RETURN_KEYWORD
                )
            )
            .spaces(1)

            // Ensure proper indentation for statements after block keywords
            .between(
                TokenSet.create(
                    PicatTokenTypes.THEN_KEYWORD,
                    PicatTokenTypes.ELSE_KEYWORD
                ),
                TokenSet.create(
                    PicatTokenTypes.END_KEYWORD,
                    PicatTokenTypes.ELSE_KEYWORD,
                    PicatTokenTypes.ELSEIF_KEYWORD
                )
            )
            .spacing(1, Integer.MAX_VALUE, 1, true, 1)

            // Ensure proper indentation for statements after loop keywords
            .between(
                TokenSet.create(
                    PicatTokenTypes.FOREACH_KEYWORD,
                    PicatTokenTypes.WHILE_KEYWORD,
                    PicatTokenTypes.FOR_KEYWORD
                ),
                PicatTokenTypes.END_KEYWORD
            )
            .spacing(1, Integer.MAX_VALUE, 1, true, 1)
    }

    /**
     * Returns the spacing builder.
     */
    fun getSpacingBuilder(): SpacingBuilder = spacingBuilder
}
