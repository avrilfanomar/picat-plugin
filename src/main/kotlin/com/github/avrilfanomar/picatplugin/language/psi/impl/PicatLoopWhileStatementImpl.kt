package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatLoopWhileStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatBody // For getBody()
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // For getCondition()

class PicatLoopWhileStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatLoopWhileStatement {
    // TODO: Implement methods, e.g.,
    // fun getBody(): PicatBody? = findChildByClass(PicatBody::class.java)
    // fun getCondition(): PicatExpression? = findChildByClass(PicatExpression::class.java) // Might need to be more specific if there are multiple expressions
}
