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

    /**
     * Returns the text of the operator.
     */
    override fun getOperatorText(): String {
        return node.text
    }

    /**
     * Returns the precedence of the operator.
     * Higher precedence operators are evaluated before lower precedence operators.
     * Default implementation returns 0.
     */
    override fun getPrecedence(): Int {
        return 0
    }
}
