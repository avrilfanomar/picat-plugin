package com.github.avrilfanomar.picatplugin.language.templates

import com.intellij.codeInsight.template.impl.TemplateSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatLiveTemplatesTest : BasePlatformTestCase() {

    fun testTemplatesAreRegistered() {
        // Open a Picat file to ensure language is loaded
        myFixture.configureByText("sample.pi", "main =>\n    true.")

        val settings = TemplateSettings.getInstance()
        val group = settings.getTemplate("if", "Picat")
        assertNotNull("'if' template should be available in Picat group", group)

        assertNotNull("'foreach' template should exist", settings.getTemplate("foreach", "Picat"))
        assertNotNull("'while' template should exist", settings.getTemplate("while", "Picat"))
        assertNotNull("'try' template should exist", settings.getTemplate("try", "Picat"))
        assertNotNull("'tryf' template should exist", settings.getTemplate("tryf", "Picat"))
    }
}
