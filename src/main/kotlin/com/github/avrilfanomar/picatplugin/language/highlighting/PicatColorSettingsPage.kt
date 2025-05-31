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
    private val descriptors = arrayOf(
        AttributesDescriptor("Keyword", PicatSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Comment", PicatSyntaxHighlighter.COMMENT),
        AttributesDescriptor("String", PicatSyntaxHighlighter.STRING),
        AttributesDescriptor("Number", PicatSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Operator", PicatSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Parentheses", PicatSyntaxHighlighter.PARENTHESES),
        AttributesDescriptor("Braces", PicatSyntaxHighlighter.BRACES),
        AttributesDescriptor("Brackets", PicatSyntaxHighlighter.BRACKETS),
        AttributesDescriptor("Identifier", PicatSyntaxHighlighter.IDENTIFIER),
        AttributesDescriptor("Variable", PicatSyntaxHighlighter.VARIABLE),
        AttributesDescriptor("Basic module function", PicatSyntaxHighlighter.BASIC_MODULE_FUNCTION),
        AttributesDescriptor("Bad character", PicatSyntaxHighlighter.BAD_CHARACTER)
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = com.github.avrilfanomar.picatplugin.language.PicatLanguage.LANGUAGE_ID

    override fun getIcon(): Icon = PicatIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = PicatSyntaxHighlighter()

    override fun getDemoText(): String = """
        % This is a sample Picat program

        import util.

        main => 
            println("Hello, world!"),
            X = 42,
            Y = X + 10,
            println(Y),
            % Using basic module functions
            L = [1, 2, 3, 4, 5],
            length(L) = Len,
            println("Length: " ++ Len),
            append(L, [6, 7], L2),
            println(L2),
            sort(L2) = Sorted,
            println(Sorted),
            if member(3, L) then
                println("3 is in the list")
            else
                println("3 is not in the list")
            end.

        factorial(0) = 1.
        factorial(N) = N * factorial(N-1) => N > 0.

        fibonacci(0) = 0.
        fibonacci(1) = 1.
        fibonacci(N) = fibonacci(N-1) + fibonacci(N-2) => N > 1.
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null
}
