package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Factory class for creating PicatBlock instances.
 * This class is responsible for creating blocks based on AST nodes,
 * which helps to separate the block creation logic from the block implementation.
 */
class PicatBlockFactory(
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder
) {
    /**
     * Creates a PicatBlock for the given node.
     *
     * @param node The AST node to create a block for
     * @param wrap The wrap settings for the block
     * @param alignment The alignment settings for the block
     * @return A new PicatBlock instance
     */
    fun createBlock(
        node: ASTNode,
        wrap: Wrap? = null,
        alignment: Alignment? = null
    ): PicatBlock {
        return PicatBlock(
            node,
            wrap,
            alignment,
            settings,
            spacingBuilder,
            this
        )
    }

    /**
     * Creates child blocks for the given parent node.
     *
     * @param node The parent node to create child blocks for
     * @return A list of child blocks
     */
    fun createChildBlocks(node: ASTNode): List<Block> {
        val blocks = ArrayList<Block>()
        var child = node.firstChildNode

        while (child != null) {
            // Skip whitespace nodes and empty nodes
            if (!child.textRange.isEmpty && !isWhitespaceNode(child)) {
                val block = createBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment()
                )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks
    }

    /**
     * Checks if the given node is a whitespace node.
     * Delegates to PicatBlock.isWhitespaceNode for consistency.
     * 
     * @param node The node to check
     * @return true if the node is a whitespace node, false otherwise
     */
    fun isWhitespaceNode(node: ASTNode): Boolean {
        return PicatBlock.isWhitespaceNode(node)
    }
}
