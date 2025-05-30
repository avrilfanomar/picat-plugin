package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.TokenSet

/**
 * Token types and element types for Picat language.
 * This file defines constants for all token types and PSI element types used in the Picat language.
 */
object PicatTokenTypes {
    // Keywords
    val MODULE_KEYWORD = PicatTokenType("MODULE_KEYWORD")
    val END_MODULE_KEYWORD = PicatTokenType("END_MODULE_KEYWORD")
    val IMPORT_KEYWORD = PicatTokenType("IMPORT_KEYWORD")
    val EXPORT_KEYWORD = PicatTokenType("EXPORT_KEYWORD")
    val INCLUDE_KEYWORD = PicatTokenType("INCLUDE_KEYWORD")
    val IF_KEYWORD = PicatTokenType("IF_KEYWORD")
    val THEN_KEYWORD = PicatTokenType("THEN_KEYWORD")
    val ELSEIF_KEYWORD = PicatTokenType("ELSEIF_KEYWORD")
    val ELSE_KEYWORD = PicatTokenType("ELSE_KEYWORD")
    val END_KEYWORD = PicatTokenType("END_KEYWORD")
    val FOREACH_KEYWORD = PicatTokenType("FOREACH_KEYWORD")
    val IN_KEYWORD = PicatTokenType("IN_KEYWORD")
    val RETURN_KEYWORD = PicatTokenType("RETURN_KEYWORD")
    val CONTINUE_KEYWORD = PicatTokenType("CONTINUE_KEYWORD")
    val BREAK_KEYWORD = PicatTokenType("BREAK_KEYWORD")
    val NOT_KEYWORD = PicatTokenType("NOT_KEYWORD")
    val FAIL_KEYWORD = PicatTokenType("FAIL_KEYWORD")
    val TRUE_KEYWORD = PicatTokenType("TRUE_KEYWORD")
    val FALSE_KEYWORD = PicatTokenType("FALSE_KEYWORD")
    val CASE_KEYWORD = PicatTokenType("CASE_KEYWORD")
    val OF_KEYWORD = PicatTokenType("OF_KEYWORD")
    val TRY_KEYWORD = PicatTokenType("TRY_KEYWORD")
    val CATCH_KEYWORD = PicatTokenType("CATCH_KEYWORD")
    val FINALLY_KEYWORD = PicatTokenType("FINALLY_KEYWORD")
    val THROW_KEYWORD = PicatTokenType("THROW_KEYWORD")
    val USING_KEYWORD = PicatTokenType("USING_KEYWORD")
    val WHILE_KEYWORD = PicatTokenType("WHILE_KEYWORD")
    val DO_KEYWORD = PicatTokenType("DO_KEYWORD")
    val PASS_KEYWORD = PicatTokenType("PASS_KEYWORD")

    // Legacy keywords (not in current BNF but kept for backward compatibility)
    val INDEX_KEYWORD = PicatTokenType("INDEX_KEYWORD")
    val PRIVATE_KEYWORD = PicatTokenType("PRIVATE_KEYWORD")
    val PUBLIC_KEYWORD = PicatTokenType("PUBLIC_KEYWORD")
    val TABLE_KEYWORD = PicatTokenType("TABLE_KEYWORD")
    val FOR_KEYWORD = PicatTokenType("FOR_KEYWORD")
    val ONCE_KEYWORD = PicatTokenType("ONCE_KEYWORD")
    val DIV_KEYWORD = PicatTokenType("DIV_KEYWORD")
    val REM_KEYWORD = PicatTokenType("REM_KEYWORD")
    val NOTIN_KEYWORD = PicatTokenType("NOTIN_KEYWORD")
    val WRITEF_KEYWORD = PicatTokenType("WRITEF_KEYWORD")
    val REPEAT_KEYWORD = PicatTokenType("REPEAT_KEYWORD")

