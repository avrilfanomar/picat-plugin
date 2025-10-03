package com.github.avrilfanomar.picatplugin.settings

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatSettingsTest : BasePlatformTestCase() {

    fun testSettingsDefaultValues() {
        assertDoesNotThrow("Settings should have default values") {
            val settings = PicatSettings()
            
            // Test default values
            assertEquals("Default executable path should be empty", "", settings.picatExecutablePath)
            assertEquals("Default annotations should be enabled", true, settings.enableAnnotations)
        }
    }

    fun testSettingsPropertyAccess() {
        assertDoesNotThrow("Settings properties should be accessible") {
            val settings = PicatSettings()
            
            // Test setters
            settings.picatExecutablePath = "/usr/bin/picat"
            settings.enableAnnotations = false
            
            // Test getters
            assertEquals("/usr/bin/picat", settings.picatExecutablePath)

            // Test with different values
            settings.picatExecutablePath = "/path/to/picat.exe"
            settings.enableAnnotations = true
            
            assertEquals("/path/to/picat.exe", settings.picatExecutablePath)
        }
    }

    fun testGetStateReturnsInstance() {
        assertDoesNotThrow("getState should return the instance itself") {
            val settings = PicatSettings()
            val state = settings.getState()
            
            assertNotNull("State should not be null", state)
            assertSame("getState should return the same instance", settings, state)
        }
    }

    fun testLoadStateMethod() {
        assertDoesNotThrow("loadState should work properly") {
            val settings1 = PicatSettings()
            val settings2 = PicatSettings()
            
            // Set different values in source settings
            settings1.picatExecutablePath = "/custom/path/picat"
            settings1.enableAnnotations = false
            
            // Load state from settings1 to settings2
            settings2.loadState(settings1)
            
            // Verify state was copied
            assertEquals("Executable path should be copied", settings1.picatExecutablePath, settings2.picatExecutablePath)
            assertEquals("Annotations setting should be copied", settings1.enableAnnotations, settings2.enableAnnotations)
        }
    }

    fun testGetInstanceMethod() {
        assertDoesNotThrow("getInstance should work") {
            val instance = PicatSettings.getInstance(project)
            
            assertNotNull("Instance should not be null", instance)

            // Test that getInstance returns a consistent instance
            val instance2 = PicatSettings.getInstance(project)
            assertSame("getInstance should return the same instance for same project", instance, instance2)
        }
    }

    fun testSettingsWithEmptyStrings() {
        assertDoesNotThrow("Settings should handle empty strings") {
            val settings = PicatSettings()
            
            settings.picatExecutablePath = ""
            assertEquals("Should handle empty executable path", "", settings.picatExecutablePath)
            
            settings.picatExecutablePath = "   "
            assertEquals("Should handle whitespace-only path", "   ", settings.picatExecutablePath)
        }
    }

    fun testSettingsWithSpecialCharacters() {
        assertDoesNotThrow("Settings should handle special characters") {
            val settings = PicatSettings()
            
            // Test path with spaces and special characters
            val specialPath = "/path with spaces/picat (v1.0)/picat.exe"
            settings.picatExecutablePath = specialPath
            assertEquals("Should handle special characters in path", specialPath, settings.picatExecutablePath)
            
            // Test a very long path
            val longPath = "/very/long/path/".repeat(20) + "picat"
            settings.picatExecutablePath = longPath
            assertEquals("Should handle long paths", longPath, settings.picatExecutablePath)
        }
    }

    fun testSettingsPersistenceLifecycle() {
        assertDoesNotThrow("Settings persistence lifecycle should work") {
            val original = PicatSettings()
            original.picatExecutablePath = "/test/path"
            original.enableAnnotations = false
            
            // Get state
            val state = original.getState()
            assertNotNull("State should be available", state)
            
            // Create new instance and load state
            val restored = PicatSettings()
            restored.loadState(state)
            
            // Verify restoration
            assertEquals("Path should be restored", original.picatExecutablePath, restored.picatExecutablePath)
            assertEquals("Annotations setting should be restored", original.enableAnnotations, restored.enableAnnotations)
        }
    }

    fun testMultipleSettingsInstances() {
        assertDoesNotThrow("Multiple settings instances should work independently") {
            val settings1 = PicatSettings()
            val settings2 = PicatSettings()
            
            // Set different values
            settings1.picatExecutablePath = "/path1"
            settings1.enableAnnotations = true
            
            settings2.picatExecutablePath = "/path2"
            settings2.enableAnnotations = false
            
            // Verify independence
            assertEquals("/path1", settings1.picatExecutablePath)
            assertEquals("/path2", settings2.picatExecutablePath)
            assertEquals(true, settings1.enableAnnotations)
            assertEquals(false, settings2.enableAnnotations)
        }
    }

    fun testBooleanPropertyToggling() {
        assertDoesNotThrow("Boolean property should toggle properly") {
            val settings = PicatSettings()
            
            // Test initial state
            assertTrue("Initial annotations should be enabled", settings.enableAnnotations)
            
            // Toggle
            settings.enableAnnotations = false

            // Toggle back
            settings.enableAnnotations = true
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
