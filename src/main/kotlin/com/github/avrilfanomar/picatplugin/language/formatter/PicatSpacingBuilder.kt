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

    companion object {
        // Rule operators (=>, ?=>, :-, <=>, ?<=>)
        val RULE_OPERATORS = TokenSet.create(
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
            PicatTokenTypes.RULE_OP,
            PicatTokenTypes.BICONDITIONAL_OP,
            PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP
        )

        // Constraint rule operators (#=>, #<=>)
        val CONSTRAINT_RULE_OPERATORS = TokenSet.create(
            PicatTokenTypes.CONSTRAINT_IMPL,
            PicatTokenTypes.CONSTRAINT_EQUIV
        )

        // Assignment operators (=, :=)
        val ASSIGNMENT_OPERATORS = TokenSet.create(
            PicatTokenTypes.ASSIGN_OP,
            PicatTokenTypes.ASSIGN_ONCE,
            PicatTokenTypes.EQUAL
        )

        // Logical operators (&&, ||, !)
        val LOGICAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.AND,
            PicatTokenTypes.OR,
            PicatTokenTypes.NOT
        )

        // Equality operators (==, !=, ===, !==)
        val EQUALITY_OPERATORS = TokenSet.create(
            PicatTokenTypes.EQUAL,
            PicatTokenTypes.NOT_EQUAL,
            PicatTokenTypes.IDENTICAL,
            PicatTokenTypes.NOT_IDENTICAL
        )

        // Relational operators (<, >, <=, >=)
        val RELATIONAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.LESS,
            PicatTokenTypes.GREATER,
            PicatTokenTypes.LESS_EQUAL,
            PicatTokenTypes.LESS_EQUAL_ALT,
            PicatTokenTypes.GREATER_EQUAL
        )

        // Additive operators (+, -, ++)
        val ADDITIVE_OPERATORS = TokenSet.create(
            PicatTokenTypes.PLUS,
            PicatTokenTypes.MINUS,
            PicatTokenTypes.CONCAT_OP
        )

        // Multiplicative operators (*, /, //, mod)
        val MULTIPLICATIVE_OPERATORS = TokenSet.create(
            PicatTokenTypes.MULTIPLY,
            PicatTokenTypes.DIVIDE,
            PicatTokenTypes.INT_DIVIDE,
            PicatTokenTypes.MODULO,
            PicatTokenTypes.MOD_KEYWORD
        )

        // Constraint operators (#=, #!=, etc.)
        val CONSTRAINT_OPERATORS = TokenSet.create(
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

        // Term comparison operators (@<, @=<, etc.)
        val TERM_COMPARISON_OPERATORS = TokenSet.create(
            PicatTokenTypes.TERM_LT,
            PicatTokenTypes.TERM_LE,
            PicatTokenTypes.TERM_LE_ALT,
            PicatTokenTypes.TERM_GT,
            PicatTokenTypes.TERM_GE
        )

        // Bitwise operators (/\, \/, <<, >>)
        val BITWISE_OPERATORS = TokenSet.create(
            PicatTokenTypes.BITWISE_AND,
            PicatTokenTypes.BITWISE_OR,
            PicatTokenTypes.SHIFT_LEFT,
            PicatTokenTypes.SHIFT_RIGHT
        )

        // Closing brackets/parentheses/braces
        val CLOSING_BRACKETS = TokenSet.create(
            PicatTokenTypes.RBRACKET,
            PicatTokenTypes.RPAR,
            PicatTokenTypes.RBRACE
        )

        // Keywords after which spaces are needed
        val KEYWORDS_WITH_SPACE_AFTER = TokenSet.create(
            PicatTokenTypes.IF_KEYWORD,
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD,
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD,
            PicatTokenTypes.RETURN_KEYWORD
        )

        // Block keywords for indentation (then, else)
        val BLOCK_KEYWORDS = TokenSet.create(
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD
        )

        // Block end keywords (end, else, elseif)
        val BLOCK_END_KEYWORDS = TokenSet.create(
            PicatTokenTypes.END_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD,
            PicatTokenTypes.ELSEIF_KEYWORD
        )

        // Loop keywords (foreach, while, for)
        val LOOP_KEYWORDS = TokenSet.create(
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD
        )
    }

    init {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
//        val commonSettings = settings.getCommonSettings(PicatLanguage)

        spacingBuilder = SpacingBuilder(settings, PicatLanguage)
            // Assignment operators (=, :=)
            .around(ASSIGNMENT_OPERATORS)
            .spaces(1)

            // Ensure proper spacing around operators in expressions
            .around(PicatTokenTypes.OPERATOR)
            .spaces(1)

            // Logical operators (&&, ||, !)
            .around(LOGICAL_OPERATORS)
            .spaceIf(picatSettings.spaceAroundLogicalOperators)

            // Equality operators (==, !=, ===, !==)
            .around(EQUALITY_OPERATORS)
            .spaces(1)

            // Relational operators (<, >, <=, >=)
            .around(RELATIONAL_OPERATORS)
            .spaces(1)

            // Additive operators (+, -)
            .around(ADDITIVE_OPERATORS)
            .spaces(1)

            // Multiplicative operators (*, /, //, mod)
            .around(MULTIPLICATIVE_OPERATORS)
            .spaces(1)

            // Rule operators (=>, ?=>)
            .before(RULE_OPERATORS)
            .spaces(1)
//
//            // Add indentation for the rule body (between rule operator and end dot)
//            .between(ruleOperators, PicatTokenTypes.DOT)
//            .spacing(4, Integer.MAX_VALUE, 1, true, 4)

            // Ensure line break after rule operators
            .after(RULE_OPERATORS)
            .spacing(0, Integer.MAX_VALUE, 1, true, 4)

            // Constraint operators (#=, #!=, etc.)
            .around(CONSTRAINT_OPERATORS)
            .spaces(1)

            // Constraint rule operators (#=>, #<=>)
            .around(CONSTRAINT_RULE_OPERATORS)
            .spaces(1)
            .after(CONSTRAINT_RULE_OPERATORS)
            .spacing(1, 1, 0, true, 1)

            // Term comparison operators (@<, @=<, etc.)
            .around(TERM_COMPARISON_OPERATORS)
            .spaces(1)

            // Range operator (..)
            .around(PicatTokenTypes.RANGE)
            .spaces(1)

            // Type constraint operator (::)
            .around(PicatTokenTypes.TYPE_CONSTRAINT)
            .spaces(1)

            // Bitwise operators (/\, \/, <<, >>)
            .around(BITWISE_OPERATORS)
            .spaces(1)

            // Punctuation
            .before(PicatTokenTypes.COMMA)
            .spaceIf(picatSettings.spaceBeforeComma)
            .after(PicatTokenTypes.COMMA)
            .spaces(1)

            // No space after comma when followed by closing brackets/parentheses/braces
            .between(PicatTokenTypes.COMMA, CLOSING_BRACKETS)
            .spaces(0)

            // Ensure proper spacing around colon
            .around(PicatTokenTypes.COLON)
            .spaces(1)

            // Parentheses, brackets, braces
            .withinPair(PicatTokenTypes.LPAR, PicatTokenTypes.RPAR)
            .spaces(0)
            .withinPair(PicatTokenTypes.LBRACKET, PicatTokenTypes.RBRACKET)
            .spaces(0)
            .withinPair(PicatTokenTypes.LBRACE, PicatTokenTypes.RBRACE)
            .spaces(0)

            // After keywords
            .after(KEYWORDS_WITH_SPACE_AFTER)
            .spaces(1)

            // Ensure proper indentation for statements after block keywords
            .between(BLOCK_KEYWORDS, BLOCK_END_KEYWORDS)
            .spacing(1, Integer.MAX_VALUE, 1, true, 1)

            // Ensure proper indentation for statements after loop keywords
            .between(LOOP_KEYWORDS, PicatTokenTypes.END_KEYWORD)
            .spacing(1, Integer.MAX_VALUE, 1, true, 1)
    }

    /**
     * Returns the spacing builder.
     */
    fun getSpacingBuilder(): SpacingBuilder = spacingBuilder
}
