package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * Custom code style settings for Picat language.
 * This class defines the code style settings specific to Picat.
 */
class PicatCodeStyleSettings(settings: CodeStyleSettings) : CustomCodeStyleSettings("PicatCodeStyleSettings", settings) {
    // Default indentation size
    var INDENT_SIZE = 4

    // Spacing around operators
    var SPACE_AROUND_ASSIGNMENT_OPERATORS = true
    var SPACE_AROUND_LOGICAL_OPERATORS = true
    var SPACE_AROUND_EQUALITY_OPERATORS = true
    var SPACE_AROUND_RELATIONAL_OPERATORS = true
    var SPACE_AROUND_ADDITIVE_OPERATORS = true
    var SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true

    // Spacing around Picat-specific operators
    var SPACE_AROUND_RULE_OPERATORS = true
    var SPACE_AROUND_CONSTRAINT_OPERATORS = true
    var SPACE_AROUND_TERM_COMPARISON_OPERATORS = true
    var SPACE_AROUND_RANGE_OPERATOR = false
    var SPACE_AROUND_TYPE_CONSTRAINT_OPERATOR = true
    var SPACE_AROUND_BITWISE_OPERATORS = true

    // Spacing within parentheses
    var SPACE_WITHIN_PARENTHESES = false
    var SPACE_WITHIN_BRACKETS = false
    var SPACE_WITHIN_BRACES = false

    // Spacing around punctuation
    var SPACE_BEFORE_COMMA = false
    var SPACE_AFTER_COMMA = false
    var SPACE_AROUND_COLON = true

    // Spacing after keywords
    var SPACE_AFTER_CONTROL_KEYWORDS = true

    // Line breaks and indentation
    var LINE_BREAK_AFTER_RULE_OPERATORS = false
    var LINE_BREAK_AFTER_BLOCK_KEYWORDS = false
    var LINE_BREAK_AFTER_DOT = false
    var LINE_BREAK_AFTER_END_KEYWORD = true

    // Line breaks
    var KEEP_LINE_BREAKS = true
    var KEEP_BLANK_LINES_IN_CODE = 2

    // Alignment
    var ALIGN_MULTILINE_PARAMETERS = true
    var ALIGN_MULTILINE_ARGUMENTS = true
    var ALIGN_CONSECUTIVE_ASSIGNMENTS = false
    var ALIGN_LIST_ELEMENTS = false

    // Multi-line expressions
    var WRAP_LONG_LINES = false
    var MAX_LINE_LENGTH = 120
    var PREFER_PARAMETERS_WRAP = false

    // Special formatting
    var SPECIAL_ELSE_IF_TREATMENT = true
    var INDENT_CASE_FROM_SWITCH = true
    var SPECIAL_LIST_COMPREHENSION_FORMATTING = true
}
