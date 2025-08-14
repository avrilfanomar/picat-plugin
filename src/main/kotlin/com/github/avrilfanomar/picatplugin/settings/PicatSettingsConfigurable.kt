package com.github.avrilfanomar.picatplugin.settings

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

/**
 * Settings UI for configuring the Picat plugin.
 * Allows setting the path to the Picat executable.
 *
 * Implemented with the IntelliJ UI DSL and BoundSearchableConfigurable to follow
 * current IDE settings UI best practices: bindings handle apply/reset/isModified
 * and the configurable is searchable.
 */
class PicatSettingsConfigurable(private val project: Project) :
    BoundSearchableConfigurable(PicatLanguage.LANGUAGE_ID, ID, ID) {

    private val settings: PicatSettings get() = PicatSettings.getInstance(project)

    override fun createPanel() = panel {
        row("Picat executable path:") {
            val descriptor = FileChooserDescriptorFactory.singleFile()
                .withTitle("Select Picat Executable")
                .withDescription("Please select the Picat executable")

            val field = TextFieldWithBrowseButton()
            field.addBrowseFolderListener(project, descriptor)
            cell(field)
                .bindText(
                    { settings.picatExecutablePath },
                    { settings.picatExecutablePath = it }
                )
                .resizableColumn()
        }
    }

    override fun getDisplayName(): String = PicatLanguage.LANGUAGE_ID

    companion object {
        const val ID: String = "preferences.picat"
    }
}
