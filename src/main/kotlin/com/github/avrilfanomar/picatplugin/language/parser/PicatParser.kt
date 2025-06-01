package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

/**
 * Main parser for Picat language.
 * Delegates parsing responsibilities to specialized parser components.
 */
class PicatParser : PsiParser {
    // Parser context and components
    private val parserContext: PicatParserContext
    private val expressionParser: PicatExpressionParser
    private val patternParser: PicatPatternParser
    private val statementParser: PicatStatementParser
    private val moduleParser: PicatModuleParser
    private val definitionParser: PicatDefinitionParser
    private val commentParser: PicatCommentParser
    private val topLevelParser: PicatTopLevelParser

    init {
        // Use factory to create and initialize parser components
        val factory = PicatParserFactory()
        parserContext = factory.createParserComponents()

        // Get references to the components we need directly
        expressionParser = parserContext.expressionParser
        patternParser = parserContext.patternParser
        statementParser = parserContext.statementParser
        moduleParser = parserContext.moduleParser
        definitionParser = parserContext.definitionParser
        commentParser = parserContext.commentParser
        topLevelParser = parserContext.topLevelParser
    }

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        // Parse the entire file content
        while (!builder.eof()) {
            when (builder.tokenType) {
                PicatTokenTypes.WHITE_SPACE -> builder.advanceLexer()
                PicatTokenTypes.COMMENT,
                PicatTokenTypes.MULTI_LINE_COMMENT -> commentParser.parseAnyComment(builder)
                PicatTokenTypes.DOT -> {
                    // Skip dots that are not part of a definition
                    builder.advanceLexer()
                }
                else -> topLevelParser.parseTopLevelItem(builder)
            }
        }

        rootMarker.done(root)
        return builder.treeBuilt
    }

}
