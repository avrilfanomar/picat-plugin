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
    private val spacingBuilder: SpacingBuilder? = null
) : AbstractBlock(node, wrap, alignment) {

    init {
        // Log block creation
        println("[DEBUG_LOG] Creating PicatBlock for node: ${node.elementType}, text: '${node.text.take(20).replace("\n", "\\n")}${if (node.text.length > 20) "..." else ""}'")
    }

    override fun buildChildren(): List<Block> {
        println("[DEBUG_LOG] Building children for node: ${myNode.elementType}")
        val blocks = ArrayList<Block>()
        var child = myNode.firstChildNode
        var childCount = 0

        while (child != null) {
            childCount++
            // Log each child node
            println("[DEBUG_LOG]   Child #$childCount: ${child.elementType}, isEmpty: ${child.textRange.isEmpty}, isWhitespace: ${isWhitespaceNode(child)}")

            // Skip whitespace nodes
            if (!child.textRange.isEmpty && !isWhitespaceNode(child)) {
                val block = PicatBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    settings,
                    spacingBuilder
                )
                blocks.add(block)
                println("[DEBUG_LOG]   Added block for child #$childCount")
            } else {
                println("[DEBUG_LOG]   Skipped child #$childCount")
            }
            child = child.treeNext
        }

        println("[DEBUG_LOG] Built ${blocks.size} children blocks out of $childCount child nodes")
        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        println("[DEBUG_LOG] Getting spacing between blocks: $child1 and $child2")

        // Use spacing builder if available
        if (spacingBuilder != null) {
            val spacing = spacingBuilder.getSpacing(this, child1, child2)
            println("[DEBUG_LOG]   Using spacing builder, result: $spacing")
            if (spacing != null) {
                return spacing
            }
        }

        // Apply custom spacing rules if spacing builder didn't provide a result
        if (child1 is PicatBlock && child2 is PicatBlock) {
            val node1 = child1.node
            val node2 = child2.node

            // Check if either node is an operator
            if (isOperator(node1.elementType) || isOperator(node2.elementType)) {
                println("[DEBUG_LOG]   Applying spacing for operators")
                return Spacing.createSpacing(1, 1, 0, true, 0)
            }

            // Special handling for rule operators (=> and ?=>)
            if (node1.elementType == PicatTokenTypes.ARROW_OP || 
                node1.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP ||
                node2.elementType == PicatTokenTypes.ARROW_OP || 
                node2.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                println("[DEBUG_LOG]   Applying spacing for rule operators")
                return Spacing.createSpacing(1, 1, 0, true, 0)
            }

            // Check if the second node is a comma
            if (node2.elementType == PicatTokenTypes.COMMA) {
                println("[DEBUG_LOG]   Applying spacing after comma")
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }

            // Check if we're after a comma (add space after comma)
            if (node1.elementType == PicatTokenTypes.COMMA) {
                println("[DEBUG_LOG]   Applying spacing after comma")
                return Spacing.createSpacing(1, 1, 0, true, 0)
            }

            // Check if nodes are part of a list or array
            if (node1.elementType == PicatTokenTypes.LBRACKET || 
                node2.elementType == PicatTokenTypes.RBRACKET ||
                node1.elementType == PicatTokenTypes.COLON) {
                println("[DEBUG_LOG]   Applying spacing for list/array elements")
                return Spacing.createSpacing(1, 1, 0, true, 0)
            }
        }

        // Fallback to basic spacing rules
        println("[DEBUG_LOG]   Using fallback spacing")
        return Spacing.createSpacing(0, 1, 0, true, 0)
    }

    /**
     * Checks if the given element type is an operator.
     * @param elementType The element type to check
     * @return true if the element type is an operator, false otherwise
     */
    private fun isOperator(elementType: com.intellij.psi.tree.IElementType): Boolean {
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

    override fun getIndent(): Indent? {
        println("[DEBUG_LOG] Getting indent for node: ${myNode.elementType}")

        // Simplified indentation rules
        val parentNode = myNode.treeParent
        if (parentNode != null) {
            println("[DEBUG_LOG]   Parent node: ${parentNode.elementType}")

            val elementType = myNode.elementType
            val text = myNode.text.trim()

            // Special handling for comments
            if (elementType == PicatTokenTypes.COMMENT || text.startsWith("%")) {
                println("[DEBUG_LOG]   Comment node, using none indent")
                return Indent.getNoneIndent()
            }

            // Special handling for rule operators
            if (elementType == PicatTokenTypes.ARROW_OP || elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                println("[DEBUG_LOG]   Rule operator, using none indent")
                return Indent.getNoneIndent()
            }

            // Special handling for keywords that should not be indented
            if (elementType == PicatTokenTypes.IF_KEYWORD || 
                elementType == PicatTokenTypes.ELSE_KEYWORD || 
                elementType == PicatTokenTypes.ELSEIF_KEYWORD || 
                elementType == PicatTokenTypes.THEN_KEYWORD || 
                elementType == PicatTokenTypes.END_KEYWORD || 
                elementType == PicatTokenTypes.FOREACH_KEYWORD) {
                println("[DEBUG_LOG]   Keyword, using none indent")
                return Indent.getNoneIndent()
            }

            // Check if we're inside a rule body
            var current = parentNode
            var depth = 0
            var shouldIndent = false

            // First, check if we're after a rule operator in the current parent
            var child = parentNode.firstChildNode
            var foundRuleOp = false
            var foundBlockKeyword = false
            var myNodeFound = false

            while (child != null) {
                if (child.elementType == PicatTokenTypes.ARROW_OP || 
                    child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                    foundRuleOp = true
                } else if (child.elementType == PicatTokenTypes.THEN_KEYWORD || 
                           child.elementType == PicatTokenTypes.ELSE_KEYWORD) {
                    foundBlockKeyword = true
                } else if (child == myNode) {
                    myNodeFound = true
                    if (foundRuleOp || foundBlockKeyword) {
                        shouldIndent = true;
                    }
                }
                child = child.treeNext
            }

            if (shouldIndent) {
                println("[DEBUG_LOG]   After rule operator or block keyword in parent, using normal indent")
                return Indent.getNormalIndent()
            }

            // If not found in direct parent, check ancestors
            while (current != null && depth < 5) { // Limit depth to avoid infinite loops
                depth++

                // Check if this node or any ancestor contains a rule operator
                child = current.firstChildNode
                foundRuleOp = false
                foundBlockKeyword = false

                while (child != null) {
                    if (child.elementType == PicatTokenTypes.ARROW_OP || 
                        child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                        foundRuleOp = true
                    } else if (child.elementType == PicatTokenTypes.THEN_KEYWORD || 
                               child.elementType == PicatTokenTypes.ELSE_KEYWORD) {
                        foundBlockKeyword = true
                    } else if ((foundRuleOp || foundBlockKeyword) && isDescendantOf(myNode, child)) {
                        // We're after a rule operator or block keyword, so indent
                        println("[DEBUG_LOG]   After rule operator or block keyword in ancestor, using normal indent")
                        return Indent.getNormalIndent()
                    }
                    child = child.treeNext
                }

                current = current.treeParent
            }
        }

        println("[DEBUG_LOG]   Default case, using none indent")
        return Indent.getNoneIndent()
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
        println("[DEBUG_LOG] Getting child indent for node: ${myNode.elementType}")

        val elementType = myNode.elementType

        // Always use normal indent for file nodes
        if (elementType.toString() == "FILE") {
            println("[DEBUG_LOG]   File node, using normal indent for children")
            return Indent.getNormalIndent()
        }

        // Always indent children after rule operators
        if (elementType == PicatTokenTypes.ARROW_OP || 
            elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
            println("[DEBUG_LOG]   Rule operator, indenting children")
            return Indent.getNormalIndent()
        }

        // Always indent children after block keywords
        if (elementType == PicatTokenTypes.THEN_KEYWORD ||
            elementType == PicatTokenTypes.ELSE_KEYWORD) {
            println("[DEBUG_LOG]   Block keyword, indenting children")
            return Indent.getNormalIndent()
        }

        // Always indent children after block structure keywords
        if (elementType == PicatTokenTypes.IF_KEYWORD || 
            elementType == PicatTokenTypes.FOREACH_KEYWORD || 
            elementType == PicatTokenTypes.FOR_KEYWORD || 
            elementType == PicatTokenTypes.WHILE_KEYWORD) {
            println("[DEBUG_LOG]   Block structure keyword, indenting children")
            return Indent.getNormalIndent()
        }

        // Check if this node contains any rule operators or block keywords
        var child = myNode.firstChildNode
        var containsRuleOp = false
        var containsBlockKeyword = false

        while (child != null) {
            if (child.elementType == PicatTokenTypes.ARROW_OP || 
                child.elementType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
                containsRuleOp = true
            } else if (child.elementType == PicatTokenTypes.THEN_KEYWORD ||
                child.elementType == PicatTokenTypes.ELSE_KEYWORD ||
                child.elementType == PicatTokenTypes.IF_KEYWORD ||
                child.elementType == PicatTokenTypes.FOREACH_KEYWORD ||
                child.elementType == PicatTokenTypes.FOR_KEYWORD ||
                child.elementType == PicatTokenTypes.WHILE_KEYWORD) {
                containsBlockKeyword = true
            }
            child = child.treeNext
        }

        if (containsRuleOp || containsBlockKeyword) {
            println("[DEBUG_LOG]   Contains rule operator or block keyword, indenting children")
            return Indent.getNormalIndent()
        }

        // For predicate definitions, we want to indent the body
        if (elementType.toString() == "Picat:PREDICATE_DEFINITION") {
            println("[DEBUG_LOG]   Predicate definition, indenting children")
            return Indent.getNormalIndent()
        }

        // For rule bodies, we want to indent the statements
        if (elementType.toString() == "Picat:RULE_BODY") {
            println("[DEBUG_LOG]   Rule body, indenting children")
            return Indent.getNormalIndent()
        }

        // For block statements, we want to indent the contents
        if (elementType.toString() == "Picat:BLOCK_STATEMENT") {
            println("[DEBUG_LOG]   Block statement, indenting children")
            return Indent.getNormalIndent()
        }

        // Default to none indent to avoid over-indentation
        println("[DEBUG_LOG]   Default child indent, using none indent")
        return Indent.getNoneIndent()
    }

    /**
     * Finds the previous non-whitespace sibling of the given node.
     * @param node The node to find the previous sibling for
     * @return The previous non-whitespace sibling, or null if there is none
     */
    private fun findPreviousNonWhitespaceSibling(node: ASTNode): ASTNode? {
        var prev = node.treePrev
        while (prev != null && isWhitespaceNode(prev)) {
            prev = prev.treePrev
        }
        return prev
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }

    /**
     * Checks if the given node is a whitespace node.
     * @param node The node to check
     * @return true if the node is a whitespace node, false otherwise
     */
    private fun isWhitespaceNode(node: ASTNode): Boolean {
        val isWhitespace = node.elementType == TokenType.WHITE_SPACE
        println("[DEBUG_LOG] Checking if node is whitespace: ${node.elementType}, result: $isWhitespace")
        return isWhitespace
    }
}
