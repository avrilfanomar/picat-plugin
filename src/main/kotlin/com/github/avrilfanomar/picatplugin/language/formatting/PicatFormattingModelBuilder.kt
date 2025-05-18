package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes

/**
 * Formatting model builder for Picat language.
 * This class is responsible for creating a formatting model that defines
 * how code should be formatted in the Picat language.
 */
class PicatFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.psiElement.containingFile
        val settings = formattingContext.codeStyleSettings

        // Create spacing settings
        val spacingBuilder = createSpacingBuilder(settings)

        val rootBlock = PicatBlock(
            formattingContext.node, 
            null, 
            null, 
            settings,
            spacingBuilder
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            file,
            rootBlock,
            settings
        )
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Create a spacing builder with explicit spacing rules
        val builder = SpacingBuilder(settings, PicatLanguage)
            // Add spaces around operators - force to true instead of using settings
            // Use spaceIf(true) to ensure consistent spacing
            .around(Tokens.ASSIGNMENT_OPERATORS).spaceIf(true)
            .around(Tokens.ADDITIVE_OPERATORS).spaceIf(true)
            .around(Tokens.MULTIPLICATIVE_OPERATORS).spaceIf(true)
            .around(Tokens.RELATIONAL_OPERATORS).spaceIf(true)
            .around(Tokens.EQUALITY_OPERATORS).spaceIf(true)
            .around(Tokens.LOGICAL_OPERATORS).spaceIf(true)

            // Add spaces around Picat-specific operators
            .around(Tokens.RULE_OPERATORS).spaceIf(true)
            .around(Tokens.CONSTRAINT_OPERATORS).spaceIf(true)
            .around(Tokens.TERM_COMPARISON_OPERATORS).spaceIf(true)

            // No space before comma, space after comma
            .before(Tokens.COMMA).spaceIf(false)
            .after(Tokens.COMMA).spaceIf(true)

            // Add space within parentheses
            .withinPair(Tokens.LPAR, Tokens.RPAR).spaceIf(picatSettings.SPACE_WITHIN_PARENTHESES)
            .withinPair(Tokens.LBRACKET, Tokens.RBRACKET).spaceIf(picatSettings.SPACE_WITHIN_BRACKETS)
            .withinPair(Tokens.LBRACE, Tokens.RBRACE).spaceIf(picatSettings.SPACE_WITHIN_BRACES)

        return builder
    }

    // Token sets for spacing rules
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

        val COMMA = PicatTokenTypes.COMMA
        val LPAR = PicatTokenTypes.LPAR
        val RPAR = PicatTokenTypes.RPAR
        val LBRACKET = PicatTokenTypes.LBRACKET
        val RBRACKET = PicatTokenTypes.RBRACKET
        val LBRACE = PicatTokenTypes.LBRACE
        val RBRACE = PicatTokenTypes.RBRACE
    }
}
