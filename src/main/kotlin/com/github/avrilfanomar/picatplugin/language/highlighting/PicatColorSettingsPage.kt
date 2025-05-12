package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.language.PicatIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

/**
 * Color settings page for Picat language.
 * Allows users to customize syntax highlighting colors.
 */
class PicatColorSettingsPage : ColorSettingsPage {
    private val DESCRIPTORS = arrayOf(
        AttributesDescriptor("Keyword", PicatSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Comment", PicatSyntaxHighlighter.COMMENT),
        AttributesDescriptor("String", PicatSyntaxHighlighter.STRING),
        AttributesDescriptor("Number", PicatSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Operator", PicatSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Parentheses", PicatSyntaxHighlighter.PARENTHESES),
        AttributesDescriptor("Braces", PicatSyntaxHighlighter.BRACES),
        AttributesDescriptor("Brackets", PicatSyntaxHighlighter.BRACKETS),
        AttributesDescriptor("Identifier", PicatSyntaxHighlighter.IDENTIFIER),
        AttributesDescriptor("Predicate", PicatSyntaxHighlighter.PREDICATE),
        AttributesDescriptor("Variable", PicatSyntaxHighlighter.VARIABLE),
        AttributesDescriptor("Bad character", PicatSyntaxHighlighter.BAD_CHARACTER)
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Picat"

    override fun getIcon(): Icon = PicatIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = PicatSyntaxHighlighter()

    override fun getDemoText(): String = """
        % This is a sample Picat program
        
        import util.
        
        main => 
            println("Hello, world!"),
            X = 42,
            Y = X + 10,
            println(Y).
        
        factorial(0) = 1.
        factorial(N) = N * factorial(N-1) => N > 0.
        
        fibonacci(0) = 0.
        fibonacci(1) = 1.
        fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) => N > 1.
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}