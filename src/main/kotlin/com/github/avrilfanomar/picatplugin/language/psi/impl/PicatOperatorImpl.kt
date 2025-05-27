package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatOperator
import com.intellij.lang.ASTNode

/**
 * Implementation of the PicatOperator interface.
 */
class PicatOperatorImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatOperator {
    /**
     * Returns the operator type as a string.
     */
    fun getOperatorType(): String {
        return node.text
    }
}