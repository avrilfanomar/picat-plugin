package com.github.avrilfanomar.picatplugin.language.parser

/**
 * Context class that holds references to all parser components.
 * This class helps reduce coupling between components by providing a single point of access.
 */
class PicatParserContext {
    lateinit var moduleParser: PicatModuleParser
    lateinit var definitionParser: PicatDefinitionParser
    lateinit var expressionParser: PicatExpressionParser
    lateinit var statementParser: PicatStatementParser
    lateinit var patternParser: PicatPatternParser
    lateinit var commentParser: PicatCommentParser
    lateinit var topLevelParser: PicatTopLevelParser
}
