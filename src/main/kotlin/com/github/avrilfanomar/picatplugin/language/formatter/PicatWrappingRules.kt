package com.github.avrilfanomar.picatplugin.language.formatter

/**
 * Wrapping rules: responsible for inserting line breaks around structural rule operators.
 * This keeps PicatIndentationEngine focused strictly on indentation.
 */
internal class PicatWrappingRules {

    /**
     * Insert a newline after rule operators so that indentation can be applied line-by-line.
     * Keeps operator spacing untouched; spacing is handled separately by PicatOperatorFormatter.
     */
    fun addLineBreaksAfterRuleOperators(code: String): String {
        var result = code
        // Ensure we handle both spaced and unspaced operators, e.g., X=>Y and X => Y
        result = result.replace("=>", "=>\n")
        result = result.replace("?=>", "?=>\n")
        result = result.replace("#=>", "#=>\n")
        result = result.replace("#<=>", "#<=>\n")
        result = result.replace(":-", ":-\n")
        // Collapse accidental double new lines
        result = result.replace("\n\n", "\n")
        // Avoid introducing spaces around the operator; the operator spacing is handled elsewhere
        result = result.replace("  \n", "\n")
        return result
    }
}

