package com.github.avrilfanomar.picatplugin.language.lexer.recognizers

import com.github.avrilfanomar.picatplugin.language.lexer.CharacterClassifier
import com.github.avrilfanomar.picatplugin.language.lexer.TokenRecognizer
import com.github.avrilfanomar.picatplugin.language.lexer.TokenSkipper
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.psi.tree.IElementType

/**
 * Recognizer for identifiers, variables, and keywords in the Picat lexer.
 */
class IdentifierRecognizer : TokenRecognizer {
    companion object {
        // List of functions and operators from the 'basic' module
        private val BASIC_MODULE_FUNCTIONS = setOf(
            "acyclic_term",
            "and_to_list",
            "append",
            "apply",
            "arg",
            "arity",
            "array",
            "ascii_alpha",
            "ascii_alpha_digit",
            "ascii_digit",
            "ascii_lowercase",
            "ascii_uppercase",
            "atom",
            "atom_chars",
            "atom_codes",
            "atomic",
            "attr",
            "attr_var",
            "avg",
            "between",
            "bigint",
            "bind_vars",
            "call",
            "call_cleanup",
            "catch",
            "char",
            "chr",
            "clear",
            "clone",
            "close",
            "code",
            "compile",
            "compare_terms",
            "compound",
            "copy_term",
            "count_all",
            "del",
            "delete",
            "delete_all",
            "different_terms",
            "digit",
            "dvar",
            "dvar_or_int",
            "eval",
            "exit",
            "fail",
            "false",
            "find_all",
            "findall",
            "first",
            "flatten",
            "float",
            "fold",
            "freeze",
            "functor",
            "get",
            "get_attr",
            "get_global_map",
            "get_heap_map",
            "get_table_map",
            "ground",
            "handle_exception",
            "has_key",
            "hash_code",
            "head",
            "heap_is_empty",
            "heap_pop",
            "heap_push",
            "heap_size",
            "heap_to_list",
            "heap_top",
            "here",
            "insert",
            "insert_all",
            "insert_ordered",
            "insert_ordered_down",
            "int",
            "integer",
            "is",
            "join",
            "keys",
            "last",
            "len",
            "length",
            "list",
            "list_to_and",
            "load",
            "lowercase",
            "map",
            "map_to_list",
            "max",
            "maxint_small",
            "maxof",
            "maxof_inc",
            "membchk",
            "member",
            "min",
            "minint_small",
            "minof",
            "minof_inc",
            "name",
            "new_array",
            "new_list",
            "new_map",
            "new_max_heap",
            "new_min_heap",
            "new_set",
            "new_struct",
            "nonvar",
            "not",
            "nth",
            "number",
            "number_chars",
            "number_codes",
            "number_vars",
            "once",
            "ord",
            "parse_radix_string",
            "parse_term",
            "pop",
            "post_event",
            "post_event_any",
            "post_event_bound",
            "post_event_dom",
            "post_event_ins",
            "prod",
            "push",
            "put",
            "put_attr",
            "rand",
            "read",
            "real",
            "reduce",
            "remove",
            "remove_dups",
            "repeat",
            "reverse",
            "second",
            "select",
            "self",
            "send",
            "size",
            "slice",
            "sort",
            "sort_down",
            "sort_down_remove_dups",
            "sort_remove_dups",
            "sorted",
            "sorted_down",
            "sqrt",
            "srand",
            "string",
            "struct",
            "subsumes",
            "sum",
            "tail",
            "text",
            "throw",
            "to",
            "to_array",
            "to_atom",
            "to_binary_string",
            "to_codes",
            "to_fstring",
            "to_hex_string",
            "to_int",
            "to_integer",
            "to_list",
            "to_lowercase",
            "to_number",
            "to_oct_string",
            "to_radix_string",
            "to_real",
            "to_string",
            "to_uppercase",
            "tree",
            "true",
            "uppercase",
            "values",
            "var",
            "variant",
            "vars",
            "write",
            "zip",
            "~"
        )

        // Map of keywords to their token types
        private val KEYWORD_MAP = mapOf(
            // Module-related keywords
            "import" to PicatTokenTypes.IMPORT_KEYWORD,
            "export" to PicatTokenTypes.EXPORT_KEYWORD,
            "include" to PicatTokenTypes.INCLUDE_KEYWORD,
            "module" to PicatTokenTypes.MODULE_KEYWORD,

            // Legacy keywords
            "index" to PicatTokenTypes.INDEX_KEYWORD,
            "private" to PicatTokenTypes.PRIVATE_KEYWORD,
            "public" to PicatTokenTypes.PUBLIC_KEYWORD,
            "table" to PicatTokenTypes.TABLE_KEYWORD,

            // Control flow keywords
            "end" to PicatTokenTypes.END_KEYWORD,
            "if" to PicatTokenTypes.IF_KEYWORD,
            "then" to PicatTokenTypes.THEN_KEYWORD,
            "else" to PicatTokenTypes.ELSE_KEYWORD,
            "elseif" to PicatTokenTypes.ELSEIF_KEYWORD,
            "while" to PicatTokenTypes.WHILE_KEYWORD,
            "do" to PicatTokenTypes.DO_KEYWORD,
            "foreach" to PicatTokenTypes.FOREACH_KEYWORD,
            "return" to PicatTokenTypes.RETURN_KEYWORD,
            "throw" to PicatTokenTypes.THROW_KEYWORD,
            "try" to PicatTokenTypes.TRY_KEYWORD,
            "catch" to PicatTokenTypes.CATCH_KEYWORD,

            // Logical and arithmetic keywords
            "not" to PicatTokenTypes.NOT_KEYWORD,
            "once" to PicatTokenTypes.ONCE_KEYWORD,
            "div" to PicatTokenTypes.DIV_KEYWORD,
            "mod" to PicatTokenTypes.MOD_KEYWORD,
            "rem" to PicatTokenTypes.REM_KEYWORD,
            "in" to PicatTokenTypes.IN_KEYWORD,
            "notin" to PicatTokenTypes.NOTIN_KEYWORD,

            // Other keywords
            "writef" to PicatTokenTypes.WRITEF_KEYWORD,
            "true" to PicatTokenTypes.TRUE_KEYWORD,
            "false" to PicatTokenTypes.FALSE_KEYWORD,
            "fail" to PicatTokenTypes.FAIL_KEYWORD,
            "pass" to PicatTokenTypes.PASS_KEYWORD,
            "repeat" to PicatTokenTypes.REPEAT_KEYWORD
        )
    }

    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        val c = buffer[startOffset]

