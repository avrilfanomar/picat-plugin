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

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return program(b, l + 1);
  }

  /* ********************************************************** */
  // head [COMMA condition] COMMA LBRACE event_pattern RBRACE ARROW_OP body DOT
  public static boolean action_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "action_rule")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = head(b, l + 1);
    r = r && action_rule_1(b, l + 1);
    r = r && consumeTokens(b, 0, COMMA, LBRACE);
    r = r && event_pattern(b, l + 1);
    r = r && consumeTokens(b, 0, RBRACE, ARROW_OP);
    r = r && body(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, ACTION_RULE, r);
    return r;
  }

  // [COMMA condition]
  private static boolean action_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "action_rule_1")) return false;
    action_rule_1_0(b, l + 1);
    return true;
  }

  // COMMA condition
  private static boolean action_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "action_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && condition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [PRIVATE_KEYWORD] action_rule (action_rule | nonbacktrackable_predicate_rule)*
  public static boolean actor_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actor_definition")) return false;
    if (!nextTokenIs(b, "<actor definition>", IDENTIFIER, PRIVATE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ACTOR_DEFINITION, "<actor definition>");
    r = actor_definition_0(b, l + 1);
    r = r && action_rule(b, l + 1);
    r = r && actor_definition_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [PRIVATE_KEYWORD]
  private static boolean actor_definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actor_definition_0")) return false;
    consumeToken(b, PRIVATE_KEYWORD);
    return true;
  }

  // (action_rule | nonbacktrackable_predicate_rule)*
  private static boolean actor_definition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actor_definition_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!actor_definition_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "actor_definition_2", c)) break;
    }
    return true;
  }

  // action_rule | nonbacktrackable_predicate_rule
  private static boolean actor_definition_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actor_definition_2_0")) return false;
    boolean r;
    r = action_rule(b, l + 1);
    if (!r) r = nonbacktrackable_predicate_rule(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // multiplicative_expression ((PLUS | CONCAT_OP | MINUS) multiplicative_expression)*
  public static boolean additive_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDITIVE_EXPRESSION, "<additive expression>");
    r = multiplicative_expression(b, l + 1);
    r = r && additive_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((PLUS | CONCAT_OP | MINUS) multiplicative_expression)*
  private static boolean additive_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!additive_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "additive_expression_1", c)) break;
    }
    return true;
  }

  // (PLUS | CONCAT_OP | MINUS) multiplicative_expression
  private static boolean additive_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = additive_expression_1_0_0(b, l + 1);
    r = r && multiplicative_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PLUS | CONCAT_OP | MINUS
  private static boolean additive_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, CONCAT_OP);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // not_constr (HASH_AND_OP not_constr)*
  public static boolean and_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AND_CONSTR, "<and constr>");
    r = not_constr(b, l + 1);
    r = r && and_constr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (HASH_AND_OP not_constr)*
  private static boolean and_constr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!and_constr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "and_constr_1", c)) break;
    }
    return true;
  }

  // HASH_AND_OP not_constr
  private static boolean and_constr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_AND_OP);
    r = r && not_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // shift_expression (BITWISE_AND shift_expression)*
  public static boolean and_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AND_EXPRESSION, "<and expression>");
    r = shift_expression(b, l + 1);
    r = r && and_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (BITWISE_AND shift_expression)*
  private static boolean and_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!and_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "and_expression_1", c)) break;
    }
    return true;
  }

  // BITWISE_AND shift_expression
  private static boolean and_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITWISE_AND);
    r = r && shift_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // negative_goal
  public static boolean argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT, "<argument>");
    r = negative_goal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LBRACE argument (COMMA argument)* RBRACE
  public static boolean array_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && argument(b, l + 1);
    r = r && array_expression_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, ARRAY_EXPRESSION, r);
    return r;
  }

  // (COMMA argument)*
  private static boolean array_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_expression_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_expression_2", c)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean array_expression_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VARIABLE AT term [AT]
  public static boolean as_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_pattern")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, AT);
    r = r && term(b, l + 1);
    r = r && as_pattern_3(b, l + 1);
    exit_section_(b, m, AS_PATTERN, r);
    return r;
  }

  // [AT]
  private static boolean as_pattern_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_pattern_3")) return false;
    consumeToken(b, AT);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean atom(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ATOM, r);
    return r;
  }

  /* ********************************************************** */
  // atom [LPAR [term (COMMA term)*] RPAR]
  public static boolean atom_or_call_no_lambda(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atom(b, l + 1);
    r = r && atom_or_call_no_lambda_1(b, l + 1);
    exit_section_(b, m, ATOM_OR_CALL_NO_LAMBDA, r);
    return r;
  }

  // [LPAR [term (COMMA term)*] RPAR]
  private static boolean atom_or_call_no_lambda_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1")) return false;
    atom_or_call_no_lambda_1_0(b, l + 1);
    return true;
  }

  // LPAR [term (COMMA term)*] RPAR
  private static boolean atom_or_call_no_lambda_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && atom_or_call_no_lambda_1_0_1(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // [term (COMMA term)*]
  private static boolean atom_or_call_no_lambda_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1_0_1")) return false;
    atom_or_call_no_lambda_1_0_1_0(b, l + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean atom_or_call_no_lambda_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = term(b, l + 1);
    r = r && atom_or_call_no_lambda_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA term)*
  private static boolean atom_or_call_no_lambda_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!atom_or_call_no_lambda_1_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "atom_or_call_no_lambda_1_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean atom_or_call_no_lambda_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_or_call_no_lambda_1_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // atom !(LPAR)
  public static boolean atom_without_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_without_args")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atom(b, l + 1);
    r = r && atom_without_args_1(b, l + 1);
    exit_section_(b, m, ATOM_WITHOUT_ARGS, r);
    return r;
  }

  // !(LPAR)
  private static boolean atom_without_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_without_args_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, LPAR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // parenthesized_goal
  //                   | variable_index
  //                   | as_pattern
  //                   | VARIABLE
  //                   | INTEGER
  //                   | FLOAT
  //                   | atom_without_args
  //                   | function_call
  //                   | list_expression
  //                   | array_expression
  //                   | lambda_term
  //                   | term_constructor
  public static boolean base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BASE_EXPRESSION, "<base expression>");
    r = parenthesized_goal(b, l + 1);
    if (!r) r = variable_index(b, l + 1);
    if (!r) r = as_pattern(b, l + 1);
    if (!r) r = consumeToken(b, VARIABLE);
    if (!r) r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = atom_without_args(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = list_expression(b, l + 1);
    if (!r) r = array_expression(b, l + 1);
    if (!r) r = lambda_term(b, l + 1);
    if (!r) r = term_constructor(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EQUAL | NOT_EQUAL | ASSIGN_OP | IDENTICAL | NOT_IDENTICAL
  //              | GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | LESS_EQUAL_PROLOG
  //              | IN_KEYWORD | HASH_EQUAL_OP | HASH_NOT_EQUAL_OP | HASH_GREATER_OP
  //              | HASH_GREATER_EQUAL_OP | HASH_LESS_OP | HASH_LESS_EQUAL_OP | HASH_LESS_EQUAL_ALT_OP
  public static boolean bin_rel_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bin_rel_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BIN_REL_OP, "<bin rel op>");
    r = consumeToken(b, EQUAL);
    if (!r) r = consumeToken(b, NOT_EQUAL);
    if (!r) r = consumeToken(b, ASSIGN_OP);
    if (!r) r = consumeToken(b, IDENTICAL);
    if (!r) r = consumeToken(b, NOT_IDENTICAL);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, GREATER_EQUAL);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, LESS_EQUAL);
    if (!r) r = consumeToken(b, LESS_EQUAL_PROLOG);
    if (!r) r = consumeToken(b, IN_KEYWORD);
    if (!r) r = consumeToken(b, HASH_EQUAL_OP);
    if (!r) r = consumeToken(b, HASH_NOT_EQUAL_OP);
    if (!r) r = consumeToken(b, HASH_GREATER_OP);
    if (!r) r = consumeToken(b, HASH_GREATER_EQUAL_OP);
    if (!r) r = consumeToken(b, HASH_LESS_OP);
    if (!r) r = consumeToken(b, HASH_LESS_EQUAL_OP);
    if (!r) r = consumeToken(b, HASH_LESS_EQUAL_ALT_OP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // goal
  public static boolean body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BODY, "<body>");
    r = goal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CATCH_KEYWORD LPAR exception_pattern RPAR goal
  public static boolean catch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause")) return false;
    if (!nextTokenIs(b, CATCH_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CATCH_KEYWORD, LPAR);
    r = r && exception_pattern(b, l + 1);
    r = r && consumeToken(b, RPAR);
    r = r && goal(b, l + 1);
    exit_section_(b, m, CATCH_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // goal
  public static boolean condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "condition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONDITION, "<condition>");
    r = goal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // negative_goal (COMMA conjunctive_goal)*
  public static boolean conjunctive_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conjunctive_goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONJUNCTIVE_GOAL, "<conjunctive goal>");
    r = negative_goal(b, l + 1);
    r = r && conjunctive_goal_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA conjunctive_goal)*
  private static boolean conjunctive_goal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conjunctive_goal_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!conjunctive_goal_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "conjunctive_goal_1", c)) break;
    }
    return true;
  }

  // COMMA conjunctive_goal
  private static boolean conjunctive_goal_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conjunctive_goal_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && conjunctive_goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // conjunctive_goal (SEMICOLON disjunctive_goal)*
  public static boolean disjunctive_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "disjunctive_goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DISJUNCTIVE_GOAL, "<disjunctive goal>");
    r = conjunctive_goal(b, l + 1);
    r = r && disjunctive_goal_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (SEMICOLON disjunctive_goal)*
  private static boolean disjunctive_goal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "disjunctive_goal_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!disjunctive_goal_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "disjunctive_goal_1", c)) break;
    }
    return true;
  }

  // SEMICOLON disjunctive_goal
  private static boolean disjunctive_goal_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "disjunctive_goal_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    r = r && disjunctive_goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // if_then_else
  //                 | foreach_loop
  //                 | while_loop
  //                 | loop_while
  //                 | try_catch
  //                 | type_annotation
  //                 | expression_with_relations
  public static boolean enclosed_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enclosed_goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENCLOSED_GOAL, "<enclosed goal>");
    r = if_then_else(b, l + 1);
    if (!r) r = foreach_loop(b, l + 1);
    if (!r) r = while_loop(b, l + 1);
    if (!r) r = loop_while(b, l + 1);
    if (!r) r = try_catch(b, l + 1);
    if (!r) r = type_annotation(b, l + 1);
    if (!r) r = expression_with_relations(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // impl_constr (HASH_BICONDITIONAL_OP impl_constr)*
  public static boolean equiv_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equiv_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQUIV_CONSTR, "<equiv constr>");
    r = impl_constr(b, l + 1);
    r = r && equiv_constr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (HASH_BICONDITIONAL_OP impl_constr)*
  private static boolean equiv_constr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equiv_constr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!equiv_constr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "equiv_constr_1", c)) break;
    }
    return true;
  }

  // HASH_BICONDITIONAL_OP impl_constr
  private static boolean equiv_constr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equiv_constr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_BICONDITIONAL_OP);
    r = r && impl_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // term (COMMA term)*
  public static boolean event_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "event_pattern")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EVENT_PATTERN, "<event pattern>");
    r = term(b, l + 1);
    r = r && event_pattern_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA term)*
  private static boolean event_pattern_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "event_pattern_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!event_pattern_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "event_pattern_1", c)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean event_pattern_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "event_pattern_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // term
  public static boolean exception_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exception_pattern")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXCEPTION_PATTERN, "<exception pattern>");
    r = term(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // range_expression
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = range_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression (bin_rel_op expression)*
  public static boolean expression_with_relations(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_relations")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION_WITH_RELATIONS, "<expression with relations>");
    r = expression(b, l + 1);
    r = r && expression_with_relations_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (bin_rel_op expression)*
  private static boolean expression_with_relations_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_relations_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression_with_relations_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expression_with_relations_1", c)) break;
    }
    return true;
  }

  // bin_rel_op expression
  private static boolean expression_with_relations_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_relations_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bin_rel_op(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FOREACH_KEYWORD LPAR iterator (COMMA (iterator | condition))* RPAR goal END_KEYWORD
  public static boolean foreach_loop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_loop")) return false;
    if (!nextTokenIs(b, FOREACH_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, FOREACH_KEYWORD, LPAR);
    r = r && iterator(b, l + 1);
    r = r && foreach_loop_3(b, l + 1);
    r = r && consumeToken(b, RPAR);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, END_KEYWORD);
    exit_section_(b, m, FOREACH_LOOP, r);
    return r;
  }

  // (COMMA (iterator | condition))*
  private static boolean foreach_loop_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_loop_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreach_loop_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreach_loop_3", c)) break;
    }
    return true;
  }

  // COMMA (iterator | condition)
  private static boolean foreach_loop_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_loop_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && foreach_loop_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // iterator | condition
  private static boolean foreach_loop_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_loop_3_0_1")) return false;
    boolean r;
    r = iterator(b, l + 1);
    if (!r) r = condition(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // atom LPAR [argument (COMMA argument)*] RPAR
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atom(b, l + 1);
    r = r && consumeToken(b, LPAR);
    r = r && function_call_2(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // [argument (COMMA argument)*]
  private static boolean function_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2")) return false;
    function_call_2_0(b, l + 1);
    return true;
  }

  // argument (COMMA argument)*
  private static boolean function_call_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument(b, l + 1);
    r = r && function_call_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argument)*
  private static boolean function_call_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_call_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_call_2_0_1", c)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean function_call_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // atom LPAR [term (COMMA term)*] RPAR
  public static boolean function_call_no_dot(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_no_dot")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atom(b, l + 1);
    r = r && consumeToken(b, LPAR);
    r = r && function_call_no_dot_2(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, FUNCTION_CALL_NO_DOT, r);
    return r;
  }

  // [term (COMMA term)*]
  private static boolean function_call_no_dot_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_no_dot_2")) return false;
    function_call_no_dot_2_0(b, l + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean function_call_no_dot_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_no_dot_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = term(b, l + 1);
    r = r && function_call_no_dot_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA term)*
  private static boolean function_call_no_dot_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_no_dot_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_call_no_dot_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_call_no_dot_2_0_1", c)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean function_call_no_dot_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_no_dot_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // function_rule | function_fact
  public static boolean function_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_clause")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_rule(b, l + 1);
    if (!r) r = function_fact(b, l + 1);
    exit_section_(b, m, FUNCTION_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // function_directive* function_clause
  public static boolean function_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DEFINITION, "<function definition>");
    r = function_definition_0(b, l + 1);
    r = r && function_clause(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // function_directive*
  private static boolean function_definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_directive(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_definition_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PRIVATE_KEYWORD | TABLE_KEYWORD
  public static boolean function_directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_directive")) return false;
    if (!nextTokenIs(b, "<function directive>", PRIVATE_KEYWORD, TABLE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DIRECTIVE, "<function directive>");
    r = consumeToken(b, PRIVATE_KEYWORD);
    if (!r) r = consumeToken(b, TABLE_KEYWORD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // head EQUAL argument DOT
  public static boolean function_fact(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_fact")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_FACT, null);
    r = head(b, l + 1);
    r = r && consumeToken(b, EQUAL);
    p = r; // pin = 2
    r = r && report_error_(b, argument(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // head EQUAL VARIABLE [COMMA condition] ARROW_OP body DOT
  public static boolean function_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_rule")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_RULE, null);
    r = head(b, l + 1);
    r = r && consumeTokens(b, 1, EQUAL, VARIABLE);
    p = r; // pin = 2
    r = r && report_error_(b, function_rule_3(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ARROW_OP)) && r;
    r = p && report_error_(b, body(b, l + 1)) && r;
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [COMMA condition]
  private static boolean function_rule_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_rule_3")) return false;
    function_rule_3_0(b, l + 1);
    return true;
  }

  // COMMA condition
  private static boolean function_rule_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_rule_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && condition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // disjunctive_goal
  public static boolean goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GOAL, "<goal>");
    r = disjunctive_goal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // atom [LPAR [argument (COMMA argument)*] RPAR]
  public static boolean head(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atom(b, l + 1);
    r = r && head_1(b, l + 1);
    exit_section_(b, m, HEAD, r);
    return r;
  }

  // [LPAR [argument (COMMA argument)*] RPAR]
  private static boolean head_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1")) return false;
    head_1_0(b, l + 1);
    return true;
  }

  // LPAR [argument (COMMA argument)*] RPAR
  private static boolean head_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && head_1_0_1(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // [argument (COMMA argument)*]
  private static boolean head_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0_1")) return false;
    head_1_0_1_0(b, l + 1);
    return true;
  }

  // argument (COMMA argument)*
  private static boolean head_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument(b, l + 1);
    r = r && head_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argument)*
  private static boolean head_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!head_1_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "head_1_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean head_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IF_KEYWORD goal THEN_KEYWORD goal (ELSEIF_KEYWORD goal THEN_KEYWORD goal)* ELSE_KEYWORD goal END_KEYWORD
  public static boolean if_then_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else")) return false;
    if (!nextTokenIs(b, IF_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, THEN_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && if_then_else_4(b, l + 1);
    r = r && consumeToken(b, ELSE_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, END_KEYWORD);
    exit_section_(b, m, IF_THEN_ELSE, r);
    return r;
  }

  // (ELSEIF_KEYWORD goal THEN_KEYWORD goal)*
  private static boolean if_then_else_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!if_then_else_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_then_else_4", c)) break;
    }
    return true;
  }

  // ELSEIF_KEYWORD goal THEN_KEYWORD goal
  private static boolean if_then_else_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSEIF_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, THEN_KEYWORD);
    r = r && goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // or_constr (HASH_ARROW_OP or_constr)*
  public static boolean impl_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "impl_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPL_CONSTR, "<impl constr>");
    r = or_constr(b, l + 1);
    r = r && impl_constr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (HASH_ARROW_OP or_constr)*
  private static boolean impl_constr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "impl_constr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!impl_constr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "impl_constr_1", c)) break;
    }
    return true;
  }

  // HASH_ARROW_OP or_constr
  private static boolean impl_constr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "impl_constr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_ARROW_OP);
    r = r && or_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IMPORT_KEYWORD import_item (COMMA import_item)* DOT
  public static boolean import_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_declaration")) return false;
    if (!nextTokenIs(b, IMPORT_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, null);
    r = consumeToken(b, IMPORT_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, import_item(b, l + 1));
    r = p && report_error_(b, import_declaration_2(b, l + 1)) && r;
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA import_item)*
  private static boolean import_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_declaration_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!import_declaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "import_declaration_2", c)) break;
    }
    return true;
  }

  // COMMA import_item
  private static boolean import_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_declaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && import_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (atom | QUALIFIED_ATOM) [DIV_RIGHT INTEGER]
  public static boolean import_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item")) return false;
    if (!nextTokenIs(b, "<import item>", IDENTIFIER, QUALIFIED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_ITEM, "<import item>");
    r = import_item_0(b, l + 1);
    r = r && import_item_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // atom | QUALIFIED_ATOM
  private static boolean import_item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item_0")) return false;
    boolean r;
    r = atom(b, l + 1);
    if (!r) r = consumeToken(b, QUALIFIED_ATOM);
    return r;
  }

  // [DIV_RIGHT INTEGER]
  private static boolean import_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item_1")) return false;
    parseTokens(b, 0, DIV_RIGHT, INTEGER);
    return true;
  }

  /* ********************************************************** */
  // INCLUDE_KEYWORD STRING (COMMA STRING)* DOT
  public static boolean include_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_declaration")) return false;
    if (!nextTokenIs(b, INCLUDE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_DECLARATION, null);
    r = consumeTokens(b, 1, INCLUDE_KEYWORD, STRING);
    p = r; // pin = 1
    r = r && report_error_(b, include_declaration_2(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA STRING)*
  private static boolean include_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_declaration_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!include_declaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "include_declaration_2", c)) break;
    }
    return true;
  }

  // COMMA STRING
  private static boolean include_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_declaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PLUS | MINUS
  public static boolean index_mode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_mode")) return false;
    if (!nextTokenIs(b, "<index mode>", MINUS, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INDEX_MODE, "<index mode>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // variable IN_KEYWORD expression | variable EQUAL expression
  public static boolean iterator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iterator")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = iterator_0(b, l + 1);
    if (!r) r = iterator_1(b, l + 1);
    exit_section_(b, m, ITERATOR, r);
    return r;
  }

  // variable IN_KEYWORD expression
  private static boolean iterator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iterator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, IN_KEYWORD);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // variable EQUAL expression
  private static boolean iterator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iterator_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, EQUAL);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LAMBDA_KEYWORD LPAR variable_list COMMA argument RPAR
  public static boolean lambda_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_term")) return false;
    if (!nextTokenIs(b, LAMBDA_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LAMBDA_KEYWORD, LPAR);
    r = r && variable_list(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, LAMBDA_TERM, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET argument list_expression_suffix RBRACKET
  public static boolean list_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && argument(b, l + 1);
    r = r && list_expression_suffix(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET [term (COMMA term)*] [PIPE term] RBRACKET
  public static boolean list_expression_no_comprehension(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && list_expression_no_comprehension_1(b, l + 1);
    r = r && list_expression_no_comprehension_2(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_EXPRESSION_NO_COMPREHENSION, r);
    return r;
  }

  // [term (COMMA term)*]
  private static boolean list_expression_no_comprehension_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_1")) return false;
    list_expression_no_comprehension_1_0(b, l + 1);
    return true;
  }

  // term (COMMA term)*
  private static boolean list_expression_no_comprehension_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = term(b, l + 1);
    r = r && list_expression_no_comprehension_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA term)*
  private static boolean list_expression_no_comprehension_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_expression_no_comprehension_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_expression_no_comprehension_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA term
  private static boolean list_expression_no_comprehension_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PIPE term]
  private static boolean list_expression_no_comprehension_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_2")) return false;
    list_expression_no_comprehension_2_0(b, l + 1);
    return true;
  }

  // PIPE term
  private static boolean list_expression_no_comprehension_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_no_comprehension_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COLON iterator (COMMA (iterator | condition))*  // list comprehension
  //                         | (COMMA argument)* [PIPE argument]
  public static boolean list_expression_suffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LIST_EXPRESSION_SUFFIX, "<list expression suffix>");
    r = list_expression_suffix_0(b, l + 1);
    if (!r) r = list_expression_suffix_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COLON iterator (COMMA (iterator | condition))*
  private static boolean list_expression_suffix_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && iterator(b, l + 1);
    r = r && list_expression_suffix_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA (iterator | condition))*
  private static boolean list_expression_suffix_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_expression_suffix_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_expression_suffix_0_2", c)) break;
    }
    return true;
  }

  // COMMA (iterator | condition)
  private static boolean list_expression_suffix_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && list_expression_suffix_0_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // iterator | condition
  private static boolean list_expression_suffix_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_0_2_0_1")) return false;
    boolean r;
    r = iterator(b, l + 1);
    if (!r) r = condition(b, l + 1);
    return r;
  }

  // (COMMA argument)* [PIPE argument]
  private static boolean list_expression_suffix_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = list_expression_suffix_1_0(b, l + 1);
    r = r && list_expression_suffix_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argument)*
  private static boolean list_expression_suffix_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_1_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_expression_suffix_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_expression_suffix_1_0", c)) break;
    }
    return true;
  }

  // COMMA argument
  private static boolean list_expression_suffix_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PIPE argument]
  private static boolean list_expression_suffix_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_1_1")) return false;
    list_expression_suffix_1_1_0(b, l + 1);
    return true;
  }

  // PIPE argument
  private static boolean list_expression_suffix_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_suffix_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LOOP_KEYWORD goal WHILE_KEYWORD LPAR goal RPAR
  public static boolean loop_while(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_while")) return false;
    if (!nextTokenIs(b, LOOP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOOP_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && consumeTokens(b, 0, WHILE_KEYWORD, LPAR);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, LOOP_WHILE, r);
    return r;
  }

  /* ********************************************************** */
  // MODULE_KEYWORD atom DOT
  public static boolean module_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_declaration")) return false;
    if (!nextTokenIs(b, MODULE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_DECLARATION, null);
    r = consumeToken(b, MODULE_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, atom(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // unary_expression ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression)*
  public static boolean multiplicative_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MULTIPLICATIVE_EXPRESSION, "<multiplicative expression>");
    r = unary_expression(b, l + 1);
    r = r && multiplicative_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression)*
  private static boolean multiplicative_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!multiplicative_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multiplicative_expression_1", c)) break;
    }
    return true;
  }

  // (MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD) unary_expression
  private static boolean multiplicative_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = multiplicative_expression_1_0_0(b, l + 1);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MULTIPLY | DIVIDE | INT_DIVIDE | DIV_RIGHT | DIV_LEFT | DIV_KEYWORD | MOD_KEYWORD | REM_KEYWORD
  private static boolean multiplicative_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, MULTIPLY);
    if (!r) r = consumeToken(b, DIVIDE);
    if (!r) r = consumeToken(b, INT_DIVIDE);
    if (!r) r = consumeToken(b, DIV_RIGHT);
    if (!r) r = consumeToken(b, DIV_LEFT);
    if (!r) r = consumeToken(b, DIV_KEYWORD);
    if (!r) r = consumeToken(b, MOD_KEYWORD);
    if (!r) r = consumeToken(b, REM_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // (NOT_KEYWORD negative_goal) | equiv_constr
  public static boolean negative_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negative_goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NEGATIVE_GOAL, "<negative goal>");
    r = negative_goal_0(b, l + 1);
    if (!r) r = equiv_constr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NOT_KEYWORD negative_goal
  private static boolean negative_goal_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negative_goal_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT_KEYWORD);
    r = r && negative_goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // head [COMMA condition] ARROW_OP body DOT
  public static boolean nonbacktrackable_predicate_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nonbacktrackable_predicate_rule")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = head(b, l + 1);
    r = r && nonbacktrackable_predicate_rule_1(b, l + 1);
    r = r && consumeToken(b, ARROW_OP);
    r = r && body(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, NONBACKTRACKABLE_PREDICATE_RULE, r);
    return r;
  }

  // [COMMA condition]
  private static boolean nonbacktrackable_predicate_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nonbacktrackable_predicate_rule_1")) return false;
    nonbacktrackable_predicate_rule_1_0(b, l + 1);
    return true;
  }

  // COMMA condition
  private static boolean nonbacktrackable_predicate_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nonbacktrackable_predicate_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && condition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (HASH_NOT_OP not_constr) | enclosed_goal
  public static boolean not_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NOT_CONSTR, "<not constr>");
    r = not_constr_0(b, l + 1);
    if (!r) r = enclosed_goal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // HASH_NOT_OP not_constr
  private static boolean not_constr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_constr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_NOT_OP);
    r = r && not_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // xor_constr (HASH_OR_OP xor_constr)*
  public static boolean or_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OR_CONSTR, "<or constr>");
    r = xor_constr(b, l + 1);
    r = r && or_constr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (HASH_OR_OP xor_constr)*
  private static boolean or_constr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!or_constr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "or_constr_1", c)) break;
    }
    return true;
  }

  // HASH_OR_OP xor_constr
  private static boolean or_constr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_OR_OP);
    r = r && xor_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // xor_expression (BITWISE_OR xor_expression)*
  public static boolean or_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OR_EXPRESSION, "<or expression>");
    r = xor_expression(b, l + 1);
    r = r && or_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (BITWISE_OR xor_expression)*
  private static boolean or_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!or_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "or_expression_1", c)) break;
    }
    return true;
  }

  // BITWISE_OR xor_expression
  private static boolean or_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITWISE_OR);
    r = r && xor_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAR goal RPAR
  public static boolean parenthesized_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_goal")) return false;
    if (!nextTokenIs(b, LPAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, PARENTHESIZED_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // primary_expression [POWER unary_expression]
  public static boolean power_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POWER_EXPRESSION, "<power expression>");
    r = primary_expression(b, l + 1);
    r = r && power_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [POWER unary_expression]
  private static boolean power_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression_1")) return false;
    power_expression_1_0(b, l + 1);
    return true;
  }

  // POWER unary_expression
  private static boolean power_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POWER);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // predicate_rule | predicate_fact
  public static boolean predicate_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_clause")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = predicate_rule(b, l + 1);
    if (!r) r = predicate_fact(b, l + 1);
    exit_section_(b, m, PREDICATE_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // predicate_directive* predicate_clause
  public static boolean predicate_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_DEFINITION, "<predicate definition>");
    r = predicate_definition_0(b, l + 1);
    r = r && predicate_clause(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // predicate_directive*
  private static boolean predicate_definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_definition_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!predicate_directive(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "predicate_definition_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PRIVATE_KEYWORD
  //                      | TABLE_KEYWORD [LPAR table_mode (COMMA table_mode)* RPAR]
  //                      | INDEX_KEYWORD LPAR index_mode (COMMA index_mode)* RPAR
  public static boolean predicate_directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_DIRECTIVE, "<predicate directive>");
    r = consumeToken(b, PRIVATE_KEYWORD);
    if (!r) r = predicate_directive_1(b, l + 1);
    if (!r) r = predicate_directive_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TABLE_KEYWORD [LPAR table_mode (COMMA table_mode)* RPAR]
  private static boolean predicate_directive_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TABLE_KEYWORD);
    r = r && predicate_directive_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [LPAR table_mode (COMMA table_mode)* RPAR]
  private static boolean predicate_directive_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_1_1")) return false;
    predicate_directive_1_1_0(b, l + 1);
    return true;
  }

  // LPAR table_mode (COMMA table_mode)* RPAR
  private static boolean predicate_directive_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && table_mode(b, l + 1);
    r = r && predicate_directive_1_1_0_2(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA table_mode)*
  private static boolean predicate_directive_1_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_1_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!predicate_directive_1_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "predicate_directive_1_1_0_2", c)) break;
    }
    return true;
  }

  // COMMA table_mode
  private static boolean predicate_directive_1_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_1_1_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && table_mode(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // INDEX_KEYWORD LPAR index_mode (COMMA index_mode)* RPAR
  private static boolean predicate_directive_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, INDEX_KEYWORD, LPAR);
    r = r && index_mode(b, l + 1);
    r = r && predicate_directive_2_3(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA index_mode)*
  private static boolean predicate_directive_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_2_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!predicate_directive_2_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "predicate_directive_2_3", c)) break;
    }
    return true;
  }

  // COMMA index_mode
  private static boolean predicate_directive_2_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_directive_2_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && index_mode(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // head DOT
  public static boolean predicate_fact(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_fact")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = head(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, PREDICATE_FACT, r);
    return r;
  }

  /* ********************************************************** */
  // head [COMMA condition] (ARROW_OP | BACKTRACKABLE_ARROW_OP) body DOT
  public static boolean predicate_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_rule")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_RULE, null);
    r = head(b, l + 1);
    r = r && predicate_rule_1(b, l + 1);
    r = r && predicate_rule_2(b, l + 1);
    p = r; // pin = 3
    r = r && report_error_(b, body(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [COMMA condition]
  private static boolean predicate_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_rule_1")) return false;
    predicate_rule_1_0(b, l + 1);
    return true;
  }

  // COMMA condition
  private static boolean predicate_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && condition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ARROW_OP | BACKTRACKABLE_ARROW_OP
  private static boolean predicate_rule_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_rule_2")) return false;
    boolean r;
    r = consumeToken(b, ARROW_OP);
    if (!r) r = consumeToken(b, BACKTRACKABLE_ARROW_OP);
    return r;
  }

  /* ********************************************************** */
  // base_expression
  public static boolean primary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY_EXPRESSION, "<primary expression>");
    r = base_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // program_item*
  static boolean program(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "program")) return false;
    while (true) {
      int c = current_position_(b);
      if (!program_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "program", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // module_declaration
  //                | import_declaration
  //                | include_declaration
  //                | predicate_definition
  //                | function_definition
  //                | actor_definition
  public static boolean program_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "program_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROGRAM_ITEM, "<program item>");
    r = module_declaration(b, l + 1);
    if (!r) r = import_declaration(b, l + 1);
    if (!r) r = include_declaration(b, l + 1);
    if (!r) r = predicate_definition(b, l + 1);
    if (!r) r = function_definition(b, l + 1);
    if (!r) r = actor_definition(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // or_expression [RANGE_OP or_expression [RANGE_OP or_expression]]
  public static boolean range_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RANGE_EXPRESSION, "<range expression>");
    r = or_expression(b, l + 1);
    r = r && range_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [RANGE_OP or_expression [RANGE_OP or_expression]]
  private static boolean range_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1")) return false;
    range_expression_1_0(b, l + 1);
    return true;
  }

  // RANGE_OP or_expression [RANGE_OP or_expression]
  private static boolean range_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RANGE_OP);
    r = r && or_expression(b, l + 1);
    r = r && range_expression_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [RANGE_OP or_expression]
  private static boolean range_expression_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1_0_2")) return false;
    range_expression_1_0_2_0(b, l + 1);
    return true;
  }

  // RANGE_OP or_expression
  private static boolean range_expression_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RANGE_OP);
    r = r && or_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // additive_expression ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression)*
  public static boolean shift_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_EXPRESSION, "<shift expression>");
    r = additive_expression(b, l + 1);
    r = r && shift_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression)*
  private static boolean shift_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shift_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shift_expression_1", c)) break;
    }
    return true;
  }

  // (SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE) additive_expression
  private static boolean shift_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = shift_expression_1_0_0(b, l + 1);
    r = r && additive_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE
  private static boolean shift_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, SHIFT_LEFT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT_TRIPLE);
    return r;
  }

  /* ********************************************************** */
  // PLUS | MINUS | MIN_KEYWORD | MAX_KEYWORD
  public static boolean table_mode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_mode")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_MODE, "<table mode>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, MIN_KEYWORD);
    if (!r) r = consumeToken(b, MAX_KEYWORD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LPAR term RPAR
  //   | variable_as_pattern
  //   | VARIABLE
  //   | INTEGER
  //   | FLOAT
  //   | atom_or_call_no_lambda
  //   | list_expression_no_comprehension
  //   | array_expression
  //   | function_call_no_dot
  //   | term_constructor
  public static boolean term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TERM, "<term>");
    r = term_0(b, l + 1);
    if (!r) r = variable_as_pattern(b, l + 1);
    if (!r) r = consumeToken(b, VARIABLE);
    if (!r) r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = atom_or_call_no_lambda(b, l + 1);
    if (!r) r = list_expression_no_comprehension(b, l + 1);
    if (!r) r = array_expression(b, l + 1);
    if (!r) r = function_call_no_dot(b, l + 1);
    if (!r) r = term_constructor(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LPAR term RPAR
  private static boolean term_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && term(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DOLLAR goal DOLLAR
  public static boolean term_constructor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_constructor")) return false;
    if (!nextTokenIs(b, DOLLAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOLLAR);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, DOLLAR);
    exit_section_(b, m, TERM_CONSTRUCTOR, r);
    return r;
  }

  /* ********************************************************** */
  // TRY_KEYWORD goal catch_clause* [FINALLY_KEYWORD goal] END_KEYWORD
  public static boolean try_catch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch")) return false;
    if (!nextTokenIs(b, TRY_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TRY_KEYWORD);
    r = r && goal(b, l + 1);
    r = r && try_catch_2(b, l + 1);
    r = r && try_catch_3(b, l + 1);
    r = r && consumeToken(b, END_KEYWORD);
    exit_section_(b, m, TRY_CATCH, r);
    return r;
  }

  // catch_clause*
  private static boolean try_catch_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!catch_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "try_catch_2", c)) break;
    }
    return true;
  }

  // [FINALLY_KEYWORD goal]
  private static boolean try_catch_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch_3")) return false;
    try_catch_3_0(b, l + 1);
    return true;
  }

  // FINALLY_KEYWORD goal
  private static boolean try_catch_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FINALLY_KEYWORD);
    r = r && goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VARIABLE DOUBLE_COLON_OP expression
  public static boolean type_annotation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_annotation")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, DOUBLE_COLON_OP);
    r = r && expression(b, l + 1);
    exit_section_(b, m, TYPE_ANNOTATION, r);
    return r;
  }

  /* ********************************************************** */
  // power_expression
  //                    | PLUS unary_expression
  //                    | MINUS unary_expression
  //                    | COMPLEMENT unary_expression
  public static boolean unary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_EXPRESSION, "<unary expression>");
    r = power_expression(b, l + 1);
    if (!r) r = unary_expression_1(b, l + 1);
    if (!r) r = unary_expression_2(b, l + 1);
    if (!r) r = unary_expression_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PLUS unary_expression
  private static boolean unary_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PLUS);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MINUS unary_expression
  private static boolean unary_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MINUS);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMPLEMENT unary_expression
  private static boolean unary_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMPLEMENT);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VARIABLE AT term [AT]
  public static boolean variable_as_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_as_pattern")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, AT);
    r = r && term(b, l + 1);
    r = r && variable_as_pattern_3(b, l + 1);
    exit_section_(b, m, VARIABLE_AS_PATTERN, r);
    return r;
  }

  // [AT]
  private static boolean variable_as_pattern_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_as_pattern_3")) return false;
    consumeToken(b, AT);
    return true;
  }

  /* ********************************************************** */
  // VARIABLE LBRACKET argument [COMMA argument] RBRACKET
  public static boolean variable_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_index")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, LBRACKET);
    r = r && argument(b, l + 1);
    r = r && variable_index_3(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, VARIABLE_INDEX, r);
    return r;
  }

  // [COMMA argument]
  private static boolean variable_index_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_index_3")) return false;
    variable_index_3_0(b, l + 1);
    return true;
  }

  // COMMA argument
  private static boolean variable_index_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_index_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET [variable (COMMA variable)*] RBRACKET
  public static boolean variable_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && variable_list_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, VARIABLE_LIST, r);
    return r;
  }

  // [variable (COMMA variable)*]
  private static boolean variable_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1")) return false;
    variable_list_1_0(b, l + 1);
    return true;
  }

  // variable (COMMA variable)*
  private static boolean variable_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    r = r && variable_list_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA variable)*
  private static boolean variable_list_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variable_list_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variable_list_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA variable
  private static boolean variable_list_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, VARIABLE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WHILE_KEYWORD LPAR goal RPAR [LOOP_KEYWORD] goal END_KEYWORD
  public static boolean while_loop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_loop")) return false;
    if (!nextTokenIs(b, WHILE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, WHILE_KEYWORD, LPAR);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, RPAR);
    r = r && while_loop_4(b, l + 1);
    r = r && goal(b, l + 1);
    r = r && consumeToken(b, END_KEYWORD);
    exit_section_(b, m, WHILE_LOOP, r);
    return r;
  }

  // [LOOP_KEYWORD]
  private static boolean while_loop_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_loop_4")) return false;
    consumeToken(b, LOOP_KEYWORD);
    return true;
  }

  /* ********************************************************** */
  // and_constr (HASH_XOR_OP and_constr)*
  public static boolean xor_constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_constr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, XOR_CONSTR, "<xor constr>");
    r = and_constr(b, l + 1);
    r = r && xor_constr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (HASH_XOR_OP and_constr)*
  private static boolean xor_constr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_constr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!xor_constr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "xor_constr_1", c)) break;
    }
    return true;
  }

  // HASH_XOR_OP and_constr
  private static boolean xor_constr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_constr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HASH_XOR_OP);
    r = r && and_constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // and_expression (BITWISE_XOR and_expression)*
  public static boolean xor_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, XOR_EXPRESSION, "<xor expression>");
    r = and_expression(b, l + 1);
    r = r && xor_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (BITWISE_XOR and_expression)*
  private static boolean xor_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!xor_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "xor_expression_1", c)) break;
    }
    return true;
  }

  // BITWISE_XOR and_expression
  private static boolean xor_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITWISE_XOR);
    r = r && and_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
