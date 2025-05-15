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

    // Spacing within parentheses
    var SPACE_WITHIN_PARENTHESES = false
    var SPACE_WITHIN_BRACKETS = false
    var SPACE_WITHIN_BRACES = false

    // Line breaks
    var KEEP_LINE_BREAKS = true
    var KEEP_BLANK_LINES_IN_CODE = 2
}
