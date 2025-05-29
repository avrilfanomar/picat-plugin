package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.formatting.Alignment
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Factory for creating PicatBlock instances.
 * This class helps to separate the block creation logic from the block implementation.
 */
class PicatBlockFactory(
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder
) {
    /**
     * Creates a PicatBlock for the given node.
     */
    fun createBlock(node: ASTNode, wrap: Wrap? = null, alignment: Alignment? = null): PicatBlock {
        return PicatBlock(node, wrap, alignment, settings, spacingBuilder, this)
    }

    /**
     * Creates child blocks for the given parent node.
     */
    fun createChildBlocks(node: ASTNode): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = node.firstChildNode
        val nodeType = node.elementType

        // Create shared alignments for specific constructs
        val alignment = when (nodeType) {
            PicatTokenTypes.RULE_BODY, 
            PicatTokenTypes.BODY -> Alignment.createAlignment(true)
            PicatTokenTypes.LIST, 
            PicatTokenTypes.FUNCTION_CALL -> Alignment.createAlignment(true)
            else -> null
        }

        // Create shared wrap for specific constructs
        val wrap = when (nodeType) {
            PicatTokenTypes.LIST, 
            PicatTokenTypes.FUNCTION_CALL -> Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true)
            else -> null
        }

        while (child != null) {
            // Always create blocks for all nodes, including whitespace
            // This ensures that the formatter can properly handle whitespace
            val childType = child.elementType

            // Determine if this child should use the shared alignment
            val childAlignment = when {
                // Align elements in lists
                nodeType == PicatTokenTypes.LIST && 
                (childType != PicatTokenTypes.LBRACKET && 
                 childType != PicatTokenTypes.RBRACKET) -> alignment
                // Align arguments in function calls
                nodeType == PicatTokenTypes.FUNCTION_CALL && 
                (childType != PicatTokenTypes.LPAR && 
                 childType != PicatTokenTypes.RPAR) -> alignment
                // Align statements in rule bodies
                (nodeType == PicatTokenTypes.RULE_BODY || 
                 nodeType == PicatTokenTypes.BODY) && 
                childType == PicatTokenTypes.STATEMENT -> alignment
                else -> null
            }

            // Determine if this child should use the shared wrap
            val childWrap = when {
                // Wrap elements in lists
                nodeType == PicatTokenTypes.LIST && 
                (childType != PicatTokenTypes.LBRACKET && 
                 childType != PicatTokenTypes.RBRACKET) -> wrap
                // Wrap arguments in function calls
                nodeType == PicatTokenTypes.FUNCTION_CALL && 
                (childType != PicatTokenTypes.LPAR && 
                 childType != PicatTokenTypes.RPAR) -> wrap
                else -> null
            }

            val childBlock = createBlock(child, childWrap, childAlignment)
            blocks.add(childBlock)

            child = child.treeNext
        }

        return blocks
    }
}
