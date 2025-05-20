package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Utility functions for Picat code formatting.
 */
object PicatFormattingUtils {
    /**
     * Checks if the element type is an operator.
     */
    fun isOperator(elementType: IElementType): Boolean {
        return PicatTokenTypes.OPERATORS.contains(elementType)
    }

    /**
     * Checks if the element type is a rule operator (=>, ?=>, etc.).
     */
    fun isRuleOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.ARROW_OP || 
               elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP
    }

    /**
     * Checks if the element type is a constraint operator (#=, #!=, etc.).
     */
    fun isConstraintOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.CONSTRAINT_EQ || 
               elementType == PicatTokenTypes.CONSTRAINT_NEQ ||
               elementType == PicatTokenTypes.CONSTRAINT_LT ||
               elementType == PicatTokenTypes.CONSTRAINT_LE ||
               elementType == PicatTokenTypes.CONSTRAINT_LE_ALT ||
               elementType == PicatTokenTypes.CONSTRAINT_GT ||
               elementType == PicatTokenTypes.CONSTRAINT_GE ||
               elementType == PicatTokenTypes.CONSTRAINT_NOT ||
               elementType == PicatTokenTypes.CONSTRAINT_AND ||
               elementType == PicatTokenTypes.CONSTRAINT_OR ||
               elementType == PicatTokenTypes.CONSTRAINT_XOR ||
               elementType == PicatTokenTypes.CONSTRAINT_IMPL ||
               elementType == PicatTokenTypes.CONSTRAINT_EQUIV
    }

    /**
     * Checks if the element type is a term comparison operator (@<, @=<, etc.).
     */
    fun isTermComparisonOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.TERM_LT || 
               elementType == PicatTokenTypes.TERM_LE ||
               elementType == PicatTokenTypes.TERM_LE_ALT ||
               elementType == PicatTokenTypes.TERM_GT ||
               elementType == PicatTokenTypes.TERM_GE
    }

    /**
     * Checks if the element type is an assignment operator (=, :=).
     */
    fun isAssignmentOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.ASSIGN_OP || 
               elementType == PicatTokenTypes.ASSIGN_ONCE
    }

    /**
     * Checks if the element type is a logical operator (and, or, not).
     */
    fun isLogicalOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.AND || 
               elementType == PicatTokenTypes.OR ||
               elementType == PicatTokenTypes.NOT
    }

    /**
     * Checks if the element type is an equality operator (==, !=, ===, !==).
     */
    fun isEqualityOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.EQUAL || 
               elementType == PicatTokenTypes.NOT_EQUAL ||
               elementType == PicatTokenTypes.IDENTICAL ||
               elementType == PicatTokenTypes.NOT_IDENTICAL
    }

    /**
     * Checks if the element type is a relational operator (<, >, <=, >=).
     */
    fun isRelationalOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.LESS || 
               elementType == PicatTokenTypes.GREATER ||
               elementType == PicatTokenTypes.LESS_EQUAL ||
               elementType == PicatTokenTypes.LESS_EQUAL_ALT ||
               elementType == PicatTokenTypes.GREATER_EQUAL
    }

    /**
     * Checks if the element type is an additive operator (+, -).
     */
    fun isAdditiveOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.PLUS || 
               elementType == PicatTokenTypes.MINUS
    }

    /**
     * Checks if the element type is a multiplicative operator (*, /, mod).
     */
    fun isMultiplicativeOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.MULTIPLY || 
               elementType == PicatTokenTypes.DIVIDE ||
               elementType == PicatTokenTypes.INT_DIVIDE ||
               elementType == PicatTokenTypes.MODULO
    }

    /**
     * Checks if the element type is a bitwise operator (/\, \/, <<, >>).
     */
    fun isBitwiseOperator(elementType: IElementType): Boolean {
        return elementType == PicatTokenTypes.BITWISE_AND || 
               elementType == PicatTokenTypes.BITWISE_OR ||
               elementType == PicatTokenTypes.SHIFT_LEFT ||
               elementType == PicatTokenTypes.SHIFT_RIGHT
    }

    /**
     * Finds the previous non-whitespace sibling of the given node.
     */
    fun findPreviousNonWhitespaceSibling(node: ASTNode): ASTNode? {
        var prev = node.treePrev
        while (prev != null && prev.elementType == TokenType.WHITE_SPACE) {
            prev = prev.treePrev
        }
        return prev
    }

    /**
     * Finds the next non-whitespace sibling of the given node.
     */
    fun findNextNonWhitespaceSibling(node: ASTNode): ASTNode? {
        var next = node.treeNext
        while (next != null && next.elementType == TokenType.WHITE_SPACE) {
            next = next.treeNext
        }
        return next
    }

    /**
     * Checks if the node is inside a list comprehension.
     */
    fun isInsideListComprehension(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            if (parent.elementType.toString() == "LIST_COMPREHENSION") {
                return true
            }
            parent = parent.treeParent
        }
        return false
    }

    /**
     * Checks if the node is inside a rule body.
     */
    fun isInsideRuleBody(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            if (parent.elementType.toString() == "BODY") {
                return true
            }
            parent = parent.treeParent
        }
        return false
    }

    /**
     * Checks if the node is inside a block statement (if-then-else, foreach, etc.).
     */
    fun isInsideBlockStatement(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            val elementType = parent.elementType.toString()
            if (elementType == "IF_THEN_ELSE" || elementType == "FOREACH_LOOP") {
                return true
            }
            parent = parent.treeParent
        }
        return false
    }
}
