package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.github.avrilfanomar.picatplugin.language.psi.PicatHeadReference
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
// import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes // For INTEGER token

class PicatHeadReferenceImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatHeadReference {
    // TODO: Implement methods, e.g.,
    // fun getAtom(): PicatAtom? = findChildByClass(PicatAtom::class.java)
    // fun getArityIdentifier(): PsiElement? = findChildByType(PicatTokenTypes.INTEGER)
}
