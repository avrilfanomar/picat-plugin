package com.github.avrilfanomar.picatplugin.language.formatter

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
        return blockFactory?.createChildBlocks(myNode) ?: emptyList()
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
        val parentType = myNode.treeParent?.elementType?.toString()
        val elementType = myNode.elementType.toString()
        val grandParentType = myNode.treeParent?.treeParent?.elementType?.toString()
        val greatGrandParentType = myNode.treeParent?.treeParent?.treeParent?.elementType?.toString()

        // Indent rule body with refined rules
        if ((parentType == "BODY" || parentType == "RULE") && picatSettings.INDENT_RULE_BODY) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for statements after rule operators
        if (parentType == "RULE_BODY" || 
            (parentType == "STATEMENT" && (grandParentType == "RULE_BODY" || grandParentType == "RULE")) ||
            (elementType == "STATEMENT" && parentType == "RULE") ||
            // Handle nested statements in rule bodies
            (parentType == "STATEMENT" && grandParentType == "STATEMENT" && 
             (greatGrandParentType == "RULE_BODY" || greatGrandParentType == "RULE"))) {
            return Indent.getNormalIndent()
        }

        // Improved indentation for block statements
        if ((parentType == "IF_THEN_ELSE" || parentType == "FOREACH_LOOP" || 
             parentType == "WHILE_LOOP" || parentType == "FOR_LOOP" || 
             parentType == "TRY_CATCH") && 
            // Don't indent keywords
            elementType != "IF_KEYWORD" && 
            elementType != "THEN_KEYWORD" && 
            elementType != "ELSE_KEYWORD" && 
            elementType != "ELSEIF_KEYWORD" &&
            elementType != "FOREACH_KEYWORD" && 
            elementType != "WHILE_KEYWORD" && 
            elementType != "FOR_KEYWORD" && 
            elementType != "DO_KEYWORD" &&
            elementType != "TRY_KEYWORD" &&
            elementType != "CATCH_KEYWORD" &&
            elementType != "END_KEYWORD" && 
            picatSettings.INDENT_BLOCK_STATEMENTS) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for list comprehension
        if (parentType == "LIST_COMPREHENSION" && 
            elementType != "LBRACKET" && 
            elementType != "RBRACKET" && 
            elementType != "PIPE" && 
            picatSettings.INDENT_LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
        }

        // Special indentation for function arguments
        if (parentType == "FUNCTION_CALL" && 
            elementType != "LPAR" && 
            elementType != "RPAR") {
            return Indent.getContinuationIndent()
        }

        // Special indentation for list elements
        if (parentType == "LIST" && 
            elementType != "LBRACKET" && 
            elementType != "RBRACKET") {
            return Indent.getContinuationIndent()
        }

        // Special indentation for nested expressions
        if (parentType == "EXPRESSION" && grandParentType == "EXPRESSION") {
            return Indent.getContinuationIndent()
        }

        return Indent.getNoneIndent()
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val elementType = myNode.elementType.toString()

        // Indent rule body with refined rules
        if ((elementType == "BODY" || elementType == "RULE") && picatSettings.INDENT_RULE_BODY) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for statements after rule operators
        if (elementType == "RULE_BODY" || elementType == "RULE" || elementType == "STATEMENT") {
            return Indent.getNormalIndent()
        }

        // Improved indentation for block statements
        if ((elementType == "IF_THEN_ELSE" || elementType == "FOREACH_LOOP" || 
             elementType == "WHILE_LOOP" || elementType == "FOR_LOOP" || 
             elementType == "TRY_CATCH") && 
            picatSettings.INDENT_BLOCK_STATEMENTS) {
            return Indent.getNormalIndent()
        }

        // Enhanced indentation for list comprehension
        if (elementType == "LIST_COMPREHENSION" && picatSettings.INDENT_LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
        }

        // Special indentation for function calls
        if (elementType == "FUNCTION_CALL") {
            return Indent.getContinuationIndent()
        }

        // Special indentation for lists
        if (elementType == "LIST") {
            return Indent.getContinuationIndent()
        }

        // Special indentation for expressions
        if (elementType == "EXPRESSION") {
            return Indent.getContinuationIndent()
        }

        return Indent.getNoneIndent()
    }

    /**
     * Determines if this block is a leaf block.
     */
    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
