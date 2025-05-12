package com.github.avrilfanomar.picatplugin.language

import com.intellij.psi.tree.TokenSet

/**
 * Token types for Picat language.
 * This file defines constants for all token types used in the Picat language.
 */
object PicatTokenTypes {
    // Keywords
    val IMPORT_KEYWORD = PicatTokenType("IMPORT_KEYWORD")
    val MODULE_KEYWORD = PicatTokenType("MODULE_KEYWORD")
    val INDEX_KEYWORD = PicatTokenType("INDEX_KEYWORD")
    val PRIVATE_KEYWORD = PicatTokenType("PRIVATE_KEYWORD")
    val PUBLIC_KEYWORD = PicatTokenType("PUBLIC_KEYWORD")
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

    // Operators
    val ASSIGN_OP = PicatTokenType("ASSIGN_OP") // =
    val ARROW_OP = PicatTokenType("ARROW_OP") // =>
    val PLUS = PicatTokenType("PLUS") // +
    val MINUS = PicatTokenType("MINUS") // -
    val MULTIPLY = PicatTokenType("MULTIPLY") // *
    val DIVIDE = PicatTokenType("DIVIDE") // /
    val MODULO = PicatTokenType("MODULO") // mod
    val EQUAL = PicatTokenType("EQUAL") // ==
    val NOT_EQUAL = PicatTokenType("NOT_EQUAL") // !=
    val LESS = PicatTokenType("LESS") // <
    val LESS_EQUAL = PicatTokenType("LESS_EQUAL") // <=
    val GREATER = PicatTokenType("GREATER") // >
    val GREATER_EQUAL = PicatTokenType("GREATER_EQUAL") // >=
    val AND = PicatTokenType("AND") // &&
    val OR = PicatTokenType("OR") // ||
    val NOT = PicatTokenType("NOT") // !

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
    val FLOAT = PicatTokenType("FLOAT")
    val STRING = PicatTokenType("STRING")
    val ATOM = PicatTokenType("ATOM")
    val VARIABLE = PicatTokenType("VARIABLE")

    // Comments
    val COMMENT = PicatTokenType("COMMENT")

    // Other
    val IDENTIFIER = PicatTokenType("IDENTIFIER")
    val WHITE_SPACE = PicatTokenType("WHITE_SPACE")
    val BAD_CHARACTER = PicatTokenType("BAD_CHARACTER")

    // Token sets
    val KEYWORDS = TokenSet.create(
        IMPORT_KEYWORD, MODULE_KEYWORD, INDEX_KEYWORD, PRIVATE_KEYWORD, PUBLIC_KEYWORD,
        END_KEYWORD, IF_KEYWORD, THEN_KEYWORD, ELSE_KEYWORD, ELSEIF_KEYWORD,
        WHILE_KEYWORD, DO_KEYWORD, FOREACH_KEYWORD, FOR_KEYWORD, RETURN_KEYWORD,
        THROW_KEYWORD, TRY_KEYWORD, CATCH_KEYWORD
    )

    val OPERATORS = TokenSet.create(
        ASSIGN_OP, ARROW_OP, PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
        EQUAL, NOT_EQUAL, LESS, LESS_EQUAL, GREATER, GREATER_EQUAL,
        AND, OR, NOT
    )

    val COMMENTS = TokenSet.create(COMMENT)
    val STRING_LITERALS = TokenSet.create(STRING)
    val NUMBERS = TokenSet.create(INTEGER, FLOAT)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)
}