    // Operators
    val ARROW_OP = PicatTokenType("ARROW_OP") // =>
    val BACKTRACKABLE_ARROW_OP = PicatTokenType("BACKTRACKABLE_ARROW_OP") // ?=>
    val BICONDITIONAL_OP = PicatTokenType("BICONDITIONAL_OP") // <=>
    val BACKTRACKABLE_BICONDITIONAL_OP = PicatTokenType("BACKTRACKABLE_BICONDITIONAL_OP") // ?<=>
    val RULE_OP = PicatTokenType("RULE_OP") // :-
    val ASSIGN_OP = PicatTokenType("ASSIGN_OP") // :=
    val RANGE_OP = PicatTokenType("RANGE_OP") // ..
    val EQUAL = PicatTokenType("EQUAL") // =
    val NOT_EQUAL = PicatTokenType("NOT_EQUAL") // !=
    val LESS = PicatTokenType("LESS") // <
    val GREATER = PicatTokenType("GREATER") // >
    val LESS_EQUAL = PicatTokenType("LESS_EQUAL") // <=
    val GREATER_EQUAL = PicatTokenType("GREATER_EQUAL") // >=
    val IDENTICAL = PicatTokenType("IDENTICAL") // =:=
    val NOT_IDENTICAL = PicatTokenType("NOT_IDENTICAL") // =\=
    val PLUS = PicatTokenType("PLUS") // +
    val MINUS = PicatTokenType("MINUS") // -
    val MULTIPLY = PicatTokenType("MULTIPLY") // *
    val POWER = PicatTokenType("POWER") // **
    val DIVIDE = PicatTokenType("DIVIDE") // /
    val INT_DIVIDE = PicatTokenType("INT_DIVIDE") // //
    val MOD_KEYWORD = PicatTokenType("MOD_KEYWORD") // mod
    val SHIFT_LEFT = PicatTokenType("SHIFT_LEFT") // <<
    val SHIFT_RIGHT = PicatTokenType("SHIFT_RIGHT") // >>
    val BACKSLASH = PicatTokenType("BACKSLASH") // \
    val AND_KEYWORD = PicatTokenType("AND_KEYWORD") // and
    val OR_KEYWORD = PicatTokenType("OR_KEYWORD") // or
    val XOR_KEYWORD = PicatTokenType("XOR_KEYWORD") // xor
    val CARET = PicatTokenType("CARET") // ^
    val AMPERSAND = PicatTokenType("AMPERSAND") // &
    val PIPE = PicatTokenType("PIPE") // |
    val IS_KEYWORD = PicatTokenType("IS_KEYWORD") // is
    val DOT_OP = PicatTokenType("DOT_OP") // .
    val CONCAT_OP = PicatTokenType("CONCAT_OP") // ++

    // Legacy operators (not in current BNF but kept for backward compatibility)
    val DIVIDE_LT = PicatTokenType("DIVIDE_LT") // /<
    val DIVIDE_GT = PicatTokenType("DIVIDE_GT") // />
    val MODULO = PicatTokenType("MODULO") // mod (alias for MOD_KEYWORD)
    val LESS_EQUAL_ALT = PicatTokenType("LESS_EQUAL_ALT") // =<
    val AND = PicatTokenType("AND") // &&
    val OR = PicatTokenType("OR") // ||
    val DATA_CONSTRUCTOR = PicatTokenType("DATA_CONSTRUCTOR") // $
    val HASH = PicatTokenType("HASH") // #
    val TILDE = PicatTokenType("TILDE") // ~
    val POWER_OP = PicatTokenType("POWER_OP") // ** (alias for POWER)
    val BITWISE_AND = PicatTokenType("BITWISE_AND") // /\
    val BITWISE_OR = PicatTokenType("BITWISE_OR") // \/
    val RANGE = PicatTokenType("RANGE") // .. (alias for RANGE_OP)
    val CONCAT = PicatTokenType("CONCAT") // ++ (alias for CONCAT_OP)
    val TYPE_CONSTRAINT = PicatTokenType("TYPE_CONSTRAINT") // ::
    val ASSIGN_ONCE = PicatTokenType("ASSIGN_ONCE") // := (alias for ASSIGN_OP)
    val NOT = PicatTokenType("NOT") // ! (alias for CUT)
    val AS_PATTERN = PicatTokenType("AS_PATTERN") // @ (alias for AT)

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
    val DOT = PicatTokenType("DOT") // .
    val COMMA = PicatTokenType("COMMA") // ,
    val SEMICOLON = PicatTokenType("SEMICOLON") // ;
    val COLON = PicatTokenType("COLON") // :
    val LPAR = PicatTokenType("LPAR") // (
    val RPAR = PicatTokenType("RPAR") // )
    val LBRACKET = PicatTokenType("LBRACKET") // [
    val RBRACKET = PicatTokenType("RBRACKET") // ]
    val LBRACE = PicatTokenType("LBRACE") // {
    val RBRACE = PicatTokenType("RBRACE") // }
    val CUT = PicatTokenType("CUT") // !
    val PIPE_CHOICE = PicatTokenType("PIPE_CHOICE") // |
    val AT = PicatTokenType("AT") // @

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
    val MULTI_LINE_COMMENT = PicatTokenType("MULTI_LINE_COMMENT")

