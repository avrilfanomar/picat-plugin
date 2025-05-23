package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatGoal
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatBody interface.
 */
class PicatBodyImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatBody {
    /**
     * Returns the goals in the body.
     */
    override fun getGoals(): List<PicatGoal> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatGoal::class.java)
    }
}