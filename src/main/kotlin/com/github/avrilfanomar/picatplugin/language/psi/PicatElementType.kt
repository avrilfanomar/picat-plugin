package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.IElementType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import org.jetbrains.annotations.NonNls

/**
 * Custom element type for Picat language PSI elements.
 * 
 * This class extends IElementType to provide Picat-specific element types
 * for the PSI (Program Structure Interface) tree representation.
 */
class PicatElementType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String {
        return "PicatElementType." + super.toString()
    }
}
