package com.github.avrilfanomar.picatplugin.language.parser

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Helper class for PicatStatementParser.
 * Contains methods for parsing specific statement types.
 */
class PicatStatementParserHelper {
    /**
     * Parses a foreach loop statement.
     */
    fun parseForeachLoop(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume the 'foreach' keyword
        builder.advanceLexer()

        // Parse the generators
        parseForeachGenerators(builder)

        // Parse the loop body
        if (builder.tokenType == PicatTokenTypes.DO_KEYWORD) {
            builder.advanceLexer()
            PicatStatementParser().parseBody(builder)
        } else {
            builder.error("Expected 'do' after foreach generators")
        }

        // Expect 'end' keyword
        if (builder.tokenType == PicatTokenTypes.END_KEYWORD) {
            builder.advanceLexer()
        } else {
            builder.error("Expected 'end' to close foreach loop")
        }

        marker.done(PicatTokenTypes.FOREACH_LOOP)
    }

    /**
     * Parses the generators in a foreach loop.
     */
    fun parseForeachGenerators(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the first generator
        parseForeachGenerator(builder)

        // Parse additional generators
        while (builder.tokenType == PicatTokenTypes.COMMA) {
            builder.advanceLexer()
            parseForeachGenerator(builder)
        }

        marker.done(PicatTokenTypes.FOREACH_GENERATORS)
    }

    /**
     * Parses a single generator in a foreach loop.
     */
    fun parseForeachGenerator(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse variable or pattern
        if (builder.tokenType == PicatTokenTypes.VARIABLE) {
            builder.advanceLexer()
        } else {
            PicatExpressionParser().parsePattern(builder)
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Expect 'in' keyword
        if (builder.tokenType == PicatTokenTypes.IN_KEYWORD) {
            builder.advanceLexer()
        } else {
            builder.error("Expected 'in' after pattern in foreach generator")
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse the expression
        PicatExpressionParser().parseExpression(builder)

        marker.done(PicatTokenTypes.FOREACH_GENERATOR)
    }

    /**
     * Parses a while loop statement.
     */
    fun parseWhileLoop(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume the 'while' keyword
        if (builder.tokenType == PicatTokenTypes.WHILE_KEYWORD) {
            builder.advanceLexer()
        } else {
            builder.error("Expected 'while'")
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse the condition in parentheses
        if (builder.tokenType == PicatTokenTypes.LPAR) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '('")
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        PicatExpressionParser().parseExpression(builder)

        if (builder.tokenType == PicatTokenTypes.RPAR) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ')'")
        }

        while (builder.tokenType == PicatTokenTypes.WHITE_SPACE) {
            builder.advanceLexer()
        }

        // Parse the loop body
        PicatStatementParser().parseBody(builder)

        // Expect 'end' keyword
        if (builder.tokenType == PicatTokenTypes.END_KEYWORD) {
            builder.advanceLexer()
        } else {
            builder.error("Expected 'end'")
        }

        marker.done(PicatTokenTypes.WHILE_LOOP)
    }

    /**
     * Parses a case expression statement.
     */
    fun parseCaseExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume the 'case' keyword
        builder.advanceLexer()

        // Parse the expression
        PicatExpressionParser().parseExpression(builder)

        // Parse the case arms
        if (builder.tokenType == PicatTokenTypes.OF_KEYWORD) {
            builder.advanceLexer()
            parseCaseArms(builder)
        } else {
            builder.error("Expected 'of' after case expression")
        }

        // Expect 'end' keyword
        if (builder.tokenType == PicatTokenTypes.END_KEYWORD) {
            builder.advanceLexer()
        } else {
            builder.error("Expected 'end' to close case expression")
        }

        marker.done(PicatTokenTypes.CASE_EXPRESSION)
    }

    /**
     * Parses the arms in a case expression.
     */
    fun parseCaseArms(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the first arm
        parseCaseArm(builder)

        // Parse additional arms
        while (builder.tokenType == PicatTokenTypes.SEMICOLON) {
            builder.advanceLexer()
            if (builder.tokenType != PicatTokenTypes.END_KEYWORD) {
                parseCaseArm(builder)
            }
        }

        marker.done(PicatTokenTypes.CASE_ARMS)
    }

    /**
     * Parses a single arm in a case expression.
     */
    fun parseCaseArm(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the pattern
        PicatExpressionParser().parsePattern(builder)

        // Expect '=>' operator
        if (builder.tokenType == PicatTokenTypes.ARROW_OP) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '=>' after pattern in case arm")
        }

        // Parse the body
        PicatStatementParser().parseBody(builder)

        marker.done(PicatTokenTypes.CASE_ARM)
    }
}
