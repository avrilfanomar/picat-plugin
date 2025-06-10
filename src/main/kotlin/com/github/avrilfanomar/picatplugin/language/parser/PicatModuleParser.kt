package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectKeyword
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat module-related constructs.
 */
class PicatModuleParser : PicatBaseParser() {
    // Helper for parsing import/export related constructs
    private lateinit var helper: PicatModuleParserHelper

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        super.initialize(parserContext)
        helper = PicatModuleParserHelper()
        helper.initialize(parserContext)
    }

    /**
     * Parses a module declaration.
     */
    fun parseModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.MODULE_KEYWORD, "Expected 'module'")
        skipWhitespace(builder)

        parseModuleName(builder)
        skipWhitespace(builder)

        // Optional export clause, as per subtask guidance and BNF
        if (builder.tokenType == PicatTokenTypes.EXPORT_KEYWORD) {
            helper.parseExportClause(builder)
            skipWhitespace(builder)
        }

        // Optional import clause, as per subtask guidance and BNF
        if (builder.tokenType == PicatTokenTypes.IMPORT_KEYWORD) {
            helper.parseImportClause(builder)
            skipWhitespace(builder)
        }

        expectToken(builder, PicatTokenTypes.EOR, "Expected EOR (e.g., '.') after module declaration")
        marker.done(PicatTokenTypes.MODULE_DECLARATION)
    }

    /**
     * Parses an end_module declaration.
     */
    fun parseEndModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.END_MODULE_KEYWORD, "Expected 'end_module'")
        skipWhitespace(builder)

        // Optional module name
        if (isAtom(builder.tokenType)) { // Use isAtom for check
            parseModuleName(builder) // Use parseModuleName for consistency
        }
        skipWhitespace(builder) // Ensure whitespace is skipped before EOR

        expectToken(builder, PicatTokenTypes.EOR, "Expected EOR (e.g., '.') after end_module declaration") // Use EOR
        marker.done(PicatTokenTypes.END_MODULE_DECLARATION)
    }

    /**
     * Parses a module name.
     */
    fun parseModuleName(builder: PsiBuilder) {
        val moduleNameMarker = builder.mark()
        if (isAtom(builder.tokenType)) { // Use PicatParserUtil.isAtom for check
            // The IDENTIFIER or QUOTED_ATOM token itself will be the content of MODULE_NAME.
            builder.advanceLexer() // Consume the atom token.
            moduleNameMarker.done(PicatTokenTypes.MODULE_NAME)
        } else {
            moduleNameMarker.drop() // Drop marker if no valid module name found
            builder.error("Expected module name")
        }
    }

    /**
     * Parses an import statement.
     */
    fun parseImportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        skipWhitespace(builder)
        helper.parseImportList(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after import statement")
        marker.done(PicatTokenTypes.IMPORT_STATEMENT)
    }

    /**
     * Parses an export statement.
     */
    fun parseExportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        skipWhitespace(builder)
        helper.parseExportList(builder)
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after export statement")
        marker.done(PicatTokenTypes.EXPORT_STATEMENT)
    }

    /**
     * Parses an include statement.
     */
    fun parseIncludeStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.INCLUDE_KEYWORD, "Expected 'include'")
        skipWhitespace(builder)

        // Parse file_spec (STRING or atom)
        if (builder.tokenType == PicatTokenTypes.STRING) {
            // Store the string value for later retrieval
            val stringMarker = builder.mark()
            builder.advanceLexer()
            stringMarker.done(PicatTokenTypes.STRING)
        } else if (isAtom(builder.tokenType)) {
            if (isAtom(builder.tokenType)) {
                builder.advanceLexer()
            } else {
                builder.error("Expected atom")
            }
        } else {
            builder.error("Expected file path string or atom")
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after include statement")
        marker.done(PicatTokenTypes.INCLUDE_STATEMENT)
    }

    /**
     * Parses a using statement.
     */
    fun parseUsingStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.USING_KEYWORD, "Expected 'using'")
        skipWhitespace(builder)

        parseModuleName(builder)

        // Optional rename list
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            skipWhitespace(builder)
            helper.parseRenameList(builder)
        }

        expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after using statement")
        marker.done(PicatTokenTypes.USING_STATEMENT)
    }

    // COMPILATION_DIRECTIVE ::= [PRIVATE_KEYWORD] (table_mode | index_mode) EOR
    fun parseCompilationDirective(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "CompilationDirective")) return false // Temp removed

        val marker = builder.mark()
        var result = true
        var pinned = false

        if (builder.tokenType == PicatTokenTypes.PRIVATE_KEYWORD) {
            builder.advanceLexer()
        }

        val choiceMarker = builder.mark()
        if (builder.tokenType == PicatTokenTypes.TABLE_KEYWORD) {
            result = parseTableMode(builder) // Removed level
            pinned = true
            choiceMarker.drop()
        } else if (builder.tokenType == PicatTokenTypes.INDEX_KEYWORD) {
            result = parseIndexMode(builder) // Removed level
            pinned = true
            choiceMarker.drop()
        } else {
            choiceMarker.drop()
            builder.error("Expected 'table' or 'index' keyword")
            result = false
        }

        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after compilation directive")

        if (result) {
            marker.done(PicatTokenTypes.COMPILATION_DIRECTIVE)
        } else {
            // If pinned and failed, error should have been reported by sub-parser or expectToken
            // If not pinned and failed (e.g. no table/index keyword), rollback.
            if (!pinned) marker.rollbackTo() else marker.drop()
        }
        return result
    }

    // TABLE_MODE ::= TABLE_KEYWORD head_reference_list
    fun parseTableMode(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "TableMode")) return false // Temp removed
        if (builder.tokenType != PicatTokenTypes.TABLE_KEYWORD) return false

        val marker = builder.mark()
        var result = PicatParserUtil.expectKeyword(builder, PicatTokenTypes.TABLE_KEYWORD, "Expected 'table'")
        result = result && parseHeadReferenceList(builder) // Removed level

        if (result) {
            marker.done(PicatTokenTypes.TABLE_MODE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // INDEX_MODE ::= INDEX_KEYWORD head_reference_list [indexing_details]
    fun parseIndexMode(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "IndexMode")) return false // Temp removed
        if (builder.tokenType != PicatTokenTypes.INDEX_KEYWORD) return false

        val marker = builder.mark()
        var result = PicatParserUtil.expectKeyword(builder, PicatTokenTypes.INDEX_KEYWORD, "Expected 'index'")
        result = result && parseHeadReferenceList(builder) // Removed level

        if (builder.tokenType == PicatTokenTypes.LPAR) { // Optional indexing_details
            result = result && parseIndexingDetails(builder) // Removed level
        }

        if (result) {
            marker.done(PicatTokenTypes.INDEX_MODE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // HEAD_REFERENCE_LIST ::= head_reference ((COMMA | SEMICOLON) head_reference)*
    fun parseHeadReferenceList(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "HeadReferenceList")) return false // Temp removed

        val marker = builder.mark()
        var result = parseHeadReference(builder) // Removed level

        while (result && (builder.tokenType == PicatTokenTypes.COMMA || builder.tokenType == PicatTokenTypes.SEMICOLON)) {
            builder.advanceLexer() // Consume COMMA or SEMICOLON
            result = result && parseHeadReference(builder) // Removed level
        }

        if (result) {
            marker.done(PicatTokenTypes.HEAD_REFERENCE_LIST)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // HEAD_REFERENCE ::= atom ('/' INTEGER)?
    fun parseHeadReference(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "HeadReference")) return false // Temp removed
        if (!isAtom(builder.tokenType)) return false // Basic lookahead

        val marker = builder.mark()
        // expressionParser.parseAtom does not return boolean and does not take level
        expressionParser.parseAtom(builder) // Call without level
        var result = true // Placeholder, assume atom parsing succeeded or marked error

        if (builder.tokenType == PicatTokenTypes.DIVIDE) { // '/'
            builder.advanceLexer()
            result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.INTEGER, "Expected integer for arity")
        }

        if (result) {
            marker.done(PicatTokenTypes.HEAD_REFERENCE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // INDEXING_DETAILS ::= LPAR argument_list RPAR
    fun parseIndexingDetails(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "IndexingDetails")) return false // Temp removed
        if (builder.tokenType != PicatTokenTypes.LPAR) return false

        val marker = builder.mark()
        var result = PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '(' for indexing details")
        // expressionParser.parseArgumentList (from PicatBaseParser) does not return boolean and does not take level
        expressionParser.parseArgumentList(builder) // Call without level
        // Assume it succeeded for now
        result = result && true
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')' after indexing details")

        if (result) {
            marker.done(PicatTokenTypes.INDEXING_DETAILS)
        } else {
            marker.rollbackTo()
        }
        return result
    }
}
