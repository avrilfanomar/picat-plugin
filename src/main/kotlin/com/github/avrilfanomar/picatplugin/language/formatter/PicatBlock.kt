package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType

/**
 * Configuration class for PicatBlock.
 */
class PicatBlockConfig(
    val settings: CodeStyleSettings,
    val spacingBuilder: SpacingBuilder? = null,
    val blockFactory: PicatBlockFactory? = null
)

/**
 * Core formatter component for Picat language.
 * This class is responsible for determining indentation, spacing, and child block creation.
 */
class PicatBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val config: PicatBlockConfig
) : AbstractBlock(node, wrap, alignment) {

    // Helper for indentation rules
    private val helper = PicatBlockHelper()

    override fun buildChildren(): List<Block> {
        return config.blockFactory?.createBlocks(myNode) ?: emptyList()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return config.spacingBuilder?.getSpacing(this, child1, child2)
    }

    override fun getIndent(): Indent? {
        val elementType = myNode.elementType
        val parentType = myNode.treeParent?.elementType
        val grandParentType = myNode.treeParent?.treeParent?.elementType
        val greatGrandParentType = myNode.treeParent?.treeParent?.treeParent?.elementType
        val picatSettings = config.settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        return calculateIndent(elementType, parentType, grandParentType, greatGrandParentType, picatSettings)
    }

    private fun calculateIndent(
        elementType: IElementType?,
        parentType: IElementType?,
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // Use a variable to store the indent
        var indent: Indent?

        // Handle whitespace indentation
        if (elementType == PicatTokenTypes.WHITE_SPACE) {
            indent = Indent.getNoneIndent()
        }
        // Handle rule body indentation
        else if (helper.shouldIndentRuleBody(parentType, picatSettings)) {
            indent = Indent.getIndent(Indent.Type.SPACES, 4, true, false)
        }
        // Handle statement indentation
        else if (helper.shouldIndentStatements(parentType, grandParentType, greatGrandParentType)) {
            indent = Indent.getNormalIndent()
        }
        // Handle block statement indentation
        else if (helper.shouldIndentBlockStatements(elementType, parentType, picatSettings)) {
            indent = Indent.getNormalIndent()
        }
        // Handle list comprehension indentation
        else if (helper.shouldIndentListComprehension(parentType, elementType, picatSettings)) {
            indent = Indent.getNormalIndent()
        }
        // Handle function arguments indentation
        else if (helper.shouldIndentFunctionArguments(elementType, parentType)) {
            indent = Indent.getNormalIndent()
        }
        // Handle list elements indentation
        else if (helper.shouldIndentListElements(elementType, parentType)) {
            indent = Indent.getNormalIndent()
        }
        // Handle nested expressions indentation
        else if (helper.shouldIndentNestedExpressions(parentType, grandParentType)) {
            indent = Indent.getNormalIndent()
        }
        // Handle statements after rule operators
        else if (parentType == PicatTokenTypes.STATEMENT && grandParentType == PicatTokenTypes.RULE) {
            indent = Indent.getNormalIndent()
        }
        // Handle elements after rule operators
        else if (parentType == PicatTokenTypes.RULE) {
            indent = Indent.getNormalIndent()
        }
        // Default to no indentation
        else {
            indent = Indent.getNoneIndent()
        }

        return indent
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Use a variable to store the indent
        var indent: Indent?

        // Always indent rule bodies and statements
        if (helper.isRuleBodyOrStatementType(elementType)) {
            indent = Indent.getNormalIndent()
        }
        // Improved indentation for block statements
        else if (helper.isBlockStatementType(elementType)) {
            indent = Indent.getNormalIndent()
        }
        // Enhanced indentation for list comprehension
        else if (elementType == PicatTokenTypes.LIST_COMPREHENSION) {
            indent = Indent.getNormalIndent()
        }
        // Proper indentation for function calls
        else if (elementType == PicatTokenTypes.FUNCTION_CALL) {
            indent = Indent.getNormalIndent()
        }
        // Consistent indentation for lists
        else if (elementType == PicatTokenTypes.LIST) {
            indent = Indent.getNormalIndent()
        }
        // Appropriate indentation for expressions
        else if (elementType == PicatTokenTypes.EXPRESSION) {
            indent = Indent.getNormalIndent()
        }
        // Indent rule bodies
        else if (elementType == PicatTokenTypes.RULE) {
            indent = Indent.getNormalIndent()
        }
        // Default to no indentation
        else {
            indent = Indent.getNoneIndent()
        }

        return indent
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
