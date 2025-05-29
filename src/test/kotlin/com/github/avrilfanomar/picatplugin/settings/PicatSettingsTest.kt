package com.github.avrilfanomar.picatplugin.settings

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertEquals

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
        assertNotNull(settings, "Settings instance should not be null")
        
        // Verify that getting the instance again returns the same instance
        val settings2 = PicatSettings.getInstance(project)
        assertSame(settings, settings2, "Getting the instance twice should return the same instance")
    }

    @Test
    fun testPicatExecutablePath() {
        // Get the settings instance for the project
        val settings = PicatSettings.getInstance(project)
        
        // Set the Picat executable path
        val testPath = "/usr/bin/picat"
        settings.picatExecutablePath = testPath
        
        // Verify that the path is correctly stored
        assertEquals(testPath, settings.picatExecutablePath, "Picat executable path should be correctly stored")
        
        // Get the settings instance again and verify that the path is still there
        val settings2 = PicatSettings.getInstance(project)
        assertEquals(testPath, settings2.picatExecutablePath, "Picat executable path should be preserved across instances")
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
        assertSame(settings, state, "State should be the same as the settings instance")
        
        // Verify that the state has the correct Picat executable path
        assertEquals(testPath, state.picatExecutablePath, "State should have the correct Picat executable path")
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
        assertEquals(testPath, settings.picatExecutablePath, "Picat executable path should be correctly loaded")
    }
}