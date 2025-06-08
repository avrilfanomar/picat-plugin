package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatActionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead // For potential getHead()
import com.github.avrilfanomar.picatplugin.language.psi.PicatBody // For potential getBody()

class PicatActionRuleImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatActionRule {
    // TODO: Implement methods, e.g.,
    // fun getHead(): PicatHead? = findChildByClass(PicatHead::class.java)
    // fun getBody(): PicatBody? = findChildByClass(PicatBody::class.java)
}
