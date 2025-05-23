package com.github.avrilfanomar.picatplugin.language.psi

import javax.swing.Icon

/**
 * File type definition for Picat programming language files.
 * Picat files have the .pi extension.
 */
class PicatFileType :
    com.intellij.openapi.fileTypes.LanguageFileType(_root_ide_package_.com.github.avrilfanomar.picatplugin.language.PicatLanguage) {
    override fun getName(): String =
        _root_ide_package_.com.github.avrilfanomar.picatplugin.language.PicatLanguage.LANGUAGE_ID

    override fun getDescription(): String = "Picat programming language file"
    override fun getDefaultExtension(): String = "pi"
    override fun getIcon(): Icon = _root_ide_package_.com.github.avrilfanomar.picatplugin.language.PicatIcons.FILE

    @Suppress("CompanionObjectInExtension")
    companion object {
        @JvmStatic
        val INSTANCE = PicatFileType()
    }
}
