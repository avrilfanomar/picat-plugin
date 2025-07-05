// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.avrilfanomar.picatplugin.language.psi.impl.*;

public interface PicatTokenTypes {

  IElementType ADDITIVE_EXPRESSION = new PicatElementType("ADDITIVE_EXPRESSION");
  IElementType ADDITIVE_EXPRESSION_REST = new PicatElementType("ADDITIVE_EXPRESSION_REST");
  IElementType ARGUMENT_LIST = new PicatElementType("ARGUMENT_LIST");
  IElementType ASSIGNABLE_EXPRESSION = new PicatElementType("ASSIGNABLE_EXPRESSION");
  IElementType ASSIGNMENT = new PicatElementType("ASSIGNMENT");
  IElementType AS_PATTERN = new PicatElementType("AS_PATTERN");
  IElementType AS_PATTERN_EXPRESSION = new PicatElementType("AS_PATTERN_EXPRESSION");
  IElementType ATOM = new PicatElementType("ATOM");
  IElementType ATOM_EXPRESSION = new PicatElementType("ATOM_EXPRESSION");
  IElementType ATOM_NO_ARGS = new PicatElementType("ATOM_NO_ARGS");
  IElementType BASE_EXPRESSION = new PicatElementType("BASE_EXPRESSION");
  IElementType BICONDITIONAL_EXPRESSION = new PicatElementType("BICONDITIONAL_EXPRESSION");
  IElementType BICONDITIONAL_EXPRESSION_REST = new PicatElementType("BICONDITIONAL_EXPRESSION_REST");
  IElementType BITWISE_AND_EXPRESSION = new PicatElementType("BITWISE_AND_EXPRESSION");
  IElementType BITWISE_AND_EXPRESSION_REST = new PicatElementType("BITWISE_AND_EXPRESSION_REST");
  IElementType BITWISE_OR_EXPRESSION = new PicatElementType("BITWISE_OR_EXPRESSION");
  IElementType BITWISE_OR_EXPRESSION_REST = new PicatElementType("BITWISE_OR_EXPRESSION_REST");
  IElementType BITWISE_XOR_EXPRESSION = new PicatElementType("BITWISE_XOR_EXPRESSION");
  IElementType BITWISE_XOR_EXPRESSION_REST = new PicatElementType("BITWISE_XOR_EXPRESSION_REST");
  IElementType BODY = new PicatElementType("BODY");
  IElementType BREAK_STMT = new PicatElementType("BREAK_STMT");
  IElementType CASE_ARM = new PicatElementType("CASE_ARM");
  IElementType CASE_ARMS = new PicatElementType("CASE_ARMS");
  IElementType CASE_EXPRESSION = new PicatElementType("CASE_EXPRESSION");
  IElementType CATCH_CLAUSE = new PicatElementType("CATCH_CLAUSE");
  IElementType CATCH_CLAUSES = new PicatElementType("CATCH_CLAUSES");
  IElementType COMPARISON = new PicatElementType("COMPARISON");
  IElementType COMPILATION_DIRECTIVE = new PicatElementType("COMPILATION_DIRECTIVE");
  IElementType CONCATENATION_EXPRESSION = new PicatElementType("CONCATENATION_EXPRESSION");
  IElementType CONCATENATION_EXPRESSION_REST = new PicatElementType("CONCATENATION_EXPRESSION_REST");
  IElementType CONDITIONAL_EXPRESSION = new PicatElementType("CONDITIONAL_EXPRESSION");
  IElementType CONTINUE_STMT = new PicatElementType("CONTINUE_STMT");
  IElementType CUT_GOAL = new PicatElementType("CUT_GOAL");
  IElementType DOLLAR_TERM_CONSTRUCTOR = new PicatElementType("DOLLAR_TERM_CONSTRUCTOR");
  IElementType ELSEIF_CLAUSE = new PicatElementType("ELSEIF_CLAUSE");
  IElementType ELSEIF_CLAUSES = new PicatElementType("ELSEIF_CLAUSES");
  IElementType EQUALITY_EXPRESSION = new PicatElementType("EQUALITY_EXPRESSION");
  IElementType EQUALITY_EXPRESSION_REST = new PicatElementType("EQUALITY_EXPRESSION_REST");
  IElementType EXPORT_LIST = new PicatElementType("EXPORT_LIST");
  IElementType EXPORT_SPEC = new PicatElementType("EXPORT_SPEC");
  IElementType EXPORT_STATEMENT = new PicatElementType("EXPORT_STATEMENT");
  IElementType EXPRESSION = new PicatElementType("EXPRESSION");
  IElementType FAIL_GOAL = new PicatElementType("FAIL_GOAL");
  IElementType FALSE_GOAL = new PicatElementType("FALSE_GOAL");
  IElementType FIELD_ACCESS_SUFFIX = new PicatElementType("FIELD_ACCESS_SUFFIX");
  IElementType FILE_SPEC = new PicatElementType("FILE_SPEC");
  IElementType FOREACH_GENERATOR = new PicatElementType("FOREACH_GENERATOR");
  IElementType FOREACH_GENERATORS = new PicatElementType("FOREACH_GENERATORS");
  IElementType FOREACH_LOOP = new PicatElementType("FOREACH_LOOP");
  IElementType FUNCTION_BODY = new PicatElementType("FUNCTION_BODY");
  IElementType FUNCTION_CALL_SUFFIX = new PicatElementType("FUNCTION_CALL_SUFFIX");
  IElementType FUNCTION_CLAUSE = new PicatElementType("FUNCTION_CLAUSE");
  IElementType FUNCTION_FACT = new PicatElementType("FUNCTION_FACT");
  IElementType FUNCTION_RULE = new PicatElementType("FUNCTION_RULE");
  IElementType GENERAL_DIRECTIVE = new PicatElementType("GENERAL_DIRECTIVE");
  IElementType GOAL = new PicatElementType("GOAL");
  IElementType GOAL_SEPARATOR = new PicatElementType("GOAL_SEPARATOR");
  IElementType HEAD = new PicatElementType("HEAD");
  IElementType HEAD_ARGS = new PicatElementType("HEAD_ARGS");
  IElementType HEAD_REFERENCE = new PicatElementType("HEAD_REFERENCE");
  IElementType HEAD_REFERENCE_LIST = new PicatElementType("HEAD_REFERENCE_LIST");
  IElementType IF_THEN_ELSE = new PicatElementType("IF_THEN_ELSE");
  IElementType IMPORT_ITEM = new PicatElementType("IMPORT_ITEM");
  IElementType IMPORT_LIST = new PicatElementType("IMPORT_LIST");
  IElementType IMPORT_STATEMENT = new PicatElementType("IMPORT_STATEMENT");
  IElementType INCLUDE_STATEMENT = new PicatElementType("INCLUDE_STATEMENT");
  IElementType INDEXING_DETAILS = new PicatElementType("INDEXING_DETAILS");
  IElementType INDEX_ACCESS_SUFFIX = new PicatElementType("INDEX_ACCESS_SUFFIX");
  IElementType INDEX_MODE = new PicatElementType("INDEX_MODE");
  IElementType LAMBDA_EXPRESSION = new PicatElementType("LAMBDA_EXPRESSION");
  IElementType LIST_COMPREHENSION_EXPRESSION = new PicatElementType("LIST_COMPREHENSION_EXPRESSION");
  IElementType LIST_COMPREHENSION_GOAL = new PicatElementType("LIST_COMPREHENSION_GOAL");
  IElementType LIST_EXPRESSION = new PicatElementType("LIST_EXPRESSION");
  IElementType LIST_ITEMS = new PicatElementType("LIST_ITEMS");
  IElementType LIST_PATTERN = new PicatElementType("LIST_PATTERN");
  IElementType LOGICAL_AND_EXPRESSION = new PicatElementType("LOGICAL_AND_EXPRESSION");
  IElementType LOGICAL_AND_EXPRESSION_REST = new PicatElementType("LOGICAL_AND_EXPRESSION_REST");
  IElementType LOGICAL_OR_EXPRESSION = new PicatElementType("LOGICAL_OR_EXPRESSION");
  IElementType LOGICAL_OR_EXPRESSION_REST = new PicatElementType("LOGICAL_OR_EXPRESSION_REST");
  IElementType LOOP_WHILE_STATEMENT = new PicatElementType("LOOP_WHILE_STATEMENT");
  IElementType MAP = new PicatElementType("MAP");
  IElementType MAP_ENTRIES = new PicatElementType("MAP_ENTRIES");
  IElementType MAP_ENTRY = new PicatElementType("MAP_ENTRY");
  IElementType MODULE_DECLARATION = new PicatElementType("MODULE_DECLARATION");
  IElementType MODULE_NAME = new PicatElementType("MODULE_NAME");
  IElementType MULTIPLICATIVE_EXPRESSION = new PicatElementType("MULTIPLICATIVE_EXPRESSION");
  IElementType MULTIPLICATIVE_EXPRESSION_REST = new PicatElementType("MULTIPLICATIVE_EXPRESSION_REST");
  IElementType NEGATION = new PicatElementType("NEGATION");
  IElementType NUMBER = new PicatElementType("NUMBER");
  IElementType PARENTHESIZED_EXPRESSION = new PicatElementType("PARENTHESIZED_EXPRESSION");
  IElementType PASS_GOAL = new PicatElementType("PASS_GOAL");
  IElementType PATTERN = new PicatElementType("PATTERN");
  IElementType PATTERN_LIST = new PicatElementType("PATTERN_LIST");
  IElementType POSTFIX_EXPRESSION = new PicatElementType("POSTFIX_EXPRESSION");
  IElementType POWER_EXPRESSION = new PicatElementType("POWER_EXPRESSION");
  IElementType PREDICATE_CLAUSE = new PicatElementType("PREDICATE_CLAUSE");
  IElementType PREDICATE_FACT = new PicatElementType("PREDICATE_FACT");
  IElementType PREDICATE_RULE = new PicatElementType("PREDICATE_RULE");
  IElementType PREDICATE_SIGNATURE = new PicatElementType("PREDICATE_SIGNATURE");
  IElementType PROCEDURE_CALL = new PicatElementType("PROCEDURE_CALL");
  IElementType QUALIFIED_ATOM = new PicatElementType("QUALIFIED_ATOM");
  IElementType RANGE_OPERAND = new PicatElementType("RANGE_OPERAND");
  IElementType RELATIONAL_EXPRESSION = new PicatElementType("RELATIONAL_EXPRESSION");
  IElementType RELATIONAL_EXPRESSION_REST = new PicatElementType("RELATIONAL_EXPRESSION_REST");
  IElementType RENAME_LIST = new PicatElementType("RENAME_LIST");
  IElementType RENAME_SPEC = new PicatElementType("RENAME_SPEC");
  IElementType RETURN_STMT = new PicatElementType("RETURN_STMT");
  IElementType RULE_OPERATOR = new PicatElementType("RULE_OPERATOR");
  IElementType SHIFT_EXPRESSION = new PicatElementType("SHIFT_EXPRESSION");
  IElementType SHIFT_EXPRESSION_REST = new PicatElementType("SHIFT_EXPRESSION_REST");
  IElementType SIMPLE_NUMBER_RANGE = new PicatElementType("SIMPLE_NUMBER_RANGE");
  IElementType STRUCTURE = new PicatElementType("STRUCTURE");
  IElementType STRUCTURE_PATTERN = new PicatElementType("STRUCTURE_PATTERN");
  IElementType TABLE_MODE = new PicatElementType("TABLE_MODE");
  IElementType TERM_CONSTRUCTOR_EXPRESSION = new PicatElementType("TERM_CONSTRUCTOR_EXPRESSION");
  IElementType THROW_STMT = new PicatElementType("THROW_STMT");
  IElementType TRUE_GOAL = new PicatElementType("TRUE_GOAL");
  IElementType TRY_CATCH = new PicatElementType("TRY_CATCH");
  IElementType TUPLE = new PicatElementType("TUPLE");
  IElementType TUPLE_ITEMS = new PicatElementType("TUPLE_ITEMS");
  IElementType TUPLE_PATTERN = new PicatElementType("TUPLE_PATTERN");
  IElementType TYPE_ANNOTATION = new PicatElementType("TYPE_ANNOTATION");
  IElementType UNARY_EXPRESSION = new PicatElementType("UNARY_EXPRESSION");
  IElementType UNARY_OPERATOR = new PicatElementType("UNARY_OPERATOR");
  IElementType UNIFICATION = new PicatElementType("UNIFICATION");
  IElementType USING_STATEMENT = new PicatElementType("USING_STATEMENT");
  IElementType VARIABLE_LIST = new PicatElementType("VARIABLE_LIST");
  IElementType WHILE_LOOP = new PicatElementType("WHILE_LOOP");

