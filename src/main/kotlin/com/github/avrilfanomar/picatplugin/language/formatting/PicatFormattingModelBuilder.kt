package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.psi.formatter.DocumentBasedFormattingModel

/**
 * Formatting model builder for Picat language.
 * This class is responsible for creating a formatting model that defines
 * how code should be formatted in the Picat language.
 */
class PicatFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.psiElement.containingFile
        val settings = formattingContext.codeStyleSettings

        // Create a spacing builder
        val spacingBuilder = PicatSpacingBuilder(settings).getSpacingBuilder()

        // Create a block factory
        val blockFactory = PicatBlockFactory(settings, spacingBuilder)

        // Create a root block
        val rootBlock = blockFactory.createBlock(formattingContext.node)

        // Create a custom formatting model that applies indentation
        return DocumentBasedFormattingModel(
            rootBlock,
            formattingContext.project,
            settings,
            file.fileType,
            file
        )
    }
}
