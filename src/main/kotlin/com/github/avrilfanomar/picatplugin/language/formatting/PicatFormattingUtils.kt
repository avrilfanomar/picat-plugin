package com.github.avrilfanomar.picatplugin.language.formatting

import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

/**
 * Utility class for Picat formatting.
 * Contains common methods used by the formatting classes.
 */
object PicatFormattingUtils {

    /**
     * Checks if the given element type is an operator.
     * @param elementType The element type to check
     * @return true if the element type is an operator, false otherwise
     */
    fun isOperator(elementType: IElementType): Boolean {
        // Assignment operators
        if (elementType == PicatTokenTypes.ASSIGN_OP || elementType == PicatTokenTypes.ASSIGN_ONCE) {
            return true
        }

        // Arithmetic operators
        if (elementType == PicatTokenTypes.PLUS || elementType == PicatTokenTypes.MINUS || 
            elementType == PicatTokenTypes.MULTIPLY || elementType == PicatTokenTypes.DIVIDE || 
            elementType == PicatTokenTypes.INT_DIVIDE || elementType == PicatTokenTypes.POWER_OP || 
            elementType == PicatTokenTypes.MODULO || elementType == PicatTokenTypes.CONCAT) {
            return true
        }

        // Relational operators
        if (elementType == PicatTokenTypes.LESS || elementType == PicatTokenTypes.LESS_EQUAL || 
            elementType == PicatTokenTypes.LESS_EQUAL_ALT || elementType == PicatTokenTypes.GREATER || 
            elementType == PicatTokenTypes.GREATER_EQUAL) {
            return true
        }

        // Equality operators
        if (elementType == PicatTokenTypes.EQUAL || elementType == PicatTokenTypes.NOT_EQUAL || 
            elementType == PicatTokenTypes.NOT_IDENTICAL || elementType == PicatTokenTypes.IDENTICAL) {
            return true
        }

        // Logical operators
        if (elementType == PicatTokenTypes.AND || elementType == PicatTokenTypes.OR || 
            elementType == PicatTokenTypes.NOT) {
            return true
        }

        // Constraint operators
        if (elementType == PicatTokenTypes.CONSTRAINT_EQ || elementType == PicatTokenTypes.CONSTRAINT_NEQ || 
            elementType == PicatTokenTypes.CONSTRAINT_LT || elementType == PicatTokenTypes.CONSTRAINT_LE || 
            elementType == PicatTokenTypes.CONSTRAINT_LE_ALT || elementType == PicatTokenTypes.CONSTRAINT_GT || 
            elementType == PicatTokenTypes.CONSTRAINT_GE || elementType == PicatTokenTypes.CONSTRAINT_NOT || 
            elementType == PicatTokenTypes.CONSTRAINT_AND || elementType == PicatTokenTypes.CONSTRAINT_OR || 
            elementType == PicatTokenTypes.CONSTRAINT_XOR || elementType == PicatTokenTypes.CONSTRAINT_IMPL || 
            elementType == PicatTokenTypes.CONSTRAINT_EQUIV || elementType == PicatTokenTypes.TYPE_CONSTRAINT) {
            return true
        }

        // Term comparison operators
        if (elementType == PicatTokenTypes.TERM_LT || elementType == PicatTokenTypes.TERM_LE || 
            elementType == PicatTokenTypes.TERM_LE_ALT || elementType == PicatTokenTypes.TERM_GT || 
            elementType == PicatTokenTypes.TERM_GE) {
            return true
        }

        return false
    }

    /**
     * Checks if node1 is a descendant of node2.
     * @param node1 The potential descendant
     * @param node2 The potential ancestor
     * @return true if node1 is a descendant of node2, false otherwise
     */
    fun isDescendantOf(node1: ASTNode, node2: ASTNode): Boolean {
        var current = node1
        while (current != node2 && current.treeParent != null) {
            current = current.treeParent
            if (current == node2) {
                return true
            }
        }
        return false
    }

    /**
     * Finds the previous non-whitespace sibling of the given node.
     * @param node The node to find the previous sibling for
     * @return The previous non-whitespace sibling, or null if there is none
     */
    fun findPreviousNonWhitespaceSibling(node: ASTNode): ASTNode? {
        var prev = node.treePrev
        while (prev != null && PicatBlock.isWhitespaceNode(prev)) {
            prev = prev.treePrev
        }
        return prev
    }

