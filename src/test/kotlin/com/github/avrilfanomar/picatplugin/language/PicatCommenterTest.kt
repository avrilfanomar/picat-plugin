package com.github.avrilfanomar.picatplugin.language

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for PicatCommenter functionality.
 * This test verifies that line and block commenting work correctly for Picat files.
 */
class PicatCommenterTest : BasePlatformTestCase() {

    @Test
    fun testLineCommentToggle() {
        // Test commenting a line
        val code = """
            main => 
                X = 10,
                writeln(X).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)

        // Place cursor on the second line (X = 10,)
        myFixture.editor.caretModel.moveToOffset(code.indexOf("X = 10"))

        // Trigger line comment action (Ctrl+/)
        myFixture.performEditorAction("CommentByLineComment")

        val result = myFixture.editor.document.text
        Assertions.assertTrue(result.contains("%"), "Line should be commented")

        // Place cursor on the second line (X = 10,)
        myFixture.editor.caretModel.moveToOffset(code.indexOf("X = 10"))
        // Test uncommenting the line
        myFixture.performEditorAction("CommentByLineComment")

        val uncommentedResult = myFixture.editor.document.text
        Assertions.assertFalse(uncommentedResult.contains("%"), "Line should be uncommented")
    }

    @Test
    fun testBlockCommentToggle() {
        // Test block commenting
        val code = """
            main => 
                X = 10,
                Y = 20,
                writeln(X + Y).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)

        // Select the middle two lines
        val startOffset = code.indexOf("X = 10")
        val endOffset = code.indexOf("Y = 20") + "Y = 20,".length
        myFixture.editor.selectionModel.setSelection(startOffset, endOffset)

        // Trigger block comment action (Ctrl+Shift+/)
        myFixture.performEditorAction("CommentByBlockComment")

        val result = myFixture.editor.document.text
        Assertions.assertTrue(result.contains("/*"), "Block should be commented")
        Assertions.assertTrue(result.contains("*/"), "Block should be commented")
    }
}
