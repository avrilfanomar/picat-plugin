package com.github.avrilfanomar.picatplugin.language

import com.intellij.lang.Language

/**
 * Language definition for Picat programming language.
 * Picat is a multi-paradigm programming language that integrates logic-based and function-based programming.
 */
object PicatLanguage : Language("Picat") {
    override fun getDisplayName(): String = LANGUAGE_ID
    override fun getMimeTypes(): Array<String> = arrayOf("text/picat")
    const val LANGUAGE_ID = "Picat"

    @Suppress("unused")
    private fun readResolve(): Any = PicatLanguage
}