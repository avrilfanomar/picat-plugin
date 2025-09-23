package com.github.avrilfanomar.picatplugin.language

/**
 * Verifies that block comment toggling removes existing block markers for Picat files
 * instead of wrapping the selection again.
 */
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PicatCommenterBlockUncommentTest : BasePlatformTestCase() {

    @Test
    fun testBlockUncommentDoesNotWrapAgain() {
        val code = """
            main => 
                X = 10,
                Y = 20,
                writeln(X + Y).
        """.trimIndent()

        // Start by inserting a block comment around the two middle lines
        myFixture.configureByText("test.pi", code)
        val startOffset = code.indexOf("X = 10")
        val endOffset = code.indexOf("Y = 20") + "Y = 20,".length
        myFixture.editor.selectionModel.setSelection(startOffset, endOffset)

        // Add block comment
        myFixture.performEditorAction("CommentByBlockComment")
        val afterComment = myFixture.editor.document.text
        assertTrue(afterComment.contains("/*"))
        assertTrue(afterComment.contains("*/"))

        // Now select the entire commented region including the markers and toggle again
        val prefixIndex = afterComment.indexOf("/*")
        val suffixIndex = afterComment.indexOf("*/", startIndex = prefixIndex + 2)
        require(prefixIndex >= 0 && suffixIndex > prefixIndex) { "Block comment markers not found after first toggle" }
        // Place caret inside the block comment (no selection) and toggle again
        myFixture.editor.selectionModel.removeSelection()
        myFixture.editor.caretModel.moveToOffset(prefixIndex + 3)

        // Toggle block comment again to remove existing block markers
        myFixture.performEditorAction("CommentByBlockComment")
        val afterUncomment = myFixture.editor.document.text

        // Ensure there are no block comment markers around the selection anymore
        Assertions.assertFalse(afterUncomment.contains("/*"), "Block comment prefix should be removed on toggle")
        Assertions.assertFalse(afterUncomment.contains("*/"), "Block comment suffix should be removed on toggle")
    }
}
