package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

/**
 * Parser for Picat language.
 * This is a placeholder implementation that will be replaced by the Grammar-Kit generated parser.
 */
class PicatParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        // Parse the file
        while (!builder.eof()) {
            parseItem(builder)
        }

        rootMarker.done(root)
        return builder.treeBuilt
    }

    /**
     * Parse an item in the file (predicate, function, module declaration, import statement, or comment).
     */
    private fun parseItem(builder: PsiBuilder) {
        // Skip whitespace
        if (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
            return
        }

        // Parse comments
        if (builder.tokenType == PicatTokenTypes.COMMENT) {
            parseSimpleToken(builder, PicatTokenTypes.COMMENT)
            return
        }

        // Parse module declarations
        if (builder.tokenType == PicatTokenTypes.MODULE_KEYWORD) {
            parseModuleDeclaration(builder)
            return
        }

        // Parse import statements
        if (builder.tokenType == PicatTokenTypes.IMPORT_KEYWORD) {
            parseImportStatement(builder)
            return
        }

        // Parse export statements
        if (builder.tokenType == PicatTokenTypes.EXPORT_KEYWORD) {
            parseExportStatement(builder)
            return
        }

        // Parse include statements
        if (builder.tokenType == PicatTokenTypes.INCLUDE_KEYWORD) {
            parseIncludeStatement(builder)
            return
        }

        // Parse predicate or function definitions
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            parsePredicateOrFunction(builder)
            return
        }

        // Skip unknown tokens
        builder.advanceLexer()
    }

    /**
     * Parse a module declaration.
     */
    private fun parseModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "module" keyword
        builder.advanceLexer()

        // Parse module name
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            parseSimpleToken(builder, PicatTokenTypes.MODULE_NAME)
        } else {
            builder.error("Expected module name")
        }

        // Consume "."
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

        marker.done(PicatTokenTypes.MODULE_DECLARATION)
    }

    /**
     * Parse an import statement.
     */
    private fun parseImportStatement(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "import" keyword
        builder.advanceLexer()

        // Parse module name
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            parseSimpleToken(builder, PicatTokenTypes.MODULE_NAME)
        } else {
            builder.error("Expected module name")
        }

        // Consume "."
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

        marker.done(PicatTokenTypes.IMPORT_STATEMENT)
    }

    /**
     * Parse an export statement.
     */
    private fun parseExportStatement(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "export" keyword
        builder.advanceLexer()

        // Parse predicate indicators (name/arity pairs)
        parsePredicateIndicatorList(builder)

        // Consume "."
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

        marker.done(PicatTokenTypes.EXPORT_STATEMENT)
    }

    /**
     * Parse a list of predicate indicators (name/arity pairs).
     */
    private fun parsePredicateIndicatorList(builder: PsiBuilder) {
        // Parse first predicate indicator
        parsePredicateIndicator(builder)

        // Parse additional predicate indicators separated by commas
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer() // Consume ","
            parsePredicateIndicator(builder)
        }
    }

    /**
     * Parse a predicate indicator (name/arity pair).
     */
    private fun parsePredicateIndicator(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse predicate name
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER || builder.tokenType == PicatTokenTypes.QUOTED_ATOM) {
            parseSimpleToken(builder, PicatTokenTypes.ATOM)
        } else {
            builder.error("Expected predicate name")
        }

        // Consume "/"
        expectToken(builder, PicatTokenTypes.DIVIDE, "Expected '/'")

        // Parse arity
        if (builder.tokenType == PicatTokenTypes.INTEGER) {
            builder.advanceLexer()
        } else {
            builder.error("Expected integer arity")
        }

        marker.done(PicatTokenTypes.PREDICATE_INDICATOR)
    }

    /**
     * Parse an include statement.
     */
    private fun parseIncludeStatement(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "include" keyword
        builder.advanceLexer()

        // Parse file path
        if (builder.tokenType == PicatTokenTypes.STRING) {
            builder.advanceLexer()
        } else {
            builder.error("Expected string literal for file path")
        }

        // Consume "."
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

        marker.done(PicatTokenTypes.INCLUDE_STATEMENT)
    }

    /**
     * Parse a predicate, function definition, or fact.
     */
    private fun parsePredicateOrFunction(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse head
        val headMarker = builder.mark()

        // Parse identifier
        builder.advanceLexer()

        // Parse arguments
        if (builder.tokenType == PicatTokenTypes.LPAR) {
            parseArgumentList(builder)
        }

        headMarker.done(PicatTokenTypes.HEAD)

        // Check if it's a function (has "=")
        val isFunction = builder.tokenType == PicatTokenTypes.EQUAL

        // Check if it's a fact (has ".")
        val isFact = builder.tokenType == PicatTokenTypes.DOT

        if (isFunction) {
            builder.advanceLexer()

            // Parse function body
            val bodyMarker = builder.mark()
            parseExpression(builder)
            bodyMarker.done(PicatTokenTypes.FUNCTION_BODY)

            // Check if it's a rule (has "=>", "?=>", etc.)
            val isRule = builder.tokenType == PicatTokenTypes.ARROW_OP ||
                    builder.tokenType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP

            if (isRule) {
                // Parse rule operator
                val opMarker = builder.mark()
                builder.advanceLexer()
                opMarker.done(PicatTokenTypes.OPERATOR)

                // Parse rule body
                val ruleBodyMarker = builder.mark()
                parseClauseList(builder)
                ruleBodyMarker.done(PicatTokenTypes.BODY)
            }

            // Consume "."
            expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

            if (isRule) {
                marker.done(PicatTokenTypes.RULE)
            } else {
                marker.done(PicatTokenTypes.FACT)
            }
        } else if (isFact) {
            // Consume "."
            expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

            marker.done(PicatTokenTypes.FACT)
        } else {
            // Parse predicate body
            val bodyMarker = builder.mark()
            parseClauseList(builder)
            bodyMarker.done(PicatTokenTypes.PREDICATE_BODY)

            // Consume "."
            expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")

            marker.done(PicatTokenTypes.PREDICATE_DEFINITION)
        }
    }

    /**
     * Parse an argument list.
     */
    private fun parseArgumentList(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "("
        builder.advanceLexer()

        // Parse arguments
        while (builder.tokenType != PicatTokenTypes.RPAR && !builder.eof()) {
            parseArgument(builder)

            // Consume "," if present
            if (builder.tokenType == PicatTokenTypes.COMMA) {
                builder.advanceLexer()
            } else {
                break
            }
        }

        // Consume ")"
        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")

        marker.done(PicatTokenTypes.ARGUMENT_LIST)
    }

    /**
     * Parse an argument.
     */
    private fun parseArgument(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExpression(builder)
        marker.done(PicatTokenTypes.ARGUMENT)
    }

    /**
     * Parse a clause list.
     */
    private fun parseClauseList(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse clauses
        while (!builder.eof()) {
            parseClause(builder)

            // Consume "," or ";" if present
            if (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
                builder.advanceLexer()
            } else {
                break
            }
        }

        marker.done(PicatTokenTypes.CLAUSE_LIST)
    }

    /**
     * Parse a clause.
     */
    private fun parseClause(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExpression(builder)
        marker.done(PicatTokenTypes.CLAUSE)
    }

    /**
     * Parse an expression.
     */
    private fun parseExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse first term
        parseTerm(builder)

        // Check if this is a rule definition (has => or ?=> operator)
        if (builder.tokenType == PicatTokenTypes.ARROW_OP || builder.tokenType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP) {
            val opMarker = builder.mark()
            builder.advanceLexer()
            opMarker.done(PicatTokenTypes.OPERATOR)

            // Parse rule body
            val bodyMarker = builder.mark()
            parseClauseList(builder)
            bodyMarker.done(PicatTokenTypes.RULE_BODY)

            marker.done(PicatTokenTypes.RULE)
            return
        }

        // Parse operator and second term if present
        while (isOperator(builder.tokenType)) {
            val opMarker = builder.mark()
            builder.advanceLexer()
            opMarker.done(PicatTokenTypes.OPERATOR)

            parseTerm(builder)
        }

        marker.done(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Parse a term.
     */
    private fun parseTerm(builder: PsiBuilder) {
        val marker = builder.mark()

        when (builder.tokenType) {
            PicatTokenTypes.INTEGER, PicatTokenTypes.FLOAT, PicatTokenTypes.STRING, PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM -> {
                parseLiteral(builder)
            }

            PicatTokenTypes.VARIABLE -> {
                parseVariable(builder)
            }

            PicatTokenTypes.LBRACKET -> {
                parseList(builder)
            }

            PicatTokenTypes.LPAR -> {
                // Consume "("
                builder.advanceLexer()

                // Parse expression
                parseExpression(builder)

                // Consume ")"
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
            }

            else -> {
                builder.error("Expected term")
                builder.advanceLexer()
            }
        }

        marker.done(PicatTokenTypes.TERM)
    }

    /**
     * Parse a literal.
     */
    private fun parseLiteral(builder: PsiBuilder) {
        val marker = builder.mark()

        when (builder.tokenType) {
            PicatTokenTypes.INTEGER, PicatTokenTypes.FLOAT, PicatTokenTypes.STRING -> {
                builder.advanceLexer()
            }

            PicatTokenTypes.IDENTIFIER, PicatTokenTypes.QUOTED_ATOM -> {
                parseAtom(builder)
            }

            else -> {
                builder.error("Expected literal")
                builder.advanceLexer()
            }
        }

        marker.done(PicatTokenTypes.LITERAL)
    }

    /**
     * Parse an atom.
     */
    private fun parseAtom(builder: PsiBuilder) {
        val marker = builder.mark()

        when (builder.tokenType) {
            PicatTokenTypes.IDENTIFIER -> {
                val idMarker = builder.mark()
                builder.advanceLexer()
                idMarker.done(PicatTokenTypes.IDENTIFIER)

                // Check if it's a structure
                if (builder.tokenType == PicatTokenTypes.LPAR) {
                    marker.drop() // Drop the atom marker
                    val structMarker = builder.mark()

                    // Parse argument list
                    parseArgumentList(builder)

                    structMarker.done(PicatTokenTypes.STRUCTURE)
                    return
                }
            }

            PicatTokenTypes.QUOTED_ATOM -> {
                builder.advanceLexer()
            }

            else -> {
                builder.error("Expected atom")
                builder.advanceLexer()
            }
        }

        marker.done(PicatTokenTypes.ATOM)
    }

    /**
     * Parse a variable.
     */
    private fun parseVariable(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(PicatTokenTypes.VARIABLE)
    }

    /**
     * Parse a list.
     */
    private fun parseList(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume "["
        builder.advanceLexer()

        // Parse list elements if present
        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parseListElements(builder)
        }

        // Consume "]"
        if (builder.tokenType == PicatTokenTypes.RBRACKET) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ']'")
        }

        marker.done(PicatTokenTypes.LIST)
    }

    /**
     * Parse list elements.
     */
    private fun parseListElements(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse expressions
        while (!builder.eof()) {
            parseExpression(builder)

            // Check for tail
            if (builder.tokenType == PicatTokenTypes.COMMA &&
                builder.lookAhead(1) == PicatTokenTypes.PIPE &&
                builder.lookAhead(2) == PicatTokenTypes.COMMA
            ) {

                // Consume ",", "|", ","
                builder.advanceLexer()
                builder.advanceLexer()
                builder.advanceLexer()

                // Parse tail expression
                parseExpression(builder)
                break
            }

            // Consume "," if present
            if (builder.tokenType == PicatTokenTypes.COMMA) {
                builder.advanceLexer()
            } else {
                break
            }
        }

        marker.done(PicatTokenTypes.LIST_ELEMENTS)
    }

    /**
     * Check if the token type is an operator.
     */
    private fun isOperator(tokenType: IElementType?): Boolean {
        return tokenType == PicatTokenTypes.PLUS ||
                tokenType == PicatTokenTypes.MINUS ||
                tokenType == PicatTokenTypes.MULTIPLY ||
                tokenType == PicatTokenTypes.DIVIDE ||
                tokenType == PicatTokenTypes.EQUAL ||
                tokenType == PicatTokenTypes.NOT_EQUAL ||
                tokenType == PicatTokenTypes.LESS ||
                tokenType == PicatTokenTypes.GREATER ||
                tokenType == PicatTokenTypes.LESS_EQUAL ||
                tokenType == PicatTokenTypes.GREATER_EQUAL ||
                tokenType == PicatTokenTypes.EQUAL_EQUAL ||
                tokenType == PicatTokenTypes.NOT_EQUAL ||
                tokenType == PicatTokenTypes.IDENTICAL ||
                tokenType == PicatTokenTypes.NOT_IDENTICAL ||
                tokenType == PicatTokenTypes.IS ||
                tokenType == PicatTokenTypes.ARROW_OP ||
                tokenType == PicatTokenTypes.BACKTRACKABLE_ARROW_OP
    }

    /**
     * Consume an expected token with error handling.
     * @param builder The PsiBuilder instance
     * @param expectedType The expected token type
     * @param errorMessage The error message to display if the token is not found
     * @return True if the token was consumed, false otherwise
     */
    private fun expectToken(builder: PsiBuilder, expectedType: IElementType, errorMessage: String): Boolean {
        if (builder.tokenType == expectedType) {
            builder.advanceLexer()
            return true
        } else {
            builder.error(errorMessage)
            return false
        }
    }

    /**
     * Parse a simple element with a marker.
     * @param builder The PsiBuilder instance
     * @param elementType The element type to mark the parsed element with
     * @param parseFunction The function to call to parse the element content
     */
    private fun parseMarkedElement(builder: PsiBuilder, elementType: IElementType, parseFunction: (PsiBuilder) -> Unit) {
        val marker = builder.mark()
        parseFunction(builder)
        marker.done(elementType)
    }

    /**
     * Parse a simple token with a marker.
     * @param builder The PsiBuilder instance
     * @param elementType The element type to mark the parsed element with
     */
    private fun parseSimpleToken(builder: PsiBuilder, elementType: IElementType) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(elementType)
    }
}
