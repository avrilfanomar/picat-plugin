package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexerAdapter // New import
// import com.github.avrilfanomar.picatplugin.language.lexer.PicatLexer // Remove this
import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes // Added import
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActionRuleImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorDefinitionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorMemberImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatActorNameImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatArgumentListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatArithmeticComparisonImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAsPatternExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAssignmentImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatAtomNoArgsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatBodyImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatBreakStmtImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseArmImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseArmsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCaseExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCatchClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCatchClausesImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatComparisonImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCompilationDirectiveImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatContinueStmtImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatCutGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatDirectiveImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatDollarTermConstructorImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatElseifClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatEndModuleDeclarationImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportSpecImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExportStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFailGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFalseGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileSpecImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatForeachGeneratorImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatForeachGeneratorsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatForeachLoopImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionBodyImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionCallImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionFactImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFunctionRuleImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadReferenceImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatHeadReferenceListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIfThenElseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportItemImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatImportStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIncludeStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexAccessExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexModeImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatIndexingDetailsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatLambdaExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListComprehensionExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListComprehensionGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListElementsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatListPatternImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatLoopWhileStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapEntriesImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapEntryImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatMapImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatModuleDeclarationImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatModuleNameImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatNegationImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPassGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPatternImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPatternListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateClauseImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateFactImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatPredicateRuleImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatProcedureCallImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatQualifiedAtomImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRenameListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRenameSpecImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatReturnStmtImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatRuleOperatorImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatStructureImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatStructurePatternImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTableModeImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTermConstructorExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatThrowStmtImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTrueGoalImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTryCatchImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTupleImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTupleItemsImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatTuplePatternImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUnaryExpressionImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUnificationImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatUsingStatementImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatVariableListImpl
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatWhileLoopImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

/**
 * Parser definition for Picat language.
 * Connects the lexer, parser, and PSI elements.
 *
 * Note: PicatParser and PicatTypes will be generated by the Grammar-Kit plugin
 * based on the BNF grammar file. This is a placeholder implementation.
 */
class PicatParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer = PicatLexerAdapter()

    override fun createParser(project: Project): PsiParser = PicatParser()

    override fun getFileNodeType(): IFileElementType = FILE

    // Updated to directly use PicatTokenTypes for whitespace
    override fun getWhitespaceTokens(): TokenSet = PicatTokenTypes.WHITESPACES

    override fun getCommentTokens(): TokenSet = PicatTokenTypes.COMMENTS

    override fun getStringLiteralElements(): TokenSet = PicatTokenTypes.STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement {
        val type = node.elementType
        return when (type) {
            PicatTokenTypes.PICAT_FILE -> PicatFileImpl(node)
            // Module-related
            PicatTokenTypes.MODULE_NAME -> PicatModuleNameImpl(node)
            PicatTokenTypes.MODULE_DECLARATION -> PicatModuleDeclarationImpl(node)
            PicatTokenTypes.END_MODULE_DECLARATION -> PicatEndModuleDeclarationImpl(node)
            // Import/Export/Include/Using statements
            PicatTokenTypes.IMPORT_STATEMENT -> PicatImportStatementImpl(node)
            PicatTokenTypes.EXPORT_STATEMENT -> PicatExportStatementImpl(node)
            PicatTokenTypes.INCLUDE_STATEMENT -> PicatIncludeStatementImpl(node)
            PicatTokenTypes.USING_STATEMENT -> PicatUsingStatementImpl(node)
            // Function/Rule body and components
            PicatTokenTypes.FUNCTION_BODY -> PicatFunctionBodyImpl(node)
            PicatTokenTypes.ARGUMENT_LIST -> PicatArgumentListImpl(node)
            PicatTokenTypes.DIRECTIVE -> PicatDirectiveImpl(node)
            PicatTokenTypes.BODY -> PicatBodyImpl(node)
            PicatTokenTypes.HEAD -> PicatHeadImpl(node)
            PicatTokenTypes.GOAL -> PicatGoalImpl(node)
            // Expressions
            PicatTokenTypes.EXPRESSION -> PicatExpressionImpl(node)
            PicatTokenTypes.STRUCTURE -> PicatStructureImpl(node)
            PicatTokenTypes.LIST_EXPRESSION -> PicatListExpressionImpl(node)
            PicatTokenTypes.LIST_ELEMENTS -> PicatListElementsImpl(node)
            PicatTokenTypes.MAP -> PicatMapImpl(node)
            PicatTokenTypes.MAP_ENTRIES -> PicatMapEntriesImpl(node)
            PicatTokenTypes.MAP_ENTRY -> PicatMapEntryImpl(node)
            PicatTokenTypes.TUPLE -> PicatTupleImpl(node)
            PicatTokenTypes.UNARY_EXPRESSION -> PicatUnaryExpressionImpl(node)
            PicatTokenTypes.FUNCTION_CALL -> PicatFunctionCallImpl(node)
            PicatTokenTypes.ATOM_NO_ARGS -> PicatAtomNoArgsImpl(node)
            PicatTokenTypes.QUALIFIED_ATOM -> PicatQualifiedAtomImpl(node)
            PicatTokenTypes.DOLLAR_TERM_CONSTRUCTOR -> PicatDollarTermConstructorImpl(node)
            PicatTokenTypes.INDEX_ACCESS_EXPRESSION -> PicatIndexAccessExpressionImpl(node)
            PicatTokenTypes.AS_PATTERN_EXPRESSION -> PicatAsPatternExpressionImpl(node)
            PicatTokenTypes.LAMBDA_EXPRESSION -> PicatLambdaExpressionImpl(node)
            PicatTokenTypes.TERM_CONSTRUCTOR_EXPRESSION -> PicatTermConstructorExpressionImpl(node)
            PicatTokenTypes.LIST_COMPREHENSION_EXPRESSION -> PicatListComprehensionExpressionImpl(node)
            // Statements and Control Flow
            PicatTokenTypes.STATEMENT -> PicatStatementImpl(node)
            PicatTokenTypes.IF_THEN_ELSE -> PicatIfThenElseImpl(node)
            PicatTokenTypes.FOREACH_LOOP -> PicatForeachLoopImpl(node)
            PicatTokenTypes.WHILE_LOOP -> PicatWhileLoopImpl(node)
            PicatTokenTypes.TRY_CATCH -> PicatTryCatchImpl(node)
            PicatTokenTypes.CASE_EXPRESSION -> PicatCaseExpressionImpl(node)
            PicatTokenTypes.CASE_ARMS -> PicatCaseArmsImpl(node)
            PicatTokenTypes.CASE_ARM -> PicatCaseArmImpl(node)
            PicatTokenTypes.CATCH_CLAUSES -> PicatCatchClausesImpl(node)
            PicatTokenTypes.CATCH_CLAUSE -> PicatCatchClauseImpl(node)
            PicatTokenTypes.ELSEIF_CLAUSE -> PicatElseifClauseImpl(node)
            PicatTokenTypes.ASSIGNMENT -> PicatAssignmentImpl(node)
            PicatTokenTypes.UNIFICATION -> PicatUnificationImpl(node)
            PicatTokenTypes.COMPARISON -> PicatComparisonImpl(node)
            PicatTokenTypes.ARITHMETIC_COMPARISON -> PicatArithmeticComparisonImpl(node)
            PicatTokenTypes.NEGATION -> PicatNegationImpl(node)
            PicatTokenTypes.FAIL_GOAL -> PicatFailGoalImpl(node)
            PicatTokenTypes.PASS_GOAL -> PicatPassGoalImpl(node)
            PicatTokenTypes.TRUE_GOAL -> PicatTrueGoalImpl(node)
            PicatTokenTypes.FALSE_GOAL -> PicatFalseGoalImpl(node)
            PicatTokenTypes.CUT_GOAL -> PicatCutGoalImpl(node)
            PicatTokenTypes.RETURN_STMT -> PicatReturnStmtImpl(node)
            PicatTokenTypes.CONTINUE_STMT -> PicatContinueStmtImpl(node)
            PicatTokenTypes.BREAK_STMT -> PicatBreakStmtImpl(node)
            PicatTokenTypes.THROW_STMT -> PicatThrowStmtImpl(node)
            PicatTokenTypes.PROCEDURE_CALL -> PicatProcedureCallImpl(node)
            PicatTokenTypes.LIST_COMPREHENSION_GOAL -> PicatListComprehensionGoalImpl(node)
            PicatTokenTypes.LOOP_WHILE_STATEMENT -> PicatLoopWhileStatementImpl(node)
            // Patterns
            PicatTokenTypes.PATTERN -> PicatPatternImpl(node)
            PicatTokenTypes.STRUCTURE_PATTERN -> PicatStructurePatternImpl(node)
            PicatTokenTypes.LIST_PATTERN -> PicatListPatternImpl(node)
            PicatTokenTypes.TUPLE_PATTERN -> PicatTuplePatternImpl(node)
            PicatTokenTypes.PATTERN_LIST -> PicatPatternListImpl(node)
            // Rules and Facts
            PicatTokenTypes.RULE_OPERATOR -> PicatRuleOperatorImpl(node)
            PicatTokenTypes.PREDICATE_RULE -> PicatPredicateRuleImpl(node)
            PicatTokenTypes.PREDICATE_FACT -> PicatPredicateFactImpl(node)
            PicatTokenTypes.FUNCTION_RULE -> PicatFunctionRuleImpl(node)
            PicatTokenTypes.FUNCTION_FACT -> PicatFunctionFactImpl(node)
            PicatTokenTypes.ACTION_RULE -> PicatActionRuleImpl(node)
            PicatTokenTypes.PREDICATE_CLAUSE -> PicatPredicateClauseImpl(node)
            PicatTokenTypes.FUNCTION_CLAUSE -> PicatFunctionClauseImpl(node)
            // Actor Model
            PicatTokenTypes.ACTOR_DEFINITION -> PicatActorDefinitionImpl(node)
            PicatTokenTypes.ACTOR_NAME -> PicatActorNameImpl(node)
            PicatTokenTypes.ACTOR_MEMBER -> PicatActorMemberImpl(node)
            // Import/Export specifics
            PicatTokenTypes.EXPORT_CLAUSE -> PicatExportClauseImpl(node)
            PicatTokenTypes.IMPORT_CLAUSE -> PicatImportClauseImpl(node)
            PicatTokenTypes.IMPORT_LIST -> PicatImportListImpl(node)
            PicatTokenTypes.EXPORT_LIST -> PicatExportListImpl(node)
            PicatTokenTypes.RENAME_LIST -> PicatRenameListImpl(node)
            PicatTokenTypes.IMPORT_ITEM -> PicatImportItemImpl(node)
            PicatTokenTypes.FILE_SPEC -> PicatFileSpecImpl(node)
            PicatTokenTypes.EXPORT_SPEC -> PicatExportSpecImpl(node)
            PicatTokenTypes.RENAME_SPEC -> PicatRenameSpecImpl(node)
            // Loops and Comprehensions specifics
            PicatTokenTypes.FOREACH_GENERATORS -> PicatForeachGeneratorsImpl(node)
            PicatTokenTypes.FOREACH_GENERATOR -> PicatForeachGeneratorImpl(node)
            PicatTokenTypes.VARIABLE_LIST -> PicatVariableListImpl(node)
            PicatTokenTypes.TUPLE_ITEMS -> PicatTupleItemsImpl(node)
            // Directives and Modes
            PicatTokenTypes.COMPILATION_DIRECTIVE -> PicatCompilationDirectiveImpl(node)
            PicatTokenTypes.TABLE_MODE -> PicatTableModeImpl(node)
            PicatTokenTypes.INDEX_MODE -> PicatIndexModeImpl(node)
            PicatTokenTypes.HEAD_REFERENCE_LIST -> PicatHeadReferenceListImpl(node)
            PicatTokenTypes.HEAD_REFERENCE -> PicatHeadReferenceImpl(node)
            PicatTokenTypes.INDEXING_DETAILS -> PicatIndexingDetailsImpl(node)
            // Default
            else -> com.intellij.extapi.psi.ASTWrapperPsiElement(node)
        }
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = PicatFile(viewProvider)
}

val FILE = IFileElementType(PicatLanguage)
