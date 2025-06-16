package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.tree.IElementType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import org.jetbrains.annotations.NonNls

class PicatTokenType(@NonNls debugName: String) : IElementType(debugName, PicatLanguage) {
    override fun toString(): String {
        return "PicatTokenType." + super.toString()
    }
}
