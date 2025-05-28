package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Parser component responsible for parsing top-level items in Picat files.
 */
class PicatTopLevelParser : PicatBaseParser() {
    /**
     * Parses a top-level item based on the current token type.
     */
    fun parseTopLevelItem(builder: PsiBuilder) {
        when (builder.tokenType) {
            PicatTokenTypes.MODULE_KEYWORD -> moduleParser.parseModuleDeclaration(builder)
            PicatTokenTypes.END_MODULE_KEYWORD -> moduleParser.parseEndModuleDeclaration(builder)
            PicatTokenTypes.IMPORT_KEYWORD -> moduleParser.parseImportStatement(builder)
            PicatTokenTypes.EXPORT_KEYWORD -> moduleParser.parseExportStatement(builder)
            PicatTokenTypes.INCLUDE_KEYWORD -> moduleParser.parseIncludeStatement(builder)
            PicatTokenTypes.USING_KEYWORD -> moduleParser.parseUsingStatement(builder)
            else -> definitionParser.parseDefinition(builder)
        }
    }
}
