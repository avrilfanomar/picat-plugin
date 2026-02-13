package com.github.avrilfanomar.picatplugin.language.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.*;

%%

%{
  public _PicatLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _PicatLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

IDENTIFIER=[a-z][a-zA-Z0-9_]*
DOT_IDENTIFIER=\.[a-z][a-zA-Z0-9_]*
VARIABLE=[A-Z_][a-zA-Z0-9_]*
INTEGER=([0-9]+|0[xX][0-9a-fA-F]+|0[oO][0-7]+|0[bB][01]+|0'(\\.|[^\n]))
FLOAT=[0-9]+\.[0-9]+([eE][+-]?[0-9]+)?
STRING=\"(\\.|[^\"\\])*\"
SINGLE_QUOTED_ATOM='([^'\\]|\\.)*'
DOT_SINGLE_QUOTED_ATOM=\\.'([^'\\]|\\.)*'
COMMENT=%.*
QUALIFIED_ATOM=[a-z][a-zA-Z0-9_]*\.[a-z][a-zA-Z0-9_]*
MULTILINE_COMMENT="/"[*]([^*]|[*][^/])*[*]"/"

%%
<YYINITIAL> {
  {WHITE_SPACE}                  { return WHITE_SPACE; }

  "module"                       { return MODULE_KEYWORD; }
  "import"                       { return IMPORT_KEYWORD; }
  "include"                      { return INCLUDE_KEYWORD; }
  "private"                      { return PRIVATE_KEYWORD; }
  "table"                        { return TABLE_KEYWORD; }
  "index"                        { return INDEX_KEYWORD; }
  "if"                           { return IF_KEYWORD; }
  "then"                         { return THEN_KEYWORD; }
  "elseif"                       { return ELSEIF_KEYWORD; }
  "else"                         { return ELSE_KEYWORD; }
  "end"                          { return END_KEYWORD; }
  "foreach"                      { return FOREACH_KEYWORD; }
  "in"                           { return IN_KEYWORD; }
  "notin"                        { return NOT_IN_KEYWORD; }
  "while"                        { return WHILE_KEYWORD; }
  "loop"                         { return LOOP_KEYWORD; }
  "try"                          { return TRY_KEYWORD; }
  "catch"                        { return CATCH_KEYWORD; }
  "finally"                      { return FINALLY_KEYWORD; }
  "throw"                        { return THROW_KEYWORD; }
  "not"                          { return NOT_KEYWORD; }
  "div"                          { return DIV_KEYWORD; }
  "mod"                          { return MOD_KEYWORD; }
  "rem"                          { return REM_KEYWORD; }
  "lambda"                       { return LAMBDA_KEYWORD; }
  "cardinality"                  { return CARDINALITY_KEYWORD; }
  "fail"                         { return FAIL_KEYWORD; }
  "repeat"                       { return REPEAT_KEYWORD; }
  "until"                        { return UNTIL_KEYWORD; }
  "once"                         { return ONCE_KEYWORD; }
  "is"                           { return IS_KEYWORD; }
  "-->"                          { return DCG_ARROW_OP; }
  ">>>"                          { return SHIFT_RIGHT_TRIPLE; }
  "?=>"                          { return BACKTRACKABLE_ARROW_OP; }
  "<=>"                          { return BICONDITIONAL_OP; }
  "#<=>"                         { return HASH_BICONDITIONAL_OP; }
  "#!="                          { return HASH_NOT_EQUAL_OP; }
  "#=>"                          { return HASH_ARROW_OP; }
  "#>="                          { return HASH_GREATER_EQUAL_OP; }
  "#=<"                          { return HASH_LESS_EQUAL_OP; }
  "#<="                          { return HASH_LESS_EQUAL_ALT_OP; }
  "@>="                          { return AT_GREATER_EQUAL_OP; }
  "@<="                          { return AT_LESS_EQUAL_OP; }
  "@=<"                          { return AT_LESS_EQUAL_PROLOG_OP; }
  "=:="                          { return NUMERICALLY_EQUAL; }
  "=\\="                         { return NUMERICALLY_NON_EQUAL; }
  "!=="                          { return NOT_IDENTICAL; }
  "#/\\"                         { return HASH_AND_OP; }
  "#\\/"                         { return HASH_OR_OP; }
  "#^"                           { return HASH_XOR_OP; }
  "#\\"                          { return HASH_NOT_BACKSLASH_OP; }
  "->"                           { return IF_THEN_OP; }
  "=>"                           { return ARROW_OP; }
  "#~"                           { return HASH_NOT_OP; }
  "#="                           { return HASH_EQUAL_OP; }
  "#>"                           { return HASH_GREATER_OP; }
  "#<"                           { return HASH_LESS_OP; }
  "@>"                           { return AT_GREATER_OP; }
  "@<"                           { return AT_LESS_OP; }
  ":="                           { return ASSIGN_OP; }
  "::"                           { return DOUBLE_COLON_OP; }
  ".."                           { return RANGE_OP; }
  "!="                           { return NOT_EQUAL; }
  "=="                           { return IDENTICAL; }
  ">"                            { return GREATER; }
  "<="                           { return LESS_EQUAL; }
  ">="                           { return GREATER_EQUAL; }
  "=<"                           { return LESS_EQUAL_PROLOG; }
  "/>"                           { return DIV_RIGHT; }
  "/<"                           { return DIV_LEFT; }
  "**"                           { return POWER; }
  ":-"                           { return PROLOG_RULE_OP; }
  "++"                           { return CONCAT_OP; }
  "<<"                           { return SHIFT_LEFT; }
  ">>"                           { return SHIFT_RIGHT; }
  "//"                           { return INT_DIVIDE; }
  "/\\"                          { return BITWISE_AND; }
  "\\/"                          { return BITWISE_OR; }
  "/"                            { return DIVIDE; }
  "="                            { return EQUAL; }
  "<"                            { return LESS; }
  "+"                            { return PLUS; }
  "-"                            { return MINUS; }
  "min"                          { return MIN; }
  "max"                          { return MAX; }
  "nt"                           { return NT; }
  "*"                            { return MULTIPLY; }
  "^"                            { return BITWISE_XOR; }
  "~"                            { return COMPLEMENT; }
  "!"                            { return EXCLAMATION; }
  "&&"                           { return AND_AND; }
  "||"                           { return OR_OR; }
  "=.."                          { return UNIV_OP; }
  "\\=="                         { return NOT_IDENTICAL_PROLOG_OP; }
  "\\="                          { return NOT_UNIFIABLE_OP; }
  "\\\\+"                        { return BACKSLASH_PLUS; }
  ","                            { return COMMA; }
  ";"                            { return SEMICOLON; }
  ":"                            { return COLON; }
  "("                            { return LPAR; }
  ")"                            { return RPAR; }
  "["                            { return LBRACKET; }
  "]"                            { return RBRACKET; }
  "{"                            { return LBRACE; }
  "}"                            { return RBRACE; }
  "|"                            { return PIPE; }
  "@"                            { return AT; }
  "$"                            { return DOLLAR; }
  "."                            { return DOT; }
  "true"                         { return TRUE; }
  "false"                        { return FALSE; }

  {IDENTIFIER}                   { return IDENTIFIER; }
  {DOT_IDENTIFIER}               { return DOT_IDENTIFIER; }
  {VARIABLE}                     { return VARIABLE; }
  {INTEGER}                      { return INTEGER; }
  {FLOAT}                        { return FLOAT; }
  {STRING}                       { return STRING; }
  {SINGLE_QUOTED_ATOM}           { return SINGLE_QUOTED_ATOM; }
  {DOT_SINGLE_QUOTED_ATOM}       { return DOT_SINGLE_QUOTED_ATOM; }
  {COMMENT}                      { return COMMENT; }
  {QUALIFIED_ATOM}               { return QUALIFIED_ATOM; }
  {MULTILINE_COMMENT}            { return MULTILINE_COMMENT; }

}

[^] { return BAD_CHARACTER; }
