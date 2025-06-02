package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat rule PSI elements.
 * A rule in Picat has a head and a body, separated by a rule operator (=>, ?=>, <=>, ?<=>, or :-).
 */
interface PicatRule : PicatPsiElement {
    /**
     * Returns the head of the rule.
     */
    fun getHead(): PsiElement?

    /**
     * Returns the body of the rule.
     */
    fun getBody(): PsiElement?

    /**
     * Returns the rule operator.
     */
    fun getOperator(): PsiElement?

}
