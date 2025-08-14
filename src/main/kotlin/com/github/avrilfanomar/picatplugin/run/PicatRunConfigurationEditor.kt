package com.github.avrilfanomar.picatplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

/**
 * Editor for Picat run configurations.
 * Provides UI for editing Picat run configuration settings.
 */
class PicatRunConfigurationEditor(project: Project) : SettingsEditor<PicatRunConfiguration>() {
    private val picatFilePathField = TextFieldWithBrowseButton()
    private val programParametersField = RawCommandLineEditor()
    private val workingDirectoryField = TextFieldWithBrowseButton()

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
    }

    override fun applyEditorTo(configuration: PicatRunConfiguration) {
        configuration.picatFilePath = picatFilePathField.text
        configuration.programParameters = programParametersField.text
        configuration.workingDirectory = workingDirectoryField.text
    }

    override fun createEditor(): JComponent {
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Picat file:", picatFilePathField)
            .addLabeledComponent("Program parameters:", programParametersField)
            .addLabeledComponent("Working directory:", workingDirectoryField)
            .panel
        return panel
    }
}
