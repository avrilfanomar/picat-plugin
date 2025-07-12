// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.avrilfanomar.picatplugin.language.psi.impl.*;

public interface PicatTokenTypes {

  IElementType ACTION_RULE = new PicatElementType("ACTION_RULE");
  IElementType ACTOR_DEFINITION = new PicatElementType("ACTOR_DEFINITION");
  IElementType ADDITIVE_EXPRESSION = new PicatElementType("ADDITIVE_EXPRESSION");
  IElementType AND_CONSTR = new PicatElementType("AND_CONSTR");
  IElementType AND_EXPRESSION = new PicatElementType("AND_EXPRESSION");
  IElementType ARGUMENT = new PicatElementType("ARGUMENT");
  IElementType ARRAY_EXPRESSION = new PicatElementType("ARRAY_EXPRESSION");
  IElementType AS_PATTERN = new PicatElementType("AS_PATTERN");
  IElementType ATOM = new PicatElementType("ATOM");
  IElementType ATOM_OR_CALL_NO_LAMBDA = new PicatElementType("ATOM_OR_CALL_NO_LAMBDA");
  IElementType ATOM_WITHOUT_ARGS = new PicatElementType("ATOM_WITHOUT_ARGS");
  IElementType BASE_EXPRESSION = new PicatElementType("BASE_EXPRESSION");
  IElementType BIN_REL_OP = new PicatElementType("BIN_REL_OP");
  IElementType BODY = new PicatElementType("BODY");
  IElementType CATCH_CLAUSE = new PicatElementType("CATCH_CLAUSE");
  IElementType CONDITION = new PicatElementType("CONDITION");
  IElementType CONJUNCTIVE_GOAL = new PicatElementType("CONJUNCTIVE_GOAL");
  IElementType DISJUNCTIVE_GOAL = new PicatElementType("DISJUNCTIVE_GOAL");
  IElementType DOLLAR_ESCAPED_ATOM = new PicatElementType("DOLLAR_ESCAPED_ATOM");
  IElementType ENCLOSED_GOAL = new PicatElementType("ENCLOSED_GOAL");
  IElementType EQUIV_CONSTR = new PicatElementType("EQUIV_CONSTR");
  IElementType EVENT_PATTERN = new PicatElementType("EVENT_PATTERN");
  IElementType EXCEPTION_PATTERN = new PicatElementType("EXCEPTION_PATTERN");
  IElementType EXPRESSION = new PicatElementType("EXPRESSION");
  IElementType EXPRESSION_WITH_RELATIONS = new PicatElementType("EXPRESSION_WITH_RELATIONS");
  IElementType FOREACH_LOOP = new PicatElementType("FOREACH_LOOP");
  IElementType FUNCTION_CALL = new PicatElementType("FUNCTION_CALL");
  IElementType FUNCTION_CALL_NO_DOT = new PicatElementType("FUNCTION_CALL_NO_DOT");
  IElementType FUNCTION_CLAUSE = new PicatElementType("FUNCTION_CLAUSE");
  IElementType FUNCTION_DEFINITION = new PicatElementType("FUNCTION_DEFINITION");
  IElementType FUNCTION_DIRECTIVE = new PicatElementType("FUNCTION_DIRECTIVE");
  IElementType FUNCTION_FACT = new PicatElementType("FUNCTION_FACT");
  IElementType FUNCTION_RULE = new PicatElementType("FUNCTION_RULE");
  IElementType GOAL = new PicatElementType("GOAL");
  IElementType HEAD = new PicatElementType("HEAD");
  IElementType IF_THEN_ELSE = new PicatElementType("IF_THEN_ELSE");
  IElementType IMPL_CONSTR = new PicatElementType("IMPL_CONSTR");
  IElementType IMPORT_DECLARATION = new PicatElementType("IMPORT_DECLARATION");
  IElementType IMPORT_ITEM = new PicatElementType("IMPORT_ITEM");
  IElementType INCLUDE_DECLARATION = new PicatElementType("INCLUDE_DECLARATION");
  IElementType INDEX_MODE = new PicatElementType("INDEX_MODE");
  IElementType ITERATOR = new PicatElementType("ITERATOR");
  IElementType LAMBDA_TERM = new PicatElementType("LAMBDA_TERM");
  IElementType LIST_EXPRESSION = new PicatElementType("LIST_EXPRESSION");
  IElementType LIST_EXPRESSION_NO_COMPREHENSION = new PicatElementType("LIST_EXPRESSION_NO_COMPREHENSION");
  IElementType LIST_EXPRESSION_SUFFIX = new PicatElementType("LIST_EXPRESSION_SUFFIX");
  IElementType LOOP_WHILE = new PicatElementType("LOOP_WHILE");
  IElementType MODULE_DECLARATION = new PicatElementType("MODULE_DECLARATION");
  IElementType MULTIPLICATIVE_EXPRESSION = new PicatElementType("MULTIPLICATIVE_EXPRESSION");
  IElementType NEGATIVE_GOAL = new PicatElementType("NEGATIVE_GOAL");
  IElementType NONBACKTRACKABLE_PREDICATE_RULE = new PicatElementType("NONBACKTRACKABLE_PREDICATE_RULE");
  IElementType NOT_CONSTR = new PicatElementType("NOT_CONSTR");
  IElementType OR_CONSTR = new PicatElementType("OR_CONSTR");
  IElementType OR_EXPRESSION = new PicatElementType("OR_EXPRESSION");
  IElementType PARENTHESIZED_GOAL = new PicatElementType("PARENTHESIZED_GOAL");
  IElementType POWER_EXPRESSION = new PicatElementType("POWER_EXPRESSION");
  IElementType PREDICATE_CLAUSE = new PicatElementType("PREDICATE_CLAUSE");
  IElementType PREDICATE_DEFINITION = new PicatElementType("PREDICATE_DEFINITION");
  IElementType PREDICATE_DIRECTIVE = new PicatElementType("PREDICATE_DIRECTIVE");
  IElementType PREDICATE_FACT = new PicatElementType("PREDICATE_FACT");
  IElementType PREDICATE_RULE = new PicatElementType("PREDICATE_RULE");
  IElementType PRIMARY_EXPRESSION = new PicatElementType("PRIMARY_EXPRESSION");
  IElementType PROGRAM_ITEM = new PicatElementType("PROGRAM_ITEM");
  IElementType QUALIFIED_FUNCTION_CALL = new PicatElementType("QUALIFIED_FUNCTION_CALL");
  IElementType RANGE_EXPRESSION = new PicatElementType("RANGE_EXPRESSION");
  IElementType SHIFT_EXPRESSION = new PicatElementType("SHIFT_EXPRESSION");
  IElementType TABLE_MODE = new PicatElementType("TABLE_MODE");
  IElementType TERM = new PicatElementType("TERM");
  IElementType TERM_CONSTRUCTOR = new PicatElementType("TERM_CONSTRUCTOR");
  IElementType TRY_CATCH = new PicatElementType("TRY_CATCH");
  IElementType TYPE_ANNOTATION = new PicatElementType("TYPE_ANNOTATION");
  IElementType UNARY_EXPRESSION = new PicatElementType("UNARY_EXPRESSION");
  IElementType VARIABLE_AS_PATTERN = new PicatElementType("VARIABLE_AS_PATTERN");
  IElementType VARIABLE_INDEX = new PicatElementType("VARIABLE_INDEX");
  IElementType VARIABLE_LIST = new PicatElementType("VARIABLE_LIST");
  IElementType WHILE_LOOP = new PicatElementType("WHILE_LOOP");
  IElementType XOR_CONSTR = new PicatElementType("XOR_CONSTR");
  IElementType XOR_EXPRESSION = new PicatElementType("XOR_EXPRESSION");

