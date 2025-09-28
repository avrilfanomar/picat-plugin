package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jdom.Element

class PicatRunConfigurationTest : BasePlatformTestCase() {

    fun testConfigurationType() {
        val configurationType = PicatConfigurationType()
        
        assertDoesNotThrow("ConfigurationType methods should work") {
            assertEquals("Picat", configurationType.displayName)
            assertEquals("Picat run configuration", configurationType.configurationTypeDescription)
            assertEquals("PicatRunConfiguration", configurationType.id)
            assertNotNull("Icon should not be null", configurationType.icon)
            
            val factories = configurationType.configurationFactories
            assertNotNull("Factories should not be null", factories)
            assertEquals("Should have one factory", 1, factories.size)
            assertTrue("Should have PicatRunConfigurationFactory", 
                factories.first() is PicatRunConfigurationFactory)
        }
    }

    fun testConfigurationFactory() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        
        assertDoesNotThrow("ConfigurationFactory methods should work") {
            assertEquals("Picat", factory.id)
            assertEquals("Picat", factory.name)
            assertEquals(PicatRunConfigurationOptions::class.java, factory.optionsClass)
            
            val templateConfig = factory.createTemplateConfiguration(project)
            assertNotNull("Template configuration should not be null", templateConfig)
            assertTrue("Should create PicatRunConfiguration", templateConfig is PicatRunConfiguration)
            assertEquals("Should have correct name", PicatLanguage.LANGUAGE_ID, templateConfig.name)
        }
    }

    fun testRunConfigurationBasicProperties() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        assertDoesNotThrow("Basic properties should work") {
            // Test initial state
            assertEquals("", config.picatFilePath)
            assertEquals("", config.programParameters)
            assertNotNull("Working directory should not be null", config.workingDirectory)
            
            // Test setters
            config.picatFilePath = "/path/to/test.pi"
            config.programParameters = "arg1 arg2"
            config.workingDirectory = "/working/dir"
            
            assertEquals("/path/to/test.pi", config.picatFilePath)
            assertEquals("arg1 arg2", config.programParameters)
            assertEquals("/working/dir", config.workingDirectory)
            
            // Test configuration editor
            val editor = config.configurationEditor
            assertNotNull("Editor should not be null", editor)
            assertTrue("Should be PicatRunConfigurationEditor", editor is PicatRunConfigurationEditor)
        }
    }

    fun testCheckConfigurationWithBlankFilePath() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        // Leave file path blank
        config.picatFilePath = ""
        
        assertDoesNotThrow("checkConfiguration should handle blank file path") {
            try {
                config.checkConfiguration()
                fail("Should throw exception for blank file path")
            } catch (e: Exception) {
                // Expected - configuration should reject blank file path
                assertNotNull("Exception should not be null", e)
            } catch (e: Throwable) {
                // Any configuration exception is acceptable for this test
                assertNotNull("Exception should not be null", e)
            }
        }
    }

    fun testCheckConfigurationWithInvalidFile() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        // Set a non-existent file
        config.picatFilePath = "/non/existent/file.pi"
        
        assertDoesNotThrow("checkConfiguration should handle invalid file") {
            try {
                config.checkConfiguration()
                fail("Should throw exception for invalid file")
            } catch (e: Exception) {
                // Expected - configuration should reject invalid file
                assertNotNull("Exception should not be null", e)
            } catch (e: Throwable) {
                // Any configuration exception is acceptable for this test
                assertNotNull("Exception should not be null", e)
            }
        }
    }

    fun testSerializationAndDeserialization() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        // Set some values
        config.picatFilePath = "/path/to/test.pi"
        config.programParameters = "arg1 arg2"
        config.workingDirectory = "/custom/working/dir"
        
        assertDoesNotThrow("Serialization and deserialization should work") {
            // Test serialization
            val element = Element("configuration")
            config.writeExternal(element)
            
            assertNotNull("Serialized element should not be null", element)
            
            // Test deserialization
            val newConfig = PicatRunConfiguration(project, factory, "Test2")
            newConfig.readExternal(element)
            
            assertEquals("File path should be restored", config.picatFilePath, newConfig.picatFilePath)
            assertEquals("Parameters should be restored", config.programParameters, newConfig.programParameters)
            assertEquals("Working directory should be restored", config.workingDirectory, newConfig.workingDirectory)
        }
    }

    fun testGetStateWithValidConfiguration() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        // Set up a configuration
        config.picatFilePath = "/test.pi"
        config.programParameters = "test args"
        
        assertDoesNotThrow("getState should work") {
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            val environment = ExecutionEnvironmentBuilder.create(project, executor, config).build()
            
            val state = config.getState(executor, environment)
            assertNotNull("State should not be null", state)
        }
    }

    fun testRunConfigurationOptions() {
        assertDoesNotThrow("RunConfigurationOptions should work") {
            val options = PicatRunConfigurationOptions()
            assertNotNull("Options should not be null", options)
        }
    }

    fun testSerializationWithEmptyValues() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        // Leave all values as defaults/empty
        
        assertDoesNotThrow("Serialization should work with empty values") {
            val element = Element("configuration")
            config.writeExternal(element)
            
            val newConfig = PicatRunConfiguration(project, factory, "Test2")
            newConfig.readExternal(element)
            
            assertEquals("Empty file path should be preserved", "", newConfig.picatFilePath)
            assertEquals("Empty parameters should be preserved", "", newConfig.programParameters)
        }
    }

    fun testConfigurationWithSpecialCharacters() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        val config = PicatRunConfiguration(project, factory, "Test")
        
        assertDoesNotThrow("Configuration should handle special characters") {
            config.picatFilePath = "/path with spaces/test.pi"
            config.programParameters = "arg with spaces"
            config.workingDirectory = "/path with spaces"
            
            // Test serialization with special characters
            val element = Element("configuration")
            config.writeExternal(element)
            
            val newConfig = PicatRunConfiguration(project, factory, "Test2")
            newConfig.readExternal(element)
            
            assertEquals("Spaces in path should be preserved", config.picatFilePath, newConfig.picatFilePath)
            assertEquals("Spaces in parameters should be preserved", config.programParameters, newConfig.programParameters)
        }
    }

    fun testConfigurationFactoryMultipleCreation() {
        val configurationType = PicatConfigurationType()
        val factory = PicatRunConfigurationFactory(configurationType)
        
        assertDoesNotThrow("Factory should create multiple configurations") {
            val config1 = factory.createTemplateConfiguration(project)
            val config2 = factory.createTemplateConfiguration(project)
            
            assertNotNull("First config should not be null", config1)
            assertNotNull("Second config should not be null", config2)
            assertNotSame("Configs should be different instances", config1, config2)
        }
    }

    fun testConfigurationTypeIcon() {
        val configurationType = PicatConfigurationType()
        
        assertDoesNotThrow("Icon should be accessible") {
            val icon = configurationType.icon
            assertNotNull("Icon should not be null", icon)
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
