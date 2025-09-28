package com.github.avrilfanomar.picatplugin.language

import com.intellij.codeInsight.template.FileTypeBasedContextType

/**
 * Context type for Picat language live templates.
 * Determines when live templates are available based on the Picat file type.
 */
class PicatFileTypeBasedContextType : FileTypeBasedContextType("Picat", PicatFileType.INSTANCE)
