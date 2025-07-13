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
  // multiplicative_expression ((PLUS | CONCAT_OP | MINUS) multiplicative_expression)*
  public static boolean additive_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ADDITIVE_EXPRESSION, "<additive expression>");
    result_ = multiplicative_expression(builder_, level_ + 1);
    result_ = result_ && additive_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((PLUS | CONCAT_OP | MINUS) multiplicative_expression)*
  private static boolean additive_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!additive_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "additive_expression_1", pos_)) break;
    }
    return true;
  }

  // (PLUS | CONCAT_OP | MINUS) multiplicative_expression
  private static boolean additive_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = additive_expression_1_0_0(builder_, level_ + 1);
    result_ = result_ && multiplicative_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PLUS | CONCAT_OP | MINUS
  private static boolean additive_expression_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, CONCAT_OP);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    return result_;
  }

  /* ********************************************************** */
  // not_constr (HASH_AND_OP not_constr)*
  public static boolean and_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AND_CONSTR, "<and constr>");
    result_ = not_constr(builder_, level_ + 1);
    result_ = result_ && and_constr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_AND_OP not_constr)*
  private static boolean and_constr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_constr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!and_constr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "and_constr_1", pos_)) break;
    }
    return true;
  }

  // HASH_AND_OP not_constr
  private static boolean and_constr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_constr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_AND_OP);
    result_ = result_ && not_constr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // shift_expression (BITWISE_AND shift_expression)*
  public static boolean and_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AND_EXPRESSION, "<and expression>");
    result_ = shift_expression(builder_, level_ + 1);
    result_ = result_ && and_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_AND shift_expression)*
  private static boolean and_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!and_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "and_expression_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_AND shift_expression
  private static boolean and_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_AND);
    result_ = result_ && shift_expression(builder_, level_ + 1);
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
  // LBRACE argument (COMMA argument)* RBRACE
  public static boolean array_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && array_expression_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ARRAY_EXPRESSION, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean array_expression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_expression_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_expression_2", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean array_expression_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  // IDENTIFIER | SINGLE_QUOTED_ATOM | dollar_escaped_atom
  public static boolean atom(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM, "<atom>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, SINGLE_QUOTED_ATOM);
    if (!result_) result_ = dollar_escaped_atom(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // atom [LPAR [term (COMMA term)*] RPAR]
  public static boolean atom_or_call_no_lambda(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM_OR_CALL_NO_LAMBDA, "<atom or call no lambda>");
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && atom_or_call_no_lambda_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [LPAR [term (COMMA term)*] RPAR]
  private static boolean atom_or_call_no_lambda_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1")) return false;
    atom_or_call_no_lambda_1_0(builder_, level_ + 1);
    return true;
  }

  // LPAR [term (COMMA term)*] RPAR
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

  // [term (COMMA term)*]
  private static boolean atom_or_call_no_lambda_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1")) return false;
    atom_or_call_no_lambda_1_0_1_0(builder_, level_ + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean atom_or_call_no_lambda_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && atom_or_call_no_lambda_1_0_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA term)*
  private static boolean atom_or_call_no_lambda_1_0_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!atom_or_call_no_lambda_1_0_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "atom_or_call_no_lambda_1_0_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean atom_or_call_no_lambda_1_0_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_or_call_no_lambda_1_0_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // atom !(LPAR)
  public static boolean atom_without_args(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_without_args")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATOM_WITHOUT_ARGS, "<atom without args>");
    result_ = atom(builder_, level_ + 1);
    result_ = result_ && atom_without_args_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !(LPAR)
  private static boolean atom_without_args_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_without_args_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LPAR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // parenthesized_goal
  //                   | function_call
  //                   | lambda_term
  //                   | term_constructor
  //                   | variable_index
  //                   | as_pattern
  //                   | list_expression
  //                   | array_expression
  //                   | STRING
  //                   | VARIABLE
  //                   | INTEGER
  //                   | FLOAT
  //                   | TRUE
  //                   | FALSE
  //                   | atom_without_args
  public static boolean base_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "base_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BASE_EXPRESSION, "<base expression>");
    result_ = parenthesized_goal(builder_, level_ + 1);
    if (!result_) result_ = function_call(builder_, level_ + 1);
    if (!result_) result_ = lambda_term(builder_, level_ + 1);
    if (!result_) result_ = term_constructor(builder_, level_ + 1);
    if (!result_) result_ = variable_index(builder_, level_ + 1);
    if (!result_) result_ = as_pattern(builder_, level_ + 1);
    if (!result_) result_ = list_expression(builder_, level_ + 1);
    if (!result_) result_ = array_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, VARIABLE);
    if (!result_) result_ = consumeToken(builder_, INTEGER);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = atom_without_args(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // EQUAL | NOT_EQUAL | ASSIGN_OP | IDENTICAL | NOT_IDENTICAL
  //              | GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | LESS_EQUAL_PROLOG
  //              | IN_KEYWORD | HASH_LESS_EQUAL_ALT_OP | HASH_LESS_EQUAL_OP | HASH_GREATER_EQUAL_OP
  //              | HASH_EQUAL_OP | HASH_NOT_EQUAL_OP | HASH_GREATER_OP | HASH_LESS_OP
  public static boolean bin_rel_op(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bin_rel_op")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BIN_REL_OP, "<bin rel op>");
    result_ = consumeToken(builder_, EQUAL);
    if (!result_) result_ = consumeToken(builder_, NOT_EQUAL);
    if (!result_) result_ = consumeToken(builder_, ASSIGN_OP);
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
  // negative_goal (COMMA conjunctive_goal)*
  public static boolean conjunctive_goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunctive_goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONJUNCTIVE_GOAL, "<conjunctive goal>");
    result_ = negative_goal(builder_, level_ + 1);
    result_ = result_ && conjunctive_goal_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA conjunctive_goal)*
  private static boolean conjunctive_goal_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunctive_goal_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conjunctive_goal_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conjunctive_goal_1", pos_)) break;
    }
    return true;
  }

  // COMMA conjunctive_goal
  private static boolean conjunctive_goal_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conjunctive_goal_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && conjunctive_goal(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // conjunctive_goal (SEMICOLON disjunctive_goal)*
  public static boolean disjunctive_goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunctive_goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DISJUNCTIVE_GOAL, "<disjunctive goal>");
    result_ = conjunctive_goal(builder_, level_ + 1);
    result_ = result_ && disjunctive_goal_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (SEMICOLON disjunctive_goal)*
  private static boolean disjunctive_goal_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunctive_goal_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!disjunctive_goal_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "disjunctive_goal_1", pos_)) break;
    }
    return true;
  }

  // SEMICOLON disjunctive_goal
  private static boolean disjunctive_goal_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "disjunctive_goal_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    result_ = result_ && disjunctive_goal(builder_, level_ + 1);
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
  // impl_constr (HASH_BICONDITIONAL_OP impl_constr)*
  public static boolean equiv_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equiv_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EQUIV_CONSTR, "<equiv constr>");
    result_ = impl_constr(builder_, level_ + 1);
    result_ = result_ && equiv_constr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_BICONDITIONAL_OP impl_constr)*
  private static boolean equiv_constr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equiv_constr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!equiv_constr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "equiv_constr_1", pos_)) break;
    }
    return true;
  }

  // HASH_BICONDITIONAL_OP impl_constr
  private static boolean equiv_constr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equiv_constr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_BICONDITIONAL_OP);
    result_ = result_ && impl_constr(builder_, level_ + 1);
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
  // range_expression
  public static boolean expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPRESSION, "<expression>");
    result_ = range_expression(builder_, level_ + 1);
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
  // atom LPAR [function_argument (COMMA function_argument)*] RPAR
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

  // atom LPAR [function_argument (COMMA function_argument)*] RPAR
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

  // [function_argument (COMMA function_argument)*]
  private static boolean function_call_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2")) return false;
    function_call_0_2_0(builder_, level_ + 1);
    return true;
  }

  // function_argument (COMMA function_argument)*
  private static boolean function_call_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_argument(builder_, level_ + 1);
    result_ = result_ && function_call_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA function_argument)*
  private static boolean function_call_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!function_call_0_2_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "function_call_0_2_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA function_argument
  private static boolean function_call_0_2_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_0_2_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && function_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // atom LPAR [term (COMMA term)*] RPAR
  //                        | IDENTIFIER DOLLAR IDENTIFIER LPAR [term (COMMA term)*] RPAR
  public static boolean function_call_no_dot(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_CALL_NO_DOT, "<function call no dot>");
    result_ = function_call_no_dot_0(builder_, level_ + 1);
    if (!result_) result_ = function_call_no_dot_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // atom LPAR [term (COMMA term)*] RPAR
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

  // [term (COMMA term)*]
  private static boolean function_call_no_dot_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2")) return false;
    function_call_no_dot_0_2_0(builder_, level_ + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean function_call_no_dot_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && function_call_no_dot_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA term)*
  private static boolean function_call_no_dot_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!function_call_no_dot_0_2_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "function_call_no_dot_0_2_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean function_call_no_dot_0_2_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_0_2_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER DOLLAR IDENTIFIER LPAR [term (COMMA term)*] RPAR
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

  // [term (COMMA term)*]
  private static boolean function_call_no_dot_1_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4")) return false;
    function_call_no_dot_1_4_0(builder_, level_ + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean function_call_no_dot_1_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && function_call_no_dot_1_4_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA term)*
  private static boolean function_call_no_dot_1_4_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!function_call_no_dot_1_4_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "function_call_no_dot_1_4_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean function_call_no_dot_1_4_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_call_no_dot_1_4_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_FACT, "<function fact>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, argument(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, DOT) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // head EQUAL argument [COMMA condition] ARROW_OP body DOT
  public static boolean function_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_rule")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_RULE, "<function rule>");
    result_ = head(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, argument(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, function_rule_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, ARROW_OP)) && result_;
    result_ = pinned_ && report_error_(builder_, body(builder_, level_ + 1)) && result_;
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
  // disjunctive_goal
  public static boolean goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GOAL, "<goal>");
    result_ = disjunctive_goal(builder_, level_ + 1);
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
  // IF_KEYWORD goal THEN_KEYWORD goal (ELSEIF_KEYWORD goal THEN_KEYWORD goal)* ELSE_KEYWORD goal END_KEYWORD
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
    result_ = result_ && consumeToken(builder_, ELSE_KEYWORD);
    result_ = result_ && goal(builder_, level_ + 1);
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

  /* ********************************************************** */
  // or_constr (HASH_ARROW_OP or_constr)*
  public static boolean impl_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "impl_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPL_CONSTR, "<impl constr>");
    result_ = or_constr(builder_, level_ + 1);
    result_ = result_ && impl_constr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_ARROW_OP or_constr)*
  private static boolean impl_constr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "impl_constr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!impl_constr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "impl_constr_1", pos_)) break;
    }
    return true;
  }

  // HASH_ARROW_OP or_constr
  private static boolean impl_constr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "impl_constr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_ARROW_OP);
    result_ = result_ && or_constr(builder_, level_ + 1);
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
  // term IN_KEYWORD expression | term EQUAL expression
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
  // LBRACKET argument list_expression_suffix RBRACKET
  public static boolean list_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && argument(builder_, level_ + 1);
    result_ = result_ && list_expression_suffix(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, LIST_EXPRESSION, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET [term (COMMA term)*] [PIPE term] RBRACKET
  public static boolean list_expression_no_comprehension(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && list_expression_no_comprehension_1(builder_, level_ + 1);
    result_ = result_ && list_expression_no_comprehension_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, LIST_EXPRESSION_NO_COMPREHENSION, result_);
    return result_;
  }

  // [term (COMMA term)*]
  private static boolean list_expression_no_comprehension_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_1")) return false;
    list_expression_no_comprehension_1_0(builder_, level_ + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean list_expression_no_comprehension_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term(builder_, level_ + 1);
    result_ = result_ && list_expression_no_comprehension_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA term)*
  private static boolean list_expression_no_comprehension_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!list_expression_no_comprehension_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "list_expression_no_comprehension_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean list_expression_no_comprehension_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [PIPE term]
  private static boolean list_expression_no_comprehension_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_2")) return false;
    list_expression_no_comprehension_2_0(builder_, level_ + 1);
    return true;
  }

  // PIPE term
  private static boolean list_expression_no_comprehension_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_no_comprehension_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // COLON iterator (COMMA (iterator | condition))*  // list comprehension
  //                         | (COMMA argument)* [PIPE argument]
  public static boolean list_expression_suffix(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LIST_EXPRESSION_SUFFIX, "<list expression suffix>");
    result_ = list_expression_suffix_0(builder_, level_ + 1);
    if (!result_) result_ = list_expression_suffix_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // COLON iterator (COMMA (iterator | condition))*
  private static boolean list_expression_suffix_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && iterator(builder_, level_ + 1);
    result_ = result_ && list_expression_suffix_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA (iterator | condition))*
  private static boolean list_expression_suffix_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!list_expression_suffix_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "list_expression_suffix_0_2", pos_)) break;
    }
    return true;
  }

  // COMMA (iterator | condition)
  private static boolean list_expression_suffix_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && list_expression_suffix_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // iterator | condition
  private static boolean list_expression_suffix_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_0_2_0_1")) return false;
    boolean result_;
    result_ = iterator(builder_, level_ + 1);
    if (!result_) result_ = condition(builder_, level_ + 1);
    return result_;
  }

  // (COMMA argument)* [PIPE argument]
  private static boolean list_expression_suffix_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = list_expression_suffix_1_0(builder_, level_ + 1);
    result_ = result_ && list_expression_suffix_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean list_expression_suffix_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_1_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!list_expression_suffix_1_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "list_expression_suffix_1_0", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean list_expression_suffix_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_1_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [PIPE argument]
  private static boolean list_expression_suffix_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_1_1")) return false;
    list_expression_suffix_1_1_0(builder_, level_ + 1);
    return true;
  }

  // PIPE argument
  private static boolean list_expression_suffix_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_expression_suffix_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  // unary_expression ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression)*
  public static boolean multiplicative_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MULTIPLICATIVE_EXPRESSION, "<multiplicative expression>");
    result_ = unary_expression(builder_, level_ + 1);
    result_ = result_ && multiplicative_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression)*
  private static boolean multiplicative_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!multiplicative_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "multiplicative_expression_1", pos_)) break;
    }
    return true;
  }

  // (MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression
  private static boolean multiplicative_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = multiplicative_expression_1_0_0(builder_, level_ + 1);
    result_ = result_ && unary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD
  private static boolean multiplicative_expression_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_1_0_0")) return false;
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
  // (NOT_KEYWORD negative_goal) | equiv_constr
  public static boolean negative_goal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negative_goal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NEGATIVE_GOAL, "<negative goal>");
    result_ = negative_goal_0(builder_, level_ + 1);
    if (!result_) result_ = equiv_constr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // NOT_KEYWORD negative_goal
  private static boolean negative_goal_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "negative_goal_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NOT_KEYWORD);
    result_ = result_ && negative_goal(builder_, level_ + 1);
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
  // (HASH_NOT_OP not_constr) | enclosed_goal
  public static boolean not_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NOT_CONSTR, "<not constr>");
    result_ = not_constr_0(builder_, level_ + 1);
    if (!result_) result_ = enclosed_goal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // HASH_NOT_OP not_constr
  private static boolean not_constr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_constr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_NOT_OP);
    result_ = result_ && not_constr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // xor_constr (HASH_OR_OP xor_constr)*
  public static boolean or_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OR_CONSTR, "<or constr>");
    result_ = xor_constr(builder_, level_ + 1);
    result_ = result_ && or_constr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_OR_OP xor_constr)*
  private static boolean or_constr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_constr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!or_constr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "or_constr_1", pos_)) break;
    }
    return true;
  }

  // HASH_OR_OP xor_constr
  private static boolean or_constr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_constr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_OR_OP);
    result_ = result_ && xor_constr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // xor_expression (BITWISE_OR xor_expression)*
  public static boolean or_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OR_EXPRESSION, "<or expression>");
    result_ = xor_expression(builder_, level_ + 1);
    result_ = result_ && or_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_OR xor_expression)*
  private static boolean or_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!or_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "or_expression_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_OR xor_expression
  private static boolean or_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_OR);
    result_ = result_ && xor_expression(builder_, level_ + 1);
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
  // primary_expression [POWER unary_expression]
  public static boolean power_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POWER_EXPRESSION, "<power expression>");
    result_ = primary_expression(builder_, level_ + 1);
    result_ = result_ && power_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [POWER unary_expression]
  private static boolean power_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expression_1")) return false;
    power_expression_1_0(builder_, level_ + 1);
    return true;
  }

  // POWER unary_expression
  private static boolean power_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "power_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POWER);
    result_ = result_ && unary_expression(builder_, level_ + 1);
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
  //                      | INDEX_KEYWORD LPAR index_mode (COMMA index_mode)* RPAR
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

  // INDEX_KEYWORD LPAR index_mode (COMMA index_mode)* RPAR
  private static boolean predicate_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INDEX_KEYWORD, LPAR);
    result_ = result_ && index_mode(builder_, level_ + 1);
    result_ = result_ && predicate_directive_2_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA index_mode)*
  private static boolean predicate_directive_2_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!predicate_directive_2_3_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "predicate_directive_2_3", pos_)) break;
    }
    return true;
  }

  // COMMA index_mode
  private static boolean predicate_directive_2_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predicate_directive_2_3_0")) return false;
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
  // base_expression
  public static boolean primary_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PRIMARY_EXPRESSION, "<primary expression>");
    result_ = base_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
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
  // IDENTIFIER DOLLAR IDENTIFIER LPAR [function_argument (COMMA function_argument)*] RPAR
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

  // [function_argument (COMMA function_argument)*]
  private static boolean qualified_function_call_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4")) return false;
    qualified_function_call_4_0(builder_, level_ + 1);
    return true;
  }

  // function_argument (COMMA function_argument)*
  private static boolean qualified_function_call_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_argument(builder_, level_ + 1);
    result_ = result_ && qualified_function_call_4_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA function_argument)*
  private static boolean qualified_function_call_4_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!qualified_function_call_4_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qualified_function_call_4_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA function_argument
  private static boolean qualified_function_call_4_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_function_call_4_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && function_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // or_expression [RANGE_OP or_expression [RANGE_OP or_expression]]
  public static boolean range_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RANGE_EXPRESSION, "<range expression>");
    result_ = or_expression(builder_, level_ + 1);
    result_ = result_ && range_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [RANGE_OP or_expression [RANGE_OP or_expression]]
  private static boolean range_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_1")) return false;
    range_expression_1_0(builder_, level_ + 1);
    return true;
  }

  // RANGE_OP or_expression [RANGE_OP or_expression]
  private static boolean range_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RANGE_OP);
    result_ = result_ && or_expression(builder_, level_ + 1);
    result_ = result_ && range_expression_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [RANGE_OP or_expression]
  private static boolean range_expression_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_1_0_2")) return false;
    range_expression_1_0_2_0(builder_, level_ + 1);
    return true;
  }

  // RANGE_OP or_expression
  private static boolean range_expression_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RANGE_OP);
    result_ = result_ && or_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // additive_expression ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression)*
  public static boolean shift_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SHIFT_EXPRESSION, "<shift expression>");
    result_ = additive_expression(builder_, level_ + 1);
    result_ = result_ && shift_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression)*
  private static boolean shift_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!shift_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "shift_expression_1", pos_)) break;
    }
    return true;
  }

  // (SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression
  private static boolean shift_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = shift_expression_1_0_0(builder_, level_ + 1);
    result_ = result_ && additive_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE
  private static boolean shift_expression_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expression_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SHIFT_LEFT);
    if (!result_) result_ = consumeToken(builder_, SHIFT_RIGHT);
    if (!result_) result_ = consumeToken(builder_, SHIFT_RIGHT_TRIPLE);
    return result_;
  }

  /* ********************************************************** */
  // PLUS | MINUS
  public static boolean table_mode(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "table_mode")) return false;
    if (!nextTokenIs(builder_, "<table mode>", MINUS, PLUS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TABLE_MODE, "<table mode>");
    result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LPAR term RPAR
  //   | variable_as_pattern
  //   | INTEGER
  //   | FLOAT
  //   | TRUE
  //   | FALSE
  //   | VARIABLE
  //   | atom_or_call_no_lambda
  //   | list_expression_no_comprehension
  //   | array_expression
  //   | function_call_no_dot
  //   | term_constructor
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
    if (!result_) result_ = list_expression_no_comprehension(builder_, level_ + 1);
    if (!result_) result_ = array_expression(builder_, level_ + 1);
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
  // DOLLAR expression | IDENTIFIER LPAR [argument (COMMA argument)*] RPAR
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

  // IDENTIFIER LPAR [argument (COMMA argument)*] RPAR
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

  // [argument (COMMA argument)*]
  private static boolean term_constructor_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2")) return false;
    term_constructor_1_2_0(builder_, level_ + 1);
    return true;
  }

  // argument (COMMA argument)*
  private static boolean term_constructor_1_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = argument(builder_, level_ + 1);
    result_ = result_ && term_constructor_1_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA argument)*
  private static boolean term_constructor_1_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!term_constructor_1_2_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "term_constructor_1_2_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean term_constructor_1_2_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_constructor_1_2_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  // power_expression
  //                    | PLUS unary_expression
  //                    | MINUS unary_expression
  //                    | COMPLEMENT unary_expression
  public static boolean unary_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNARY_EXPRESSION, "<unary expression>");
    result_ = power_expression(builder_, level_ + 1);
    if (!result_) result_ = unary_expression_1(builder_, level_ + 1);
    if (!result_) result_ = unary_expression_2(builder_, level_ + 1);
    if (!result_) result_ = unary_expression_3(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // PLUS unary_expression
  private static boolean unary_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expression_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PLUS);
    result_ = result_ && unary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MINUS unary_expression
  private static boolean unary_expression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expression_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, MINUS);
    result_ = result_ && unary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COMPLEMENT unary_expression
  private static boolean unary_expression_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expression_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMPLEMENT);
    result_ = result_ && unary_expression(builder_, level_ + 1);
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
  // VARIABLE LBRACKET argument [COMMA argument] RBRACKET
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

  // [COMMA argument]
  private static boolean variable_index_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_index_3")) return false;
    variable_index_3_0(builder_, level_ + 1);
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
  // LBRACKET [VARIABLE (COMMA VARIABLE)*] RBRACKET
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

  // [VARIABLE (COMMA VARIABLE)*]
  private static boolean variable_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1")) return false;
    variable_list_1_0(builder_, level_ + 1);
    return true;
  }

  // VARIABLE (COMMA VARIABLE)*
  private static boolean variable_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, VARIABLE);
    result_ = result_ && variable_list_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA VARIABLE)*
  private static boolean variable_list_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!variable_list_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_list_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA VARIABLE
  private static boolean variable_list_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_list_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COMMA, VARIABLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  // and_constr (HASH_XOR_OP and_constr)*
  public static boolean xor_constr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_constr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, XOR_CONSTR, "<xor constr>");
    result_ = and_constr(builder_, level_ + 1);
    result_ = result_ && xor_constr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (HASH_XOR_OP and_constr)*
  private static boolean xor_constr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_constr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!xor_constr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "xor_constr_1", pos_)) break;
    }
    return true;
  }

  // HASH_XOR_OP and_constr
  private static boolean xor_constr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_constr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_XOR_OP);
    result_ = result_ && and_constr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // and_expression (BITWISE_XOR and_expression)*
  public static boolean xor_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, XOR_EXPRESSION, "<xor expression>");
    result_ = and_expression(builder_, level_ + 1);
    result_ = result_ && xor_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (BITWISE_XOR and_expression)*
  private static boolean xor_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!xor_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "xor_expression_1", pos_)) break;
    }
    return true;
  }

  // BITWISE_XOR and_expression
  private static boolean xor_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "xor_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BITWISE_XOR);
    result_ = result_ && and_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

}
