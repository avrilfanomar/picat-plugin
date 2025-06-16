package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

/**
 * Producer for Picat run configurations.
 * Creates run configurations from the context of a Picat file.
 */
class PicatRunConfigurationProducer : LazyRunConfigurationProducer<PicatRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory {
        return PicatConfigurationType().configurationFactories[0]
    }

    override fun setupConfigurationFromContext(
        configuration: PicatRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        // Use a variable to store the result
        var isSetupSuccessful = false

        val psiFile = context.psiLocation?.containingFile

        // Only proceed if we have a valid Picat file
        if (psiFile != null && psiFile.fileType is PicatFileType) {
            val virtualFile = psiFile.virtualFile

            if (virtualFile != null) {
                // Set the configuration properties
                configuration.picatFilePath = virtualFile.path
                configuration.name = virtualFile.nameWithoutExtension
                isSetupSuccessful = true
            }
        }

        return isSetupSuccessful
    }

    override fun isConfigurationFromContext(
        configuration: PicatRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        // Use a variable to store the result
        var isFromContext = false

        val psiFile = context.psiLocation?.containingFile

        // Only proceed if we have a valid Picat file
        if (psiFile != null && psiFile.fileType is PicatFileType) {
            val virtualFile = psiFile.virtualFile

            if (virtualFile != null) {
                isFromContext = configuration.picatFilePath == virtualFile.path
            }
        }

        return isFromContext
    }
}
