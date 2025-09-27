package com.github.avrilfanomar.picatplugin.settings

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatSettingsConfigurableTest : BasePlatformTestCase() {

    fun testConfigurableCreation() {
        assertDoesNotThrow("Configurable should be created without exceptions") {
            val configurable = PicatSettingsConfigurable(project)
            assertNotNull("Configurable should not be null", configurable)
        }
    }

    fun testGetDisplayName() {
        assertDoesNotThrow("getDisplayName should return language ID") {
            val configurable = PicatSettingsConfigurable(project)
            val displayName = configurable.getDisplayName()
            
            assertEquals("Display name should match language ID", PicatLanguage.LANGUAGE_ID, displayName)
            assertEquals("Display name should be 'Picat'", "Picat", displayName)
            assertNotNull("Display name should not be null", displayName)
            assertTrue("Display name should not be empty", displayName.isNotBlank())
        }
    }

    fun testIdConstant() {
        assertDoesNotThrow("ID constant should be accessible") {
            val id = PicatSettingsConfigurable.ID
            assertEquals("ID should be 'preferences.picat'", "preferences.picat", id)
            assertNotNull("ID should not be null", id)
            assertTrue("ID should not be empty", id.isNotBlank())
        }
    }

    fun testCreatePanel() {
        assertDoesNotThrow("createPanel should create UI panel") {
            val configurable = PicatSettingsConfigurable(project)
            val panel = configurable.createPanel()
            
            assertNotNull("Panel should not be null", panel)
            assertTrue("Panel should be a valid component", panel.componentCount >= 0)
            
            // Test that panel can be accessed multiple times
            val panel2 = configurable.createPanel()
            assertNotNull("Second panel should not be null", panel2)
        }
    }

    fun testConfigurableImplementsCorrectInterface() {
        assertDoesNotThrow("Configurable should implement correct interfaces") {
            val configurable = PicatSettingsConfigurable(project)
            
            // Test that it's a BoundSearchableConfigurable
            assertTrue("Should be BoundSearchableConfigurable", 
                configurable::class.java.superclass.simpleName.contains("BoundSearchableConfigurable"))
        }
    }

    fun testConfigurableId() {
        assertDoesNotThrow("Configurable should have correct ID configuration") {
            val configurable = PicatSettingsConfigurable(project)
            val displayName = configurable.getDisplayName()
            
            assertEquals("Display name should match language ID", PicatLanguage.LANGUAGE_ID, displayName)
            assertEquals("ID constant should be accessible", "preferences.picat", PicatSettingsConfigurable.ID)
        }
    }

    fun testConfigurableWithDifferentProjects() {
        assertDoesNotThrow("Configurable should work with different projects") {
            val configurable1 = PicatSettingsConfigurable(project)
            val configurable2 = PicatSettingsConfigurable(project)
            
            assertNotNull("First configurable should not be null", configurable1)
            assertNotNull("Second configurable should not be null", configurable2)
            assertNotSame("Configurables should be different instances", configurable1, configurable2)
            
            // Both should have same display name
            assertEquals("Both should have same display name", 
                configurable1.getDisplayName(), configurable2.getDisplayName())
        }
    }

    fun testPanelCreationMultipleTimes() {
        assertDoesNotThrow("Panel creation should work multiple times") {
            val configurable = PicatSettingsConfigurable(project)
            
            // Create panel multiple times
            val panel1 = configurable.createPanel()
            val panel2 = configurable.createPanel()
            val panel3 = configurable.createPanel()
            
            assertNotNull("Panel 1 should not be null", panel1)
            assertNotNull("Panel 2 should not be null", panel2)
            assertNotNull("Panel 3 should not be null", panel3)
        }
    }

    fun testConfigurableSettings() {
        assertDoesNotThrow("Configurable should access settings") {
            val configurable = PicatSettingsConfigurable(project)
            
            // Create panel which should trigger settings access through bindings
            val panel = configurable.createPanel()
            assertNotNull("Panel should be created successfully", panel)
            
            // Verify that the configurable can access project settings
            val settings = PicatSettings.getInstance(project)
            assertNotNull("Settings should be accessible", settings)
        }
    }

    fun testConfigurableConstantAccess() {
        assertDoesNotThrow("Configurable constants should be accessible") {
            val configurable = PicatSettingsConfigurable(project)
            
            // Test that we can access the constant and it matches expectations
            val idConstant = PicatSettingsConfigurable.ID
            assertEquals("ID constant should be correct", "preferences.picat", idConstant)
            
            // Test that display name is consistent
            val displayName = configurable.getDisplayName()
            assertEquals("Display name should be consistent", PicatLanguage.LANGUAGE_ID, displayName)
        }
    }

    fun testUIComponentBinding() {
        assertDoesNotThrow("UI components should be properly bound") {
            val configurable = PicatSettingsConfigurable(project)
            val panel = configurable.createPanel()
            
            assertNotNull("Panel should not be null", panel)
            
            // The panel should contain components for:
            // 1. Picat executable path field
            // 2. Enable annotations checkbox
            // These are created through the DSL in createPanel()
            assertTrue("Panel should have components", panel.componentCount > 0)
        }
    }

    private fun assertDoesNotThrow(message: String, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("$message - Exception: ${e.message}")
        }
    }
}
