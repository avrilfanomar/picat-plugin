package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat goal PSI elements.
 * A goal in Picat is a call, if-then-else, assignment, unification, comparison, etc.
 */
interface PicatGoal : PicatPsiElement {
    /**
     * Returns the type of the goal.
     */
    fun getType(): String

    /**
     * Returns the call of the goal, if it's a call.
     */
    fun getCall(): PsiElement?

    /**
     * Returns the if-then-else of the goal, if it's an if-then-else.
     */
    fun getIfThenElse(): PsiElement?

    /**
     * Returns the assignment of the goal, if it's an assignment.
     */
    fun getAssignment(): PsiElement?

    /**
     * Returns the unification of the goal, if it's a unification.
     */
    fun getUnification(): PsiElement?

    /**
     * Returns the comparison of the goal, if it's a comparison.
     */
    fun getComparison(): PsiElement?

    /**
     * Returns the negation of the goal, if it's a negation.
     */
    fun getNegation(): PsiElement?
}