package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatLambdaExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatVariableList // For potential getVariableList()
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // For potential getExpression()
import com.github.avrilfanomar.picatplugin.language.psi.PicatBody // For potential getBody()

class PicatLambdaExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatLambdaExpression {
    // TODO: Implement methods, e.g.,
    // fun getVariableList(): PicatVariableList? = findChildByClass(PicatVariableList::class.java)
    // fun getExpression(): PicatExpression? = findChildByClass(PicatExpression::class.java)
    // fun getBody(): PicatBody? = findChildByClass(PicatBody::class.java)
}
