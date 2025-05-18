package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTypes
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
        if (builder.tokenType == PicatTypes.WHITE_SPACE) {
            builder.advanceLexer()
            return
        }
        
        // Parse comments
        if (builder.tokenType == PicatTypes.COMMENT) {
            val marker = builder.mark()
            builder.advanceLexer()
            marker.done(PicatTypes.COMMENT)
            return
        }
        
        // Parse module declarations
        if (builder.tokenType == PicatTypes.MODULE_KEYWORD) {
            parseModuleDeclaration(builder)
            return
        }
        
        // Parse import statements
        if (builder.tokenType == PicatTypes.IMPORT_KEYWORD) {
            parseImportStatement(builder)
            return
        }
        
        // Parse predicate or function definitions
        if (builder.tokenType == PicatTypes.IDENTIFIER) {
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
        if (builder.tokenType == PicatTypes.IDENTIFIER) {
            val nameMarker = builder.mark()
            builder.advanceLexer()
            nameMarker.done(PicatTypes.MODULE_NAME)
        } else {
            builder.error("Expected module name")
        }
        
        // Consume "."
        if (builder.tokenType == PicatTypes.DOT) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '.'")
        }
        
        marker.done(PicatTypes.MODULE_DECLARATION)
    }
    
    /**
     * Parse an import statement.
     */
    private fun parseImportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Consume "import" keyword
        builder.advanceLexer()
        
        // Parse module name
        if (builder.tokenType == PicatTypes.IDENTIFIER) {
            val nameMarker = builder.mark()
            builder.advanceLexer()
            nameMarker.done(PicatTypes.MODULE_NAME)
        } else {
            builder.error("Expected module name")
        }
        
        // Consume "."
        if (builder.tokenType == PicatTypes.DOT) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '.'")
        }
        
        marker.done(PicatTypes.IMPORT_STATEMENT)
    }
    
    /**
     * Parse a predicate or function definition.
     */
    private fun parsePredicateOrFunction(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Parse identifier
        builder.advanceLexer()
        
        // Parse arguments
        if (builder.tokenType == PicatTypes.LPAR) {
            parseArgumentList(builder)
        }
        
        // Check if it's a function (has "=")
        val isFunction = builder.tokenType == PicatTypes.EQUAL
        if (isFunction) {
            builder.advanceLexer()
            
            // Parse function body
            val bodyMarker = builder.mark()
            parseExpression(builder)
            bodyMarker.done(PicatTypes.FUNCTION_BODY)
        } else {
            // Parse predicate body
            val bodyMarker = builder.mark()
            parseClauseList(builder)
            bodyMarker.done(PicatTypes.PREDICATE_BODY)
        }
        
        // Consume "."
        if (builder.tokenType == PicatTypes.DOT) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '.'")
        }
        
        if (isFunction) {
            marker.done(PicatTypes.FUNCTION_DEFINITION)
        } else {
            marker.done(PicatTypes.PREDICATE_DEFINITION)
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
        while (builder.tokenType != PicatTypes.RPAR && !builder.eof()) {
            parseArgument(builder)
            
            // Consume "," if present
            if (builder.tokenType == PicatTypes.COMMA) {
                builder.advanceLexer()
            } else {
                break
            }
        }
        
        // Consume ")"
        if (builder.tokenType == PicatTypes.RPAR) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ')'")
        }
        
        marker.done(PicatTypes.ARGUMENT_LIST)
    }
    
    /**
     * Parse an argument.
     */
    private fun parseArgument(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExpression(builder)
        marker.done(PicatTypes.ARGUMENT)
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
            if (builder.tokenType == PicatTypes.COMMA || builder.tokenType == PicatTypes.SEMICOLON) {
                builder.advanceLexer()
            } else {
                break
            }
        }
        
        marker.done(PicatTypes.CLAUSE_LIST)
    }
    
    /**
     * Parse a clause.
     */
    private fun parseClause(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExpression(builder)
        marker.done(PicatTypes.CLAUSE)
    }
    
    /**
     * Parse an expression.
     */
    private fun parseExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Parse first term
        parseTerm(builder)
        
        // Parse operator and second term if present
        while (isOperator(builder.tokenType)) {
            val opMarker = builder.mark()
            builder.advanceLexer()
            opMarker.done(PicatTypes.OPERATOR)
            
            parseTerm(builder)
        }
        
        marker.done(PicatTypes.EXPRESSION)
    }
    
    /**
     * Parse a term.
     */
    private fun parseTerm(builder: PsiBuilder) {
        val marker = builder.mark()
        
        when (builder.tokenType) {
            PicatTypes.INTEGER, PicatTypes.FLOAT, PicatTypes.STRING, PicatTypes.IDENTIFIER, PicatTypes.QUOTED_ATOM -> {
                parseLiteral(builder)
            }
            PicatTypes.VARIABLE -> {
                parseVariable(builder)
            }
            PicatTypes.LBRACKET -> {
                parseList(builder)
            }
            PicatTypes.LPAR -> {
                // Consume "("
                builder.advanceLexer()
                
                // Parse expression
                parseExpression(builder)
                
                // Consume ")"
                if (builder.tokenType == PicatTypes.RPAR) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected ')'")
                }
            }
            else -> {
                builder.error("Expected term")
                builder.advanceLexer()
            }
        }
        
        marker.done(PicatTypes.TERM)
    }
    
    /**
     * Parse a literal.
     */
    private fun parseLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        
        when (builder.tokenType) {
            PicatTypes.INTEGER, PicatTypes.FLOAT, PicatTypes.STRING -> {
                builder.advanceLexer()
            }
            PicatTypes.IDENTIFIER, PicatTypes.QUOTED_ATOM -> {
                parseAtom(builder)
            }
            else -> {
                builder.error("Expected literal")
                builder.advanceLexer()
            }
        }
        
        marker.done(PicatTypes.LITERAL)
    }
    
    /**
     * Parse an atom.
     */
    private fun parseAtom(builder: PsiBuilder) {
        val marker = builder.mark()
        
        when (builder.tokenType) {
            PicatTypes.IDENTIFIER -> {
                val idMarker = builder.mark()
                builder.advanceLexer()
                idMarker.done(PicatTypes.IDENTIFIER)
                
                // Check if it's a structure
                if (builder.tokenType == PicatTypes.LPAR) {
                    marker.drop() // Drop the atom marker
                    val structMarker = builder.mark()
                    
                    // Parse argument list
                    parseArgumentList(builder)
                    
                    structMarker.done(PicatTypes.STRUCTURE)
                    return
                }
            }
            PicatTypes.QUOTED_ATOM -> {
                builder.advanceLexer()
            }
            else -> {
                builder.error("Expected atom")
                builder.advanceLexer()
            }
        }
        
        marker.done(PicatTypes.ATOM)
    }
    
    /**
     * Parse a variable.
     */
    private fun parseVariable(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(PicatTypes.VARIABLE)
    }
    
    /**
     * Parse a list.
     */
    private fun parseList(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Consume "["
        builder.advanceLexer()
        
        // Parse list elements if present
        if (builder.tokenType != PicatTypes.RBRACKET) {
            parseListElements(builder)
        }
        
        // Consume "]"
        if (builder.tokenType == PicatTypes.RBRACKET) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ']'")
        }
        
        marker.done(PicatTypes.LIST)
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
            if (builder.tokenType == PicatTypes.COMMA && 
                builder.lookAhead(1) == PicatTypes.PIPE && 
                builder.lookAhead(2) == PicatTypes.COMMA) {
                
                // Consume ",", "|", ","
                builder.advanceLexer()
                builder.advanceLexer()
                builder.advanceLexer()
                
                // Parse tail expression
                parseExpression(builder)
                break
            }
            
            // Consume "," if present
            if (builder.tokenType == PicatTypes.COMMA) {
                builder.advanceLexer()
            } else {
                break
            }
        }
        
        marker.done(PicatTypes.LIST_ELEMENTS)
    }
    
    /**
     * Check if the token type is an operator.
     */
    private fun isOperator(tokenType: IElementType?): Boolean {
        return tokenType == PicatTypes.PLUS ||
               tokenType == PicatTypes.MINUS ||
               tokenType == PicatTypes.MULTIPLY ||
               tokenType == PicatTypes.DIVIDE ||
               tokenType == PicatTypes.EQUAL ||
               tokenType == PicatTypes.NOT_EQUAL ||
               tokenType == PicatTypes.LESS ||
               tokenType == PicatTypes.GREATER ||
               tokenType == PicatTypes.LESS_EQUAL ||
               tokenType == PicatTypes.GREATER_EQUAL ||
               tokenType == PicatTypes.EQUAL_EQUAL ||
               tokenType == PicatTypes.NOT_EQUAL ||
               tokenType == PicatTypes.IDENTICAL ||
               tokenType == PicatTypes.NOT_IDENTICAL ||
               tokenType == PicatTypes.IS
    }
}