        // Check for variables (start with uppercase letter or underscore) or identifiers/keywords (start with letter)
        return CharacterClassifier.isUpperCase(c) || c == '_' || CharacterClassifier.isLetter(c)
    }

    override fun recognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Pair<IElementType, Int> {
        val skipper = TokenSkipper(buffer, endOffset)
        val c = buffer[startOffset]

        // Handle variables (start with uppercase letter or underscore)
        if (CharacterClassifier.isUpperCase(c) || c == '_') {
            val endPos = skipper.skipIdentifier(startOffset)
            return Pair(PicatTokenTypes.VARIABLE, endPos)
        }

        // Handle identifiers and keywords
        val endPos = skipper.skipIdentifier(startOffset)
        val text = buffer.substring(startOffset, endPos)

        // Get token type for the text
        val tokenType = getTokenTypeForText(text)

        return Pair(tokenType, endPos)
    }

    /**
     * Determines the token type for a given text.
     */
    private fun getTokenTypeForText(text: String): IElementType {
        // Check if it's a keyword
        val keywordType = KEYWORD_MAP[text]
        if (keywordType != null) {
            return keywordType
        }

        // Check if it's a basic module function or default to identifier
        return if (BASIC_MODULE_FUNCTIONS.contains(text)) {
            PicatTokenTypes.BASIC_MODULE_FUNCTION
        } else {
            PicatTokenTypes.IDENTIFIER
        }
    }
}
