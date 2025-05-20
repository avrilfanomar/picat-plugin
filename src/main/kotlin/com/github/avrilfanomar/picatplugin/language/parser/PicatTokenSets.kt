package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.TokenSet

/**
 * Token sets for Picat language.
 * These token sets are used by the parser definition.
 */
object PicatTokenSets {
    /**
     * Token set for comments.
     */
    val COMMENTS = PicatTokenTypes.COMMENTS

    /**
     * Token set for string literals.
     */
    val STRINGS = PicatTokenTypes.STRING_LITERALS

    /**
     * Token set for operators.
     */
    val OPERATORS = PicatTokenTypes.OPERATORS
}
