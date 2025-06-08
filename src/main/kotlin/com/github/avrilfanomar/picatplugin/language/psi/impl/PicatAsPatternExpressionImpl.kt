package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatAsPatternExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariable // For potential getVariable()
import com.github.avrilfanomar.picatplugin.language.psi.PicatPattern // For potential getPattern()

class PicatAsPatternExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatAsPatternExpression {
    // TODO: Implement methods, e.g.,
    // fun getVariable(): PicatVariable? = findChildByClass(PicatVariable::class.java)
    // fun getPattern(): PicatPattern? = findChildByClass(PicatPattern::class.java)
}