    // Module functions and operators
    val BASIC_MODULE_FUNCTION = PicatTokenType("BASIC_MODULE_FUNCTION")

    // Other
    val IDENTIFIER = PicatTokenType("IDENTIFIER")
    val WHITE_SPACE = PicatTokenType("WHITE_SPACE")
    val BAD_CHARACTER = PicatTokenType("BAD_CHARACTER")

    // Token sets
    val KEYWORDS = TokenSet.create(
        // BNF keywords
        MODULE_KEYWORD, END_MODULE_KEYWORD, IMPORT_KEYWORD, EXPORT_KEYWORD, INCLUDE_KEYWORD,
        IF_KEYWORD, THEN_KEYWORD, ELSEIF_KEYWORD, ELSE_KEYWORD, END_KEYWORD,
        FOREACH_KEYWORD, IN_KEYWORD, RETURN_KEYWORD, CONTINUE_KEYWORD, BREAK_KEYWORD,
        NOT_KEYWORD, FAIL_KEYWORD, TRUE_KEYWORD, FALSE_KEYWORD, CASE_KEYWORD, OF_KEYWORD,
        TRY_KEYWORD, CATCH_KEYWORD, FINALLY_KEYWORD, THROW_KEYWORD, USING_KEYWORD,
        WHILE_KEYWORD, DO_KEYWORD, PASS_KEYWORD, MOD_KEYWORD, AND_KEYWORD, OR_KEYWORD, XOR_KEYWORD, IS_KEYWORD,

        // Legacy keywords (kept for backward compatibility)
        INDEX_KEYWORD, PRIVATE_KEYWORD, PUBLIC_KEYWORD, TABLE_KEYWORD, FOR_KEYWORD,
        ONCE_KEYWORD, DIV_KEYWORD, REM_KEYWORD, NOTIN_KEYWORD, WRITEF_KEYWORD, REPEAT_KEYWORD
    )

    val OPERATORS = TokenSet.create(
        // BNF operators
        ARROW_OP, BACKTRACKABLE_ARROW_OP, BICONDITIONAL_OP, BACKTRACKABLE_BICONDITIONAL_OP,
        RULE_OP, ASSIGN_OP, RANGE_OP, EQUAL, NOT_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        IDENTICAL, NOT_IDENTICAL, PLUS, MINUS, MULTIPLY, POWER, DIVIDE, INT_DIVIDE,
        MOD_KEYWORD, SHIFT_LEFT, SHIFT_RIGHT, BACKSLASH, AND_KEYWORD, OR_KEYWORD, XOR_KEYWORD,
        CARET, AMPERSAND, PIPE, IS_KEYWORD, DOT_OP, CONCAT_OP,

        // Legacy operators (kept for backward compatibility)
        POWER_OP, DIVIDE_LT, DIVIDE_GT, MODULO, LESS_EQUAL_ALT, AND, OR,
        DATA_CONSTRUCTOR, HASH, TILDE, BITWISE_AND, BITWISE_OR, RANGE, CONCAT,
        TYPE_CONSTRAINT, ASSIGN_ONCE, CUT, AT,

        // Constraint operators
        CONSTRAINT_EQ, CONSTRAINT_NEQ, CONSTRAINT_LT, CONSTRAINT_LE, CONSTRAINT_LE_ALT,
        CONSTRAINT_GT, CONSTRAINT_GE, CONSTRAINT_NOT, CONSTRAINT_AND, CONSTRAINT_OR,
        CONSTRAINT_XOR, CONSTRAINT_IMPL, CONSTRAINT_EQUIV,

        // Term comparison operators
        TERM_LT, TERM_LE, TERM_LE_ALT, TERM_GT, TERM_GE
    )

