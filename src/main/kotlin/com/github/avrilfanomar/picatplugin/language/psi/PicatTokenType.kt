package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.IElementType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import org.jetbrains.annotations.NonNls

/**
 * Custom token type for Picat language lexical tokens.
 * 
 * This class extends IElementType to provide Picat-specific token types
 * used by the lexer and parser for syntax analysis.
 */
class PicatTokenType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage)
