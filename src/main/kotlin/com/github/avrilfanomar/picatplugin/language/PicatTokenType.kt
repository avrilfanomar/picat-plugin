package com.github.avrilfanomar.picatplugin.language

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/**
 * Token type for Picat language elements.
 */
class PicatTokenType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String = "PicatTokenType." + super.toString()
}

/**
 * Element type for Picat language elements.
 */
class PicatElementType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String = "PicatElementType." + super.toString()
}