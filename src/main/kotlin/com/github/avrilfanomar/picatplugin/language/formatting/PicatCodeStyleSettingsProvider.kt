package com.github.avrilfanomar.picatplugin.language.formatting

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.github.avrilfanomar.picatplugin.language.PicatLanguage

/**
 * Provider for Picat code style settings.
 * This class makes Picat code style settings available in the IDE settings.
 */
class PicatCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return PicatCodeStyleSettings(settings)
    }

    override fun getConfigurableDisplayName(): String {
        return "Picat"
    }

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return PicatCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class PicatCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
        TabbedLanguageCodeStylePanel(PicatLanguage, currentSettings, settings) {
        // You can override methods here to customize the panel
    }
}