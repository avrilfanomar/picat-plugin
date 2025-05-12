package com.github.avrilfanomar.picatplugin.language

import com.intellij.lang.Language

/**
 * Language definition for Picat programming language.
 * Picat is a multi-paradigm programming language that integrates logic-based and function-based programming.
 */
object PicatLanguage : Language("Picat") {
    override fun getDisplayName(): String = "Picat"
    override fun getMimeTypes(): Array<String> = arrayOf("text/picat")
    
    // ID used for internal language identification
    const val LANGUAGE_ID = "Picat"
}