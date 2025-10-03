package com.github.avrilfanomar.picatplugin.language.completion

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

/**
 * Completion contributor for Picat language.
 * Provides code completion for keywords, built-in functions, and constants.
 */
class PicatCompletionContributor : CompletionContributor() {

    init {
        // Add completion for all Picat language elements
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(PicatLanguage),
            PicatCompletionProvider()
        )
    }

    private class PicatCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            // Add local symbols first
            addLocalSymbols(parameters, result)
            
            // Add keywords
            addKeywords(result)

            // Add built-in functions
            addBuiltInFunctions(result)

            // Add built-in constants
            addBuiltInConstants(result)
        }

        /**
         * Add local symbols (predicates/functions) with arity hints (P1 requirement).
         * Prioritizes local symbols for context-aware completion.
         */
        private fun addLocalSymbols(parameters: CompletionParameters, result: CompletionResultSet) {
            val file = parameters.originalFile
            val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java)
            
            // Group heads by name and collect their arities
            val symbolMap = mutableMapOf<String, MutableSet<Int>>()
            heads.forEach { head ->
                val atomName = head.atom.text
                if (!atomName.isNullOrBlank()) {
                    val arity = head.headArgs?.argumentList?.size ?: 0
                    symbolMap.getOrPut(atomName) { mutableSetOf() }.add(arity)
                }
            }
            
            // Add completion items with arity information
            symbolMap.forEach { (name, arities) ->
                arities.forEach { arity ->
                    result.addElement(
                        LookupElementBuilder.create(name)
                            .withTypeText("local predicate/function")
                            .withTailText("/$arity", true) // P1: include arity hints
                    )
                }
            }
        }

        private fun addKeywords(result: CompletionResultSet) {
            val keywords = listOf(
                "module", "import", "include", "private", "table", "index",
                "if", "then", "elseif", "else", "end", "foreach", "in",
                "while", "loop", "try", "catch", "finally", "not",
                "div", "mod", "rem", "lambda", "cardinality", "fail",
                "repeat", "until"
            )

            keywords.forEach { keyword ->
                result.addElement(
                    LookupElementBuilder.create(keyword)
                        .withTypeText("keyword")
                        .withBoldness(true)
                )
            }
        }

        private fun addBuiltInFunctions(result: CompletionResultSet) {
            val builtInFunctions = listOf(
                // List operations
                "length", "append", "sort", "member", "reverse", "flatten",

                // Math operations
                "max", "min", "abs", "sqrt", "sin", "cos", "tan",

                // Map operations
                "new_map", "put", "get", "keys", "values", "has_key",

                // I/O operations
                "println", "print", "read", "write",

                // String operations
                "to_string", "to_atom", "atom_length", "atom_concat",

                // Type checking
                "var", "nonvar", "atom", "number", "integer", "float",
                "compound", "list", "is_list",

                // Control
                "halt", "once", "findall", "bagof", "setof"
            )

            builtInFunctions.forEach { function ->
                result.addElement(
                    LookupElementBuilder.create(function)
                        .withTypeText("built-in function")
                        .withTailText("()")
                )
            }
        }

        private fun addBuiltInConstants(result: CompletionResultSet) {
            val constants = listOf("true", "false")

            constants.forEach { constant ->
                result.addElement(
                    LookupElementBuilder.create(constant)
                        .withTypeText("constant")
                        .withBoldness(true)
                )
            }
        }
    }
}
