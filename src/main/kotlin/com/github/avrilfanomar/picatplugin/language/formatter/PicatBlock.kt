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
    /** Code style settings for formatting. */
    val settings: CodeStyleSettings,
    /** Builder for creating spacing rules. */
    val spacingBuilder: SpacingBuilder? = null,
    /** Factory for creating child blocks. */
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
            picatSettings = config.settings.getCustomSettings(PicatCodeStyleSettings::class.java),
            node = myNode
        )

        return determineIndent(context)
    }

    private fun determineIndent(context: IndentContext): Indent? {
        return when {
            // Handle whitespace and file-level comments
            shouldUseNoIndent(context) -> Indent.getNoneIndent()
            // Handle cases that require normal indentation
            shouldUseNormalIndent(context) -> Indent.getNormalIndent()
            // Default to null (use parent's child indent)
            else -> null
        }
    }

    private fun shouldUseNoIndent(context: IndentContext): Boolean {
        return context.elementType == TokenType.WHITE_SPACE ||
                (context.elementType == PicatTokenTypes.COMMENT ||
                        context.elementType == PicatTokenTypes.MULTILINE_COMMENT) && context.parentType == null
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
                isElementAfterRuleOperator(context.parentType) ||
                isConsequenceConjunction(context) ||
                isIfThenAfterSemicolon(context)
    }

    /**
     * Checks if this CONJUNCTION is the second child of IF_THEN (the consequence after ->).
     * In Prolog-style if-then-else, the consequence after -> should be indented.
     */
    private fun isConsequenceConjunction(context: IndentContext): Boolean {
        val isConjunctionInIfThen =
            context.elementType == PicatTokenTypes.CONJUNCTION &&
                context.parentType == PicatTokenTypes.IF_THEN
        return isConjunctionInIfThen && hasIfThenOpBefore(context.node)
    }

    /**
     * Checks if the node has an IF_THEN_OP (arrow) immediately before it (skipping whitespace).
     */
    private fun hasIfThenOpBefore(node: ASTNode): Boolean {
        var sibling = node.treePrev
        while (sibling != null) {
            val elementType = sibling.elementType
            if (elementType == TokenType.WHITE_SPACE) {
                sibling = sibling.treePrev
                continue
            }
            return elementType == PicatTokenTypes.IF_THEN_OP
        }
        return false
    }

    /**
     * Checks if this IF_THEN element comes after a SEMICOLON in a disjunction.
     * In Prolog-style if-then-else, else branches after ; should be indented.
     */
    private fun isIfThenAfterSemicolon(context: IndentContext): Boolean {
        val isIfThenInDisjunction =
            context.elementType == PicatTokenTypes.IF_THEN &&
                context.parentType == PicatTokenTypes.DISJUNCTION
        return isIfThenInDisjunction && hasSemicolonBefore(context.node)
    }

    /**
     * Checks if the node has a SEMICOLON or OR_OR before it (skipping whitespace and comments).
     */
    private fun hasSemicolonBefore(node: ASTNode): Boolean {
        var sibling = node.treePrev
        while (sibling != null) {
            val elementType = sibling.elementType
            if (isWhitespaceOrComment(elementType)) {
                sibling = sibling.treePrev
                continue
            }
            return elementType == PicatTokenTypes.SEMICOLON || elementType == PicatTokenTypes.OR_OR
        }
        return false
    }

    private fun isWhitespaceOrComment(elementType: IElementType): Boolean {
        return elementType == TokenType.WHITE_SPACE ||
            elementType == PicatTokenTypes.COMMENT ||
            elementType == PicatTokenTypes.MULTILINE_COMMENT
    }

    private fun isCommentInRuleBody(context: IndentContext): Boolean {
        val isCommentToken =
            context.elementType == PicatTokenTypes.COMMENT || context.elementType == PicatTokenTypes.MULTILINE_COMMENT
        return isCommentToken &&
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
        val picatSettings: PicatCodeStyleSettings,
        val node: ASTNode
    )

    /**
     * Determines the indentation for child blocks.
     * This is called to determine the indent that should be applied to children of this node.
     * Returns Normal indent for specific element types, null otherwise to let children decide.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType
        return if (shouldIndentChildElements(elementType)) {
            Indent.getNormalIndent()
        } else {
            null
        }
    }

    private fun shouldIndentChildElements(elementType: IElementType): Boolean {
        return helper.isRuleBodyOrStatementType(elementType) ||
                helper.isBlockStatementType(elementType) ||
                elementType == PicatTokenTypes.LIST_EXPR ||
                elementType == PicatTokenTypes.EXPRESSION ||
                elementType == PicatTokenTypes.PREDICATE_RULE ||
                elementType == PicatTokenTypes.FUNCTION_RULE ||
                elementType == PicatTokenTypes.IF_THEN
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