  IElementType ARROW_OP = new PicatTokenType("=>");
  IElementType ASSIGN_OP = new PicatTokenType(":=");
  IElementType AT = new PicatTokenType("@");
  IElementType BACKTRACKABLE_ARROW_OP = new PicatTokenType("?=>");
  IElementType BICONDITIONAL_OP = new PicatTokenType("<=>");
  IElementType BITWISE_AND = new PicatTokenType("/\\\\");
  IElementType BITWISE_OR = new PicatTokenType("\\\\/");
  IElementType BITWISE_XOR = new PicatTokenType("^");
  IElementType CARDINALITY_KEYWORD = new PicatTokenType("cardinality");
  IElementType CATCH_KEYWORD = new PicatTokenType("catch");
  IElementType COLON = new PicatTokenType(":");
  IElementType COMMA = new PicatTokenType(",");
  IElementType COMMENT = new PicatTokenType("COMMENT");
  IElementType COMPLEMENT = new PicatTokenType("~");
  IElementType CONCAT_OP = new PicatTokenType("++");
  IElementType DIVIDE = new PicatTokenType("/");
  IElementType DIV_KEYWORD = new PicatTokenType("div");
  IElementType DIV_LEFT = new PicatTokenType("/<");
  IElementType DIV_RIGHT = new PicatTokenType("/>");
  IElementType DOLLAR = new PicatTokenType("$");
  IElementType DOT = new PicatTokenType(".");
  IElementType DOUBLE_COLON_OP = new PicatTokenType("::");
  IElementType ELSEIF_KEYWORD = new PicatTokenType("elseif");
  IElementType ELSE_KEYWORD = new PicatTokenType("else");
  IElementType END_KEYWORD = new PicatTokenType("end");
  IElementType EQUAL = new PicatTokenType("=");
  IElementType FALSE = new PicatTokenType("false");
  IElementType FINALLY_KEYWORD = new PicatTokenType("finally");
  IElementType FLOAT = new PicatTokenType("FLOAT");
  IElementType FOREACH_KEYWORD = new PicatTokenType("foreach");
  IElementType GREATER = new PicatTokenType(">");
  IElementType GREATER_EQUAL = new PicatTokenType(">=");
  IElementType HASH_AND_OP = new PicatTokenType("#/\\\\");
  IElementType HASH_ARROW_OP = new PicatTokenType("#=>");
  IElementType HASH_BICONDITIONAL_OP = new PicatTokenType("#<=>");
  IElementType HASH_EQUAL_OP = new PicatTokenType("#=");
  IElementType HASH_GREATER_EQUAL_OP = new PicatTokenType("#>=");
  IElementType HASH_GREATER_OP = new PicatTokenType("#>");
  IElementType HASH_LESS_EQUAL_ALT_OP = new PicatTokenType("#<=");
  IElementType HASH_LESS_EQUAL_OP = new PicatTokenType("#=<");
  IElementType HASH_LESS_OP = new PicatTokenType("#<");
  IElementType HASH_NOT_EQUAL_OP = new PicatTokenType("#!=");
  IElementType HASH_NOT_OP = new PicatTokenType("#~");
  IElementType HASH_OR_OP = new PicatTokenType("#\\\\/");
  IElementType HASH_XOR_OP = new PicatTokenType("#^");
  IElementType IDENTICAL = new PicatTokenType("==");
  IElementType IDENTIFIER = new PicatTokenType("IDENTIFIER");
  IElementType IF_KEYWORD = new PicatTokenType("if");
  IElementType IMPORT_KEYWORD = new PicatTokenType("import");
  IElementType INCLUDE_KEYWORD = new PicatTokenType("include");
  IElementType INDEX_KEYWORD = new PicatTokenType("index");
  IElementType INTEGER = new PicatTokenType("INTEGER");
  IElementType INT_DIVIDE = new PicatTokenType("//");
  IElementType IN_KEYWORD = new PicatTokenType("in");
  IElementType LAMBDA_KEYWORD = new PicatTokenType("lambda");
  IElementType LBRACE = new PicatTokenType("{");
  IElementType LBRACKET = new PicatTokenType("[");
  IElementType LESS = new PicatTokenType("<");
  IElementType LESS_EQUAL = new PicatTokenType("<=");
  IElementType LESS_EQUAL_PROLOG = new PicatTokenType("=<");
  IElementType LOOP_KEYWORD = new PicatTokenType("loop");
  IElementType LPAR = new PicatTokenType("(");
  IElementType MAX_KEYWORD = new PicatTokenType("max");
  IElementType MINUS = new PicatTokenType("-");
  IElementType MIN_KEYWORD = new PicatTokenType("min");
  IElementType MODULE_KEYWORD = new PicatTokenType("module");
  IElementType MOD_KEYWORD = new PicatTokenType("mod");
  IElementType MULTILINE_COMMENT = new PicatTokenType("MULTILINE_COMMENT");
  IElementType MULTIPLY = new PicatTokenType("*");
  IElementType NOT_EQUAL = new PicatTokenType("!=");
  IElementType NOT_IDENTICAL = new PicatTokenType("!==");
  IElementType NOT_KEYWORD = new PicatTokenType("not");
  IElementType PIPE = new PicatTokenType("|");
  IElementType PLUS = new PicatTokenType("+");
  IElementType POWER = new PicatTokenType("**");
  IElementType PRIVATE_KEYWORD = new PicatTokenType("private");
  IElementType PROLOG_RULE_OP = new PicatTokenType(":-");
  IElementType QUALIFIED_ATOM = new PicatTokenType("QUALIFIED_ATOM");
  IElementType RANGE_OP = new PicatTokenType("..");
  IElementType RBRACE = new PicatTokenType("}");
  IElementType RBRACKET = new PicatTokenType("]");
  IElementType REM_KEYWORD = new PicatTokenType("rem");
  IElementType RPAR = new PicatTokenType(")");
  IElementType SEMICOLON = new PicatTokenType(";");
  IElementType SHIFT_LEFT = new PicatTokenType("<<");
  IElementType SHIFT_RIGHT = new PicatTokenType(">>");
  IElementType SHIFT_RIGHT_TRIPLE = new PicatTokenType(">>>");
  IElementType STRING = new PicatTokenType("STRING");
  IElementType TABLE_KEYWORD = new PicatTokenType("table");
  IElementType THEN_KEYWORD = new PicatTokenType("then");
  IElementType TRUE = new PicatTokenType("true");
  IElementType TRY_KEYWORD = new PicatTokenType("try");
  IElementType VARIABLE = new PicatTokenType("variable");
  IElementType WHILE_KEYWORD = new PicatTokenType("while");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ACTION_RULE) {
        return new PicatActionRuleImpl(node);
      }
      else if (type == ACTOR_DEFINITION) {
        return new PicatActorDefinitionImpl(node);
      }
      else if (type == ADDITIVE_EXPRESSION) {
        return new PicatAdditiveExpressionImpl(node);
      }
      else if (type == AND_CONSTR) {
        return new PicatAndConstrImpl(node);
      }
      else if (type == AND_EXPRESSION) {
        return new PicatAndExpressionImpl(node);
      }
      else if (type == ARGUMENT) {
        return new PicatArgumentImpl(node);
      }
      else if (type == ARRAY_EXPRESSION) {
        return new PicatArrayExpressionImpl(node);
      }
      else if (type == AS_PATTERN) {
        return new PicatAsPatternImpl(node);
      }
      else if (type == ATOM) {
        return new PicatAtomImpl(node);
      }
      else if (type == ATOM_OR_CALL_NO_LAMBDA) {
        return new PicatAtomOrCallNoLambdaImpl(node);
      }
      else if (type == ATOM_WITHOUT_ARGS) {
        return new PicatAtomWithoutArgsImpl(node);
      }
      else if (type == BASE_EXPRESSION) {
        return new PicatBaseExpressionImpl(node);
      }
      else if (type == BIN_REL_OP) {
        return new PicatBinRelOpImpl(node);
      }
      else if (type == BODY) {
        return new PicatBodyImpl(node);
      }
      else if (type == CATCH_CLAUSE) {
        return new PicatCatchClauseImpl(node);
      }
      else if (type == CONDITION) {
        return new PicatConditionImpl(node);
      }
      else if (type == CONJUNCTIVE_GOAL) {
        return new PicatConjunctiveGoalImpl(node);
      }
      else if (type == DISJUNCTIVE_GOAL) {
        return new PicatDisjunctiveGoalImpl(node);
      }
      else if (type == DOLLAR_ESCAPED_ATOM) {
        return new PicatDollarEscapedAtomImpl(node);
      }
      else if (type == ENCLOSED_GOAL) {
        return new PicatEnclosedGoalImpl(node);
      }
      else if (type == EQUIV_CONSTR) {
        return new PicatEquivConstrImpl(node);
      }
      else if (type == EVENT_PATTERN) {
        return new PicatEventPatternImpl(node);
      }
      else if (type == EXCEPTION_PATTERN) {
        return new PicatExceptionPatternImpl(node);
      }
      else if (type == EXPRESSION) {
        return new PicatExpressionImpl(node);
      }
      else if (type == EXPRESSION_WITH_RELATIONS) {
        return new PicatExpressionWithRelationsImpl(node);
      }
      else if (type == FOREACH_LOOP) {
        return new PicatForeachLoopImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new PicatFunctionCallImpl(node);
      }
      else if (type == FUNCTION_CALL_NO_DOT) {
        return new PicatFunctionCallNoDotImpl(node);
      }
      else if (type == FUNCTION_CLAUSE) {
        return new PicatFunctionClauseImpl(node);
      }
      else if (type == FUNCTION_DEFINITION) {
        return new PicatFunctionDefinitionImpl(node);
      }
      else if (type == FUNCTION_DIRECTIVE) {
        return new PicatFunctionDirectiveImpl(node);
      }
      else if (type == FUNCTION_FACT) {
        return new PicatFunctionFactImpl(node);
      }
      else if (type == FUNCTION_RULE) {
        return new PicatFunctionRuleImpl(node);
      }
      else if (type == GOAL) {
        return new PicatGoalImpl(node);
      }
      else if (type == HEAD) {
        return new PicatHeadImpl(node);
      }
      else if (type == IF_THEN_ELSE) {
        return new PicatIfThenElseImpl(node);
      }
      else if (type == IMPL_CONSTR) {
        return new PicatImplConstrImpl(node);
      }
      else if (type == IMPORT_DECLARATION) {
        return new PicatImportDeclarationImpl(node);
      }
      else if (type == IMPORT_ITEM) {
        return new PicatImportItemImpl(node);
      }
      else if (type == INCLUDE_DECLARATION) {
        return new PicatIncludeDeclarationImpl(node);
      }
      else if (type == INDEX_MODE) {
        return new PicatIndexModeImpl(node);
      }
      else if (type == ITERATOR) {
        return new PicatIteratorImpl(node);
      }
      else if (type == LAMBDA_TERM) {
        return new PicatLambdaTermImpl(node);
      }
      else if (type == LIST_EXPRESSION) {
        return new PicatListExpressionImpl(node);
      }
      else if (type == LIST_EXPRESSION_NO_COMPREHENSION) {
        return new PicatListExpressionNoComprehensionImpl(node);
      }
      else if (type == LIST_EXPRESSION_SUFFIX) {
        return new PicatListExpressionSuffixImpl(node);
      }
      else if (type == LOOP_WHILE) {
        return new PicatLoopWhileImpl(node);
      }
      else if (type == MODULE_DECLARATION) {
        return new PicatModuleDeclarationImpl(node);
      }
      else if (type == MULTIPLICATIVE_EXPRESSION) {
        return new PicatMultiplicativeExpressionImpl(node);
      }
      else if (type == NEGATIVE_GOAL) {
        return new PicatNegativeGoalImpl(node);
      }
      else if (type == NONBACKTRACKABLE_PREDICATE_RULE) {
        return new PicatNonbacktrackablePredicateRuleImpl(node);
      }
      else if (type == NOT_CONSTR) {
        return new PicatNotConstrImpl(node);
      }
      else if (type == OR_CONSTR) {
        return new PicatOrConstrImpl(node);
      }
      else if (type == OR_EXPRESSION) {
        return new PicatOrExpressionImpl(node);
      }
      else if (type == PARENTHESIZED_GOAL) {
        return new PicatParenthesizedGoalImpl(node);
      }
      else if (type == POWER_EXPRESSION) {
        return new PicatPowerExpressionImpl(node);
      }
      else if (type == PREDICATE_CLAUSE) {
        return new PicatPredicateClauseImpl(node);
      }
      else if (type == PREDICATE_DEFINITION) {
        return new PicatPredicateDefinitionImpl(node);
      }
      else if (type == PREDICATE_DIRECTIVE) {
        return new PicatPredicateDirectiveImpl(node);
      }
      else if (type == PREDICATE_FACT) {
        return new PicatPredicateFactImpl(node);
      }
      else if (type == PREDICATE_RULE) {
        return new PicatPredicateRuleImpl(node);
      }
      else if (type == PRIMARY_EXPRESSION) {
        return new PicatPrimaryExpressionImpl(node);
      }
      else if (type == PROGRAM_ITEM) {
        return new PicatProgramItemImpl(node);
      }
      else if (type == QUALIFIED_FUNCTION_CALL) {
        return new PicatQualifiedFunctionCallImpl(node);
      }
      else if (type == RANGE_EXPRESSION) {
        return new PicatRangeExpressionImpl(node);
      }
      else if (type == SHIFT_EXPRESSION) {
        return new PicatShiftExpressionImpl(node);
      }
      else if (type == TABLE_MODE) {
        return new PicatTableModeImpl(node);
      }
      else if (type == TERM) {
        return new PicatTermImpl(node);
      }
      else if (type == TERM_CONSTRUCTOR) {
        return new PicatTermConstructorImpl(node);
      }
      else if (type == TRY_CATCH) {
        return new PicatTryCatchImpl(node);
      }
      else if (type == TYPE_ANNOTATION) {
        return new PicatTypeAnnotationImpl(node);
      }
      else if (type == UNARY_EXPRESSION) {
        return new PicatUnaryExpressionImpl(node);
      }
      else if (type == VARIABLE_AS_PATTERN) {
        return new PicatVariableAsPatternImpl(node);
      }
      else if (type == VARIABLE_INDEX) {
        return new PicatVariableIndexImpl(node);
      }
      else if (type == VARIABLE_LIST) {
        return new PicatVariableListImpl(node);
      }
      else if (type == WHILE_LOOP) {
        return new PicatWhileLoopImpl(node);
      }
      else if (type == XOR_CONSTR) {
        return new PicatXorConstrImpl(node);
      }
      else if (type == XOR_EXPRESSION) {
        return new PicatXorExpressionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
