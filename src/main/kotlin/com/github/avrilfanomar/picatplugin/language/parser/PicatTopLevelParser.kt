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
            PicatTokenTypes.TABLE_KEYWORD, PicatTokenTypes.INDEX_KEYWORD -> moduleParser.parseCompilationDirective(builder) // Removed level
            // PRIVATE_KEYWORD could start a compilation_directive or actor_definition.
            // This requires more sophisticated lookahead or trying.
            // For now, assume if PRIVATE_KEYWORD is followed by TABLE/INDEX, it's a compilation_directive.
            // Otherwise, it might be an actor or a private predicate/function.
            PicatTokenTypes.PRIVATE_KEYWORD -> {
                val lookaheadMarker = builder.mark()
                builder.advanceLexer() // Consume PRIVATE_KEYWORD
                val nextToken = builder.tokenType
                lookaheadMarker.rollbackTo()

                if (nextToken == PicatTokenTypes.TABLE_KEYWORD || nextToken == PicatTokenTypes.INDEX_KEYWORD) {
                    moduleParser.parseCompilationDirective(builder) // Removed level
                } else if (isLikelyActorDefinition(builder)) { // isLikelyActorDefinition is a new lookahead needed
                    parseActorDefinition(builder) // Removed level
                }
                else {
                    // Assuming private predicate/function, handled by parseDefinition
                    // parseDefinition would also need to handle an optional PRIVATE_KEYWORD
                    definitionParser.parseDefinition(builder)
                }
            }
            // Heuristic: if it's an atom not followed by typical predicate/function operators, try actor.
            // This is very simplified and error-prone without proper lookahead for distinguishing actors.
            else if (isLikelyActorDefinition(builder)) {
                 parseActorDefinition(builder) // Removed level
            }
            else -> definitionParser.parseDefinition(builder)
        }
    }

    // ACTOR_DEFINITION ::= [PRIVATE_KEYWORD] actor_name EOR (actor_member EOR)* END_KEYWORD EOR
    fun parseActorDefinition(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ActorDefinition")) return false // Temp removed

        val marker = builder.mark()
        var result = true

        if (builder.tokenType == PicatTokenTypes.PRIVATE_KEYWORD) {
            builder.advanceLexer()
        }

        result = result && parseActorName(builder) // Removed level
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after actor name")

        while (result && builder.tokenType != PicatTokenTypes.END_KEYWORD && !builder.eof()) {
            val memberMarker = builder.mark()
            if (parseActorMember(builder)) { // Removed level
                result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after actor member")
                memberMarker.drop()
            } else {
                // Error or unexpected token, break loop
                memberMarker.drop()
                builder.error("Expected actor member or 'end'")
                result = false; // Stop parsing members
                break;
            }
        }

        result = result && PicatParserUtil.expectKeyword(builder, PicatTokenTypes.END_KEYWORD, "Expected 'end' for actor definition")
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.EOR, "Expected EOR after actor 'end'")

        if (result) {
            marker.done(PicatTokenTypes.ACTOR_DEFINITION)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // ACTOR_NAME ::= atom
    fun parseActorName(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ActorName")) return false // Temp removed
        val marker = builder.mark()
        // expressionParser.parseAtom now takes only builder and returns boolean
        val result = expressionParser.parseAtom(builder) // Removed level
        if (result) {
            marker.done(PicatTokenTypes.ACTOR_NAME)
        } else {
            builder.error("Expected atom for actor name")
            marker.rollbackTo()
        }
        return result
    }

    // ACTOR_MEMBER ::= action_rule | predicate_clause | function_clause | compilation_directive
    fun parseActorMember(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ActorMember")) return false // Temp removed

        val marker = builder.mark()
        var result = false

        // This requires lookahead to distinguish.
        // For simplicity, trying in an order. This is not robust.
        if (isActionRule(builder)) { // isActionRule would check for head then HASH_ARROW_OP
            result = parseActionRule(builder) // Removed level
        } else if (isCompilationDirective(builder)) { // checks for TABLE_KEYWORD or INDEX_KEYWORD or PRIVATE_KEYWORD + TABLE/INDEX
             result = moduleParser.parseCompilationDirective(builder) // Removed level
        } else if (definitionParser.isFunctionClause(builder)) { // Removed level, assuming updated
             result = definitionParser.parseFunctionClause(builder) // Removed level
        } else if (definitionParser.isPredicateClause(builder)) { // Removed level, assuming updated
             result = definitionParser.parsePredicateClause(builder) // Removed level
        } else {
            builder.error("Expected action rule, predicate/function clause, or compilation directive")
            marker.drop() // Drop marker as no valid alternative found
            return false
        }

        if (result) {
            marker.done(PicatTokenTypes.ACTOR_MEMBER)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // ACTION_RULE ::= head HASH_ARROW_OP body
    fun parseActionRule(builder: PsiBuilder): Boolean { // Removed level
        // if (!GeneratedParserUtilBase.recursion_guard_(builder, level, "ActionRule")) return false // Temp removed
        // Basic lookahead: check if current structure looks like a head.
        // A more robust lookahead would check for HASH_ARROW_OP after the head.
        // if (!definitionParser.isHead(builder)) return false // Assuming isHead lookahead - isHead does not exist
        // For now, proceed without this specific lookahead, relying on parseHead to handle errors.

        val marker = builder.mark()
        var result = definitionParser.parseHead(builder) // Use boolean returning parseHead
        result = result && PicatParserUtil.expectToken(builder, PicatTokenTypes.HASH_ARROW_OP, "Expected '#=>' for action rule")
        result = result && statementParser.parseBody(builder, 0) // Pass 0 or appropriate default

        if (result) {
            marker.done(PicatTokenTypes.ACTION_RULE)
        } else {
            marker.rollbackTo()
        }
        return result
    }

    // Simplified lookahead functions (these would need proper implementation)
    private fun isLikelyActorDefinition(builder: PsiBuilder): Boolean {
        // This is a placeholder. A real lookahead would be needed to differentiate
        // 'atom EOR' (potential actor start) from 'atom EOR' (fact) or 'atom RULE_OP ...' (rule)
        // or 'atom ASSIGN_OP ...' (function).
        // It might check for a sequence that doesn't match other top-level items.
        // For now, make it very restrictive or always false to avoid breaking existing parsing.
        return false // Disable for now to avoid breaking existing definition parsing
    }

    private fun isActionRule(builder: PsiBuilder): Boolean {
        // Placeholder: A real lookahead would parse a head and check if HASH_ARROW_OP follows.
        val marker = builder.mark()
        var isAction = false
        // definitionParser.parseHead returns Unit, so cannot use in if condition directly.
        // Use parseHeadAndReturnBool or a dedicated lookahead version of parseHead.
        // For now, let's assume a simplified check or that parseHeadAndReturnBool is suitable for lookahead.
        if (definitionParser.parseHeadAndReturnBool(builder)) { // Try parsing a head
            if (builder.tokenType == PicatTokenTypes.HASH_ARROW_OP) {
                isAction = true
            }
        }
        marker.rollbackTo()
        return isAction
    }

    private fun isCompilationDirective(builder: PsiBuilder): Boolean {
        if (builder.tokenType == PicatTokenTypes.TABLE_KEYWORD || builder.tokenType == PicatTokenTypes.INDEX_KEYWORD) return true
        if (builder.tokenType == PicatTokenTypes.PRIVATE_KEYWORD) {
            val marker = builder.mark()
            builder.advanceLexer()
            val result = builder.tokenType == PicatTokenTypes.TABLE_KEYWORD || builder.tokenType == PicatTokenTypes.INDEX_KEYWORD
            marker.rollbackTo()
            return result
        }
        return false
    }
}
