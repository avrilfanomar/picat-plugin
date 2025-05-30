package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat definitions (facts, rules, predicates, functions).
 */
class PicatDefinitionParser : PicatBaseParser() {

    /**
     * Parses a Picat definition (fact, rule, predicate, or function).
     */
    fun parseDefinition(builder: PsiBuilder) {
        // Don't create a marker here, let the specific parse methods create their own markers

        // Check what kind of definition this is
        when {
            isFunctionDefinition(builder) -> {
                // Parse as a function definition (which is also marked as a fact)
                parseFunctionDefinition(builder)
            }
            isRuleDefinition(builder) -> {
                // Parse as a rule
                parseRuleDefinition(builder)
            }
            isPredicateDefinition(builder) -> {
                // Parse as a predicate definition
                parsePredicateDefinition(builder)
            }
            isFact(builder) -> {
                // Parse as a simple fact
                parseFact(builder)
            }
            else -> {
                // Error case
                val errorMarker = builder.mark()
                builder.error("Unexpected definition")
                builder.advanceLexer()
                errorMarker.drop()
            }
        }
    }


    /**
     * Parses a function definition.
     */
    fun parseFunctionDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        skipWhitespace(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.EQUAL, "Expected '=' in function definition")
        skipWhitespace(builder)

        expressionParser.parseExpression(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after function definition")
        // Mark as both a function definition and a fact
        val factMarker = marker.precede()
        marker.done(PicatTokenTypes.FUNCTION_DEFINITION)
        factMarker.done(PicatTokenTypes.FACT)
    }

    /**
     * Parses a rule definition.
     */
    fun parseRuleDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        // Parse rule operator (=>, ?=>, <=>, ?<=>, :-)
        val opMarker = builder.mark()
        if (PicatParserUtil.isRuleOperator(builder.tokenType)) {
            builder.advanceLexer()
            skipWhitespace(builder)
            opMarker.done(PicatTokenTypes.RULE_OPERATOR)
        } else {
            opMarker.drop()
            builder.error("Expected rule operator (=>, ?=>, <=>, ?<=>, :-)")
        }

        statementParser.parseBody(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after rule definition")
        marker.done(PicatTokenTypes.RULE)
    }

    /**
     * Parses a predicate definition.
     */
    fun parsePredicateDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)
        skipWhitespace(builder)

        // Check if this is a function definition (has an equals sign)
        val isFunction = builder.tokenType == PicatTokenTypes.EQUAL

        // Optional body
        if (builder.tokenType != PicatTokenTypes.DOT) {
            if (isFunction) {
                builder.advanceLexer() // Skip the equals sign
                skipWhitespace(builder)
                expressionParser.parseExpression(builder) // Parse the expression
            } else {
                skipWhitespace(builder)
                statementParser.parseBody(builder)
            }
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after predicate definition")

        // If this is a function definition, mark it as both a function definition and a fact
        if (isFunction) {
            val factMarker = marker.precede()
            marker.done(PicatTokenTypes.FUNCTION_DEFINITION)
            factMarker.done(PicatTokenTypes.FACT)
        } else {
            marker.done(PicatTokenTypes.PREDICATE_DEFINITION)
        }
    }

    /**
     * Parses a fact.
     */
    fun parseFact(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        // Optional function body
        if (builder.tokenType == PicatTokenTypes.EQUAL) {
            builder.advanceLexer()
            skipWhitespace(builder)
            expressionParser.parseExpression(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after fact")
        marker.done(PicatTokenTypes.FACT)
    }

    /**
     * Parses a predicate/function head.
     */
    fun parseHead(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            isStructure(builder) -> parseStructure(builder)
            isQualifiedAtom(builder) -> parseQualifiedAtom(builder)
            isAtom(builder.tokenType) -> parseAtom(builder)
            else -> {
                builder.error("Expected predicate/function head")
                builder.advanceLexer()
                marker.drop()
                return
            }
        }

        marker.done(PicatTokenTypes.HEAD)
    }

    /**
     * Checks if the current token sequence represents a function definition.
     */
    fun isFunctionDefinition(builder: PsiBuilder): Boolean {
        // Look ahead to see if this is a function definition
        val marker = builder.mark()

        parseHead(builder)
        skipWhitespace(builder)
        val result = builder.tokenType == PicatTokenTypes.EQUAL
        marker.rollbackTo()

        return result
    }

    /**
     * Checks if the current token sequence represents a rule definition.
     */
    fun isRuleDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        parseHead(builder)
        skipWhitespace(builder)
        val result = PicatParserUtil.isRuleOperator(builder.tokenType)
        marker.rollbackTo()

        return result
    }

    /**
     * Checks if the current token sequence represents a predicate definition.
     */
    fun isPredicateDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        parseHead(builder)
        skipWhitespace(builder)
        // A predicate definition has a body (not just a dot)
        // and doesn't have an equals sign (which would make it a function definition)
        val result = builder.tokenType != PicatTokenTypes.DOT && builder.tokenType != PicatTokenTypes.EQUAL
        marker.rollbackTo()

        return result
    }

    /**
     * Checks if the current token sequence represents a fact.
     */
    fun isFact(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        parseHead(builder)
        // A fact is either just a head followed by a dot,
        // or a head followed by an equals sign, an expression, and a dot
        if (builder.tokenType == PicatTokenTypes.DOT) {
            result = true
        } else if (builder.tokenType == PicatTokenTypes.EQUAL) {
            // This is a function definition, which is also a fact
            result = true
        }
        marker.rollbackTo()

        return result
    }
}