    /**
     * Checks if the given node is a block node (if-then-else, foreach, etc.).
     * @param node The node to check
     * @return true if the node is a block node, false otherwise
     */
    fun isBlockNode(node: ASTNode): Boolean {
        val nodeType = node.elementType.toString()

        // Check if the node type indicates a block
        if (nodeType.contains("BLOCK") || nodeType.contains("Block")) {
            return true
        }

        // Check if the node contains block keywords
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == PicatTokenTypes.IF_KEYWORD || 
                child.elementType == PicatTokenTypes.FOREACH_KEYWORD || 
                child.elementType == PicatTokenTypes.FOR_KEYWORD || 
                child.elementType == PicatTokenTypes.WHILE_KEYWORD ||
                child.elementType == PicatTokenTypes.THEN_KEYWORD ||
                child.elementType == PicatTokenTypes.ELSE_KEYWORD) {
                return true
            }
            child = child.treeNext
        }

        return false
    }

    /**
     * Checks if the given node is after a rule operator (=> or ?=>) in the same parent or in an ancestor.
     * @param node The node to check
     * @return true if the node is after a rule operator, false otherwise
     */
    fun isAfterRuleOperator(node: ASTNode): Boolean {
        // Check if any sibling before this node is a rule operator
        var current = node
        while (current.treePrev != null) {
            current = current.treePrev
            if (current.elementType == PicatTokenTypes.ARROW_OP || 
                current.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                return true
            }
        }

        // Check if any ancestor contains a rule operator before this node
        var parent = node.treeParent
        var depth = 0
        while (parent != null && depth < 5) { // Increased depth to handle more complex structures
            depth++
            var child = parent.firstChildNode
            var foundRuleOp = false
            var foundNode = false
            var ruleOpPosition = -1
            var nodePosition = -1
            var position = 0

            // First pass: find positions of rule operators and the node
            while (child != null) {
                if (child.elementType == PicatTokenTypes.ARROW_OP || 
                    child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                    foundRuleOp = true
                    ruleOpPosition = position
                } 
                if (child == node || isDescendantOf(node, child)) {
                    foundNode = true
                    nodePosition = position
                }
                position++
                child = child.treeNext
            }

            // Check if the node is after a rule operator
            if (foundRuleOp && foundNode && nodePosition > ruleOpPosition) {
                return true
            }

            // Check if the parent's type indicates it's a rule body
            val parentType = parent.elementType.toString()
            if (parentType.contains("RULE_BODY") || 
                parentType.contains("PREDICATE_BODY") || 
                parentType.contains("FUNCTION_BODY")) {

                // Check if there's a rule operator in any ancestor of this parent
                var ancestor = parent.treeParent
                var ancestorDepth = 0
                while (ancestor != null && ancestorDepth < 3) {
                    ancestorDepth++
                    var ancestorChild = ancestor.firstChildNode
                    while (ancestorChild != null) {
                        if (ancestorChild.elementType == PicatTokenTypes.ARROW_OP || 
                            ancestorChild.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                            // If we find a rule operator in an ancestor, and we're in a rule body,
                            // then we're after a rule operator
                            return true
                        }
                        ancestorChild = ancestorChild.treeNext
                    }
                    ancestor = ancestor.treeParent
                }
            }

            parent = parent.treeParent
        }

        return false
    }

    /**
     * Checks if the given node is inside a block statement (if-then-else, foreach, etc.).
     * @param node The node to check
     * @return true if the node is inside a block statement, false otherwise
     */
    fun isInsideBlock(node: ASTNode): Boolean {
        // Check if any ancestor contains block keywords
        var parent = node.treeParent
        var depth = 0
        while (parent != null && depth < 15) { // Increase depth to handle deeply nested blocks
            depth++

            // Check if this parent node is a block statement
            val parentType = parent.elementType.toString()
            if (parentType.contains("BLOCK") || parentType.contains("Block")) {
                return true
            }

            // Check if this parent contains block keywords
            var child = parent.firstChildNode
            var foundBlockStart = false
            var foundBlockEnd = false
            var nodePosition = -1
            var blockStartPosition = -1
            var blockEndPosition = -1
            var position = 0

            // First pass: find all block markers and the node position
            while (child != null) {
                // Track positions of important elements
                if (child.elementType == PicatTokenTypes.IF_KEYWORD || 
                    child.elementType == PicatTokenTypes.FOREACH_KEYWORD || 
                    child.elementType == PicatTokenTypes.FOR_KEYWORD || 
                    child.elementType == PicatTokenTypes.WHILE_KEYWORD) {
                    foundBlockStart = true
                    blockStartPosition = position
                } else if (child.elementType == PicatTokenTypes.THEN_KEYWORD) {
                    // THEN is a special case - it marks the start of the block body
                    foundBlockStart = true
                    blockStartPosition = position
                } else if (child.elementType == PicatTokenTypes.ELSE_KEYWORD) {
                    // ELSE is also a special case - it marks both the end of the previous block and start of a new one
                    foundBlockEnd = false  // Reset end flag
                    foundBlockStart = true
                    blockStartPosition = position
                } else if (child.elementType == PicatTokenTypes.END_KEYWORD) {
                    foundBlockEnd = true
                    blockEndPosition = position
                } else if (child == node || isDescendantOf(node, child)) {
                    nodePosition = position
                }

                position++
                child = child.treeNext
            }

            // Check if the node is between block start and end
            if (foundBlockStart && (nodePosition > blockStartPosition) && 
                (!foundBlockEnd || nodePosition < blockEndPosition)) {
                return true
            }

            // Second pass: check for rule operators
            child = parent.firstChildNode
            var foundRuleOp = false
            var ruleOpPosition = -1
            position = 0

            while (child != null) {
                if (child.elementType == PicatTokenTypes.ARROW_OP || 
                    child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                    foundRuleOp = true
                    ruleOpPosition = position
                } else if (child == node || isDescendantOf(node, child)) {
                    nodePosition = position
                }
                position++
                child = child.treeNext
            }

            // If the node is after a rule operator, it's inside a rule body
            if (foundRuleOp && nodePosition > ruleOpPosition) {
                return true
            }

            // Special case: check if this is a block-like structure without explicit keywords
            // For example, a sequence of statements that should be indented together
            if (parentType.contains("STATEMENT") || 
                parentType.contains("BODY") || 
                parentType.contains("CLAUSE")) {
                // Check if we're inside a statement list or similar structure
                var prevSibling = parent.treePrev
                while (prevSibling != null) {
                    if (prevSibling.elementType == PicatTokenTypes.THEN_KEYWORD ||
                        prevSibling.elementType == PicatTokenTypes.ELSE_KEYWORD ||
                        prevSibling.elementType == PicatTokenTypes.ARROW_OP ||
                        prevSibling.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                        return true
                    }
                    prevSibling = prevSibling.treePrev
                }
            }

            parent = parent.treeParent
        }

        return false
    }
}
