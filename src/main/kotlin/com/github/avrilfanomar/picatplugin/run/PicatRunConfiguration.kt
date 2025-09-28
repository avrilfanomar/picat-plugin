package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import java.io.File

/**
 * Run configuration for Picat programs.
 * Allows running Picat programs with the configured Picat executable.
 */
class PicatRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<PicatRunConfigurationOptions>(project, factory, name) {

    /** Path to the Picat file to execute. */
    var picatFilePath: String = ""
    /** Command-line parameters to pass to the Picat program. */
    var programParameters: String = ""
    /** Working directory for the Picat program execution. */
    var workingDirectory: String = project.basePath ?: ""

    override fun getOptions(): PicatRunConfigurationOptions {
        return super.getOptions() as PicatRunConfigurationOptions
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return PicatRunConfigurationEditor(project)
    }

    override fun checkConfiguration() {
        val settings = PicatSettings.getInstance(project)
        if (settings.picatExecutablePath.isBlank()) {
            throw RuntimeConfigurationError(
                "Picat executable path is not set. Please configure it in Settings -> Tools -> Picat."
            )
        }

        val picatExecutable = File(settings.picatExecutablePath)
        if (!picatExecutable.exists() || !picatExecutable.canExecute()) {
            throw RuntimeConfigurationError("Invalid Picat executable: ${settings.picatExecutablePath}")
        }

        if (picatFilePath.isBlank()) {
            throw RuntimeConfigurationError("Picat file path is not specified")
        }

        val picatFile = File(picatFilePath)
        if (!picatFile.exists() || !picatFile.isFile) {
            throw RuntimeConfigurationError("Invalid Picat file: $picatFilePath")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                val settings = PicatSettings.getInstance(project)
                val commandLine = GeneralCommandLine()
                    .withExePath(settings.picatExecutablePath)
                    .withParameters(picatFilePath)
                    .withWorkDirectory(workingDirectory)

                // Add additional program parameters if specified
                if (programParameters.isNotBlank()) {
                    commandLine.addParameters(programParameters.split(" "))
                }

                val processHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, "PICAT_FILE_PATH", picatFilePath)
        JDOMExternalizerUtil.writeField(element, "PROGRAM_PARAMETERS", programParameters)
        JDOMExternalizerUtil.writeField(element, "WORKING_DIRECTORY", workingDirectory)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        picatFilePath = JDOMExternalizerUtil.readField(element, "PICAT_FILE_PATH", "")
        programParameters = JDOMExternalizerUtil.readField(element, "PROGRAM_PARAMETERS", "")
        workingDirectory = JDOMExternalizerUtil.readField(element, "WORKING_DIRECTORY", project.basePath ?: "")
    }
}

/**
 * Options class for Picat run configuration.
 * Extends RunConfigurationOptions to provide Picat-specific configuration storage.
 */
class PicatRunConfigurationOptions : RunConfigurationOptions()
