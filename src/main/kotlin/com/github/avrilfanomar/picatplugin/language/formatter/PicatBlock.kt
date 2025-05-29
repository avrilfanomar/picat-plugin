package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Wrap
import com.intellij.formatting.Alignment
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Indent
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
        // If we have a block factory, use it to create child blocks
        if (blockFactory != null) {
            return blockFactory.createChildBlocks(myNode)
        }

        // Otherwise, create child blocks manually
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            // Create a block for each child node
            val childBlock = PicatBlock(
                child,
                null,
                null,
                settings,
                spacingBuilder,
                this.blockFactory
            )
            blocks.add(childBlock)

            child = child.treeNext
        }

        return blocks
    }

    /**
     * Determines the spacing between child blocks.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder?.getSpacing(this, child1, child2)
    }

    /**
     * Checks if the given type is a block statement type.
     */
    private fun isBlockStatementType(type: Any?): Boolean {
        return type == PicatTokenTypes.IF_THEN_ELSE || 
               type == PicatTokenTypes.FOREACH_LOOP ||
               type == PicatTokenTypes.WHILE_LOOP || 
               type == PicatTokenTypes.FOR_LOOP ||
               type == PicatTokenTypes.TRY_CATCH
    }

    /**
     * Checks if the given type is a rule body type.
     */
    private fun isRuleBodyType(type: Any?): Boolean {
        return type == PicatTokenTypes.BODY || 
               type == PicatTokenTypes.RULE || 
               type == PicatTokenTypes.RULE_BODY
    }

    /**
     * Checks if the given type is a statement or expression type.
     */
    private fun isStatementOrExpression(type: Any?): Boolean {
        return type == PicatTokenTypes.STATEMENT || 
               type == PicatTokenTypes.EXPRESSION
    }

    /**
     * Determines indentation for whitespace nodes.
     */
    private fun getWhitespaceIndent(
        elementType: Any?,
        parentType: Any?,
        grandParentType: Any?
    ): Indent? {
        if (elementType != PicatTokenTypes.WHITE_SPACE) {
            return null
        }

        // If the parent is a rule or a block statement, indent the whitespace
        if (isRuleBodyType(parentType) || 
            isBlockStatementType(parentType) ||
            // Also indent whitespace in statements within rule bodies or block statements
            (parentType == PicatTokenTypes.STATEMENT &&
                    (isRuleBodyType(grandParentType) || 
                     isBlockStatementType(grandParentType)))) {
            return Indent.getNormalIndent()
        }
        return Indent.getNoneIndent()
    }

    /**
     * Determines indentation for rule bodies and statements.
     */
    private fun getRuleBodyIndent(
        elementType: Any?,
        parentType: Any?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // Handle rule body indentation
        if (elementType == PicatTokenTypes.BODY && 
            parentType == PicatTokenTypes.RULE && 
            picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // Handle rule body statements indentation
        if (elementType == PicatTokenTypes.STATEMENT && 
            isRuleBodyType(parentType) && 
            picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for block statements and list comprehensions.
     */
    private fun getBlockIndent(
        elementType: Any?,
        parentType: Any?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // Handle block statement indentation
        if (isStatementOrExpression(elementType) && 
            isBlockStatementType(parentType) && 
            picatSettings.indentBlockStatements) {
            return Indent.getNormalIndent()
        }

        // Handle list comprehension indentation
        if (isStatementOrExpression(elementType) && 
            parentType == PicatTokenTypes.LIST_COMPREHENSION && 
            picatSettings.indentListComprehension) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for expressions in parentheses.
     */
    private fun getParenthesizedExpressionIndent(
        elementType: Any?,
        parentType: Any?
    ): Indent? {
        // Handle indentation for expressions in parentheses
        // Note: We check for expressions that are children of terms with parentheses
        if (elementType == PicatTokenTypes.EXPRESSION && 
            parentType == PicatTokenTypes.TERM && 
            myNode.treeParent?.findChildByType(PicatTokenTypes.LPAR) != null) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for arguments and list elements.
     */
    private fun getArgumentIndent(
        elementType: Any?,
        parentType: Any?
    ): Indent? {
        // Handle indentation for arguments in function calls
        if (elementType == PicatTokenTypes.ARGUMENT && parentType == PicatTokenTypes.ARGUMENT_LIST) {
            return Indent.getContinuationIndent()
        }

        // Handle indentation for elements in lists
        if (elementType == PicatTokenTypes.EXPRESSION && parentType == PicatTokenTypes.LIST_ELEMENTS) {
            return Indent.getContinuationIndent()
        }

        // Handle indentation for elements in structures
        if (elementType == PicatTokenTypes.ARGUMENT && parentType == PicatTokenTypes.STRUCTURE) {
            return Indent.getContinuationIndent()
        }

        return null
    }

    /**
     * Determines the indentation for this block.
     */
    override fun getIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val parentType = myNode.treeParent?.elementType
        val elementType = myNode.elementType
        val grandParentType = myNode.treeParent?.treeParent?.elementType

        // Try each indentation strategy in order
        return getWhitespaceIndent(elementType, parentType, grandParentType)
            ?: getRuleBodyIndent(elementType, parentType, picatSettings)
            ?: getBlockIndent(elementType, parentType, picatSettings)
            ?: getParenthesizedExpressionIndent(elementType, parentType)
            ?: getArgumentIndent(elementType, parentType)
            ?: Indent.getNoneIndent() // Default: no indentation
    }

    /**
     * Determines indentation for rule body child blocks.
     */
    private fun getChildRuleBodyIndent(
        elementType: Any?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // For rule bodies, indent child blocks
        if (elementType == PicatTokenTypes.RULE && picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // For rule bodies, indent child blocks
        if ((elementType == PicatTokenTypes.BODY || elementType == PicatTokenTypes.RULE_BODY) && 
            picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for block statement child blocks.
     */
    private fun getChildBlockIndent(
        elementType: Any?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // For block statements, indent child blocks
        if (isBlockStatementType(elementType) && picatSettings.indentBlockStatements) {
            return Indent.getNormalIndent()
        }

        // For list comprehensions, indent child blocks
        if (elementType == PicatTokenTypes.LIST_COMPREHENSION && picatSettings.indentListComprehension) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for parenthesized expression child blocks.
     */
    private fun getChildParenthesizedExpressionIndent(elementType: Any?): Indent? {
        // For terms with parentheses, indent child blocks
        if (elementType == PicatTokenTypes.TERM && 
            myNode.findChildByType(PicatTokenTypes.LPAR) != null) {
            return Indent.getNormalIndent()
        }

        return null
    }

    /**
     * Determines indentation for argument and list element child blocks.
     */
    private fun getChildArgumentIndent(elementType: Any?): Indent? {
        // For argument lists, indent child blocks
        if (elementType == PicatTokenTypes.ARGUMENT_LIST) {
            return Indent.getContinuationIndent()
        }

        // For list elements, indent child blocks
        if (elementType == PicatTokenTypes.LIST_ELEMENTS) {
            return Indent.getContinuationIndent()
        }

        return null
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val elementType = myNode.elementType

        // Try each indentation strategy in order
        return getChildRuleBodyIndent(elementType, picatSettings)
            ?: getChildBlockIndent(elementType, picatSettings)
            ?: getChildParenthesizedExpressionIndent(elementType)
            ?: getChildArgumentIndent(elementType)
            ?: Indent.getNoneIndent() // Default: no indentation for child blocks
    }

    /**
     * Determines if this block is a leaf block (has no children).
     */
    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
