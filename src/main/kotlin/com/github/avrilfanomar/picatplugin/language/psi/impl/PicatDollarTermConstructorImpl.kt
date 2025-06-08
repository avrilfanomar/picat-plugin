package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatDollarTermConstructor
import com.github.avrilfanomar.picatplugin.language.psi.PicatGoal // For potential getGoal() method

class PicatDollarTermConstructorImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatDollarTermConstructor {
    // Example of a possible accessor method, assuming 'goal' is a direct child type
    // fun getGoal(): PicatGoal? = findChildByClass(PicatGoal::class.java)
}
