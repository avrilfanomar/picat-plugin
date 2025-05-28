package com.github.avrilfanomar.picatplugin.settings

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

/**
 * Settings UI for configuring the Picat plugin.
 * Allows setting the path to the Picat executable.
 */
class PicatSettingsConfigurable(private val project: Project) : Configurable {
    private var picatExecutablePathField: TextFieldWithBrowseButton? = null
    private val settings: PicatSettings = PicatSettings.getInstance(project)

    override fun getDisplayName(): String = PicatLanguage.LANGUAGE_ID

    override fun createComponent(): JComponent {
        picatExecutablePathField = TextFieldWithBrowseButton()
        picatExecutablePathField?.addBrowseFolderListener(
            "Select Picat Executable",
            "Please select the Picat executable",
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        val panel = JBPanel<JBPanel<*>>()
        panel.add(
            FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Picat executable path:"), picatExecutablePathField!!, true)
                .panel
        )

        return panel
    }

    override fun isModified(): Boolean {
        return picatExecutablePathField?.text != settings.picatExecutablePath
    }

    override fun apply() {
        settings.picatExecutablePath = picatExecutablePathField?.text ?: ""
    }

    override fun reset() {
        picatExecutablePathField?.text = settings.picatExecutablePath
    }

    override fun disposeUIResources() {
        picatExecutablePathField = null
    }
}
