package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatIcons
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * File type definition for Picat programming language files.
 * Picat files have the .pi extension.
 */
class PicatFileType : LanguageFileType(PicatLanguage) {
    override fun getName(): String = PicatLanguage.LANGUAGE_ID

    override fun getDescription(): String = "Picat programming language file"
    override fun getDefaultExtension(): String = "pi"
    override fun getIcon(): Icon = PicatIcons.FILE

    @Suppress("CompanionObjectInExtension")
    companion object {
        @JvmStatic
        val INSTANCE = PicatFileType()
    }
}
