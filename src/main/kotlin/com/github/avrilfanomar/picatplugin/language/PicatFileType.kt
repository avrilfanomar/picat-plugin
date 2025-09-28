package com.github.avrilfanomar.picatplugin.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * File type definition for Picat language files (.pi extension).
 * Provides metadata and icons for Picat files in the IDE.
 */
object PicatFileType : LanguageFileType(PicatLanguage) { // Assuming PicatLanguage is an object

    /** Static instance of PicatFileType for use throughout the plugin. */
    val INSTANCE = PicatFileType

    override fun getName(): String = "Picat File"

    override fun getDescription(): String = "Picat language file"

    override fun getDefaultExtension(): String = "pi" // Common extension for Picat

    override fun getIcon(): Icon = PicatIcons.FILE // Assuming PicatIcons.FILE is defined
}
