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

        // Get custom settings
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Otherwise, create child blocks directly
        val blocks = ArrayList<Block>()
        var child = myNode.firstChildNode

        // Create alignments based on settings
        val parameterAlignment = if (picatSettings.ALIGN_MULTILINE_PARAMETERS) Alignment.createAlignment() else null
        val argumentAlignment = if (picatSettings.ALIGN_MULTILINE_ARGUMENTS) Alignment.createAlignment() else null
        val assignmentAlignment = if (picatSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS) Alignment.createAlignment() else null
        val listElementAlignment = if (picatSettings.ALIGN_LIST_ELEMENTS) Alignment.createAlignment() else null

        while (child != null) {
            // Skip whitespace nodes and empty nodes
            if (!child.textRange.isEmpty && !isWhitespaceNode(child)) {
                // Determine the appropriate alignment for this node
                val alignment = when {
                    // Align parameters in function/predicate declarations
                    PicatFormattingUtils.isParameter(child) -> parameterAlignment
                    // Align arguments in function/predicate calls
                    PicatFormattingUtils.isArgument(child) -> argumentAlignment
                    // Align consecutive assignments
                    child.elementType == PicatTokenTypes.ASSIGN_OP || 
                    child.elementType == PicatTokenTypes.ASSIGN_ONCE -> assignmentAlignment
                    // Align list elements
                    PicatFormattingUtils.isListElement(child) -> listElementAlignment
                    // Default: no alignment
                    else -> null
                }

                // Determine wrap type based on settings
                val wrapType = when {
                    // Wrap long lines if enabled
                    picatSettings.WRAP_LONG_LINES && child.textLength > picatSettings.MAX_LINE_LENGTH -> WrapType.NORMAL
                    // Wrap parameters if enabled
                    picatSettings.PREFER_PARAMETERS_WRAP && PicatFormattingUtils.isParameter(child) -> WrapType.NORMAL
                    // Default: no wrapping
                    else -> WrapType.NONE
                }

                val block = PicatBlock(
                    child,
                    Wrap.createWrap(wrapType, false),
                    alignment,
                    settings,
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks
    }

    /**
     * Determines if this node should indent its children.
     * @return true if this node should indent its children, false otherwise
     */
    private fun shouldIndentChildren(): Boolean {
        val elementType = myNode.elementType
        val elementTypeStr = elementType.toString()

        // Get custom settings
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Always indent children after rule operators
        if (elementType == PicatTokenTypes.ARROW_OP || 
            elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
            return true
        }

        // Special handling for else-if statements
        if (elementType == PicatTokenTypes.ELSE_KEYWORD) {
            // Check if this is an else-if statement
            if (PicatFormattingUtils.isElseIf(myNode)) {
                // If special else-if treatment is enabled, don't indent
                return !picatSettings.SPECIAL_ELSE_IF_TREATMENT
            }
            return true
        }

        // Always indent children after block keywords
        if (elementType == PicatTokenTypes.THEN_KEYWORD) {
            return true
        }

        // Always indent children after block structure keywords
        if (elementType == PicatTokenTypes.IF_KEYWORD || 
            elementType == PicatTokenTypes.FOREACH_KEYWORD || 
            elementType == PicatTokenTypes.FOR_KEYWORD || 
            elementType == PicatTokenTypes.WHILE_KEYWORD ||
            elementType == PicatTokenTypes.DO_KEYWORD ||
            elementType == PicatTokenTypes.REPEAT_KEYWORD ||
            elementType == PicatTokenTypes.TRY_KEYWORD ||
            elementType == PicatTokenTypes.CATCH_KEYWORD) {
            return true
        }

        // Check if this node is a block node
        if (isBlockNode(myNode)) {
            return true
        }

        // Special handling for rule bodies
        if (elementTypeStr.contains("RULE_BODY") ||
            elementTypeStr.contains("PREDICATE_BODY") ||
            elementTypeStr.contains("FUNCTION_BODY") ||
            elementTypeStr.contains("CLAUSE_BODY") ||
            elementTypeStr.contains("STATEMENT_LIST") ||
            elementTypeStr.contains("BODY")) {
            return true
        }

        // If this node contains a rule operator, indent its children
        if (containsRuleOperator(myNode)) {
            return true
        }

        // If this node contains a block keyword, indent its children
        if (containsBlockKeyword(myNode)) {
            return true
        }

        // If this node is a block statement or contains a block statement, indent its children
        if (elementTypeStr.contains("BLOCK") || elementTypeStr.contains("Block")) {
            return true
        }

        // Special handling for case statements
        if (PicatFormattingUtils.isCaseStatement(myNode)) {
            // If the indent case from switch is enabled, indent
            return picatSettings.INDENT_CASE_FROM_SWITCH
        }

        // Special handling for list comprehensions
        if (elementTypeStr.contains("LIST_COMPREHENSION") || 
            (elementType == PicatTokenTypes.COLON && PicatFormattingUtils.isInsideListComprehension(myNode))) {
            // If special list comprehension formatting is enabled, indent
            return picatSettings.SPECIAL_LIST_COMPREHENSION_FORMATTING
        }

        // Special handling for constraint expressions
        if (elementType == PicatTokenTypes.TYPE_CONSTRAINT ||
            elementTypeStr.contains("CONSTRAINT") ||
            PicatFormattingUtils.isConstraintExpression(myNode)) {
            return true
        }

        return false
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // If no child blocks, return default spacing
        if (child1 == null || child2 == null) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        // Get custom settings
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

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
                return Spacing.createSpacing(0, 0, 1, true, picatSettings.INDENT_SIZE)
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
                return Spacing.createSpacing(0, 0, 1, true, picatSettings.INDENT_SIZE)
            }

            // Handle spacing after end keyword
            if (child1Type == PicatTokenTypes.END_KEYWORD) {
                if (child2Type == PicatTokenTypes.COMMA || child2Type == PicatTokenTypes.DOT) {
                    return Spacing.createSpacing(0, 0, 0, false, 0)
                }
                return Spacing.createSpacing(0, 0, 1, true, picatSettings.INDENT_SIZE)
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
                return Spacing.createSpacing(0, 0, 1, true, picatSettings.INDENT_SIZE)
            }

            // Handle spacing after dot
            if (child1Type == PicatTokenTypes.DOT) {
                // Line break after dot
                return Spacing.createSpacing(0, 0, 1, true, picatSettings.INDENT_SIZE)
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

        // Default spacing - use settings for line breaks and blank lines
        return Spacing.createSpacing(0, 0, 0, picatSettings.KEEP_LINE_BREAKS, picatSettings.KEEP_BLANK_LINES_IN_CODE)
    }

    override fun getIndent(): Indent? {
        // Always use normal indent for all blocks
        return Indent.getIndent(Indent.Type.NORMAL, false, false)
    }

    /**
     * Checks if node1 is a descendant of node2.
     * @param node1 The potential descendant
     * @param node2 The potential ancestor
     * @return true if node1 is a descendant of node2, false otherwise
     */
    private fun isDescendantOf(node1: ASTNode, node2: ASTNode): Boolean {
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
     * Determine the indentation for child blocks.
     * This method is called when the formatter needs to know how to indent
     * children of the current block.
     */
    override fun getChildIndent(): Indent? {
        // Get custom settings
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        // Use the custom indent size for indentation
        return Indent.getIndent(Indent.Type.NORMAL, false, false)
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
