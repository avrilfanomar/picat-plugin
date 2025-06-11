package com.github.avrilfanomar.picatplugin.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes;

%%

%public
%class _PicatLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return; %eof}

%state NORMAL

// Define regular expressions for common character classes
LETTER = [a-zA-Z]
DIGIT = [0-9]
HEX_DIGIT = [0-9a-fA-F]
OCTAL_DIGIT = [0-7]
BINARY_DIGIT = [01]

IDENTIFIER_START = ({LETTER}|"_")
IDENTIFIER_PART = ({LETTER}|{DIGIT}|"_")
VARIABLE_START = [A-Z_]

WHITESPACE = [\
]+

// Comment rules
LINE_COMMENT = "%"[^
]*
MULTI_LINE_COMMENT = "/*" [^*] ~"*/" | "/*" "*"+ "/"

// String and Atom rules
STRING_LITERAL = \" ( [^\"\\] | \\. )* \"
QUOTED_ATOM_LITERAL = \' ( [^\'\\] | \\. )* \'

// Number rules
INTEGER_LITERAL = {DIGIT}+
HEX_LITERAL = "0" [xX] {HEX_DIGIT}+
OCTAL_LITERAL = "0" [oO] {OCTAL_DIGIT}+
BINARY_LITERAL = "0" [bB] {BINARY_DIGIT}+
FLOAT_LITERAL = {DIGIT}+ "." {DIGIT}* ( [eE] [+\-]? {DIGIT}+ )? |
                {DIGIT}* "." {DIGIT}+ ( [eE] [+\-]? {DIGIT}+ )? |
                {DIGIT}+ [eE] [+\-]? {DIGIT}+

