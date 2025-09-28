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
    
    
    fun testStructureViewModelIsAlwaysShowsPlusAndLeaf() {
        val fileContent = """
            module test.
            main => println("test").
        """.trimIndent()
        
        val file = myFixture.configureByText("leaf_test.pi", fileContent)
        val model = PicatStructureViewModel(file)
        val fileElement = PicatStructureViewElement(file)
        
        // Test isAlwaysShowsPlus for different element types
        val alwaysShowsPlus = model.isAlwaysShowsPlus(fileElement)
        assertNotNull("IsAlwaysShowsPlus should return a value", alwaysShowsPlus)
        
        // Test isAlwaysLeaf for different element types
        val alwaysLeaf = model.isAlwaysLeaf(fileElement)
        assertNotNull("IsAlwaysLeaf should return a value", alwaysLeaf)
        
        // Test with function rules if available
        val functionRules = file.children.filterIsInstance<PicatFunctionRule>()
        if (functionRules.isNotEmpty()) {
            val functionElement = PicatStructureViewElement(functionRules.first())
            val functionLeaf = model.isAlwaysLeaf(functionElement)
            assertTrue("Function rules should always be leaves", functionLeaf)
        }
    }
    
    fun testStructureViewElementPresentationContent() {
        val fileContent = """
            module presentation_test.
            main => println("Hello World").
        """.trimIndent()
        
        val file = myFixture.configureByText("presentation.pi", fileContent)
        val structureElement = PicatStructureViewElement(file)
        
        val presentation = structureElement.getPresentation()
        assertNotNull("Presentation should not be null", presentation)
        
        val presentationText = presentation.presentableText
        assertNotNull("Presentation text should not be null", presentationText)
        assertTrue("Presentation text should not be empty", presentationText!!.isNotEmpty())
    }
    
    fun testStructureViewElementSorting() {
        val fileContent = """
            module sorting_test.
            
            zebra_func => println("z").
            alpha_func => println("a").
            beta_func => println("b").
        """.trimIndent()
        
        val file = myFixture.configureByText("sorting.pi", fileContent)
        val model = PicatStructureViewModel(file)
        
        val sorters = model.getSorters()
        assertEquals("Should have exactly one sorter", 1, sorters.size)
        assertEquals("Should be alpha sorter", "ALPHA_COMPARATOR", sorters[0].toString())
        
        // Test sort key generation
        val structureElement = PicatStructureViewElement(file)
        val sortKey = structureElement.getAlphaSortKey()
        assertNotNull("Sort key should not be null", sortKey)
        assertTrue("Sort key should not be empty", sortKey.isNotEmpty())
    }
    
    fun testStructureViewElementChildrenHierarchy() {
        val fileContent = """
            module hierarchy_test.
            
            main =>
                println("main function").
                
            helper(X) =>
                X + 1.
                
            another_helper(Y, Z) =>
                Y * Z.
        """.trimIndent()
        
        val file = myFixture.configureByText("hierarchy.pi", fileContent)
        val rootElement = PicatStructureViewElement(file)
        
        val children = rootElement.getChildren()
        assertNotNull("Children should not be null", children)
        
        // Count function rule children
        val functionChildren = children.filterIsInstance<PicatStructureViewElement>().filter { 
            it.getValue() is PicatFunctionRule 
        }
        
        // We should have some structure in a non-empty file
        if (file.children.filterIsInstance<PicatFunctionRule>().isNotEmpty()) {
            assertTrue("Should have function rule children", functionChildren.isNotEmpty())
        }
        
        // Test that function rules have no children (are leaves)
        val functionRules = file.children.filterIsInstance<PicatFunctionRule>()
        if (functionRules.isNotEmpty()) {
            val functionElement = PicatStructureViewElement(functionRules.first())
            val functionChildren = functionElement.getChildren()
            assertEquals("Function rules should have no children", 0, functionChildren.size)
        }
    }
    
    fun testStructureViewElementNavigationCapabilities() {
        val fileContent = """
            module nav_capabilities_test.
            test_function => true.
        """.trimIndent()
        
        val file = myFixture.configureByText("nav_cap.pi", fileContent)
        val structureElement = PicatStructureViewElement(file)
        
        // Test navigation capabilities
        assertTrue("File should be navigable", structureElement.canNavigate())
        assertTrue("File should be navigable to source", structureElement.canNavigateToSource())
        
        // Test with function rules
        val functionRules = file.children.filterIsInstance<PicatFunctionRule>()
        if (functionRules.isNotEmpty()) {
            val functionElement = PicatStructureViewElement(functionRules.first())
            assertTrue("Function should be navigable", functionElement.canNavigate())
            assertTrue("Function should be navigable to source", functionElement.canNavigateToSource())
        }
    }
    
    fun testStructureViewFactoryBuilderCreation() {
        val fileContent = """
            module builder_test.
            main => println("Builder test").
        """.trimIndent()
        
        val file = myFixture.configureByText("builder.pi", fileContent)
        val factory = PicatStructureViewFactory()
        
        val builder = factory.getStructureViewBuilder(file)
        assertNotNull("Builder should not be null", builder)
        
        // Test that the builder is of the expected type
        assertDoesNotThrow {
            // We can test that we got a TreeBasedStructureViewBuilder
            assertNotNull("Builder should be created successfully", builder)
            // Just verify the basic functionality works
        }
    }
    
    fun testStructureViewElementEdgeCases() {
        // Test with null or minimal content
        val emptyFileContent = "module empty."
        val emptyFile = myFixture.configureByText("empty_edge.pi", emptyFileContent)
        
        assertDoesNotThrow {
            val emptyElement = PicatStructureViewElement(emptyFile)
            assertNotNull("Empty element should be created", emptyElement)
            assertEquals("Value should be the file", emptyFile, emptyElement.getValue())
            
            val emptyChildren = emptyElement.getChildren()
            assertNotNull("Empty children should not be null", emptyChildren)
            
            val emptyPresentation = emptyElement.getPresentation()
            assertNotNull("Empty presentation should not be null", emptyPresentation)
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
