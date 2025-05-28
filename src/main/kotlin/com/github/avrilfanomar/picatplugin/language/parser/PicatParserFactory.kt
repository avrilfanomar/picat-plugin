package com.github.avrilfanomar.picatplugin.language.parser

/**
 * Factory class for creating and initializing parser components.
 * This class follows the Factory pattern to create and configure parser components.
 */
class PicatParserFactory {
    /**
     * Creates and initializes all parser components.
     * @return A context containing all initialized parser components.
     */
    fun createParserComponents(): PicatParserContext {
        // Create parser components
        val expressionParser = PicatExpressionParser()
        val patternParser = PicatPatternParser()
        val statementParser = PicatStatementParser()
        val moduleParser = PicatModuleParser()
        val definitionParser = PicatDefinitionParser()
        val commentParser = PicatCommentParser()
        val topLevelParser = PicatTopLevelParser()

        // Create and set up parser context
        val parserContext = PicatParserContext()
        parserContext.expressionParser = expressionParser
        parserContext.patternParser = patternParser
        parserContext.statementParser = statementParser
        parserContext.moduleParser = moduleParser
        parserContext.definitionParser = definitionParser
        parserContext.commentParser = commentParser
        parserContext.topLevelParser = topLevelParser

        // Initialize parser components with the context
        expressionParser.initialize(parserContext)
        patternParser.initialize(parserContext)
        statementParser.initialize(parserContext)
        moduleParser.initialize(parserContext)
        definitionParser.initialize(parserContext)
        commentParser.initialize(parserContext)
        topLevelParser.initialize(parserContext)

        return parserContext
    }
}