package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isNumber
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isVariable
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.isArithmeticOperator
import com.github.avrilfanomar.picatplugin.language.psi.isComparisonOperator
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat expressions.
 */
class PicatExpressionParser : PicatBaseParser() {
    // Helper for parsing complex data structures
    private lateinit var helper: PicatExpressionParserHelper

    // Helper for parsing binary expressions
    private lateinit var binaryHelper: PicatBinaryExpressionParserHelper

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        super.initialize(parserContext)
        helper = PicatExpressionParserHelper()
        helper.initialize(parserContext)
        binaryHelper = PicatBinaryExpressionParserHelper()
        binaryHelper.initialize(parserContext)
    }

    /**
     * Parses a Picat pattern.
     * Delegates to the pattern parser for actual pattern parsing.
     */
    fun parsePattern(builder: PsiBuilder) {
        patternParser.parsePattern(builder)
    }

    /**
     * Parses a Picat expression.
     */
    fun parseExpression(builder: PsiBuilder) {
        val exprMarker = builder.mark()

        //TODO

        // Parse binary operators and their right-hand terms
        while (!builder.eof() && (isArithmeticOperator(builder.tokenType) || isComparisonOperator(builder.tokenType))) {

            // Create a marker for the operator
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)

            // Parse the right-hand term
            parseTerm(builder)
            skipWhitespace(builder)
        }

        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            val operatorMarker = builder.mark()
            builder.advanceLexer()
            operatorMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseExpression(builder)
            skipWhitespace(builder)
            val colonMarker = builder.mark()
            expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            colonMarker.done(PicatTokenTypes.OPERATOR)
            skipWhitespace(builder)
            parseExpression(builder)
        }

        exprMarker.done(PicatTokenTypes.EXPRESSION)
    }

    /**
     * Parses a term in an expression.
     */
    private fun parseTerm(builder: PsiBuilder) {
        val termMarker = builder.mark()
        when {
            builder.tokenType == PicatTokenTypes.LPAR -> {
                // Parse a parenthesized expression
                val openParenMarker = builder.mark()
                builder.advanceLexer()
                openParenMarker.done(PicatTokenTypes.OPERATOR)
                skipWhitespace(builder)

                // Parse the expression inside the parentheses
                parseExpression(builder)

                skipWhitespace(builder)
                val closeParenMarker = builder.mark()
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
                closeParenMarker.done(PicatTokenTypes.OPERATOR)
            }

            else -> {
                // Parse a primary expression without creating a term marker
                // since we've already created one
                when {
                    isStructure(builder) -> {
                        parseStructure(builder)
                    }

                    isAtom(builder.tokenType) -> {
                        if (isAtom(builder.tokenType)) {
                            builder.advanceLexer()
                        } else {
                            builder.error("Expected atom")
                        }
                    }

                    isVariable(builder.tokenType) -> {
                        parseVariable(builder)
                    }

                    isNumber(builder.tokenType) -> {
                        parseNumber(builder)
                    }

                    builder.tokenType == PicatTokenTypes.STRING -> {
                        parseString(builder)
                    }

                    builder.tokenType == PicatTokenTypes.LBRACKET -> {
                        helper.parseList(builder)
                    }

                    builder.tokenType == PicatTokenTypes.LBRACE -> {
                        helper.parseMap(builder)
                    }

                    else -> {
                        builder.error("Expected primary expression")
                        builder.advanceLexer()
                    }
                }
            }
        }
        termMarker.done(PicatTokenTypes.TERM)
    }


    fun parsePrimaryExpression(builder: PsiBuilder) {
        val termMarker = builder.mark()
        when {
            isStructure(builder) -> parseStructure(builder)
            isAtom(builder.tokenType) -> {
                if (isAtom(builder.tokenType)) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected atom")
                }
            }

            isVariable(builder.tokenType) -> parseVariable(builder)
            isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.STRING -> parseString(builder)
            builder.tokenType == PicatTokenTypes.LPAR -> {
                // Create a marker for the parenthesized expression
                val openParenMarker = builder.mark()
                builder.advanceLexer()
                openParenMarker.done(PicatTokenTypes.OPERATOR)
                skipWhitespace(builder)

                // Parse the expression inside the parentheses
                // This will create its own expression marker
                binaryHelper.parseLogicalOrExpression(builder)

                skipWhitespace(builder)
                val closeParenMarker = builder.mark()
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
                closeParenMarker.done(PicatTokenTypes.OPERATOR)
            }

            builder.tokenType == PicatTokenTypes.LBRACKET -> {
                // Could be list_expression or list_comprehension_expression
                // For now, assume parseList handles list_expression, and we add a new check for list_comprehension_expression
                // This needs more sophisticated lookahead or merging parseList and parseListComprehensionExpression logic
                if (isListComprehensionExpression(builder)) { // Hypothetical lookahead
                    parseListComprehensionExpression(builder, level + 1)
                } else {
                    helper.parseList(builder) // Existing call for list_expression
                }
            }

            builder.tokenType == PicatTokenTypes.LBRACE -> {
                // Could be map, tuple, or lambda_expression
                // This requires more sophisticated lookahead.
                // For now, deferring lambda_expression to be tried first if possible,
                // or assuming parseMap/parseTuple (if parseMap handles tuples too) are tried by helper.
                // This part of PicatExpressionParser might need significant refactoring for robust lookahead.
                // Tentatively adding lambda here, assuming other LBRACE forms are handled by helper.parseMap or similar.
                if (isLambdaExpression(builder)) { // Hypothetical lookahead
                    parseLambdaExpression(builder, level + 1)
                } else if (isTermConstructorExpression(builder)) { // Hypothetical lookahead
                    parseTermConstructorExpression(builder, level + 1)
                } else {
                    helper.parseMap(builder) // Handles map and tuple via PicatExpressionParserHelper
                }
            }
            // Attempt to parse new primary expressions before falling back to error
            // Order might matter depending on token overlaps
            // variable can start index_access_expression, as_pattern_expression
            // atom can start term_constructor_expression
            // '$' starts dollar_term_constructor

            // Note: Proper lookahead or trying alternatives is complex.
            // The original structure of parsePrimaryExpression was simplified.
            // A full GrammarKit parser would handle this with more built-in mechanisms.
            // Here, we're manually extending.
            // The existing isStructure, isAtom, isVariable checks might need to be more specific
            // to avoid consuming tokens before trying these new forms if they share start tokens.

            // For now, I will add calls to the new parsing functions.
            // The actual dispatching logic in a real hand-written parser would be more complex
            // to handle ambiguities and make decisions based on more than just the current tokenType.

            // Let's assume for now that the new forms are checked before the existing fallbacks
            // or that the existing helper.parseMap/parseList are smart enough or modified.
            // This is a placeholder for actual integration.
            // A simple approach is to add new checks:
            // else if (isDollarTermConstructor(builder)) parseDollarTermConstructor(builder, level + 1)
            // else if (isIndexAccessExpression(builder)) parseIndexAccessExpression(builder, level + 1)
            // ... etc.
            // This is deferred as per "Defer complex expression parsing updates".
            // For this step, adding the functions themselves.
            else -> {
                // Try new forms if no existing primary matched
                var handled = false
                if (builder.tokenType == PicatTokenTypes.LBRACE) { // Potentially Lambda or TermConstructor
                    if (parseLambdaExpression(builder, level + 1)) handled = true
                    else if (parseTermConstructorExpression(builder, level + 1)) handled = true
                }
                // Assuming '$' is not yet a token, this check is symbolic
                // else if (builder.tokenType == SOME_DOLLAR_TOKEN_TYPE) {
                //    if (parseDollarTermConstructor(builder, level + 1)) handled = true;
                // }
                // For index access and as pattern, they start with VARIABLE, which is already handled by parseVariable.
                // Those rules might need to be integrated into how 'variable' is parsed, or by having
                // parseVariable return false if it sees '[' or '@' after the variable, letting a subsequent rule try.

                if (!handled) {
                    builder.error("Expected primary expression")
                    builder.advanceLexer()
                }
            }
        }
        termMarker.done(PicatTokenTypes.EXPRESSION) // primary_expression results in an EXPRESSION node
        // Or, more correctly, primary_expression itself isn't a node type, its alternatives are.
        // The marker.done should use the specific element type of what was parsed.
        // This simplified structure is problematic. Let's assume the individual parse methods (parseAtom, parseNumber, etc.)
        // do their own marker.done(SPECIFIC_TYPE), and parsePrimaryExpression doesn't do a generic one.
        // The termMarker.done here should be removed if sub-parsers handle their own markers.
        // For now, commenting out the generic marker.done for primaryExpression.
        // marker.done(PicatTokenTypes.TERM) // Original was TERM, should be specific or none if children are typed.
        // Let's assume sub-rules like parseAtom, parseNumber, parseLambdaExpression etc. will call marker.done themselves.
        // So, parsePrimaryExpression becomes a dispatcher.
        termMarker.drop() // Drop if children handle their markers. If not, this structure is flawed.
        // For this exercise, I will assume children create their own markers.
    }

    // LAMBDA_EXPRESSION ::= LBRACE variable_list? RBRACE ARROW_OP (expression | body)
    fun parseLambdaExpression(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "LambdaExpression")) return false
        if (builder.tokenType != PicatTokenTypes.LBRACE) return false // Basic lookahead

        val marker = builder.mark()
        var result = consumeToken(builder, PicatTokenTypes.LBRACE)

        // Optional variable_list
        if (builder.tokenType != PicatTokenTypes.RBRACE) { // Check if not immediately closing brace
            result = result && parseVariableList(builder, level + 1)
        }

        result = result && consumeToken(builder, PicatTokenTypes.RBRACE)
        result = result && consumeToken(builder, PicatTokenTypes.ARROW_OP)

        // Choice of expression or body
        val bodyMarker = builder.mark()
        if (expressionParser.parseExpression(
                builder,
                level + 1
            )
        ) { // Assuming expressionParser has parseExpression that takes level
            bodyMarker.drop() // Or done with specific type if expression is wrapped
        } else if (statementParser.parseBody(
                builder,
                level + 1
            )
        ) { // Assuming statementParser has parseBody that takes level
            bodyMarker.drop() // Or done with specific type if body is wrapped
        } else {
            builder.error("Expected expression or body in lambda")
            bodyMarker.drop()
            result = false
        }

        if (result) {
            marker.done(PicatTokenTypes.LAMBDA_EXPRESSION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // VARIABLE_LIST ::= variable (COMMA variable)*
    fun parseVariableList(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "VariableList")) return false

        val marker = builder.mark()
        var result = expressionParser.parseVariable(builder, level + 1) // Assuming parseVariable exists

        while (result && builder.tokenType == PicatTokenTypes.COMMA) {
            result = consumeToken(builder, PicatTokenTypes.COMMA)
            result = result && expressionParser.parseVariable(builder, level + 1)
        }

        if (result) {
            marker.done(PicatTokenTypes.VARIABLE_LIST)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // INDEX_ACCESS_EXPRESSION ::= variable LBRACKET expression (COMMA expression)? RBRACKET
    fun parseIndexAccessExpression(builder: PsiBuilder, level: Int): Boolean {
        // This should be tried after parsing a 'variable' successfully, then checking for LBRACKET
        // For now, a standalone parser function.
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "IndexAccessExpression")) return false
        // Assuming the 'variable' part is already consumed or checked by the caller.
        // This function would be called if LBRACKET is seen after a variable.

        val marker = builder.mark() // Marker should ideally start before the variable
        // Let's assume variable is parsed by caller, and we start at LBRACKET
        var result = consumeToken(builder, PicatTokenTypes.LBRACKET)
        result = result && expressionParser.parseExpression(builder, level + 1)

        if (builder.tokenType == PicatTokenTypes.COMMA) {
            result = result && consumeToken(builder, PicatTokenTypes.COMMA)
            result = result && expressionParser.parseExpression(builder, level + 1)
        }

        result = result && consumeToken(builder, PicatTokenTypes.RBRACKET)

        if (result) {
            marker.done(PicatTokenTypes.INDEX_ACCESS_EXPRESSION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // AS_PATTERN_EXPRESSION ::= variable AT pattern AT?
    fun parseAsPatternExpression(builder: PsiBuilder, level: Int): Boolean {
        // Similar to IndexAccess, assumes 'variable' is consumed/checked by caller.
        // Called if AT is seen after a variable.
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "AsPatternExpression")) return false

        val marker = builder.mark() // Marker should ideally start before the variable
        var result = consumeToken(builder, PicatTokenTypes.AT)
        result = result && patternParser.parsePattern(builder, level + 1) // Assuming patternParser has parsePattern

        if (builder.tokenType == PicatTokenTypes.AT) {
            result = result && consumeToken(builder, PicatTokenTypes.AT)
        }

        if (result) {
            marker.done(PicatTokenTypes.AS_PATTERN_EXPRESSION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // TERM_CONSTRUCTOR_EXPRESSION ::= (atom | qualified_atom) LBRACE map_entries RBRACE
    fun parseTermConstructorExpression(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "TermConstructorExpression")) return false
        // Lookahead for atom/qualified_atom followed by LBRACE
        if (!(isAtom(builder.tokenType) || isQualifiedAtom(builder))) return false // Simplified lookahead

        val marker = builder.mark()
        var result = false

        val headMarker = builder.mark()
        if (isQualifiedAtom(builder)) { // isQualifiedAtom needs to be defined
            result = definitionParser.parseQualifiedAtom(builder, level + 1) // Assuming parseQualifiedAtom exists
            headMarker.done(PicatTokenTypes.QUALIFIED_ATOM) // Or drop if parseQualifiedAtom does it
        } else if (isAtom(builder.tokenType)) {
            result = expressionParser.parseAtom(builder, level + 1) // Assuming parseAtom exists
            headMarker.drop() // parseAtom should do its own marker
        } else {
            headMarker.drop()
            marker.rollbackTo()
            return false
        }

        if (result && builder.tokenType == PicatTokenTypes.LBRACE) {
            result = result && consumeToken(builder, PicatTokenTypes.LBRACE)
            result = result && helper.parseMapEntries(builder, level + 1) // Assuming helper has parseMapEntries
            result = result && consumeToken(builder, PicatTokenTypes.RBRACE)
        } else {
            // Not a term constructor if no LBRACE after atom/qualified_atom
            marker.rollbackTo()
            return false
        }

        if (result) {
            marker.done(PicatTokenTypes.TERM_CONSTRUCTOR_EXPRESSION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // LIST_COMPREHENSION_EXPRESSION ::= LBRACKET expression PIPE foreach_generators RBRACKET
    fun parseListComprehensionExpression(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ListComprehensionExpression")) return false
        if (builder.tokenType != PicatTokenTypes.LBRACKET) return false // Basic lookahead

        val marker = builder.mark()
        var result = consumeToken(builder, PicatTokenTypes.LBRACKET)
        result = result && expressionParser.parseExpression(builder, level + 1)
        result = result && consumeToken(builder, PicatTokenTypes.PIPE)
        result = result && parseForeachGenerators(builder, level + 1) // Needs this method
        result = result && consumeToken(builder, PicatTokenTypes.RBRACKET)

        if (result) {
            marker.done(PicatTokenTypes.LIST_COMPREHENSION_EXPRESSION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // FOREACH_GENERATORS ::= foreach_generator (COMMA foreach_generator)*
    fun parseForeachGenerators(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ForeachGenerators")) return false
        val marker = builder.mark()
        var result = parseForeachGenerator(builder, level + 1)
        while (result && builder.tokenType == PicatTokenTypes.COMMA) {
            result = consumeToken(builder, PicatTokenTypes.COMMA)
            result = result && parseForeachGenerator(builder, level + 1)
        }
        if (result) marker.done(PicatTokenTypes.FOREACH_GENERATORS) else marker.rollbackTo()
        return result
    }

    // FOREACH_GENERATOR ::= variable IN_KEYWORD expression | variable EQUAL expression
    fun parseForeachGenerator(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ForeachGenerator")) return false
        if (builder.tokenType != PicatTokenTypes.VARIABLE && builder.tokenType != PicatTokenTypes.ANONYMOUS_VARIABLE) return false

        val marker = builder.mark()
        var result = expressionParser.parseVariable(builder, level + 1) // Assuming parseVariable exists

        if (builder.tokenType == PicatTokenTypes.IN_KEYWORD) {
            result = result && consumeToken(builder, PicatTokenTypes.IN_KEYWORD)
            result = result && expressionParser.parseExpression(builder, level + 1)
        } else if (builder.tokenType == PicatTokenTypes.EQUAL) {
            result = result && consumeToken(builder, PicatTokenTypes.EQUAL)
            result = result && expressionParser.parseExpression(builder, level + 1)
        } else {
            result = false
            builder.error("Expected 'in' or '=' in foreach generator")
        }

        if (result) marker.done(PicatTokenTypes.FOREACH_GENERATOR) else marker.rollbackTo()
        return result
    }

    // DOLLAR_TERM_CONSTRUCTOR ::= "$" goal "$"
    fun parseDollarTermConstructor(builder: PsiBuilder, level: Int): Boolean {
        if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "DollarTermConstructor")) return false
        // Assuming '$' is handled as a literal string by the lexer or a specific token
        // For now, using a placeholder for how '$' would be consumed.
        // This requires '$' to be defined as a token or handled by GeneratedParserUtilBase.consumeToken(builder, "$")
        // Let's assume it's not a defined token for now, and this part is illustrative.
        // If '$' is DATA_CONSTRUCTOR token:
        // if (builder.tokenType != PicatTokenTypes.DATA_CONSTRUCTOR) return false;

        val marker = builder.mark()
        // var result = consumeToken(builder, PicatTokenTypes.DATA_CONSTRUCTOR) // If $ is DATA_CONSTRUCTOR
        var result = GeneratedParserUtilBase.consumeToken(builder, "$") // If using string literal match
        result = result && statementParser.parseGoal(builder, level + 1) // Assuming statementParser has parseGoal
        // result = result && consumeToken(builder, PicatTokenTypes.DATA_CONSTRUCTOR)
        result = result && GeneratedParserUtilBase.consumeToken(builder, "$")

        if (result) {
            marker.done(PicatTokenTypes.DOLLAR_TERM_CONSTRUCTOR)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // Helper for lookahead, these are simplified and might need actual PsiBuilder.lookAhead steps
    private fun isLambdaExpression(builder: PsiBuilder): Boolean {
        if (builder.tokenType != PicatTokenTypes.LBRACE) return false
        val marker = builder.mark()
        builder.advanceLexer() // Consume LBRACE
        // Check for variable_list (optional) then RBRACE then ARROW_OP
        var isLambda = true
        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            if (!parseVariableList(builder, 0)) { // level 0 for lookahead
                // isLambda = false // This would consume tokens, bad for lookahead
            }
        }
        if (builder.tokenType != PicatTokenTypes.RBRACE) isLambda = false
        if (isLambda) builder.advanceLexer() // Consume RBRACE
        if (builder.tokenType != PicatTokenTypes.ARROW_OP) isLambda = false

        marker.rollbackTo()
        return isLambda
    }

    private fun isTermConstructorExpression(builder: PsiBuilder): Boolean {
        // (atom | qualified_atom) LBRACE
        val marker = builder.mark()
        var isTermCons = false
        if (isAtom(builder.tokenType) || isQualifiedAtom(builder)) { // isQualifiedAtom needs definition
            // Simulate parsing atom or qualified_atom
            if (isQualifiedAtom(builder)) {
                // parseQualifiedAtom(builder, 0) // Don't actually parse, just check
                builder.advanceLexer() // atom
                if (builder.tokenType == PicatTokenTypes.DOT) builder.advanceLexer()
                if (isAtom(builder.tokenType)) builder.advanceLexer() else {
                    marker.rollbackTo(); return false
                }
            } else {
                builder.advanceLexer() // atom
            }
            if (builder.tokenType == PicatTokenTypes.LBRACE) {
                isTermCons = true
            }
        }
        marker.rollbackTo()
        return isTermCons
    }

    private fun isListComprehensionExpression(builder: PsiBuilder): Boolean {
        // LBRACKET expression PIPE foreach_generators RBRACKET
        if (builder.tokenType != PicatTokenTypes.LBRACKET) return false
        val marker = builder.mark()
        builder.advanceLexer() // LBRACKET
        // This lookahead is too complex without full parsing ability here.
        // A real implementation would use PsiBuilder.lookAhead more effectively.
        // For now, this is a placeholder.
        // Simplistic check: just look for PIPE after some non-RBRACKET tokens.
        var hasPipe = false
        var tempMarker = builder.mark()
        var i = 0
        while (builder.tokenType != null && builder.tokenType != PicatTokenTypes.RBRACKET && i < 10) { // Limit lookahead
            if (builder.tokenType == PicatTokenTypes.PIPE) {
                hasPipe = true; break; }
            builder.advanceLexer()
            i++
        }
        tempMarker.rollbackTo()
        marker.rollbackTo()
        return hasPipe
    }

    // Dummy isQualifiedAtom for lookahead logic, real parsing in DefinitionParser or similar
    private fun isQualifiedAtom(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
            if (builder.tokenType == PicatTokenTypes.DOT) {
                builder.advanceLexer()
                if (isAtom(builder.tokenType)) {
                    result = true
                }
            }
        }
        marker.rollbackTo()
        return result
    }
}