    val COMMENTS = TokenSet.create(COMMENT, MULTI_LINE_COMMENT)
    val STRING_LITERALS = TokenSet.create(STRING)
    val NUMBERS = TokenSet.create(INTEGER, HEX_INTEGER, OCTAL_INTEGER, BINARY_INTEGER, FLOAT)
    val ATOMS = TokenSet.create(ATOM, QUOTED_ATOM)
    val VARIABLES = TokenSet.create(VARIABLE, ANONYMOUS_VARIABLE)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)

    // PSI Element Types
    val MODULE_NAME = PicatElementType("MODULE_NAME")
    val MODULE_DECLARATION = PicatElementType("MODULE_DECLARATION")
    val END_MODULE_DECLARATION = PicatElementType("END_MODULE_DECLARATION")
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
    val STATEMENT = PicatElementType("STATEMENT")
    val PROGRAM = PicatElementType("PROGRAM")
    val IF_THEN_ELSE = PicatElementType("IF_THEN_ELSE")
    val FOREACH_LOOP = PicatElementType("FOREACH_LOOP")
    val WHILE_LOOP = PicatElementType("WHILE_LOOP")
    val FOR_LOOP = PicatElementType("FOR_LOOP")
    val TRY_CATCH = PicatElementType("TRY_CATCH")
    val LIST_COMPREHENSION = PicatElementType("LIST_COMPREHENSION")
    val FUNCTION_CALL = PicatElementType("FUNCTION_CALL")
    val USING_STATEMENT = PicatElementType("USING_STATEMENT")

    // New PSI Element Types
    val CASE_EXPRESSION = PicatElementType("CASE_EXPRESSION")
    val CASE_ARMS = PicatElementType("CASE_ARMS")
    val CASE_ARM = PicatElementType("CASE_ARM")
    val PATTERN = PicatElementType("PATTERN")
    val STRUCTURE_PATTERN = PicatElementType("STRUCTURE_PATTERN")
    val LIST_PATTERN = PicatElementType("LIST_PATTERN")
    val TUPLE_PATTERN = PicatElementType("TUPLE_PATTERN")
    val PATTERN_LIST = PicatElementType("PATTERN_LIST")
    val THROW_STATEMENT = PicatElementType("THROW_STATEMENT")
    val MAP = PicatElementType("MAP")
    val MAP_ENTRIES = PicatElementType("MAP_ENTRIES")
    val MAP_ENTRY = PicatElementType("MAP_ENTRY")

    // Additional PSI Element Types
    val FACT = PicatElementType("FACT")
    val EXPORT_STATEMENT = PicatElementType("EXPORT_STATEMENT")
    val PREDICATE_INDICATOR = PicatElementType("PREDICATE_INDICATOR")
    val INCLUDE_STATEMENT = PicatElementType("INCLUDE_STATEMENT")
    val HEAD = PicatElementType("HEAD")
    val GOAL = PicatElementType("GOAL")
    val ATOM_NO_ARGS = PicatElementType("ATOM_NO_ARGS")
    val RANGE_EXPRESSION = PicatElementType("RANGE_EXPRESSION")

    // Missing PSI Element Types needed for parser
    val DEFINITION = PicatElementType("DEFINITION")
    val RULE_OPERATOR = PicatElementType("RULE_OPERATOR")
    val QUESTION = PicatTokenType("QUESTION") // ?
    val UNARY_EXPRESSION = PicatElementType("UNARY_EXPRESSION")
    val EXPORT_CLAUSE = PicatElementType("EXPORT_CLAUSE")
    val IMPORT_CLAUSE = PicatElementType("IMPORT_CLAUSE")
    val IMPORT_LIST = PicatElementType("IMPORT_LIST")
    val EXPORT_LIST = PicatElementType("EXPORT_LIST")
    val RENAME_LIST = PicatElementType("RENAME_LIST")
    val FOREACH_GENERATORS = PicatElementType("FOREACH_GENERATORS")
    val FOREACH_GENERATOR = PicatElementType("FOREACH_GENERATOR")
    val CASE_CLAUSES = PicatElementType("CASE_CLAUSES")
    val CATCH_CLAUSES = PicatElementType("CATCH_CLAUSES")
    val CATCH_CLAUSE = PicatElementType("CATCH_CLAUSE")
    val ELSEIF_CLAUSE = PicatElementType("ELSEIF_CLAUSE")
    val MODULE_SPEC = PicatElementType("MODULE_SPEC")

    // Statement types
    val NEGATION = PicatElementType("NEGATION")
    val FAIL_STATEMENT = PicatElementType("FAIL_STATEMENT")
    val PASS_STATEMENT = PicatElementType("PASS_STATEMENT")
    val TRUE_STATEMENT = PicatElementType("TRUE_STATEMENT")
    val FALSE_STATEMENT = PicatElementType("FALSE_STATEMENT")
    val CUT_STATEMENT = PicatElementType("CUT_STATEMENT")
    val RETURN_STATEMENT = PicatElementType("RETURN_STATEMENT")
    val CONTINUE_STATEMENT = PicatElementType("CONTINUE_STATEMENT")
    val BREAK_STATEMENT = PicatElementType("BREAK_STATEMENT")
    val ASSIGNMENT = PicatElementType("ASSIGNMENT")
}
