package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectKeyword
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
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
        expectKeyword(builder, PicatTokenTypes.EXPORT_KEYWORD, "Expected 'export'")
        skipWhitespace(builder)
        parseExportList(builder)
        marker.done(PicatTokenTypes.EXPORT_CLAUSE)
    }

    /**
     * Parses an import clause in a module declaration.
     */
    fun parseImportClause(builder: PsiBuilder) {
        val marker = builder.mark()
        expectKeyword(builder, PicatTokenTypes.IMPORT_KEYWORD, "Expected 'import'")
        skipWhitespace(builder)
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
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
    }

    /**
     * Parses an import list.
     */
    fun parseImportList(builder: PsiBuilder) {
        val marker = builder.mark()
        parseModuleSpec(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            skipWhitespace(builder)
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
            skipWhitespace(builder)
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
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
            skipWhitespace(builder)
            if (isAtom(builder.tokenType)) {
                builder.advanceLexer()
            } else {
                builder.error("Expected atom")
            }
        }

        // Parse additional rename specs
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()

            if (isAtom(builder.tokenType)) {
                builder.advanceLexer()
            } else {
                builder.error("Expected atom")
            }
            if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
                builder.advanceLexer()
                skipWhitespace(builder)
                if (isAtom(builder.tokenType)) {
                    builder.advanceLexer()
                } else {
                    builder.error("Expected atom")
                }
            }
        }

        marker.done(PicatTokenTypes.RENAME_LIST)
    }
}
