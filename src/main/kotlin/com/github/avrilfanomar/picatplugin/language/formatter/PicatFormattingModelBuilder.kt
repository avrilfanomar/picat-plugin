package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Entry point for the Picat formatter.
 * This class creates a formatting model for Picat code.
 */
class PicatFormattingModelBuilder : FormattingModelBuilder {
    /**
     * Creates a formatting model for the given context.
     */
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings

        val spacingBuilder = createSpacingBuilder(settings)
        val rootBlock = PicatBlock2(element.node, null, null, spacingBuilder, settings)

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            settings
        )
    }

    /**
     * Creates a spacing builder with rules for Picat code.
     */
    @SuppressWarnings("LongMethod")
    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val picatSettings = settings.getCustomSettings(PicatCodeStyleSettings::class.java)

        return SpacingBuilder(settings, PicatLanguage)
            // Assignment operators (=, :=)
            .around(
                TokenSet.create(
                    PicatTokenTypes.ASSIGN_OP,
                    PicatTokenTypes.EQUAL
                )
            )
            .spaces(1)

            // Logical operators (not)
            .around(PicatTokenTypes.NOT_KEYWORD)
            .spaceIf(picatSettings.spaceAroundLogicalOperators)

            // Equality operators (==, !=, ===, !==)
            .around(
                TokenSet.create(
                    PicatTokenTypes.EQUAL,
                    PicatTokenTypes.NOT_EQUAL,
                    PicatTokenTypes.IDENTICAL,
                    PicatTokenTypes.NOT_IDENTICAL
                )
            )
            .spaces(1)

            // Relational operators (<, >, <=, >=, =<)
            .around(
                TokenSet.create(
                    PicatTokenTypes.LESS,
                    PicatTokenTypes.GREATER,
                    PicatTokenTypes.LESS_EQUAL,
                    PicatTokenTypes.GREATER_EQUAL,
                    PicatTokenTypes.LESS_EQUAL_PROLOG
                )
            )
            .spaces(1)

            // Term comparison operators (@<, @>, @=<, @>=, @=<)
            .around(
                TokenSet.create(
                    PicatTokenTypes.AT_LESS_OP,
                    PicatTokenTypes.AT_GREATER_OP,
                    PicatTokenTypes.AT_LESS_EQUAL_OP,
                    PicatTokenTypes.AT_GREATER_EQUAL_OP,
                    PicatTokenTypes.AT_LESS_EQUAL_PROLOG_OP
                )
            )
            .spaces(1)

            // Additive operators (+, -, ++)
            .around(
                TokenSet.create(
                    PicatTokenTypes.PLUS,
                    PicatTokenTypes.MINUS,
                    PicatTokenTypes.CONCAT_OP
                )
            )
            .spaces(1)

            // Multiplicative operators (*, /, //, div, mod, rem)
            .around(
                TokenSet.create(
                    PicatTokenTypes.MULTIPLY,
                    PicatTokenTypes.DIVIDE,
                    PicatTokenTypes.INT_DIVIDE,
                    PicatTokenTypes.DIV_KEYWORD,
                    PicatTokenTypes.MOD_KEYWORD,
                    PicatTokenTypes.REM_KEYWORD
                )
            )
            .spaces(1)

            // Rule operators (=>, ?=>, :-)
            .around(
                TokenSet.create(
                    PicatTokenTypes.ARROW_OP,
                    PicatTokenTypes.BACKTRACKABLE_ARROW_OP,
                    PicatTokenTypes.PROLOG_RULE_OP
                )
            )
            .spaces(1)

            // Constraint operators (#=, #!=, #>, #>=, #<, #=<, #<=, #/\, #\/, #^, #~)
            .around(
                TokenSet.create(
                    PicatTokenTypes.HASH_AND_OP,
                    PicatTokenTypes.HASH_OR_OP,
                    PicatTokenTypes.HASH_XOR_OP,
                    PicatTokenTypes.HASH_NOT_OP,
                    PicatTokenTypes.HASH_EQUAL_OP,
                    PicatTokenTypes.HASH_NOT_EQUAL_OP,
                    PicatTokenTypes.HASH_GREATER_OP,
                    PicatTokenTypes.HASH_GREATER_EQUAL_OP,
                    PicatTokenTypes.HASH_LESS_OP,
                    PicatTokenTypes.HASH_LESS_EQUAL_OP,
                    PicatTokenTypes.HASH_LESS_EQUAL_ALT_OP
                )
            )
            .spaces(1)

            // Constraint rule operators (#=>, #<=>)
            .around(
                TokenSet.create(
                    PicatTokenTypes.HASH_ARROW_OP,
                    PicatTokenTypes.HASH_BICONDITIONAL_OP
                )
            )
            .spaces(1)

            // Range operator (..)
            .around(PicatTokenTypes.RANGE_OP)
            .spaces(1)

            // Type constraint operator (::)
            .around(PicatTokenTypes.DOUBLE_COLON_OP)
            .spaces(1)

            // Bitwise operators (/\, \/, ^, ~, <<, >>, >>>)
            .around(
                TokenSet.create(
                    PicatTokenTypes.BITWISE_AND,
                    PicatTokenTypes.BITWISE_OR,
                    PicatTokenTypes.BITWISE_XOR,
                    PicatTokenTypes.COMPLEMENT,
                    PicatTokenTypes.SHIFT_LEFT,
                    PicatTokenTypes.SHIFT_RIGHT,
                    PicatTokenTypes.SHIFT_RIGHT_TRIPLE
                )
            )
            .spaces(1)

            // Punctuation
            .before(PicatTokenTypes.COMMA)
            .spaceIf(picatSettings.spaceBeforeComma)
            .after(PicatTokenTypes.COMMA)
            .spaces(1)

            // No space after comma when followed by closing brackets/parentheses/braces
            .between(
                PicatTokenTypes.COMMA, TokenSet.create(
                    PicatTokenTypes.RBRACKET,
                    PicatTokenTypes.RPAR,
                    PicatTokenTypes.RBRACE
                )
            )
            .spaces(0)

            // Ensure proper spacing around colon
            .around(PicatTokenTypes.COLON)
            .spaces(1)

            // After keywords
            .after(
                TokenSet.create(
                    PicatTokenTypes.IF_KEYWORD,
                    PicatTokenTypes.THEN_KEYWORD,
                    PicatTokenTypes.ELSE_KEYWORD,
                    PicatTokenTypes.ELSEIF_KEYWORD,
                    PicatTokenTypes.FOREACH_KEYWORD,
                    PicatTokenTypes.WHILE_KEYWORD,
                    PicatTokenTypes.LOOP_KEYWORD,
                    PicatTokenTypes.TRY_KEYWORD,
                    PicatTokenTypes.CATCH_KEYWORD,
                    PicatTokenTypes.FINALLY_KEYWORD,
                    PicatTokenTypes.IN_KEYWORD
                )
            )
            .spaces(1)

            // Ensure proper spacing after dot
            .after(PicatTokenTypes.DOT)
            .lineBreakInCode()
    }
}

