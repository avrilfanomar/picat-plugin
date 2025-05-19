package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
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

        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE) {
                val childBlock = createBlock(child)
                blocks.add(childBlock)
            }
            child = child.treeNext
        }

        return blocks
    }
}