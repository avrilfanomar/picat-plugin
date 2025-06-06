package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.expectToken
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isAtom
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isNumber
import com.github.avrilfanomar.picatplugin.language.parser.PicatParserUtil.isVariable
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

    protected fun parseStructure(builder: PsiBuilder) {
        val marker = builder.mark()
        if (PicatTokenTypes.DATA_CONSTRUCTOR == builder.tokenType) {
            builder.advanceLexer()
        }
        if (isQualifiedAtom(builder)) {
            parseQualifiedAtom(builder)
        }
        if (isAtom(builder.tokenType)) {
            val atomMarker = builder.mark()
            builder.advanceLexer()
            atomMarker.done(PicatTokenTypes.ATOM)
        }
        skipWhitespace(builder)
        expectToken(builder, PicatTokenTypes.LPAR, "Expected '('")
        skipWhitespace(builder)

        if (builder.tokenType != PicatTokenTypes.RPAR) {
            parseArgumentList(builder)
        }

        skipWhitespace(builder)
        expectToken(builder, PicatTokenTypes.RPAR, "Expected ')'")
        marker.done(PicatTokenTypes.STRUCTURE)
    }

    protected fun parseQualifiedAtom(builder: PsiBuilder) {
        val marker = builder.mark()
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
        expectToken(builder, PicatTokenTypes.DOT, "Expected '.'")
        skipWhitespace(builder)
        if (isAtom(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected atom")
        }
        marker.done(PicatTokenTypes.ATOM_NO_ARGS)
    }

    protected fun parseVariable(builder: PsiBuilder) {
        if (isVariable(builder.tokenType)) {
            builder.advanceLexer()
        } else {
            builder.error("Expected variable")
        }
    }

    protected fun parseNumber(builder: PsiBuilder) {
        if (isNumber(builder.tokenType)) {
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

        // Parse first argument
        val argMarker = builder.mark()
        expressionParser.parseExpression(builder)
        argMarker.done(PicatTokenTypes.ARGUMENT)
        skipWhitespace(builder)

        // Parse additional arguments
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            skipWhitespace(builder)

            val nextArgMarker = builder.mark()
            expressionParser.parseExpression(builder)
            nextArgMarker.done(PicatTokenTypes.ARGUMENT)
            skipWhitespace(builder)
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

        // Check if it's a variable or a pattern
        if (isVariable(builder.tokenType) || isAtom(builder.tokenType) || isStructure(builder)) {
            // For variable or atom, advance lexer
            if (isVariable(builder.tokenType) || isAtom(builder.tokenType)) {
                builder.advanceLexer()
            } else {
                // For structure, parse it
                parseStructure(builder)
            }
            skipWhitespace(builder)
            result = builder.tokenType == PicatTokenTypes.ASSIGN_OP || builder.tokenType == PicatTokenTypes.EQUAL
        }

        marker.rollbackTo()
        return result
    }
}
