package com.github.avrilfanomar.picatplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.io.File
import javax.swing.JComponent

/**
 * Editor for Picat run configurations.
 * Provides UI for editing Picat run configuration settings.
 */
class PicatRunConfigurationEditor(project: Project) : SettingsEditor<PicatRunConfiguration>() {
    private val picatFilePathField = TextFieldWithBrowseButton()
    private val programParametersField = RawCommandLineEditor()
    private val workingDirectoryField = TextFieldWithBrowseButton()
    private val picatPathField = JBTextField()

    init {
        picatFilePathField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile().withExtensionFilter("pi")
                .withTitle("Select Picat File")
                .withDescription("Please select a Picat file to run")
        )

        workingDirectoryField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle("Select Working Directory")
                .withDescription("Please select a working directory")
        )
    }

    override fun resetEditorFrom(configuration: PicatRunConfiguration) {
        picatFilePathField.text = configuration.picatFilePath
        programParametersField.text = configuration.programParameters
        workingDirectoryField.text = configuration.workingDirectory
        picatPathField.text = configuration.picatPath
    }

    override fun applyEditorTo(configuration: PicatRunConfiguration) {
        configuration.picatFilePath = picatFilePathField.text
        configuration.programParameters = programParametersField.text
        configuration.workingDirectory = workingDirectoryField.text
        configuration.picatPath = picatPathField.text
    }

    override fun createEditor(): JComponent {
        val pathSeparatorHint = if (File.pathSeparatorChar == ':') "colon" else "semicolon"
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Picat file:", picatFilePathField)
            .addLabeledComponent("Program parameters:", programParametersField)
            .addLabeledComponent("Working directory:", workingDirectoryField)
            .addLabeledComponent("Module paths (PICATPATH):", picatPathField)
            .addTooltip("Directories where Picat looks for modules. Separate paths with $pathSeparatorHint. " +
                    "Paths can be relative to working dir or absolute.")
            .panel
        return panel
    }
}
