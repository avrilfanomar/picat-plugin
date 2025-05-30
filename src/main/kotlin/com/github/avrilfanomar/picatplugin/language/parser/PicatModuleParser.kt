package com.github.avrilfanomar.picatplugin.language.parser

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
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.MODULE_KEYWORD, "Expected 'module'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseModuleName(builder)

        // Optional export clause
        if (builder.tokenType == PicatTokenTypes.EXPORT_KEYWORD) {
            helper.parseExportClause(builder)
        }

        // Optional import clause
        if (builder.tokenType == PicatTokenTypes.IMPORT_KEYWORD) {
            helper.parseImportClause(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after module declaration")
        marker.done(PicatTokenTypes.MODULE_DECLARATION)
    }

    /**
     * Parses an end_module declaration.
     */
    fun parseEndModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_MODULE_KEYWORD, "Expected 'end_module'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Optional module name
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER) {
            parseModuleName(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after end_module")
        marker.done(PicatTokenTypes.END_MODULE_DECLARATION)
    }

    /**
     * Parses a module name.
     */
    fun parseModuleName(builder: PsiBuilder) {
        val marker = builder.mark()
        if (builder.tokenType == PicatTokenTypes.IDENTIFIER || builder.tokenType == PicatTokenTypes.QUOTED_ATOM) {
            // Create an ATOM element
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)

            marker.done(PicatTokenTypes.MODULE_NAME)
        } else {
            marker.drop()
            builder.error("Expected module name")
        }
    }

    /**
     * Parses an import statement.
     */
    fun parseImportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        helper.parseImportList(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after import statement")
        marker.done(PicatTokenTypes.IMPORT_STATEMENT)
    }

    /**
     * Parses an export statement.
     */
    fun parseExportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        helper.parseExportList(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after export statement")
        marker.done(PicatTokenTypes.EXPORT_STATEMENT)
    }

    /**
     * Parses an include statement.
     */
    fun parseIncludeStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.INCLUDE_KEYWORD, "Expected 'include'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse file_spec (STRING or atom)
        if (builder.tokenType == PicatTokenTypes.STRING) {
            // Store the string value for later retrieval
            val stringMarker = builder.mark()
            builder.advanceLexer()
            stringMarker.done(PicatTokenTypes.STRING)
        } else if (PicatParserUtil.isAtom(builder.tokenType)) {
            parseAtom(builder)
        } else {
            builder.error("Expected file path string or atom")
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after include statement")
        marker.done(PicatTokenTypes.INCLUDE_STATEMENT)
    }

    /**
     * Parses a using statement.
     */
    fun parseUsingStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.USING_KEYWORD, "Expected 'using'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        parseModuleName(builder)

        // Optional rename list
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            helper.parseRenameList(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after using statement")
        marker.done(PicatTokenTypes.USING_STATEMENT)
    }

}
