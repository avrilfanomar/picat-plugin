package com.github.avrilfanomar.picatplugin.language.formatter

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.openapi.components.Service
import com.intellij.psi.codeStyle.CodeStyleSettingsManager

/**
 * Service that provides access to the PicatCustomFormatter.
 * This service is registered in the plugin.xml file and can be accessed through the service() function.
 */
@Service
class PicatFormatterService {
    /**
     * Gets or creates a PicatCustomFormatter for the given settings and spacing builder.
     */
    fun getFormatter(): PicatCustomFormatter {
        val defaultSettings = CodeStyleSettingsManager.getInstance().createSettings()
        return PicatCustomFormatter(defaultSettings)
    }

    fun getFormatter(settings: com.intellij.psi.codeStyle.CodeStyleSettings): PicatCustomFormatter {
        return PicatCustomFormatter(settings)
    }
}

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
