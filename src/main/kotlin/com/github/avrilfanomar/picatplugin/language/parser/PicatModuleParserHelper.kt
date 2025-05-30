package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Helper class for PicatModuleParser.
 * Contains methods for parsing import/export related constructs.
 */
class PicatModuleParserHelper : PicatBaseParser() {
    /**
     * Parses an export clause in a module declaration.
     */
    fun parseExportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        parseExportList(builder)
        marker.done(PicatTokenTypes.EXPORT_CLAUSE)
    }

    /**
     * Parses an import clause in a module declaration.
     */
    fun parseImportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        PicatParserUtil.expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }
        parseImportList(builder)
        marker.done(PicatTokenTypes.IMPORT_CLAUSE)
    }

    /**
     * Parses an export list.
     */
    fun parseExportList(builder: PsiBuilder) {
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
    fun parseImportList(builder: PsiBuilder) {
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
    fun parseModuleSpec(builder: PsiBuilder) {
        val marker = builder.mark()
        context.moduleParser.parseModuleName(builder)

        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseRenameList(builder)
        }

        marker.done(PicatTokenTypes.MODULE_SPEC)
    }

    /**
     * Parses a rename list with one or more rename specifications.
     * Each rename specification is an atom optionally followed by an arrow and another atom.
     */
    fun parseRenameList(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse first rename spec
        parseAtom(builder)
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                builder.advanceLexer()
            }
            parseAtom(builder)
        }

        // Parse additional rename specs
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()

            parseAtom(builder)
            if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
                builder.advanceLexer()
                while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
                    builder.advanceLexer()
                }
                parseAtom(builder)
            }
        }

        marker.done(PicatTokenTypes.RENAME_LIST)
    }
}
