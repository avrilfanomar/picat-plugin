package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.TokenSet

/**
 * Token types and element types for Picat language.
 * This file defines constants for all token types and PSI element types used in the Picat language.
 */
object PicatTokenTypes {
    // Keywords
    val IMPORT_KEYWORD = PicatTokenType("IMPORT_KEYWORD")
    val EXPORT_KEYWORD = PicatTokenType("EXPORT_KEYWORD")
    val INCLUDE_KEYWORD = PicatTokenType("INCLUDE_KEYWORD")
    val MODULE_KEYWORD = PicatTokenType("MODULE_KEYWORD")
    val INDEX_KEYWORD = PicatTokenType("INDEX_KEYWORD")
    val PRIVATE_KEYWORD = PicatTokenType("PRIVATE_KEYWORD")
    val PUBLIC_KEYWORD = PicatTokenType("PUBLIC_KEYWORD")
    val TABLE_KEYWORD = PicatTokenType("TABLE_KEYWORD")
    val END_KEYWORD = PicatTokenType("END_KEYWORD")
    val IF_KEYWORD = PicatTokenType("IF_KEYWORD")
    val THEN_KEYWORD = PicatTokenType("THEN_KEYWORD")
    val ELSE_KEYWORD = PicatTokenType("ELSE_KEYWORD")
    val ELSEIF_KEYWORD = PicatTokenType("ELSEIF_KEYWORD")
    val WHILE_KEYWORD = PicatTokenType("WHILE_KEYWORD")
    val DO_KEYWORD = PicatTokenType("DO_KEYWORD")
    val FOREACH_KEYWORD = PicatTokenType("FOREACH_KEYWORD")
    val FOR_KEYWORD = PicatTokenType("FOR_KEYWORD")
    val RETURN_KEYWORD = PicatTokenType("RETURN_KEYWORD")
    val THROW_KEYWORD = PicatTokenType("THROW_KEYWORD")
    val TRY_KEYWORD = PicatTokenType("TRY_KEYWORD")
    val CATCH_KEYWORD = PicatTokenType("CATCH_KEYWORD")
    val NOT_KEYWORD = PicatTokenType("NOT_KEYWORD")
    val ONCE_KEYWORD = PicatTokenType("ONCE_KEYWORD")
    val DIV_KEYWORD = PicatTokenType("DIV_KEYWORD")
    val MOD_KEYWORD = PicatTokenType("MOD_KEYWORD")
    val REM_KEYWORD = PicatTokenType("REM_KEYWORD")
    val IN_KEYWORD = PicatTokenType("IN_KEYWORD")
    val NOTIN_KEYWORD = PicatTokenType("NOTIN_KEYWORD")
    val WRITEF_KEYWORD = PicatTokenType("WRITEF_KEYWORD")
    val TRUE_KEYWORD = PicatTokenType("TRUE_KEYWORD")
    val FALSE_KEYWORD = PicatTokenType("FALSE_KEYWORD")
    val FAIL_KEYWORD = PicatTokenType("FAIL_KEYWORD")
    val REPEAT_KEYWORD = PicatTokenType("REPEAT_KEYWORD")

    // Operators
    val ASSIGN_OP = PicatTokenType("ASSIGN_OP") // =
    val ARROW_OP = PicatTokenType("ARROW_OP") // =>
    val BACKTRACKABLE_ARROW_OP = PicatTokenType("BACKTRACKABLE_ARROW_OP") // ?=>
    val PLUS = PicatTokenType("PLUS") // +
    val MINUS = PicatTokenType("MINUS") // -
    val MULTIPLY = PicatTokenType("MULTIPLY") // *
    val POWER_OP = PicatTokenType("POWER_OP") // **
    val DIVIDE = PicatTokenType("DIVIDE") // /
    val INT_DIVIDE = PicatTokenType("INT_DIVIDE") // //
    val DIVIDE_LT = PicatTokenType("DIVIDE_LT") // /<
    val DIVIDE_GT = PicatTokenType("DIVIDE_GT") // />
    val MODULO = PicatTokenType("MODULO") // mod
    val EQUAL = PicatTokenType("EQUAL") // ==
    val NOT_EQUAL = PicatTokenType("NOT_EQUAL") // !=
    val NOT_IDENTICAL = PicatTokenType("NOT_IDENTICAL") // !==
    val IDENTICAL = PicatTokenType("IDENTICAL") // ===
    val LESS = PicatTokenType("LESS") // <
    val LESS_EQUAL = PicatTokenType("LESS_EQUAL") // <=
    val LESS_EQUAL_ALT = PicatTokenType("LESS_EQUAL_ALT") // =<
    val GREATER = PicatTokenType("GREATER") // >
    val GREATER_EQUAL = PicatTokenType("GREATER_EQUAL") // >=
    val AND = PicatTokenType("AND") // &&
    val OR = PicatTokenType("OR") // ||
    val PIPE = PicatTokenType("PIPE") // |
    val AS_PATTERN = PicatTokenType("AS_PATTERN") // @
    val DATA_CONSTRUCTOR = PicatTokenType("DATA_CONSTRUCTOR") // $
    val POWER = PicatTokenType("POWER") // ^
    val HASH = PicatTokenType("HASH") // #
    val TILDE = PicatTokenType("TILDE") // ~
    val BACKSLASH = PicatTokenType("BACKSLASH") // \
    val NOT = PicatTokenType("NOT") // !
    val SHIFT_LEFT = PicatTokenType("SHIFT_LEFT") // <<
    val SHIFT_RIGHT = PicatTokenType("SHIFT_RIGHT") // >>
    val BITWISE_AND = PicatTokenType("BITWISE_AND") // /\
    val BITWISE_OR = PicatTokenType("BITWISE_OR") // \/
    val RANGE = PicatTokenType("RANGE") // ..
    val CONCAT = PicatTokenType("CONCAT") // ++
    val TYPE_CONSTRAINT = PicatTokenType("TYPE_CONSTRAINT") // ::
    val ASSIGN_ONCE = PicatTokenType("ASSIGN_ONCE") // :=

