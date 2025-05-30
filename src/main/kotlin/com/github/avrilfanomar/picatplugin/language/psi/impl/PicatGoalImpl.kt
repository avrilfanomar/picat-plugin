package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatGoal
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatGoal interface.
 */
class PicatGoalImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatGoal {
    /**
     * Returns the type of the goal.
     */
    override fun getType(): String {
        if (getCall() != null) return "call"
        if (getIfThenElse() != null) return "if_then_else"
        if (getAssignment() != null) return "assignment"
        if (getUnification() != null) return "unification"
        if (getComparison() != null) return "comparison"
        if (getNegation() != null) return "negation"
        return "unknown"
    }

    /**
     * Returns the call of the goal, if it's a call.
     */
    override fun getCall(): PsiElement? {
        return findChildByType(PicatTokenTypes.STRUCTURE) ?: findChildByType(PicatTokenTypes.ATOM)
    }

    /**
     * Returns the if-then-else of the goal, if it's an if-then-else.
     */
    override fun getIfThenElse(): PsiElement? {
        return findChildByType(PicatTokenTypes.IF_THEN_ELSE)
    }

    /**
     * Returns the assignment of the goal, if it's an assignment.
     */
    override fun getAssignment(): PsiElement? {
        // Look for a variable followed by an assignment operator
        val variable = findChildByType<PsiElement>(PicatTokenTypes.VARIABLE)
        if (variable != null) {
            val nextSibling = PsiTreeUtil.skipWhitespacesAndCommentsForward(variable)
            if (nextSibling != null && nextSibling.node.elementType == PicatTokenTypes.ASSIGN_ONCE) {
                return variable
            }
        }
        return null
    }

    /**
     * Returns the unification of the goal, if it's a unification.
     */
    override fun getUnification(): PsiElement? {
        // Look for an expression followed by an equal sign
        val children = node.getChildren(null)
        for (i in 0 until children.size - 1) {
            if (children[i].elementType == PicatTokenTypes.EXPRESSION &&
                (children[i + 1].elementType == PicatTokenTypes.EQUAL ||
                        children[i + 1].elementType == PicatTokenTypes.NOT_EQUAL)
            ) {
                return children[i].psi
            }
        }
        return null
    }

    /**
     * Returns the comparison of the goal, if it's a comparison.
     */
    override fun getComparison(): PsiElement? {
        // Look for an expression followed by a comparison operator
        val children = node.getChildren(null)
        for (i in 0 until children.size - 1) {
            if (isComparisonExpression(children, i)) {
                return children[i].psi
            }
        }
        return null
    }

    /**
     * Checks if the element at index i is an expression followed by a comparison operator.
     */
    private fun isComparisonExpression(children: Array<ASTNode>, i: Int): Boolean {
        return children[i].elementType == PicatTokenTypes.EXPRESSION &&
                isComparisonOperator(children[i + 1].elementType)
    }

    /**
     * Checks if the element type is a comparison operator.
     */
    private fun isComparisonOperator(elementType: com.intellij.psi.tree.IElementType): Boolean {
        return elementType == PicatTokenTypes.LESS ||
                elementType == PicatTokenTypes.GREATER ||
                elementType == PicatTokenTypes.LESS_EQUAL ||
                elementType == PicatTokenTypes.GREATER_EQUAL
    }

    /**
     * Returns the negation of the goal, if it's a negation.
     */
    override fun getNegation(): PsiElement? {
        // Look for a 'not' keyword
        return findChildByType(PicatTokenTypes.NOT_KEYWORD)
    }
}
