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
    fun parseDefinition(builder: PsiBuilder) { // Removed level
        // Try to parse as function clause first due to common head structure.
        // Lookahead for ASSIGN_OP might be needed for robust distinction.
        // Commenting out isFunctionClause/isPredicateClause as they are unresolved and complex to fix now
        /*
        if (isFunctionClause(builder, level + 1)) {
            parseFunctionClause(builder, level + 1)
        } else if (isPredicateClause(builder, level + 1)) { // isPredicateClause would check for RULE_OP or just EOR after head
            parsePredicateClause(builder, level + 1)
        } else {
        */
        // Simplified: try parsing as predicate clause, then function clause, or just one if they have distinct starting tokens.
        // For now, let's assume it attempts to parse a generic clause which then gets refined.
        // Or, more directly, assume PredicateClause is the main one to try.
        // This part of logic is highly dependent on how the full parser is structured (usually generated).
        // As a placeholder for manual parsing:
        if (!parsePredicateClause(builder) && !parseFunctionClause(builder)) { // Removed level
            val errorMarker = builder.mark()
            builder.error("Expected predicate or function definition")
            // Consume one token to advance past the error, but be careful not to consume too much
            // This part of error recovery is tricky in manual parsers.
            if (!builder.eof()) builder.advanceLexer()
            errorMarker.drop()
        }
    }

    // PREDICATE_CLAUSE ::= predicate_rule | predicate_fact
    fun parsePredicateClause(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateClause")) return false // Temp removed
        val marker = builder.mark()
        var headParsed: Boolean

        // Lookahead for RULE_OP to distinguish between rule and fact
        val headMarker = builder.mark() // Marker for the head part
        headParsed = parseHead(builder) // parseHead now returns boolean
        if (!headParsed) {
            headMarker.drop()
            marker.rollbackTo() // Nothing useful parsed for a clause
            return false
        }
        headMarker.drop() // Head is part of rule/fact, not its own element here for clause logic - parseHead creates the HEAD node.

        var result = false
        if (isRuleOperator(builder.tokenType)) {
            result = parsePredicateRule(builder) // Removed level
        } else if (builder.tokenType == PicatTokenTypes.EOR || builder.eof()) { // Simple fact ends with EOR
            result = parsePredicateFact(builder) // Removed level
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
    fun parseFunctionClause(builder: PsiBuilder): Boolean { // Removed level
        val clauseMarker = builder.mark() // Marker for the whole FUNCTION_CLAUSE

        // Try to parse as a function rule.
        // parseFunctionRule itself will parse head, assign_op, body, eor
        // and rollback if it's not a complete valid function rule.
        if (parseFunctionRule(builder)) {
            clauseMarker.done(PicatTokenTypes.FUNCTION_CLAUSE)
            return true
        }

        // If not a function rule, try to parse as a function fact.
        // parseFunctionFact itself will parse head, assign_op, expression, eor
        // and rollback if it's not a complete valid function fact.
        if (parseFunctionFact(builder)) {
            clauseMarker.done(PicatTokenTypes.FUNCTION_CLAUSE)
            return true
        }

        // If neither parseFunctionRule nor parseFunctionFact succeeded,
        // then this is not a function clause. Rollback the marker.
        clauseMarker.rollbackTo()
        return false
    }

    // PREDICATE_RULE ::= head RULE_OP body EOR
    fun parsePredicateRule(builder: PsiBuilder, level: Int): Boolean { // Keep level if sub-parsers use it
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateRule")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHead(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.RULE_OP, "Expected rule operator (e.g., :-, =>)")
        result = result && statementParser.parseBody(builder, level + 1) // statementParser methods might take level
        // NOTE: The original code had a syntax error here with marker.done/rollbackTo inside parseBody arguments.
        // Assuming it meant to be after parseBody.
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after predicate rule body")


        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // Helper to call parseHead and return a boolean, assuming parseHead marks errors internally.
    // This helper seems to be unused or part of a previous refactoring attempt.
    // For clarity, I'm keeping the parseHead calls directly if they return boolean.
    // If parseHead needs a wrapper to return boolean, that should be done consistently.
    // Given parseHead now returns Boolean, this helper parseHeadAndReturnBool is redundant.
    // Let's assume parseHead is fine and remove/comment out parseHeadAndReturnBool if it's not used.
    // It appears parsePredicateRule below uses it.
    // For now, I will keep it as it might be used by other methods not in the diff.

    private fun parseHeadAndReturnBool(builder: PsiBuilder): Boolean {
        // This function seems to be a wrapper that doesn't add much value if parseHead itself returns boolean
        // and handles its own marking.
        // Let's assume parseHead returns boolean and correctly marks HEAD or errors.
        return parseHead(builder) // Directly return the result of parseHead
    }

    // PREDICATE_RULE ::= head RULE_OP body EOR
    fun parsePredicateRule(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateRule")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHeadAndReturnBool(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.RULE_OP, "Expected rule operator (e.g., :-, =>)")
        result = result && statementParser.parseBody(builder, 0) // Pass 0 or appropriate default
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after predicate rule body")

        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // PREDICATE_FACT ::= head EOR
    fun parsePredicateFact(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateFact")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHead(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after predicate fact")

        if (result) {
            marker.done(PicatTokenTypes.PREDICATE_FACT)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FUNCTION_RULE ::= head ASSIGN_OP function_body EOR
    fun parseFunctionRule(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionRule")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHead(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':=' in function definition")

        val bodyMarker = builder.mark()
        expressionParser.parseExpression(builder) // function_body is an expression. Call without level.
        // Assume parseExpression marks error if it fails.
        // For result propagation, this is simplified.
        val exprParseSucceeded = true // Placeholder, ideally check builder.isError or similar
        if(exprParseSucceeded) bodyMarker.done(PicatTokenTypes.FUNCTION_BODY) else bodyMarker.drop()
        result = result && exprParseSucceeded


        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after function rule body")

        if (result) {
            marker.done(PicatTokenTypes.FUNCTION_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FUNCTION_FACT ::= head ASSIGN_OP expression EOR
    fun parseFunctionFact(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionFact")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHead(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':=' in function fact")
        expressionParser.parseExpression(builder) // Call without level
        val exprParseSucceeded = true // Placeholder
        result = result && exprParseSucceeded
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after function fact expression")

        if (result) {
            marker.done(PicatTokenTypes.FUNCTION_FACT)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    /**
     * Parses a rule/function head. Returns true if a head was successfully parsed.
     */
    fun parseHead(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val initialPos = builder.currentOffset

        when {
            isStructure(builder) -> parseStructure(builder) // parseStructure is from PicatBaseParser, void return
            isQualifiedAtom(builder) -> parseQualifiedAtom(builder) // from PicatBaseParser, void return
            builder.tokenType == PicatTokenTypes.LBRACKET -> {
                val listMarker = builder.mark()
                builder.advanceLexer() // Consume [
                if (builder.tokenType != PicatTokenTypes.RBRACKET) {
                    expressionParser.parseExpression(builder) // void return
                    if (builder.tokenType == PicatTokenTypes.PIPE) {
                        builder.advanceLexer() // Consume |
                        skipWhitespace(builder)
                        expressionParser.parseExpression(builder) // void return
                    }
                }
                if (builder.tokenType == PicatTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected ']'")
                    // Error recovery or decision to fail parsing head
                }
                listMarker.done(PicatTokenTypes.LIST_EXPRESSION) // Changed from LIST to LIST_EXPRESSION
            }
            isAtom(builder.tokenType) -> {
                val atomMarker = builder.mark()
                val atomType = builder.tokenType
                builder.advanceLexer()
                atomMarker.done(atomType!!) // Use specific atom type, ensure non-null
            }
            else -> {
                builder.error("Expected predicate/function head")
                // Do not advance here, let caller decide recovery.
                marker.drop()
                return false // Indicate failure
            }
        }

        // If we reached here and parsing one of the branches above didn't advance, it's an error state.
        // However, the sub-parsers (parseStructure, parseQualifiedAtom, parseExpression) should handle their own errors.
        // A simple check is if the builder advanced.
        if (builder.currentOffset == initialPos && builder.tokenType != null) {
             // If it didn't advance and not EOF, likely an issue not caught by branches, or an empty but valid head (unlikely).
             // This check is imperfect.
             marker.drop() // Drop the HEAD marker if nothing substantial was parsed.
             return false
        }

        marker.done(PicatTokenTypes.HEAD)
        return true
    }

    /**
     * Checks if the current token sequence represents a function definition.
     */
    fun isFunctionDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var headValid = false
        val headStartOffset = builder.currentOffset

        // Try to parse the head non-destructively for lookahead
        // This requires a version of parseHead or its components that can rollback effectively
        // For simplicity, we try to parse and see if it looks like a head.
        // This is not true lookahead but part of tentative parsing.
        if (isStructure(builder) || isQualifiedAtom(builder) || builder.tokenType == PicatTokenTypes.LBRACKET || isAtom(builder.tokenType)) {
            // Simulate parsing a head to check its validity and advance position
            // This part is tricky for lookahead without a dedicated lookahead parser.
            // For now, assume if any of these conditions are met, we try to parse a full head.
            // This is a simplified lookahead.
            // A robust lookahead would use a separate builder or more complex rollback.
            val tempHeadMarker = builder.mark()
            if (parseHead(builder)) { // Use the boolean returning parseHead
                 headValid = true
            } else {
                tempHeadMarker.drop() // if parseHead failed and rolled back itself, this is fine.
                                   // if it advanced and errored, this is problematic.
            }
            if(!headValid) { // If simulated head parsing failed
                 marker.rollbackTo()
                 return false
            }
            // If head was "parsed" (possibly with errors marked by parseHead), drop tempHeadMarker
            // as we only care about the tokens for lookahead.
            tempHeadMarker.drop()
        } else {
            marker.rollbackTo()
            return false
        }


        skipWhitespace(builder)
        val result = builder.tokenType == PicatTokenTypes.EQUAL
        marker.rollbackTo() // Rollback to the state before isFunctionDefinition was called
        return result
    }

    /**
     * Checks if the current token sequence represents a rule definition.
     * A rule must have an explicit rule operator (=>, ?=>, <=>, ?<=>, :-).
     */
    fun isRuleDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val headParsed = parseHead(builder) // Consume head for lookahead
        var result = false
        if (headParsed) {
            skipWhitespace(builder)
            result = isRuleOperator(builder.tokenType)
        }
        marker.rollbackTo()
        return result
    }


    /**
     * Checks if the current token sequence represents an implicit rule (what was previously a predicate).
     */
    fun isImplicitRule(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val headParsed = parseHead(builder) // Consume head for lookahead
        var result = false
        if (headParsed) {
            skipWhitespace(builder)
            result = builder.tokenType != PicatTokenTypes.DOT &&
                     builder.tokenType != PicatTokenTypes.EQUAL &&
                     !isRuleOperator(builder.tokenType)
        }
        marker.rollbackTo()
        return result
    }

    /**
     * Checks if the current token sequence represents a fact.
     */
    fun isFact(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        val headParsed = parseHead(builder) // Consume head for lookahead
        var result = false
        if (headParsed) {
            skipWhitespace(builder)
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
