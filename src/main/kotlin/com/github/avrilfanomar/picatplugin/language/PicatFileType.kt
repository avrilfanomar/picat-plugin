package com.github.avrilfanomar.picatplugin.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object PicatFileType : LanguageFileType(PicatLanguage) { // Assuming PicatLanguage is an object

    override fun getName(): String = "Picat File"

    override fun getDescription(): String = "Picat language file"

    override fun getDefaultExtension(): String = "pi" // Common extension for Picat

    override fun getIcon(): Icon = PicatIcons.FILE // Assuming PicatIcons.FILE is defined
}
