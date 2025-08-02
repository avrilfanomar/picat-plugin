package com.github.avrilfanomar.picatplugin.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.WHITE_SPACE;

public class PicatParserUtil extends GeneratedParserUtilBase {

    public static boolean isWhitespaceNext(PsiBuilder builder, int level) {
        if (builder.eof()) return true;
        IElementType nextToken = builder.lookAhead(1);
        return nextToken == WHITE_SPACE;
    }

    public static boolean isNonWhitespaceNext(PsiBuilder builder, int level) {
        return !isWhitespaceNext(builder, level);
    }
}