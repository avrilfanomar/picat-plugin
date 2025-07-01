package com.github.avrilfanomar.picatplugin.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import static com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.*;

%% // Separator 1: After user code

// JFlex options
%public
%class _PicatLexer
%implements FlexLexer
%function advance
%type IElementType
%eofval{
  zzAtEOF = true; // Mark EOF
  return null;
%eofval}

// Macro definitions (restored from subtask 12 attempt)
// CRLF macro removed as it's unused and its components are directly in WHITE_SPACE
WHITE_SPACE=[ \t\f\r\n]+
COMMENT_LINE=\%.*
IDENTIFIER_REGEX=[a-z][a-zA-Z0-9_]*
VARIABLE_REGEX=[A-Z_][a-zA-Z0-9_]*
NUMBER_REGEX=[0-9]+
HEX_NUMBER_REGEX=0[xX][0-9a-fA-F]+
OCTAL_NUMBER_REGEX=0[oO][0-7]+
BINARY_NUMBER_REGEX=0[bB][01]+
FLOAT_NUMBER_REGEX=([0-9]+)\.([0-9]+)([eE][+-]?[0-9]+)?

STRING_REGEX=\"([^\"\\]|\\.)*\"
QUOTED_ATOM_REGEX=\'([^\'\\]|\\.)*\'
MULTI_LINE_COMMENT_REGEX=\/\*([^*]|\*+[^*/])*\*+\/

%% // Separator 2: After options and macros

<YYINITIAL> {
  {WHITE_SPACE}               { return TokenType.WHITE_SPACE; }
  {COMMENT_LINE}              { return COMMENT; }
  {MULTI_LINE_COMMENT_REGEX}  { return MULTI_LINE_COMMENT; }

  // Keywords from Picat.bnf tokens section
  "module"                    { return MODULE_KEYWORD; }
  "end_module"                { return END_MODULE_KEYWORD; }
  "import"                    { return IMPORT_KEYWORD; }
  "export"                    { return EXPORT_KEYWORD; }
  "include"                   { return INCLUDE_KEYWORD; }
  "if"                        { return IF_KEYWORD; }
  "then"                      { return THEN_KEYWORD; }
  "elseif"                    { return ELSEIF_KEYWORD; }
  "else"                      { return ELSE_KEYWORD; }
  "end"                       { return END_KEYWORD; }
  "foreach"                   { return FOREACH_KEYWORD; }
  "in"                        { return IN_KEYWORD; }
  "loop"                      { return LOOP_KEYWORD; }
  "private"                   { return PRIVATE_KEYWORD; }
  "table"                     { return TABLE_KEYWORD; }
  "index"                     { return INDEX_KEYWORD; }
  "return"                    { return RETURN_KEYWORD; }
  "continue"                  { return CONTINUE_KEYWORD; }
  "break"                     { return BREAK_KEYWORD; }
  "not"                       { return NOT_KEYWORD; }
  "fail"                      { return FAIL_KEYWORD; }
  "true"                      { return TRUE_KEYWORD; }
  "false"                     { return FALSE_KEYWORD; }
  "case"                      { return CASE_KEYWORD; }
  "of"                        { return OF_KEYWORD; }
  "try"                       { return TRY_KEYWORD; }
  "catch"                     { return CATCH_KEYWORD; }
  "finally"                   { return FINALLY_KEYWORD; }
  "throw"                     { return THROW_KEYWORD; }
  "using"                     { return USING_KEYWORD; }
  "while"                     { return WHILE_KEYWORD; }
  "do"                        { return DO_KEYWORD; }
  "pass"                      { return PASS_KEYWORD; }
  "div"                       { return DIV_KEYWORD; }
  "rem"                       { return REM_KEYWORD; }
  "mod"                       { return MOD_KEYWORD; }
  "and"                       { return AND_KEYWORD; }
  "or"                        { return OR_KEYWORD; }
  "xor"                       { return XOR_KEYWORD; }
  "is"                        { return IS_KEYWORD; }

  // Operators from Picat.bnf tokens section
  "=>"                        { return ARROW_OP; }
  "?=>"                       { return BACKTRACKABLE_ARROW_OP; }
  "<=>"                       { return BICONDITIONAL_OP; }
  "?<=>"                      { return BACKTRACKABLE_BICONDITIONAL_OP; }
  "#<=>"                      { return HASH_BICONDITIONAL_OP; }
  "#=>"                       { return HASH_ARROW_OP; }
  "#\\/"                      { return HASH_OR_OP; }
  "#^"                        { return HASH_CARET_OP; }
  "#/\\"                      { return HASH_AND_OP; }
  "#~"                        { return HASH_TILDE_OP; }
  "#=<"                       { return HASH_LESS_EQUAL_OP; }
  ":-"                        { return RULE_OP; }
  "::"                        { return DOUBLE_COLON_OP; }
  ":="                        { return ASSIGN_OP; }
  ".."                        { return RANGE_OP; }
  "="                         { return EQUAL; }
  "!="                        { return NOT_EQUAL; }
  "<"                         { return LESS; }
  ">"                         { return GREATER; }
  "<="                        { return LESS_EQUAL; }
  ">="                        { return GREATER_EQUAL; }
  "=:="                       { return IDENTICAL; }
  "=\\="                      { return NOT_IDENTICAL; }
  "+"                         { return PLUS; }
  "-"                         { return MINUS; }
  "*"                         { return MULTIPLY; }
  "**"                        { return POWER; }
  "/"                         { return DIVIDE; }
  "//"                        { return INT_DIVIDE; }
  "<<"                        { return SHIFT_LEFT; }
  ">>"                        { return SHIFT_RIGHT; }
  ">>>"                       { return SHIFT_RIGHT_TRIPLE_OP; }
  "\\"                        { return BACKSLASH; }
  "^"                         { return CARET; }
  "&"                         { return AMPERSAND; }
  "|"                         { return PIPE; }
  "."                         { return DOT; }
  "++"                        { return CONCAT_OP; }

  // Separators
  ","                         { return COMMA; }
  ";"                         { return SEMICOLON; }
  ":"                         { return COLON; }
  "("                         { return LPAR; }
  ")"                         { return RPAR; }
  "["                         { return LBRACKET; } // Corrected token name
  "]"                         { return RBRACKET; } // Corrected token name
  "{"                         { return LBRACE; }
  "}"                         { return RBRACE; }
  "!"                         { return CUT; }
  "@"                         { return AT; }

  // Literals (using regex macros)
  {NUMBER_REGEX}              { return INTEGER; }
  {HEX_NUMBER_REGEX}          { return HEX_INTEGER; }
  {OCTAL_NUMBER_REGEX}        { return OCTAL_INTEGER; }
  {BINARY_NUMBER_REGEX}       { return BINARY_INTEGER; }
  {FLOAT_NUMBER_REGEX}        { return FLOAT; }
  {STRING_REGEX}              { return STRING; }
  {QUOTED_ATOM_REGEX}         { return QUOTED_ATOM; }

  // IMPORTANT: Order for IDENTIFIER, VARIABLE matters to avoid shadowing
  {VARIABLE_REGEX}            { return VARIABLE; }
  {IDENTIFIER_REGEX}          { return IDENTIFIER; }
}

[^] { return TokenType.BAD_CHARACTER; }
