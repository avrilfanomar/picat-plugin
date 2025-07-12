package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFile

/**
 * A document formatting strategy for Picat language.
 * This class uses PicatCustomFormatter to format Picat code.
 */
class PicatDocumentFormattingStrategy : AsyncDocumentFormattingService() {
    override fun getFeatures(): Set<FormattingService.Feature> = emptySet()

    override fun canFormat(file: PsiFile): Boolean = file.language == PicatLanguage

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val file = request.context.containingFile
        if (!canFormat(file)) return null

        val settings = request.context.codeStyleSettings
        val spacingBuilder = PicatSpacingBuilder(settings).getSpacingBuilder()
        val formatterService = service<PicatFormatterService>()
        val customFormatter = formatterService.getFormatter(settings, spacingBuilder)

        return object : FormattingTask {
            override fun run() {
                val text = request.documentText
                val formattedText = customFormatter.format(text)
                request.onTextReady(formattedText)
            }

            override fun cancel(): Boolean = false
        }
    }

    override fun getNotificationGroupId(): String = "Picat Formatting"

    override fun getName(): String = "Picat Formatter"
}