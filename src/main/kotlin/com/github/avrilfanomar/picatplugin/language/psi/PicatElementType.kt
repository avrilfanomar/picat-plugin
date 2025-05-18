package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/**
 * Element type for Picat language elements.
 */
class PicatElementType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String = "PicatElementType." + super.toString()
}