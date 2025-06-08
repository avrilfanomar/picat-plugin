package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.TokenSet

object PicatTokenTypes {
    // Keywords from BNF
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
    val LOOP_KEYWORD = PicatTokenType("LOOP_KEYWORD")
    val PRIVATE_KEYWORD = PicatTokenType("PRIVATE_KEYWORD")
    val TABLE_KEYWORD = PicatTokenType("TABLE_KEYWORD")
    val INDEX_KEYWORD = PicatTokenType("INDEX_KEYWORD")
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
    val DIV_KEYWORD = PicatTokenType("DIV_KEYWORD")
    val REM_KEYWORD = PicatTokenType("REM_KEYWORD")
    val MOD_KEYWORD = PicatTokenType("MOD_KEYWORD")
    val AND_KEYWORD = PicatTokenType("AND_KEYWORD")
    val OR_KEYWORD = PicatTokenType("OR_KEYWORD")
    val XOR_KEYWORD = PicatTokenType("XOR_KEYWORD")
    val IS_KEYWORD = PicatTokenType("IS_KEYWORD")

    // Operators from BNF
    val ARROW_OP = PicatTokenType("ARROW_OP")
    val BACKTRACKABLE_ARROW_OP = PicatTokenType("BACKTRACKABLE_ARROW_OP")
    val BICONDITIONAL_OP = PicatTokenType("BICONDITIONAL_OP")
    val BACKTRACKABLE_BICONDITIONAL_OP = PicatTokenType("BACKTRACKABLE_BICONDITIONAL_OP")
    val HASH_BICONDITIONAL_OP = PicatTokenType("HASH_BICONDITIONAL_OP")
    val HASH_ARROW_OP = PicatTokenType("HASH_ARROW_OP")
    val HASH_OR_OP = PicatTokenType("HASH_OR_OP")
    val HASH_CARET_OP = PicatTokenType("HASH_CARET_OP")
    val HASH_AND_OP = PicatTokenType("HASH_AND_OP")
    val HASH_TILDE_OP = PicatTokenType("HASH_TILDE_OP")
    val RULE_OP = PicatTokenType("RULE_OP")
    val ASSIGN_OP = PicatTokenType("ASSIGN_OP")
    val RANGE_OP = PicatTokenType("RANGE_OP")
    val EQUAL = PicatTokenType("EQUAL")
    val NOT_EQUAL = PicatTokenType("NOT_EQUAL")
    val LESS = PicatTokenType("LESS")
    val GREATER = PicatTokenType("GREATER")
    val LESS_EQUAL = PicatTokenType("LESS_EQUAL")
    val GREATER_EQUAL = PicatTokenType("GREATER_EQUAL")
    val IDENTICAL = PicatTokenType("IDENTICAL")
    val NOT_IDENTICAL = PicatTokenType("NOT_IDENTICAL")
    val PLUS = PicatTokenType("PLUS")
    val MINUS = PicatTokenType("MINUS")
    val MULTIPLY = PicatTokenType("MULTIPLY")
    val POWER = PicatTokenType("POWER")
    val DIVIDE = PicatTokenType("DIVIDE")
    val INT_DIVIDE = PicatTokenType("INT_DIVIDE")
    val SHIFT_LEFT = PicatTokenType("SHIFT_LEFT")
    val SHIFT_RIGHT = PicatTokenType("SHIFT_RIGHT")
    val SHIFT_RIGHT_TRIPLE_OP = PicatTokenType("SHIFT_RIGHT_TRIPLE_OP")
    val BACKSLASH = PicatTokenType("BACKSLASH")
    val CARET = PicatTokenType("CARET")
    val AMPERSAND = PicatTokenType("AMPERSAND")
    val PIPE = PicatTokenType("PIPE")
    val DOT_OP = PicatTokenType("DOT_OP")
    val CONCAT_OP = PicatTokenType("CONCAT_OP")

