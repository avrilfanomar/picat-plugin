package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings

/** Utility to compute indentation string based on IDE settings. */
internal object PicatIndentUtil {
    fun getIndentation(settings: CodeStyleSettings, level: Int): String {
        val common = settings.getCommonSettings(PicatLanguage)
        val size = common.indentOptions?.INDENT_SIZE ?: 4
        return " ".repeat(size * maxOf(0, level))
    }
}
