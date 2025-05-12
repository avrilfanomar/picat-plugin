package com.github.avrilfanomar.picatplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

/**
 * Editor for Picat run configurations.
 * Provides UI for editing Picat run configuration settings.
 */
class PicatRunConfigurationEditor(private val project: Project) : SettingsEditor<PicatRunConfiguration>() {
    private val picatFilePathField = TextFieldWithBrowseButton()
    private val programParametersField = RawCommandLineEditor()
    private val workingDirectoryField = TextFieldWithBrowseButton()

    init {
        picatFilePathField.addBrowseFolderListener(
            "Select Picat File",
            "Please select a Picat file to run",
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("pi")
        )

        workingDirectoryField.addBrowseFolderListener(
            "Select Working Directory",
            "Please select a working directory",
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
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
