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
class PicatBlockConfig(val settings: CodeStyleSettings, val spacingBuilder: SpacingBuilder? = null, val blockFactory: PicatBlockFactory? = null)

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
        return config.blockFactory?.createBlocks(myNode, config) ?: emptyList()
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
        // Handle whitespace indentation
        if (elementType == PicatTokenTypes.WHITE_SPACE) {
            return getIndentForWhitespace(parentType, picatSettings)
        }

        // Handle rule body indentation
        if (helper.shouldIndentRuleBody(parentType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle statement indentation
        if (helper.shouldIndentStatements(elementType, parentType, grandParentType, greatGrandParentType)) {
            return Indent.getNormalIndent()
        }

        // Handle block statement indentation
        if (helper.shouldIndentBlockStatements(elementType, parentType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle list comprehension indentation
        if (helper.shouldIndentListComprehension(parentType, elementType, picatSettings)) {
            return Indent.getNormalIndent()
        }

        // Handle function arguments indentation
        if (helper.shouldIndentFunctionArguments(elementType, parentType)) {
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

        return Indent.getNoneIndent()
    }

    private fun getIndentForWhitespace(parentType: IElementType?, picatSettings: PicatCodeStyleSettings): Indent? {
        // Indent whitespace in rule bodies
        if (helper.isRuleBodyOrStatementType(parentType) && picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }

        // Indent whitespace in block statements
        if (helper.isBlockStatementType(parentType) && picatSettings.indentBlockStatements) {
            return Indent.getNormalIndent()
        }

        return Indent.getNoneIndent()
    }

    private fun shouldIndentRuleBody(parentType: IElementType?, picatSettings: PicatCodeStyleSettings): Boolean {
        return helper.isRuleBodyOrStatementType(parentType) && picatSettings.indentRuleBody
    }

    private fun shouldIndentStatements(
        elementType: IElementType?,
        parentType: IElementType?,
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?
    ): Boolean {
        // Indent statements in rule bodies
        if (helper.isRuleBodyOrStatement(parentType, grandParentType)) {
            return true
        }

        // Indent nested statements
        if (helper.isNestedStatementInRuleBody(parentType, grandParentType, greatGrandParentType)) {
            return true
        }

        return false
    }

    private fun shouldIndentBlockStatements(
        elementType: IElementType?,
        parentType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        // Indent statements in block statements
        if (helper.isBlockStatementType(parentType) && picatSettings.indentBlockStatements) {
            return !helper.isBlockKeywordType(elementType)
        }

        return false
    }

    private fun shouldIndentListComprehension(
        parentType: IElementType?,
        elementType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        // Indent list comprehension elements
        if (helper.isListComprehensionNonBracketOrPipe(parentType, elementType) && picatSettings.indentListComprehension) {
            return true
        }

        return false
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Use a variable to store the indent
        var indent: Indent? = null

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
