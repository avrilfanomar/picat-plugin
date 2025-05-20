package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatTerm interface.
 */
class PicatTermImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatTerm {
    /**
     * Returns the literal of the term, if it's a literal.
     */
    override fun getLiteral(): PicatLiteral? {
        return PsiTreeUtil.getChildOfType(this, PicatLiteral::class.java)
    }
    
    /**
     * Returns the variable of the term, if it's a variable.
     */
    override fun getVariable(): PicatVariable? {
        return PsiTreeUtil.getChildOfType(this, PicatVariable::class.java)
    }
    
    /**
     * Returns the list of the term, if it's a list.
     */
    override fun getList(): PicatList? {
        return PsiTreeUtil.getChildOfType(this, PicatList::class.java)
    }
    
    /**
     * Returns the structure of the term, if it's a structure.
     */
    override fun getStructure(): PicatStructure? {
        return PsiTreeUtil.getChildOfType(this, PicatStructure::class.java)
    }
    
    /**
     * Returns the expression of the term, if it's a parenthesized expression.
     */
    override fun getExpression(): PicatExpression? {
        return PsiTreeUtil.getChildOfType(this, PicatExpression::class.java)
    }
}