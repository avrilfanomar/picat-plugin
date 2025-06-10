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
        return type == PicatTokenTypes.HEAD || // RULE and FACT are not ElementTypes themselves for specific rules
               type == PicatTokenTypes.BODY ||
               // PicatTokenTypes.FUNCTION_DEFINITION, // Replaced
               PicatTokenTypes.FUNCTION_BODY ||
               // New types:
               PicatTokenTypes.PREDICATE_RULE,
               PicatTokenTypes.PREDICATE_FACT,
               PicatTokenTypes.FUNCTION_RULE,
               PicatTokenTypes.FUNCTION_FACT,
               PicatTokenTypes.ACTION_RULE,
               PicatTokenTypes.PREDICATE_CLAUSE,
               PicatTokenTypes.FUNCTION_CLAUSE
    }

    /**
     * Creates a rule-related PSI element.
     */
    fun createRuleElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.HEAD -> PicatHeadImpl(node)
            PicatTokenTypes.BODY -> PicatBodyImpl(node)
            PicatTokenTypes.FUNCTION_BODY -> PicatFunctionBodyImpl(node)
            // New types:
            PicatTokenTypes.PREDICATE_RULE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateRuleImpl(node)
            PicatTokenTypes.PREDICATE_FACT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateFactImpl(node)
            PicatTokenTypes.FUNCTION_RULE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionRuleImpl(node)
            PicatTokenTypes.FUNCTION_FACT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionFactImpl(node)
            PicatTokenTypes.ACTION_RULE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActionRuleImpl(node)
            PicatTokenTypes.PREDICATE_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateClauseImpl(node)
            PicatTokenTypes.FUNCTION_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionClauseImpl(node)
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
               type == PicatTokenTypes.USING_STATEMENT || // Added
               type == PicatTokenTypes.GOAL ||
               type == PicatTokenTypes.MODULE_DECLARATION || // This is more of a definition
               type == PicatTokenTypes.END_MODULE_DECLARATION || // Added
               // PicatTokenTypes.EXPRESSION || // Expression is too broad for "statement"
               // PicatTokenTypes.TERM || // Term is too broad
               type == PicatTokenTypes.LOOP_WHILE_STATEMENT || // New
               type == PicatTokenTypes.COMPILATION_DIRECTIVE || // New
               type == PicatTokenTypes.IF_THEN_ELSE || // Added from existing PSI list
               type == PicatTokenTypes.FOREACH_LOOP || // Added
               type == PicatTokenTypes.WHILE_LOOP || // Added
               type == PicatTokenTypes.TRY_CATCH || // Added
               type == PicatTokenTypes.CASE_EXPRESSION || // Added
               type == PicatTokenTypes.ASSIGNMENT || // Added
               type == PicatTokenTypes.NEGATION || // Added
               type == PicatTokenTypes.FAIL_GOAL || // Added
               type == PicatTokenTypes.PASS_GOAL || // Added
               type == PicatTokenTypes.TRUE_GOAL || // Added
               type == PicatTokenTypes.FALSE_GOAL || // Added
               type == PicatTokenTypes.CUT_GOAL || // Added
               type == PicatTokenTypes.RETURN_STMT || // Added
               type == PicatTokenTypes.CONTINUE_STMT || // Added
               type == PicatTokenTypes.BREAK_STMT || // Added
               type == PicatTokenTypes.THROW_STMT     // Added
    }

    /**
     * Creates a statement-related PSI element.
     */
    fun createStatementElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.EXPORT_STATEMENT -> PicatExportStatementImpl(node)
            PicatTokenTypes.IMPORT_STATEMENT -> PicatImportStatementImpl(node)
            PicatTokenTypes.INCLUDE_STATEMENT -> PicatIncludeStatementImpl(node)
            PicatTokenTypes.USING_STATEMENT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUsingStatementImpl(node) // Assuming exists
            PicatTokenTypes.GOAL -> PicatGoalImpl(node)
            PicatTokenTypes.MODULE_DECLARATION -> PicatModuleDeclarationImpl(node)
            PicatTokenTypes.END_MODULE_DECLARATION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatEndModuleDeclarationImpl(node)
            PicatTokenTypes.LOOP_WHILE_STATEMENT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatLoopWhileStatementImpl(node)
            PicatTokenTypes.COMPILATION_DIRECTIVE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCompilationDirectiveImpl(node)
            PicatTokenTypes.WHILE_LOOP -> PicatWhileLoopImpl(node) // Already had this one
            PicatTokenTypes.TRY_CATCH -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTryCatchImpl(node)
            PicatTokenTypes.CASE_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseExpressionImpl(node)

            // Commenting out missing Impl classes for now to reduce compile errors in this file
            // PicatTokenTypes.IF_THEN_ELSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIfThenElseImpl(node)
            // PicatTokenTypes.FOREACH_LOOP -> PicatForeachLoopImpl(node) // PicatForeachLoopImpl exists
            // PicatTokenTypes.ASSIGNMENT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAssignmentImpl(node)
            // PicatTokenTypes.NEGATION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatNegationImpl(node)
            // PicatTokenTypes.FAIL_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFailGoalImpl(node)
            // PicatTokenTypes.PASS_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPassGoalImpl(node)
            // PicatTokenTypes.TRUE_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTrueGoalImpl(node)
            // PicatTokenTypes.FALSE_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFalseGoalImpl(node)
            // PicatTokenTypes.CUT_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCutGoalImpl(node)
            // PicatTokenTypes.RETURN_STMT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatReturnStmtImpl(node)
            // PicatTokenTypes.CONTINUE_STMT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatContinueStmtImpl(node)
            // PicatTokenTypes.BREAK_STMT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatBreakStmtImpl(node)
            // PicatTokenTypes.THROW_STMT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatThrowStmtImpl(node)
            // PicatTokenTypes.USING_STATEMENT -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUsingStatementImpl(node)
            else -> throw IllegalArgumentException("Unexpected statement element type: $type")
        }
    }

    /**
     * Checks if the element type is a structure-related element.
     */
    fun isStructureElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.STRUCTURE ||
               type == PicatTokenTypes.ARGUMENT_LIST ||
               type == PicatTokenTypes.LIST_EXPRESSION ||
               type == PicatTokenTypes.LIST_ELEMENTS ||
               type == PicatTokenTypes.EXPRESSION ||
               // type == PicatTokenTypes.TERM || // TERM is too generic, specific terms are better
               type == PicatTokenTypes.MAP ||
               type == PicatTokenTypes.MAP_ENTRIES ||
               type == PicatTokenTypes.MAP_ENTRY || // Added
               type == PicatTokenTypes.TUPLE || // Added
               type == PicatTokenTypes.TUPLE_ITEMS || // Added
               type == PicatTokenTypes.ATOM_NO_ARGS || // Added
               type == PicatTokenTypes.QUALIFIED_ATOM || // Added
               type == PicatTokenTypes.RULE_OPERATOR || // Added
               type == PicatTokenTypes.UNARY_EXPRESSION || // Added
               type == PicatTokenTypes.EXPORT_CLAUSE || // Added
               type == PicatTokenTypes.IMPORT_CLAUSE || // Added
               type == PicatTokenTypes.IMPORT_LIST || // Added
               type == PicatTokenTypes.EXPORT_LIST || // Added
               type == PicatTokenTypes.RENAME_LIST || // Added
               type == PicatTokenTypes.FOREACH_GENERATORS || // Added
               type == PicatTokenTypes.FOREACH_GENERATOR || // Added
               type == PicatTokenTypes.IMPORT_ITEM || // Added
               type == PicatTokenTypes.FILE_SPEC || // Added
               type == PicatTokenTypes.EXPORT_SPEC || // Added
               type == PicatTokenTypes.RENAME_SPEC || // Added
               type == PicatTokenTypes.CASE_ARMS || // Added
               type == PicatTokenTypes.CASE_ARM || // Added
               type == PicatTokenTypes.CATCH_CLAUSES || // Added
               type == PicatTokenTypes.CATCH_CLAUSE || // Added
               type == PicatTokenTypes.ELSEIF_CLAUSE || // Added
               type == PicatTokenTypes.PATTERN || // Added
               type == PicatTokenTypes.STRUCTURE_PATTERN || // Added
               type == PicatTokenTypes.LIST_PATTERN || // Added
               type == PicatTokenTypes.TUPLE_PATTERN || // Added
               type == PicatTokenTypes.PATTERN_LIST || // Added
               type == PicatTokenTypes.FUNCTION_CALL || // Added
               type == PicatTokenTypes.PROCEDURE_CALL || // Added
               type == PicatTokenTypes.LIST_COMPREHENSION_GOAL || // Added
               // New Expression types
               type == PicatTokenTypes.DOLLAR_TERM_CONSTRUCTOR ||
               type == PicatTokenTypes.INDEX_ACCESS_EXPRESSION ||
               type == PicatTokenTypes.AS_PATTERN_EXPRESSION ||
               type == PicatTokenTypes.LAMBDA_EXPRESSION ||
               type == PicatTokenTypes.VARIABLE_LIST ||
               type == PicatTokenTypes.TERM_CONSTRUCTOR_EXPRESSION ||
               type == PicatTokenTypes.LIST_COMPREHENSION_EXPRESSION ||
               // Directive parts
               type == PicatTokenTypes.TABLE_MODE ||
               type == PicatTokenTypes.INDEX_MODE ||
               type == PicatTokenTypes.HEAD_REFERENCE_LIST ||
               type == PicatTokenTypes.HEAD_REFERENCE ||
               type == PicatTokenTypes.INDEXING_DETAILS ||
               // Actor parts
               type == PicatTokenTypes.ACTOR_DEFINITION ||
               type == PicatTokenTypes.ACTOR_NAME ||
               type == PicatTokenTypes.ACTOR_MEMBER
    }

    /**
     * Creates a structure-related PSI element.
     */
    fun createStructureElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            PicatTokenTypes.STRUCTURE -> PicatStructureImpl(node)
            // PicatTokenTypes.ARGUMENT -> PicatArgumentImpl(node) // ARGUMENT is not a rule type
            PicatTokenTypes.ARGUMENT_LIST -> PicatArgumentListImpl(node)
            PicatTokenTypes.LIST_EXPRESSION -> PicatListImpl(node) // Assuming PicatListImpl is for LIST_EXPRESSION
            PicatTokenTypes.LIST_ELEMENTS -> PicatListElementsImpl(node)
            PicatTokenTypes.EXPRESSION -> PicatExpressionImpl(node)
            // PicatTokenTypes.TERM -> PicatTermImpl(node) // TERM is too generic
            PicatTokenTypes.QUALIFIED_ATOM -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatQualifiedAtomImpl(node)
            PicatTokenTypes.EXPORT_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportClauseImpl(node)
            PicatTokenTypes.IMPORT_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportClauseImpl(node)
            PicatTokenTypes.IMPORT_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportListImpl(node)
            PicatTokenTypes.EXPORT_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportListImpl(node)
            PicatTokenTypes.RENAME_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRenameListImpl(node)
            PicatTokenTypes.IMPORT_ITEM -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportItemImpl(node)
            PicatTokenTypes.FILE_SPEC -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileSpecImpl(node)
            PicatTokenTypes.EXPORT_SPEC -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportSpecImpl(node)
            PicatTokenTypes.RENAME_SPEC -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRenameSpecImpl(node)
            PicatTokenTypes.CASE_ARMS -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseArmsImpl(node)
            PicatTokenTypes.CASE_ARM -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseArmImpl(node)
            PicatTokenTypes.CATCH_CLAUSES -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCatchClausesImpl(node)
            PicatTokenTypes.CATCH_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCatchClauseImpl(node)
            PicatTokenTypes.PROCEDURE_CALL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatProcedureCallImpl(node)

            // Commenting out known missing Impl classes for now
            // PicatTokenTypes.MAP -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapImpl(node)
            // PicatTokenTypes.MAP_ENTRIES -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapEntriesImpl(node)
            // PicatTokenTypes.MAP_ENTRY -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapEntryImpl(node)
            // PicatTokenTypes.TUPLE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTupleImpl(node)
            // PicatTokenTypes.TUPLE_ITEMS -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTupleItemsImpl(node)
            // PicatTokenTypes.ATOM_NO_ARGS -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAtomNoArgsImpl(node)
            // PicatTokenTypes.RULE_OPERATOR -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRuleOperatorImpl(node)
            // PicatTokenTypes.UNARY_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUnaryExpressionImpl(node)
            // PicatTokenTypes.FOREACH_GENERATORS -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatForeachGeneratorsImpl(node)
            // PicatTokenTypes.FOREACH_GENERATOR -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatForeachGeneratorImpl(node)
            // PicatTokenTypes.ELSEIF_CLAUSE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatElseifClauseImpl(node)
            // PicatTokenTypes.PATTERN -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPatternImpl(node)
            // PicatTokenTypes.STRUCTURE_PATTERN -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatStructurePatternImpl(node)
            // PicatTokenTypes.LIST_PATTERN -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListPatternImpl(node)
            // PicatTokenTypes.TUPLE_PATTERN -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTuplePatternImpl(node)
            // PicatTokenTypes.PATTERN_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPatternListImpl(node)
            // PicatTokenTypes.FUNCTION_CALL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionCallImpl(node)
            // PicatTokenTypes.LIST_COMPREHENSION_GOAL -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListComprehensionGoalImpl(node)
            // PicatTokenTypes.DOLLAR_TERM_CONSTRUCTOR -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatDollarTermConstructorImpl(node)
            // PicatTokenTypes.INDEX_ACCESS_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexAccessExpressionImpl(node)
            // PicatTokenTypes.AS_PATTERN_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAsPatternExpressionImpl(node)
            // PicatTokenTypes.LAMBDA_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatLambdaExpressionImpl(node)
            // PicatTokenTypes.VARIABLE_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatVariableListImpl(node)
            // PicatTokenTypes.TERM_CONSTRUCTOR_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTermConstructorExpressionImpl(node)
            // PicatTokenTypes.LIST_COMPREHENSION_EXPRESSION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListComprehensionExpressionImpl(node)
            // PicatTokenTypes.TABLE_MODE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTableModeImpl(node)
            // PicatTokenTypes.INDEX_MODE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexModeImpl(node)
            // PicatTokenTypes.HEAD_REFERENCE_LIST -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadReferenceListImpl(node)
            // PicatTokenTypes.HEAD_REFERENCE -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadReferenceImpl(node)
            // PicatTokenTypes.INDEXING_DETAILS -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexingDetailsImpl(node)
            // PicatTokenTypes.ACTOR_DEFINITION -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorDefinitionImpl(node)
            // PicatTokenTypes.ACTOR_NAME -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorNameImpl(node)
            // PicatTokenTypes.ACTOR_MEMBER -> com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorMemberImpl(node)
            else -> throw IllegalArgumentException("Unexpected structure element type: $type")
        }
    }

    /**
     * Checks if the element type is a literal-related element.
     */
    fun isLiteralElement(type: IElementType): Boolean {
        return type == PicatTokenTypes.VARIABLE || // LITERAL and ATOM are not ElementTypes for specific rules
               type == PicatTokenTypes.IDENTIFIER ||
               type == PicatTokenTypes.MODULE_NAME
    }

    /**
     * Creates a literal-related PSI element.
     */
    fun createLiteralElement(node: ASTNode): PsiElement {
        return when (val type = node.elementType) {
            // PicatTokenTypes.LITERAL -> PicatLiteralImpl(node) // LITERAL is not an ElementType here
            PicatTokenTypes.VARIABLE -> PicatVariableImpl(node)
            // PicatTokenTypes.ATOM -> PicatAtomImpl(node) // ATOM is not an ElementType here, IDENTIFIER or QUOTED_ATOM are tokens
            PicatTokenTypes.IDENTIFIER -> PicatIdentifierImpl(node) // This is a token, not usually a complex PSI element by itself unless wrapped
            PicatTokenTypes.MODULE_NAME -> PicatModuleNameImpl(node) // MODULE_NAME is an ElementType that wraps an atom
            else -> throw IllegalArgumentException("Unexpected literal element type: $type")
        }
    }
}