    // Separators from BNF
    val DOT = PicatTokenType("DOT")
    val EOR = PicatTokenType("EOR")
    val COMMA = PicatTokenType("COMMA")
    val SEMICOLON = PicatTokenType("SEMICOLON")
    val COLON = PicatTokenType("COLON")
    val LPAR = PicatTokenType("LPAR")
    val RPAR = PicatTokenType("RPAR")
    val LBRACKET = PicatTokenType("LBRACKET")
    val RBRACKET = PicatTokenType("RBRACKET")
    val LBRACE = PicatTokenType("LBRACE")
    val RBRACE = PicatTokenType("RBRACE")
    val CUT = PicatTokenType("CUT")
    val PIPE_CHOICE = PicatTokenType("PIPE_CHOICE")
    val AT = PicatTokenType("AT")

    // Literals from BNF
    val IDENTIFIER = PicatTokenType("IDENTIFIER")
    val VARIABLE = PicatTokenType("VARIABLE")
    val ANONYMOUS_VARIABLE = PicatTokenType("ANONYMOUS_VARIABLE")
    val INTEGER = PicatTokenType("INTEGER")
    val HEX_INTEGER = PicatTokenType("HEX_INTEGER")
    val OCTAL_INTEGER = PicatTokenType("OCTAL_INTEGER")
    val BINARY_INTEGER = PicatTokenType("BINARY_INTEGER")
    val FLOAT = PicatTokenType("FLOAT")
    val STRING = PicatTokenType("STRING")
    val QUOTED_ATOM = PicatTokenType("QUOTED_ATOM")

    // Comments (as named tokens from BNF)
    val COMMENT = PicatTokenType("COMMENT")
    val MULTI_LINE_COMMENT = PicatTokenType("MULTI_LINE_COMMENT")

    val WHITE_SPACE = PicatTokenType("WHITE_SPACE")
    val BAD_CHARACTER = PicatTokenType("BAD_CHARACTER")

    // Token sets
    val KEYWORDS = TokenSet.create(
        MODULE_KEYWORD, END_MODULE_KEYWORD, IMPORT_KEYWORD, EXPORT_KEYWORD, INCLUDE_KEYWORD,
        IF_KEYWORD, THEN_KEYWORD, ELSEIF_KEYWORD, ELSE_KEYWORD, END_KEYWORD,
        FOREACH_KEYWORD, IN_KEYWORD, LOOP_KEYWORD, PRIVATE_KEYWORD, TABLE_KEYWORD, INDEX_KEYWORD,
        RETURN_KEYWORD, CONTINUE_KEYWORD, BREAK_KEYWORD,
        NOT_KEYWORD, FAIL_KEYWORD, TRUE_KEYWORD, FALSE_KEYWORD, CASE_KEYWORD, OF_KEYWORD,
        TRY_KEYWORD, CATCH_KEYWORD, FINALLY_KEYWORD, THROW_KEYWORD, USING_KEYWORD,
        WHILE_KEYWORD, DO_KEYWORD, PASS_KEYWORD,
        DIV_KEYWORD, REM_KEYWORD, MOD_KEYWORD, AND_KEYWORD, OR_KEYWORD, XOR_KEYWORD, IS_KEYWORD
    )

    val OPERATORS = TokenSet.create(
        ARROW_OP, BACKTRACKABLE_ARROW_OP, BICONDITIONAL_OP, BACKTRACKABLE_BICONDITIONAL_OP,
        HASH_BICONDITIONAL_OP, HASH_ARROW_OP, HASH_OR_OP, HASH_CARET_OP, HASH_AND_OP, HASH_TILDE_OP,
        RULE_OP, ASSIGN_OP, RANGE_OP, EQUAL, NOT_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        IDENTICAL, NOT_IDENTICAL, PLUS, MINUS, MULTIPLY, POWER, DIVIDE, INT_DIVIDE,
        SHIFT_LEFT, SHIFT_RIGHT, SHIFT_RIGHT_TRIPLE_OP,
        BACKSLASH, CARET, AMPERSAND, PIPE, DOT_OP, CONCAT_OP
    )

