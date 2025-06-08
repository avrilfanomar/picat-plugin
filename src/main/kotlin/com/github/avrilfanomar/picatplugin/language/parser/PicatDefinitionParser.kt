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

    // Entry point for items that are predicate/function definitions
    fun parseDefinition(builder: PsiBuilder, level: Int = 0) {
        // Try to parse as function clause first due to common head structure.
        // Lookahead for ASSIGN_OP might be needed for robust distinction.
        if (isFunctionClause(builder, level + 1)) {
            parseFunctionClause(builder, level + 1)
        } else if (isPredicateClause(builder, level + 1)) { // isPredicateClause would check for RULE_OP or just EOR after head
            parsePredicateClause(builder, level + 1)
        } else {
            val errorMarker = builder.mark()
            builder.error("Expected predicate or function definition")
            // Consume one token to advance past the error, but be careful not to consume too much
            // This part of error recovery is tricky in manual parsers.
            if (!builder.eof()) builder.advanceLexer()
            errorMarker.drop()
        }
    }

    // PREDICATE_CLAUSE ::= predicate_rule | predicate_fact
    fun parsePredicateClause(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateClause")) return false
        val marker = builder.mark()
        var result: Boolean

        // Lookahead for RULE_OP to distinguish between rule and fact
        val headMarker = builder.mark()
        parseHead(builder, level + 1) // Assuming parseHead doesn't advance past the head
        headMarker.drop() // Head is part of rule/fact, not its own element here for clause logic

        if (isRuleOperator(builder.tokenType)) {
            result = parsePredicateRule(builder, level + 1)
        } else if (builder.tokenType == PicatTokenTypes.EOR || builder.eof()) { // Simple fact ends with EOR
            result = parsePredicateFact(builder, level + 1)
        } else {
            // Could be an implicit rule if body follows without RULE_OP, but BNF implies RULE_OP for rules.
            // For now, stick to explicit fact or rule.
            builder.error("Expected rule operator or end of fact for predicate clause")
            marker.rollbackTo() // Rollback the clause marker
            return false
        }

        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_CLAUSE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FUNCTION_CLAUSE ::= function_rule | function_fact
    fun parseFunctionClause(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionClause")) return false
        val marker = builder.mark()
        var result: Boolean

        // Lookahead logic: both start with head then ASSIGN_OP.
        // The distinction is if function_body (which is an expression) is complex enough or if it's a simple expression.
        // For now, we might not distinguish deeply here, but rely on how parseFunctionRule/Fact are called.
        // Let's assume we try parseFunctionRule which might be more complex.
        // A better way is to parse head, then ASSIGN_OP, then try function_body, then expression.
        // For simplicity now, let's assume the caller (parseDefinition) makes a reasonable guess or we try both.

        // This simplified logic assumes parseFunctionRule and parseFunctionFact can correctly rollback
        // or be distinguished by more subtle lookahead.
        if (parseFunctionRule(builder, level + 1)) { // Try rule first
            result = true
        } else if (parseFunctionFact(builder, level + 1)) { // Then fact
            result = true
        } else {
            builder.error("Expected function rule or fact")
            marker.rollbackTo()
            return false
        }

        if (result) {
            marker.done(PicatTokenTypes.FUNCTION_CLAUSE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // PREDICATE_RULE ::= head RULE_OP body EOR
    fun parsePredicateRule(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateRule")) return false

        val marker = builder.mark()
        var result = parseHead(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.RULE_OP, "Expected rule operator (e.g., :-, =>)")
        result = result && statementParser.parseBody(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after predicate rule body")

        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // PREDICATE_FACT ::= head EOR
    fun parsePredicateFact(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateFact")) return false

        val marker = builder.mark()
        var result = parseHead(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after predicate fact")

        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_FACT)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FUNCTION_RULE ::= head ASSIGN_OP function_body EOR
    fun parseFunctionRule(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionRule")) return false

        val marker = builder.mark()
        var result = parseHead(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':=' in function definition")

        val bodyMarker = builder.mark()
        result = result && expressionParser.parseExpression(builder, level + 1) // function_body is an expression
        if(result) bodyMarker.done(PicatTokenTypes.FUNCTION_BODY) else bodyMarker.drop()

        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after function rule body")

        if (result) {
            marker.done(PicatTokenTypes.FUNCTION_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FUNCTION_FACT ::= head ASSIGN_OP expression EOR
    fun parseFunctionFact(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionFact")) return false

        val marker = builder.mark()
        var result = parseHead(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':=' in function fact")
        result = result && expressionParser.parseExpression(builder, level + 1)
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after function fact expression")

        if (result) {
            marker.done(PicatTokenTypes.FUNCTION_FACT)
        } else {
            marker.rollbackTo()
        }
        return result
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
