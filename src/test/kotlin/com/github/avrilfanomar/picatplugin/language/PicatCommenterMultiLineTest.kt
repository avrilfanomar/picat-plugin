package com.github.avrilfanomar.picatplugin.language

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PicatCommenterMultiLineTest : BasePlatformTestCase() {

    @Test
    fun testMultiLineLineCommentToggle() {
        val code = """
            main => 
                X = 10,
                Y = 20,
                writeln(X + Y).
        """.trimIndent()

        myFixture.configureByText("test.pi", code)

        // Select two middle lines fully (from start of X to end of Y line)
        val startOffset = code.indexOf("X = 10")
        val endOffset = code.indexOf("Y = 20") + "Y = 20,".length
        myFixture.editor.selectionModel.setSelection(startOffset, endOffset)

        // Comment selection with line comment
        myFixture.performEditorAction("CommentByLineComment")
        val afterComment = myFixture.editor.document.text
        // Ensure both lines now have line comment prefix
        val commentedLines = afterComment.lines().filter { it.trim().startsWith("%") }
        assertTrue(commentedLines.size >= 2, "Selected lines should be line-commented")

        // Uncomment selection
        myFixture.performEditorAction("CommentByLineComment")
        val afterUncomment = myFixture.editor.document.text
        // Ensure the two lines do not start with % anymore
        val lines = afterUncomment.lines()
        val xLine = lines.first { it.contains("X = 10") }
        val yLine = lines.first { it.contains("Y = 20") }
        assertFalse(xLine.trim().startsWith("%"), "X line should be uncommented")
        assertFalse(yLine.trim().startsWith("%"), "Y line should be uncommented")
    }

}
