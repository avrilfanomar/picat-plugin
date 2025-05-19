package com.github.avrilfanomar.picatplugin.language.formatting

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet

/**
 * Builder class for creating spacing rules for Picat language.
 * This class encapsulates all spacing-related logic, making it easier to maintain
 * and extend the formatting rules.
 */
class PicatSpacingBuilder(settings: CodeStyleSettings) {

    private val spacingBuilder: SpacingBuilder

    init {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Create a spacing builder with spacing rules based on settings
        spacingBuilder = SpacingBuilder(settings, PicatLanguage)
            // Add spaces around operators based on settings
            .around(Tokens.ASSIGNMENT_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .around(Tokens.ADDITIVE_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
            .around(Tokens.MULTIPLICATIVE_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
            .around(Tokens.RELATIONAL_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
            .around(Tokens.EQUALITY_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_EQUALITY_OPERATORS)
            .around(Tokens.LOGICAL_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_LOGICAL_OPERATORS)

            // Add spaces around Picat-specific operators based on settings
            .around(Tokens.RULE_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_RULE_OPERATORS)
            .around(Tokens.CONSTRAINT_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_CONSTRAINT_OPERATORS)
            .around(Tokens.TERM_COMPARISON_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_TERM_COMPARISON_OPERATORS)
            .around(Tokens.RANGE_OPERATOR).spaceIf(picatSettings.SPACE_AROUND_RANGE_OPERATOR)
            .around(Tokens.TYPE_CONSTRAINT_OPERATOR).spaceIf(picatSettings.SPACE_AROUND_TYPE_CONSTRAINT_OPERATOR)
            .around(Tokens.BITWISE_OPERATORS).spaceIf(picatSettings.SPACE_AROUND_BITWISE_OPERATORS)

            // Spacing around punctuation
            .before(Tokens.COMMA).spaceIf(picatSettings.SPACE_BEFORE_COMMA)
            .after(Tokens.COMMA).spaceIf(picatSettings.SPACE_AFTER_COMMA)
            .around(Tokens.COLON).spaceIf(picatSettings.SPACE_AROUND_COLON)

            // Spacing within parentheses, brackets, and braces
            .withinPair(Tokens.LPAR, Tokens.RPAR).spaceIf(picatSettings.SPACE_WITHIN_PARENTHESES)
            .withinPair(Tokens.LBRACKET, Tokens.RBRACKET).spaceIf(picatSettings.SPACE_WITHIN_BRACKETS)
            .withinPair(Tokens.LBRACE, Tokens.RBRACE).spaceIf(picatSettings.SPACE_WITHIN_BRACES)

            // Line breaks after rule operators with indentation
            .after(Tokens.RULE_OPERATORS).spacing(0, 0, if (picatSettings.LINE_BREAK_AFTER_RULE_OPERATORS) 1 else 0, picatSettings.KEEP_LINE_BREAKS, if (picatSettings.LINE_BREAK_AFTER_RULE_OPERATORS) picatSettings.INDENT_SIZE else 0)

            // Line breaks after block keywords with indentation
            .after(Tokens.THEN_KEYWORD).spacing(0, 0, if (picatSettings.LINE_BREAK_AFTER_BLOCK_KEYWORDS) 1 else 0, picatSettings.KEEP_LINE_BREAKS, if (picatSettings.LINE_BREAK_AFTER_BLOCK_KEYWORDS) picatSettings.INDENT_SIZE else 0)
            .after(Tokens.ELSE_KEYWORD).spacing(0, 0, if (picatSettings.LINE_BREAK_AFTER_BLOCK_KEYWORDS) 1 else 0, picatSettings.KEEP_LINE_BREAKS, if (picatSettings.LINE_BREAK_AFTER_BLOCK_KEYWORDS) picatSettings.INDENT_SIZE else 0)

            // Spaces after control keywords
            .after(Tokens.IF_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.FOREACH_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.FOR_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.WHILE_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.DO_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.REPEAT_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.CATCH_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)
            .after(Tokens.TRY_KEYWORD).spaceIf(picatSettings.SPACE_AFTER_CONTROL_KEYWORDS)

            // Line breaks after the end keyword
            .after(Tokens.END_KEYWORD).spacing(0, 0, if (picatSettings.LINE_BREAK_AFTER_END_KEYWORD) 1 else 0, picatSettings.KEEP_LINE_BREAKS, if (picatSettings.LINE_BREAK_AFTER_END_KEYWORD) picatSettings.INDENT_SIZE else 0)

            // Line break after dot at the end of a predicate or function
            .after(Tokens.DOT).spacing(0, 0, if (picatSettings.LINE_BREAK_AFTER_DOT) 1 else 0, picatSettings.KEEP_LINE_BREAKS, if (picatSettings.LINE_BREAK_AFTER_DOT) picatSettings.INDENT_SIZE else 0)

            // Special handling for list comprehensions
            .around(Tokens.IN_KEYWORD).spaceIf(true)
            .around(Tokens.NOTIN_KEYWORD).spaceIf(true)

            // Special handling for function calls
            .between(Tokens.IDENTIFIER, Tokens.LPAR).spaceIf(false)

    }

    /**
     * Gets the underlying SpacingBuilder.
     * @return The SpacingBuilder instance
     */
    fun getSpacingBuilder(): SpacingBuilder {
        return spacingBuilder
    }

    /**
     * Token sets for spacing rules
     */
    private object Tokens {
        val ASSIGNMENT_OPERATORS = TokenSet.create(
            PicatTokenTypes.ASSIGN_OP, PicatTokenTypes.ASSIGN_ONCE
        )

        val ADDITIVE_OPERATORS = TokenSet.create(
            PicatTokenTypes.PLUS, PicatTokenTypes.MINUS, PicatTokenTypes.CONCAT
        )

        val MULTIPLICATIVE_OPERATORS = TokenSet.create(
            PicatTokenTypes.MULTIPLY, PicatTokenTypes.DIVIDE, PicatTokenTypes.INT_DIVIDE,
            PicatTokenTypes.POWER_OP, PicatTokenTypes.MODULO
        )

        val RELATIONAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.LESS, PicatTokenTypes.LESS_EQUAL, PicatTokenTypes.LESS_EQUAL_ALT,
            PicatTokenTypes.GREATER, PicatTokenTypes.GREATER_EQUAL
        )

        val EQUALITY_OPERATORS = TokenSet.create(
            PicatTokenTypes.EQUAL, PicatTokenTypes.NOT_EQUAL, PicatTokenTypes.NOT_IDENTICAL,
            PicatTokenTypes.IDENTICAL
        )

        val LOGICAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.AND, PicatTokenTypes.OR, PicatTokenTypes.NOT
        )

        // Picat-specific operator token sets
        val RULE_OPERATORS = TokenSet.create(
            PicatTokenTypes.ARROW_OP, PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )

        val CONSTRAINT_OPERATORS = TokenSet.create(
            PicatTokenTypes.CONSTRAINT_EQ, PicatTokenTypes.CONSTRAINT_NEQ,
            PicatTokenTypes.CONSTRAINT_LT, PicatTokenTypes.CONSTRAINT_LE, PicatTokenTypes.CONSTRAINT_LE_ALT,
            PicatTokenTypes.CONSTRAINT_GT, PicatTokenTypes.CONSTRAINT_GE,
            PicatTokenTypes.CONSTRAINT_NOT, PicatTokenTypes.CONSTRAINT_AND, PicatTokenTypes.CONSTRAINT_OR,
            PicatTokenTypes.CONSTRAINT_XOR, PicatTokenTypes.CONSTRAINT_IMPL, PicatTokenTypes.CONSTRAINT_EQUIV
        )

        val TERM_COMPARISON_OPERATORS = TokenSet.create(
            PicatTokenTypes.TERM_LT, PicatTokenTypes.TERM_LE, PicatTokenTypes.TERM_LE_ALT,
            PicatTokenTypes.TERM_GT, PicatTokenTypes.TERM_GE
        )

        val RANGE_OPERATOR = PicatTokenTypes.RANGE
        val TYPE_CONSTRAINT_OPERATOR = PicatTokenTypes.TYPE_CONSTRAINT

        val BITWISE_OPERATORS = TokenSet.create(
            PicatTokenTypes.BITWISE_AND, PicatTokenTypes.BITWISE_OR,
            PicatTokenTypes.SHIFT_LEFT, PicatTokenTypes.SHIFT_RIGHT
        )

        // Block keywords
        val THEN_KEYWORD = PicatTokenTypes.THEN_KEYWORD
        val ELSE_KEYWORD = PicatTokenTypes.ELSE_KEYWORD
        val IF_KEYWORD = PicatTokenTypes.IF_KEYWORD
        val FOREACH_KEYWORD = PicatTokenTypes.FOREACH_KEYWORD
        val FOR_KEYWORD = PicatTokenTypes.FOR_KEYWORD
        val WHILE_KEYWORD = PicatTokenTypes.WHILE_KEYWORD
        val DO_KEYWORD = PicatTokenTypes.DO_KEYWORD
        val REPEAT_KEYWORD = PicatTokenTypes.REPEAT_KEYWORD
        val TRY_KEYWORD = PicatTokenTypes.TRY_KEYWORD
        val CATCH_KEYWORD = PicatTokenTypes.CATCH_KEYWORD
        val END_KEYWORD = PicatTokenTypes.END_KEYWORD

        // Special keywords
        val IN_KEYWORD = PicatTokenTypes.IN_KEYWORD
        val NOTIN_KEYWORD = PicatTokenTypes.NOTIN_KEYWORD

        // Other tokens
        val COMMA = PicatTokenTypes.COMMA
        val DOT = PicatTokenTypes.DOT
        val LPAR = PicatTokenTypes.LPAR
        val RPAR = PicatTokenTypes.RPAR
        val LBRACKET = PicatTokenTypes.LBRACKET
        val RBRACKET = PicatTokenTypes.RBRACKET
        val LBRACE = PicatTokenTypes.LBRACE
        val RBRACE = PicatTokenTypes.RBRACE
        val COLON = PicatTokenTypes.COLON
        val IDENTIFIER = PicatTokenTypes.IDENTIFIER
    }
}
