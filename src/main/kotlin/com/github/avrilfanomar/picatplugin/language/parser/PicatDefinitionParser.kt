package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isRuleOperator
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat definitions (facts, rules, functions).
 */
class PicatDefinitionParser : PicatBaseParser() {

    /**
     * Parses a Picat definition (fact, rule, implicit rule, or function).
     */
    fun parseDefinition(builder: PsiBuilder) {
        // Don't create a marker here, let the specific parse methods create their own markers

        // Check what kind of definition this is
        val isFunctionDef = isFunctionDefinition(builder)
        val isRuleDef = isRuleDefinition(builder)
        val isImplicitRuleDef = isImplicitRule(builder)
        val isFactDef = isFact(builder)


        when {
            isFunctionDef -> {
                // Parse as a function definition (which is also marked as a fact)
                parseFunctionDefinition(builder)
            }

            isRuleDef -> {
                // Parse as a rule
                parseRuleDefinition(builder)
            }

            isImplicitRuleDef -> {
                // Parse as an implicit rule
                parseRuleDefinition(builder)
            }

            isFactDef -> {
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

        // Parse the head, which could be a structure, atom, or pattern
        if (isStructure(builder)) {
            parseStructure(builder)
        } else if (isQualifiedAtom(builder)) {
            parseQualifiedAtom(builder)
        } else if (builder.tokenType == PicatTokenTypes.LBRACKET) {
            // Handle pattern matching with lists
            expressionParser.parsePrimaryExpression(builder)
        } else if (isAtom(builder.tokenType)) {
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)
        } else {
            // Error case
            builder.error("Expected function head")
            builder.advanceLexer()
            marker.drop()
            return
        }

        skipWhitespace(builder)
        expectToken(builder, PicatTokenTypes.EQUAL, "Expected '=' in function definition")
        skipWhitespace(builder)

        // Mark the function body
        val bodyMarker = builder.mark()
        expressionParser.parseExpression(builder)
        bodyMarker.done(PicatTokenTypes.FUNCTION_BODY)

        skipWhitespace(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after function definition")
        marker.done(PicatTokenTypes.FUNCTION_DEFINITION)
    }

    /**
     * Parses a rule definition.
     * A rule must have an explicit rule operator (=>, ?=>, <=>, ?<=>, :-).
     * Rules can also have conditions, e.g., factorial(N) => N * factorial(N-1) => N > 0.
     */
    fun parseRuleDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)
        skipWhitespace(builder)

        // Parse rule operator (=>, ?=>, <=>, ?<=>, :-)
        val opMarker = builder.mark()
        val ruleOpType = builder.tokenType
        val isBiconditionalRule = ruleOpType == PicatTokenTypes.BICONDITIONAL_OP ||
                ruleOpType == PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP

        if (isRuleOperator(ruleOpType)) {
            builder.advanceLexer()
            opMarker.done(PicatTokenTypes.RULE_OPERATOR)
            skipWhitespace(builder)
        } else {
            opMarker.drop()
            builder.error("Expected rule operator (=>, ?=>, <=>, ?<=>, :-)")
        }

        // For biconditional rules, parse the entire body as a single expression
        if (isBiconditionalRule) {
            val bodyMarker = builder.mark()
            expressionParser.parseExpression(builder)
            bodyMarker.done(PicatTokenTypes.BODY)
        } else {
            // Parse the body using the statement parser's parseBody method
            statementParser.parseBody(builder)

            // Check for a condition (any rule operator)
            if (isRuleOperator(builder.tokenType)) {
                val condOpMarker = builder.mark()
                builder.advanceLexer()
                skipWhitespace(builder)
                condOpMarker.done(PicatTokenTypes.RULE_OPERATOR)

                // Parse the condition using the statement parser's parseBody method
                statementParser.parseBody(builder)
            }
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after rule definition")
        marker.done(PicatTokenTypes.RULE)
    }


    /**
     * Parses a fact.
     */
    fun parseFact(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after fact")
        marker.done(PicatTokenTypes.FACT)
    }

    /**
     * Parses a rule/function head.
     */
    fun parseHead(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            isStructure(builder) -> parseStructure(builder)
            isQualifiedAtom(builder) -> parseQualifiedAtom(builder)
            builder.tokenType == PicatTokenTypes.LBRACKET -> {
                // Handle pattern matching with lists
                val listMarker = builder.mark()
                builder.advanceLexer() // Consume [

                // Parse list items if the list is not empty
                if (builder.tokenType != PicatTokenTypes.RBRACKET) {
                    expressionParser.parseExpression(builder)

                    // Check for list tail
                    if (builder.tokenType == PicatTokenTypes.PIPE) {
                        builder.advanceLexer() // Consume |
                        skipWhitespace(builder)
                        expressionParser.parseExpression(builder)
                    }
                }

                // Expect closing bracket
                if (builder.tokenType == PicatTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected ']'")
                }

                listMarker.done(PicatTokenTypes.LIST)
            }

            isAtom(builder.tokenType) -> {
                val atomMarker = builder.mark()
                builder.advanceLexer()
                atomMarker.done(PicatTokenTypes.ATOM)
            }

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

        // Try to parse the head, which could be a structure, atom, or pattern
        if (isStructure(builder)) {
            parseStructure(builder)
        } else if (isQualifiedAtom(builder)) {
            parseQualifiedAtom(builder)
        } else if (builder.tokenType == PicatTokenTypes.LBRACKET) {
            // Handle pattern matching with lists
            expressionParser.parsePrimaryExpression(builder)
        } else if (isAtom(builder.tokenType)) {
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)
        } else {
            marker.rollbackTo()
            return false
        }

        skipWhitespace(builder)
        // A function definition must have an equals sign
        // This includes pattern matching functions like sum([]) = 0
        val result = builder.tokenType == PicatTokenTypes.EQUAL
        marker.rollbackTo()

        return result
    }

    /**
     * Checks if the current token sequence represents a rule definition.
     * A rule must have an explicit rule operator (=>, ?=>, <=>, ?<=>, :-).
     */
    fun isRuleDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        parseHead(builder)
        skipWhitespace(builder)
        // A rule definition must have an explicit rule operator
        val result = isRuleOperator(builder.tokenType)
        marker.rollbackTo()

        return result
    }


    /**
     * Checks if the current token sequence represents an implicit rule (what was previously a predicate).
     */
    fun isImplicitRule(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        parseHead(builder)
        skipWhitespace(builder)
        // An implicit rule has a body (not just a dot)
        // and doesn't have an equals sign (which would make it a function definition)
        // and doesn't have a rule operator (which would make it an explicit rule)
        val result = builder.tokenType != PicatTokenTypes.DOT &&
                builder.tokenType != PicatTokenTypes.EQUAL &&
                !isRuleOperator(builder.tokenType)
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
        skipWhitespace(builder)
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
