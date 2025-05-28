package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatIcons
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

/**
 * Configuration type for Picat run configurations.
 */
class PicatConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = PicatLanguage.LANGUAGE_ID
    override fun getConfigurationTypeDescription(): String = "Picat run configuration"
    override fun getId(): String = "PicatRunConfiguration"
    override fun getIcon(): Icon = PicatIcons.FILE

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(PicatRunConfigurationFactory(this))
}

/**
 * Factory for creating Picat run configurations.
 */
class PicatRunConfigurationFactory(configurationType: ConfigurationType) : ConfigurationFactory(configurationType) {
    override fun getId(): String = PicatLanguage.LANGUAGE_ID
    override fun getName(): String = PicatLanguage.LANGUAGE_ID

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return PicatRunConfiguration(project, this, PicatLanguage.LANGUAGE_ID)
    }

    override fun getOptionsClass(): Class<PicatRunConfigurationOptions> = PicatRunConfigurationOptions::class.java
}