    // Constraint operators
    val CONSTRAINT_EQ = PicatTokenType("CONSTRAINT_EQ") // #=
    val CONSTRAINT_NEQ = PicatTokenType("CONSTRAINT_NEQ") // #!=
    val CONSTRAINT_LT = PicatTokenType("CONSTRAINT_LT") // #<
    val CONSTRAINT_LE = PicatTokenType("CONSTRAINT_LE") // #=<
    val CONSTRAINT_LE_ALT = PicatTokenType("CONSTRAINT_LE_ALT") // #<=
    val CONSTRAINT_GT = PicatTokenType("CONSTRAINT_GT") // #>
    val CONSTRAINT_GE = PicatTokenType("CONSTRAINT_GE") // #>=
    val CONSTRAINT_NOT = PicatTokenType("CONSTRAINT_NOT") // #~
    val CONSTRAINT_AND = PicatTokenType("CONSTRAINT_AND") // #/\
    val CONSTRAINT_OR = PicatTokenType("CONSTRAINT_OR") // #\/
    val CONSTRAINT_XOR = PicatTokenType("CONSTRAINT_XOR") // #^
    val CONSTRAINT_IMPL = PicatTokenType("CONSTRAINT_IMPL") // #=>
    val CONSTRAINT_EQUIV = PicatTokenType("CONSTRAINT_EQUIV") // #<=>

    // Term comparison operators
    val TERM_LT = PicatTokenType("TERM_LT") // @<
    val TERM_LE = PicatTokenType("TERM_LE") // @=<
    val TERM_LE_ALT = PicatTokenType("TERM_LE_ALT") // @<=
    val TERM_GT = PicatTokenType("TERM_GT") // @>
    val TERM_GE = PicatTokenType("TERM_GE") // @>=

    // Separators
    val COMMA = PicatTokenType("COMMA") // ,
    val DOT = PicatTokenType("DOT") // .
    val SEMICOLON = PicatTokenType("SEMICOLON") // ;
    val COLON = PicatTokenType("COLON") // :
    val LPAR = PicatTokenType("LPAR") // (
    val RPAR = PicatTokenType("RPAR") // )
    val LBRACKET = PicatTokenType("LBRACKET") // [
    val RBRACKET = PicatTokenType("RBRACKET") // ]
    val LBRACE = PicatTokenType("LBRACE") // {
    val RBRACE = PicatTokenType("RBRACE") // }

    // Literals
    val INTEGER = PicatTokenType("INTEGER")
    val HEX_INTEGER = PicatTokenType("HEX_INTEGER")
    val OCTAL_INTEGER = PicatTokenType("OCTAL_INTEGER")
    val BINARY_INTEGER = PicatTokenType("BINARY_INTEGER")
    val FLOAT = PicatTokenType("FLOAT")
    val STRING = PicatTokenType("STRING")
    val ATOM = PicatTokenType("ATOM")
    val QUOTED_ATOM = PicatTokenType("QUOTED_ATOM")
    val VARIABLE = PicatTokenType("VARIABLE")
    val ANONYMOUS_VARIABLE = PicatTokenType("ANONYMOUS_VARIABLE")

    // Comments
    val COMMENT = PicatTokenType("COMMENT")

    // Module functions and operators
    val BASIC_MODULE_FUNCTION = PicatTokenType("BASIC_MODULE_FUNCTION")

    // Other
    val IDENTIFIER = PicatTokenType("IDENTIFIER")
    val WHITE_SPACE = PicatTokenType("WHITE_SPACE")
    val BAD_CHARACTER = PicatTokenType("BAD_CHARACTER")

    // Token sets
    val KEYWORDS = TokenSet.create(
        IMPORT_KEYWORD, EXPORT_KEYWORD, INCLUDE_KEYWORD, MODULE_KEYWORD, INDEX_KEYWORD, PRIVATE_KEYWORD, PUBLIC_KEYWORD, TABLE_KEYWORD,
        END_KEYWORD, IF_KEYWORD, THEN_KEYWORD, ELSE_KEYWORD, ELSEIF_KEYWORD,
        WHILE_KEYWORD, DO_KEYWORD, FOREACH_KEYWORD, FOR_KEYWORD, RETURN_KEYWORD,
        THROW_KEYWORD, TRY_KEYWORD, CATCH_KEYWORD, NOT_KEYWORD, ONCE_KEYWORD,
        DIV_KEYWORD, MOD_KEYWORD, REM_KEYWORD, IN_KEYWORD, NOTIN_KEYWORD, WRITEF_KEYWORD,
        TRUE_KEYWORD, FALSE_KEYWORD, FAIL_KEYWORD, REPEAT_KEYWORD
    )

