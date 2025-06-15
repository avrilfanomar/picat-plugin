package com.github.avrilfanomar.picatplugin.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes;
import com.intellij.psi.TokenType; // Added for WHITE_SPACE and BAD_CHARACTER

%public
%class _PicatLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return; %eof}

// Line and Character counters
%line
%char

// Define common regex patterns
// Simplified for direct use in rules or kept as is from BNF
// WHITE_SPACE_CHAR=[\ \n\r\t\f] // Using directly in rule
// IDENTIFIER_START=[a-z] // Using directly in rule
// IDENTIFIER_PART=[a-zA-Z0-9_] // Using directly in rule

%% // Lexical rules start here

<YYINITIAL> {
    // Whitespace
    ([\ \n\r\t\f])+ { return TokenType.WHITE_SPACE; }

    // Comments
    "%[^\r\n]*" { return PicatTokenTypes.COMMENT; }
    "/\*([^*]|\*+[^*/])*\*+/" { return PicatTokenTypes.MULTI_LINE_COMMENT; }

    // Keywords (ordered to avoid conflicts where possible, though JFlex prefers longest match)
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
    "loop" { return PicatTokenTypes.LOOP_KEYWORD; }
    "private" { return PicatTokenTypes.PRIVATE_KEYWORD; }
    "table" { return PicatTokenTypes.TABLE_KEYWORD; }
    "index" { return PicatTokenTypes.INDEX_KEYWORD; }
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
    "div" { return PicatTokenTypes.DIV_KEYWORD; } // Keyword operator
    "rem" { return PicatTokenTypes.REM_KEYWORD; } // Keyword operator
    "mod" { return PicatTokenTypes.MOD_KEYWORD; } // Keyword operator
    "and" { return PicatTokenTypes.AND_KEYWORD; } // Keyword operator
    "or" { return PicatTokenTypes.OR_KEYWORD; }   // Keyword operator
    "xor" { return PicatTokenTypes.XOR_KEYWORD; } // Keyword operator
    "is" { return PicatTokenTypes.IS_KEYWORD; }   // Keyword operator

    // Operators (ordered by length/specificity where conflicts might occur)
    "?<=>" { return PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP; }
    "?=>" { return PicatTokenTypes.BACKTRACKABLE_ARROW_OP; }
    "#<=>" { return PicatTokenTypes.HASH_BICONDITIONAL_OP; }
    "#=>" { return PicatTokenTypes.HASH_ARROW_OP; }
    "#\\/" { return PicatTokenTypes.HASH_OR_OP; }
    "#^" { return PicatTokenTypes.HASH_CARET_OP; }
    "#/\\" { return PicatTokenTypes.HASH_AND_OP; }
    "#~" { return PicatTokenTypes.HASH_TILDE_OP; }
    ":-" { return PicatTokenTypes.RULE_OP; }
    ":=" { return PicatTokenTypes.ASSIGN_OP; }
    ".." { return PicatTokenTypes.RANGE_OP; }
    "=>" { return PicatTokenTypes.ARROW_OP; }
    "<=>" { return PicatTokenTypes.BICONDITIONAL_OP; }
    "!=" { return PicatTokenTypes.NOT_EQUAL; }
    "<=" { return PicatTokenTypes.LESS_EQUAL; }
    ">=" { return PicatTokenTypes.GREATER_EQUAL; }
    "=:=" { return PicatTokenTypes.IDENTICAL; }
    "=\\=" { return PicatTokenTypes.NOT_IDENTICAL; } // Escaped backslash
    "**" { return PicatTokenTypes.POWER; }
    "//" { return PicatTokenTypes.INT_DIVIDE; }
    "<<" { return PicatTokenTypes.SHIFT_LEFT; }
    ">>" { return PicatTokenTypes.SHIFT_RIGHT; }
    ">>>" { return PicatTokenTypes.SHIFT_RIGHT_TRIPLE_OP; }
    "++" { return PicatTokenTypes.CONCAT_OP; }

    // EOR must be before DOT if DOT is also a standalone token.
    // The BNF has DOT_OP="." and DOT="." and EOR="regexp:\.\s+"
    // Assuming EOR is the specific one, then DOT_OP and DOT are general.
    // The regex for EOR is a dot followed by one or more whitespace characters.
    \.\s+ { return PicatTokenTypes.EOR; }


    // Single character operators / Separators
    "=" { return PicatTokenTypes.EQUAL; }
    "<" { return PicatTokenTypes.LESS; }
    ">" { return PicatTokenTypes.GREATER; }
    "+" { return PicatTokenTypes.PLUS; }
    "-" { return PicatTokenTypes.MINUS; }
    "*" { return PicatTokenTypes.MULTIPLY; }
    "/" { return PicatTokenTypes.DIVIDE; }
    "\\" { return PicatTokenTypes.BACKSLASH; } // Single backslash for the token itself
    "^" { return PicatTokenTypes.CARET; }
    "&" { return PicatTokenTypes.AMPERSAND; }
    "|" { return PicatTokenTypes.PIPE; } // Note: PIPE_CHOICE in BNF is also "|", lexer returns one type. Parser disambiguates.
    "." { return PicatTokenTypes.DOT; } // Or DOT_OP, assuming they are the same IElementType
    "," { return PicatTokenTypes.COMMA; }
    ";" { return PicatTokenTypes.SEMICOLON; }
    ":" { return PicatTokenTypes.COLON; }
    "(" { return PicatTokenTypes.LPAR; }
    ")" { return PicatTokenTypes.RPAR; }
    "[" { return PicatTokenTypes.LBRACKET; }
    "]" { return PicatTokenTypes.RBRACKET; }
    "{" { return PicatTokenTypes.LBRACE; }
    "}" { return PicatTokenTypes.RBRACE; }
    "!" { return PicatTokenTypes.CUT; }
    "@" { return PicatTokenTypes.AT; }


    // Literals (Regex-based)
    [a-z][a-zA-Z0-9_]* { return PicatTokenTypes.IDENTIFIER; }
    [A-Z][a-zA-Z0-9_]* { return PicatTokenTypes.VARIABLE; }
    "_"([a-zA-Z0-9_]*)? { return PicatTokenTypes.ANONYMOUS_VARIABLE; } // Made the rest optional as per typical anonymous var like _ or _Var

    "0"[xX][0-9a-fA-F]+ { return PicatTokenTypes.HEX_INTEGER; }
    "0"[oO][0-7]+ { return PicatTokenTypes.OCTAL_INTEGER; }
    "0"[bB][01]+ { return PicatTokenTypes.BINARY_INTEGER; }
    [0-9]+\.[0-9]+([eE][+-]?[0-9]+)? { return PicatTokenTypes.FLOAT; }
    [0-9]+ { return PicatTokenTypes.INTEGER; } // Must be after FLOAT and specific base integers

    // String and Quoted Atom regex need careful escaping for JFlex
    // BNF: \"([^\"\\]|\\.)*\"  => JFlex: string literal with embedded regex
    // JFlex string for regex: Use \" for literal quote, \\ for literal backslash
    \"([^\"\\\\]|\\\\.)*\" { return PicatTokenTypes.STRING; }
    \'([^\'\\\\]|\\\\.)*\' { return PicatTokenTypes.QUOTED_ATOM; }

}

// Catch-all for any other character (usually an error)
[^] { return TokenType.BAD_CHARACTER; }