  IElementType AMPERSAND = new PicatTokenType("&");
  IElementType AND_KEYWORD = new PicatTokenType("and");
  IElementType ARROW_OP = new PicatTokenType("=>");
  IElementType ASSIGN_OP = new PicatTokenType(":=");
  IElementType AT = new PicatTokenType("@");
  IElementType BACKSLASH = new PicatTokenType("\\\\");
  IElementType BACKTRACKABLE_ARROW_OP = new PicatTokenType("?=>");
  IElementType BACKTRACKABLE_BICONDITIONAL_OP = new PicatTokenType("?<=>");
  IElementType BICONDITIONAL_OP = new PicatTokenType("<=>");
  IElementType BINARY_INTEGER = new PicatTokenType("BINARY_INTEGER");
  IElementType BREAK_KEYWORD = new PicatTokenType("break");
  IElementType CARET = new PicatTokenType("^");
  IElementType CASE_KEYWORD = new PicatTokenType("case");
  IElementType CATCH_KEYWORD = new PicatTokenType("catch");
  IElementType COLON = new PicatTokenType(":");
  IElementType COMMA = new PicatTokenType(",");
  IElementType COMMENT = new PicatTokenType("COMMENT");
  IElementType CONCAT_OP = new PicatTokenType("++");
  IElementType CONTINUE_KEYWORD = new PicatTokenType("continue");
  IElementType CUT = new PicatTokenType("!");
  IElementType DIVIDE = new PicatTokenType("/");
  IElementType DIV_KEYWORD = new PicatTokenType("div");
  IElementType DOLLAR = new PicatTokenType("$");
  IElementType DOT = new PicatTokenType(".");
  IElementType DOUBLE_COLON_OP = new PicatTokenType("::");
  IElementType DO_KEYWORD = new PicatTokenType("do");
  IElementType ELSEIF_KEYWORD = new PicatTokenType("elseif");
  IElementType ELSE_KEYWORD = new PicatTokenType("else");
  IElementType END_KEYWORD = new PicatTokenType("end");
  IElementType END_MODULE_KEYWORD = new PicatTokenType("end_module");
  IElementType EOF = new PicatTokenType("EOF");
  IElementType EQUAL = new PicatTokenType("=");
  IElementType EXPORT_KEYWORD = new PicatTokenType("export");
  IElementType FAIL_KEYWORD = new PicatTokenType("fail");
  IElementType FALSE_KEYWORD = new PicatTokenType("false");
  IElementType FIELD_ACCESS = new PicatTokenType("FIELD_ACCESS");
  IElementType FINALLY_KEYWORD = new PicatTokenType("finally");
  IElementType FLOAT = new PicatTokenType("FLOAT");
  IElementType FOREACH_KEYWORD = new PicatTokenType("foreach");
  IElementType GREATER = new PicatTokenType(">");
  IElementType GREATER_EQUAL = new PicatTokenType(">=");
  IElementType HASH_AND_OP = new PicatTokenType("#/\\\\");
  IElementType HASH_ARROW_OP = new PicatTokenType("#=>");
  IElementType HASH_BICONDITIONAL_OP = new PicatTokenType("#<=>");
  IElementType HASH_CARET_OP = new PicatTokenType("#^");
  IElementType HASH_EQUAL_OP = new PicatTokenType("#=");
  IElementType HASH_LESS_EQUAL_OP = new PicatTokenType("#=<");
  IElementType HASH_NOT_EQUAL_OP = new PicatTokenType("#!=");
  IElementType HASH_OR_OP = new PicatTokenType("#\\\\/");
  IElementType HASH_TILDE_OP = new PicatTokenType("#~");
  IElementType HEX_INTEGER = new PicatTokenType("HEX_INTEGER");
  IElementType IDENTICAL = new PicatTokenType("=:=");
  IElementType IDENTIFIER = new PicatTokenType("IDENTIFIER");
  IElementType IF_KEYWORD = new PicatTokenType("if");
  IElementType IMPORT_KEYWORD = new PicatTokenType("import");
  IElementType INCLUDE_KEYWORD = new PicatTokenType("include");
  IElementType INDEX_KEYWORD = new PicatTokenType("index");
  IElementType INTEGER = new PicatTokenType("INTEGER");
  IElementType INT_DIVIDE = new PicatTokenType("//");
  IElementType IN_KEYWORD = new PicatTokenType("in");
  IElementType IS_KEYWORD = new PicatTokenType("is");
  IElementType LBRACE = new PicatTokenType("{");
  IElementType LBRACKET = new PicatTokenType("[");
  IElementType LESS = new PicatTokenType("<");
  IElementType LESS_EQUAL = new PicatTokenType("<=");
  IElementType LOOP_KEYWORD = new PicatTokenType("loop");
  IElementType LPAR = new PicatTokenType("(");
  IElementType MINUS = new PicatTokenType("-");
  IElementType MODULE_KEYWORD = new PicatTokenType("module");
  IElementType MOD_KEYWORD = new PicatTokenType("mod");
  IElementType MULTIPLY = new PicatTokenType("*");
  IElementType MULTI_LINE_COMMENT = new PicatTokenType("MULTI_LINE_COMMENT");
  IElementType NOT_EQUAL = new PicatTokenType("!=");
  IElementType NOT_IDENTICAL = new PicatTokenType("=\\\\=");
  IElementType NOT_KEYWORD = new PicatTokenType("not");
  IElementType OCTAL_INTEGER = new PicatTokenType("OCTAL_INTEGER");
  IElementType OF_KEYWORD = new PicatTokenType("of");
  IElementType OR_KEYWORD = new PicatTokenType("or");
  IElementType PASS_KEYWORD = new PicatTokenType("pass");
  IElementType PIPE = new PicatTokenType("|");
  IElementType PLUS = new PicatTokenType("+");
  IElementType POWER = new PicatTokenType("**");
  IElementType PRIVATE_KEYWORD = new PicatTokenType("private");
  IElementType QUESTION_MARK = new PicatTokenType("?");
  IElementType QUOTED_ATOM = new PicatTokenType("QUOTED_ATOM");
  IElementType RANGE_OP = new PicatTokenType("..");
  IElementType RBRACE = new PicatTokenType("}");
  IElementType RBRACKET = new PicatTokenType("]");
  IElementType REM_KEYWORD = new PicatTokenType("rem");
  IElementType RETURN_KEYWORD = new PicatTokenType("return");
  IElementType RPAR = new PicatTokenType(")");
  IElementType RULE_OP = new PicatTokenType(":-");
  IElementType SEMICOLON = new PicatTokenType(";");
  IElementType SHIFT_LEFT = new PicatTokenType("<<");
  IElementType SHIFT_RIGHT = new PicatTokenType(">>");
  IElementType SHIFT_RIGHT_TRIPLE_OP = new PicatTokenType(">>>");
  IElementType STRING = new PicatTokenType("STRING");
  IElementType TABLE_KEYWORD = new PicatTokenType("table");
  IElementType THEN_KEYWORD = new PicatTokenType("then");
  IElementType THROW_KEYWORD = new PicatTokenType("throw");
  IElementType TRUE_KEYWORD = new PicatTokenType("true");
  IElementType TRY_KEYWORD = new PicatTokenType("try");
  IElementType USING_KEYWORD = new PicatTokenType("using");
  IElementType VARIABLE = new PicatTokenType("variable");
  IElementType WHILE_KEYWORD = new PicatTokenType("while");
  IElementType XOR_KEYWORD = new PicatTokenType("xor");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDITIVE_EXPRESSION) {
        return new PicatAdditiveExpressionImpl(node);
      }
      else if (type == ADDITIVE_EXPRESSION_REST) {
        return new PicatAdditiveExpressionRestImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new PicatArgumentListImpl(node);
      }
      else if (type == ASSIGNABLE_EXPRESSION) {
        return new PicatAssignableExpressionImpl(node);
      }
      else if (type == ASSIGNMENT) {
        return new PicatAssignmentImpl(node);
      }
      else if (type == AS_PATTERN) {
        return new PicatAsPatternImpl(node);
      }
      else if (type == AS_PATTERN_EXPRESSION) {
        return new PicatAsPatternExpressionImpl(node);
      }
      else if (type == ATOM) {
        return new PicatAtomImpl(node);
      }
      else if (type == ATOM_EXPRESSION) {
        return new PicatAtomExpressionImpl(node);
      }
      else if (type == ATOM_NO_ARGS) {
        return new PicatAtomNoArgsImpl(node);
      }
      else if (type == BASE_EXPRESSION) {
        return new PicatBaseExpressionImpl(node);
      }
      else if (type == BICONDITIONAL_EXPRESSION) {
        return new PicatBiconditionalExpressionImpl(node);
      }
      else if (type == BICONDITIONAL_EXPRESSION_REST) {
        return new PicatBiconditionalExpressionRestImpl(node);
      }
      else if (type == BITWISE_AND_EXPRESSION) {
        return new PicatBitwiseAndExpressionImpl(node);
      }
      else if (type == BITWISE_AND_EXPRESSION_REST) {
        return new PicatBitwiseAndExpressionRestImpl(node);
      }
      else if (type == BITWISE_OR_EXPRESSION) {
        return new PicatBitwiseOrExpressionImpl(node);
      }
      else if (type == BITWISE_OR_EXPRESSION_REST) {
        return new PicatBitwiseOrExpressionRestImpl(node);
      }
      else if (type == BITWISE_XOR_EXPRESSION) {
        return new PicatBitwiseXorExpressionImpl(node);
      }
      else if (type == BITWISE_XOR_EXPRESSION_REST) {
        return new PicatBitwiseXorExpressionRestImpl(node);
      }
      else if (type == BODY) {
        return new PicatBodyImpl(node);
      }
      else if (type == BREAK_STMT) {
        return new PicatBreakStmtImpl(node);
      }
      else if (type == CASE_ARM) {
        return new PicatCaseArmImpl(node);
      }
      else if (type == CASE_ARMS) {
        return new PicatCaseArmsImpl(node);
      }
      else if (type == CASE_EXPRESSION) {
        return new PicatCaseExpressionImpl(node);
      }
      else if (type == CATCH_CLAUSE) {
        return new PicatCatchClauseImpl(node);
      }
      else if (type == CATCH_CLAUSES) {
        return new PicatCatchClausesImpl(node);
      }
      else if (type == COMPARISON) {
        return new PicatComparisonImpl(node);
      }
      else if (type == COMPILATION_DIRECTIVE) {
        return new PicatCompilationDirectiveImpl(node);
      }
      else if (type == CONCATENATION_EXPRESSION) {
        return new PicatConcatenationExpressionImpl(node);
      }
      else if (type == CONCATENATION_EXPRESSION_REST) {
        return new PicatConcatenationExpressionRestImpl(node);
      }
      else if (type == CONDITIONAL_EXPRESSION) {
        return new PicatConditionalExpressionImpl(node);
      }
      else if (type == CONTINUE_STMT) {
        return new PicatContinueStmtImpl(node);
      }
      else if (type == CUT_GOAL) {
        return new PicatCutGoalImpl(node);
      }
      else if (type == DOLLAR_TERM_CONSTRUCTOR) {
        return new PicatDollarTermConstructorImpl(node);
      }
      else if (type == ELSEIF_CLAUSE) {
        return new PicatElseifClauseImpl(node);
      }
      else if (type == ELSEIF_CLAUSES) {
        return new PicatElseifClausesImpl(node);
      }
      else if (type == EQUALITY_EXPRESSION) {
        return new PicatEqualityExpressionImpl(node);
      }
      else if (type == EQUALITY_EXPRESSION_REST) {
        return new PicatEqualityExpressionRestImpl(node);
      }
      else if (type == EXPORT_LIST) {
        return new PicatExportListImpl(node);
      }
      else if (type == EXPORT_SPEC) {
        return new PicatExportSpecImpl(node);
      }
      else if (type == EXPORT_STATEMENT) {
        return new PicatExportStatementImpl(node);
      }
      else if (type == EXPRESSION) {
        return new PicatExpressionImpl(node);
      }
      else if (type == FAIL_GOAL) {
        return new PicatFailGoalImpl(node);
      }
      else if (type == FALSE_GOAL) {
        return new PicatFalseGoalImpl(node);
      }
      else if (type == FIELD_ACCESS_SUFFIX) {
        return new PicatFieldAccessSuffixImpl(node);
      }
      else if (type == FILE_SPEC) {
        return new PicatFileSpecImpl(node);
      }
      else if (type == FOREACH_GENERATOR) {
        return new PicatForeachGeneratorImpl(node);
      }
      else if (type == FOREACH_GENERATORS) {
        return new PicatForeachGeneratorsImpl(node);
      }
      else if (type == FOREACH_LOOP) {
        return new PicatForeachLoopImpl(node);
      }
      else if (type == FUNCTION_BODY) {
        return new PicatFunctionBodyImpl(node);
      }
      else if (type == FUNCTION_CALL_SUFFIX) {
        return new PicatFunctionCallSuffixImpl(node);
      }
      else if (type == FUNCTION_CLAUSE) {
        return new PicatFunctionClauseImpl(node);
      }
      else if (type == FUNCTION_FACT) {
        return new PicatFunctionFactImpl(node);
      }
      else if (type == FUNCTION_RULE) {
        return new PicatFunctionRuleImpl(node);
      }
      else if (type == GENERAL_DIRECTIVE) {
        return new PicatGeneralDirectiveImpl(node);
      }
      else if (type == GOAL) {
        return new PicatGoalImpl(node);
      }
      else if (type == GOAL_SEPARATOR) {
        return new PicatGoalSeparatorImpl(node);
      }
      else if (type == HEAD) {
        return new PicatHeadImpl(node);
      }
      else if (type == HEAD_ARGS) {
        return new PicatHeadArgsImpl(node);
      }
      else if (type == HEAD_REFERENCE) {
        return new PicatHeadReferenceImpl(node);
      }
      else if (type == HEAD_REFERENCE_LIST) {
        return new PicatHeadReferenceListImpl(node);
      }
      else if (type == IF_THEN_ELSE) {
        return new PicatIfThenElseImpl(node);
      }
      else if (type == IMPORT_ITEM) {
        return new PicatImportItemImpl(node);
      }
      else if (type == IMPORT_LIST) {
        return new PicatImportListImpl(node);
      }
      else if (type == IMPORT_STATEMENT) {
        return new PicatImportStatementImpl(node);
      }
      else if (type == INCLUDE_STATEMENT) {
        return new PicatIncludeStatementImpl(node);
      }
      else if (type == INDEXING_DETAILS) {
        return new PicatIndexingDetailsImpl(node);
      }
      else if (type == INDEX_ACCESS_SUFFIX) {
        return new PicatIndexAccessSuffixImpl(node);
      }
      else if (type == INDEX_MODE) {
        return new PicatIndexModeImpl(node);
      }
      else if (type == LAMBDA_EXPRESSION) {
        return new PicatLambdaExpressionImpl(node);
      }
      else if (type == LIST_COMPREHENSION_EXPRESSION) {
        return new PicatListComprehensionExpressionImpl(node);
      }
      else if (type == LIST_COMPREHENSION_GOAL) {
        return new PicatListComprehensionGoalImpl(node);
      }
      else if (type == LIST_EXPRESSION) {
        return new PicatListExpressionImpl(node);
      }
      else if (type == LIST_ITEMS) {
        return new PicatListItemsImpl(node);
      }
      else if (type == LIST_PATTERN) {
        return new PicatListPatternImpl(node);
      }
      else if (type == LOGICAL_AND_EXPRESSION) {
        return new PicatLogicalAndExpressionImpl(node);
      }
      else if (type == LOGICAL_AND_EXPRESSION_REST) {
        return new PicatLogicalAndExpressionRestImpl(node);
      }
      else if (type == LOGICAL_OR_EXPRESSION) {
        return new PicatLogicalOrExpressionImpl(node);
      }
      else if (type == LOGICAL_OR_EXPRESSION_REST) {
        return new PicatLogicalOrExpressionRestImpl(node);
      }
      else if (type == LOOP_WHILE_STATEMENT) {
        return new PicatLoopWhileStatementImpl(node);
      }
      else if (type == MAP) {
        return new PicatMapImpl(node);
      }
      else if (type == MAP_ENTRIES) {
        return new PicatMapEntriesImpl(node);
      }
      else if (type == MAP_ENTRY) {
        return new PicatMapEntryImpl(node);
      }
      else if (type == MODULE_DECLARATION) {
        return new PicatModuleDeclarationImpl(node);
      }
      else if (type == MODULE_NAME) {
        return new PicatModuleNameImpl(node);
      }
      else if (type == MULTIPLICATIVE_EXPRESSION) {
        return new PicatMultiplicativeExpressionImpl(node);
      }
      else if (type == MULTIPLICATIVE_EXPRESSION_REST) {
        return new PicatMultiplicativeExpressionRestImpl(node);
      }
      else if (type == NEGATION) {
        return new PicatNegationImpl(node);
      }
      else if (type == NUMBER) {
        return new PicatNumberImpl(node);
      }
      else if (type == PARENTHESIZED_EXPRESSION) {
        return new PicatParenthesizedExpressionImpl(node);
      }
      else if (type == PASS_GOAL) {
        return new PicatPassGoalImpl(node);
      }
      else if (type == PATTERN) {
        return new PicatPatternImpl(node);
      }
      else if (type == PATTERN_LIST) {
        return new PicatPatternListImpl(node);
      }
      else if (type == POSTFIX_EXPRESSION) {
        return new PicatPostfixExpressionImpl(node);
      }
      else if (type == POWER_EXPRESSION) {
        return new PicatPowerExpressionImpl(node);
      }
      else if (type == PREDICATE_CLAUSE) {
        return new PicatPredicateClauseImpl(node);
      }
      else if (type == PREDICATE_FACT) {
        return new PicatPredicateFactImpl(node);
      }
      else if (type == PREDICATE_RULE) {
        return new PicatPredicateRuleImpl(node);
      }
      else if (type == PREDICATE_SIGNATURE) {
        return new PicatPredicateSignatureImpl(node);
      }
      else if (type == PROCEDURE_CALL) {
        return new PicatProcedureCallImpl(node);
      }
      else if (type == QUALIFIED_ATOM) {
        return new PicatQualifiedAtomImpl(node);
      }
      else if (type == RANGE_OPERAND) {
        return new PicatRangeOperandImpl(node);
      }
      else if (type == RELATIONAL_EXPRESSION) {
        return new PicatRelationalExpressionImpl(node);
      }
      else if (type == RELATIONAL_EXPRESSION_REST) {
        return new PicatRelationalExpressionRestImpl(node);
      }
      else if (type == RENAME_LIST) {
        return new PicatRenameListImpl(node);
      }
      else if (type == RENAME_SPEC) {
        return new PicatRenameSpecImpl(node);
      }
      else if (type == RETURN_STMT) {
        return new PicatReturnStmtImpl(node);
      }
      else if (type == RULE_OPERATOR) {
        return new PicatRuleOperatorImpl(node);
      }
      else if (type == SHIFT_EXPRESSION) {
        return new PicatShiftExpressionImpl(node);
      }
      else if (type == SHIFT_EXPRESSION_REST) {
        return new PicatShiftExpressionRestImpl(node);
      }
      else if (type == SIMPLE_NUMBER_RANGE) {
        return new PicatSimpleNumberRangeImpl(node);
      }
      else if (type == STRUCTURE) {
        return new PicatStructureImpl(node);
      }
      else if (type == STRUCTURE_PATTERN) {
        return new PicatStructurePatternImpl(node);
      }
      else if (type == TABLE_MODE) {
        return new PicatTableModeImpl(node);
      }
      else if (type == TERM_CONSTRUCTOR_EXPRESSION) {
        return new PicatTermConstructorExpressionImpl(node);
      }
      else if (type == THROW_STMT) {
        return new PicatThrowStmtImpl(node);
      }
      else if (type == TRUE_GOAL) {
        return new PicatTrueGoalImpl(node);
      }
      else if (type == TRY_CATCH) {
        return new PicatTryCatchImpl(node);
      }
      else if (type == TUPLE) {
        return new PicatTupleImpl(node);
      }
      else if (type == TUPLE_ITEMS) {
        return new PicatTupleItemsImpl(node);
      }
      else if (type == TUPLE_PATTERN) {
        return new PicatTuplePatternImpl(node);
      }
      else if (type == TYPE_ANNOTATION) {
        return new PicatTypeAnnotationImpl(node);
      }
      else if (type == UNARY_EXPRESSION) {
        return new PicatUnaryExpressionImpl(node);
      }
      else if (type == UNARY_OPERATOR) {
        return new PicatUnaryOperatorImpl(node);
      }
      else if (type == UNIFICATION) {
        return new PicatUnificationImpl(node);
      }
      else if (type == USING_STATEMENT) {
        return new PicatUsingStatementImpl(node);
      }
      else if (type == VARIABLE_LIST) {
        return new PicatVariableListImpl(node);
      }
      else if (type == WHILE_LOOP) {
        return new PicatWhileLoopImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
