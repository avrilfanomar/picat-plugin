package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariableList
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariable // For potential getVariableList()
import com.intellij.psi.util.PsiTreeUtil

class PicatVariableListImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatVariableList {
    // TODO: Implement methods, e.g.,
    // fun getVariables(): List<PicatVariable> = PsiTreeUtil.getChildrenOfTypeAsList(this, PicatVariable::class.java)
}
