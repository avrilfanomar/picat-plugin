package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.github.avrilfanomar.picatplugin.language.psi.PicatTermConstructorExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom // For getFunctor()
import com.github.avrilfanomar.picatplugin.language.psi.PicatQualifiedAtom // For getFunctor()
import com.github.avrilfanomar.picatplugin.language.psi.PicatMapEntries // For getMapEntries()

class PicatTermConstructorExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatTermConstructorExpression {
    // TODO: Implement methods, e.g.,
    // fun getFunctor(): PsiElement? = findChildByClass(PicatAtom::class.java) ?: findChildByClass(PicatQualifiedAtom::class.java)
    // fun getMapEntries(): PicatMapEntries? = findChildByClass(PicatMapEntries::class.java)
}
