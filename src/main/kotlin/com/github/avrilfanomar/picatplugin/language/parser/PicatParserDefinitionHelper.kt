package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRuleImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatBodyImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFactImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionDefinitionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionBodyImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIncludeStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatModuleDeclarationImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTermImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatStructureImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatArgumentImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatArgumentListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListElementsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatOperatorImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatLiteralImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatVariableImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAtomImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIdentifierImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatModuleNameImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

/**
 * Helper class for PicatParserDefinition.
 * Contains methods for creating PSI elements based on element types.
 */
class PicatParserDefinitionHelper {
    /**
     * Checks if the element type is a rule-related element.
     */
    fun isRuleElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.RULE ||
               type == PicatTokenTypes.HEAD ||
               type == PicatTokenTypes.BODY ||
               type == PicatTokenTypes.FACT ||
               type == PicatTokenTypes.FUNCTION_DEFINITION ||
               type == PicatTokenTypes.FUNCTION_BODY
    }

    /**
     * Creates a rule-related PSI element.
     */
    fun createRuleElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.RULE -> PicatRuleImpl(node)
            PicatTokenTypes.HEAD -> PicatHeadImpl(node)
            PicatTokenTypes.BODY -> PicatBodyImpl(node)
            PicatTokenTypes.FACT -> PicatFactImpl(node)
            PicatTokenTypes.FUNCTION_DEFINITION -> PicatFunctionDefinitionImpl(node)
            PicatTokenTypes.FUNCTION_BODY -> PicatFunctionBodyImpl(node)
            else -> throw IllegalArgumentException("Unexpected rule element type: $type")
        }
    }

    /**
     * Checks if the element type is a statement-related element.
     */
    fun isStatementElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.EXPORT_STATEMENT ||
               type == PicatTokenTypes.IMPORT_STATEMENT ||
               type == PicatTokenTypes.INCLUDE_STATEMENT ||
               type == PicatTokenTypes.GOAL ||
               type == PicatTokenTypes.MODULE_DECLARATION ||
               type == PicatTokenTypes.EXPRESSION ||
               type == PicatTokenTypes.TERM
    }

    /**
     * Creates a statement-related PSI element.
     */
    fun createStatementElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.EXPORT_STATEMENT -> PicatExportStatementImpl(node)
            PicatTokenTypes.IMPORT_STATEMENT -> PicatImportStatementImpl(node)
            PicatTokenTypes.INCLUDE_STATEMENT -> PicatIncludeStatementImpl(node)
            PicatTokenTypes.GOAL -> PicatGoalImpl(node)
            PicatTokenTypes.MODULE_DECLARATION -> PicatModuleDeclarationImpl(node)
            PicatTokenTypes.EXPRESSION -> PicatExpressionImpl(node)
            PicatTokenTypes.TERM -> PicatTermImpl(node)
            else -> throw IllegalArgumentException("Unexpected statement element type: $type")
        }
    }

    /**
     * Checks if the element type is a structure-related element.
     */
    fun isStructureElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.STRUCTURE ||
               type == PicatTokenTypes.ARGUMENT ||
               type == PicatTokenTypes.ARGUMENT_LIST ||
               type == PicatTokenTypes.LIST ||
               type == PicatTokenTypes.LIST_ELEMENTS ||
               type == PicatTokenTypes.OPERATOR
    }

    /**
     * Creates a structure-related PSI element.
     */
    fun createStructureElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.STRUCTURE -> PicatStructureImpl(node)
            PicatTokenTypes.ARGUMENT -> PicatArgumentImpl(node)
            PicatTokenTypes.ARGUMENT_LIST -> PicatArgumentListImpl(node)
            PicatTokenTypes.LIST -> PicatListImpl(node)
            PicatTokenTypes.LIST_ELEMENTS -> PicatListElementsImpl(node)
            PicatTokenTypes.OPERATOR -> PicatOperatorImpl(node)
            else -> throw IllegalArgumentException("Unexpected structure element type: $type")
        }
    }

    /**
     * Checks if the element type is a literal-related element.
     */
    fun isLiteralElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.LITERAL ||
               type == PicatTokenTypes.VARIABLE ||
               type == PicatTokenTypes.ATOM ||
               type == PicatTokenTypes.IDENTIFIER ||
               type == PicatTokenTypes.MODULE_NAME
    }

    /**
     * Creates a literal-related PSI element.
     */
    fun createLiteralElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.LITERAL -> PicatLiteralImpl(node)
            PicatTokenTypes.VARIABLE -> PicatVariableImpl(node)
            PicatTokenTypes.ATOM -> PicatAtomImpl(node)
            PicatTokenTypes.IDENTIFIER -> PicatIdentifierImpl(node)
            PicatTokenTypes.MODULE_NAME -> PicatModuleNameImpl(node)
            else -> throw IllegalArgumentException("Unexpected literal element type: $type")
        }
    }
}
