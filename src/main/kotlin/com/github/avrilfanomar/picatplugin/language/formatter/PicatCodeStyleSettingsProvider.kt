package com.github.avrilfanomar.picatplugin.language.formatter

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

/**
 * A lightweight general CodeStyleSettingsProvider entry for Picat.
 *
 * This complements the LanguageCodeStyleSettingsProvider so that Picat
 * appears consistently under the general Code Style settings category.
 * It uses the same kind of panel as the language provider.
 */
class PicatCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun getConfigurableDisplayName(): String = "Picat"

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(
            settings,
            modelSettings,
            configurableDisplayName
        ) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return object : TabbedLanguageCodeStylePanel(
                    PicatLanguage,
                    currentSettings,
                    settings
                ) {
                    override fun initTabs(settings: CodeStyleSettings) {
                        addIndentOptionsTab(settings)
                        addSpacesTab(settings)
                        addWrappingAndBracesTab(settings)
                    }
                }
            }
        }
    }
}
