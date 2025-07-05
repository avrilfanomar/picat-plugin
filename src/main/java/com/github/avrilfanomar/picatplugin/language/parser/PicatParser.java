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
    return picatFile(b, l + 1);
  }

  /* ********************************************************** */
  // multiplicative_expression additive_expression_rest*
  public static boolean additive_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDITIVE_EXPRESSION, "<additive expression>");
    r = multiplicative_expression(b, l + 1);
    r = r && additive_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // additive_expression_rest*
  private static boolean additive_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!additive_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "additive_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( PLUS | MINUS ) multiplicative_expression
  public static boolean additive_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_rest")) return false;
    if (!nextTokenIs(b, "<additive expression rest>", MINUS, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDITIVE_EXPRESSION_REST, "<additive expression rest>");
    r = additive_expression_rest_0(b, l + 1);
    r = r && multiplicative_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PLUS | MINUS
  private static boolean additive_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // expression ( COMMA expression )*
  public static boolean argument_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, "<argument list>");
    r = expression(b, l + 1);
    r = r && argument_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA expression )*
  private static boolean argument_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argument_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_list_1", c)) break;
    }
    return true;
  }

  // COMMA expression
  private static boolean argument_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable AT pattern
  public static boolean as_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_pattern")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, AT);
    r = r && pattern(b, l + 1);
    exit_section_(b, m, AS_PATTERN, r);
    return r;
  }

  /* ********************************************************** */
  // variable AT pattern
  public static boolean as_pattern_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_pattern_expression")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, AT);
    r = r && pattern(b, l + 1);
    exit_section_(b, m, AS_PATTERN_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // variable | atom_expression
  public static boolean assignable_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignable_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNABLE_EXPRESSION, "<assignable expression>");
    r = consumeToken(b, VARIABLE);
    if (!r) r = atom_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // assignable_expression ASSIGN_OP expression
  public static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT, "<assignment>");
    r = assignable_expression(b, l + 1);
    r = r && consumeToken(b, ASSIGN_OP);
    r = r && expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER | QUOTED_ATOM
  public static boolean atom(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom")) return false;
    if (!nextTokenIs(b, "<atom>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATOM, "<atom>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, QUOTED_ATOM);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // base_expression postfix_expression*
  public static boolean atom_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATOM_EXPRESSION, "<atom expression>");
    r = base_expression(b, l + 1);
    r = r && atom_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // postfix_expression*
  private static boolean atom_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!postfix_expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "atom_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // atom
  public static boolean atom_no_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atom_no_args")) return false;
    if (!nextTokenIs(b, "<atom no args>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATOM_NO_ARGS, "<atom no args>");
    r = atom(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // term_constructor_expression
  //                   | as_pattern_expression
  //                   | simple_number_range
  //                   | list_comprehension_expression
  //                   | list_expression
  //                   | tuple
  //                   | map
  //                   | lambda_expression
  //                   | dollar_term_constructor
  //                   | parenthesized_expression
  //                   | STRING
  //                   | atom
  //                   | number
  //                   | variable
  public static boolean base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BASE_EXPRESSION, "<base expression>");
    r = term_constructor_expression(b, l + 1);
    if (!r) r = as_pattern_expression(b, l + 1);
    if (!r) r = simple_number_range(b, l + 1);
    if (!r) r = list_comprehension_expression(b, l + 1);
    if (!r) r = list_expression(b, l + 1);
    if (!r) r = tuple(b, l + 1);
    if (!r) r = map(b, l + 1);
    if (!r) r = lambda_expression(b, l + 1);
    if (!r) r = dollar_term_constructor(b, l + 1);
    if (!r) r = parenthesized_expression(b, l + 1);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = atom(b, l + 1);
    if (!r) r = number(b, l + 1);
    if (!r) r = consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // logical_or_expression biconditional_expression_rest*
  public static boolean biconditional_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "biconditional_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BICONDITIONAL_EXPRESSION, "<biconditional expression>");
    r = logical_or_expression(b, l + 1);
    r = r && biconditional_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // biconditional_expression_rest*
  private static boolean biconditional_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "biconditional_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!biconditional_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "biconditional_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( BICONDITIONAL_OP | HASH_BICONDITIONAL_OP | BACKTRACKABLE_BICONDITIONAL_OP ) logical_or_expression
  public static boolean biconditional_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "biconditional_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BICONDITIONAL_EXPRESSION_REST, "<biconditional expression rest>");
    r = biconditional_expression_rest_0(b, l + 1);
    r = r && logical_or_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BICONDITIONAL_OP | HASH_BICONDITIONAL_OP | BACKTRACKABLE_BICONDITIONAL_OP
  private static boolean biconditional_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "biconditional_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, BICONDITIONAL_OP);
    if (!r) r = consumeToken(b, HASH_BICONDITIONAL_OP);
    if (!r) r = consumeToken(b, BACKTRACKABLE_BICONDITIONAL_OP);
    return r;
  }

  /* ********************************************************** */
  // equality_expression bitwise_and_expression_rest*
  public static boolean bitwise_and_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_AND_EXPRESSION, "<bitwise and expression>");
    r = equality_expression(b, l + 1);
    r = r && bitwise_and_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // bitwise_and_expression_rest*
  private static boolean bitwise_and_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_and_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_and_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // AMPERSAND equality_expression
  public static boolean bitwise_and_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expression_rest")) return false;
    if (!nextTokenIs(b, AMPERSAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMPERSAND);
    r = r && equality_expression(b, l + 1);
    exit_section_(b, m, BITWISE_AND_EXPRESSION_REST, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_xor_expression bitwise_or_expression_rest*
  public static boolean bitwise_or_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_OR_EXPRESSION, "<bitwise or expression>");
    r = bitwise_xor_expression(b, l + 1);
    r = r && bitwise_or_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // bitwise_or_expression_rest*
  private static boolean bitwise_or_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_or_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_or_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PIPE bitwise_xor_expression
  public static boolean bitwise_or_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expression_rest")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && bitwise_xor_expression(b, l + 1);
    exit_section_(b, m, BITWISE_OR_EXPRESSION_REST, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_and_expression bitwise_xor_expression_rest*
  public static boolean bitwise_xor_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_XOR_EXPRESSION, "<bitwise xor expression>");
    r = bitwise_and_expression(b, l + 1);
    r = r && bitwise_xor_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // bitwise_xor_expression_rest*
  private static boolean bitwise_xor_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_xor_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_xor_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( CARET | XOR_KEYWORD | HASH_CARET_OP ) bitwise_and_expression
  public static boolean bitwise_xor_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_XOR_EXPRESSION_REST, "<bitwise xor expression rest>");
    r = bitwise_xor_expression_rest_0(b, l + 1);
    r = r && bitwise_and_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CARET | XOR_KEYWORD | HASH_CARET_OP
  private static boolean bitwise_xor_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, CARET);
    if (!r) r = consumeToken(b, XOR_KEYWORD);
    if (!r) r = consumeToken(b, HASH_CARET_OP);
    return r;
  }

  /* ********************************************************** */
  // goal ( goal_separator goal )*
  public static boolean body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BODY, "<body>");
    r = goal(b, l + 1);
    r = r && body_1(b, l + 1);
    exit_section_(b, l, m, r, false, PicatParser::body_recover);
    return r;
  }

  // ( goal_separator goal )*
  private static boolean body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "body_1", c)) break;
    }
    return true;
  }

  // goal_separator goal
  private static boolean body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = goal_separator(b, l + 1);
    r = r && goal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(DOT | SEMICOLON | END_KEYWORD | EOF)
  static boolean body_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !body_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // DOT | SEMICOLON | END_KEYWORD | EOF
  private static boolean body_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_recover_0")) return false;
    boolean r;
    r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, END_KEYWORD);
    if (!r) r = consumeToken(b, EOF);
    return r;
  }

  /* ********************************************************** */
  // BREAK_KEYWORD
  public static boolean break_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_stmt")) return false;
    if (!nextTokenIs(b, BREAK_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BREAK_KEYWORD);
    exit_section_(b, m, BREAK_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // pattern ARROW_OP body
  public static boolean case_arm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arm")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CASE_ARM, "<case arm>");
    r = pattern(b, l + 1);
    r = r && consumeToken(b, ARROW_OP);
    r = r && body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // case_arm ( SEMICOLON case_arm )*
  public static boolean case_arms(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arms")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CASE_ARMS, "<case arms>");
    r = case_arm(b, l + 1);
    r = r && case_arms_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( SEMICOLON case_arm )*
  private static boolean case_arms_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arms_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!case_arms_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_arms_1", c)) break;
    }
    return true;
  }

  // SEMICOLON case_arm
  private static boolean case_arms_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arms_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    r = r && case_arm(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CASE_KEYWORD expression OF_KEYWORD case_arms END_KEYWORD
  public static boolean case_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expression")) return false;
    if (!nextTokenIs(b, CASE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CASE_EXPRESSION, null);
    r = consumeToken(b, CASE_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1));
    r = p && report_error_(b, consumeToken(b, OF_KEYWORD)) && r;
    r = p && report_error_(b, case_arms(b, l + 1)) && r;
    r = p && consumeToken(b, END_KEYWORD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // CATCH_KEYWORD pattern ARROW_OP body
  public static boolean catch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause")) return false;
    if (!nextTokenIs(b, CATCH_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CATCH_KEYWORD);
    r = r && pattern(b, l + 1);
    r = r && consumeToken(b, ARROW_OP);
    r = r && body(b, l + 1);
    exit_section_(b, m, CATCH_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // catch_clause+
  public static boolean catch_clauses(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clauses")) return false;
    if (!nextTokenIs(b, CATCH_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clause(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!catch_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "catch_clauses", c)) break;
    }
    exit_section_(b, m, CATCH_CLAUSES, r);
    return r;
  }

  /* ********************************************************** */
  // expression ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL | HASH_LESS_EQUAL_OP | IDENTICAL | NOT_IDENTICAL | HASH_EQUAL_OP ) expression
  public static boolean comparison(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPARISON, "<comparison>");
    r = expression(b, l + 1);
    r = r && comparison_1(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LESS | GREATER | LESS_EQUAL | GREATER_EQUAL | HASH_LESS_EQUAL_OP | IDENTICAL | NOT_IDENTICAL | HASH_EQUAL_OP
  private static boolean comparison_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_1")) return false;
    boolean r;
    r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, LESS_EQUAL);
    if (!r) r = consumeToken(b, GREATER_EQUAL);
    if (!r) r = consumeToken(b, HASH_LESS_EQUAL_OP);
    if (!r) r = consumeToken(b, IDENTICAL);
    if (!r) r = consumeToken(b, NOT_IDENTICAL);
    if (!r) r = consumeToken(b, HASH_EQUAL_OP);
    return r;
  }

  /* ********************************************************** */
  // [PRIVATE_KEYWORD] (table_mode | index_mode) DOT
  public static boolean compilation_directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compilation_directive")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILATION_DIRECTIVE, "<compilation directive>");
    r = compilation_directive_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, compilation_directive_1(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [PRIVATE_KEYWORD]
  private static boolean compilation_directive_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compilation_directive_0")) return false;
    consumeToken(b, PRIVATE_KEYWORD);
    return true;
  }

  // table_mode | index_mode
  private static boolean compilation_directive_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compilation_directive_1")) return false;
    boolean r;
    r = table_mode(b, l + 1);
    if (!r) r = index_mode(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // shift_expression concatenation_expression_rest*
  public static boolean concatenation_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "concatenation_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONCATENATION_EXPRESSION, "<concatenation expression>");
    r = shift_expression(b, l + 1);
    r = r && concatenation_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // concatenation_expression_rest*
  private static boolean concatenation_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "concatenation_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!concatenation_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "concatenation_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CONCAT_OP shift_expression
  public static boolean concatenation_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "concatenation_expression_rest")) return false;
    if (!nextTokenIs(b, CONCAT_OP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONCAT_OP);
    r = r && shift_expression(b, l + 1);
    exit_section_(b, m, CONCATENATION_EXPRESSION_REST, r);
    return r;
  }

  /* ********************************************************** */
  // biconditional_expression [ QUESTION_MARK expression COLON expression ]
  public static boolean conditional_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONDITIONAL_EXPRESSION, "<conditional expression>");
    r = biconditional_expression(b, l + 1);
    r = r && conditional_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ QUESTION_MARK expression COLON expression ]
  private static boolean conditional_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expression_1")) return false;
    conditional_expression_1_0(b, l + 1);
    return true;
  }

  // QUESTION_MARK expression COLON expression
  private static boolean conditional_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CONTINUE_KEYWORD
  public static boolean continue_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continue_stmt")) return false;
    if (!nextTokenIs(b, CONTINUE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONTINUE_KEYWORD);
    exit_section_(b, m, CONTINUE_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // CUT
  public static boolean cut_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cut_goal")) return false;
    if (!nextTokenIs(b, CUT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CUT);
    exit_section_(b, m, CUT_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // DOLLAR goal
  public static boolean dollar_term_constructor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dollar_term_constructor")) return false;
    if (!nextTokenIs(b, DOLLAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOLLAR);
    r = r && goal(b, l + 1);
    exit_section_(b, m, DOLLAR_TERM_CONSTRUCTOR, r);
    return r;
  }

  /* ********************************************************** */
  // ELSEIF_KEYWORD expression THEN_KEYWORD body
  public static boolean elseif_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause")) return false;
    if (!nextTokenIs(b, ELSEIF_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSEIF_KEYWORD);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, THEN_KEYWORD);
    r = r && body(b, l + 1);
    exit_section_(b, m, ELSEIF_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // elseif_clause+
  public static boolean elseif_clauses(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clauses")) return false;
    if (!nextTokenIs(b, ELSEIF_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = elseif_clause(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!elseif_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "elseif_clauses", c)) break;
    }
    exit_section_(b, m, ELSEIF_CLAUSES, r);
    return r;
  }

  /* ********************************************************** */
  // relational_expression equality_expression_rest*
  public static boolean equality_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQUALITY_EXPRESSION, "<equality expression>");
    r = relational_expression(b, l + 1);
    r = r && equality_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // equality_expression_rest*
  private static boolean equality_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!equality_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "equality_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( IDENTICAL | NOT_IDENTICAL | HASH_EQUAL_OP | HASH_NOT_EQUAL_OP | EQUAL | NOT_EQUAL ) relational_expression
  public static boolean equality_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQUALITY_EXPRESSION_REST, "<equality expression rest>");
    r = equality_expression_rest_0(b, l + 1);
    r = r && relational_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IDENTICAL | NOT_IDENTICAL | HASH_EQUAL_OP | HASH_NOT_EQUAL_OP | EQUAL | NOT_EQUAL
  private static boolean equality_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, IDENTICAL);
    if (!r) r = consumeToken(b, NOT_IDENTICAL);
    if (!r) r = consumeToken(b, HASH_EQUAL_OP);
    if (!r) r = consumeToken(b, HASH_NOT_EQUAL_OP);
    if (!r) r = consumeToken(b, EQUAL);
    if (!r) r = consumeToken(b, NOT_EQUAL);
    return r;
  }

  /* ********************************************************** */
  // export_spec ( COMMA export_spec )*
  public static boolean export_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_list")) return false;
    if (!nextTokenIs(b, "<export list>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPORT_LIST, "<export list>");
    r = export_spec(b, l + 1);
    r = r && export_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA export_spec )*
  private static boolean export_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!export_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "export_list_1", c)) break;
    }
    return true;
  }

  // COMMA export_spec
  private static boolean export_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && export_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // predicate_signature | atom
  public static boolean export_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_spec")) return false;
    if (!nextTokenIs(b, "<export spec>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPORT_SPEC, "<export spec>");
    r = predicate_signature(b, l + 1);
    if (!r) r = atom(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EXPORT_KEYWORD export_list DOT
  public static boolean export_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_statement")) return false;
    if (!nextTokenIs(b, EXPORT_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EXPORT_STATEMENT, null);
    r = consumeToken(b, EXPORT_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, export_list(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // conditional_expression
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = conditional_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // FAIL_KEYWORD
  public static boolean fail_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fail_goal")) return false;
    if (!nextTokenIs(b, FAIL_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FAIL_KEYWORD);
    exit_section_(b, m, FAIL_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // FALSE_KEYWORD
  public static boolean false_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "false_goal")) return false;
    if (!nextTokenIs(b, FALSE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FALSE_KEYWORD);
    exit_section_(b, m, FALSE_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // FIELD_ACCESS
  public static boolean field_access_suffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_access_suffix")) return false;
    if (!nextTokenIs(b, FIELD_ACCESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FIELD_ACCESS);
    exit_section_(b, m, FIELD_ACCESS_SUFFIX, r);
    return r;
  }

  /* ********************************************************** */
  // STRING | atom
  public static boolean file_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FILE_SPEC, "<file spec>");
    r = consumeToken(b, STRING);
    if (!r) r = atom(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // variable IN_KEYWORD expression | variable EQUAL expression
  public static boolean foreach_generator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generator")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = foreach_generator_0(b, l + 1);
    if (!r) r = foreach_generator_1(b, l + 1);
    exit_section_(b, m, FOREACH_GENERATOR, r);
    return r;
  }

  // variable IN_KEYWORD expression
  private static boolean foreach_generator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, IN_KEYWORD);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // variable EQUAL expression
  private static boolean foreach_generator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generator_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLE, EQUAL);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // foreach_generator ( COMMA foreach_generator )*
  public static boolean foreach_generators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generators")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = foreach_generator(b, l + 1);
    r = r && foreach_generators_1(b, l + 1);
    exit_section_(b, m, FOREACH_GENERATORS, r);
    return r;
  }

  // ( COMMA foreach_generator )*
  private static boolean foreach_generators_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generators_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreach_generators_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreach_generators_1", c)) break;
    }
    return true;
  }

  // COMMA foreach_generator
  private static boolean foreach_generators_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_generators_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && foreach_generator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FOREACH_KEYWORD LPAR foreach_generators RPAR body END_KEYWORD
  public static boolean foreach_loop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_loop")) return false;
    if (!nextTokenIs(b, FOREACH_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_LOOP, null);
    r = consumeTokens(b, 1, FOREACH_KEYWORD, LPAR);
    p = r; // pin = 1
    r = r && report_error_(b, foreach_generators(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RPAR)) && r;
    r = p && report_error_(b, body(b, l + 1)) && r;
    r = p && consumeToken(b, END_KEYWORD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // expression [ rule_operator body ]
  public static boolean function_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_BODY, "<function body>");
    r = expression(b, l + 1);
    r = r && function_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ rule_operator body ]
  private static boolean function_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_body_1")) return false;
    function_body_1_0(b, l + 1);
    return true;
  }

  // rule_operator body
  private static boolean function_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rule_operator(b, l + 1);
    r = r && body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAR [argument_list] RPAR
  public static boolean function_call_suffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_suffix")) return false;
    if (!nextTokenIs(b, LPAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && function_call_suffix_1(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, FUNCTION_CALL_SUFFIX, r);
    return r;
  }

  // [argument_list]
  private static boolean function_call_suffix_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_suffix_1")) return false;
    argument_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // function_rule | function_fact
  public static boolean function_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_clause")) return false;
    if (!nextTokenIs(b, "<function clause>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_CLAUSE, "<function clause>");
    r = function_rule(b, l + 1);
    if (!r) r = function_fact(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // head EQUAL expression DOT
  public static boolean function_fact(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_fact")) return false;
    if (!nextTokenIs(b, "<function fact>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_FACT, "<function fact>");
    r = head(b, l + 1);
    r = r && consumeToken(b, EQUAL);
    p = r; // pin = 2
    r = r && report_error_(b, expression(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // head EQUAL function_body DOT
  public static boolean function_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_rule")) return false;
    if (!nextTokenIs(b, "<function rule>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_RULE, "<function rule>");
    r = head(b, l + 1);
    r = r && consumeToken(b, EQUAL);
    p = r; // pin = 2
    r = r && report_error_(b, function_body(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // import_statement | export_statement | include_statement | using_statement | compilation_directive
  public static boolean general_directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "general_directive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GENERAL_DIRECTIVE, "<general directive>");
    r = import_statement(b, l + 1);
    if (!r) r = export_statement(b, l + 1);
    if (!r) r = include_statement(b, l + 1);
    if (!r) r = using_statement(b, l + 1);
    if (!r) r = compilation_directive(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // if_then_else
  //        | case_expression
  //        | try_catch
  //        | assignment
  //        | type_annotation
  //        | unification
  //        | comparison
  //        | negation
  //        | fail_goal
  //        | pass_goal
  //        | true_goal
  //        | false_goal
  //        | foreach_loop
  //        | while_loop
  //        | loop_while_statement
  //        | list_comprehension_goal
  //        | cut_goal
  //        | return_stmt
  //        | continue_stmt
  //        | break_stmt
  //        | throw_stmt
  //        | procedure_call
  //        | expression
  public static boolean goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GOAL, "<goal>");
    r = if_then_else(b, l + 1);
    if (!r) r = case_expression(b, l + 1);
    if (!r) r = try_catch(b, l + 1);
    if (!r) r = assignment(b, l + 1);
    if (!r) r = type_annotation(b, l + 1);
    if (!r) r = unification(b, l + 1);
    if (!r) r = comparison(b, l + 1);
    if (!r) r = negation(b, l + 1);
    if (!r) r = fail_goal(b, l + 1);
    if (!r) r = pass_goal(b, l + 1);
    if (!r) r = true_goal(b, l + 1);
    if (!r) r = false_goal(b, l + 1);
    if (!r) r = foreach_loop(b, l + 1);
    if (!r) r = while_loop(b, l + 1);
    if (!r) r = loop_while_statement(b, l + 1);
    if (!r) r = list_comprehension_goal(b, l + 1);
    if (!r) r = cut_goal(b, l + 1);
    if (!r) r = return_stmt(b, l + 1);
    if (!r) r = continue_stmt(b, l + 1);
    if (!r) r = break_stmt(b, l + 1);
    if (!r) r = throw_stmt(b, l + 1);
    if (!r) r = procedure_call(b, l + 1);
    if (!r) r = expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SEMICOLON | COMMA
  public static boolean goal_separator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goal_separator")) return false;
    if (!nextTokenIs(b, "<goal separator>", COMMA, SEMICOLON)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GOAL_SEPARATOR, "<goal separator>");
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, COMMA);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ( structure | atom_no_args | qualified_atom ) [ COMMA head_args ]
  public static boolean head(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head")) return false;
    if (!nextTokenIs(b, "<head>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEAD, "<head>");
    r = head_0(b, l + 1);
    r = r && head_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // structure | atom_no_args | qualified_atom
  private static boolean head_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_0")) return false;
    boolean r;
    r = structure(b, l + 1);
    if (!r) r = atom_no_args(b, l + 1);
    if (!r) r = qualified_atom(b, l + 1);
    return r;
  }

  // [ COMMA head_args ]
  private static boolean head_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1")) return false;
    head_1_0(b, l + 1);
    return true;
  }

  // COMMA head_args
  private static boolean head_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && head_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expression ( COMMA expression )*
  public static boolean head_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEAD_ARGS, "<head args>");
    r = expression(b, l + 1);
    r = r && head_args_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA expression )*
  private static boolean head_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_args_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!head_args_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "head_args_1", c)) break;
    }
    return true;
  }

  // COMMA expression
  private static boolean head_args_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_args_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // atom ["/" INTEGER]
  public static boolean head_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference")) return false;
    if (!nextTokenIs(b, "<head reference>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEAD_REFERENCE, "<head reference>");
    r = atom(b, l + 1);
    r = r && head_reference_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ["/" INTEGER]
  private static boolean head_reference_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference_1")) return false;
    parseTokens(b, 0, DIVIDE, INTEGER);
    return true;
  }

  /* ********************************************************** */
  // head_reference ( (COMMA | SEMICOLON) head_reference )*
  public static boolean head_reference_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference_list")) return false;
    if (!nextTokenIs(b, "<head reference list>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEAD_REFERENCE_LIST, "<head reference list>");
    r = head_reference(b, l + 1);
    r = r && head_reference_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( (COMMA | SEMICOLON) head_reference )*
  private static boolean head_reference_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!head_reference_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "head_reference_list_1", c)) break;
    }
    return true;
  }

  // (COMMA | SEMICOLON) head_reference
  private static boolean head_reference_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = head_reference_list_1_0_0(b, l + 1);
    r = r && head_reference(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA | SEMICOLON
  private static boolean head_reference_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "head_reference_list_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // IF_KEYWORD expression THEN_KEYWORD body [elseif_clauses] [ELSE_KEYWORD body] END_KEYWORD
  public static boolean if_then_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else")) return false;
    if (!nextTokenIs(b, IF_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_THEN_ELSE, null);
    r = consumeToken(b, IF_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1));
    r = p && report_error_(b, consumeToken(b, THEN_KEYWORD)) && r;
    r = p && report_error_(b, body(b, l + 1)) && r;
    r = p && report_error_(b, if_then_else_4(b, l + 1)) && r;
    r = p && report_error_(b, if_then_else_5(b, l + 1)) && r;
    r = p && consumeToken(b, END_KEYWORD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [elseif_clauses]
  private static boolean if_then_else_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else_4")) return false;
    elseif_clauses(b, l + 1);
    return true;
  }

  // [ELSE_KEYWORD body]
  private static boolean if_then_else_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else_5")) return false;
    if_then_else_5_0(b, l + 1);
    return true;
  }

  // ELSE_KEYWORD body
  private static boolean if_then_else_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_then_else_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE_KEYWORD);
    r = r && body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // module_name [ "=>" rename_list ]
  public static boolean import_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item")) return false;
    if (!nextTokenIs(b, "<import item>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_ITEM, "<import item>");
    r = module_name(b, l + 1);
    r = r && import_item_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "=>" rename_list ]
  private static boolean import_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item_1")) return false;
    import_item_1_0(b, l + 1);
    return true;
  }

  // "=>" rename_list
  private static boolean import_item_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_item_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARROW_OP);
    r = r && rename_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // import_item ( COMMA import_item )*
  public static boolean import_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_list")) return false;
    if (!nextTokenIs(b, "<import list>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_LIST, "<import list>");
    r = import_item(b, l + 1);
    r = r && import_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA import_item )*
  private static boolean import_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!import_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "import_list_1", c)) break;
    }
    return true;
  }

  // COMMA import_item
  private static boolean import_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && import_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IMPORT_KEYWORD import_list DOT
  public static boolean import_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_statement")) return false;
    if (!nextTokenIs(b, IMPORT_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_STATEMENT, null);
    r = consumeToken(b, IMPORT_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, import_list(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INCLUDE_KEYWORD file_spec DOT
  public static boolean include_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "include_statement")) return false;
    if (!nextTokenIs(b, INCLUDE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_STATEMENT, null);
    r = consumeToken(b, INCLUDE_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, file_spec(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LBRACKET expression [ COMMA expression ] RBRACKET
  public static boolean index_access_suffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_access_suffix")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expression(b, l + 1);
    r = r && index_access_suffix_2(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, INDEX_ACCESS_SUFFIX, r);
    return r;
  }

  // [ COMMA expression ]
  private static boolean index_access_suffix_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_access_suffix_2")) return false;
    index_access_suffix_2_0(b, l + 1);
    return true;
  }

  // COMMA expression
  private static boolean index_access_suffix_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_access_suffix_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INDEX_KEYWORD head_reference_list [indexing_details]
  public static boolean index_mode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_mode")) return false;
    if (!nextTokenIs(b, INDEX_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INDEX_KEYWORD);
    r = r && head_reference_list(b, l + 1);
    r = r && index_mode_2(b, l + 1);
    exit_section_(b, m, INDEX_MODE, r);
    return r;
  }

  // [indexing_details]
  private static boolean index_mode_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_mode_2")) return false;
    indexing_details(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LPAR argument_list RPAR
  public static boolean indexing_details(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexing_details")) return false;
    if (!nextTokenIs(b, LPAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && argument_list(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, INDEXING_DETAILS, r);
    return r;
  }

  /* ********************************************************** */
  // function_clause | predicate_clause | COMMENT | MULTI_LINE_COMMENT | general_directive
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = function_clause(b, l + 1);
    if (!r) r = predicate_clause(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, MULTI_LINE_COMMENT);
    if (!r) r = general_directive(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // LBRACE [variable_list] RBRACE ARROW_OP (expression | body)
  public static boolean lambda_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_expression")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && lambda_expression_1(b, l + 1);
    r = r && consumeTokens(b, 0, RBRACE, ARROW_OP);
    r = r && lambda_expression_4(b, l + 1);
    exit_section_(b, m, LAMBDA_EXPRESSION, r);
    return r;
  }

  // [variable_list]
  private static boolean lambda_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_expression_1")) return false;
    variable_list(b, l + 1);
    return true;
  }

  // expression | body
  private static boolean lambda_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_expression_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1);
    if (!r) r = body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET expression ( COLON | PIPE ) foreach_generators RBRACKET
  public static boolean list_comprehension_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_comprehension_expression")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expression(b, l + 1);
    r = r && list_comprehension_expression_2(b, l + 1);
    r = r && foreach_generators(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_COMPREHENSION_EXPRESSION, r);
    return r;
  }

  // COLON | PIPE
  private static boolean list_comprehension_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_comprehension_expression_2")) return false;
    boolean r;
    r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, PIPE);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET expression PIPE foreach_generators RBRACKET
  public static boolean list_comprehension_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_comprehension_goal")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PIPE);
    r = r && foreach_generators(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_COMPREHENSION_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET [list_items] RBRACKET
  public static boolean list_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && list_expression_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_EXPRESSION, r);
    return r;
  }

  // [list_items]
  private static boolean list_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_expression_1")) return false;
    list_items(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expression ( ( COMMA | SEMICOLON ) expression )* [ PIPE expression ]
  public static boolean list_items(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LIST_ITEMS, "<list items>");
    r = expression(b, l + 1);
    r = r && list_items_1(b, l + 1);
    r = r && list_items_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ( COMMA | SEMICOLON ) expression )*
  private static boolean list_items_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_items_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_items_1", c)) break;
    }
    return true;
  }

  // ( COMMA | SEMICOLON ) expression
  private static boolean list_items_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = list_items_1_0_0(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA | SEMICOLON
  private static boolean list_items_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  // [ PIPE expression ]
  private static boolean list_items_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items_2")) return false;
    list_items_2_0(b, l + 1);
    return true;
  }

  // PIPE expression
  private static boolean list_items_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_items_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET [pattern_list] [PIPE pattern] RBRACKET
  public static boolean list_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_pattern")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && list_pattern_1(b, l + 1);
    r = r && list_pattern_2(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, LIST_PATTERN, r);
    return r;
  }

  // [pattern_list]
  private static boolean list_pattern_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_pattern_1")) return false;
    pattern_list(b, l + 1);
    return true;
  }

  // [PIPE pattern]
  private static boolean list_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_pattern_2")) return false;
    list_pattern_2_0(b, l + 1);
    return true;
  }

  // PIPE pattern
  private static boolean list_pattern_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_pattern_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && pattern(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_or_expression logical_and_expression_rest*
  public static boolean logical_and_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGICAL_AND_EXPRESSION, "<logical and expression>");
    r = bitwise_or_expression(b, l + 1);
    r = r && logical_and_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // logical_and_expression_rest*
  private static boolean logical_and_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logical_and_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_and_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( AND_KEYWORD | HASH_AND_OP ) bitwise_or_expression
  public static boolean logical_and_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expression_rest")) return false;
    if (!nextTokenIs(b, "<logical and expression rest>", AND_KEYWORD, HASH_AND_OP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGICAL_AND_EXPRESSION_REST, "<logical and expression rest>");
    r = logical_and_expression_rest_0(b, l + 1);
    r = r && bitwise_or_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // AND_KEYWORD | HASH_AND_OP
  private static boolean logical_and_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_and_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, AND_KEYWORD);
    if (!r) r = consumeToken(b, HASH_AND_OP);
    return r;
  }

  /* ********************************************************** */
  // logical_and_expression logical_or_expression_rest*
  public static boolean logical_or_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGICAL_OR_EXPRESSION, "<logical or expression>");
    r = logical_and_expression(b, l + 1);
    r = r && logical_or_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // logical_or_expression_rest*
  private static boolean logical_or_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logical_or_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_or_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( OR_KEYWORD | HASH_OR_OP ) logical_and_expression
  public static boolean logical_or_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expression_rest")) return false;
    if (!nextTokenIs(b, "<logical or expression rest>", HASH_OR_OP, OR_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGICAL_OR_EXPRESSION_REST, "<logical or expression rest>");
    r = logical_or_expression_rest_0(b, l + 1);
    r = r && logical_and_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OR_KEYWORD | HASH_OR_OP
  private static boolean logical_or_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_or_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, OR_KEYWORD);
    if (!r) r = consumeToken(b, HASH_OR_OP);
    return r;
  }

  /* ********************************************************** */
  // LOOP_KEYWORD body WHILE_KEYWORD LPAR expression RPAR DOT
  public static boolean loop_while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_while_statement")) return false;
    if (!nextTokenIs(b, LOOP_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOOP_WHILE_STATEMENT, null);
    r = consumeToken(b, LOOP_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, body(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, WHILE_KEYWORD, LPAR)) && r;
    r = p && report_error_(b, expression(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, RPAR, DOT)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LBRACE [map_entries] RBRACE
  public static boolean map(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && map_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, MAP, r);
    return r;
  }

  // [map_entries]
  private static boolean map_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_1")) return false;
    map_entries(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // map_entry ( COMMA map_entry )*
  public static boolean map_entries(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_entries")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRIES, "<map entries>");
    r = map_entry(b, l + 1);
    r = r && map_entries_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA map_entry )*
  private static boolean map_entries_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_entries_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!map_entries_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "map_entries_1", c)) break;
    }
    return true;
  }

  // COMMA map_entry
  private static boolean map_entries_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_entries_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && map_entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expression COLON expression
  public static boolean map_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_entry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRY, "<map entry>");
    r = expression(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MODULE_KEYWORD module_name DOT
  public static boolean module_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_declaration")) return false;
    if (!nextTokenIs(b, MODULE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_DECLARATION, null);
    r = consumeToken(b, MODULE_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, module_name(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // atom
  public static boolean module_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_name")) return false;
    if (!nextTokenIs(b, "<module name>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODULE_NAME, "<module name>");
    r = atom(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // power_expression multiplicative_expression_rest*
  public static boolean multiplicative_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MULTIPLICATIVE_EXPRESSION, "<multiplicative expression>");
    r = power_expression(b, l + 1);
    r = r && multiplicative_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // multiplicative_expression_rest*
  private static boolean multiplicative_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!multiplicative_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multiplicative_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( MULTIPLY | DIVIDE | INT_DIVIDE | MOD_KEYWORD | DIV_KEYWORD | REM_KEYWORD ) power_expression
  public static boolean multiplicative_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MULTIPLICATIVE_EXPRESSION_REST, "<multiplicative expression rest>");
    r = multiplicative_expression_rest_0(b, l + 1);
    r = r && power_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MULTIPLY | DIVIDE | INT_DIVIDE | MOD_KEYWORD | DIV_KEYWORD | REM_KEYWORD
  private static boolean multiplicative_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, MULTIPLY);
    if (!r) r = consumeToken(b, DIVIDE);
    if (!r) r = consumeToken(b, INT_DIVIDE);
    if (!r) r = consumeToken(b, MOD_KEYWORD);
    if (!r) r = consumeToken(b, DIV_KEYWORD);
    if (!r) r = consumeToken(b, REM_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // NOT_KEYWORD goal
  public static boolean negation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negation")) return false;
    if (!nextTokenIs(b, NOT_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT_KEYWORD);
    r = r && goal(b, l + 1);
    exit_section_(b, m, NEGATION, r);
    return r;
  }

  /* ********************************************************** */
  // INTEGER | FLOAT | HEX_INTEGER | OCTAL_INTEGER | BINARY_INTEGER
  public static boolean number(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMBER, "<number>");
    r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, HEX_INTEGER);
    if (!r) r = consumeToken(b, OCTAL_INTEGER);
    if (!r) r = consumeToken(b, BINARY_INTEGER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LPAR expression RPAR
  public static boolean parenthesized_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_expression")) return false;
    if (!nextTokenIs(b, LPAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAR);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, m, PARENTHESIZED_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // PASS_KEYWORD
  public static boolean pass_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pass_goal")) return false;
    if (!nextTokenIs(b, PASS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PASS_KEYWORD);
    exit_section_(b, m, PASS_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // variable | atom | number | structure_pattern | list_pattern | tuple_pattern | as_pattern
  public static boolean pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATTERN, "<pattern>");
    r = consumeToken(b, VARIABLE);
    if (!r) r = atom(b, l + 1);
    if (!r) r = number(b, l + 1);
    if (!r) r = structure_pattern(b, l + 1);
    if (!r) r = list_pattern(b, l + 1);
    if (!r) r = tuple_pattern(b, l + 1);
    if (!r) r = as_pattern(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // pattern ( COMMA pattern )*
  public static boolean pattern_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATTERN_LIST, "<pattern list>");
    r = pattern(b, l + 1);
    r = r && pattern_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA pattern )*
  private static boolean pattern_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!pattern_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pattern_list_1", c)) break;
    }
    return true;
  }

  // COMMA pattern
  private static boolean pattern_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && pattern(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // module_declaration? item_*
  static boolean picatFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "picatFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = picatFile_0(b, l + 1);
    r = r && picatFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // module_declaration?
  private static boolean picatFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "picatFile_0")) return false;
    module_declaration(b, l + 1);
    return true;
  }

  // item_*
  private static boolean picatFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "picatFile_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "picatFile_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // function_call_suffix | index_access_suffix | field_access_suffix
  public static boolean postfix_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POSTFIX_EXPRESSION, "<postfix expression>");
    r = function_call_suffix(b, l + 1);
    if (!r) r = index_access_suffix(b, l + 1);
    if (!r) r = field_access_suffix(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // unary_expression [ POWER power_expression ]
  public static boolean power_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POWER_EXPRESSION, "<power expression>");
    r = unary_expression(b, l + 1);
    r = r && power_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ POWER power_expression ]
  private static boolean power_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression_1")) return false;
    power_expression_1_0(b, l + 1);
    return true;
  }

  // POWER power_expression
  private static boolean power_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "power_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POWER);
    r = r && power_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // predicate_rule | predicate_fact
  public static boolean predicate_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_clause")) return false;
    if (!nextTokenIs(b, "<predicate clause>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_CLAUSE, "<predicate clause>");
    r = predicate_rule(b, l + 1);
    if (!r) r = predicate_fact(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // head DOT
  public static boolean predicate_fact(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_fact")) return false;
    if (!nextTokenIs(b, "<predicate fact>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_FACT, "<predicate fact>");
    r = head(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !DOT
  static boolean predicate_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, DOT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // head rule_operator body DOT
  public static boolean predicate_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_rule")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_RULE, "<predicate rule>");
    r = head(b, l + 1);
    r = r && rule_operator(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, body(b, l + 1));
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, PicatParser::predicate_recover);
    return r || p;
  }

  /* ********************************************************** */
  // atom "/" INTEGER
  public static boolean predicate_signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_signature")) return false;
    if (!nextTokenIs(b, "<predicate signature>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_SIGNATURE, "<predicate signature>");
    r = atom(b, l + 1);
    r = r && consumeTokens(b, 0, DIVIDE, INTEGER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // atom_expression
  public static boolean procedure_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROCEDURE_CALL, "<procedure call>");
    r = atom_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // atom DOT atom
  public static boolean qualified_atom(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_atom")) return false;
    if (!nextTokenIs(b, "<qualified atom>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUALIFIED_ATOM, "<qualified atom>");
    r = atom(b, l + 1);
    r = r && consumeToken(b, DOT);
    r = r && atom(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INTEGER | variable | atom | parenthesized_expression
  public static boolean range_operand(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_operand")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RANGE_OPERAND, "<range operand>");
    r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, VARIABLE);
    if (!r) r = atom(b, l + 1);
    if (!r) r = parenthesized_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // concatenation_expression relational_expression_rest*
  public static boolean relational_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RELATIONAL_EXPRESSION, "<relational expression>");
    r = concatenation_expression(b, l + 1);
    r = r && relational_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // relational_expression_rest*
  private static boolean relational_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!relational_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "relational_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL | HASH_LESS_EQUAL_OP | IS_KEYWORD ) concatenation_expression
  public static boolean relational_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RELATIONAL_EXPRESSION_REST, "<relational expression rest>");
    r = relational_expression_rest_0(b, l + 1);
    r = r && concatenation_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LESS | GREATER | LESS_EQUAL | GREATER_EQUAL | HASH_LESS_EQUAL_OP | IS_KEYWORD
  private static boolean relational_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, LESS_EQUAL);
    if (!r) r = consumeToken(b, GREATER_EQUAL);
    if (!r) r = consumeToken(b, HASH_LESS_EQUAL_OP);
    if (!r) r = consumeToken(b, IS_KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // rename_spec ( COMMA rename_spec )*
  public static boolean rename_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_list")) return false;
    if (!nextTokenIs(b, "<rename list>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RENAME_LIST, "<rename list>");
    r = rename_spec(b, l + 1);
    r = r && rename_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA rename_spec )*
  private static boolean rename_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!rename_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "rename_list_1", c)) break;
    }
    return true;
  }

  // COMMA rename_spec
  private static boolean rename_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && rename_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // atom [ "=>" atom ]
  public static boolean rename_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_spec")) return false;
    if (!nextTokenIs(b, "<rename spec>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RENAME_SPEC, "<rename spec>");
    r = atom(b, l + 1);
    r = r && rename_spec_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "=>" atom ]
  private static boolean rename_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_spec_1")) return false;
    rename_spec_1_0(b, l + 1);
    return true;
  }

  // "=>" atom
  private static boolean rename_spec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rename_spec_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARROW_OP);
    r = r && atom(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // RETURN_KEYWORD [expression]
  public static boolean return_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_stmt")) return false;
    if (!nextTokenIs(b, RETURN_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN_KEYWORD);
    r = r && return_stmt_1(b, l + 1);
    exit_section_(b, m, RETURN_STMT, r);
    return r;
  }

  // [expression]
  private static boolean return_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_stmt_1")) return false;
    expression(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ARROW_OP | BACKTRACKABLE_ARROW_OP | BICONDITIONAL_OP | BACKTRACKABLE_BICONDITIONAL_OP | RULE_OP
  public static boolean rule_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RULE_OPERATOR, "<rule operator>");
    r = consumeToken(b, ARROW_OP);
    if (!r) r = consumeToken(b, BACKTRACKABLE_ARROW_OP);
    if (!r) r = consumeToken(b, BICONDITIONAL_OP);
    if (!r) r = consumeToken(b, BACKTRACKABLE_BICONDITIONAL_OP);
    if (!r) r = consumeToken(b, RULE_OP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // additive_expression shift_expression_rest*
  public static boolean shift_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_EXPRESSION, "<shift expression>");
    r = additive_expression(b, l + 1);
    r = r && shift_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // shift_expression_rest*
  private static boolean shift_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shift_expression_rest(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shift_expression_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ( SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE_OP ) additive_expression
  public static boolean shift_expression_rest(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_rest")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_EXPRESSION_REST, "<shift expression rest>");
    r = shift_expression_rest_0(b, l + 1);
    r = r && additive_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SHIFT_LEFT | SHIFT_RIGHT | SHIFT_RIGHT_TRIPLE_OP
  private static boolean shift_expression_rest_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expression_rest_0")) return false;
    boolean r;
    r = consumeToken(b, SHIFT_LEFT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT_TRIPLE_OP);
    return r;
  }

  /* ********************************************************** */
  // range_operand RANGE_OP range_operand
  public static boolean simple_number_range(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_number_range")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_NUMBER_RANGE, "<simple number range>");
    r = range_operand(b, l + 1);
    r = r && consumeToken(b, RANGE_OP);
    r = r && range_operand(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // atom LPAR [argument_list] RPAR
  public static boolean structure(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure")) return false;
    if (!nextTokenIs(b, "<structure>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCTURE, "<structure>");
    r = atom(b, l + 1);
    r = r && consumeToken(b, LPAR);
    r = r && structure_2(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [argument_list]
  private static boolean structure_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_2")) return false;
    argument_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // atom LPAR [pattern_list] RPAR
  public static boolean structure_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_pattern")) return false;
    if (!nextTokenIs(b, "<structure pattern>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCTURE_PATTERN, "<structure pattern>");
    r = atom(b, l + 1);
    r = r && consumeToken(b, LPAR);
    r = r && structure_pattern_2(b, l + 1);
    r = r && consumeToken(b, RPAR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [pattern_list]
  private static boolean structure_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_pattern_2")) return false;
    pattern_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TABLE_KEYWORD head_reference_list
  public static boolean table_mode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_mode")) return false;
    if (!nextTokenIs(b, TABLE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TABLE_KEYWORD);
    r = r && head_reference_list(b, l + 1);
    exit_section_(b, m, TABLE_MODE, r);
    return r;
  }

  /* ********************************************************** */
  // (atom | qualified_atom) LBRACE map_entries RBRACE
  public static boolean term_constructor_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_constructor_expression")) return false;
    if (!nextTokenIs(b, "<term constructor expression>", IDENTIFIER, QUOTED_ATOM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TERM_CONSTRUCTOR_EXPRESSION, "<term constructor expression>");
    r = term_constructor_expression_0(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && map_entries(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // atom | qualified_atom
  private static boolean term_constructor_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_constructor_expression_0")) return false;
    boolean r;
    r = atom(b, l + 1);
    if (!r) r = qualified_atom(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // THROW_KEYWORD expression
  public static boolean throw_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throw_stmt")) return false;
    if (!nextTokenIs(b, THROW_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, THROW_KEYWORD);
    r = r && expression(b, l + 1);
    exit_section_(b, m, THROW_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // TRUE_KEYWORD
  public static boolean true_goal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "true_goal")) return false;
    if (!nextTokenIs(b, TRUE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TRUE_KEYWORD);
    exit_section_(b, m, TRUE_GOAL, r);
    return r;
  }

  /* ********************************************************** */
  // TRY_KEYWORD body catch_clauses [FINALLY_KEYWORD body] END_KEYWORD
  public static boolean try_catch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch")) return false;
    if (!nextTokenIs(b, TRY_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRY_CATCH, null);
    r = consumeToken(b, TRY_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, body(b, l + 1));
    r = p && report_error_(b, catch_clauses(b, l + 1)) && r;
    r = p && report_error_(b, try_catch_3(b, l + 1)) && r;
    r = p && consumeToken(b, END_KEYWORD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [FINALLY_KEYWORD body]
  private static boolean try_catch_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch_3")) return false;
    try_catch_3_0(b, l + 1);
    return true;
  }

  // FINALLY_KEYWORD body
  private static boolean try_catch_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FINALLY_KEYWORD);
    r = r && body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACE [tuple_items] RBRACE
  public static boolean tuple(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && tuple_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, TUPLE, r);
    return r;
  }

  // [tuple_items]
  private static boolean tuple_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_1")) return false;
    tuple_items(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expression ( COMMA expression )*
  public static boolean tuple_items(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_items")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TUPLE_ITEMS, "<tuple items>");
    r = expression(b, l + 1);
    r = r && tuple_items_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( COMMA expression )*
  private static boolean tuple_items_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_items_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!tuple_items_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tuple_items_1", c)) break;
    }
    return true;
  }

  // COMMA expression
  private static boolean tuple_items_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_items_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACE [pattern_list] RBRACE
  public static boolean tuple_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_pattern")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && tuple_pattern_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, TUPLE_PATTERN, r);
    return r;
  }

  // [pattern_list]
  private static boolean tuple_pattern_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_pattern_1")) return false;
    pattern_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // variable DOUBLE_COLON_OP expression
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
  // unary_operator* atom_expression
  public static boolean unary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_EXPRESSION, "<unary expression>");
    r = unary_expression_0(b, l + 1);
    r = r && atom_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // unary_operator*
  private static boolean unary_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!unary_operator(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unary_expression_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PLUS | MINUS | NOT_KEYWORD | BACKSLASH | HASH_TILDE_OP
  public static boolean unary_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_OPERATOR, "<unary operator>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, NOT_KEYWORD);
    if (!r) r = consumeToken(b, BACKSLASH);
    if (!r) r = consumeToken(b, HASH_TILDE_OP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression ( EQUAL | NOT_EQUAL | HASH_NOT_EQUAL_OP ) expression
  public static boolean unification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unification")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNIFICATION, "<unification>");
    r = expression(b, l + 1);
    r = r && unification_1(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EQUAL | NOT_EQUAL | HASH_NOT_EQUAL_OP
  private static boolean unification_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unification_1")) return false;
    boolean r;
    r = consumeToken(b, EQUAL);
    if (!r) r = consumeToken(b, NOT_EQUAL);
    if (!r) r = consumeToken(b, HASH_NOT_EQUAL_OP);
    return r;
  }

  /* ********************************************************** */
  // USING_KEYWORD module_name [ "=>" rename_list ] DOT
  public static boolean using_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using_statement")) return false;
    if (!nextTokenIs(b, USING_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, USING_STATEMENT, null);
    r = consumeToken(b, USING_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, module_name(b, l + 1));
    r = p && report_error_(b, using_statement_2(b, l + 1)) && r;
    r = p && consumeToken(b, DOT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ "=>" rename_list ]
  private static boolean using_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using_statement_2")) return false;
    using_statement_2_0(b, l + 1);
    return true;
  }

  // "=>" rename_list
  private static boolean using_statement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using_statement_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARROW_OP);
    r = r && rename_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable ( COMMA variable )*
  public static boolean variable_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    r = r && variable_list_1(b, l + 1);
    exit_section_(b, m, VARIABLE_LIST, r);
    return r;
  }

  // ( COMMA variable )*
  private static boolean variable_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variable_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variable_list_1", c)) break;
    }
    return true;
  }

  // COMMA variable
  private static boolean variable_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, VARIABLE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WHILE_KEYWORD LPAR expression RPAR body END_KEYWORD
  public static boolean while_loop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_loop")) return false;
    if (!nextTokenIs(b, WHILE_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_LOOP, null);
    r = consumeTokens(b, 1, WHILE_KEYWORD, LPAR);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RPAR)) && r;
    r = p && report_error_(b, body(b, l + 1)) && r;
    r = p && consumeToken(b, END_KEYWORD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

}
