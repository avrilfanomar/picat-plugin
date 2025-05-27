package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class PicatParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        // Parse the entire file content
        while (!builder.eof()) {
            when (builder.tokenType) {
                PicatTokenTypes.WHITE_SPACE -> builder.advanceLexer()
                PicatTokenTypes.COMMENT,
                PicatTokenTypes.MULTI_LINE_COMMENT -> parseComment(builder)

                else -> parseTopLevelItem(builder)
            }
        }

        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseTopLevelItem(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.MODULE_KEYWORD -> parseModuleDeclaration(builder)
            PicatTokenTypes.END_MODULE_KEYWORD -> parseEndModuleDeclaration(builder)
            PicatTokenTypes.IMPORT_KEYWORD -> parseImportStatement(builder)
            PicatTokenTypes.EXPORT_KEYWORD -> parseExportStatement(builder)
            PicatTokenTypes.INCLUDE_KEYWORD -> parseIncludeStatement(builder)
            PicatTokenTypes.USING_KEYWORD -> parseUsingStatement(builder)
            else -> parseDefinition(builder)
        }
    }

    private fun parseModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.MODULE_KEYWORD, "Expected 'module'")

        parseModuleName(builder)

        // Optional export clause
        if (builder.tokenType == PicatTokenTypes.EXPORT_KEYWORD) {
            parseExportClause(builder)
        }

        // Optional import clause
        if (builder.tokenType == PicatTokenTypes.IMPORT_KEYWORD) {
            parseImportClause(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after module declaration")
        marker.done(PicatTokenTypes.MODULE_DECLARATION)
    }

    private fun parseEndModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.END_MODULE_KEYWORD, "Expected 'end_module'")

        // Optional module name
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            parseModuleName(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after end_module")
        marker.done(PicatTokenTypes.END_MODULE_DECLARATION)
    }

    private fun parseModuleName(builder: PsiBuilder) {
        val marker = builder.mark()
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER || builder.tokenType == PicatTokenTypes.QUOTED_ATOM) {
            builder.advanceLexer()
            marker.done(PicatTokenTypes.MODULE_NAME)
        } else {
            marker.drop()
            builder.error("Expected module name")
        }
    }

    private fun parseImportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")

        parseImportList(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after import statement")
        marker.done(PicatTokenTypes.IMPORT_STATEMENT)
    }

    private fun parseExportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")

        parseExportList(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after export statement")
        marker.done(PicatTokenTypes.EXPORT_STATEMENT)
    }

    private fun parseIncludeStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.INCLUDE_KEYWORD, "Expected 'include'")

        // Parse file_spec (STRING or atom)
        if (builder.tokenType == PicatTokenTypes.STRING) {
            // Store the string value for later retrieval
            val stringMarker = builder.mark()
            builder.advanceLexer()
            stringMarker.done(PicatTokenTypes.STRING)
        } else if (isAtom(builder.tokenType)) {
            parseAtom(builder)
        } else {
            builder.error("Expected file path string or atom")
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after include statement")
        marker.done(PicatTokenTypes.INCLUDE_STATEMENT)
    }

    private fun parseUsingStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.USING_KEYWORD, "Expected 'using'")

        parseModuleName(builder)

        // Optional rename list
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseRenameList(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after using statement")
        marker.done(PicatTokenTypes.USING_STATEMENT)
    }

    private fun parseDefinition(builder: PsiBuilder) {
        // Don't create a marker here, let the specific parse methods create their own markers

        // Always try to parse as a fact first
        val marker = builder.mark()
        val factResult = tryParseFact(builder)
        marker.rollbackTo()

        when {
            factResult -> parseFact(builder)
            isFunctionDefinition(builder) -> parseFunctionDefinition(builder)
            isRuleDefinition(builder) -> parseRuleDefinition(builder)
            isPredicateDefinition(builder) -> parsePredicateDefinition(builder)
            else -> {
                val errorMarker = builder.mark()
                builder.error("Unexpected definition")
                builder.advanceLexer()
                errorMarker.drop()
            }
        }
    }

    private fun tryParseFact(builder: PsiBuilder): Boolean {
        var success = true

        // Try to parse the head
        if (isAtom(builder.tokenType) || isStructure(builder) || isQualifiedAtom(builder)) {
            if (isStructure(builder)) {
                // Skip the atom
                builder.advanceLexer()
                // Skip the opening parenthesis
                builder.advanceLexer()
                // Skip the arguments
                var parenCount = 1
                while (parenCount > 0 && !builder.eof()) {
                    if (builder.tokenType == PicatTokenTypes.LPAR) {
                        parenCount++
                    } else if (builder.tokenType == PicatTokenTypes.RPAR) {
                        parenCount--
                    }
                    builder.advanceLexer()
                }
            } else {
                // Skip the atom or qualified atom
                builder.advanceLexer()
                if (isQualifiedAtom(builder)) {
                    // Skip the dot and the second atom
                    builder.advanceLexer()
                    builder.advanceLexer()
                }
            }

            // Optional equals sign and expression
            if (builder.tokenType == PicatTokenTypes.EQUAL) {
                builder.advanceLexer()
                // Skip the expression (this is simplified)
                while (builder.tokenType != PicatTokenTypes.DOT && !builder.eof()) {
                    builder.advanceLexer()
                }
            }

            // Check for the dot
            success = builder.tokenType == PicatTokenTypes.DOT
        } else {
            success = false
        }

        return success
    }

    private fun parseFunctionDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        expectToken(builder, PicatTokenTypes.EQUAL, "Expected '=' in function definition")

        parseExpression(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after function definition")
        // Function definitions are also facts according to the BNF grammar
        marker.done(PicatTokenTypes.FACT)
    }

    private fun parseRuleDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        // Parse rule operator (=>, ?=>, <=>, ?<=>, :-)
        val opMarker = builder.mark()
        if (isRuleOperator(builder.tokenType)) {
            builder.advanceLexer()
            opMarker.done(PicatTokenTypes.RULE_OPERATOR)
        } else {
            opMarker.drop()
            builder.error("Expected rule operator (=>, ?=>, <=>, ?<=>, :-)")
        }

        parseBody(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after rule definition")
        marker.done(PicatTokenTypes.RULE)
    }

    private fun parsePredicateDefinition(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        // Check if this is a function definition (has an equals sign)
        val isFunction = builder.tokenType == PicatTokenTypes.EQUAL

        // Optional body
        if (builder.tokenType != PicatTokenTypes.DOT) {
            if (isFunction) {
                builder.advanceLexer() // Skip the equals sign
                parseExpression(builder) // Parse the expression
            } else {
                parseBody(builder)
            }
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after predicate definition")

        // If this is a function definition, mark it as a fact
        if (isFunction) {
            marker.done(PicatTokenTypes.FACT)
        } else {
            marker.done(PicatTokenTypes.PREDICATE_DEFINITION)
        }
    }

    private fun parseFact(builder: PsiBuilder) {
        val marker = builder.mark()
        parseHead(builder)

        // Optional function body
        if (builder.tokenType == PicatTokenTypes.EQUAL) {
            builder.advanceLexer()
            parseExpression(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after fact")
        marker.done(PicatTokenTypes.FACT)
    }

    private fun parseHead(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            isAtom(builder.tokenType) -> parseAtom(builder)
            isStructure(builder) -> parseStructure(builder)
            isQualifiedAtom(builder) -> parseQualifiedAtom(builder)
            else -> {
                builder.error("Expected predicate/function head")
                builder.advanceLexer()
                marker.drop()
                return
            }
        }

        marker.done(PicatTokenTypes.HEAD)
    }

    private fun parseBody(builder: PsiBuilder) {
        val marker = builder.mark()

        parseGoal(builder)
        while (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseGoal(builder)
        }

        marker.done(PicatTokenTypes.BODY)
    }

    private fun parseGoal(builder: PsiBuilder) {
        val marker = builder.mark()

        when (builder.tokenType) {
            PicatTokenTypes.IF_KEYWORD -> parseIfThenElse(builder)
            PicatTokenTypes.FOREACH_KEYWORD -> parseForeachLoop(builder)
            PicatTokenTypes.WHILE_KEYWORD -> parseWhileLoop(builder)
            PicatTokenTypes.CASE_KEYWORD -> parseCaseExpression(builder)
            PicatTokenTypes.TRY_KEYWORD -> parseTryCatch(builder)
            PicatTokenTypes.NOT_KEYWORD -> parseNegation(builder)
            PicatTokenTypes.FAIL_KEYWORD -> parseFail(builder)
            PicatTokenTypes.TRUE_KEYWORD -> parseTrue(builder)
            PicatTokenTypes.FALSE_KEYWORD -> parseFalse(builder)
            PicatTokenTypes.CUT -> parseCut(builder)
            PicatTokenTypes.RETURN_KEYWORD -> parseReturn(builder)
            PicatTokenTypes.CONTINUE_KEYWORD -> parseContinue(builder)
            PicatTokenTypes.BREAK_KEYWORD -> parseBreak(builder)
            PicatTokenTypes.THROW_KEYWORD -> parseThrow(builder)
            else -> {
                if (isAssignment(builder)) {
                    parseAssignment(builder)
                } else {
                    parseExpression(builder)
                }
            }
        }

        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseIfThenElse(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.IF_KEYWORD, "Expected 'if'")

        parseExpression(builder)
        expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")

        parseBody(builder)

        // Optional elseif clauses
        while (builder.tokenType == PicatTokenTypes.ELSEIF_KEYWORD) {
            builder.advanceLexer()
            parseExpression(builder)
            expectKeyword(builder, PicatTokenTypes.THEN_KEYWORD, "Expected 'then'")
            parseBody(builder)
        }

        // Optional else clause
        if (builder.tokenType == PicatTokenTypes.ELSE_KEYWORD) {
            builder.advanceLexer()
            parseBody(builder)
        }

        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.IF_THEN_ELSE)
    }

    private fun parseForeachLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.FOREACH_KEYWORD, "Expected 'foreach'")

        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        parseForeachGenerators(builder)
        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")

        parseBody(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.FOREACH_LOOP)
    }

    private fun parseWhileLoop(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.WHILE_KEYWORD, "Expected 'while'")

        parseExpression(builder)
        expectKeyword(builder, PicatTokenTypes.DO_KEYWORD, "Expected 'do'")

        parseBody(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.WHILE_LOOP)
    }

    private fun parseCaseExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.CASE_KEYWORD, "Expected 'case'")

        parseExpression(builder)
        expectKeyword(builder, PicatTokenTypes.OF_KEYWORD, "Expected 'of'")

        parseCaseArms(builder)
        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.CASE_EXPRESSION)
    }

    private fun parseTryCatch(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.TRY_KEYWORD, "Expected 'try'")

        parseBody(builder)
        expectKeyword(builder, PicatTokenTypes.CATCH_KEYWORD, "Expected 'catch'")

        parseCatchClauses(builder)

        // Optional finally clause
        if (builder.tokenType == PicatTokenTypes.FINALLY_KEYWORD) {
            builder.advanceLexer()
            parseBody(builder)
        }

        expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end'")
        marker.done(PicatTokenTypes.TRY_CATCH)
    }

    private fun parseExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        parseLogicalOrExpression(builder)

        // Ternary operator
        if (builder.tokenType == PicatTokenTypes.QUESTION) {
            builder.advanceLexer()
            parseExpression(builder)
            expectToken(builder, PicatTokenTypes.COLON, "Expected ':' in ternary operator")
            parseExpression(builder)
        }

        marker.done(PicatTokenTypes.EXPRESSION)
    }

    private fun parseLogicalOrExpression(builder: PsiBuilder) {
        parseLogicalAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.OR_KEYWORD) {
            builder.advanceLexer()
            parseLogicalAndExpression(builder)
        }
    }

    private fun parseLogicalAndExpression(builder: PsiBuilder) {
        parseBitwiseOrExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AND_KEYWORD) {
            builder.advanceLexer()
            parseBitwiseOrExpression(builder)
        }
    }

    private fun parseBitwiseOrExpression(builder: PsiBuilder) {
        parseBitwiseXorExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PIPE) {
            builder.advanceLexer()
            parseBitwiseXorExpression(builder)
        }
    }

    private fun parseBitwiseXorExpression(builder: PsiBuilder) {
        parseBitwiseAndExpression(builder)
        while (builder.tokenType == PicatTokenTypes.XOR_KEYWORD || builder.tokenType == PicatTokenTypes.CARET) {
            builder.advanceLexer()
            parseBitwiseAndExpression(builder)
        }
    }

    private fun parseBitwiseAndExpression(builder: PsiBuilder) {
        parseEqualityExpression(builder)
        while (builder.tokenType == PicatTokenTypes.AMPERSAND) {
            builder.advanceLexer()
            parseEqualityExpression(builder)
        }
    }

    private fun parseEqualityExpression(builder: PsiBuilder) {
        parseRelationalExpression(builder)
        while (builder.tokenType == PicatTokenTypes.IDENTICAL || builder.tokenType == PicatTokenTypes.NOT_IDENTICAL) {
            builder.advanceLexer()
            parseRelationalExpression(builder)
        }
    }

    private fun parseRelationalExpression(builder: PsiBuilder) {
        parseShiftExpression(builder)
        while (isRelationalOperator(builder.tokenType)) {
            builder.advanceLexer()
            parseShiftExpression(builder)
        }
    }

    private fun parseShiftExpression(builder: PsiBuilder) {
        parseAdditiveExpression(builder)
        while (builder.tokenType == PicatTokenTypes.SHIFT_LEFT || builder.tokenType == PicatTokenTypes.SHIFT_RIGHT) {
            builder.advanceLexer()
            parseAdditiveExpression(builder)
        }
    }

    private fun parseAdditiveExpression(builder: PsiBuilder) {
        parseMultiplicativeExpression(builder)
        while (builder.tokenType == PicatTokenTypes.PLUS || builder.tokenType == PicatTokenTypes.MINUS) {
            builder.advanceLexer()
            parseMultiplicativeExpression(builder)
        }
    }

    private fun parseMultiplicativeExpression(builder: PsiBuilder) {
        parsePowerExpression(builder)
        while (isMultiplicativeOperator(builder.tokenType)) {
            builder.advanceLexer()
            parsePowerExpression(builder)
        }
    }

    private fun parsePowerExpression(builder: PsiBuilder) {
        parseUnaryExpression(builder)
        while (builder.tokenType == PicatTokenTypes.POWER) {
            builder.advanceLexer()
            parseUnaryExpression(builder)
        }
    }

    private fun parseUnaryExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        if (isUnaryOperator(builder.tokenType)) {
            builder.advanceLexer()
        }

        parsePrimaryExpression(builder)
        marker.done(PicatTokenTypes.UNARY_EXPRESSION)
    }

    private fun parsePrimaryExpression(builder: PsiBuilder) {
        when {
            isAtom(builder.tokenType) -> parseAtom(builder)
            isVariable(builder.tokenType) -> parseVariable(builder)
            isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.STRING -> parseString(builder)
            builder.tokenType == PicatTokenTypes.LPAR -> {
                builder.advanceLexer()
                parseExpression(builder)
                expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
            }

            builder.tokenType == PicatTokenTypes.LBRACKET -> parseList(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> parseMap(builder)
            else -> {
                builder.error("Expected primary expression")
                builder.advanceLexer()
            }
        }
    }

    private fun expectKeyword(builder: PsiBuilder, type: IElementType, error: String) {
        if (builder.tokenType == type) {
            builder.advanceLexer()
        } else {
            builder.error(error)
        }
    }

    private fun expectToken(builder: PsiBuilder, type: IElementType, error: String) {
        if (builder.tokenType == type) {
            builder.advanceLexer()
        } else {
            builder.error(error)
        }
    }

    private fun isAtom(type: IElementType?): Boolean =
        type == PicatTokenTypes.IDENTIFIER || type == PicatTokenTypes.QUOTED_ATOM

    private fun isVariable(type: IElementType?): Boolean =
        type == PicatTokenTypes.VARIABLE || type == PicatTokenTypes.ANONYMOUS_VARIABLE

    private fun isNumber(type: IElementType?): Boolean =
        type == PicatTokenTypes.INTEGER || type == PicatTokenTypes.FLOAT ||
                type == PicatTokenTypes.HEX_INTEGER || type == PicatTokenTypes.OCTAL_INTEGER ||
                type == PicatTokenTypes.BINARY_INTEGER

    private fun isRelationalOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.LESS || type == PicatTokenTypes.GREATER ||
                type == PicatTokenTypes.LESS_EQUAL || type == PicatTokenTypes.GREATER_EQUAL

    private fun isMultiplicativeOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.MULTIPLY || type == PicatTokenTypes.DIVIDE ||
                type == PicatTokenTypes.INT_DIVIDE || type == PicatTokenTypes.MOD_KEYWORD

    private fun isUnaryOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.PLUS || type == PicatTokenTypes.MINUS ||
                type == PicatTokenTypes.NOT_KEYWORD || type == PicatTokenTypes.BACKSLASH

    private fun isRuleOperator(type: IElementType?): Boolean =
        type == PicatTokenTypes.ARROW_OP || type == PicatTokenTypes.BACKTRACKABLE_ARROW_OP ||
                type == PicatTokenTypes.BICONDITIONAL_OP || type == PicatTokenTypes.BACKTRACKABLE_BICONDITIONAL_OP ||
                type == PicatTokenTypes.RULE_OP

    private fun isFunctionDefinition(builder: PsiBuilder): Boolean {
        // Look ahead to see if this is a function definition
        val marker = builder.mark()
        var result = false

        parseHead(builder)
        result = builder.tokenType == PicatTokenTypes.EQUAL
        marker.rollbackTo()

        return result
    }

    private fun isRuleDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        parseHead(builder)
        result = isRuleOperator(builder.tokenType)
        marker.rollbackTo()

        return result
    }

    private fun isPredicateDefinition(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        parseHead(builder)
        // A predicate definition has a body (not just a dot)
        // and doesn't have an equals sign (which would make it a function definition)
        result = builder.tokenType != PicatTokenTypes.DOT && builder.tokenType != PicatTokenTypes.EQUAL
        marker.rollbackTo()

        return result
    }

    private fun isFact(builder: PsiBuilder): Boolean {
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

    private fun isStructure(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
            result = builder.tokenType == PicatTokenTypes.LPAR
        }

        marker.rollbackTo()
        return result
    }

    private fun isQualifiedAtom(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
            if (builder.tokenType == PicatTokenTypes.DOT) {
                builder.advanceLexer()
                result = isAtom(builder.tokenType)
            }
        }

        marker.rollbackTo()
        return result
    }

    private fun isAssignment(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        if (builder.tokenType == PicatTokenTypes.VARIABLE) {
            builder.advanceLexer()
            result = builder.tokenType == PicatTokenTypes.ASSIGN_OP
        }

        marker.rollbackTo()
        return result
    }

    // Basic parsing methods
    private fun parseComment(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(PicatTokenTypes.COMMENT)
    }

    private fun parseAtom(builder: PsiBuilder) {
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER || builder.tokenType == PicatTokenTypes.QUOTED_ATOM) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
    }

    private fun parseStructure(builder: PsiBuilder) {
        val marker = builder.mark()
        parseAtom(builder)
        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parseArgumentList(builder)
        }

        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE)
    }

    private fun parseQualifiedAtom(builder: PsiBuilder) {
        val marker = builder.mark()
        parseAtom(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")
        parseAtom(builder)
        marker.done(PicatTokenTypes.ATOM_NO_ARGS)
    }

    private fun parseVariable(builder: PsiBuilder) {
        if (builder.tokenType == PicatTokenTypes.VARIABLE || builder.tokenType == PicatTokenTypes.ANONYMOUS_VARIABLE) {
            builder.advanceLexer()
        } else {
            builder.error("Expected variable")
        }
    }

    private fun parseNumber(builder: PsiBuilder) {
        if (isNumber(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected number")
        }
    }

    private fun parseString(builder: PsiBuilder) {
        if (builder.tokenType == PicatTokenTypes.STRING) {
            builder.advanceLexer()
        } else {
            builder.error("Expected string")
        }
    }

    private fun parseList(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACKET, "Expected '['")

        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parseListItems(builder)
        }

        expectToken(builder, PicatTokenTypes.RBRACKET, "Expected ']'")
        marker.done(PicatTokenTypes.LIST)
    }

    private fun parseListItems(builder: PsiBuilder) {
        parseExpression(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseExpression(builder)
        }

        if (builder.tokenType == PicatTokenTypes.PIPE) {
            builder.advanceLexer()
            parseExpression(builder)
        }
    }

    private fun parseMap(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACE, "Expected '{'")

        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parseMapEntries(builder)
        }

        expectToken(builder, PicatTokenTypes.RBRACE, "Expected '}'")
        marker.done(PicatTokenTypes.MAP)
    }

    private fun parseMapEntries(builder: PsiBuilder) {
        parseMapEntry(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseMapEntry(builder)
        }
    }

    private fun parseMapEntry(builder: PsiBuilder) {
        parseExpression(builder)
        expectToken(builder, PicatTokenTypes.COLON, "Expected ':'")
        parseExpression(builder)
    }

    private fun parseArgumentList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExpression(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseExpression(builder)
        }

        marker.done(PicatTokenTypes.ARGUMENT_LIST)
    }

    // Clause parsing methods
    private fun parseExportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        parseExportList(builder)
        marker.done(PicatTokenTypes.EXPORT_CLAUSE)
    }

    private fun parseImportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        parseImportList(builder)
        marker.done(PicatTokenTypes.IMPORT_CLAUSE)
    }

    private fun parseExportList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExportSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseExportSpec(builder)
        }

        marker.done(PicatTokenTypes.EXPORT_LIST)
    }

    private fun parseExportSpec(builder: PsiBuilder) {
        if (builder.lookAhead(1) == PicatTokenTypes.DIVIDE || builder.lookAhead(1) == PicatTokenTypes.INTEGER) {
            parsePredicateIndicator(builder)
        } else {
            parseAtom(builder)
        }
    }

    private fun parsePredicateIndicator(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the predicate name (atom)
        if (isAtom(builder.tokenType)) {
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)
        } else {
            builder.error("Expected atom")
            builder.advanceLexer()
        }

        // Parse the '/' separator
        expectToken(builder, PicatTokenTypes.DIVIDE, "Expected '/'")

        // Parse the arity (integer)
        if (builder.tokenType == PicatTokenTypes.INTEGER) {
            builder.advanceLexer()
        } else {
            builder.error("Expected integer")
        }

        marker.done(PicatTokenTypes.PREDICATE_INDICATOR)
    }

    private fun parseImportList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseModuleSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseModuleSpec(builder)
        }

        marker.done(PicatTokenTypes.IMPORT_LIST)
    }

    private fun parseModuleSpec(builder: PsiBuilder) {
        parseModuleName(builder)

        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseRenameList(builder)
        }
    }

    private fun parseRenameList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseRenameSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseRenameSpec(builder)
        }

        marker.done(PicatTokenTypes.RENAME_LIST)
    }

    private fun parseRenameSpec(builder: PsiBuilder) {
        parseAtom(builder)

        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseAtom(builder)
        }
    }

    // Goal parsing methods
    private fun parseNegation(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.NOT_KEYWORD, "Expected 'not'")
        parseGoal(builder)
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseFail(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.FAIL_KEYWORD, "Expected 'fail'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseTrue(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.TRUE_KEYWORD, "Expected 'true'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseFalse(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.FALSE_KEYWORD, "Expected 'false'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseCut(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.CUT, "Expected '!'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseReturn(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.RETURN_KEYWORD, "Expected 'return'")
        parseExpression(builder)
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseContinue(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.CONTINUE_KEYWORD, "Expected 'continue'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseBreak(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.BREAK_KEYWORD, "Expected 'break'")
        marker.done(PicatTokenTypes.GOAL)
    }

    private fun parseThrow(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.THROW_KEYWORD, "Expected 'throw'")
        parseExpression(builder)
        marker.done(PicatTokenTypes.THROW_STATEMENT)
    }

    private fun parseAssignment(builder: PsiBuilder) {
        val marker = builder.mark()
        parseVariable(builder)
        expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':='")
        parseExpression(builder)
        marker.done(PicatTokenTypes.GOAL)
    }

    // Control structure parsing methods
    private fun parseForeachGenerators(builder: PsiBuilder) {
        val marker = builder.mark()
        parseForeachGenerator(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseForeachGenerator(builder)
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATORS)
    }

    private fun parseForeachGenerator(builder: PsiBuilder) {
        val marker = builder.mark()
        parseVariable(builder)

        if (builder.tokenType == PicatTokenTypes.IN_KEYWORD) {
            builder.advanceLexer()
            parseExpression(builder)
        } else if (builder.tokenType == PicatTokenTypes.EQUAL) {
            builder.advanceLexer()
            parseExpression(builder)
        } else {
            builder.error("Expected 'in' or '='")
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATOR)
    }

    private fun parseCaseArms(builder: PsiBuilder) {
        val marker = builder.mark()
        parseCaseArm(builder)

        while (builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseCaseArm(builder)
        }

        marker.done(PicatTokenTypes.CASE_ARMS)
    }

    private fun parseCaseArm(builder: PsiBuilder) {
        val marker = builder.mark()
        parsePattern(builder)
        expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        parseBody(builder)
        marker.done(PicatTokenTypes.CASE_ARM)
    }

    private fun parsePattern(builder: PsiBuilder) {
        val marker = builder.mark()

        when {
            isVariable(builder.tokenType) -> parseVariable(builder)
            isAtom(builder.tokenType) -> parseAtom(builder)
            isNumber(builder.tokenType) -> parseNumber(builder)
            builder.tokenType == PicatTokenTypes.ANONYMOUS_VARIABLE -> builder.advanceLexer()
            builder.tokenType == PicatTokenTypes.LBRACKET -> parseListPattern(builder)
            builder.tokenType == PicatTokenTypes.LBRACE -> parseTuplePattern(builder)
            isStructure(builder) -> parseStructurePattern(builder)
            else -> {
                builder.error("Expected pattern")
                builder.advanceLexer()
            }
        }

        marker.done(PicatTokenTypes.PATTERN)
    }

    private fun parseStructurePattern(builder: PsiBuilder) {
        val marker = builder.mark()
        parseAtom(builder)
        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parsePatternList(builder)
        }

        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE_PATTERN)
    }

    private fun parseListPattern(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACKET, "Expected '['")

        if (builder.tokenType != PicatTokenTypes.RBRACKET) {
            parsePatternList(builder)

            if (builder.tokenType == PicatTokenTypes.PIPE) {
                builder.advanceLexer()
                parsePattern(builder)
            }
        }

        expectToken(builder, PicatTokenTypes.RBRACKET, "Expected ']'")
        marker.done(PicatTokenTypes.LIST_PATTERN)
    }

    private fun parseTuplePattern(builder: PsiBuilder) {
        val marker = builder.mark()
        expectToken(builder, PicatTokenTypes.LBRACE, "Expected '{'")

        if (builder.tokenType != PicatTokenTypes.RBRACE) {
            parsePatternList(builder)
        }

        expectToken(builder, PicatTokenTypes.RBRACE, "Expected '}'")
        marker.done(PicatTokenTypes.TUPLE_PATTERN)
    }

    private fun parsePatternList(builder: PsiBuilder) {
        val marker = builder.mark()
        parsePattern(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parsePattern(builder)
        }

        marker.done(PicatTokenTypes.PATTERN_LIST)
    }

    private fun parseCatchClauses(builder: PsiBuilder) {
        val marker = builder.mark()
        parseCatchClause(builder)

        while (builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            parseCatchClause(builder)
        }

        marker.done(PicatTokenTypes.CATCH_CLAUSES)
    }

    private fun parseCatchClause(builder: PsiBuilder) {
        val marker = builder.mark()
        parsePattern(builder)
        expectToken(builder, PicatTokenTypes.ARROW_OP, "Expected '=>'")
        parseBody(builder)
        marker.done(PicatTokenTypes.CATCH_CLAUSE)
    }
}
