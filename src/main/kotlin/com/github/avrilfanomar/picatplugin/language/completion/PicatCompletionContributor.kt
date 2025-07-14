package com.github.avrilfanomar.picatplugin.language.completion

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
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
            // Add keywords
            addKeywords(result)

            // Add built-in functions
            addBuiltInFunctions(result)

            // Add built-in constants
            addBuiltInConstants(result)
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
                        .withInsertHandler { context, _ ->
                            val editor = context.editor
                            val caretOffset = editor.caretModel.offset
                            editor.document.insertString(caretOffset, "()")
                            editor.caretModel.moveToOffset(caretOffset + 1)
                        }
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
