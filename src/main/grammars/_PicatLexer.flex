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
FIELD_ACCESS=\.[a-z][a-zA-Z0-9_]*
INTEGER=[0-9]+
HEX_INTEGER=0[xX][0-9a-fA-F]+
OCTAL_INTEGER=0[oO][0-7]+
BINARY_INTEGER=0[bB][01]+
FLOAT=[0-9]+\.[0-9]+([eE][+-]?[0-9]+)?
STRING=\"([^\"\\]|\\\\.)*\"
QUOTED_ATOM='([^'\\]|\\\\.)*'
COMMENT=%[^\r\n]*
MULTI_LINE_COMMENT="/"\*([^*]|\*+[^*/])*\*+"/"

%%
<YYINITIAL> {
  {WHITE_SPACE}              { return WHITE_SPACE; }

  "module"                   { return MODULE_KEYWORD; }
  "end_module"               { return END_MODULE_KEYWORD; }
  "import"                   { return IMPORT_KEYWORD; }
  "export"                   { return EXPORT_KEYWORD; }
  "include"                  { return INCLUDE_KEYWORD; }
  "if"                       { return IF_KEYWORD; }
  "then"                     { return THEN_KEYWORD; }
  "elseif"                   { return ELSEIF_KEYWORD; }
  "else"                     { return ELSE_KEYWORD; }
  "end"                      { return END_KEYWORD; }
  "foreach"                  { return FOREACH_KEYWORD; }
  "in"                       { return IN_KEYWORD; }
  "loop"                     { return LOOP_KEYWORD; }
  "private"                  { return PRIVATE_KEYWORD; }
  "table"                    { return TABLE_KEYWORD; }
  "index"                    { return INDEX_KEYWORD; }
  "return"                   { return RETURN_KEYWORD; }
  "continue"                 { return CONTINUE_KEYWORD; }
  "break"                    { return BREAK_KEYWORD; }
  "not"                      { return NOT_KEYWORD; }
  "fail"                     { return FAIL_KEYWORD; }
  "true"                     { return TRUE_KEYWORD; }
  "false"                    { return FALSE_KEYWORD; }
  "case"                     { return CASE_KEYWORD; }
  "of"                       { return OF_KEYWORD; }
  "try"                      { return TRY_KEYWORD; }
  "catch"                    { return CATCH_KEYWORD; }
  "finally"                  { return FINALLY_KEYWORD; }
  "throw"                    { return THROW_KEYWORD; }
  "using"                    { return USING_KEYWORD; }
  "while"                    { return WHILE_KEYWORD; }
  "do"                       { return DO_KEYWORD; }
  "pass"                     { return PASS_KEYWORD; }
  "=>"                       { return ARROW_OP; }
  "?=>"                      { return BACKTRACKABLE_ARROW_OP; }
  "<=>"                      { return BICONDITIONAL_OP; }
  "?<=>"                     { return BACKTRACKABLE_BICONDITIONAL_OP; }
  "#<=>"                     { return HASH_BICONDITIONAL_OP; }
  "#=>"                      { return HASH_ARROW_OP; }
  "#\\\\/"                   { return HASH_OR_OP; }
  "#^"                       { return HASH_CARET_OP; }
  "#/\\\\"                   { return HASH_AND_OP; }
  "#~"                       { return HASH_TILDE_OP; }
  "#="                       { return HASH_EQUAL_OP; }
  "#=<"                      { return HASH_LESS_EQUAL_OP; }
  ":-"                       { return RULE_OP; }
  ":="                       { return ASSIGN_OP; }
  ".."                       { return RANGE_OP; }
  "="                        { return EQUAL; }
  "!="                       { return NOT_EQUAL; }
  "#!="                      { return HASH_NOT_EQUAL_OP; }
  "<"                        { return LESS; }
  ">"                        { return GREATER; }
  "<="                       { return LESS_EQUAL; }
  ">="                       { return GREATER_EQUAL; }
  "=:="                      { return IDENTICAL; }
  "=\\\\="                   { return NOT_IDENTICAL; }
  "+"                        { return PLUS; }
  "-"                        { return MINUS; }
  "*"                        { return MULTIPLY; }
  "**"                       { return POWER; }
  "/"                        { return DIVIDE; }
  "//"                       { return INT_DIVIDE; }
  "div"                      { return DIV_KEYWORD; }
  "rem"                      { return REM_KEYWORD; }
  "mod"                      { return MOD_KEYWORD; }
  "<<"                       { return SHIFT_LEFT; }
  ">>"                       { return SHIFT_RIGHT; }
  ">>>"                      { return SHIFT_RIGHT_TRIPLE_OP; }
  "\\\\"                     { return BACKSLASH; }
  "and"                      { return AND_KEYWORD; }
  "or"                       { return OR_KEYWORD; }
  "xor"                      { return XOR_KEYWORD; }
  "^"                        { return CARET; }
  "&"                        { return AMPERSAND; }
  "|"                        { return PIPE; }
  "is"                       { return IS_KEYWORD; }
  "++"                       { return CONCAT_OP; }
  "::"                       { return DOUBLE_COLON_OP; }
  "."                        { return DOT; }
  ","                        { return COMMA; }
  ";"                        { return SEMICOLON; }
  ":"                        { return COLON; }
  "("                        { return LPAR; }
  ")"                        { return RPAR; }
  "["                        { return LBRACKET; }
  "]"                        { return RBRACKET; }
  "{"                        { return LBRACE; }
  "}"                        { return RBRACE; }
  "!"                        { return CUT; }
  "@"                        { return AT; }
  "?"                        { return QUESTION_MARK; }
  "$"                        { return DOLLAR; }

  {IDENTIFIER}               { return IDENTIFIER; }
  {VARIABLE}                 { return VARIABLE; }
  {FIELD_ACCESS}             { return FIELD_ACCESS; }
  {INTEGER}                  { return INTEGER; }
  {HEX_INTEGER}              { return HEX_INTEGER; }
  {OCTAL_INTEGER}            { return OCTAL_INTEGER; }
  {BINARY_INTEGER}           { return BINARY_INTEGER; }
  {FLOAT}                    { return FLOAT; }
  {STRING}                   { return STRING; }
  {QUOTED_ATOM}              { return QUOTED_ATOM; }
  {COMMENT}                  { return COMMENT; }
  {MULTI_LINE_COMMENT}       { return MULTI_LINE_COMMENT; }

}

[^] { return BAD_CHARACTER; }
