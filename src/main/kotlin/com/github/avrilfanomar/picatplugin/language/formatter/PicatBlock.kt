package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.*
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
            if (parentType == PicatTokenTypes.BODY || parentType == PicatTokenTypes.RULE || parentType == PicatTokenTypes.RULE_BODY ||
                parentType == PicatTokenTypes.IF_THEN_ELSE || parentType == PicatTokenTypes.FOREACH_LOOP ||
                parentType == PicatTokenTypes.WHILE_LOOP || parentType == PicatTokenTypes.FOR_LOOP ||
                parentType == PicatTokenTypes.TRY_CATCH ||
                // Also indent whitespace in statements within rule bodies or block statements
                (parentType == PicatTokenTypes.STATEMENT && 
                    (grandParentType == PicatTokenTypes.RULE_BODY || grandParentType == PicatTokenTypes.RULE ||
                     grandParentType == PicatTokenTypes.IF_THEN_ELSE || grandParentType == PicatTokenTypes.FOREACH_LOOP ||
                     grandParentType == PicatTokenTypes.WHILE_LOOP || grandParentType == PicatTokenTypes.FOR_LOOP ||
                     grandParentType == PicatTokenTypes.TRY_CATCH)) ||
                // Also indent whitespace in comments within rule bodies or block statements
                (parentType == PicatTokenTypes.COMMENT && 
                    (grandParentType == PicatTokenTypes.RULE_BODY || grandParentType == PicatTokenTypes.RULE ||
                     grandParentType == PicatTokenTypes.IF_THEN_ELSE || grandParentType == PicatTokenTypes.FOREACH_LOOP ||
                     grandParentType == PicatTokenTypes.WHILE_LOOP || grandParentType == PicatTokenTypes.FOR_LOOP ||
                     grandParentType == PicatTokenTypes.TRY_CATCH)) ||
                // Also indent whitespace in expressions within statements
                (parentType == PicatTokenTypes.EXPRESSION && 
                    (grandParentType == PicatTokenTypes.STATEMENT || grandParentType == PicatTokenTypes.RULE_BODY))
            ) {
                return Indent.getNormalIndent()
            }
            return Indent.getNoneIndent()
        }

        // Indent rule body with refined rules
        if ((parentType == PicatTokenTypes.BODY || parentType == PicatTokenTypes.RULE) && picatSettings.INDENT_RULE_BODY) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for statements after rule operators
        if (parentType == PicatTokenTypes.RULE_BODY ||
            (parentType == PicatTokenTypes.STATEMENT && (grandParentType == PicatTokenTypes.RULE_BODY || grandParentType == PicatTokenTypes.RULE)) ||
            (elementType == PicatTokenTypes.STATEMENT && parentType == PicatTokenTypes.RULE) ||
            // Handle nested statements in rule bodies
            (parentType == PicatTokenTypes.STATEMENT && grandParentType == PicatTokenTypes.STATEMENT &&
                    (greatGrandParentType == PicatTokenTypes.RULE_BODY || greatGrandParentType == PicatTokenTypes.RULE))
        ) {
            return Indent.getNormalIndent()
        }

        // Improved indentation for block statements
        if ((parentType == PicatTokenTypes.IF_THEN_ELSE || parentType == PicatTokenTypes.FOREACH_LOOP ||
                    parentType == PicatTokenTypes.WHILE_LOOP || parentType == PicatTokenTypes.FOR_LOOP ||
                    parentType == PicatTokenTypes.TRY_CATCH) &&
            // Don't indent keywords
            elementType != PicatTokenTypes.IF_KEYWORD &&
            elementType != PicatTokenTypes.THEN_KEYWORD &&
            elementType != PicatTokenTypes.ELSE_KEYWORD &&
            elementType != PicatTokenTypes.ELSEIF_KEYWORD &&
            elementType != PicatTokenTypes.FOREACH_KEYWORD &&
            elementType != PicatTokenTypes.WHILE_KEYWORD &&
            elementType != PicatTokenTypes.FOR_KEYWORD &&
            elementType != PicatTokenTypes.DO_KEYWORD &&
            elementType != PicatTokenTypes.TRY_KEYWORD &&
            elementType != PicatTokenTypes.CATCH_KEYWORD &&
            elementType != PicatTokenTypes.END_KEYWORD &&
            picatSettings.INDENT_BLOCK_STATEMENTS) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for list comprehension
        if (parentType == PicatTokenTypes.LIST_COMPREHENSION &&
            elementType != PicatTokenTypes.LBRACKET &&
            elementType != PicatTokenTypes.RBRACKET &&
            elementType != PicatTokenTypes.PIPE &&
            picatSettings.INDENT_LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
        }

        // Special indentation for function arguments
        if (parentType == PicatTokenTypes.FUNCTION_CALL &&
            elementType != PicatTokenTypes.LPAR &&
            elementType != PicatTokenTypes.RPAR
        ) {
            return Indent.getContinuationIndent()
        }

        // Special indentation for list elements
        if (parentType == PicatTokenTypes.LIST &&
            elementType != PicatTokenTypes.LBRACKET &&
            elementType != PicatTokenTypes.RBRACKET
        ) {
            return Indent.getContinuationIndent()
        }

        // Special indentation for nested expressions
        if (parentType == PicatTokenTypes.EXPRESSION && grandParentType == PicatTokenTypes.EXPRESSION) {
            return Indent.getContinuationIndent()
        }

        return Indent.getNoneIndent()
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Always indent rule bodies and statements
        if (elementType == PicatTokenTypes.BODY || elementType == PicatTokenTypes.RULE || elementType == PicatTokenTypes.RULE_BODY ||
            elementType == PicatTokenTypes.STATEMENT || elementType == PicatTokenTypes.PROGRAM
        ) {
            return Indent.getNormalIndent()
        }

        // Improved indentation for block statements
        if (elementType == PicatTokenTypes.IF_THEN_ELSE || elementType == PicatTokenTypes.FOREACH_LOOP ||
            elementType == PicatTokenTypes.WHILE_LOOP || elementType == PicatTokenTypes.FOR_LOOP ||
            elementType == PicatTokenTypes.TRY_CATCH
        ) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for list comprehension
        if (elementType == PicatTokenTypes.LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
        }

        // Special indentation for function calls
        if (elementType == PicatTokenTypes.FUNCTION_CALL) {
            return Indent.getContinuationIndent()
        }

        // Special indentation for lists
        if (elementType == PicatTokenTypes.LIST) {
            return Indent.getContinuationIndent()
        }

        // Special indentation for expressions
        if (elementType == PicatTokenTypes.EXPRESSION) {
            return Indent.getContinuationIndent()
        }

        // Default to normal indent for all other elements
        return Indent.getNormalIndent()
    }

    /**
     * Determines if this block is a leaf block.
     */
    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
