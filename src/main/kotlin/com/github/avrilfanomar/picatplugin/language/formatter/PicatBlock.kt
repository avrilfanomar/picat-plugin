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
        val context = IndentContext(
            elementType = myNode.elementType,
            parentType = myNode.treeParent?.elementType,
            grandParentType = myNode.treeParent?.treeParent?.elementType,
            greatGrandParentType = myNode.treeParent?.treeParent?.treeParent?.elementType,
            picatSettings = config.settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        )

        return determineIndent(context)
    }

    private fun determineIndent(context: IndentContext): Indent {
        return when {
            // Handle whitespace and file-level comments
            shouldUseNoIndent(context) -> Indent.getNoneIndent()
            // Handle cases that require normal indentation
            shouldUseNormalIndent(context) -> Indent.getNormalIndent()
            // Default to no indentation
            else -> Indent.getNoneIndent()
        }
    }

    private fun shouldUseNoIndent(context: IndentContext): Boolean {
        return context.elementType == TokenType.WHITE_SPACE ||
                (context.elementType == PicatTokenTypes.COMMENT && context.parentType == null)
    }

    private fun shouldUseNormalIndent(context: IndentContext): Boolean {
        return isCommentInRuleBody(context) ||
                helper.shouldIndentRuleBody(context.parentType, context.picatSettings) ||
                helper.shouldIndentStatements(
                    context.parentType, 
                    context.grandParentType, 
                    context.greatGrandParentType
                ) ||
                helper.shouldIndentBlockStatements(
                    context.elementType, 
                    context.parentType, 
                    context.picatSettings
                ) ||
                helper.shouldIndentListComprehension(context.elementType, context.picatSettings) ||
                helper.shouldIndentFunctionArguments(context.elementType) ||
                helper.shouldIndentListElements(context.elementType, context.parentType) ||
                helper.shouldIndentNestedExpressions(context.parentType, context.grandParentType) ||
                isElementAfterRuleOperator(context.parentType)
    }

    private fun isCommentInRuleBody(context: IndentContext): Boolean {
        return context.elementType == PicatTokenTypes.COMMENT &&
                (context.parentType == PicatTokenTypes.BODY ||
                        context.parentType == PicatTokenTypes.PREDICATE_RULE ||
                        context.parentType == PicatTokenTypes.FUNCTION_RULE)
    }

    private fun isElementAfterRuleOperator(parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.PREDICATE_RULE || parentType == PicatTokenTypes.FUNCTION_RULE
    }

    private data class IndentContext(
        val elementType: IElementType,
        val parentType: IElementType?,
        val grandParentType: IElementType?,
        val greatGrandParentType: IElementType?,
        val picatSettings: PicatCodeStyleSettings
    )

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        return if (shouldIndentChildElements(elementType)) {
            Indent.getNormalIndent()
        } else {
            Indent.getNoneIndent()
        }
    }

    private fun shouldIndentChildElements(elementType: IElementType): Boolean {
        return helper.isRuleBodyOrStatementType(elementType) ||
                helper.isBlockStatementType(elementType) ||
                elementType == PicatTokenTypes.LIST_EXPR ||
                elementType == PicatTokenTypes.EXPRESSION ||
                elementType == PicatTokenTypes.PREDICATE_RULE ||
                elementType == PicatTokenTypes.FUNCTION_RULE
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
