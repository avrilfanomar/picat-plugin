package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import java.util.*

/**
 * Block implementation for Picat language formatting.
 * This class defines how code blocks should be formatted in the Picat language.
 */
class PicatBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val settings: CodeStyleSettings
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = ArrayList<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            // Skip whitespace nodes
            if (!child.textRange.isEmpty) {
                val block = PicatBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    settings
                )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // Basic spacing rules
        return Spacing.createSpacing(1, 1, 0, true, 1)
    }

    override fun getIndent(): Indent? {
        // Basic indentation rules
        return Indent.getNoneIndent()
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
