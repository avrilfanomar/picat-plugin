package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatIcons
import com.github.avrilfanomar.picatplugin.language.psi.PicatFileType
import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware

/**
 * Action to run the current Picat file.
 * Adds a menu item to the context menu and the Run menu.
 */
class PicatRunFileAction : AnAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        // Set presentation properties lazily
        e.presentation.setText("Run Picat File")
        e.presentation.description = "Run current Picat file"
        e.presentation.icon = PicatIcons.FILE

        // Only enable for Picat files
        e.presentation.isEnabledAndVisible = project != null &&
                file != null &&
                file.fileType == PicatFileType.Companion.INSTANCE
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        // Create or get an existing run configuration
        val runManager = RunManager.getInstance(project)
        val configurationType = PicatConfigurationType()
        val factory = configurationType.configurationFactories[0]

        // Look for an existing configuration for this file
        val existingConfigs = runManager.getConfigurationSettingsList(configurationType)
        val config: RunnerAndConfigurationSettings? = existingConfigs.find {
            (it.configuration as? PicatRunConfiguration)?.picatFilePath == file.path
        } ?: run {
            // Create a new configuration
            val configuration = factory.createConfiguration(
                file.nameWithoutExtension,
                factory.createTemplateConfiguration(project)
            ) as PicatRunConfiguration

            configuration.picatFilePath = file.path

            // Add to run manager
            val settings = runManager.createConfiguration(configuration, factory)
            runManager.addConfiguration(settings)
            settings
        }

        // Set as selected configuration
        runManager.selectedConfiguration = config

        // Execute the run configuration
        val executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID)
        if (executor != null && config != null) {
            ExecutionUtil.runConfiguration(config, executor)
        }
    }
}
