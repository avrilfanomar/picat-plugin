package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * Customizable code style settings for Picat.
 * This class defines all the configurable formatting options for Picat code.
 */
class PicatCodeStyleSettings(settings: CodeStyleSettings) : CustomCodeStyleSettings("PicatCodeStyleSettings", settings) {
    // Spacing options
    @JvmField var SPACE_AROUND_ASSIGNMENT_OPERATORS = true
    @JvmField var SPACE_AROUND_LOGICAL_OPERATORS = true
    @JvmField var SPACE_AROUND_EQUALITY_OPERATORS = true
    @JvmField var SPACE_AROUND_RELATIONAL_OPERATORS = true
    @JvmField var SPACE_AROUND_ADDITIVE_OPERATORS = true
    @JvmField var SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true

    // Picat-specific operators
    @JvmField var SPACE_BEFORE_RULE_OPERATORS = true
    @JvmField var SPACE_AROUND_CONSTRAINT_OPERATORS = true
    @JvmField var SPACE_AROUND_TERM_COMPARISON_OPERATORS = true
    @JvmField var SPACE_AROUND_RANGE_OPERATOR = true
    @JvmField var SPACE_AROUND_TYPE_CONSTRAINT_OPERATOR = true
    @JvmField var SPACE_AROUND_BITWISE_OPERATORS = true

    // Punctuation
    @JvmField var SPACE_BEFORE_COMMA = false
    @JvmField var SPACE_AFTER_COMMA = false
    @JvmField var SPACE_AROUND_COLON = true

    // Line breaks
    @JvmField var KEEP_LINE_BREAK_AFTER_RULE_OPERATORS = true
    @JvmField var LINE_BREAK_AFTER_BLOCK_KEYWORDS = true
    @JvmField var LINE_BREAK_AFTER_DOT = true
    @JvmField var LINE_BREAK_AFTER_END_KEYWORD = true

    // Indentation
    @JvmField var INDENT_RULE_BODY = true
    @JvmField var INDENT_BLOCK_STATEMENTS = true
    @JvmField var INDENT_LIST_COMPREHENSION = true
}
