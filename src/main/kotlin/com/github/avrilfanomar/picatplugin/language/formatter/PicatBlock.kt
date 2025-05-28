package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Wrap
import com.intellij.formatting.Alignment
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock

/**
 * Core formatter component for Picat language.
 * This class is responsible for determining indentation, spacing, and child block creation.
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
     * Builds child blocks for this block.
     */
    override fun buildChildren(): List<Block> {
        // If we have a block factory, use it to create child blocks
        if (blockFactory != null) {
            return blockFactory.createChildBlocks(myNode)
        }

        // Otherwise, create child blocks manually
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            // Create a block for each child node
            val childBlock = PicatBlock(
                child,
                null,
                null,
                settings,
                spacingBuilder,
                this.blockFactory
            )
            blocks.add(childBlock)

            child = child.treeNext
        }

        return blocks
    }

    /**
     * Determines the spacing between child blocks.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder?.getSpacing(this, child1, child2)
    }

    /**
     * Determines the indentation for this block.
     */
    override fun getIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val parentType = myNode.treeParent?.elementType
        val elementType = myNode.elementType
        val grandParentType = myNode.treeParent?.treeParent?.elementType
        val greatGrandParentType = myNode.treeParent?.treeParent?.treeParent?.elementType

        // Handle whitespace nodes
        if (elementType == PicatTokenTypes.WHITE_SPACE) {
            // If the parent is a rule or a block statement, indent the whitespace
            if (parentType == PicatTokenTypes.BODY || 
                parentType == PicatTokenTypes.RULE || 
                parentType == PicatTokenTypes.RULE_BODY ||
                parentType == PicatTokenTypes.IF_THEN_ELSE || 
                parentType == PicatTokenTypes.FOREACH_LOOP ||
                parentType == PicatTokenTypes.WHILE_LOOP || 
                parentType == PicatTokenTypes.FOR_LOOP ||
                parentType == PicatTokenTypes.TRY_CATCH ||
                // Also indent whitespace in statements within rule bodies or block statements
                (parentType == PicatTokenTypes.STATEMENT &&
                        (grandParentType == PicatTokenTypes.RULE_BODY || 
                         grandParentType == PicatTokenTypes.RULE ||
                         grandParentType == PicatTokenTypes.IF_THEN_ELSE || 
                         grandParentType == PicatTokenTypes.FOREACH_LOOP ||
                         grandParentType == PicatTokenTypes.WHILE_LOOP || 
                         grandParentType == PicatTokenTypes.FOR_LOOP ||
                         grandParentType == PicatTokenTypes.TRY_CATCH))) {
                return Indent.getNormalIndent()
            }
            return Indent.getNoneIndent()
        }

        // Handle rule body indentation
        if (elementType == PicatTokenTypes.BODY && parentType == PicatTokenTypes.RULE && picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // Handle rule body statements indentation
        if (elementType == PicatTokenTypes.STATEMENT && 
            (parentType == PicatTokenTypes.BODY || parentType == PicatTokenTypes.RULE_BODY) && 
            picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // Handle block statement indentation
        if ((elementType == PicatTokenTypes.STATEMENT || elementType == PicatTokenTypes.EXPRESSION) && 
            (parentType == PicatTokenTypes.IF_THEN_ELSE || 
             parentType == PicatTokenTypes.FOREACH_LOOP ||
             parentType == PicatTokenTypes.WHILE_LOOP || 
             parentType == PicatTokenTypes.FOR_LOOP ||
             parentType == PicatTokenTypes.TRY_CATCH) && 
            picatSettings.indentBlockStatements) {
            return Indent.getNormalIndent()
        }

        // Handle list comprehension indentation
        if ((elementType == PicatTokenTypes.STATEMENT || elementType == PicatTokenTypes.EXPRESSION) && 
            parentType == PicatTokenTypes.LIST_COMPREHENSION && 
            picatSettings.indentListComprehension) {
            return Indent.getNormalIndent()
        }

        // Handle indentation for expressions in parentheses
        if (elementType == PicatTokenTypes.EXPRESSION && parentType == PicatTokenTypes.PARENTHESIZED_EXPRESSION) {
            return Indent.getNormalIndent()
        }

        // Handle indentation for arguments in function calls
        if (elementType == PicatTokenTypes.ARGUMENT && parentType == PicatTokenTypes.ARGUMENT_LIST) {
            return Indent.getContinuationIndent()
        }

        // Handle indentation for elements in lists
        if (elementType == PicatTokenTypes.EXPRESSION && parentType == PicatTokenTypes.LIST_ELEMENTS) {
            return Indent.getContinuationIndent()
        }

        // Handle indentation for elements in structures
        if (elementType == PicatTokenTypes.ARGUMENT && parentType == PicatTokenTypes.STRUCTURE) {
            return Indent.getContinuationIndent()
        }

        // Default: no indentation
        return Indent.getNoneIndent()
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val elementType = myNode.elementType

        // For rule bodies, indent child blocks
        if (elementType == PicatTokenTypes.RULE && picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // For rule bodies, indent child blocks
        if ((elementType == PicatTokenTypes.BODY || elementType == PicatTokenTypes.RULE_BODY) && 
            picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // For block statements, indent child blocks
        if ((elementType == PicatTokenTypes.IF_THEN_ELSE || 
             elementType == PicatTokenTypes.FOREACH_LOOP ||
             elementType == PicatTokenTypes.WHILE_LOOP || 
             elementType == PicatTokenTypes.FOR_LOOP ||
             elementType == PicatTokenTypes.TRY_CATCH) && 
            picatSettings.indentBlockStatements) {
            return Indent.getNormalIndent()
        }

        // For list comprehensions, indent child blocks
        if (elementType == PicatTokenTypes.LIST_COMPREHENSION && picatSettings.indentListComprehension) {
            return Indent.getNormalIndent()
        }

        // For parenthesized expressions, indent child blocks
        if (elementType == PicatTokenTypes.PARENTHESIZED_EXPRESSION) {
            return Indent.getNormalIndent()
        }

        // For argument lists, indent child blocks
        if (elementType == PicatTokenTypes.ARGUMENT_LIST) {
            return Indent.getContinuationIndent()
        }

        // For list elements, indent child blocks
        if (elementType == PicatTokenTypes.LIST_ELEMENTS) {
            return Indent.getContinuationIndent()
        }

        // Default: no indentation for child blocks
        return Indent.getNoneIndent()
    }

    /**
     * Determines if this block is a leaf block (has no children).
     */
    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
