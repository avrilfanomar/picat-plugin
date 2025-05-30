package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.IElementType

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
        val config = PicatBlockConfig(settings, spacingBuilder, this)
        return PicatBlock(node, wrap, alignment, config)
    }

    /**
     * Creates blocks for the given node.
     * This method is called from PicatBlock.buildChildren().
     */
    fun createBlocks(node: ASTNode): List<Block> {
        return createChildBlocks(node)
    }

    /**
     * Creates child blocks for the given parent node.
     */
    fun createChildBlocks(node: ASTNode): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = node.firstChildNode
        val nodeType = node.elementType

        // Create shared alignments and wraps for specific constructs
        val alignment = createSharedAlignment(nodeType)
        val wrap = createSharedWrap(nodeType)

        while (child != null) {
            // Always create blocks for all nodes, including whitespace
            // This ensures that the formatter can properly handle whitespace
            val childType = child.elementType

            // Determine if this child should use the shared alignment and wrap
            val childAlignment = getChildAlignment(nodeType, childType, alignment)
            val childWrap = getChildWrap(nodeType, childType, wrap)

            val childBlock = createBlock(child, childWrap, childAlignment)
            blocks.add(childBlock)

            child = child.treeNext
        }

        return blocks
    }

    /**
     * Creates shared alignment for specific node types.
     */
    private fun createSharedAlignment(nodeType: IElementType): Alignment? {
        return when (nodeType) {
            PicatTokenTypes.BODY -> Alignment.createAlignment(true)
            PicatTokenTypes.LIST,
            PicatTokenTypes.FUNCTION_CALL -> Alignment.createAlignment(true)

            else -> null
        }
    }

    /**
     * Creates shared wrap for specific node types.
     */
    private fun createSharedWrap(nodeType: IElementType): Wrap? {
        return when (nodeType) {
            PicatTokenTypes.LIST,
            PicatTokenTypes.FUNCTION_CALL -> Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true)

            else -> null
        }
    }

    /**
     * Determines if a child should use the shared alignment.
     */
    private fun getChildAlignment(
        nodeType: IElementType,
        childType: IElementType,
        alignment: Alignment?
    ): Alignment? {
        return when {
            // Align elements in lists
            isListElement(nodeType, childType) -> alignment
            // Align arguments in function calls
            isFunctionCallArgument(nodeType, childType) -> alignment
            // Align statements in rule bodies
            isRuleBodyStatement(nodeType, childType) -> alignment
            else -> null
        }
    }

    /**
     * Determines if a child should use the shared wrap.
     */
    private fun getChildWrap(
        nodeType: IElementType,
        childType: IElementType,
        wrap: Wrap?
    ): Wrap? {
        return when {
            // Wrap elements in lists
            isListElement(nodeType, childType) -> wrap
            // Wrap arguments in function calls
            isFunctionCallArgument(nodeType, childType) -> wrap
            else -> null
        }
    }

    /**
     * Checks if the node is a list element (not a bracket).
     */
    private fun isListElement(nodeType: IElementType, childType: IElementType): Boolean {
        return nodeType == PicatTokenTypes.LIST &&
                childType != PicatTokenTypes.LBRACKET &&
                childType != PicatTokenTypes.RBRACKET
    }

    /**
     * Checks if the node is a function call argument (not a parenthesis).
     */
    private fun isFunctionCallArgument(nodeType: IElementType, childType: IElementType): Boolean {
        return nodeType == PicatTokenTypes.FUNCTION_CALL &&
                childType != PicatTokenTypes.LPAR &&
                childType != PicatTokenTypes.RPAR
    }

    /**
     * Checks if the node is a statement in a rule body.
     */
    private fun isRuleBodyStatement(nodeType: IElementType, childType: IElementType): Boolean {
        return nodeType == PicatTokenTypes.BODY &&
                childType == PicatTokenTypes.STATEMENT
    }
}
