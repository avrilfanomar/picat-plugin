package com.github.avrilfanomar.picatplugin.language

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import com.github.avrilfanomar.picatplugin.language.PicatLanguage

/**
 * Element type for Picat language elements.
 */
class PicatElementType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String = "PicatElementType." + super.toString()
}
