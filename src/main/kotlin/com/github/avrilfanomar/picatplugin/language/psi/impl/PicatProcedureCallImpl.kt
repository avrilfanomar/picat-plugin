package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.* // ktlint-disable no-wildcard-imports
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class PicatProcedureCallImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatProcedureCall {

    override fun getCoreExpression(): PicatExpression? {
        // The parser wraps an expression (atom or structure) with PROCEDURE_CALL.
        // So, the first PicatExpression child is the core.
        return findChildByClass(PicatExpression::class.java)
    }

    override fun getNameIdentifier(): PsiElement? {
        val core = getCoreExpression()
        return when (core) {
            is PicatStructure -> core.getFunctor() // Updated to use getFunctor()
            is PicatAtom -> core.getIdentifier() ?: core.getQuotedAtom() // From PicatAtom interface
            is PicatQualifiedAtom -> core.getNameAtom() // From PicatQualifiedAtom interface
            else -> null
        }
    }

    override fun getArgumentList(): PicatArgumentList? {
        val core = getCoreExpression()
        return if (core is PicatStructure) {
            core.getArgumentList() // Assuming PicatStructure has getArgumentList()
        } else {
            null
        }
    }

    override fun getName(): String? {
        return getNameIdentifier()?.text
    }

    override fun setName(name: String): PsiElement {
        // Complex: involves potentially changing atom/functor name.
        // Delegate to PsiImplUtil or similar if available for actual refactoring.
        val nameId = getNameIdentifier()
        // nameId?.replace(PicatPsiElementFactory.createAtom(project, name)) // Example
        return this
    }
}
