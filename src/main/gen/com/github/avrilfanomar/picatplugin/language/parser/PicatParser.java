// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.*;
import static com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PicatParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return program(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // head [COMMA condition] COMMA LBRACE event_pattern RBRACE ARROW_OP body DOT
  public static boolean action_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_rule")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACTION_RULE, "<action rule>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && action_rule_1(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COMMA, LBRACE);
    result_ = result_ && event_pattern(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, RBRACE, ARROW_OP);
    result_ = result_ && body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DOT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [COMMA condition]
  private static boolean action_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_rule_1")) return false;
    action_rule_1_0(builder_, level_ + 1);
    return true;
  }

  // COMMA condition
  private static boolean action_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && condition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // [PRIVATE_KEYWORD] action_rule (action_rule | nonbacktrackable_predicate_rule)*
  public static boolean actor_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actor_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACTOR_DEFINITION, "<actor definition>");
    result_ = actor_definition_0(builder_, level_ + 1);
    result_ = result_ && action_rule(builder_, level_ + 1);
    result_ = result_ && actor_definition_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [PRIVATE_KEYWORD]
  private static boolean actor_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actor_definition_0")) return false;
    consumeToken(builder_, PRIVATE_KEYWORD);
    return true;
  }

  // (action_rule | nonbacktrackable_predicate_rule)*
  private static boolean actor_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actor_definition_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!actor_definition_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "actor_definition_2", pos_)) break;
    }
    return true;
  }

  // action_rule | nonbacktrackable_predicate_rule
  private static boolean actor_definition_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actor_definition_2_0")) return false;
    boolean result_;
    result_ = action_rule(builder_, level_ + 1);
    if (!result_) result_ = nonbacktrackable_predicate_rule(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // multiplicative_expr ((PLUS | CONCAT_OP | MINUS) multiplicative_expr)*
  public static boolean additive_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ADDITIVE_EXPR, "<additive expr>");
    result_ = multiplicative_expr(builder_, level_ + 1);
    result_ = result_ && additive_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((PLUS | CONCAT_OP | MINUS) multiplicative_expr)*
  private static boolean additive_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!additive_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "additive_expr_1", pos_)) break;
    }
    return true;
  }

  // (PLUS | CONCAT_OP | MINUS) multiplicative_expr
  private static boolean additive_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = additive_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && multiplicative_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PLUS | CONCAT_OP | MINUS
  private static boolean additive_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, CONCAT_OP);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    return result_;
  }

  /* ********************************************************** */
  // shift_expr (BITWISE_AND shift_expr)*
  public static boolean and_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AND_EXPR, "<and expr>");
    result_ = shift_expr(builder_, level_ + 1);
    result_ = result_ && and_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_AND shift_expr)*
  private static boolean and_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!and_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "and_expr_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_AND shift_expr
  private static boolean and_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_AND);
    result_ = result_ && shift_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // expression
  public static boolean argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARGUMENT, "<argument>");
    result_ = expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // COMMA argument argument_list_tail?
  public static boolean argument_list_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && argument_list_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, ARGUMENT_LIST_TAIL, result_);
    return result_;
  }

  // argument_list_tail?
  private static boolean argument_list_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_list_tail_2")) return false;
    argument_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACE [argument array_items_tail?] RBRACE
  public static boolean array_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && array_expr_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ARRAY_EXPR, result_);
    return result_;
  }

  // [argument array_items_tail?]
  private static boolean array_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_1")) return false;
    array_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // argument array_items_tail?
  private static boolean array_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && array_expr_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // array_items_tail?
  private static boolean array_expr_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_1_0_1")) return false;
    array_items_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // COMMA argument array_items_tail?
  public static boolean array_items_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_items_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && array_items_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, ARRAY_ITEMS_TAIL, result_);
    return result_;
  }

  // array_items_tail?
  private static boolean array_items_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_items_tail_2")) return false;
    array_items_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // VARIABLE AT term [AT]
  public static boolean as_pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "as_pattern")) return false;
    if (!nextTokenIs(builder_, VARIABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VARIABLE, AT);
    result_ = result_ && term(builder_, level_ + 1);
    result_ = result_ && as_pattern_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, AS_PATTERN, result_);
    return result_;
  }

  // [AT]
  private static boolean as_pattern_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "as_pattern_3")) return false;
    consumeToken(builder_, AT);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER | SINGLE_QUOTED_ATOM | dollar_escaped_atom | MIN | MAX
  public static boolean atom(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM, "<atom>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, SINGLE_QUOTED_ATOM);
    if (!result_) result_ = dollar_escaped_atom(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, MIN);
    if (!result_) result_ = consumeToken(builder_, MAX);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // atom !LPAR
  public static boolean atom_no_args(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_no_args")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM_NO_ARGS, "<atom no args>");
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && atom_no_args_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !LPAR
  private static boolean atom_no_args_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_no_args_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LPAR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // atom [LPAR [term term_list_tail?] RPAR]
  public static boolean atom_or_call_no_lambda(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM_OR_CALL_NO_LAMBDA, "<atom or call no lambda>");
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && atom_or_call_no_lambda_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [LPAR [term term_list_tail?] RPAR]
  private static boolean atom_or_call_no_lambda_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1")) return false;
    atom_or_call_no_lambda_1_0(builder_, level_ + 1);
    return true;
  }

  // LPAR [term term_list_tail?] RPAR
  private static boolean atom_or_call_no_lambda_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && atom_or_call_no_lambda_1_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [term term_list_tail?]
  private static boolean atom_or_call_no_lambda_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1")) return false;
    atom_or_call_no_lambda_1_0_1_0(builder_, level_ + 1);
    return true;
  }

  // term term_list_tail?
  private static boolean atom_or_call_no_lambda_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && atom_or_call_no_lambda_1_0_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // term_list_tail?
  private static boolean atom_or_call_no_lambda_1_0_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1_0_1")) return false;
    term_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // parenthesized_goal
  //             | lambda_term
  //             | function_call
  //             | term_constructor
  //             | as_pattern
  //             | list_expr
  //             | variable_index
  //             | array_expr
  //             | STRING
  //             | VARIABLE
  //             | INTEGER
  //             | FLOAT
  //             | TRUE
  //             | FALSE
  //             | atom_no_args
  public static boolean base_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "base_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BASE_EXPR, "<base expr>");
    result_ = parenthesized_goal(builder_, level_ + 1);
    if (!result_) result_ = lambda_term(builder_, level_ + 1);
    if (!result_) result_ = function_call(builder_, level_ + 1);
    if (!result_) result_ = term_constructor(builder_, level_ + 1);
    if (!result_) result_ = as_pattern(builder_, level_ + 1);
    if (!result_) result_ = list_expr(builder_, level_ + 1);
    if (!result_) result_ = variable_index(builder_, level_ + 1);
    if (!result_) result_ = array_expr(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, VARIABLE);
    if (!result_) result_ = consumeToken(builder_, INTEGER);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = atom_no_args(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // EQUAL | NOT_EQUAL | ASSIGN_OP | NUMERICALLY_EQUAL | NUMERICALLY_NON_EQUAL | IDENTICAL | NOT_IDENTICAL
  //              | GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | LESS_EQUAL_PROLOG
  //              | IN_KEYWORD | HASH_LESS_EQUAL_ALT_OP | HASH_LESS_EQUAL_OP | HASH_GREATER_EQUAL_OP
  //              | HASH_EQUAL_OP | HASH_NOT_EQUAL_OP | HASH_GREATER_OP | HASH_LESS_OP
  //              | AT_LESS_EQUAL_OP | AT_GREATER_EQUAL_OP
  //              | AT_GREATER_OP | AT_LESS_OP | AT_LESS_EQUAL_PROLOG_OP
  public static boolean bin_rel_op(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bin_rel_op")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BIN_REL_OP, "<bin rel op>");
    result_ = consumeToken(builder_, EQUAL);
    if (!result_) result_ = consumeToken(builder_, NOT_EQUAL);
    if (!result_) result_ = consumeToken(builder_, ASSIGN_OP);
    if (!result_) result_ = consumeToken(builder_, NUMERICALLY_EQUAL);
    if (!result_) result_ = consumeToken(builder_, NUMERICALLY_NON_EQUAL);
    if (!result_) result_ = consumeToken(builder_, IDENTICAL);
    if (!result_) result_ = consumeToken(builder_, NOT_IDENTICAL);
    if (!result_) result_ = consumeToken(builder_, GREATER);
    if (!result_) result_ = consumeToken(builder_, GREATER_EQUAL);
    if (!result_) result_ = consumeToken(builder_, LESS);
    if (!result_) result_ = consumeToken(builder_, LESS_EQUAL);
    if (!result_) result_ = consumeToken(builder_, LESS_EQUAL_PROLOG);
    if (!result_) result_ = consumeToken(builder_, IN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, HASH_LESS_EQUAL_ALT_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_LESS_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_GREATER_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_NOT_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_GREATER_OP);
    if (!result_) result_ = consumeToken(builder_, HASH_LESS_OP);
    if (!result_) result_ = consumeToken(builder_, AT_LESS_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, AT_GREATER_EQUAL_OP);
    if (!result_) result_ = consumeToken(builder_, AT_GREATER_OP);
    if (!result_) result_ = consumeToken(builder_, AT_LESS_OP);
    if (!result_) result_ = consumeToken(builder_, AT_LESS_EQUAL_PROLOG_OP);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // goal
  public static boolean body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BODY, "<body>");
    result_ = goal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // CATCH_KEYWORD LPAR exception_pattern RPAR goal
  public static boolean catch_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clause")) return false;
    if (!nextTokenIs(builder_, CATCH_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CATCH_KEYWORD, LPAR);
    result_ = result_ && exception_pattern(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    result_ = result_ && goal(builder_, level_ + 1);
    exit_section_(builder_, marker_, CATCH_CLAUSE, result_);
    return result_;
  }

  /* ********************************************************** */
  // goal
  public static boolean condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "condition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONDITION, "<condition>");
    result_ = goal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // negation (COMMA negation)*
  public static boolean conjunction(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONJUNCTION, "<conjunction>");
    result_ = negation(builder_, level_ + 1);
    result_ = result_ && conjunction_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA negation)*
  private static boolean conjunction_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conjunction_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conjunction_1", pos_)) break;
    }
    return true;
  }

  // COMMA negation
  private static boolean conjunction_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && negation(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // negated (HASH_AND_OP negated)*
  public static boolean conjunction_and(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction_and")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONJUNCTION_AND, "<conjunction and>");
    result_ = negated(builder_, level_ + 1);
    result_ = result_ && conjunction_and_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_AND_OP negated)*
  private static boolean conjunction_and_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction_and_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conjunction_and_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conjunction_and_1", pos_)) break;
    }
    return true;
  }

  // HASH_AND_OP negated
  private static boolean conjunction_and_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunction_and_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_AND_OP);
    result_ = result_ && negated(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // conjunction (SEMICOLON conjunction)*
  public static boolean disjunction(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DISJUNCTION, "<disjunction>");
    result_ = conjunction(builder_, level_ + 1);
    result_ = result_ && disjunction_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (SEMICOLON conjunction)*
  private static boolean disjunction_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!disjunction_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "disjunction_1", pos_)) break;
    }
    return true;
  }

  // SEMICOLON conjunction
  private static boolean disjunction_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    result_ = result_ && conjunction(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // exclusive_or (HASH_OR_OP exclusive_or)*
  public static boolean disjunction_or(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction_or")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DISJUNCTION_OR, "<disjunction or>");
    result_ = exclusive_or(builder_, level_ + 1);
    result_ = result_ && disjunction_or_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_OR_OP exclusive_or)*
  private static boolean disjunction_or_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction_or_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!disjunction_or_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "disjunction_or_1", pos_)) break;
    }
    return true;
  }

  // HASH_OR_OP exclusive_or
  private static boolean disjunction_or_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunction_or_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_OR_OP);
    result_ = result_ && exclusive_or(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // DOLLAR IDENTIFIER
  public static boolean dollar_escaped_atom(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dollar_escaped_atom")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOLLAR, IDENTIFIER);
    exit_section_(builder_, marker_, DOLLAR_ESCAPED_ATOM, result_);
    return result_;
  }

  /* ********************************************************** */
  // (DOT_IDENTIFIER | DOT_SINGLE_QUOTED_ATOM) [LPAR [argument (COMMA argument)*] RPAR]
  public static boolean dot_access(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access")) return false;
    if (!nextTokenIs(builder_, "<dot access>", DOT_IDENTIFIER, DOT_SINGLE_QUOTED_ATOM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DOT_ACCESS, "<dot access>");
    result_ = dot_access_0(builder_, level_ + 1);
    result_ = result_ && dot_access_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // DOT_IDENTIFIER | DOT_SINGLE_QUOTED_ATOM
  private static boolean dot_access_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DOT_IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, DOT_SINGLE_QUOTED_ATOM);
    return result_;
  }

  // [LPAR [argument (COMMA argument)*] RPAR]
  private static boolean dot_access_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1")) return false;
    dot_access_1_0(builder_, level_ + 1);
    return true;
  }

  // LPAR [argument (COMMA argument)*] RPAR
  private static boolean dot_access_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && dot_access_1_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [argument (COMMA argument)*]
  private static boolean dot_access_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1_0_1")) return false;
    dot_access_1_0_1_0(builder_, level_ + 1);
    return true;
  }

  // argument (COMMA argument)*
  private static boolean dot_access_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && dot_access_1_0_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean dot_access_1_0_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1_0_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!dot_access_1_0_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "dot_access_1_0_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean dot_access_1_0_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dot_access_1_0_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // if_then_else
  //                 | foreach_loop
  //                 | while_loop
  //                 | loop_while
  //                 | try_catch
  //                 | type_annotation
  //                 | expression_with_relations
  public static boolean enclosed_goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enclosed_goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENCLOSED_GOAL, "<enclosed goal>");
    result_ = if_then_else(builder_, level_ + 1);
    if (!result_) result_ = foreach_loop(builder_, level_ + 1);
    if (!result_) result_ = while_loop(builder_, level_ + 1);
    if (!result_) result_ = loop_while(builder_, level_ + 1);
    if (!result_) result_ = try_catch(builder_, level_ + 1);
    if (!result_) result_ = type_annotation(builder_, level_ + 1);
    if (!result_) result_ = expression_with_relations(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // implication (HASH_BICONDITIONAL_OP implication)*
  public static boolean equivalence(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equivalence")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EQUIVALENCE, "<equivalence>");
    result_ = implication(builder_, level_ + 1);
    result_ = result_ && equivalence_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_BICONDITIONAL_OP implication)*
  private static boolean equivalence_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equivalence_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!equivalence_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "equivalence_1", pos_)) break;
    }
    return true;
  }

  // HASH_BICONDITIONAL_OP implication
  private static boolean equivalence_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equivalence_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_BICONDITIONAL_OP);
    result_ = result_ && implication(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // term (COMMA term)*
  public static boolean event_pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "event_pattern")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EVENT_PATTERN, "<event pattern>");
    result_ = term(builder_, level_ + 1);
    result_ = result_ && event_pattern_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA term)*
  private static boolean event_pattern_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "event_pattern_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!event_pattern_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "event_pattern_1", pos_)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean event_pattern_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "event_pattern_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // term
  public static boolean exception_pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exception_pattern")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCEPTION_PATTERN, "<exception pattern>");
    result_ = term(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // conjunction_and (HASH_XOR_OP conjunction_and)*
  public static boolean exclusive_or(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exclusive_or")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCLUSIVE_OR, "<exclusive or>");
    result_ = conjunction_and(builder_, level_ + 1);
    result_ = result_ && exclusive_or_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_XOR_OP conjunction_and)*
  private static boolean exclusive_or_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exclusive_or_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!exclusive_or_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "exclusive_or_1", pos_)) break;
    }
    return true;
  }

  // HASH_XOR_OP conjunction_and
  private static boolean exclusive_or_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exclusive_or_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_XOR_OP);
    result_ = result_ && conjunction_and(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // range_expr
  public static boolean expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPRESSION, "<expression>");
    result_ = range_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // expression (bin_rel_op expression)*
  public static boolean expression_with_relations(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_relations")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPRESSION_WITH_RELATIONS, "<expression with relations>");
    result_ = expression(builder_, level_ + 1);
    result_ = result_ && expression_with_relations_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (bin_rel_op expression)*
  private static boolean expression_with_relations_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_relations_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expression_with_relations_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expression_with_relations_1", pos_)) break;
    }
    return true;
  }

  // bin_rel_op expression
  private static boolean expression_with_relations_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_relations_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = bin_rel_op(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // FOREACH_KEYWORD LPAR iterator (COMMA (iterator | condition))* RPAR goal END_KEYWORD
  public static boolean foreach_loop(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_loop")) return false;
    if (!nextTokenIs(builder_, FOREACH_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FOREACH_KEYWORD, LPAR);
    result_ = result_ && iterator(builder_, level_ + 1);
    result_ = result_ && foreach_loop_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, END_KEYWORD);
    exit_section_(builder_, marker_, FOREACH_LOOP, result_);
    return result_;
  }

  // (COMMA (iterator | condition))*
  private static boolean foreach_loop_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_loop_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!foreach_loop_3_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "foreach_loop_3", pos_)) break;
    }
    return true;
  }

  // COMMA (iterator | condition)
  private static boolean foreach_loop_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_loop_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && foreach_loop_3_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // iterator | condition
  private static boolean foreach_loop_3_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_loop_3_0_1")) return false;
    boolean result_;
    result_ = iterator(builder_, level_ + 1);
    if (!result_) result_ = condition(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // expression_with_relations
  public static boolean function_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_argument")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_ARGUMENT, "<function argument>");
    result_ = expression_with_relations(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // COMMA function_argument function_argument_list_tail?
  public static boolean function_argument_list_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_argument_list_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && function_argument(builder_, level_ + 1);
    result_ = result_ && function_argument_list_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, FUNCTION_ARGUMENT_LIST_TAIL, result_);
    return result_;
  }

  // function_argument_list_tail?
  private static boolean function_argument_list_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_argument_list_tail_2")) return false;
    function_argument_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // atom LPAR [function_argument function_argument_list_tail?] RPAR
  //                | qualified_function_call
  public static boolean function_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_CALL, "<function call>");
    result_ = function_call_0(builder_, level_ + 1);
    if (!result_) result_ = qualified_function_call(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // atom LPAR [function_argument function_argument_list_tail?] RPAR
  private static boolean function_call_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAR);
    result_ = result_ && function_call_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [function_argument function_argument_list_tail?]
  private static boolean function_call_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2")) return false;
    function_call_0_2_0(builder_, level_ + 1);
    return true;
  }

  // function_argument function_argument_list_tail?
  private static boolean function_call_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_argument(builder_, level_ + 1);
    result_ = result_ && function_call_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // function_argument_list_tail?
  private static boolean function_call_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2_0_1")) return false;
    function_argument_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // atom LPAR [term term_list_tail?] RPAR
  //                        | IDENTIFIER DOLLAR IDENTIFIER LPAR [term term_list_tail?] RPAR
  public static boolean function_call_no_dot(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_CALL_NO_DOT, "<function call no dot>");
    result_ = function_call_no_dot_0(builder_, level_ + 1);
    if (!result_) result_ = function_call_no_dot_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // atom LPAR [term term_list_tail?] RPAR
  private static boolean function_call_no_dot_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAR);
    result_ = result_ && function_call_no_dot_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [term term_list_tail?]
  private static boolean function_call_no_dot_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2")) return false;
    function_call_no_dot_0_2_0(builder_, level_ + 1);
    return true;
  }

  // term term_list_tail?
  private static boolean function_call_no_dot_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && function_call_no_dot_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // term_list_tail?
  private static boolean function_call_no_dot_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2_0_1")) return false;
    term_list_tail(builder_, level_ + 1);
    return true;
  }

  // IDENTIFIER DOLLAR IDENTIFIER LPAR [term term_list_tail?] RPAR
  private static boolean function_call_no_dot_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, DOLLAR, IDENTIFIER, LPAR);
    result_ = result_ && function_call_no_dot_1_4(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [term term_list_tail?]
  private static boolean function_call_no_dot_1_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4")) return false;
    function_call_no_dot_1_4_0(builder_, level_ + 1);
    return true;
  }

  // term term_list_tail?
  private static boolean function_call_no_dot_1_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && function_call_no_dot_1_4_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // term_list_tail?
  private static boolean function_call_no_dot_1_4_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4_0_1")) return false;
    term_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // function_rule | function_fact
  public static boolean function_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_clause")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_CLAUSE, "<function clause>");
    result_ = function_rule(builder_, level_ + 1);
    if (!result_) result_ = function_fact(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // function_directive* function_clause
  public static boolean function_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_DEFINITION, "<function definition>");
    result_ = function_definition_0(builder_, level_ + 1);
    result_ = result_ && function_clause(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // function_directive*
  private static boolean function_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_definition_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!function_directive(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "function_definition_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PRIVATE_KEYWORD | TABLE_KEYWORD
  public static boolean function_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_directive")) return false;
    if (!nextTokenIs(builder_, "<function directive>", PRIVATE_KEYWORD, TABLE_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_DIRECTIVE, "<function directive>");
    result_ = consumeToken(builder_, PRIVATE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TABLE_KEYWORD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // head EQUAL argument DOT
  public static boolean function_fact(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_fact")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_FACT, "<function fact>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DOT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // head EQUAL argument [COMMA condition] ARROW_OP body DOT
  public static boolean function_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_rule")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_RULE, "<function rule>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && function_rule_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, ARROW_OP);
    pinned_ = result_; // pin = 5
    result_ = result_ && report_error_(builder_, body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [COMMA condition]
  private static boolean function_rule_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_rule_3")) return false;
    function_rule_3_0(builder_, level_ + 1);
    return true;
  }

  // COMMA condition
  private static boolean function_rule_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_rule_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && condition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // disjunction
  public static boolean goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GOAL, "<goal>");
    result_ = disjunction(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // atom [LPAR [argument (COMMA argument)*] RPAR]
  public static boolean head(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD, "<head>");
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && head_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [LPAR [argument (COMMA argument)*] RPAR]
  private static boolean head_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1")) return false;
    head_1_0(builder_, level_ + 1);
    return true;
  }

  // LPAR [argument (COMMA argument)*] RPAR
  private static boolean head_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && head_1_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [argument (COMMA argument)*]
  private static boolean head_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1_0_1")) return false;
    head_1_0_1_0(builder_, level_ + 1);
    return true;
  }

  // argument (COMMA argument)*
  private static boolean head_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && head_1_0_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean head_1_0_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1_0_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!head_1_0_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "head_1_0_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean head_1_0_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head_1_0_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IF_KEYWORD goal THEN_KEYWORD goal (ELSEIF_KEYWORD goal THEN_KEYWORD goal)* (ELSE_KEYWORD goal)? END_KEYWORD
  public static boolean if_then_else(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_then_else")) return false;
    if (!nextTokenIs(builder_, IF_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IF_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, THEN_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && if_then_else_4(builder_, level_ + 1);
    result_ = result_ && if_then_else_5(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, END_KEYWORD);
    exit_section_(builder_, marker_, IF_THEN_ELSE, result_);
    return result_;
  }

  // (ELSEIF_KEYWORD goal THEN_KEYWORD goal)*
  private static boolean if_then_else_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_then_else_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!if_then_else_4_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "if_then_else_4", pos_)) break;
    }
    return true;
  }

  // ELSEIF_KEYWORD goal THEN_KEYWORD goal
  private static boolean if_then_else_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_then_else_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ELSEIF_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, THEN_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (ELSE_KEYWORD goal)?
  private static boolean if_then_else_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_then_else_5")) return false;
    if_then_else_5_0(builder_, level_ + 1);
    return true;
  }

  // ELSE_KEYWORD goal
  private static boolean if_then_else_5_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_then_else_5_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ELSE_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // disjunction_or (HASH_ARROW_OP disjunction_or)*
  public static boolean implication(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "implication")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPLICATION, "<implication>");
    result_ = disjunction_or(builder_, level_ + 1);
    result_ = result_ && implication_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_ARROW_OP disjunction_or)*
  private static boolean implication_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "implication_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!implication_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "implication_1", pos_)) break;
    }
    return true;
  }

  // HASH_ARROW_OP disjunction_or
  private static boolean implication_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "implication_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_ARROW_OP);
    result_ = result_ && disjunction_or(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IMPORT_KEYWORD import_item (COMMA import_item)* DOT
  public static boolean import_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_declaration")) return false;
    if (!nextTokenIs(builder_, IMPORT_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPORT_DECLARATION, null);
    result_ = consumeToken(builder_, IMPORT_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, import_item(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, import_declaration_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (COMMA import_item)*
  private static boolean import_declaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_declaration_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!import_declaration_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "import_declaration_2", pos_)) break;
    }
    return true;
  }

  // COMMA import_item
  private static boolean import_declaration_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_declaration_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && import_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (atom | QUALIFIED_ATOM) [DIV_RIGHT INTEGER]
  public static boolean import_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPORT_ITEM, "<import item>");
    result_ = import_item_0(builder_, level_ + 1);
    result_ = result_ && import_item_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // atom | QUALIFIED_ATOM
  private static boolean import_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_item_0")) return false;
    boolean result_;
    result_ = atom(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, QUALIFIED_ATOM);
    return result_;
  }

  // [DIV_RIGHT INTEGER]
  private static boolean import_item_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_item_1")) return false;
    parseTokens(builder_, 0, DIV_RIGHT, INTEGER);
    return true;
  }

  /* ********************************************************** */
  // INCLUDE_KEYWORD STRING (COMMA STRING)* DOT
  public static boolean include_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_declaration")) return false;
    if (!nextTokenIs(builder_, INCLUDE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INCLUDE_DECLARATION, null);
    result_ = consumeTokens(builder_, 1, INCLUDE_KEYWORD, STRING);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, include_declaration_2(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (COMMA STRING)*
  private static boolean include_declaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_declaration_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!include_declaration_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "include_declaration_2", pos_)) break;
    }
    return true;
  }

  // COMMA STRING
  private static boolean include_declaration_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_declaration_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COMMA, STRING);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // PLUS | MINUS
  public static boolean index_mode(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "index_mode")) return false;
    if (!nextTokenIs(builder_, "<index mode>", MINUS, PLUS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INDEX_MODE, "<index mode>");
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // term IN_KEYWORD expression
  //            | term EQUAL expression
  public static boolean iterator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iterator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ITERATOR, "<iterator>");
    result_ = iterator_0(builder_, level_ + 1);
    if (!result_) result_ = iterator_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // term IN_KEYWORD expression
  private static boolean iterator_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iterator_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, IN_KEYWORD);
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // term EQUAL expression
  private static boolean iterator_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "iterator_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LAMBDA_KEYWORD LPAR variable_list COMMA argument RPAR
  public static boolean lambda_term(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lambda_term")) return false;
    if (!nextTokenIs(builder_, LAMBDA_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, LAMBDA_KEYWORD, LPAR);
    result_ = result_ && variable_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, LAMBDA_TERM, result_);
    return result_;
  }

  /* ********************************************************** */
  // COMMA (iterator | condition) list_comprehension_tail?
  public static boolean list_comprehension_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_comprehension_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && list_comprehension_tail_1(builder_, level_ + 1);
    result_ = result_ && list_comprehension_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, LIST_COMPREHENSION_TAIL, result_);
    return result_;
  }

  // iterator | condition
  private static boolean list_comprehension_tail_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_comprehension_tail_1")) return false;
    boolean result_;
    result_ = iterator(builder_, level_ + 1);
    if (!result_) result_ = condition(builder_, level_ + 1);
    return result_;
  }

  // list_comprehension_tail?
  private static boolean list_comprehension_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_comprehension_tail_2")) return false;
    list_comprehension_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACKET [argument list_expr_suffix?] RBRACKET
  public static boolean list_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && list_expr_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, LIST_EXPR, result_);
    return result_;
  }

  // [argument list_expr_suffix?]
  private static boolean list_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_1")) return false;
    list_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // argument list_expr_suffix?
  private static boolean list_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && list_expr_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // list_expr_suffix?
  private static boolean list_expr_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_1_0_1")) return false;
    list_expr_suffix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACKET [term term_list_tail?] [PIPE term] RBRACKET
  public static boolean list_expr_no_comprehension(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && list_expr_no_comprehension_1(builder_, level_ + 1);
    result_ = result_ && list_expr_no_comprehension_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, LIST_EXPR_NO_COMPREHENSION, result_);
    return result_;
  }

  // [term term_list_tail?]
  private static boolean list_expr_no_comprehension_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension_1")) return false;
    list_expr_no_comprehension_1_0(builder_, level_ + 1);
    return true;
  }

  // term term_list_tail?
  private static boolean list_expr_no_comprehension_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && list_expr_no_comprehension_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // term_list_tail?
  private static boolean list_expr_no_comprehension_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension_1_0_1")) return false;
    term_list_tail(builder_, level_ + 1);
    return true;
  }

  // [PIPE term]
  private static boolean list_expr_no_comprehension_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension_2")) return false;
    list_expr_no_comprehension_2_0(builder_, level_ + 1);
    return true;
  }

  // PIPE term
  private static boolean list_expr_no_comprehension_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_no_comprehension_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (COLON iterator list_comprehension_tail?)   // list comprehension
  //                    | (list_items_tail? [PIPE argument])
  public static boolean list_expr_suffix(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LIST_EXPR_SUFFIX, "<list expr suffix>");
    result_ = list_expr_suffix_0(builder_, level_ + 1);
    if (!result_) result_ = list_expr_suffix_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // COLON iterator list_comprehension_tail?
  private static boolean list_expr_suffix_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && iterator(builder_, level_ + 1);
    result_ = result_ && list_expr_suffix_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // list_comprehension_tail?
  private static boolean list_expr_suffix_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_0_2")) return false;
    list_comprehension_tail(builder_, level_ + 1);
    return true;
  }

  // list_items_tail? [PIPE argument]
  private static boolean list_expr_suffix_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = list_expr_suffix_1_0(builder_, level_ + 1);
    result_ = result_ && list_expr_suffix_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // list_items_tail?
  private static boolean list_expr_suffix_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_1_0")) return false;
    list_items_tail(builder_, level_ + 1);
    return true;
  }

  // [PIPE argument]
  private static boolean list_expr_suffix_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_1_1")) return false;
    list_expr_suffix_1_1_0(builder_, level_ + 1);
    return true;
  }

  // PIPE argument
  private static boolean list_expr_suffix_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expr_suffix_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // COMMA argument list_items_tail?
  public static boolean list_items_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_items_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && list_items_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, LIST_ITEMS_TAIL, result_);
    return result_;
  }

  // list_items_tail?
  private static boolean list_items_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_items_tail_2")) return false;
    list_items_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LOOP_KEYWORD goal WHILE_KEYWORD LPAR goal RPAR
  public static boolean loop_while(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "loop_while")) return false;
    if (!nextTokenIs(builder_, LOOP_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LOOP_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, WHILE_KEYWORD, LPAR);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, LOOP_WHILE, result_);
    return result_;
  }

  /* ********************************************************** */
  // MODULE_KEYWORD atom DOT
  public static boolean module_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "module_declaration")) return false;
    if (!nextTokenIs(builder_, MODULE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MODULE_DECLARATION, null);
    result_ = consumeToken(builder_, MODULE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, atom(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // unary_expr ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expr)*
  public static boolean multiplicative_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MULTIPLICATIVE_EXPR, "<multiplicative expr>");
    result_ = unary_expr(builder_, level_ + 1);
    result_ = result_ && multiplicative_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expr)*
  private static boolean multiplicative_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!multiplicative_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "multiplicative_expr_1", pos_)) break;
    }
    return true;
  }

  // (MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expr
  private static boolean multiplicative_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = multiplicative_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD
  private static boolean multiplicative_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, MULTIPLY);
    if (!result_) result_ = consumeToken(builder_, DIVIDE);
    if (!result_) result_ = consumeToken(builder_, INT_DIVIDE);
    if (!result_) result_ = consumeToken(builder_, DIV_RIGHT);
    if (!result_) result_ = consumeToken(builder_, DIV_LEFT);
    if (!result_) result_ = consumeToken(builder_, DIV_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MOD_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, REM_KEYWORD);
    return result_;
  }

  /* ********************************************************** */
  // HASH_NOT_OP negated | enclosed_goal
  public static boolean negated(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negated")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NEGATED, "<negated>");
    result_ = negated_0(builder_, level_ + 1);
    if (!result_) result_ = enclosed_goal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // HASH_NOT_OP negated
  private static boolean negated_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negated_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_NOT_OP);
    result_ = result_ && negated(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // NOT_KEYWORD negation | equivalence
  public static boolean negation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NEGATION, "<negation>");
    result_ = negation_0(builder_, level_ + 1);
    if (!result_) result_ = equivalence(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // NOT_KEYWORD negation
  private static boolean negation_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negation_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NOT_KEYWORD);
    result_ = result_ && negation(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // head [COMMA condition] ARROW_OP body DOT
  public static boolean nonbacktrackable_predicate_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonbacktrackable_predicate_rule")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NONBACKTRACKABLE_PREDICATE_RULE, "<nonbacktrackable predicate rule>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && nonbacktrackable_predicate_rule_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, ARROW_OP);
    result_ = result_ && body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DOT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [COMMA condition]
  private static boolean nonbacktrackable_predicate_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonbacktrackable_predicate_rule_1")) return false;
    nonbacktrackable_predicate_rule_1_0(builder_, level_ + 1);
    return true;
  }

  // COMMA condition
  private static boolean nonbacktrackable_predicate_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonbacktrackable_predicate_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && condition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // xor_expr (BITWISE_OR xor_expr)*
  public static boolean or_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OR_EXPR, "<or expr>");
    result_ = xor_expr(builder_, level_ + 1);
    result_ = result_ && or_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_OR xor_expr)*
  private static boolean or_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!or_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "or_expr_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_OR xor_expr
  private static boolean or_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_OR);
    result_ = result_ && xor_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LPAR goal RPAR
  public static boolean parenthesized_goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_goal")) return false;
    if (!nextTokenIs(builder_, LPAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, PARENTHESIZED_GOAL, result_);
    return result_;
  }

  /* ********************************************************** */
  // dot_access
  public static boolean postfix_op(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_op")) return false;
    if (!nextTokenIs(builder_, "<postfix op>", DOT_IDENTIFIER, DOT_SINGLE_QUOTED_ATOM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POSTFIX_OP, "<postfix op>");
    result_ = dot_access(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // postfix_op postfix_ops?
  public static boolean postfix_ops(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_ops")) return false;
    if (!nextTokenIs(builder_, "<postfix ops>", DOT_IDENTIFIER, DOT_SINGLE_QUOTED_ATOM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POSTFIX_OPS, "<postfix ops>");
    result_ = postfix_op(builder_, level_ + 1);
    result_ = result_ && postfix_ops_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // postfix_ops?
  private static boolean postfix_ops_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "postfix_ops_1")) return false;
    postfix_ops(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // primary_expr [POWER unary_expr]
  public static boolean power_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POWER_EXPR, "<power expr>");
    result_ = primary_expr(builder_, level_ + 1);
    result_ = result_ && power_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [POWER unary_expr]
  private static boolean power_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expr_1")) return false;
    power_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // POWER unary_expr
  private static boolean power_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POWER);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // predicate_rule | predicate_fact
  public static boolean predicate_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_clause")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PREDICATE_CLAUSE, "<predicate clause>");
    result_ = predicate_rule(builder_, level_ + 1);
    if (!result_) result_ = predicate_fact(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // predicate_directive* predicate_clause
  public static boolean predicate_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PREDICATE_DEFINITION, "<predicate definition>");
    result_ = predicate_definition_0(builder_, level_ + 1);
    result_ = result_ && predicate_clause(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // predicate_directive*
  private static boolean predicate_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_definition_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!predicate_directive(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "predicate_definition_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PRIVATE_KEYWORD
  //                      | TABLE_KEYWORD [LPAR table_mode (COMMA table_mode)* RPAR]
  //                      | INDEX_KEYWORD (LPAR index_mode (COMMA index_mode)* RPAR)+
  public static boolean predicate_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PREDICATE_DIRECTIVE, "<predicate directive>");
    result_ = consumeToken(builder_, PRIVATE_KEYWORD);
    if (!result_) result_ = predicate_directive_1(builder_, level_ + 1);
    if (!result_) result_ = predicate_directive_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TABLE_KEYWORD [LPAR table_mode (COMMA table_mode)* RPAR]
  private static boolean predicate_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TABLE_KEYWORD);
    result_ = result_ && predicate_directive_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [LPAR table_mode (COMMA table_mode)* RPAR]
  private static boolean predicate_directive_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_1_1")) return false;
    predicate_directive_1_1_0(builder_, level_ + 1);
    return true;
  }

  // LPAR table_mode (COMMA table_mode)* RPAR
  private static boolean predicate_directive_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && table_mode(builder_, level_ + 1);
    result_ = result_ && predicate_directive_1_1_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA table_mode)*
  private static boolean predicate_directive_1_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_1_1_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!predicate_directive_1_1_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "predicate_directive_1_1_0_2", pos_)) break;
    }
    return true;
  }

  // COMMA table_mode
  private static boolean predicate_directive_1_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_1_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && table_mode(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // INDEX_KEYWORD (LPAR index_mode (COMMA index_mode)* RPAR)+
  private static boolean predicate_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, INDEX_KEYWORD);
    result_ = result_ && predicate_directive_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (LPAR index_mode (COMMA index_mode)* RPAR)+
  private static boolean predicate_directive_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = predicate_directive_2_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!predicate_directive_2_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "predicate_directive_2_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LPAR index_mode (COMMA index_mode)* RPAR
  private static boolean predicate_directive_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && index_mode(builder_, level_ + 1);
    result_ = result_ && predicate_directive_2_1_0_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA index_mode)*
  private static boolean predicate_directive_2_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_1_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!predicate_directive_2_1_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "predicate_directive_2_1_0_2", pos_)) break;
    }
    return true;
  }

  // COMMA index_mode
  private static boolean predicate_directive_2_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && index_mode(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // head DOT
  public static boolean predicate_fact(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_fact")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PREDICATE_FACT, "<predicate fact>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DOT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // head [COMMA condition] (ARROW_OP | BACKTRACKABLE_ARROW_OP | PROLOG_RULE_OP) body DOT
  public static boolean predicate_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_rule")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PREDICATE_RULE, "<predicate rule>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && predicate_rule_1(builder_, level_ + 1);
    result_ = result_ && predicate_rule_2(builder_, level_ + 1);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [COMMA condition]
  private static boolean predicate_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_rule_1")) return false;
    predicate_rule_1_0(builder_, level_ + 1);
    return true;
  }

  // COMMA condition
  private static boolean predicate_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && condition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ARROW_OP | BACKTRACKABLE_ARROW_OP | PROLOG_RULE_OP
  private static boolean predicate_rule_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_rule_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, ARROW_OP);
    if (!result_) result_ = consumeToken(builder_, BACKTRACKABLE_ARROW_OP);
    if (!result_) result_ = consumeToken(builder_, PROLOG_RULE_OP);
    return result_;
  }

  /* ********************************************************** */
  // base_expr postfix_ops?
  public static boolean primary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PRIMARY_EXPR, "<primary expr>");
    result_ = base_expr(builder_, level_ + 1);
    result_ = result_ && primary_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // postfix_ops?
  private static boolean primary_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expr_1")) return false;
    postfix_ops(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // program_item*
  static boolean program(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "program")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!program_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "program", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // MULTILINE_COMMENT
  //                | module_declaration
  //                | import_declaration
  //                | include_declaration
  //                | predicate_definition
  //                | function_definition
  //                | actor_definition
  public static boolean program_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "program_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROGRAM_ITEM, "<program item>");
    result_ = consumeToken(builder_, MULTILINE_COMMENT);
    if (!result_) result_ = module_declaration(builder_, level_ + 1);
    if (!result_) result_ = import_declaration(builder_, level_ + 1);
    if (!result_) result_ = include_declaration(builder_, level_ + 1);
    if (!result_) result_ = predicate_definition(builder_, level_ + 1);
    if (!result_) result_ = function_definition(builder_, level_ + 1);
    if (!result_) result_ = actor_definition(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER DOLLAR IDENTIFIER LPAR [function_argument function_argument_list_tail?] RPAR
  public static boolean qualified_function_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, DOLLAR, IDENTIFIER, LPAR);
    result_ = result_ && qualified_function_call_4(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, QUALIFIED_FUNCTION_CALL, result_);
    return result_;
  }

  // [function_argument function_argument_list_tail?]
  private static boolean qualified_function_call_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4")) return false;
    qualified_function_call_4_0(builder_, level_ + 1);
    return true;
  }

  // function_argument function_argument_list_tail?
  private static boolean qualified_function_call_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_argument(builder_, level_ + 1);
    result_ = result_ && qualified_function_call_4_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // function_argument_list_tail?
  private static boolean qualified_function_call_4_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4_0_1")) return false;
    function_argument_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // or_expr [RANGE_OP or_expr [RANGE_OP or_expr]]
  public static boolean range_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RANGE_EXPR, "<range expr>");
    result_ = or_expr(builder_, level_ + 1);
    result_ = result_ && range_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [RANGE_OP or_expr [RANGE_OP or_expr]]
  private static boolean range_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expr_1")) return false;
    range_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // RANGE_OP or_expr [RANGE_OP or_expr]
  private static boolean range_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RANGE_OP);
    result_ = result_ && or_expr(builder_, level_ + 1);
    result_ = result_ && range_expr_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [RANGE_OP or_expr]
  private static boolean range_expr_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expr_1_0_2")) return false;
    range_expr_1_0_2_0(builder_, level_ + 1);
    return true;
  }

  // RANGE_OP or_expr
  private static boolean range_expr_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expr_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RANGE_OP);
    result_ = result_ && or_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // additive_expr ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expr)*
  public static boolean shift_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SHIFT_EXPR, "<shift expr>");
    result_ = additive_expr(builder_, level_ + 1);
    result_ = result_ && shift_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expr)*
  private static boolean shift_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!shift_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "shift_expr_1", pos_)) break;
    }
    return true;
  }

  // (SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expr
  private static boolean shift_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = shift_expr_1_0_0(builder_, level_ + 1);
    result_ = result_ && additive_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE
  private static boolean shift_expr_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expr_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SHIFT_LEFT);
    if (!result_) result_ = consumeToken(builder_, SHIFT_RIGHT);
    if (!result_) result_ = consumeToken(builder_, SHIFT_RIGHT_TRIPLE);
    return result_;
  }

  /* ********************************************************** */
  // PLUS | MINUS | MIN | MAX | NT
  public static boolean table_mode(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "table_mode")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TABLE_MODE, "<table mode>");
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, MIN);
    if (!result_) result_ = consumeToken(builder_, MAX);
    if (!result_) result_ = consumeToken(builder_, NT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LPAR term RPAR
  //        | variable_as_pattern
  //        | INTEGER
  //        | FLOAT
  //        | TRUE
  //        | FALSE
  //        | VARIABLE
  //        | atom_or_call_no_lambda
  //        | list_expr_no_comprehension
  //        | array_expr
  //        | function_call_no_dot
  //        | term_constructor
  public static boolean term(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TERM, "<term>");
    result_ = term_0(builder_, level_ + 1);
    if (!result_) result_ = variable_as_pattern(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, INTEGER);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, VARIABLE);
    if (!result_) result_ = atom_or_call_no_lambda(builder_, level_ + 1);
    if (!result_) result_ = list_expr_no_comprehension(builder_, level_ + 1);
    if (!result_) result_ = array_expr(builder_, level_ + 1);
    if (!result_) result_ = function_call_no_dot(builder_, level_ + 1);
    if (!result_) result_ = term_constructor(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // LPAR term RPAR
  private static boolean term_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAR);
    result_ = result_ && term(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // DOLLAR expression | IDENTIFIER LPAR [argument argument_list_tail?] RPAR
  public static boolean term_constructor(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor")) return false;
    if (!nextTokenIs(builder_, "<term constructor>", DOLLAR, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TERM_CONSTRUCTOR, "<term constructor>");
    result_ = term_constructor_0(builder_, level_ + 1);
    if (!result_) result_ = term_constructor_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // DOLLAR expression
  private static boolean term_constructor_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DOLLAR);
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER LPAR [argument argument_list_tail?] RPAR
  private static boolean term_constructor_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, LPAR);
    result_ = result_ && term_constructor_1_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [argument argument_list_tail?]
  private static boolean term_constructor_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2")) return false;
    term_constructor_1_2_0(builder_, level_ + 1);
    return true;
  }

  // argument argument_list_tail?
  private static boolean term_constructor_1_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && term_constructor_1_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // argument_list_tail?
  private static boolean term_constructor_1_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2_0_1")) return false;
    argument_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // COMMA term term_list_tail?
  public static boolean term_list_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_list_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    result_ = result_ && term_list_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, TERM_LIST_TAIL, result_);
    return result_;
  }

  // term_list_tail?
  private static boolean term_list_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_list_tail_2")) return false;
    term_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TRY_KEYWORD goal catch_clause* [FINALLY_KEYWORD goal] END_KEYWORD
  public static boolean try_catch(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_catch")) return false;
    if (!nextTokenIs(builder_, TRY_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TRY_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && try_catch_2(builder_, level_ + 1);
    result_ = result_ && try_catch_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, END_KEYWORD);
    exit_section_(builder_, marker_, TRY_CATCH, result_);
    return result_;
  }

  // catch_clause*
  private static boolean try_catch_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_catch_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!catch_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "try_catch_2", pos_)) break;
    }
    return true;
  }

  // [FINALLY_KEYWORD goal]
  private static boolean try_catch_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_catch_3")) return false;
    try_catch_3_0(builder_, level_ + 1);
    return true;
  }

  // FINALLY_KEYWORD goal
  private static boolean try_catch_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_catch_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, FINALLY_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // VARIABLE DOUBLE_COLON_OP expression
  public static boolean type_annotation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_annotation")) return false;
    if (!nextTokenIs(builder_, VARIABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VARIABLE, DOUBLE_COLON_OP);
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, TYPE_ANNOTATION, result_);
    return result_;
  }

  /* ********************************************************** */
  // power_expr
  //              | PLUS unary_expr
  //              | MINUS unary_expr
  //              | COMPLEMENT unary_expr
  public static boolean unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNARY_EXPR, "<unary expr>");
    result_ = power_expr(builder_, level_ + 1);
    if (!result_) result_ = unary_expr_1(builder_, level_ + 1);
    if (!result_) result_ = unary_expr_2(builder_, level_ + 1);
    if (!result_) result_ = unary_expr_3(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // PLUS unary_expr
  private static boolean unary_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PLUS);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MINUS unary_expr
  private static boolean unary_expr_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, MINUS);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COMPLEMENT unary_expr
  private static boolean unary_expr_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMPLEMENT);
    result_ = result_ && unary_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // VARIABLE AT term [AT]
  public static boolean variable_as_pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_as_pattern")) return false;
    if (!nextTokenIs(builder_, VARIABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VARIABLE, AT);
    result_ = result_ && term(builder_, level_ + 1);
    result_ = result_ && variable_as_pattern_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, VARIABLE_AS_PATTERN, result_);
    return result_;
  }

  // [AT]
  private static boolean variable_as_pattern_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_as_pattern_3")) return false;
    consumeToken(builder_, AT);
    return true;
  }

  /* ********************************************************** */
  // VARIABLE LBRACKET argument (COMMA argument)* RBRACKET
  public static boolean variable_index(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_index")) return false;
    if (!nextTokenIs(builder_, VARIABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VARIABLE, LBRACKET);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && variable_index_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, VARIABLE_INDEX, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean variable_index_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_index_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!variable_index_3_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_index_3", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean variable_index_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_index_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET [VARIABLE variable_list_tail?] RBRACKET
  public static boolean variable_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && variable_list_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, VARIABLE_LIST, result_);
    return result_;
  }

  // [VARIABLE variable_list_tail?]
  private static boolean variable_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1")) return false;
    variable_list_1_0(builder_, level_ + 1);
    return true;
  }

  // VARIABLE variable_list_tail?
  private static boolean variable_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, VARIABLE);
    result_ = result_ && variable_list_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // variable_list_tail?
  private static boolean variable_list_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1_0_1")) return false;
    variable_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // COMMA VARIABLE variable_list_tail?
  public static boolean variable_list_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_tail")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COMMA, VARIABLE);
    result_ = result_ && variable_list_tail_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, VARIABLE_LIST_TAIL, result_);
    return result_;
  }

  // variable_list_tail?
  private static boolean variable_list_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_tail_2")) return false;
    variable_list_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // WHILE_KEYWORD LPAR goal RPAR [LOOP_KEYWORD] goal END_KEYWORD
  public static boolean while_loop(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_loop")) return false;
    if (!nextTokenIs(builder_, WHILE_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, WHILE_KEYWORD, LPAR);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    result_ = result_ && while_loop_4(builder_, level_ + 1);
    result_ = result_ && goal(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, END_KEYWORD);
    exit_section_(builder_, marker_, WHILE_LOOP, result_);
    return result_;
  }

  // [LOOP_KEYWORD]
  private static boolean while_loop_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_loop_4")) return false;
    consumeToken(builder_, LOOP_KEYWORD);
    return true;
  }

  /* ********************************************************** */
  // and_expr (BITWISE_XOR and_expr)*
  public static boolean xor_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, XOR_EXPR, "<xor expr>");
    result_ = and_expr(builder_, level_ + 1);
    result_ = result_ && xor_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_XOR and_expr)*
  private static boolean xor_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!xor_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "xor_expr_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_XOR and_expr
  private static boolean xor_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_XOR);
    result_ = result_ && and_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

}
