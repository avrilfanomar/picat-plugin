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

        // Indent rule body
        if (parentType == "BODY" && picatSettings.INDENT_RULE_BODY) {
            return Indent.getNormalIndent()
        }

        // Indent block statements
        if ((parentType == "IF_THEN_ELSE" || parentType == "FOREACH_LOOP") && 
            elementType != "IF_KEYWORD" && 
            elementType != "THEN_KEYWORD" && 
            elementType != "ELSE_KEYWORD" && 
            elementType != "FOREACH_KEYWORD" && 
            elementType != "END_KEYWORD" && 
            picatSettings.INDENT_BLOCK_STATEMENTS) {
            return Indent.getNormalIndent()
        }

        // Indent list comprehension
        if (parentType == "LIST_COMPREHENSION" && 
            elementType != "LBRACKET" && 
            elementType != "RBRACKET" && 
            elementType != "PIPE" && 
            picatSettings.INDENT_LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
        }

        return Indent.getNoneIndent()
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val elementType = myNode.elementType.toString()

        // Indent rule body
        if (elementType == "BODY" && picatSettings.INDENT_RULE_BODY) {
            return Indent.getNormalIndent()
        }

        // Indent block statements
        if ((elementType == "IF_THEN_ELSE" || elementType == "FOREACH_LOOP") && 
            picatSettings.INDENT_BLOCK_STATEMENTS) {
            return Indent.getNormalIndent()
        }

        // Indent list comprehension
        if (elementType == "LIST_COMPREHENSION" && picatSettings.INDENT_LIST_COMPREHENSION) {
            return Indent.getNormalIndent()
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