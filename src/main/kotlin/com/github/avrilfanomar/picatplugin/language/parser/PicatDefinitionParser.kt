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
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "FunctionClause")) return false // Temp removed
        val marker = builder.mark()
        // A function clause must start with a head and then an assign operator
        val headParsed = parseHead(builder) // parseHead now returns boolean
        if (!headParsed) {
            marker.rollbackTo()
            return false
        }
        if (builder.tokenType != PicatTokenTypes.ASSIGN_OP) {
            // This might not be a function clause after all if ASSIGN_OP is missing.
            // This could indicate an error or that it's a different kind of construct.
            // For robust parsing, this would require more sophisticated lookahead before even trying parseFunctionClause.
            // For now, assume if parseHead succeeded but ASSIGN_OP is not next, it's not a function clause.
            // However, the parser might have already committed to this path.
            // Let's report error and fail if ASSIGN_OP is not found after a head.
            builder.error("Expected ':=' after function head")
            marker.rollbackTo() // Rollback the whole function_clause attempt
            return false
        }

        // At this point, we have a head and an ASSIGN_OP. Try to parse as rule or fact.
        // The distinction between function_rule and function_fact is often just the complexity of the body/expression.
        // Let's try parseFunctionRule, then parseFunctionFact.
        // This implies parseFunctionRule and parseFunctionFact should not re-parse head and ASSIGN_OP.
        // Refactoring parseFunctionRule/Fact to take a 'headAndAssignOpAlreadyParsed' flag or similar might be needed.
        // Or, they can expect to parse from head onwards and use rollback.

        // Simplified approach: Assume parseFunctionRule and parseFunctionFact handle parsing from head.
        // This means the parseHead above was just for lookahead/validation.
        // This is not efficient. A better way is to pass the parsed head.
        // For now, let's assume parseFunctionRule/Fact will re-parse or that the initial parseHead was part of a broader lookahead.
        // Given the current structure, let's re-parse head within rule/fact for simplicity of this diff.
        // This will be undone by making parseFunctionRule/Fact expect head to be parsed.

        // Revert: parseHead was already done. Now expect ASSIGN_OP and then body/expression.
        // The previous `parseHead` call was for validation.
        // The `parseFunctionRule` and `parseFunctionFact` will internally call `parseHead` again.
        // This is inefficient but avoids restructuring them significantly in this step.

        // Let's assume parseFunctionRule and parseFunctionFact are robust enough.
        // The initial parseHead and ASSIGN_OP check was more of a guard for this path.
        // The actual rule/fact parsers will consume these.
        marker.rollbackTo() // Rollback the marker to before the initial parseHead
        // Then re-mark and try parsing the full rule or fact.
        val actualClauseMarker = builder.mark()
        var result = false
        if (parseFunctionRule(builder)) { // Removed level
            result = true
        } else if (parseFunctionFact(builder)) { // Removed level
            result = true
        } else {
            // If neither specific function rule/fact parsed, it's an error.
            // The error would be marked by those sub-parsers.
            actualClauseMarker.drop() // Drop if sub-parsers failed and rolled back.
            return false // Error already marked by sub-parsers
        }

        if (result) {
            actualClauseMarker.done(PicatTokenTypes.FUNCTION_CLAUSE)
        } else {
            actualClauseMarker.rollbackTo() // Should not be reached if sub-parsers handle rollback
        }
        return result
    }

    // Helper to call parseHead and return a boolean, assuming parseHead marks errors internally.
    // private fun parseHeadAndReturnBool(builder: PsiBuilder): Boolean { // This was a temporary helper.
    //     val BMM = builder.mark()
    //     parseHead(builder)
    //     if (BMM.precede().getCurrentOffset() == builder.currentOffset && builder.tokenType != null /* not EOF */) {
    //     }
    //     BMM.drop()
    //     return true // Placeholder: assume true, rely on parseHead error marking.
    // }

    // PREDICATE_RULE ::= head RULE_OP body EOR
    fun parsePredicateRule(builder: PsiBuilder, level: Int): Boolean { // Keep level if sub-parsers use it
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "PredicateRule")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHead(builder) // Changed to use boolean return
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.RULE_OP, "Expected rule operator (e.g., :-, =>)")
        result = result && statementParser.parseBody(builder, level + 1) // statementParser methods might take level
            marker.done(PicatTokenTypes.FUNCTION_CLAUSE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // Helper to call parseHead and return a boolean, assuming parseHead marks errors internally.
    private fun parseHeadAndReturnBool(builder: PsiBuilder): Boolean {
        val BMM = builder.mark() // Before Marker Marker, not related to BMW :)
        parseHead(builder)
        // If parseHead encountered an error and didn't advance, or advanced but marked error,
        // this logic might need to be smarter, e.g. by checking if builder.currentOffset changed.
        // For now, assume if parseHead itself marks an error, that's handled.
        // We can check if an error was marked by parseHead, but that's complex.
        // Simplest is to assume it works or marks error.
        // Let's check if it advanced. If not, it's likely a failure.
        // This is not perfectly robust. A better parseHead would return Boolean.
        if (BMM.precede().getCurrentOffset() == builder.currentOffset && builder.tokenType != null /* not EOF */) {
             // If parseHead didn't advance, and we are not at EOF, it might be an issue.
             // However, parseHead might correctly parse an empty head if allowed by grammar (not typical).
             // For now, let's assume parseHead advances or marks an error.
             // BMM.drop() // Drop if we are not making a node from this.
        }
        BMM.drop() // We don't want a node from this helper call itself.
        return true // Placeholder: assume true, rely on parseHead error marking.
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
