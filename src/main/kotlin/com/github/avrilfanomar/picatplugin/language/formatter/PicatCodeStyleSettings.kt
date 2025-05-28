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
    @JvmField
    var spaceAroundAssignmentOperators = true
    @JvmField
    var spaceAroundLogicalOperators = true
    @JvmField
    var spaceAroundEqualityOperators = true
    @JvmField
    var spaceAroundRelationalOperators = true
    @JvmField
    var spaceAroundAdditiveOperators = true
    @JvmField
    var spaceAroundMultiplicativeOperators = true

    // Picat-specific operators
    @JvmField
    var spaceBeforeRuleOperators = true
    @JvmField
    var spaceAroundConstraintOperators = true
    @JvmField
    var spaceAroundTermComparisonOperators = true
    @JvmField
    var spaceAroundRangeOperator = true
    @JvmField
    var spaceAroundTypeConstraintOperator = true
    @JvmField
    var spaceAroundBitwiseOperators = true

    // Punctuation
    @JvmField
    var spaceBeforeComma = false
    @JvmField
    var spaceAfterComma = true
    @JvmField
    var spaceAroundColon = true

    // Line breaks
    @JvmField
    var keepLineBreakAfterRuleOperators = true
    @JvmField
    var lineBreakAfterBlockKeywords = true
    @JvmField
    var lineBreakAfterDot = true
    @JvmField
    var lineBreakAfterEndKeyword = true

    // Indentation
    @JvmField
    var indentRuleBody = true
    @JvmField
    var indentBlockStatements = true
    @JvmField
    var indentListComprehension = true
}
