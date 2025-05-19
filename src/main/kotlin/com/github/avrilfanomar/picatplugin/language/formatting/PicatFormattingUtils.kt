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
     * Checks if an element type matches any of the provided token types.
     * @param elementType The element type to check
     * @param tokenTypes The token types to check against
     * @return true if the element type matches any of the token types, false otherwise
     */
    private fun isTokenType(elementType: IElementType, vararg tokenTypes: IElementType): Boolean {
        return tokenTypes.any { it == elementType }
    }

    /**
     * Checks if a node's element type string contains any of the provided substrings.
     * @param node The node to check
     * @param substrings The substrings to check for
     * @return true if the node's element type contains any of the substrings, false otherwise
     */
    private fun nodeTypeContains(node: ASTNode, vararg substrings: String): Boolean {
        val nodeType = node.elementType.toString()
        return substrings.any { nodeType.contains(it) }
    }

    /**
     * Checks if a node is between specific tokens in its parent.
     * @param node The node to check
     * @param startToken The token that should appear before the node
     * @param endToken The token that should appear after the node
     * @param parent The parent node to search in, defaults to node's parent
     * @return true if the node is between the specified tokens, false otherwise
     */
    private fun isNodeBetweenTokens(
        node: ASTNode, 
        startToken: IElementType, 
        endToken: IElementType, 
        parent: ASTNode = node.treeParent
    ): Boolean {
        var foundStart = false
        var foundEnd = false
        var child = parent.firstChildNode

        while (child != null) {
            if (child.elementType == startToken) {
                foundStart = true
            } else if (child.elementType == endToken) {
                foundEnd = true
            }
            child = child.treeNext
        }

        return foundStart && foundEnd
    }

    /**
     * Finds the relative positions of nodes within a parent.
     * @param parent The parent node to search in
     * @param nodeToFind The node to find the position of
     * @param positionMarkers Map of element types to track positions for
     * @return Map of element types to their positions, and the position of nodeToFind
     */
    private fun findRelativePositions(
        parent: ASTNode,
        nodeToFind: ASTNode,
        positionMarkers: Set<IElementType>
    ): Pair<Map<IElementType, Int>, Int> {
        var position = 0
        var nodePosition = -1
        val markerPositions = mutableMapOf<IElementType, Int>()

        var child = parent.firstChildNode
        while (child != null) {
            if (child == nodeToFind || isDescendantOf(nodeToFind, child)) {
                nodePosition = position
            }

            if (positionMarkers.contains(child.elementType)) {
                markerPositions[child.elementType] = position
            }

            position++
            child = child.treeNext
        }

        return Pair(markerPositions, nodePosition)
    }

    /**
     * Checks if the given element type is an operator.
     * @param elementType The element type to check
     * @return true if the element type is an operator, false otherwise
     */
    fun isOperator(elementType: IElementType): Boolean {
        // Assignment operators
        if (isTokenType(elementType, 
            PicatTokenTypes.ASSIGN_OP, 
            PicatTokenTypes.ASSIGN_ONCE)) {
            return true
        }

        // Arithmetic operators
        if (isTokenType(elementType,
            PicatTokenTypes.PLUS, 
            PicatTokenTypes.MINUS, 
            PicatTokenTypes.MULTIPLY, 
            PicatTokenTypes.DIVIDE, 
            PicatTokenTypes.INT_DIVIDE, 
            PicatTokenTypes.POWER_OP, 
            PicatTokenTypes.MODULO, 
            PicatTokenTypes.CONCAT)) {
            return true
        }

        // Relational operators
        if (isTokenType(elementType,
            PicatTokenTypes.LESS, 
            PicatTokenTypes.LESS_EQUAL, 
            PicatTokenTypes.LESS_EQUAL_ALT, 
            PicatTokenTypes.GREATER, 
            PicatTokenTypes.GREATER_EQUAL)) {
            return true
        }

        // Equality operators
        if (isTokenType(elementType,
            PicatTokenTypes.EQUAL, 
            PicatTokenTypes.NOT_EQUAL, 
            PicatTokenTypes.NOT_IDENTICAL, 
            PicatTokenTypes.IDENTICAL)) {
            return true
        }

        // Logical operators
        if (isTokenType(elementType,
            PicatTokenTypes.AND, 
            PicatTokenTypes.OR, 
            PicatTokenTypes.NOT)) {
            return true
        }

        // Constraint operators
        if (isConstraintOperator(elementType)) {
            return true
        }

        // Term comparison operators
        if (isTokenType(elementType,
            PicatTokenTypes.TERM_LT, 
            PicatTokenTypes.TERM_LE, 
            PicatTokenTypes.TERM_LE_ALT, 
            PicatTokenTypes.TERM_GT, 
            PicatTokenTypes.TERM_GE)) {
            return true
        }

        // Bitwise operators
        if (isTokenType(elementType,
            PicatTokenTypes.BITWISE_AND, 
            PicatTokenTypes.BITWISE_OR,
            PicatTokenTypes.SHIFT_LEFT, 
            PicatTokenTypes.SHIFT_RIGHT)) {
            return true
        }

        // Range operator
        return isTokenType(elementType, PicatTokenTypes.RANGE)
    }

    /**
     * Checks if the given node is a constraint operator.
     * @param elementType The element type to check
     * @return true if the element type is a constraint operator, false otherwise
     */
    fun isConstraintOperator(elementType: IElementType): Boolean {
        return isTokenType(elementType,
            PicatTokenTypes.CONSTRAINT_EQ,
            PicatTokenTypes.CONSTRAINT_NEQ,
            PicatTokenTypes.CONSTRAINT_LT,
            PicatTokenTypes.CONSTRAINT_LE,
            PicatTokenTypes.CONSTRAINT_LE_ALT,
            PicatTokenTypes.CONSTRAINT_GT,
            PicatTokenTypes.CONSTRAINT_GE,
            PicatTokenTypes.CONSTRAINT_NOT,
            PicatTokenTypes.CONSTRAINT_AND,
            PicatTokenTypes.CONSTRAINT_OR,
            PicatTokenTypes.CONSTRAINT_XOR,
            PicatTokenTypes.CONSTRAINT_IMPL,
            PicatTokenTypes.CONSTRAINT_EQUIV,
            PicatTokenTypes.TYPE_CONSTRAINT
        )
    }

    /**
     * Checks if the given node is inside a list comprehension.
     * @param node The node to check
     * @return true if the node is inside a list comprehension, false otherwise
     */
    fun isInsideListComprehension(node: ASTNode): Boolean {
        // Check if the node is inside a list (surrounded by [ and ])
        var parent = node.treeParent
        var foundLBracket = false
        var foundRBracket = false

        while (parent != null) {
            val parentType = parent.elementType.toString()

            // If the parent type indicates a list comprehension, return true
            if (parentType.contains("LIST_COMPREHENSION")) {
                return true
            }

            // Check if we're inside a list
            var child = parent.firstChildNode
            while (child != null) {
                if (child.elementType == PicatTokenTypes.LBRACKET) {
                    foundLBracket = true
                } else if (child.elementType == PicatTokenTypes.RBRACKET) {
                    foundRBracket = true
                }
                child = child.treeNext
            }

            // If we found both brackets and the node contains a colon, it's likely a list comprehension
            if (foundLBracket && foundRBracket) {
                // Check if there's a colon in the list
                child = parent.firstChildNode
                while (child != null) {
                    if (child.elementType == PicatTokenTypes.COLON) {
                        return true
                    }

                    // Also check for "in" keyword which is common in list comprehensions
                    if (child.elementType == PicatTokenTypes.IN_KEYWORD) {
                        return true
                    }

                    child = child.treeNext
                }
            }

            parent = parent.treeParent
        }

        return false
    }

    /**
     * Checks if the given node is a constraint expression or part of one.
     * @param node The node to check
     * @return true if the node is a constraint expression, false otherwise
     */
    fun isConstraintExpression(node: ASTNode): Boolean {
        // Check if the node itself is a constraint operator
        if (isConstraintOperator(node.elementType)) {
            return true
        }

        // Check if any child is a constraint operator
        var child = node.firstChildNode
        while (child != null) {
            if (isConstraintOperator(child.elementType)) {
                return true
            }
            child = child.treeNext
        }

        // Check if any parent contains a constraint operator
        var parent = node.treeParent
        while (parent != null) {
            child = parent.firstChildNode
            while (child != null) {
                if (isConstraintOperator(child.elementType)) {
                    return true
                }
                child = child.treeNext
            }
            parent = parent.treeParent
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
        // Check if the node type indicates a block
        if (nodeTypeContains(node, "BLOCK", "Block")) {
            return true
        }

        // Check if the node contains block keywords
        var child = node.firstChildNode
        while (child != null) {
            if (isTokenType(child.elementType,
                PicatTokenTypes.IF_KEYWORD,
                PicatTokenTypes.FOREACH_KEYWORD,
                PicatTokenTypes.FOR_KEYWORD,
                PicatTokenTypes.WHILE_KEYWORD,
                PicatTokenTypes.THEN_KEYWORD,
                PicatTokenTypes.ELSE_KEYWORD)) {
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
        // Define rule operators for reuse
        val ruleOperators = setOf(
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )

        // Check if any sibling before this node is a rule operator
        var current = node
        while (current.treePrev != null) {
            current = current.treePrev
            if (isTokenType(current.elementType, 
                PicatTokenTypes.ARROW_OP, 
                PicatTokenTypes.BACKTRACKABLE_ARROW_OP)) {
                return true
            }
        }

        // Check if any ancestor contains a rule operator before this node
        var parent = node.treeParent
        var depth = 0
        while (parent != null && depth < 10) { // Increased depth to handle more complex structures
            depth++

            // Find positions of rule operators and the node
            val (markerPositions, nodePosition) = findRelativePositions(parent, node, ruleOperators)

            // Check if there's a rule operator and if the node is after it
            for (entry in markerPositions) {
                if (nodePosition > entry.value) {
                    return true
                }
            }

            // Check if the parent's type indicates it's a rule body
            if (nodeTypeContains(parent, 
                "RULE_BODY", "PREDICATE_BODY", "FUNCTION_BODY", "STATEMENT_LIST", "BODY")) {

                // Check if there's a rule operator in any ancestor of this parent
                var ancestor = parent.treeParent
                var ancestorDepth = 0
                while (ancestor != null && ancestorDepth < 5) { // Increased depth
                    ancestorDepth++
                    var ancestorChild = ancestor.firstChildNode
                    while (ancestorChild != null) {
                        if (isTokenType(ancestorChild.elementType,
                            PicatTokenTypes.ARROW_OP,
                            PicatTokenTypes.BACKTRACKABLE_ARROW_OP)) {
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
        // Define block start keywords and rule operators for reuse
        val blockStartKeywords = setOf(
            PicatTokenTypes.IF_KEYWORD,
            PicatTokenTypes.FOREACH_KEYWORD,
            PicatTokenTypes.FOR_KEYWORD,
            PicatTokenTypes.WHILE_KEYWORD,
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD
        )

        val blockEndKeywords = setOf(
            PicatTokenTypes.END_KEYWORD
        )

        val ruleOperators = setOf(
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )

        val specialPrevSiblings = setOf(
            PicatTokenTypes.THEN_KEYWORD,
            PicatTokenTypes.ELSE_KEYWORD,
            PicatTokenTypes.ARROW_OP,
            PicatTokenTypes.BACKTRACKABLE_ARROW_OP
        )

        // Check if any ancestor contains block keywords
        var parent = node.treeParent
        var depth = 0
        while (parent != null && depth < 20) { // Increase depth to handle deeply nested blocks
            depth++

            // Check if this parent node is a block statement
            if (nodeTypeContains(parent, "BLOCK", "Block")) {
                return true
            }

            // Check if this parent is a statement list or body
            if (nodeTypeContains(parent, 
                "STATEMENT_LIST", "BODY", "RULE_BODY", "PREDICATE_BODY", "FUNCTION_BODY")) {
                return true
            }

            // Find positions of block markers and the node
            val positionMarkers = blockStartKeywords + blockEndKeywords
            val (markerPositions, nodePosition) = findRelativePositions(parent, node, positionMarkers)

            // Check if the node is between block start and end
            var blockStartPosition = -1
            var blockEndPosition = -1
            var foundBlockStart = false
            var foundBlockEnd = false

            for (entry in markerPositions) {
                if (blockStartKeywords.contains(entry.key)) {
                    foundBlockStart = true
                    if (entry.value > blockStartPosition) {
                        blockStartPosition = entry.value
                    }
                } else if (blockEndKeywords.contains(entry.key)) {
                    foundBlockEnd = true
                    blockEndPosition = entry.value
                }
            }

            if (foundBlockStart && (nodePosition > blockStartPosition) && 
                (!foundBlockEnd || nodePosition < blockEndPosition)) {
                return true
            }

            // Check for rule operators
            val (ruleOpPositions, nodePos) = findRelativePositions(parent, node, ruleOperators)

            // If the node is after a rule operator, it's inside a rule body
            for (entry in ruleOpPositions) {
                if (nodePos > entry.value) {
                    return true
                }
            }

            // Special case: check if this is a block-like structure without explicit keywords
            if (nodeTypeContains(parent, "STATEMENT", "BODY", "CLAUSE")) {
                // Check if we're inside a statement list or similar structure
                var prevSibling = parent.treePrev
                while (prevSibling != null) {
                    if (isTokenType(prevSibling.elementType, *specialPrevSiblings.toTypedArray())) {
                        return true
                    }
                    prevSibling = prevSibling.treePrev
                }
            }

            parent = parent.treeParent
        }

        return false
    }
    /**
     * Checks if the given node is a parameter in a function or predicate declaration.
     * @param node The node to check
     * @return true if the node is a parameter, false otherwise
     */
    fun isParameter(node: ASTNode): Boolean {
        // Check if the node type indicates it's a parameter
        if (nodeTypeContains(node, "PARAMETER", "Parameter")) {
            return true
        }

        // Check if the node is inside a parameter list
        var parent = node.treeParent
        while (parent != null) {
            if (nodeTypeContains(parent, "PARAMETER_LIST", "ParameterList")) {
                return true
            }

            // Check if we're inside a function or predicate declaration
            if (nodeTypeContains(parent, "FUNCTION_DECL", "PREDICATE_DECL") &&
                node.elementType == PicatTokenTypes.IDENTIFIER) {
                // Check if we're between parentheses
                if (isNodeBetweenTokens(node, PicatTokenTypes.LPAR, PicatTokenTypes.RPAR, parent)) {
                    return true
                }
            }

            parent = parent.treeParent
        }

        return false
    }

    /**
     * Checks if the given node is an argument in a function or predicate call.
     * @param node The node to check
     * @return true if the node is an argument, false otherwise
     */
    fun isArgument(node: ASTNode): Boolean {
        // Check if the node type indicates it's an argument
        if (nodeTypeContains(node, "ARGUMENT", "Argument")) {
            return true
        }

        // Check if the node is inside an argument list
        var parent = node.treeParent
        while (parent != null) {
            if (nodeTypeContains(parent, "ARGUMENT_LIST", "ArgumentList", "CALL", "Call")) {
                return true
            }

            // Check if we're inside a function or predicate call
            if (nodeTypeContains(parent, "FUNCTION_CALL", "PREDICATE_CALL") &&
                !isTokenType(node.elementType, PicatTokenTypes.LPAR, PicatTokenTypes.RPAR)) {
                // Check if we're between parentheses
                if (isNodeBetweenTokens(node, PicatTokenTypes.LPAR, PicatTokenTypes.RPAR, parent)) {
                    return true
                }
            }

            parent = parent.treeParent
        }

        return false
    }

    /**
     * Checks if the given node is an element in a list.
     * @param node The node to check
     * @return true if the node is a list element, false otherwise
     */
    fun isListElement(node: ASTNode): Boolean {
        // Check if the node type indicates it's a list element
        if (nodeTypeContains(node, "LIST_ELEMENT", "ListElement")) {
            return true
        }

        // Check if the node is inside a list
        var parent = node.treeParent
        while (parent != null) {
            val parentType = parent.elementType.toString()
            if (parentType.contains("LIST") && !parentType.contains("LIST_COMPREHENSION")) {
                // Make sure we're not a bracket or comma
                if (!isTokenType(node.elementType, 
                    PicatTokenTypes.LBRACKET, 
                    PicatTokenTypes.RBRACKET,
                    PicatTokenTypes.COMMA)) {
                    return true
                }
            }

            parent = parent.treeParent
        }

        return false
    }

    /**
     * Checks if the given node is part of an else-if statement.
     * @param node The node to check
     * @return true if the node is part of an else-if statement, false otherwise
     */
    fun isElseIf(node: ASTNode): Boolean {
        // Check if this node is an ELSE keyword
        if (isTokenType(node.elementType, PicatTokenTypes.ELSE_KEYWORD)) {
            // Look for an IF keyword immediately after the ELSE
            var next = node.treeNext
            while (next != null && PicatBlock.isWhitespaceNode(next)) {
                next = next.treeNext
            }

            if (next != null && isTokenType(next.elementType, PicatTokenTypes.IF_KEYWORD)) {
                return true
            }
        }

        // Check if this node is an IF keyword preceded by an ELSE
        if (isTokenType(node.elementType, PicatTokenTypes.IF_KEYWORD)) {
            var prev = node.treePrev
            while (prev != null && PicatBlock.isWhitespaceNode(prev)) {
                prev = prev.treePrev
            }

            if (prev != null && isTokenType(prev.elementType, PicatTokenTypes.ELSE_KEYWORD)) {
                return true
            }
        }

        return false
    }

    /**
     * Checks if the given node is a case statement inside a switch.
     * @param node The node to check
     * @return true if the node is a case statement, false otherwise
     */
    fun isCaseStatement(node: ASTNode): Boolean {
        // Check if the node type indicates it's a case statement
        if (nodeTypeContains(node, "CASE", "Case")) {
            return true
        }

        // In Picat, case statements are often implemented as multiple clauses
        // with pattern matching, so check for that pattern
        var parent = node.treeParent
        while (parent != null) {
            if (nodeTypeContains(parent, 
                "SWITCH", "Switch", "PATTERN_MATCHING", "PatternMatching")) {
                return true
            }

            parent = parent.treeParent
        }

        return false
    }
}
