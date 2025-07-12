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
VARIABLE=[A-Z_][a-zA-Z0-9_]*
INTEGER=[0-9]+
FLOAT=[0-9]+\.[0-9]+([eE][+-]?[0-9]+)?
STRING=\"([^\"\\]|\\\\.)*\"
COMMENT=%[^\r\n]*
QUALIFIED_ATOM=[a-z][a-zA-Z0-9_]*\\.[a-z][a-zA-Z0-9_]*

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  "module"               { return MODULE_KEYWORD; }
  "import"               { return IMPORT_KEYWORD; }
  "include"              { return INCLUDE_KEYWORD; }
  "private"              { return PRIVATE_KEYWORD; }
  "table"                { return TABLE_KEYWORD; }
  "index"                { return INDEX_KEYWORD; }
  "if"                   { return IF_KEYWORD; }
  "then"                 { return THEN_KEYWORD; }
  "elseif"               { return ELSEIF_KEYWORD; }
  "else"                 { return ELSE_KEYWORD; }
  "end"                  { return END_KEYWORD; }
  "foreach"              { return FOREACH_KEYWORD; }
  "in"                   { return IN_KEYWORD; }
  "while"                { return WHILE_KEYWORD; }
  "loop"                 { return LOOP_KEYWORD; }
  "try"                  { return TRY_KEYWORD; }
  "catch"                { return CATCH_KEYWORD; }
  "finally"              { return FINALLY_KEYWORD; }
  "not"                  { return NOT_KEYWORD; }
  "div"                  { return DIV_KEYWORD; }
  "mod"                  { return MOD_KEYWORD; }
  "rem"                  { return REM_KEYWORD; }
  "lambda"               { return LAMBDA_KEYWORD; }
  "cardinality"          { return CARDINALITY_KEYWORD; }
  "true"                 { return TRUE; }
  "false"                { return FALSE; }
  "fail"                 { return FAIL_KEYWORD; }
  "repeat"               { return REPEAT_KEYWORD; }
  "until"                { return UNTIL_KEYWORD; }
  "=>"                   { return ARROW_OP; }
  "?=>"                  { return BACKTRACKABLE_ARROW_OP; }
  "<=>"                  { return BICONDITIONAL_OP; }
  "#<=>"                 { return HASH_BICONDITIONAL_OP; }
  "#=>"                  { return HASH_ARROW_OP; }
  "#\\/"                 { return HASH_OR_OP; }
  "#^"                   { return HASH_XOR_OP; }
  "#/\\"                 { return HASH_AND_OP; }
  "#~"                   { return HASH_NOT_OP; }
  "#="                   { return HASH_EQUAL_OP; }
  "#!="                  { return HASH_NOT_EQUAL_OP; }
  "#>"                   { return HASH_GREATER_OP; }
  "#>="                  { return HASH_GREATER_EQUAL_OP; }
  "#<"                   { return HASH_LESS_OP; }
  "#=<"                  { return HASH_LESS_EQUAL_OP; }
  "#<="                  { return HASH_LESS_EQUAL_ALT_OP; }
  ":-"                   { return PROLOG_RULE_OP; }
  ":="                   { return ASSIGN_OP; }
  "::"                   { return DOUBLE_COLON_OP; }
  ".."                   { return RANGE_OP; }
  "="                    { return EQUAL; }
  "!="                   { return NOT_EQUAL; }
  "=="                   { return IDENTICAL; }
  "!=="                  { return NOT_IDENTICAL; }
  "<"                    { return LESS; }
  ">"                    { return GREATER; }
  "<="                   { return LESS_EQUAL; }
  ">="                   { return GREATER_EQUAL; }
  "=<"                   { return LESS_EQUAL_PROLOG; }
  "+"                    { return PLUS; }
  "-"                    { return MINUS; }
  "*"                    { return MULTIPLY; }
  "/"                    { return DIVIDE; }
  "//"                   { return INT_DIVIDE; }
  "/>"                   { return DIV_RIGHT; }
  "/<"                   { return DIV_LEFT; }
  "**"                   { return POWER; }
  "++"                   { return CONCAT_OP; }
  "<<"                   { return SHIFT_LEFT; }
  ">>"                   { return SHIFT_RIGHT; }
  ">>>"                  { return SHIFT_RIGHT_TRIPLE; }
  "\\/"                  { return BITWISE_OR; }
  "^"                    { return BITWISE_XOR; }
  "/\\"                  { return BITWISE_AND; }
  "~"                    { return COMPLEMENT; }
  "."                    { return DOT; }
  ","                    { return COMMA; }
  ";"                    { return SEMICOLON; }
  ":"                    { return COLON; }
  "("                    { return LPAR; }
  ")"                    { return RPAR; }
  "["                    { return LBRACKET; }
  "]"                    { return RBRACKET; }
  "{"                    { return LBRACE; }
  "}"                    { return RBRACE; }
  "|"                    { return PIPE; }
  "@"                    { return AT; }
  "$"                    { return DOLLAR; }

  {IDENTIFIER}           { return IDENTIFIER; }
  {VARIABLE}             { return VARIABLE; }
  {INTEGER}              { return INTEGER; }
  {FLOAT}                { return FLOAT; }
  {STRING}               { return STRING; }
  {COMMENT}              { return COMMENT; }
  {QUALIFIED_ATOM}       { return QUALIFIED_ATOM; }

}

[^] { return BAD_CHARACTER; }
