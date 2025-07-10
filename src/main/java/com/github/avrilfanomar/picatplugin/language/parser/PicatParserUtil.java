package com.github.avrilfanomar.picatplugin.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;

public class PicatParserUtil extends GeneratedParserUtilBase {

    /**
     * Checks if the next character after the current token is whitespace
     * This is used to distinguish between dot access (obj.method) and statement terminators (statement .)
     */
    public static boolean isWhitespaceNext(PsiBuilder builder, int level) {
        // Get the current token type
        IElementType currentToken = builder.getTokenType();
        if (currentToken == null) {
            return false;
        }

        // Get the text of the current token and its position
        String currentTokenText = builder.getTokenText();
        if (currentTokenText == null) {
            return false;
        }

        // Get the original text and current offset
        CharSequence originalText = builder.getOriginalText();
        int currentOffset = builder.getCurrentOffset();

        // Calculate the position after the current token
        int afterTokenOffset = currentOffset + currentTokenText.length();

        // Check if we're at the end of the text
        if (afterTokenOffset >= originalText.length()) {
            return true; // Consider end of file as whitespace
        }

        // Check if the next character is whitespace
        char nextChar = originalText.charAt(afterTokenOffset);
        return Character.isWhitespace(nextChar);
    }

    /**
     * Alternative implementation that checks if the next character is NOT whitespace
     * This can be used with positive lookahead instead of negative lookahead
     */
    public static boolean isNonWhitespaceNext(PsiBuilder builder, int level) {
        return !isWhitespaceNext(builder, level);
    }
}