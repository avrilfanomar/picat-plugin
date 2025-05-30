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
        return blockFactory?.createChildBlocks(myNode) ?: emptyList()
    }

    /**
     * Determines the spacing between child blocks.
     */
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder?.getSpacing(this, child1, child2)
    }

    /**
     * Determines the indentation for this block.
     */
    override fun getIndent(): Indent? {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)
        val parentType = myNode.treeParent?.elementType
        val elementType = myNode.elementType
        val grandParentType = myNode.treeParent?.treeParent?.elementType
        val greatGrandParentType = myNode.treeParent?.treeParent?.treeParent?.elementType

        // Use a variable to store the indent and return it at the end
        val indent = calculateIndent(
            elementType, 
            parentType, 
            grandParentType, 
            greatGrandParentType, 
            picatSettings
        )

        return indent
    }

    /**
     * Calculates the appropriate indent based on the element type and context.
     */
    private fun calculateIndent(
        elementType: IElementType?,
        parentType: IElementType?,
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Indent? {
        // Use a variable to store the indent
        var indent: Indent? = null

        // Handle whitespace nodes
        if (elementType == PicatTokenTypes.WHITE_SPACE) {
            indent = getIndentForWhitespace(parentType, picatSettings)
        }
        // Indent rule body with refined rules
        else if (shouldIndentRuleBody(parentType, picatSettings)) {
            indent = Indent.getNormalIndent()
        }
        // Enhanced indentation for statements after rule operators
        else if (shouldIndentStatements(elementType, parentType, grandParentType, greatGrandParentType)) {
            indent = Indent.getNormalIndent()
        }
        // Improved indentation for block statements
        else if (shouldIndentBlockStatements(elementType, parentType, picatSettings)) {
            indent = Indent.getNormalIndent()
        }
        // Enhanced indentation for list comprehension
        else if (shouldIndentListComprehension(parentType, elementType, picatSettings)) {
            indent = Indent.getNormalIndent()
        }
        // Special indentation for function arguments
        else if (shouldIndentFunctionArguments(elementType, parentType)) {
            indent = Indent.getContinuationIndent()
        }
        // Special indentation for list elements
        else if (shouldIndentListElements(elementType, parentType)) {
            indent = Indent.getContinuationIndent()
        }
        // Special indentation for nested expressions
        else if (shouldIndentNestedExpressions(parentType, grandParentType)) {
            indent = Indent.getContinuationIndent()
        }
        // Default indentation
        else {
            indent = Indent.getNoneIndent()
        }

        return indent
    }

    /**
     * Determines if whitespace should be indented.
     */
    private fun getIndentForWhitespace(parentType: IElementType?, picatSettings: PicatCodeStyleSettings): Indent? {
        // If the parent is a rule or a block statement, indent the whitespace
        if (isRuleOrBlockStatementType(parentType) && picatSettings.indentRuleBody) {
            return Indent.getNormalIndent()
        }
        return Indent.getNoneIndent()
    }

    /**
     * Determines if rule body should be indented.
     */
    private fun shouldIndentRuleBody(parentType: IElementType?, picatSettings: PicatCodeStyleSettings): Boolean {
        return (parentType == PicatTokenTypes.BODY || parentType == PicatTokenTypes.RULE) && 
                picatSettings.indentRuleBody
    }

    /**
     * Determines if statements should be indented.
     */
    private fun shouldIndentStatements(
        elementType: IElementType?, 
        parentType: IElementType?, 
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?
    ): Boolean {
        return isRuleBodyOrStatement(parentType, grandParentType) ||
                (elementType == PicatTokenTypes.STATEMENT && parentType == PicatTokenTypes.RULE) ||
                // Handle nested statements in rule bodies
                isNestedStatementInRuleBody(parentType, grandParentType, greatGrandParentType)
    }

    /**
     * Determines if block statements should be indented.
     */
    private fun shouldIndentBlockStatements(
        elementType: IElementType?, 
        parentType: IElementType?, 
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        return isBlockStatementType(parentType) &&
                // Don't indent keywords
                !isBlockKeywordType(elementType) &&
                picatSettings.indentBlockStatements
    }

    /**
     * Determines if list comprehension should be indented.
     */
    private fun shouldIndentListComprehension(
        parentType: IElementType?, 
        elementType: IElementType?, 
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        return isListComprehensionNonBracketOrPipe(parentType, elementType) &&
                picatSettings.indentListComprehension
    }

    /**
     * Determines if function arguments should be indented.
     */
    private fun shouldIndentFunctionArguments(elementType: IElementType?, parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.FUNCTION_CALL &&
                elementType != PicatTokenTypes.LPAR &&
                elementType != PicatTokenTypes.RPAR
    }

    /**
     * Determines if list elements should be indented.
     */
    private fun shouldIndentListElements(elementType: IElementType?, parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.LIST &&
                elementType != PicatTokenTypes.LBRACKET &&
                elementType != PicatTokenTypes.RBRACKET
    }

    /**
     * Determines if nested expressions should be indented.
     */
    private fun shouldIndentNestedExpressions(parentType: IElementType?, grandParentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.EXPRESSION && grandParentType == PicatTokenTypes.EXPRESSION
    }

    /**
     * Checks if a type is a rule or block statement.
     */
    private fun isRuleOrBlockStatementType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.BODY || 
               type == PicatTokenTypes.RULE || 
               type == PicatTokenTypes.RULE_BODY ||
               type == PicatTokenTypes.IF_THEN_ELSE || 
               type == PicatTokenTypes.FOREACH_LOOP ||
               type == PicatTokenTypes.WHILE_LOOP || 
               type == PicatTokenTypes.FOR_LOOP ||
               type == PicatTokenTypes.TRY_CATCH
    }

    /**
     * Checks if a type is a rule body or statement.
     */
    private fun isRuleBodyOrStatement(parentType: IElementType?, grandParentType: IElementType?): Boolean {
        return (parentType == PicatTokenTypes.BODY && grandParentType == PicatTokenTypes.RULE) ||
               (parentType == PicatTokenTypes.RULE_BODY)
    }

    /**
     * Checks if a statement is nested in a rule body.
     */
    private fun isNestedStatementInRuleBody(
        parentType: IElementType?, 
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?
    ): Boolean {
        return (parentType == PicatTokenTypes.STATEMENT && 
                (grandParentType == PicatTokenTypes.BODY || grandParentType == PicatTokenTypes.RULE_BODY) &&
                (greatGrandParentType == PicatTokenTypes.RULE || greatGrandParentType == PicatTokenTypes.STATEMENT))
    }

    /**
     * Checks if a type is a block statement.
     */
    private fun isBlockStatementType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.IF_THEN_ELSE || 
               type == PicatTokenTypes.FOREACH_LOOP ||
               type == PicatTokenTypes.WHILE_LOOP || 
               type == PicatTokenTypes.FOR_LOOP ||
               type == PicatTokenTypes.TRY_CATCH
    }

    /**
     * Checks if a type is a block keyword.
     */
    private fun isBlockKeywordType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.IF_KEYWORD || 
               type == PicatTokenTypes.THEN_KEYWORD ||
               type == PicatTokenTypes.ELSE_KEYWORD || 
               type == PicatTokenTypes.FOREACH_KEYWORD ||
               type == PicatTokenTypes.WHILE_KEYWORD || 
               type == PicatTokenTypes.FOR_KEYWORD ||
               type == PicatTokenTypes.TRY_KEYWORD || 
               type == PicatTokenTypes.CATCH_KEYWORD
    }

    /**
     * Checks if a type is a list comprehension non-bracket or pipe.
     */
    private fun isListComprehensionNonBracketOrPipe(parentType: IElementType?, elementType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.LIST_COMPREHENSION &&
               elementType != PicatTokenTypes.LBRACKET &&
               elementType != PicatTokenTypes.RBRACKET &&
               elementType != PicatTokenTypes.PIPE
    }

    /**
     * Checks if a type is a rule body or statement type.
     */
    private fun isRuleBodyOrStatementType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.BODY || 
               type == PicatTokenTypes.RULE_BODY ||
               type == PicatTokenTypes.STATEMENT
    }

    /**
     * Determines the indentation for child blocks.
     */
    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Use a variable to store the indent
        var indent: Indent? = null

        // Always indent rule bodies and statements
        if (isRuleBodyOrStatementType(elementType)) {
            indent = Indent.getNormalIndent()
        }
        // Improved indentation for block statements
        else if (isBlockStatementType(elementType)) {
            indent = Indent.getNormalIndent()
        }
        // Enhanced indentation for list comprehension
        else if (elementType == PicatTokenTypes.LIST_COMPREHENSION) {
            indent = Indent.getNormalIndent()
        }
        // Special indentation for function calls
        else if (elementType == PicatTokenTypes.FUNCTION_CALL) {
            indent = Indent.getContinuationIndent()
        }
        // Special indentation for lists
        else if (elementType == PicatTokenTypes.LIST) {
            indent = Indent.getContinuationIndent()
        }
        // Special indentation for expressions
        else if (elementType == PicatTokenTypes.EXPRESSION) {
            indent = Indent.getContinuationIndent()
        }
        // Default to no indent for all other elements, consistent with getIndent()
        else {
            indent = Indent.getNoneIndent()
        }

        return indent
    }

    /**
     * Determines if this block is a leaf block.
     */
    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }
}
