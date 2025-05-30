package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.skipWhitespace
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder

/**
 * Base class for all Picat parser components.
 * Provides common functionality and access to other parser components.
 */
abstract class PicatBaseParser : PicatParserComponent {
    // References to other parser components through context
    protected lateinit var context: PicatParserContext

    // Lazy properties to ensure context is initialized before access
    protected val moduleParser: PicatModuleParser
        get() = context.moduleParser
    protected val definitionParser: PicatDefinitionParser
        get() = context.definitionParser
    protected val expressionParser: PicatExpressionParser
        get() = context.expressionParser
    protected val statementParser: PicatStatementParser
        get() = context.statementParser
    protected val patternParser: PicatPatternParser
        get() = context.patternParser

    /**
     * Initialize this parser with references to other parser components.
     */
    override fun initialize(parserContext: PicatParserContext) {
        this.context = parserContext
    }

    protected fun parseAtom(builder: PsiBuilder) {
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
    }

    protected fun parseStructure(builder: PsiBuilder) {
        val marker = builder.mark()
        if (isQualifiedAtom(builder)) {
            parseQualifiedAtom(builder)
        }
        if (isAtom(builder.tokenType)) {
            parseAtom(builder)
        }
        skipWhitespace(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        skipWhitespace(builder)

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parseArgumentList(builder)
        }

        PicatParserUtil.expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE)
    }

    protected fun parseQualifiedAtom(builder: PsiBuilder) {
        val marker = builder.mark()
        parseAtom(builder)
        PicatParserUtil.expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")
        skipWhitespace(builder)
        parseAtom(builder)
        marker.done(PicatTokenTypes.ATOM_NO_ARGS)
    }

    protected fun parseVariable(builder: PsiBuilder) {
        if (PicatParserUtil.isVariable(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected variable")
        }
    }

    protected fun parseNumber(builder: PsiBuilder) {
        if (PicatParserUtil.isNumber(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected number")
        }
    }

    protected fun parseString(builder: PsiBuilder) {
        if (builder.tokenType == PicatTokenTypes.STRING) {
            builder.advanceLexer()
        } else {
            builder.error("Expected string")
        }
    }

    protected fun parseArgumentList(builder: PsiBuilder) {
        val marker = builder.mark()
        expressionParser.parseExpression(builder)
        skipWhitespace(builder)

        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            skipWhitespace(builder)
            expressionParser.parseExpression(builder)
        }

        marker.done(PicatTokenTypes.ARGUMENT_LIST)
    }

    // Structure checking methods
    protected fun isStructure(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        var result = false

        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
            result = builder.tokenType == PicatTokenTypes.LPAR
        }

        marker.rollbackTo()
        return result
    }

    protected fun isQualifiedAtom(builder: PsiBuilder): Boolean {
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

    protected fun isAssignment(builder: PsiBuilder): Boolean {
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
