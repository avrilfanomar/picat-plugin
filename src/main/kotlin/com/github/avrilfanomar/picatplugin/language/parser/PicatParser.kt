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

        if (builder.tokenType == PicatTokenTypes.STRING || builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
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
        val marker = builder.mark()

        when {
            isFunctionDefinition(builder) -> parseFunctionDefinition(builder)
            isRuleDefinition(builder) -> parseRuleDefinition(builder)
            isPredicateDefinition(builder) -> parsePredicateDefinition(builder)
            isFact(builder) -> parseFact(builder)
            else -> {
                builder.error("Unexpected definition")
                builder.advanceLexer()
                marker.drop()
                return
            }
        }

        marker.done(PicatTokenTypes.DEFINITION)
    }

    private fun parseFunctionDefinition(builder: PsiBuilder) {
        parseHead(builder)

        expectToken(builder, PicatTokenTypes.ASSIGN_OP, "Expected ':=' in function definition")

        parseExpression(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after function definition")
    }

    private fun parseRuleDefinition(builder: PsiBuilder) {
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
    }

    private fun parsePredicateDefinition(builder: PsiBuilder) {
        parseHead(builder)

        // Optional body
        if (builder.tokenType != PicatTokenTypes.DOT) {
            parseBody(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after predicate definition")
    }

    private fun parseFact(builder: PsiBuilder) {
        parseHead(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after fact")
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
        result = builder.tokenType == PicatTokenTypes.ASSIGN_OP
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
        result = builder.tokenType != PicatTokenTypes.DOT
        marker.rollbackTo()

        return result
    }

    private fun isFact(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        parseHead(builder)
        result = builder.tokenType == PicatTokenTypes.DOT
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
}