/**
 * Block implementation for Picat code.
 */
class PicatBlock2(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder,
    private val settings: CodeStyleSettings
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE) {
                val childWrap = createChildWrap(child)
                val childAlignment = createChildAlignment(child)
                val childBlock = PicatBlock2(child, childWrap, childAlignment, spacingBuilder, settings)
                blocks.add(childBlock)
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun getIndent(): Indent? {
        val parentType = myNode.treeParent?.elementType
        val elementType = myNode.elementType

        if (isRuleBody(elementType, parentType) ||
            isStatementInBlock(parentType) ||
            isListElement(elementType, parentType)
        ) {
            return Indent.getNormalIndent()
        }

        // No indentation for other elements
        return Indent.getNoneIndent()
    }

    override fun getChildIndent(): Indent? {
        val elementType = myNode.elementType

        // Indent children of rule bodies
        if (isRuleBodyType(elementType) ||
            isBlockStatementType(elementType) ||
            elementType == PicatTokenTypes.LIST_EXPRESSION
        ) {
            return Indent.getNormalIndent()
        }

        // No indentation for other elements
        return Indent.getNoneIndent()
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null
    }

    private fun createChildWrap(child: ASTNode): Wrap? {
        // Add wrapping for long lists and function calls
        if (child.elementType == PicatTokenTypes.LIST_EXPRESSION) {
            return Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true)
        }
        return null
    }

    private fun createChildAlignment(child: ASTNode): Alignment? {
        // Align elements in lists and function calls
        if (child.elementType == PicatTokenTypes.LIST_EXPRESSION ||
            child.elementType == PicatTokenTypes.BODY
        ) {
            return Alignment.createAlignment(true)
        }
        return null
    }

    private fun isRuleBody(elementType: IElementType?, parentType: IElementType?): Boolean {
        return elementType == PicatTokenTypes.GOAL &&
                (parentType == PicatTokenTypes.BODY ||
                        parentType == PicatTokenTypes.PREDICATE_RULE ||
                        parentType == PicatTokenTypes.FUNCTION_RULE)
    }

    private fun isStatementInBlock(parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.IF_THEN_ELSE ||
                parentType == PicatTokenTypes.FOREACH_LOOP ||
                parentType == PicatTokenTypes.WHILE_LOOP ||
                parentType == PicatTokenTypes.TRY_CATCH
    }

    private fun isListElement(elementType: IElementType?, parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.LIST_EXPRESSION &&
                elementType != PicatTokenTypes.LBRACKET &&
                elementType != PicatTokenTypes.RBRACKET
    }

    private fun isRuleBodyType(elementType: IElementType?): Boolean {
        return elementType == PicatTokenTypes.BODY ||
                elementType == PicatTokenTypes.PREDICATE_RULE ||
                elementType == PicatTokenTypes.FUNCTION_RULE
    }

    private fun isBlockStatementType(elementType: IElementType?): Boolean {
        return elementType == PicatTokenTypes.IF_THEN_ELSE ||
                elementType == PicatTokenTypes.FOREACH_LOOP ||
                elementType == PicatTokenTypes.WHILE_LOOP ||
                elementType == PicatTokenTypes.TRY_CATCH
    }
}
