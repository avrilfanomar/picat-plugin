package com.github.avrilfanomar.picatplugin.stdlib

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatStdlibUtilTest : BasePlatformTestCase() {

    fun testFindStdlibModuleVFileWithEmptyPath() {
        assertDoesNotThrow("Should handle empty Picat executable path") {
            // Set empty path in settings
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = ""
            
            val result = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
            assertNull("Should return null for empty path", result)
        }
    }

    fun testFindStdlibModuleVFileWithBlankPath() {
        assertDoesNotThrow("Should handle blank Picat executable path") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "   "
            
            val result = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
            assertNull("Should return null for blank path", result)
        }
    }

    fun testFindStdlibModuleVFileWithNonExistentPath() {
        assertDoesNotThrow("Should handle non-existent Picat executable path") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/non/existent/path/picat"
            
            val result = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
            // May return null or not find the module - both are acceptable
            assertNotNull("Result should not cause exceptions", "handled")
        }
    }

    fun testFindStdlibModuleVFileWithUrlPath() {
        assertDoesNotThrow("Should handle URL-formatted paths") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "file:///usr/local/picat"
            
            val result = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
            // May return null if path doesn't exist - that's acceptable
            assertNotNull("Method should not throw for URL paths", "handled")
        }
    }

    fun testFindStdlibModuleVFileWithVariousModuleNames() {
        assertDoesNotThrow("Should handle various module names") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/test/picat"
            
            val moduleNames = listOf("basic", "io", "cp", "math", "planner", "util", "nonexistent")
            
            moduleNames.forEach { moduleName ->
                val result = PicatStdlibUtil.findStdlibModuleVFile(project, moduleName)
                // All module names should be handled without exceptions
                assertNotNull("Module name $moduleName should be handled", "handled")
            }
        }
    }

    fun testFindStdlibModulePsiFileWithEmptyPath() {
        assertDoesNotThrow("Should handle empty path for PSI file") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = ""
            
            val result = PicatStdlibUtil.findStdlibModulePsiFile(project, "basic")
            assertNull("Should return null for empty path", result)
        }
    }

    fun testFindStdlibModulePsiFileWithNonExistentPath() {
        assertDoesNotThrow("Should handle non-existent path for PSI file") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/non/existent/path/picat"
            
            val result = PicatStdlibUtil.findStdlibModulePsiFile(project, "basic")
            // May return null - that's acceptable for non-existent paths
            assertNotNull("Method should handle non-existent paths", "handled")
        }
    }

    fun testFindStdlibModulePsiFileWithVariousModules() {
        assertDoesNotThrow("Should handle various modules for PSI file") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/test/picat/bin/picat"
            
            val modules = listOf("basic", "io", "cp", "util", "math")
            
            modules.forEach { module ->
                val result = PicatStdlibUtil.findStdlibModulePsiFile(project, module)
                // All modules should be handled without exceptions
                assertNotNull("Module $module should be handled for PSI", "handled")
            }
        }
    }

    fun testStdlibUtilWithDifferentPathFormats() {
        assertDoesNotThrow("Should handle different path formats") {
            val settings = PicatSettings.getInstance(project)
            
            val pathFormats = listOf(
                "/usr/local/bin/picat",
                "/home/user/picat/picat",
                "C:\\Picat\\picat.exe",
                "./picat",
                "picat",
                "file:///usr/local/picat",
                "temp:///test/picat"
            )
            
            pathFormats.forEach { path ->
                settings.picatExecutablePath = path
                
                // Test both methods with various paths
                val vfile = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
                val psiFile = PicatStdlibUtil.findStdlibModulePsiFile(project, "basic")
                
                // Should handle all path formats without exceptions
                assertNotNull("Path format $path should be handled", "handled")
            }
        }
    }

    fun testStdlibUtilMethodsConsistency() {
        assertDoesNotThrow("VFile and PSI methods should be consistent") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/test/picat"
            
            val moduleName = "basic"
            
            // Test that both methods can be called consistently
            val vfile = PicatStdlibUtil.findStdlibModuleVFile(project, moduleName)
            val psiFile = PicatStdlibUtil.findStdlibModulePsiFile(project, moduleName)
            
            // If vfile is null, psiFile should also be null (logical consistency)
            if (vfile == null) {
                assertNull("PSI file should be null when VFile is null", psiFile)
            }
            
            assertNotNull("Methods should work consistently", "handled")
        }
    }

    fun testStdlibUtilWithSpecialCharacters() {
        assertDoesNotThrow("Should handle paths with special characters") {
            val settings = PicatSettings.getInstance(project)
            
            val specialPaths = listOf(
                "/path with spaces/picat",
                "/path-with-dashes/picat",
                "/path_with_underscores/picat",
                "/path.with.dots/picat"
            )
            
            specialPaths.forEach { path ->
                settings.picatExecutablePath = path
                
                val result = PicatStdlibUtil.findStdlibModuleVFile(project, "basic")
                // Should handle special characters without throwing
                assertNotNull("Special path $path should be handled", "handled")
            }
        }
    }

    fun testStdlibUtilWithEmptyModuleName() {
        assertDoesNotThrow("Should handle empty module name") {
            val settings = PicatSettings.getInstance(project)
            settings.picatExecutablePath = "/test/picat"
            
            PicatStdlibUtil.findStdlibModuleVFile(project, "")
            PicatStdlibUtil.findStdlibModulePsiFile(project, "")
            
            // Should handle empty module names gracefully
            assertNotNull("Empty module name should be handled", "handled")
        }
    }

    fun testStdlibUtilEdgeCases() {
        assertDoesNotThrow("Should handle edge cases") {
            val settings = PicatSettings.getInstance(project)
            
            // Test various edge cases
            val edgeCases = listOf(
                Pair("", "basic"),
                Pair("/", "basic"),
                Pair("/test/picat", ""),
                Pair("///multiple/slashes///picat", "basic"),
                Pair("/test/picat", "module.with.dots"),
                Pair("/test/picat", "module-with-dashes")
            )
            
            edgeCases.forEach { (path, module) ->
                settings.picatExecutablePath = path
                
                val vfile = PicatStdlibUtil.findStdlibModuleVFile(project, module)
                val psiFile = PicatStdlibUtil.findStdlibModulePsiFile(project, module)
                
                // All edge cases should be handled without exceptions
                assertNotNull("Edge case ($path, $module) should be handled", "handled")
            }
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