%%
<NORMAL> {
    // Whitespace
    {WHITESPACE} { return PicatTokenTypes.WHITE_SPACE; }

    // Comments
    {LINE_COMMENT} { return PicatTokenTypes.COMMENT; }
    {MULTI_LINE_COMMENT} { return PicatTokenTypes.MULTI_LINE_COMMENT; }

    // Strings and Atoms
    {STRING_LITERAL} { return PicatTokenTypes.STRING; }
    {QUOTED_ATOM_LITERAL} { return PicatTokenTypes.QUOTED_ATOM; }

    // Numbers (order matters for "0" prefix)
    {HEX_LITERAL} { return PicatTokenTypes.HEX_INTEGER; }
    {OCTAL_LITERAL} { return PicatTokenTypes.OCTAL_INTEGER; }
    {BINARY_LITERAL} { return PicatTokenTypes.BINARY_INTEGER; }
    {FLOAT_LITERAL} { return PicatTokenTypes.FLOAT; }
    {INTEGER_LITERAL} { return PicatTokenTypes.INTEGER; }

    // Keywords
    "module" { return PicatTokenTypes.MODULE_KEYWORD; }
    "end_module" { return PicatTokenTypes.END_MODULE_KEYWORD; }
    "import" { return PicatTokenTypes.IMPORT_KEYWORD; }
    "export" { return PicatTokenTypes.EXPORT_KEYWORD; }
    "include" { return PicatTokenTypes.INCLUDE_KEYWORD; }
    "if" { return PicatTokenTypes.IF_KEYWORD; }
    "then" { return PicatTokenTypes.THEN_KEYWORD; }
    "elseif" { return PicatTokenTypes.ELSEIF_KEYWORD; }
    "else" { return PicatTokenTypes.ELSE_KEYWORD; }
    "end" { return PicatTokenTypes.END_KEYWORD; }
    "foreach" { return PicatTokenTypes.FOREACH_KEYWORD; }
    "in" { return PicatTokenTypes.IN_KEYWORD; }
    "return" { return PicatTokenTypes.RETURN_KEYWORD; }
    "continue" { return PicatTokenTypes.CONTINUE_KEYWORD; }
    "break" { return PicatTokenTypes.BREAK_KEYWORD; }
    "not" { return PicatTokenTypes.NOT_KEYWORD; }
    "fail" { return PicatTokenTypes.FAIL_KEYWORD; }
    "true" { return PicatTokenTypes.TRUE_KEYWORD; }
    "false" { return PicatTokenTypes.FALSE_KEYWORD; }
    "case" { return PicatTokenTypes.CASE_KEYWORD; }
    "of" { return PicatTokenTypes.OF_KEYWORD; }
    "try" { return PicatTokenTypes.TRY_KEYWORD; }
    "catch" { return PicatTokenTypes.CATCH_KEYWORD; }
    "finally" { return PicatTokenTypes.FINALLY_KEYWORD; }
    "throw" { return PicatTokenTypes.THROW_KEYWORD; }
    "using" { return PicatTokenTypes.USING_KEYWORD; }
    "while" { return PicatTokenTypes.WHILE_KEYWORD; }
    "do" { return PicatTokenTypes.DO_KEYWORD; }
    "pass" { return PicatTokenTypes.PASS_KEYWORD; }
    "index" { return PicatTokenTypes.INDEX_KEYWORD; }
    "private" { return PicatTokenTypes.PRIVATE_KEYWORD; }
    "public" { return PicatTokenTypes.PUBLIC_KEYWORD; }
    "table" { return PicatTokenTypes.TABLE_KEYWORD; }
    "once" { return PicatTokenTypes.ONCE_KEYWORD; }
    "div" { return PicatTokenTypes.DIV_KEYWORD; }
    "rem" { return PicatTokenTypes.REM_KEYWORD; }
    "notin" { return PicatTokenTypes.NOTIN_KEYWORD; }
    "writef" { return PicatTokenTypes.WRITEF_KEYWORD; }
    "repeat" { return PicatTokenTypes.REPEAT_KEYWORD; }
    "mod" { return PicatTokenTypes.MOD_KEYWORD; }
    "and" { return PicatTokenTypes.AND_KEYWORD; }
    "or" { return PicatTokenTypes.OR_KEYWORD; }
    "xor" { return PicatTokenTypes.XOR_KEYWORD; }
    "is" { return PicatTokenTypes.IS_KEYWORD; }

    // Operators (longest first)
    "?<=>" { return PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP; }
    "#<=>" { return PicatTokenTypes.CONSTRAINT_EQUIV; }
    "?=>"  { return PicatTokenTypes.BACKTRACKABLE_ARROW_OP; }
    "!=="  { return PicatTokenTypes.NOT_IDENTICAL; } // Replaces =\=
    "==="  { return PicatTokenTypes.IDENTICAL; } // Replaces =:=
    "<=>"  { return PicatTokenTypes.BICONDITIONAL_OP; }
    "#=>"  { return PicatTokenTypes.CONSTRAINT_IMPL; }
    "#/\"  { return PicatTokenTypes.CONSTRAINT_AND; }
    "#\/"  { return PicatTokenTypes.CONSTRAINT_OR; }
    "#<="  { return PicatTokenTypes.CONSTRAINT_LE_ALT; }
    "#!="  { return PicatTokenTypes.CONSTRAINT_NEQ; }
    "#>="  { return PicatTokenTypes.CONSTRAINT_GE; }
    "#=<"  { return PicatTokenTypes.CONSTRAINT_LE; }
    "@=<"  { return PicatTokenTypes.TERM_LE; }
    "@<="  { return PicatTokenTypes.TERM_LE_ALT; }
    "@>="  { return PicatTokenTypes.TERM_GE; }
    "=>"   { return PicatTokenTypes.ARROW_OP; }
    ":="   { return PicatTokenTypes.ASSIGN_OP; }
    "=="   { return PicatTokenTypes.EQUAL_EQUAL; }
    "!="   { return PicatTokenTypes.NOT_EQUAL; }
    "<="   { return PicatTokenTypes.LESS_EQUAL; }
    "=<"   { return PicatTokenTypes.LESS_EQUAL_ALT; }
    ">="   { return PicatTokenTypes.GREATER_EQUAL; }
    "&&"   { return PicatTokenTypes.AND; }
    "||"   { return PicatTokenTypes.OR; }
    "**"   { return PicatTokenTypes.POWER_OP; }
    "//"   { return PicatTokenTypes.INT_DIVIDE; }
    "/<"   { return PicatTokenTypes.DIVIDE_LT; }
    "/>"   { return PicatTokenTypes.DIVIDE_GT; }
    "::"   { return PicatTokenTypes.TYPE_CONSTRAINT; }
    ".."   { return PicatTokenTypes.RANGE_OP; }
    "++"   { return PicatTokenTypes.CONCAT_OP; }
    "<<"   { return PicatTokenTypes.SHIFT_LEFT; }
    ">>"   { return PicatTokenTypes.SHIFT_RIGHT; }
    "/\"   { return PicatTokenTypes.BITWISE_AND; }
    "\/"  { return PicatTokenTypes.BITWISE_OR; }
    "#="   { return PicatTokenTypes.CONSTRAINT_EQ; }
    "#<"   { return PicatTokenTypes.CONSTRAINT_LT; }
    "#>"   { return PicatTokenTypes.CONSTRAINT_GT; }
    "#~"   { return PicatTokenTypes.CONSTRAINT_NOT; }
    "#^"   { return PicatTokenTypes.CONSTRAINT_XOR; }
    "@<"   { return PicatTokenTypes.TERM_LT; }
    "@>"   { return PicatTokenTypes.TERM_GT; }
    ":-"   { return PicatTokenTypes.RULE_OP; }
    "="    { return PicatTokenTypes.EQUAL; }
    "+"    { return PicatTokenTypes.PLUS; }
    "-"    { return PicatTokenTypes.MINUS; }
    "*"    { return PicatTokenTypes.MULTIPLY; }
    "/"    { return PicatTokenTypes.DIVIDE; }
    "<"    { return PicatTokenTypes.LESS; }
    ">"    { return PicatTokenTypes.GREATER; }
    "!"    { return PicatTokenTypes.CUT; } // Replaces NOT
    "|"    { return PicatTokenTypes.PIPE; }
    "@"    { return PicatTokenTypes.AT; } // Replaces AS_PATTERN
    "$"    { return PicatTokenTypes.DATA_CONSTRUCTOR; }
    "^"    { return PicatTokenTypes.CARET; } // Replaces POWER
    "#"    { return PicatTokenTypes.HASH; }
    "~"    { return PicatTokenTypes.TILDE; }
    "\\" { return PicatTokenTypes.BACKSLASH; } // Escaped backslash
    ","    { return PicatTokenTypes.COMMA; }
    "."    { return PicatTokenTypes.DOT; }
    ";"    { return PicatTokenTypes.SEMICOLON; }
    ":"    { return PicatTokenTypes.COLON; }
    "("    { return PicatTokenTypes.LPAR; }
    ")"    { return PicatTokenTypes.RPAR; }
    "["    { return PicatTokenTypes.LBRACKET; }
    "]"    { return PicatTokenTypes.RBRACKET; }
    "{"    { return PicatTokenTypes.LBRACE; }
    "}"    { return PicatTokenTypes.RBRACE; }
    "?"    { return PicatTokenTypes.QUESTION; }


    // Identifiers and Variables
    {VARIABLE_START}{IDENTIFIER_PART}* { return PicatTokenTypes.VARIABLE; }
    {IDENTIFIER_START}{IDENTIFIER_PART}* { return PicatTokenTypes.IDENTIFIER; }

    // Bad Character Rule (matches any other character)
    [^] { return PicatTokenTypes.BAD_CHARACTER; }
}
