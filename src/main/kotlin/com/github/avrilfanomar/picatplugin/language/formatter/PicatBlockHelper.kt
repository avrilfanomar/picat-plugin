package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.psi.tree.IElementType

/**
 * Helper class for PicatBlock.
 * Contains methods for determining indentation rules.
 */
class PicatBlockHelper {

    /**
     * Checks if the given type is a rule body or statement type.
     */
    fun isRuleBodyOrStatementType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.GOAL ||
                type == PicatTokenTypes.BODY ||
                type == PicatTokenTypes.COMMENT
    }

    /**
     * Checks if the given type is a block statement type.
     */
    fun isBlockStatementType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.IF_THEN_ELSE ||
                type == PicatTokenTypes.FOREACH_LOOP ||
                type == PicatTokenTypes.WHILE_LOOP ||
                type == PicatTokenTypes.TRY_CATCH
    }

    /**
     * Checks if the given type is a block keyword type.
     */
    fun isBlockKeywordType(type: IElementType?): Boolean {
        return type == PicatTokenTypes.IF_KEYWORD ||
                type == PicatTokenTypes.THEN_KEYWORD ||
                type == PicatTokenTypes.ELSE_KEYWORD ||
                type == PicatTokenTypes.FOREACH_KEYWORD ||
                type == PicatTokenTypes.WHILE_KEYWORD ||
                type == PicatTokenTypes.TRY_KEYWORD ||
                type == PicatTokenTypes.CATCH_KEYWORD
    }

    /**
     * Checks if the element is part of a list comprehension but not a bracket or pipe.
     */
    fun isListComprehensionNonBracketOrPipe(parentType: IElementType?, elementType: IElementType?): Boolean {
        return (parentType == PicatTokenTypes.LIST_COMPREHENSION_GOAL ||
                parentType == PicatTokenTypes.LIST_COMPREHENSION_EXPRESSION) &&
                elementType != PicatTokenTypes.LBRACKET &&
                elementType != PicatTokenTypes.RBRACKET &&
                elementType != PicatTokenTypes.PIPE
    }

    /**
     * Checks if the element is a rule body or statement based on parent and grandparent types.
     */
    fun isRuleBodyOrStatement(parentType: IElementType?, grandParentType: IElementType?): Boolean {
        return (parentType == PicatTokenTypes.BODY &&
                (grandParentType == PicatTokenTypes.PREDICATE_RULE || grandParentType == PicatTokenTypes.FUNCTION_RULE))
    }

    /**
     * Checks if the element is a nested statement in a rule body.
     */
    fun isNestedStatementInRuleBody(
        parentType: IElementType?,
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?
    ): Boolean {
        return (parentType == PicatTokenTypes.GOAL &&
                (grandParentType == PicatTokenTypes.BODY ||
                        grandParentType == PicatTokenTypes.PREDICATE_RULE ||
                        grandParentType == PicatTokenTypes.FUNCTION_RULE) &&
                (greatGrandParentType == PicatTokenTypes.PREDICATE_RULE ||
                        greatGrandParentType == PicatTokenTypes.FUNCTION_RULE ||
                        greatGrandParentType == PicatTokenTypes.GOAL))
    }

    /**
     * Checks if the element is a nested expression.
     */
    fun shouldIndentNestedExpressions(parentType: IElementType?, grandParentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.EXPRESSION && grandParentType == PicatTokenTypes.EXPRESSION
    }

    /**
     * Checks if the element is a function argument that should be indented.
     */
    fun shouldIndentFunctionArguments(elementType: IElementType?, parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.FUNCTION_CALL &&
                elementType != PicatTokenTypes.LPAR &&
                elementType != PicatTokenTypes.RPAR
    }

    /**
     * Checks if the element is a list element that should be indented.
     */
    fun shouldIndentListElements(elementType: IElementType?, parentType: IElementType?): Boolean {
        return parentType == PicatTokenTypes.LIST_EXPRESSION &&
                elementType != PicatTokenTypes.LBRACKET &&
                elementType != PicatTokenTypes.RBRACKET
    }

    /**
     * Checks if a rule body should be indented based on settings.
     */
    fun shouldIndentRuleBody(parentType: IElementType?, picatSettings: PicatCodeStyleSettings): Boolean {
        return isRuleBodyOrStatementType(parentType) && 
               picatSettings.indentRuleBody
    }

    /**
     * Checks if list comprehension elements should be indented based on settings.
     */
    fun shouldIndentListComprehension(
        parentType: IElementType?,
        elementType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        return isListComprehensionNonBracketOrPipe(parentType, elementType) && 
               picatSettings.indentListComprehension
    }

    /**
     * Checks if block statements should be indented based on settings.
     */
    fun shouldIndentBlockStatements(
        elementType: IElementType?,
        parentType: IElementType?,
        picatSettings: PicatCodeStyleSettings
    ): Boolean {
        // Indent statements in block statements
        if (isBlockStatementType(parentType) && 
            picatSettings.indentBlockStatements) {
            return !isBlockKeywordType(elementType)
        }

        return false
    }

    /**
     * Checks if statements should be indented based on their context.
     */
    fun shouldIndentStatements(
        parentType: IElementType?,
        grandParentType: IElementType?,
        greatGrandParentType: IElementType?
    ): Boolean {
        // Use a variable to store the result
        var shouldIndent = false

        // Indent statements in rule bodies
        if (isRuleBodyOrStatement(parentType, grandParentType)) {
            shouldIndent = true
        }
        // Indent nested statements
        else if (isNestedStatementInRuleBody(parentType, grandParentType, greatGrandParentType)) {
            shouldIndent = true
        }

        return shouldIndent
    }
}
