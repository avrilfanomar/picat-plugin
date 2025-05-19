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

        // Create a spacing builder with explicit spacing rules
        spacingBuilder = SpacingBuilder(settings, PicatLanguage)
            // Add spaces around operators - force to true instead of using settings
            // Use exact spacing to ensure consistent formatting
            .around(Tokens.ASSIGNMENT_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.ADDITIVE_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.MULTIPLICATIVE_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.RELATIONAL_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.EQUALITY_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.LOGICAL_OPERATORS).spacing(1, 1, 0, false, 0)

            // Add spaces around Picat-specific operators
            .around(Tokens.RULE_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.CONSTRAINT_OPERATORS).spacing(1, 1, 0, false, 0)
            .around(Tokens.TERM_COMPARISON_OPERATORS).spacing(1, 1, 0, false, 0)

            // No space before comma, space after comma
            .before(Tokens.COMMA).spacing(0, 0, 0, false, 0)
            .after(Tokens.COMMA).spacing(1, 1, 0, false, 0)

            // Add space within parentheses
            .withinPair(Tokens.LPAR, Tokens.RPAR).spaceIf(picatSettings.SPACE_WITHIN_PARENTHESES)
            .withinPair(Tokens.LBRACKET, Tokens.RBRACKET).spaceIf(picatSettings.SPACE_WITHIN_BRACKETS)
            .withinPair(Tokens.LBRACE, Tokens.RBRACE).spaceIf(picatSettings.SPACE_WITHIN_BRACES)

            // No space after left parenthesis and before right parenthesis
            .after(Tokens.LPAR).spacing(0, 0, 0, false, 0)
            .before(Tokens.RPAR).spacing(0, 0, 0, false, 0)

            // No space after left bracket and before right bracket
            .after(Tokens.LBRACKET).spacing(0, 0, 0, false, 0)
            .before(Tokens.RBRACKET).spacing(0, 0, 0, false, 0)

            // No space after left brace and before right brace
            .after(Tokens.LBRACE).spacing(0, 0, 0, false, 0)
            .before(Tokens.RBRACE).spacing(0, 0, 0, false, 0)

            // Add line breaks after rule operators with indentation
            .after(Tokens.RULE_OPERATORS).spacing(0, 0, 1, true, 1)

            // Add line breaks after block keywords with indentation
            .after(Tokens.THEN_KEYWORD).spacing(0, 0, 1, true, 1)
            .after(Tokens.ELSE_KEYWORD).spacing(0, 0, 1, true, 1)

            // Add spaces after block structure keywords
            .after(Tokens.IF_KEYWORD).spacing(1, 1, 0, false, 0)
            .after(Tokens.FOREACH_KEYWORD).spacing(1, 1, 0, false, 0)
            .after(Tokens.FOR_KEYWORD).spacing(1, 1, 0, false, 0)
            .after(Tokens.WHILE_KEYWORD).spacing(1, 1, 0, false, 0)

            // Add line breaks after end keyword
            .after(Tokens.END_KEYWORD).spacing(0, 0, 1, true, 0)

            // Add spaces around colon in list comprehensions
            .around(Tokens.COLON).spacing(1, 1, 0, false, 0)

            // Add line break after dot at the end of a predicate or function
            .after(Tokens.DOT).spacing(0, 0, 1, true, 0)

            // Add space after comment
            .after(PicatTokenTypes.COMMENT).spacing(0, 0, 1, true, 0)
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
            PicatTokenTypes.CONSTRAINT_XOR, PicatTokenTypes.CONSTRAINT_IMPL, PicatTokenTypes.CONSTRAINT_EQUIV,
            PicatTokenTypes.TYPE_CONSTRAINT
        )

        val TERM_COMPARISON_OPERATORS = TokenSet.create(
            PicatTokenTypes.TERM_LT, PicatTokenTypes.TERM_LE, PicatTokenTypes.TERM_LE_ALT,
            PicatTokenTypes.TERM_GT, PicatTokenTypes.TERM_GE
        )

        // Block keywords
        val THEN_KEYWORD = PicatTokenTypes.THEN_KEYWORD
        val ELSE_KEYWORD = PicatTokenTypes.ELSE_KEYWORD
        val IF_KEYWORD = PicatTokenTypes.IF_KEYWORD
        val FOREACH_KEYWORD = PicatTokenTypes.FOREACH_KEYWORD
        val FOR_KEYWORD = PicatTokenTypes.FOR_KEYWORD
        val WHILE_KEYWORD = PicatTokenTypes.WHILE_KEYWORD
        val END_KEYWORD = PicatTokenTypes.END_KEYWORD

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
    }
}
