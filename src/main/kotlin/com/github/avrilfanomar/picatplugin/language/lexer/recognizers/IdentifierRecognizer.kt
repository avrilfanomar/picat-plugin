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
            "acyclic_term", "and_to_list", "append", "apply", "arg", "arity", "array",
            "ascii_alpha", "ascii_alpha_digit", "ascii_digit", "ascii_lowercase", "ascii_uppercase",
            "atom", "atom_chars", "atom_codes", "atomic", "attr_var", "avg", "between",
            "bigint", "bind_vars", "call", "call_cleanup", "catch", "char", "chr", "clear",
            "compare_terms", "compound", "copy_term", "count_all", "del", "delete", "delete_all",
            "different_terms", "digit", "dvar", "dvar_or_int", "fail", "false", "find_all",
            "findall", "first", "flatten", "float", "fold", "freeze", "functor", "get",
            "get_attr", "get_global_map", "get_heap_map", "get_table_map", "ground",
            "handle_exception", "has_key", "hash_code", "head", "heap_is_empty", "heap_pop",
            "heap_push", "heap_size", "heap_to_list", "heap_top", "insert", "insert_all",
            "insert_ordered", "insert_ordered_down", "int", "integer", "is", "keys", "last",
            "len", "length", "list", "list_to_and", "lowercase", "map", "map_to_list", "max",
            "maxint_small", "maxof", "maxof_inc", "membchk", "member", "min", "minint_small",
            "minof", "minof_inc", "name", "new_array", "new_list", "new_map", "new_max_heap",
            "new_min_heap", "new_set", "new_struct", "nonvar", "not", "nth", "number",
            "number_chars", "number_codes", "number_vars", "once", "ord", "parse_radix_string",
            "parse_term", "post_event", "post_event_any", "post_event_bound", "post_event_dom",
            "post_event_ins", "prod", "put", "put_attr", "real", "reduce", "remove_dups",
            "repeat", "reverse", "second", "select", "size", "slice", "sort", "sort_down",
            "sort_down_remove_dups", "sort_remove_dups", "sorted", "sorted_down", "string",
            "struct", "subsumes", "sum", "tail", "throw", "to_array", "to_atom",
            "to_binary_string", "to_codes", "to_fstring", "to_hex_string", "to_int",
            "to_integer", "to_list", "to_lowercase", "to_number", "to_oct_string",
            "to_radix_string", "to_real", "to_string", "to_uppercase", "true", "uppercase",
            "values", "var", "variant", "vars", "zip"
        )
    }

    override fun canRecognize(buffer: CharSequence, startOffset: Int, endOffset: Int): Boolean {
        if (startOffset >= endOffset) {
            return false
        }

        val c = buffer[startOffset]
        
        // Check for variables (start with uppercase letter or underscore)
        if (CharacterClassifier.isUpperCase(c) || c == '_') {
            return true
        }
        
        // Check for identifiers and keywords (start with lowercase letter)
        return CharacterClassifier.isLetter(c)
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
        
        // Check for keywords
        val tokenType = when (text) {
            "import" -> PicatTokenTypes.IMPORT_KEYWORD
            "export" -> PicatTokenTypes.EXPORT_KEYWORD
            "include" -> PicatTokenTypes.INCLUDE_KEYWORD
            "module" -> PicatTokenTypes.MODULE_KEYWORD
            "index" -> PicatTokenTypes.INDEX_KEYWORD
            "private" -> PicatTokenTypes.PRIVATE_KEYWORD
            "public" -> PicatTokenTypes.PUBLIC_KEYWORD
            "table" -> PicatTokenTypes.TABLE_KEYWORD
            "end" -> PicatTokenTypes.END_KEYWORD
            "if" -> PicatTokenTypes.IF_KEYWORD
            "then" -> PicatTokenTypes.THEN_KEYWORD
            "else" -> PicatTokenTypes.ELSE_KEYWORD
            "elseif" -> PicatTokenTypes.ELSEIF_KEYWORD
            "while" -> PicatTokenTypes.WHILE_KEYWORD
            "do" -> PicatTokenTypes.DO_KEYWORD
            "foreach" -> PicatTokenTypes.FOREACH_KEYWORD
            "for" -> PicatTokenTypes.FOR_KEYWORD
            "return" -> PicatTokenTypes.RETURN_KEYWORD
            "throw" -> PicatTokenTypes.THROW_KEYWORD
            "try" -> PicatTokenTypes.TRY_KEYWORD
            "catch" -> PicatTokenTypes.CATCH_KEYWORD
            "not" -> PicatTokenTypes.NOT_KEYWORD
            "once" -> PicatTokenTypes.ONCE_KEYWORD
            "div" -> PicatTokenTypes.DIV_KEYWORD
            "mod" -> PicatTokenTypes.MOD_KEYWORD
            "rem" -> PicatTokenTypes.REM_KEYWORD
            "in" -> PicatTokenTypes.IN_KEYWORD
            "notin" -> PicatTokenTypes.NOTIN_KEYWORD
            "writef" -> PicatTokenTypes.WRITEF_KEYWORD
            "true" -> PicatTokenTypes.TRUE_KEYWORD
            "false" -> PicatTokenTypes.FALSE_KEYWORD
            "fail" -> PicatTokenTypes.FAIL_KEYWORD
            "repeat" -> PicatTokenTypes.REPEAT_KEYWORD
            else -> {
                // Check if the identifier is a 'basic' module function or operator
                if (BASIC_MODULE_FUNCTIONS.contains(text)) {
                    PicatTokenTypes.BASIC_MODULE_FUNCTION
                } else {
                    PicatTokenTypes.IDENTIFIER
                }
            }
        }
        
        return Pair(tokenType, endPos)
    }
}