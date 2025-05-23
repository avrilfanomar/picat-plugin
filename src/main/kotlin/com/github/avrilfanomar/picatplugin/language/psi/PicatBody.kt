package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat body PSI elements.
 * A body in Picat is a sequence of goals that appears after the rule operator in a rule.
 */
interface PicatBody : PicatPsiElement {
    /**
     * Returns the goals in the body.
     */
    fun getGoals(): List<PicatGoal>
}
