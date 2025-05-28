package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat range expression PSI elements.
 * A range expression in Picat is a term optionally followed by ".." and another term.
 */
interface PicatRangeExpression : PsiElement {
    /**
     * Returns the left term of the range expression.
     */
    fun getLeft(): PicatTerm?

    /**
     * Returns the right term of the range expression, if any.
     * If this is not a range expression (i.e., there is no ".." operator),
     * this method returns null.
     */
    fun getRight(): PicatTerm?
}
