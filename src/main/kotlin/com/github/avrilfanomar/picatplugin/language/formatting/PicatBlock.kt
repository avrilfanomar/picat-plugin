package com.github.avrilfanomar.picatplugin.language.formatting

import com.github.avrilfanomar.picatplugin.language.PicatTokenTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock

/**
 * Block implementation for Picat language formatting.
 * This class defines how code blocks should be formatted in the Picat language.
 */
class PicatBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder? = null,
    private val blockFactory: PicatBlockFactory? = null
) : AbstractBlock(node, wrap, alignment) {

    /**
     * Builds the child blocks for this block.
     * If a block factory is provided, it will be used to create the child blocks.
     * Otherwise, child blocks will be created directly.
     */
    override fun buildChildren(): List<Block> {
        // If we have a block factory, use it to create child blocks
        if (blockFactory != null) {
            return blockFactory.createChildBlocks(myNode)
        }

        // Otherwise, create child blocks directly
        val blocks = ArrayList<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            // Skip whitespace nodes and empty nodes
            if (!child.textRange.isEmpty && !isWhitespaceNode(child)) {
                val block = PicatBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    settings,
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // If no child blocks, return default spacing
        if (child1 == null || child2 == null) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        // Apply custom spacing rules
        if (child1 is PicatBlock && child2 is PicatBlock) {
            val child1Type = child1.node.elementType
            val child2Type = child2.node.elementType

            // Add a line break after rule operators (=> and ?=>)
            if (child1Type == PicatTokenTypes.ARROW_OP || child1Type == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                // No line break if the next element is a comment
                if (child2Type == PicatTokenTypes.COMMENT) {
                    return Spacing.createSpacing(1, 1, 0, false, 0)
                }
                // Force a line break with indentation
                return Spacing.createSpacing(0, 0, 1, true, 1)
            }

            // Add space around operators
            if (PicatFormattingUtils.isOperator(child1Type) || PicatFormattingUtils.isOperator(child2Type)) {
                return Spacing.createSpacing(1, 1, 0, false, 0)
            }

            // Handle comma spacing
            if (child1Type == PicatTokenTypes.COMMA) {
                // Space after comma
                return Spacing.createSpacing(1, 1, 0, false, 0)
            }
            if (child2Type == PicatTokenTypes.COMMA) {
                // No space before comma
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Space around colon in list comprehensions
            if (child1Type == PicatTokenTypes.COLON || child2Type == PicatTokenTypes.COLON) {
                return Spacing.createSpacing(1, 1, 0, false, 0)
            }

            // Handle spacing around block keywords
            if (child1Type == PicatTokenTypes.THEN_KEYWORD ||
                child1Type == PicatTokenTypes.ELSE_KEYWORD
            ) {
                return Spacing.createSpacing(0, 0, 1, true, 1)
            }

            // Handle spacing after end keyword
            if (child1Type == PicatTokenTypes.END_KEYWORD) {
                if (child2Type == PicatTokenTypes.COMMA || child2Type == PicatTokenTypes.DOT) {
                    return Spacing.createSpacing(0, 0, 0, false, 0)
                }
                return Spacing.createSpacing(0, 0, 1, true, 0)
            }

            // Handle spacing after block structure keywords
            if (child1Type == PicatTokenTypes.IF_KEYWORD ||
                child1Type == PicatTokenTypes.FOREACH_KEYWORD ||
                child1Type == PicatTokenTypes.FOR_KEYWORD ||
                child1Type == PicatTokenTypes.WHILE_KEYWORD
            ) {
                // Space after these keywords
                return Spacing.createSpacing(1, 1, 0, false, 0)
            }

            // Handle spacing around parentheses
            if (child1Type == PicatTokenTypes.LPAR) {
                // No space after left parenthesis
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
            if (child2Type == PicatTokenTypes.RPAR) {
                // No space before right parenthesis
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Handle spacing around brackets
            if (child1Type == PicatTokenTypes.LBRACKET) {
                // No space after left bracket
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
            if (child2Type == PicatTokenTypes.RBRACKET) {
                // No space before right bracket
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Handle spacing around braces
            if (child1Type == PicatTokenTypes.LBRACE) {
                // No space after left brace
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
            if (child2Type == PicatTokenTypes.RBRACE) {
                // No space before right brace
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Handle spacing after comments
            if (child1Type == PicatTokenTypes.COMMENT) {
                // Line break after comment
                return Spacing.createSpacing(0, 0, 1, true, 0)
            }

            // Handle spacing after dot
            if (child1Type == PicatTokenTypes.DOT) {
                // Line break after dot
                return Spacing.createSpacing(0, 0, 1, true, 0)
            }

            // Handle spacing around function calls
            if (child1Type == PicatTokenTypes.IDENTIFIER && child2Type == PicatTokenTypes.LPAR) {
                // No space between function name and opening parenthesis
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Handle spacing around range operator
            if (child1Type == PicatTokenTypes.RANGE || child2Type == PicatTokenTypes.RANGE) {
                // No space around range operator
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        // Use spacing builder as fallback
        if (spacingBuilder != null) {
            val spacing = spacingBuilder.getSpacing(this, child1, child2)
            if (spacing != null) {
                return spacing
            }
        }

        // Default spacing - no space
        return Spacing.createSpacing(0, 0, 0, false, 0)
    }

    /**
     * Checks if the given element type is an operator.
     * Delegates to PicatFormattingUtils.isOperator for consistency.
     *
     * @param elementType The element type to check
     * @return true if the element type is an operator, false otherwise
     */
    private fun isOperator(elementType: com.intellij.psi.tree.IElementType): Boolean {
        return PicatFormattingUtils.isOperator(elementType)
    }

    override fun getIndent(): Indent? {
        // Get parent node
        val parentNode = myNode.treeParent
        if (parentNode == null) {
            return Indent.getNoneIndent()
        }

        val elementType = myNode.elementType
        val elementTypeStr = elementType.toString()
        val text = myNode.text.trim()
        val parentTypeStr = parentNode.elementType.toString()

        // Special handling for comments
        if (elementType == PicatTokenTypes.COMMENT || text.startsWith("%")) {
            // If the comment is at the beginning of a line after a rule operator, indent it
            if (isAfterRuleOperator(myNode)) {
                return Indent.getNormalIndent()
            }

            // If the comment is inside a block, indent it
            if (isInsideBlock(myNode)) {
                return Indent.getNormalIndent()
            }

            // If the comment is on the same line as indented code, use continuation indent
            val prevSibling = findPreviousNonWhitespaceSibling(myNode)
            if (prevSibling != null && (isAfterRuleOperator(prevSibling) || isInsideBlock(prevSibling))) {
                return Indent.getContinuationIndent()
            }

            return Indent.getNoneIndent()
        }

        // No indentation for rule operators
        if (elementType == PicatTokenTypes.ARROW_OP || elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
            return Indent.getNoneIndent()
        }

        // No indentation for block keywords
        if (elementType == PicatTokenTypes.IF_KEYWORD ||
            elementType == PicatTokenTypes.ELSE_KEYWORD ||
            elementType == PicatTokenTypes.ELSEIF_KEYWORD ||
            elementType == PicatTokenTypes.THEN_KEYWORD ||
            elementType == PicatTokenTypes.END_KEYWORD ||
            elementType == PicatTokenTypes.FOREACH_KEYWORD ||
            elementType == PicatTokenTypes.FOR_KEYWORD ||
            elementType == PicatTokenTypes.WHILE_KEYWORD
        ) {
            return Indent.getNoneIndent()
        }

        // Check if we're inside a rule body
        var isInRuleBody = false
        var parent = parentNode
        while (parent != null) {
            var child = parent.firstChildNode
            while (child != null) {
                if (child.elementType == PicatTokenTypes.ARROW_OP || 
                    child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                    isInRuleBody = true
                    break
                }
                child = child.treeNext
            }
            if (isInRuleBody) break
            parent = parent.treeParent
        }

        // Always indent code after rule operators
        if (isAfterRuleOperator(myNode)) {
            return Indent.getNormalIndent()
        }

        // Always indent code inside blocks
        if (isInsideBlock(myNode)) {
            return Indent.getNormalIndent()
        }

        // If we're in a rule body but not directly after the rule operator,
        // we still need to indent
        if (isInRuleBody) {
            return Indent.getNormalIndent()
        }

        // Indent code inside predicate and function bodies
        if (parentTypeStr.contains("PREDICATE_BODY") ||
            parentTypeStr.contains("FUNCTION_BODY") ||
            parentTypeStr.contains("CLAUSE_LIST")
        ) {
            return Indent.getNormalIndent()
        }

        // Indent clauses in clause lists
        if (elementTypeStr.contains("CLAUSE") && parentTypeStr.contains("CLAUSE_LIST")) {
            return Indent.getNormalIndent()
        }

        // Default to no indent
        return Indent.getNoneIndent()
    }

    /**
     * Checks if node1 is a descendant of node2.
     * Delegates to PicatFormattingUtils.isDescendantOf for consistency.
     *
     * @param node1 The potential descendant
     * @param node2 The potential ancestor
     * @return true if node1 is a descendant of node2, false otherwise
     */
    private fun isDescendantOf(node1: ASTNode, node2: ASTNode): Boolean {
        return PicatFormattingUtils.isDescendantOf(node1, node2)
    }

    /**
     * Determine the indentation for child blocks.
     * This method is called when the formatter needs to know how to indent
     * children of the current block.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType
        val elementTypeStr = elementType.toString()

        // Always use normal indent for file nodes
        if (elementTypeStr == "FILE") {
            return Indent.getNormalIndent()
        }

        // Always indent children after rule operators
        if (elementType == PicatTokenTypes.ARROW_OP || 
            elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
            return Indent.getNormalIndent()
        }

        // Always indent children after block keywords
        if (elementType == PicatTokenTypes.THEN_KEYWORD ||
            elementType == PicatTokenTypes.ELSE_KEYWORD) {
            return Indent.getNormalIndent()
        }

        // Always indent children after block structure keywords
        if (elementType == PicatTokenTypes.IF_KEYWORD || 
            elementType == PicatTokenTypes.FOREACH_KEYWORD || 
            elementType == PicatTokenTypes.FOR_KEYWORD || 
            elementType == PicatTokenTypes.WHILE_KEYWORD) {
            return Indent.getNormalIndent()
        }

        // Check if this node is a block node
        if (isBlockNode(myNode)) {
            return Indent.getNormalIndent()
        }

        // Special handling for rule bodies
        if (elementTypeStr.contains("RULE_BODY") ||
            elementTypeStr.contains("PREDICATE_BODY") ||
            elementTypeStr.contains("FUNCTION_BODY") ||
            elementTypeStr.contains("CLAUSE_BODY")
        ) {
            return Indent.getNormalIndent()
        }

        // Special handling for statements inside blocks
        if (elementTypeStr.contains("STATEMENT") &&
            (myNode.treeParent != null &&
                    (myNode.treeParent.elementType.toString().contains("BLOCK") ||
                            myNode.treeParent.elementType.toString().contains("BODY")))
        ) {
            return Indent.getNormalIndent()
        }

        // If this node contains a rule operator, indent its children
        if (containsRuleOperator(myNode)) {
            return Indent.getNormalIndent()
        }

        // If this node contains a block keyword, indent its children
        if (containsBlockKeyword(myNode)) {
            return Indent.getNormalIndent()
        }

        // If this node is a block statement or contains a block statement, indent its children
        if (elementTypeStr.contains("BLOCK") || elementTypeStr.contains("Block")) {
            return Indent.getNormalIndent()
        }

        // PSI-aware indentation rules - simplify by checking for common patterns
        if (elementTypeStr.contains("DEFINITION") ||
            elementTypeStr.contains("BODY") ||
            elementTypeStr.contains("CLAUSE_LIST") ||
            elementTypeStr.contains("RULE_BODY") ||
            elementTypeStr.contains("BLOCK_STATEMENT")
        ) {
            return Indent.getNormalIndent()
        }

        // If we're inside a rule body, indent children
        if (isInRuleBody(myNode)) {
            return Indent.getNormalIndent()
        }

        // Check if we're inside a block
        if (isInsideBlock(myNode)) {
            return Indent.getNormalIndent()
        }

        // Check if we're after a rule operator
        if (isAfterRuleOperator(myNode)) {
            return Indent.getNormalIndent()
        }

        // Default to normal indent for most cases to ensure consistent indentation
        return Indent.getNormalIndent()
    }

    /**
     * Checks if the node contains a rule operator.
     * @param node The node to check
     * @return true if the node contains a rule operator, false otherwise
     */
    private fun containsRuleOperator(node: ASTNode): Boolean {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == PicatTokenTypes.ARROW_OP || 
                child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                return true
            }
            child = child.treeNext
        }
        return false
    }

    /**
     * Checks if the node contains a block keyword.
     * @param node The node to check
     * @return true if the node contains a block keyword, false otherwise
     */
    private fun containsBlockKeyword(node: ASTNode): Boolean {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == PicatTokenTypes.THEN_KEYWORD ||
                child.elementType == PicatTokenTypes.ELSE_KEYWORD ||
                child.elementType == PicatTokenTypes.IF_KEYWORD ||
                child.elementType == PicatTokenTypes.FOREACH_KEYWORD ||
                child.elementType == PicatTokenTypes.FOR_KEYWORD ||
                child.elementType == PicatTokenTypes.WHILE_KEYWORD) {
                return true
            }
            child = child.treeNext
        }
        return false
    }

    /**
     * Checks if the node is inside a rule body.
     * @param node The node to check
     * @return true if the node is inside a rule body, false otherwise
     */
    private fun isInRuleBody(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            var childNode = parent.firstChildNode
            while (childNode != null) {
                if (childNode.elementType == PicatTokenTypes.ARROW_OP ||
                    childNode.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP
                ) {
                    return true
                }
                childNode = childNode.treeNext
            }
            parent = parent.treeParent
        }
        return false
    }

    /**
     * Finds the previous non-whitespace sibling of the given node.
     * Delegates to PicatFormattingUtils.findPreviousNonWhitespaceSibling for consistency.
     *
     * @param node The node to find the previous sibling for
     * @return The previous non-whitespace sibling, or null if there is none
     */
    private fun findPreviousNonWhitespaceSibling(node: ASTNode): ASTNode? {
        return PicatFormattingUtils.findPreviousNonWhitespaceSibling(node)
    }

    /**
     * Checks if the given node is after a rule operator (=> or ?=>) in the same parent or in an ancestor.
     * Delegates to PicatFormattingUtils.isAfterRuleOperator for consistency.
     *
     * @param node The node to check
     * @return true if the node is after a rule operator, false otherwise
     */
    private fun isAfterRuleOperator(node: ASTNode): Boolean {
        return PicatFormattingUtils.isAfterRuleOperator(node)
    }

    /**
     * Checks if the given node is a block node (if-then-else, foreach, etc.).
     * Delegates to PicatFormattingUtils.isBlockNode for consistency.
     *
     * @param node The node to check
     * @return true if the node is a block node, false otherwise
     */
    private fun isBlockNode(node: ASTNode): Boolean {
        return PicatFormattingUtils.isBlockNode(node)
    }

    /**
     * Checks if the given node is inside a block statement (if-then-else, foreach, etc.).
     * Delegates to PicatFormattingUtils.isInsideBlock for consistency.
     *
     * @param node The node to check
     * @return true if the node is inside a block statement, false otherwise
     */
    private fun isInsideBlock(node: ASTNode): Boolean {
        return PicatFormattingUtils.isInsideBlock(node)
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }

    companion object {
        /**
         * Checks if the given node is a whitespace node.
         * @param node The node to check
         * @return true if the node is a whitespace node, false otherwise
         */
        fun isWhitespaceNode(node: ASTNode): Boolean {
            return node.elementType == TokenType.WHITE_SPACE
        }
    }
}
