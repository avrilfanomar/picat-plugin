package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider

/**
 * Formatting model builder for Picat language.
 * This class is responsible for creating a formatting model that defines
 * how code should be formatted in the Picat language.
 */
class PicatFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.psiElement.containingFile
        val settings = formattingContext.codeStyleSettings
        
        val rootBlock = PicatBlock(formattingContext.node, null, null, settings)
        
        return FormattingModelProvider.createFormattingModelForPsiFile(
            file,
            rootBlock,
            settings
        )
    }
}