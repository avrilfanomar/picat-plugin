package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
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

        // Handle whitespace
        if (elementType == TokenType.WHITE_SPACE) {
            return Indent.getNoneIndent()
        }

        // Handle comments at the beginning of the file
        if (elementType == PicatTokenTypes.COMMENT && parentType == null) {
            return Indent.getNoneIndent()
        }

        // Handle comments in rule bodies
        if (elementType == PicatTokenTypes.COMMENT && 
            (parentType == PicatTokenTypes.BODY || 
             parentType == PicatTokenTypes.PREDICATE_RULE || 
             parentType == PicatTokenTypes.FUNCTION_RULE)) {
            return Indent.getNormalIndent()
        }

        // Handle rule body indentation
        if (helper.shouldIndentRuleBody(parentType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle statement indentation
        if (helper.shouldIndentStatements(parentType, grandParentType, greatGrandParentType)) {
            return Indent.getNormalIndent()
        }

        // Handle block statement indentation
        if (helper.shouldIndentBlockStatements(elementType, parentType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle list comprehension indentation
        if (helper.shouldIndentListComprehension(elementType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle function arguments indentation
        if (helper.shouldIndentFunctionArguments(elementType)) {
            return Indent.getNormalIndent()
        }

        // Handle list elements indentation
        if (helper.shouldIndentListElements(elementType, parentType)) {
            return Indent.getNormalIndent()
        }

        // Handle nested expressions indentation
        if (helper.shouldIndentNestedExpressions(parentType, grandParentType)) {
            return Indent.getNormalIndent()
        }

        // Handle elements after rule operators
        if (parentType == PicatTokenTypes.PREDICATE_RULE || parentType == PicatTokenTypes.FUNCTION_RULE) {
            return Indent.getNormalIndent()
        }

        // Default to no indentation
        return Indent.getNoneIndent()
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Always indent rule bodies and statements
        if (helper.isRuleBodyOrStatementType(elementType)) {
            return Indent.getNormalIndent()
        }

        // Improved indentation for block statements
        if (helper.isBlockStatementType(elementType)) {
            return Indent.getNormalIndent()
        }

        // Consistent indentation for lists
        if (elementType == PicatTokenTypes.LIST_EXPRESSION) {
            return Indent.getNormalIndent()
        }

        // Appropriate indentation for expressions
        if (elementType == PicatTokenTypes.EXPRESSION) {
            return Indent.getNormalIndent()
        }

        // Indent rule bodies
        if (elementType == PicatTokenTypes.PREDICATE_RULE || elementType == PicatTokenTypes.FUNCTION_RULE) {
            return Indent.getNormalIndent()
        }

        // Default to no indentation
        return Indent.getNoneIndent()
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
