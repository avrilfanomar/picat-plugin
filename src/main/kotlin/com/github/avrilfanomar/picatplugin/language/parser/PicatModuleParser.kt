package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing Picat module-related constructs.
 */
class PicatModuleParser : PicatBaseParser() {

    /**
     * Parses a module declaration.
     */
    fun parseModuleDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.MODULE_KEYWORD, "Expected 'module'")

        parseModuleName(builder)

        // Optional export clause
        if (builder.tokenType == PicatTokenTypes.EXPORT_KEYWORD) {
            parseExportClause(builder)
        }

        // Optional import clause
        if (builder.tokenType == PicatTokenTypes.IMPORT_KEYWORD) {
            parseImportClause(builder)
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
        parseImportList(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after import statement")
        marker.done(PicatTokenTypes.IMPORT_STATEMENT)
    }

    /**
     * Parses an export statement.
     */
    fun parseExportStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")

        parseExportList(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after export statement")
        marker.done(PicatTokenTypes.EXPORT_STATEMENT)
    }

    /**
     * Parses an include statement.
     */
    fun parseIncludeStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.INCLUDE_KEYWORD, "Expected 'include'")

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

        parseModuleName(builder)

        // Optional rename list
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseRenameList(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.' after using statement")
        marker.done(PicatTokenTypes.USING_STATEMENT)
    }

    /**
     * Parses an export clause in a module declaration.
     */
    private fun parseExportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        parseExportList(builder)
        marker.done(PicatTokenTypes.EXPORT_CLAUSE)
    }

    /**
     * Parses an import clause in a module declaration.
     */
    private fun parseImportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        parseImportList(builder)
        marker.done(PicatTokenTypes.IMPORT_CLAUSE)
    }

    /**
     * Parses an export list.
     */
    private fun parseExportList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseExportSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseExportSpec(builder)
        }

        marker.done(PicatTokenTypes.EXPORT_LIST)
    }

    /**
     * Parses an export specification.
     */
    private fun parseExportSpec(builder: PsiBuilder) {
        if (builder.lookAhead(1) == PicatTokenTypes.DIVIDE || builder.lookAhead(1) == PicatTokenTypes.INTEGER) {
            parsePredicateIndicator(builder)
        } else {
            parseAtom(builder)
        }
    }

    /**
     * Parses a predicate indicator (name/arity).
     */
    private fun parsePredicateIndicator(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the predicate name (atom)
        if (PicatParserUtil.isAtom(builder.tokenType)) {
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)
        } else {
            builder.error("Expected atom")
            builder.advanceLexer()
        }

        // Parse the '/' separator
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DIVIDE, "Expected '/'")

        // Parse the arity (integer)
        if (builder.tokenType == PicatTokenTypes.INTEGER) {
            builder.advanceLexer()
        } else {
            builder.error("Expected integer")
        }

        marker.done(PicatTokenTypes.PREDICATE_INDICATOR)
    }

    /**
     * Parses an import list.
     */
    private fun parseImportList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseModuleSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseModuleSpec(builder)
        }

        marker.done(PicatTokenTypes.IMPORT_LIST)
    }

    /**
     * Parses a module specification.
     */
    private fun parseModuleSpec(builder: PsiBuilder) {
        val marker = builder.mark()
        parseModuleName(builder)

        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseRenameList(builder)
        }

        marker.done(PicatTokenTypes.MODULE_SPEC)
    }

    /**
     * Parses a rename list.
     */
    private fun parseRenameList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseRenameSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseRenameSpec(builder)
        }

        marker.done(PicatTokenTypes.RENAME_LIST)
    }

    /**
     * Parses a rename specification.
     */
    private fun parseRenameSpec(builder: PsiBuilder) {
        parseAtom(builder)

        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            parseAtom(builder)
        }
    }
}
