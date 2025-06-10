package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatCaseArm
import com.github.avrilfanomar.picatplugin.language.psi.PicatCaseArms
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PicatCaseArmsImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCaseArms {

    override fun getArmList(): List<PicatCaseArm> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatCaseArm::class.java)
        // Alternative, if CASE_ARM is a token type for the rule:
        // return findChildrenByType(PicatTokenTypes.CASE_ARM)
    }
}
