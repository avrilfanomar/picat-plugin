package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatIndexAccessExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariable // For potential getVariable()
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // For potential getIndexExpressions()
import com.intellij.psi.util.PsiTreeUtil

class PicatIndexAccessExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatIndexAccessExpression {
    // TODO: Implement methods, e.g.,
    // fun getVariable(): PicatVariable? = findChildByClass(PicatVariable::class.java)
    // fun getIndexExpressions(): List<PicatExpression> = PsiTreeUtil.getChildrenOfTypeAsList(this, PicatExpression::class.java)
}
