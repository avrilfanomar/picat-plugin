package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * Customizable code style settings for Picat.
 * This class defines all the configurable formatting options for Picat code.
 */
class PicatCodeStyleSettings(settings: CodeStyleSettings) :
    CustomCodeStyleSettings("PicatCodeStyleSettings", settings) {
    // Spacing options
    /** Whether to add spaces around assignment operators (=, :=). */
    @JvmField
    var spaceAroundAssignmentOperators = true
    /** Whether to add spaces around logical operators (and, or, not). */
    @JvmField
    var spaceAroundLogicalOperators = true
    /** Whether to add spaces around equality operators (==, !=). */
    @JvmField
    var spaceAroundEqualityOperators = true
    /** Whether to add spaces around relational operators (<, >, <=, >=). */
    @JvmField
    var spaceAroundRelationalOperators = true
    /** Whether to add spaces around additive operators (+, -). */
    @JvmField
    var spaceAroundAdditiveOperators = true
    /** Whether to add spaces around multiplicative operators (*, /, mod). */
    @JvmField
    var spaceAroundMultiplicativeOperators = true

    // Picat-specific operators
    /** Whether to add spaces before rule operators (:-, =>, ?=>). */
    @JvmField
    var spaceBeforeRuleOperators = true
    /** Whether to add spaces around constraint operators (#=, #<, #>). */
    @JvmField
    var spaceAroundConstraintOperators = tru
    /** Whether to add spaces around term comparison operators (@<, @>, @=<, @>=). */
    @JvmField
    var spaceAroundTermComparisonOperators = true
    /** Whether to add spaces around range operator (..). */
    @JvmField
    var spaceAroundRangeOperator = false
    /** Whether to add spaces around type constraint operator (::). */
    @JvmField
    var spaceAroundTypeConstraintOperator = true
    /** Whether to add spaces around bitwise operators (/\, \/, <<, >>). */
    @JvmField
    var spaceAroundBitwiseOperators = true

    // Punctuation
    /** Whether to add spaces before commas. */
    @JvmField
    var spaceBeforeComma = false
    /** Whether to add spaces after commas. */
    @JvmField
    var spaceAfterComma = true
    /** Whether to add spaces around colons. */
    @JvmField
    var spaceAroundColon = true

    // Line breaks
    /** Whether to keep line breaks after rule operators (:-, =>, ?=>). */
    @JvmField
    var keepLineBreakAfterRuleOperators = true
    /** Whether to add line breaks after block keywords (if, foreach, while). */
    @JvmField
    var lineBreakAfterBlockKeywords = true
    /** Whether to add line breaks after dot terminators. */
    @JvmField
    var lineBreakAfterDot = true
    /** Whether to add line breaks after end keywords. */
    @JvmField
    var lineBreakAfterEndKeyword = true

    // Indentation
    /** Whether to indent rule bodies. */
    @JvmField
    var indentRuleBody = true
    /** Whether to indent block statements (if-then-else, loops). */
    @JvmField
    var indentBlockStatements = true
    /** Whether to indent list comprehension expressions. */
    @JvmField
    var indentListComprehension = true
}
