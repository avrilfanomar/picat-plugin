package com.github.avrilfanomar.picatplugin.settings

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test

/**
 * Test for the PicatSettings class.
 * This test verifies that the settings are correctly stored and retrieved.
 */
class PicatSettingsTest : BasePlatformTestCase() {

    @Test
    fun testGetInstance() {
        // Get the settings instance for the project
        val settings = PicatSettings.getInstance(project)
        
        // Verify that the instance is not null
        assertNotNull("Settings instance should not be null", settings)
        
        // Verify that getting the instance again returns the same instance
        val settings2 = PicatSettings.getInstance(project)
        assertSame("Getting the instance twice should return the same instance", settings, settings2)
    }

    @Test
    fun testPicatExecutablePath() {
        // Get the settings instance for the project
        val settings = PicatSettings.getInstance(project)
        
        // Set the Picat executable path
        val testPath = "/usr/bin/picat"
        settings.picatExecutablePath = testPath
        
        // Verify that the path is correctly stored
        assertEquals("Picat executable path should be correctly stored", testPath, settings.picatExecutablePath)
        
        // Get the settings instance again and verify that the path is still there
        val settings2 = PicatSettings.getInstance(project)
        assertEquals("Picat executable path should be preserved across instances", testPath, settings2.picatExecutablePath)
    }

    @Test
    fun testGetState() {
        // Get the settings instance for the project
        val settings = PicatSettings.getInstance(project)
        
        // Set the Picat executable path
        val testPath = "/usr/bin/picat"
        settings.picatExecutablePath = testPath
        
        // Get the state
        val state = settings.state
        
        // Verify that the state is the same as the settings instance
        assertSame("State should be the same as the settings instance", settings, state)
        
        // Verify that the state has the correct Picat executable path
        assertEquals("State should have the correct Picat executable path", testPath, state.picatExecutablePath)
    }

    @Test
    fun testLoadState() {
        // Get the settings instance for the project
        val settings = PicatSettings.getInstance(project)
        
        // Create a new settings instance with a different path
        val newSettings = PicatSettings()
        val testPath = "/usr/local/bin/picat"
        newSettings.picatExecutablePath = testPath
        
        // Load the state from the new settings
        settings.loadState(newSettings)
        
        // Verify that the path is correctly loaded
        assertEquals("Picat executable path should be correctly loaded", testPath, settings.picatExecutablePath)
    }
}