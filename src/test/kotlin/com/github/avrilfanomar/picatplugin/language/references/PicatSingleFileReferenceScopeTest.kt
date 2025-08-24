package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatSingleFileReferenceScopeTest : BasePlatformTestCase() {

    fun testDoesNotResolveAcrossFiles() {
        // Define a predicate in file A
        val fileA = myFixture.configureByText(
            "defs.pi",
            """
            module m.
            
            p().
            """.trimIndent()
        )

        // Use the same name in file B, but without local definition
        val fileB = myFixture.configureByText(
            "use.pi",
            """
            module m.
            
            q() :- <caret>p().
            """.trimIndent()
        )

        val offsetB = fileB.text.indexOf("p()")
        myFixture.editor.caretModel.moveToOffset(offsetB)

        val refB = fileB.findReferenceAt(offsetB)
        // Reference should exist but not resolve to the definition in other file
        assertNotNull("Expected a reference at usage site", refB)
        val resolvedB = refB!!.resolve()
        assertNull("Reference should not resolve across files", resolvedB)

        // Now ensure that within file A, references resolve locally (single-file scope)
        val offsetA = fileA.text.indexOf("p()")
        myFixture.editor.caretModel.moveToOffset(offsetA)
        val refA = fileA.findReferenceAt(offsetA)
        assertNotNull("Expected a reference at definition site in the same file", refA)
        val resolvedA = refA!!.resolve()
        assertNotNull("Reference in the same file should resolve to the local definition", resolvedA)
        assertEquals("Resolution should remain within the same file", fileA.virtualFile, resolvedA!!.containingFile.virtualFile)
    }
}
