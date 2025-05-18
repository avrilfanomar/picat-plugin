package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTypes
import com.intellij.psi.tree.TokenSet

/**
 * Token sets for Picat language.
 * These token sets are used by the parser definition.
 */
object PicatTokenSets {
    /**
     * Token set for comments.
     */
    val COMMENTS = PicatTypes.COMMENTS

    /**
     * Token set for string literals.
     */
    val STRINGS = PicatTypes.STRINGS

    /**
     * Token set for operators.
     */
    val OPERATORS = PicatTypes.OPERATORS
}
