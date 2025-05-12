package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

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
        val psiFile = context.psiLocation?.containingFile ?: return false
        
        // Only handle Picat files
        if (psiFile.fileType != PicatFileType.INSTANCE) {
            return false
        }
        
        // Set the configuration properties
        val virtualFile = psiFile.virtualFile ?: return false
        configuration.picatFilePath = virtualFile.path
        configuration.name = virtualFile.nameWithoutExtension
        
        return true
    }

    override fun isConfigurationFromContext(
        configuration: PicatRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val psiFile = context.psiLocation?.containingFile ?: return false
        
        // Only handle Picat files
        if (psiFile.fileType != PicatFileType.INSTANCE) {
            return false
        }
        
        val virtualFile = psiFile.virtualFile ?: return false
        return configuration.picatFilePath == virtualFile.path
    }
}