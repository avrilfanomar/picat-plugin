package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatIcons
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

/**
 * Configuration type for Picat run configurations.
 */
class PicatConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "Picat"
    override fun getConfigurationTypeDescription(): String = "Picat run configuration"
    override fun getId(): String = "PicatRunConfiguration"
    override fun getIcon(): Icon = PicatIcons.FILE
    
    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(PicatRunConfigurationFactory(this))
}

/**
 * Factory for creating Picat run configurations.
 */
class PicatRunConfigurationFactory(configurationType: ConfigurationType) : ConfigurationFactory(configurationType) {
    override fun getId(): String = "Picat"
    override fun getName(): String = "Picat"
    
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return PicatRunConfiguration(project, this, "Picat")
    }
    
    override fun getOptionsClass(): Class<PicatRunConfigurationOptions> = PicatRunConfigurationOptions::class.java
}