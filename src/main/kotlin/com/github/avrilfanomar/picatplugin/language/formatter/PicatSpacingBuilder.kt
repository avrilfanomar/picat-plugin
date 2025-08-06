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
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )

        // Constraint rule operators (#=>, #<=>)
        val CONSTRAINT_RULE_OPERATORS = TokenSet.create(
            PicatTokenTypes.HASH_ARROW_OP,
            PicatTokenTypes.HASH_BICONDITIONAL_OP
        )

        // Assignment operators (:=)
        val ASSIGNMENT_OPERATORS = TokenSet.create(
            PicatTokenTypes.ASSIGN_OP
        )

        // Logical operators (&&, ||, !)
        val LOGICAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.HASH_OR_OP,
            PicatTokenTypes.NOT_KEYWORD
        )

        // Equality operators (==, !=, ===, !==)
        val EQUALITY_OPERATORS = TokenSet.create(
            PicatTokenTypes.NOT_EQUAL,
            PicatTokenTypes.IDENTICAL,
            PicatTokenTypes.NOT_IDENTICAL
        )

        // Relational operators (<, >, <=, >=)
        val RELATIONAL_OPERATORS = TokenSet.create(
            PicatTokenTypes.LESS,
            PicatTokenTypes.GREATER,
            PicatTokenTypes.LESS_EQUAL,
            PicatTokenTypes.GREATER_EQUAL,
            PicatTokenTypes.LESS_EQUAL_PROLOG
        )

        // Term comparison operators (@<, @>, @=<, @>=)
        val TERM_COMPARISON_OPERATORS = TokenSet.create(
            PicatTokenTypes.AT_LESS_OP,
            PicatTokenTypes.AT_GREATER_OP,
            PicatTokenTypes.AT_LESS_EQUAL_OP,
            PicatTokenTypes.AT_GREATER_EQUAL_OP,
            PicatTokenTypes.AT_LESS_EQUAL_PROLOG_OP
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
            PicatTokenTypes.DIV_KEYWORD,
            PicatTokenTypes.MOD_KEYWORD,
            PicatTokenTypes.REM_KEYWORD
        )

        // Constraint operators (#=, #!=, #>, #>=, #<, #=<, #<=, etc.)
        val CONSTRAINT_OPERATORS = TokenSet.create(
            PicatTokenTypes.HASH_AND_OP,
            PicatTokenTypes.HASH_OR_OP,
            PicatTokenTypes.HASH_XOR_OP,
            PicatTokenTypes.HASH_NOT_OP,
            PicatTokenTypes.HASH_EQUAL_OP,
            PicatTokenTypes.HASH_NOT_EQUAL_OP,
            PicatTokenTypes.HASH_GREATER_OP,
            PicatTokenTypes.HASH_GREATER_EQUAL_OP,
            PicatTokenTypes.HASH_LESS_OP,
            PicatTokenTypes.HASH_LESS_EQUAL_OP,
            PicatTokenTypes.HASH_LESS_EQUAL_ALT_OP
        )

        // Bitwise operators (/\, \/, <<, >>)
        val BITWISE_OPERATORS = TokenSet.create(
            PicatTokenTypes.BITWISE_OR,
            PicatTokenTypes.BITWISE_AND,
            PicatTokenTypes.BITWISE_XOR,
            PicatTokenTypes.COMPLEMENT,
            PicatTokenTypes.SHIFT_LEFT,
            PicatTokenTypes.SHIFT_RIGHT,
            PicatTokenTypes.SHIFT_RIGHT_TRIPLE
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
            PicatTokenTypes.ELSEIF_KEYWORD,
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD,
            PicatTokenTypes.LOOP_KEYWORD,
            PicatTokenTypes.TRY_KEYWORD,
            PicatTokenTypes.CATCH_KEYWORD,
            PicatTokenTypes.FINALLY_KEYWORD,
            PicatTokenTypes.IN_KEYWORD
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

        spacingBuilder = SpacingBuilder(settings, PicatLanguage)
            // Assignment operators (:=)
            .around(ASSIGNMENT_OPERATORS)
            .spaces(1)

            // Special handling for EQUAL token (used for both assignment and equality)
            .around(PicatTokenTypes.EQUAL)
            .spaces(1)

            // Logical operators (&&, ||, !)
            .around(LOGICAL_OPERATORS)
            .spaceIf(picatSettings.spaceAroundLogicalOperators)

            // Equality operators (!=, ===, !==)
            .around(EQUALITY_OPERATORS)
            .spaces(1)

            // Relational operators (<, >, <=, >=, =<)
            .around(RELATIONAL_OPERATORS)
            .spaces(1)

            // Term comparison operators (@<, @>, @=<, @>=)
            .around(TERM_COMPARISON_OPERATORS)
            .spaces(1)

            // Additive operators (+, -, ++)
            .around(ADDITIVE_OPERATORS)
            .spaces(1)

            // Multiplicative operators (*, /, //, mod)
            .around(MULTIPLICATIVE_OPERATORS)
            .spaces(1)

            // Rule operators (=>, ?=>)
            .around(RULE_OPERATORS)
            .spaces(1)

            // Ensure line break after rule operators
            .after(RULE_OPERATORS)
            .lineBreakInCode()

            // Constraint operators (#=, #!=, etc.)
            .around(CONSTRAINT_OPERATORS)
            .spaces(1)

            // Constraint rule operators (#=>, #<=>)
            .around(CONSTRAINT_RULE_OPERATORS)
            .spaces(1)

            // Range operator (..)
            .around(PicatTokenTypes.RANGE_OP)
            .spaces(1)

            // Type constraint operator (::)
            .around(PicatTokenTypes.DOUBLE_COLON_OP)
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
            .spaceIf(false)
            .withinPair(PicatTokenTypes.LBRACKET, PicatTokenTypes.RBRACKET)
            .spaceIf(false)
            .withinPair(PicatTokenTypes.LBRACE, PicatTokenTypes.RBRACE)
            .spaceIf(false)

            // After keywords
            .after(KEYWORDS_WITH_SPACE_AFTER)
            .spaces(1)

            // Ensure proper indentation for statements after block keywords
            .between(BLOCK_KEYWORDS, BLOCK_END_KEYWORDS)
            .lineBreakInCode()

            // Ensure proper indentation for statements after loop keywords
            .between(LOOP_KEYWORDS, PicatTokenTypes.END_KEYWORD)
            .lineBreakInCode()

            // Line break after dot
            .after(PicatTokenTypes.DOT)
            .lineBreakInCode()
    }

    /**
     * Returns the spacing builder.
     */
    fun getSpacingBuilder(): SpacingBuilder = spacingBuilder
}