    val OPERATORS = TokenSet.create(
        ASSIGN_OP, ARROW_OP, BACKTRACKABLE_ARROW_OP, PLUS, MINUS, MULTIPLY, POWER_OP,
        DIVIDE, INT_DIVIDE, DIVIDE_LT, DIVIDE_GT, MODULO,
        EQUAL, NOT_EQUAL, NOT_IDENTICAL, IDENTICAL, LESS, LESS_EQUAL, LESS_EQUAL_ALT,
        GREATER, GREATER_EQUAL, AND, OR, PIPE, AS_PATTERN, DATA_CONSTRUCTOR,
        POWER, HASH, TILDE, BACKSLASH, NOT, SHIFT_LEFT, SHIFT_RIGHT,
        BITWISE_AND, BITWISE_OR, RANGE, CONCAT, TYPE_CONSTRAINT, ASSIGN_ONCE,
        CONSTRAINT_EQ, CONSTRAINT_NEQ, CONSTRAINT_LT, CONSTRAINT_LE, CONSTRAINT_LE_ALT,
        CONSTRAINT_GT, CONSTRAINT_GE, CONSTRAINT_NOT, CONSTRAINT_AND, CONSTRAINT_OR,
        CONSTRAINT_XOR, CONSTRAINT_IMPL, CONSTRAINT_EQUIV,
        TERM_LT, TERM_LE, TERM_LE_ALT, TERM_GT, TERM_GE
    )

    val COMMENTS = TokenSet.create(COMMENT)
    val STRING_LITERALS = TokenSet.create(STRING)
    val NUMBERS = TokenSet.create(INTEGER, HEX_INTEGER, OCTAL_INTEGER, BINARY_INTEGER, FLOAT)
    val ATOMS = TokenSet.create(ATOM, QUOTED_ATOM)
    val VARIABLES = TokenSet.create(VARIABLE, ANONYMOUS_VARIABLE)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)

    // PSI Element Types
    val MODULE_NAME = PicatElementType("MODULE_NAME")
    val MODULE_DECLARATION = PicatElementType("MODULE_DECLARATION")
    val IMPORT_STATEMENT = PicatElementType("IMPORT_STATEMENT")
    val FUNCTION_BODY = PicatElementType("FUNCTION_BODY")
    val PREDICATE_BODY = PicatElementType("PREDICATE_BODY")
    val FUNCTION_DEFINITION = PicatElementType("FUNCTION_DEFINITION")
    val PREDICATE_DEFINITION = PicatElementType("PREDICATE_DEFINITION")
    val ARGUMENT_LIST = PicatElementType("ARGUMENT_LIST")
    val ARGUMENT = PicatElementType("ARGUMENT")
    val CLAUSE_LIST = PicatElementType("CLAUSE_LIST")
    val CLAUSE = PicatElementType("CLAUSE")
    val OPERATOR = PicatElementType("OPERATOR")
    val EXPRESSION = PicatElementType("EXPRESSION")
    val TERM = PicatElementType("TERM")
    val LITERAL = PicatElementType("LITERAL")
    val STRUCTURE = PicatElementType("STRUCTURE")
    val LIST = PicatElementType("LIST")
    val LIST_ELEMENTS = PicatElementType("LIST_ELEMENTS")
    val EQUAL_EQUAL = PicatElementType("EQUAL_EQUAL")
    val IS = PicatElementType("IS")
    val BODY = PicatElementType("BODY")
    val RULE = PicatElementType("RULE")
    val RULE_BODY = PicatElementType("RULE_BODY")
    val STATEMENT = PicatElementType("STATEMENT")
    val PROGRAM = PicatElementType("PROGRAM")
    val IF_THEN_ELSE = PicatElementType("IF_THEN_ELSE")
    val FOREACH_LOOP = PicatElementType("FOREACH_LOOP")
    val WHILE_LOOP = PicatElementType("WHILE_LOOP")
    val FOR_LOOP = PicatElementType("FOR_LOOP")
    val TRY_CATCH = PicatElementType("TRY_CATCH")
    val LIST_COMPREHENSION = PicatElementType("LIST_COMPREHENSION")
    val FUNCTION_CALL = PicatElementType("FUNCTION_CALL")

    // Additional PSI Element Types
    val FACT = PicatElementType("FACT")
    val EXPORT_STATEMENT = PicatElementType("EXPORT_STATEMENT")
    val PREDICATE_INDICATOR = PicatElementType("PREDICATE_INDICATOR")
    val INCLUDE_STATEMENT = PicatElementType("INCLUDE_STATEMENT")
    val HEAD = PicatElementType("HEAD")
    val GOAL = PicatElementType("GOAL")
}