    val COMMENTS = TokenSet.create(COMMENT, MULTI_LINE_COMMENT)
    val STRING_LITERALS = TokenSet.create(STRING)
    val NUMBERS = TokenSet.create(INTEGER, HEX_INTEGER, OCTAL_INTEGER, BINARY_INTEGER, FLOAT)
    val ATOMS = TokenSet.create(IDENTIFIER, QUOTED_ATOM) // Corrected based on BNF `atom` rule
    val VARIABLES = TokenSet.create(VARIABLE, ANONYMOUS_VARIABLE)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)

    // --- PSI Element Types ---
    // Existing (some might be adjusted or become more specific wrappers)
    val PICAT_FILE = PicatElementType("PICAT_FILE") // Added for root
    val MODULE_NAME = PicatElementType("MODULE_NAME")
    val MODULE_DECLARATION = PicatElementType("MODULE_DECLARATION") // Covers module_decl in BNF
    val END_MODULE_DECLARATION = PicatElementType("END_MODULE_DECLARATION")
    val IMPORT_STATEMENT = PicatElementType("IMPORT_STATEMENT")
    val EXPORT_STATEMENT = PicatElementType("EXPORT_STATEMENT")
    val INCLUDE_STATEMENT = PicatElementType("INCLUDE_STATEMENT")
    val USING_STATEMENT = PicatElementType("USING_STATEMENT")

    val FUNCTION_BODY = PicatElementType("FUNCTION_BODY")
    val ARGUMENT_LIST = PicatElementType("ARGUMENT_LIST")
    // val ARGUMENT = PicatElementType("ARGUMENT") // Not a distinct rule with own element type in current BNF

    val DIRECTIVE = PicatElementType("DIRECTIVE") // For general_directive rule

    val EXPRESSION = PicatElementType("EXPRESSION")
    val STRUCTURE = PicatElementType("STRUCTURE")
    val LIST_EXPRESSION = PicatElementType("LIST_EXPRESSION") // Renamed from LIST
    val LIST_ELEMENTS = PicatElementType("LIST_ELEMENTS")
    val MAP = PicatElementType("MAP")
    val MAP_ENTRIES = PicatElementType("MAP_ENTRIES")
    val MAP_ENTRY = PicatElementType("MAP_ENTRY")
    val TUPLE = PicatElementType("TUPLE") // Added, as it's a primary_expression

    val BODY = PicatElementType("BODY")
    val STATEMENT = PicatElementType("STATEMENT") // A general type, content determined by 'goal'
    val HEAD = PicatElementType("HEAD")
    val GOAL = PicatElementType("GOAL") // A general type for goals

    // Control flow and specific goal types (some existing, some new)
    val IF_THEN_ELSE = PicatElementType("IF_THEN_ELSE")
    val FOREACH_LOOP = PicatElementType("FOREACH_LOOP")
    val WHILE_LOOP = PicatElementType("WHILE_LOOP")
    val TRY_CATCH = PicatElementType("TRY_CATCH")
    val CASE_EXPRESSION = PicatElementType("CASE_EXPRESSION")
    val CASE_ARMS = PicatElementType("CASE_ARMS")
    val CASE_ARM = PicatElementType("CASE_ARM")
    val CATCH_CLAUSES = PicatElementType("CATCH_CLAUSES")
    val CATCH_CLAUSE = PicatElementType("CATCH_CLAUSE")
    val ELSEIF_CLAUSE = PicatElementType("ELSEIF_CLAUSE")

    val PATTERN = PicatElementType("PATTERN")
    val STRUCTURE_PATTERN = PicatElementType("STRUCTURE_PATTERN")
    val LIST_PATTERN = PicatElementType("LIST_PATTERN")
    val TUPLE_PATTERN = PicatElementType("TUPLE_PATTERN")
    val PATTERN_LIST = PicatElementType("PATTERN_LIST")

    val FUNCTION_CALL = PicatElementType("FUNCTION_CALL")
    val ATOM_NO_ARGS = PicatElementType("ATOM_NO_ARGS")
    val QUALIFIED_ATOM = PicatElementType("QUALIFIED_ATOM") // Added for head parts

    // Element types for rules that were previously tokens or simple rules
    val RULE_OPERATOR = PicatElementType("RULE_OPERATOR")
    val UNARY_EXPRESSION = PicatElementType("UNARY_EXPRESSION")
    val EXPORT_CLAUSE = PicatElementType("EXPORT_CLAUSE")
    val IMPORT_CLAUSE = PicatElementType("IMPORT_CLAUSE")
    val IMPORT_LIST = PicatElementType("IMPORT_LIST")
    val EXPORT_LIST = PicatElementType("EXPORT_LIST")
    val RENAME_LIST = PicatElementType("RENAME_LIST")
    val FOREACH_GENERATORS = PicatElementType("FOREACH_GENERATORS")
    val FOREACH_GENERATOR = PicatElementType("FOREACH_GENERATOR")
    val IMPORT_ITEM = PicatElementType("IMPORT_ITEM") // Was MODULE_SPEC
    val FILE_SPEC = PicatElementType("FILE_SPEC") // Added
    val EXPORT_SPEC = PicatElementType("EXPORT_SPEC") // Added
    val RENAME_SPEC = PicatElementType("RENAME_SPEC") // Added
    val TUPLE_ITEMS = PicatElementType("TUPLE_ITEMS") // Added

    // Statement-like goals from BNF 'goal' rule
    val ASSIGNMENT = PicatElementType("ASSIGNMENT")
    val UNIFICATION = PicatElementType("UNIFICATION") // Added
    val COMPARISON = PicatElementType("COMPARISON") // Added
    val ARITHMETIC_COMPARISON = PicatElementType("ARITHMETIC_COMPARISON") // Added
    val NEGATION = PicatElementType("NEGATION")
    // FAIL, PASS, TRUE_GOAL, FALSE_GOAL, CUT, RETURN_STMT, CONTINUE_STMT, BREAK_STMT, THROW_STMT
    // are specific goals but might share a common "SimpleGoal" PSI or be distinct
    val FAIL_GOAL = PicatElementType("FAIL_GOAL")
    val PASS_GOAL = PicatElementType("PASS_GOAL")
    val TRUE_GOAL = PicatElementType("TRUE_GOAL")
    val FALSE_GOAL = PicatElementType("FALSE_GOAL")
    val CUT_GOAL = PicatElementType("CUT_GOAL") // was CUT_STATEMENT
    val RETURN_STMT = PicatElementType("RETURN_STMT")
    val CONTINUE_STMT = PicatElementType("CONTINUE_STMT")
    val BREAK_STMT = PicatElementType("BREAK_STMT")
    val THROW_STMT = PicatElementType("THROW_STMT")
    val PROCEDURE_CALL = PicatElementType("PROCEDURE_CALL") // Added (was 'call')
    val LIST_COMPREHENSION_GOAL = PicatElementType("LIST_COMPREHENSION_GOAL") // was LIST_COMPREHENSION

    // New PSI Element Types from recent BNF changes
    val PREDICATE_RULE = PicatElementType("PREDICATE_RULE")
    val PREDICATE_FACT = PicatElementType("PREDICATE_FACT")
    val FUNCTION_RULE = PicatElementType("FUNCTION_RULE")
    val FUNCTION_FACT = PicatElementType("FUNCTION_FACT")
    val ACTOR_DEFINITION = PicatElementType("ACTOR_DEFINITION")
    val ACTION_RULE = PicatElementType("ACTION_RULE")
    val LOOP_WHILE_STATEMENT = PicatElementType("LOOP_WHILE_STATEMENT")
    val DOLLAR_TERM_CONSTRUCTOR = PicatElementType("DOLLAR_TERM_CONSTRUCTOR")
    val INDEX_ACCESS_EXPRESSION = PicatElementType("INDEX_ACCESS_EXPRESSION")
    val AS_PATTERN_EXPRESSION = PicatElementType("AS_PATTERN_EXPRESSION")
    val LAMBDA_EXPRESSION = PicatElementType("LAMBDA_EXPRESSION")
    val VARIABLE_LIST = PicatElementType("VARIABLE_LIST")
    val TERM_CONSTRUCTOR_EXPRESSION = PicatElementType("TERM_CONSTRUCTOR_EXPRESSION")
    val LIST_COMPREHENSION_EXPRESSION = PicatElementType("LIST_COMPREHENSION_EXPRESSION")

    val COMPILATION_DIRECTIVE = PicatElementType("COMPILATION_DIRECTIVE")
    val TABLE_MODE = PicatElementType("TABLE_MODE")
    val INDEX_MODE = PicatElementType("INDEX_MODE")
    val HEAD_REFERENCE_LIST = PicatElementType("HEAD_REFERENCE_LIST")
    val HEAD_REFERENCE = PicatElementType("HEAD_REFERENCE")
    val INDEXING_DETAILS = PicatElementType("INDEXING_DETAILS")

    val ACTOR_NAME = PicatElementType("ACTOR_NAME") // For actor_name rule
    val ACTOR_MEMBER = PicatElementType("ACTOR_MEMBER")
    val PREDICATE_CLAUSE = PicatElementType("PREDICATE_CLAUSE")
    val FUNCTION_CLAUSE = PicatElementType("FUNCTION_CLAUSE")

    // Expression hierarchy levels (if they need distinct PSI types)
    // These are often not needed if they only serve for precedence in parser
    // val BICONDITIONAL_EXPRESSION_LEVEL = PicatElementType("BICONDITIONAL_EXPRESSION_LEVEL")
    // val IMPLICATION_EXPRESSION_LEVEL = PicatElementType("IMPLICATION_EXPRESSION_LEVEL")
    // val CONDITIONAL_EXPRESSION = PicatElementType("CONDITIONAL_EXPRESSION") // Already exists as CASE_EXPRESSION? No, ternary.
    // val LOGICAL_OR_EXPRESSION = PicatElementType("LOGICAL_OR_EXPRESSION")
    // val LOGICAL_AND_EXPRESSION = PicatElementType("LOGICAL_AND_EXPRESSION")
    // val BITWISE_OR_EXPRESSION = PicatElementType("BITWISE_OR_EXPRESSION")
    // val BITWISE_XOR_EXPRESSION = PicatElementType("BITWISE_XOR_EXPRESSION")
    // val BITWISE_AND_EXPRESSION = PicatElementType("BITWISE_AND_EXPRESSION")
    // val EQUALITY_EXPRESSION = PicatElementType("EQUALITY_EXPRESSION")
    // val RELATIONAL_EXPRESSION = PicatElementType("RELATIONAL_EXPRESSION")
    // val SHIFT_EXPRESSION = PicatElementType("SHIFT_EXPRESSION")
    // val ADDITIVE_EXPRESSION = PicatElementType("ADDITIVE_EXPRESSION")
    // val MULTIPLICATIVE_EXPRESSION = PicatElementType("MULTIPLICATIVE_EXPRESSION")
    // val POWER_EXPRESSION = PicatElementType("POWER_EXPRESSION")

    // Old element types from original file, to be pruned if truly replaced or unused:
    // val FUNCTION_DEFINITION = PicatElementType("FUNCTION_DEFINITION") // Replaced by FUNCTION_RULE / FUNCTION_FACT
    // val CLAUSE_LIST = PicatElementType("CLAUSE_LIST") // No longer a specific rule
    // val CLAUSE = PicatElementType("CLAUSE") // Replaced by PREDICATE_CLAUSE / FUNCTION_CLAUSE
    // val TERM = PicatElementType("TERM") // Concept, not specific PSI type for parsing usually
    // val LITERAL = PicatElementType("LITERAL") // Covered by atom, number, string, variable
    // val LIST = PicatElementType("LIST") // Now LIST_EXPRESSION
    // val EQUAL_EQUAL = PicatElementType("EQUAL_EQUAL") // Covered by IDENTICAL or EQUAL tokens
    // val IS = PicatElementType("IS") // IS_KEYWORD token
    // val RULE = PicatElementType("RULE") // Replaced by PREDICATE_RULE
    // val LIST_COMPREHENSION = PicatElementType("LIST_COMPREHENSION") // Replaced by LIST_COMPREHENSION_GOAL and LIST_COMPREHENSION_EXPRESSION
    // val THROW_STATEMENT = PicatElementType("THROW_STATEMENT") // Now THROW_STMT
    // val FACT = PicatElementType("FACT") // Replaced by PREDICATE_FACT
    // val RANGE_EXPRESSION = PicatElementType("RANGE_EXPRESSION") // This is a specific expression, covered by EXPRESSION
    // val DEFINITION = PicatElementType("DEFINITION") // Covered by item_ and its choices
    // val QUESTION = PicatTokenType("QUESTION") // Not a token, it's a literal in grammar rule.
    // val MODULE_SPEC = PicatElementType("MODULE_SPEC") // Renamed to IMPORT_ITEM
}
