package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatStructureViewTest : BasePlatformTestCase() {

    fun testStructureViewElementBasicCreation() {
        val fileContent = """
            module test.
            
            main =>
                println("Hello World").
        """.trimIndent()
        
        val file = myFixture.configureByText("test.pi", fileContent)
        
        // Test that we can create structure elements without exceptions
        assertDoesNotThrow {
            val structureElement = PicatStructureViewElement(file)
            
            // Test basic properties that should be accessible
            assertEquals(file, structureElement.getValue())
            assertTrue("Should be able to navigate", structureElement.canNavigate())
            assertTrue("Should be able to navigate to source", structureElement.canNavigateToSource())
            
            // Test that we can get presentation
            val presentation = structureElement.getPresentation()
            assertNotNull("Presentation should not be null", presentation)
            
            // Test that we can get sort key
            val sortKey = structureElement.getAlphaSortKey()
            assertNotNull("Sort key should not be null", sortKey)
            
            // Test that we can get children without exceptions
            val children = structureElement.getChildren()
            assertNotNull("Children array should not be null", children)
        }
    }
    
    fun testStructureViewElementWithFunctionRule() {
        val fileContent = """
            module test.
            main => println("test").
        """.trimIndent()
        
        val file = myFixture.configureByText("function_test.pi", fileContent)
        
        // Find function rule elements
        val functionRules = file.children.filterIsInstance<PicatFunctionRule>()
        if (functionRules.isNotEmpty()) {
            val functionRule = functionRules.first()
            
            assertDoesNotThrow {
                val structureElement = PicatStructureViewElement(functionRule)
                assertEquals(functionRule, structureElement.getValue())
                assertNotNull(structureElement.getPresentation())
            }
        }
    }
    
    fun testStructureViewElementNavigation() {
        val fileContent = """
            module test.
            main => true.
        """.trimIndent()
        
        val file = myFixture.configureByText("nav_test.pi", fileContent)
        val structureElement = PicatStructureViewElement(file)
        
        // Test navigation capabilities without throwing exceptions
        assertDoesNotThrow {
            structureElement.navigate(true)
            structureElement.navigate(false)
        }
    }
    
    fun testStructureViewFactory() {
        val fileContent = """
            module test.
            main => println("factory test").
        """.trimIndent()
        
        val file = myFixture.configureByText("factory_test.pi", fileContent)
        
        assertDoesNotThrow {
            val factory = PicatStructureViewFactory()
            val builder = factory.getStructureViewBuilder(file)
            assertNotNull("Builder should not be null", builder)
        }
    }
    
    fun testStructureViewModelCreation() {
        val fileContent = """
            module test.
            
            main =>
                println("Hello").
                
            helper(X) =>
                X + 1.
        """.trimIndent()
        
        val file = myFixture.configureByText("model_test.pi", fileContent)
        
        assertDoesNotThrow {
            val model = PicatStructureViewModel(file)
            assertNotNull("Model should not be null", model)
            
            // Test that we can get sorters
            val sorters = model.getSorters()
            assertNotNull("Sorters should not be null", sorters)
            assertEquals("Should have one sorter", 1, sorters.size)
        }
    }
    
    fun testStructureViewModelElementInfoProvider() {
        val fileContent = """
            module test.
            test_func => true.
        """.trimIndent()
        
        val file = myFixture.configureByText("info_test.pi", fileContent)
        val model = PicatStructureViewModel(file)
        val rootElement = PicatStructureViewElement(file)
        
        assertDoesNotThrow {
            // Test the element info provider methods
            model.isAlwaysShowsPlus(rootElement)
            model.isAlwaysLeaf(rootElement)
        }
    }
    
    fun testStructureViewElementWithEmptyFile() {
        val fileContent = """
            module empty.
        """.trimIndent()
        
        val file = myFixture.configureByText("empty.pi", fileContent)
        
        assertDoesNotThrow {
            val structureElement = PicatStructureViewElement(file)
            
            // Test with minimal file
            assertEquals(file, structureElement.getValue())
            assertNotNull(structureElement.getPresentation())
            assertNotNull(structureElement.getAlphaSortKey())
            assertNotNull(structureElement.getChildren())
        }
    }
    
    private fun assertDoesNotThrow(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("Action should not throw exception: ${e.message}")
        }
    }
}
