package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * Base implementation class for all Picat PSI elements.
 * This class is extended by all specific Picat PSI element implementation classes.
 */
open class PicatPsiElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), PicatPsiElement {
    override fun toString(): String = "PicatPsiElement:" + node.elementType
}
