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

        // Log that formatting model is being created
        println("[DEBUG_LOG] Creating formatting model for file: ${file.name}")
        println("[DEBUG_LOG] PSI element: ${formattingContext.psiElement}")
        println("[DEBUG_LOG] Node: ${formattingContext.node}")

        // Create spacing settings
        val spacingBuilder = createSpacingBuilder(settings)

        val rootBlock = PicatBlock(
            formattingContext.node, 
            null, 
            null, 
            settings,
            spacingBuilder
        )

        println("[DEBUG_LOG] Created root block: $rootBlock")

        return FormattingModelProvider.createFormattingModelForPsiFile(
            file,
            rootBlock,
            settings
        )
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Log the spacing settings
        println("[DEBUG_LOG] Creating spacing builder with settings:")
        println("[DEBUG_LOG] SPACE_AROUND_ASSIGNMENT_OPERATORS: ${picatSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_ADDITIVE_OPERATORS: ${picatSettings.SPACE_AROUND_ADDITIVE_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_MULTIPLICATIVE_OPERATORS: ${picatSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_RELATIONAL_OPERATORS: ${picatSettings.SPACE_AROUND_RELATIONAL_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_EQUALITY_OPERATORS: ${picatSettings.SPACE_AROUND_EQUALITY_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_LOGICAL_OPERATORS: ${picatSettings.SPACE_AROUND_LOGICAL_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_RULE_OPERATORS: ${picatSettings.SPACE_AROUND_RULE_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_CONSTRAINT_OPERATORS: ${picatSettings.SPACE_AROUND_CONSTRAINT_OPERATORS}")
        println("[DEBUG_LOG] SPACE_AROUND_TERM_COMPARISON_OPERATORS: ${picatSettings.SPACE_AROUND_TERM_COMPARISON_OPERATORS}")

        // Create a spacing builder with explicit spacing rules
        val builder = SpacingBuilder(settings, PicatLanguage)
            // Add spaces around operators - force to true instead of using settings
            .around(Tokens.ASSIGNMENT_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.ADDITIVE_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.MULTIPLICATIVE_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.RELATIONAL_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.EQUALITY_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.LOGICAL_OPERATORS).spacing(1, 1, 0, true, 0)

            // Add spaces around Picat-specific operators
            .around(Tokens.RULE_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.CONSTRAINT_OPERATORS).spacing(1, 1, 0, true, 0)
            .around(Tokens.TERM_COMPARISON_OPERATORS).spacing(1, 1, 0, true, 0)

            // Add space after comma
            .after(Tokens.COMMA).spacing(1, 1, 0, true, 0)

            // Add space within parentheses
            .withinPair(Tokens.LPAR, Tokens.RPAR).spaceIf(picatSettings.SPACE_WITHIN_PARENTHESES)
            .withinPair(Tokens.LBRACKET, Tokens.RBRACKET).spaceIf(picatSettings.SPACE_WITHIN_BRACKETS)
            .withinPair(Tokens.LBRACE, Tokens.RBRACE).spaceIf(picatSettings.SPACE_WITHIN_BRACES)

        println("[DEBUG_LOG] Created spacing builder: $builder")
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

