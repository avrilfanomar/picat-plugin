package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Entry point for the Picat formatter.
 * This class creates a formatting model for Picat code.
 */
class PicatFormattingModelBuilder : FormattingModelBuilder {
    /**
     * Creates a formatting model for the given context.
     */
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings

        val spacingBuilder = PicatSpacingBuilder(settings).getSpacingBuilder()
        val blockFactory = PicatBlockFactory(settings, spacingBuilder)
        val rootBlock = blockFactory.createBlock(element.node)

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            settings
        )
    }
}
