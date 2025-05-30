package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatFunctionBody interface.
 */
class PicatFunctionBodyImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFunctionBody {
    /**
     * Returns the expression that represents the function's return value.
     */
    override fun getReturnExpression(): PicatExpression? {
        return PsiTreeUtil.getChildOfType(this, PicatExpression::class.java)
    }

    /**
     * Returns the statements in the function body, if any.
     * For simple function bodies, this will be empty.
     */
    override fun getStatements(): List<PicatPsiElement> {
        return emptyList()
    }
}
