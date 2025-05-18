package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Token types for Picat language.
 * This is a placeholder implementation that will be replaced by the Grammar-Kit generated class.
 */
object PicatTypes {
    // File
    val FILE = IElementType("FILE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Whitespace and comments
    val WHITE_SPACE = IElementType("WHITE_SPACE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val COMMENT = IElementType("COMMENT", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Keywords
    val MODULE_KEYWORD = IElementType("MODULE_KEYWORD", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val IMPORT_KEYWORD = IElementType("IMPORT_KEYWORD", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Identifiers and literals
    val IDENTIFIER = IElementType("IDENTIFIER", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val VARIABLE = IElementType("VARIABLE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val INTEGER = IElementType("INTEGER", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val FLOAT = IElementType("FLOAT", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val STRING = IElementType("STRING", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val QUOTED_ATOM = IElementType("QUOTED_ATOM", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Punctuation
    val DOT = IElementType("DOT", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val COMMA = IElementType("COMMA", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val SEMICOLON = IElementType("SEMICOLON", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LPAR = IElementType("LPAR", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val RPAR = IElementType("RPAR", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LBRACKET = IElementType("LBRACKET", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val RBRACKET = IElementType("RBRACKET", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val PIPE = IElementType("PIPE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Operators
    val PLUS = IElementType("PLUS", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val MINUS = IElementType("MINUS", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val MULTIPLY = IElementType("MULTIPLY", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val DIVIDE = IElementType("DIVIDE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val EQUAL = IElementType("EQUAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val NOT_EQUAL = IElementType("NOT_EQUAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LESS = IElementType("LESS", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val GREATER = IElementType("GREATER", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LESS_EQUAL = IElementType("LESS_EQUAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val GREATER_EQUAL = IElementType("GREATER_EQUAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val EQUAL_EQUAL = IElementType("EQUAL_EQUAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val IDENTICAL = IElementType("IDENTICAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val NOT_IDENTICAL = IElementType("NOT_IDENTICAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val IS = IElementType("IS", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // PSI elements
    val MODULE_DECLARATION = IElementType("MODULE_DECLARATION", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val MODULE_NAME = IElementType("MODULE_NAME", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val IMPORT_STATEMENT = IElementType("IMPORT_STATEMENT", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val PREDICATE_DEFINITION = IElementType("PREDICATE_DEFINITION", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val PREDICATE_HEAD = IElementType("PREDICATE_HEAD", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val PREDICATE_BODY = IElementType("PREDICATE_BODY", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val FUNCTION_DEFINITION = IElementType("FUNCTION_DEFINITION", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val FUNCTION_HEAD = IElementType("FUNCTION_HEAD", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val FUNCTION_BODY = IElementType("FUNCTION_BODY", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val ARGUMENT_LIST = IElementType("ARGUMENT_LIST", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val ARGUMENT = IElementType("ARGUMENT", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val CLAUSE_LIST = IElementType("CLAUSE_LIST", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val CLAUSE = IElementType("CLAUSE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val EXPRESSION = IElementType("EXPRESSION", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val TERM = IElementType("TERM", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LITERAL = IElementType("LITERAL", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val ATOM = IElementType("ATOM", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val STRUCTURE = IElementType("STRUCTURE", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LIST = IElementType("LIST", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val LIST_ELEMENTS = IElementType("LIST_ELEMENTS", com.github.avrilfanomar.picatplugin.language.PicatLanguage)
    val OPERATOR = IElementType("OPERATOR", com.github.avrilfanomar.picatplugin.language.PicatLanguage)

    // Token sets
    val COMMENTS = TokenSet.create(COMMENT)
    val STRINGS = TokenSet.create(STRING, QUOTED_ATOM)
    val OPERATORS = TokenSet.create(
        PLUS, MINUS, MULTIPLY, DIVIDE, EQUAL, NOT_EQUAL, LESS, GREATER,
        LESS_EQUAL, GREATER_EQUAL, EQUAL_EQUAL, IDENTICAL, NOT_IDENTICAL, IS
    )

    /**
     * Factory for creating PSI elements.
     * This is a placeholder implementation that will be replaced by the Grammar-Kit generated class.
     */
    object Factory {
        fun createElement(node: com.intellij.lang.ASTNode): com.intellij.psi.PsiElement {
            // This is a simplified implementation that will be replaced by the Grammar-Kit generated code
            // For now, just return a generic PSI element
            return com.intellij.extapi.psi.ASTWrapperPsiElement(node)
        }
    }
}
