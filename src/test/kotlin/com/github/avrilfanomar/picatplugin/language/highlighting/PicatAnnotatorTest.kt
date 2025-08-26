package com.github.avrilfanomar.picatplugin.language.highlighting

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatAnnotatorTest : BasePlatformTestCase() {

    fun testUnresolvedPredicateReferenceIsAnnotated() {
        myFixture.configureByText(
            "a.pi",
            """
            module a.
            
            p().
            q() :- p(), missing().
            """.trimIndent()
        )
        val infos: List<HighlightInfo> = myFixture.doHighlighting()
        // ensure 'missing' is highlighted as an error
        val text = myFixture.file.text
        val idx = text.indexOf("missing")
        assertTrue("Test sanity: 'missing' should be present", idx >= 0)
        val hit = infos.any { info ->
            info.severity == com.intellij.lang.annotation.HighlightSeverity.ERROR &&
                text.substring(info.startOffset, info.endOffset) == "missing"
        }
        assertTrue("Expected an error highlight covering 'missing'", hit)
    }

    fun testResolvedPredicateReferenceHasNoAnnotation() {
        myFixture.configureByText(
            "b.pi",
            """
            module b.
            
            missing().
            q() :- missing().
            """.trimIndent()
        )
        val infos = myFixture.doHighlighting()
        val text = myFixture.file.text
        val hasMissingError = infos.any { info ->
            info.severity == com.intellij.lang.annotation.HighlightSeverity.ERROR &&
                text.substring(info.startOffset, info.endOffset) == "missing"
        }
        assertFalse("No error highlight expected for 'missing' when target exists", hasMissingError)
    }

    fun testStyleWarningForUnnecessaryQuotes() {
        myFixture.configureByText(
            "c.pi",
            """
            module c.
            
            'foo'().
            """.trimIndent()
        )
        val infos = myFixture.doHighlighting()
        val text = myFixture.file.text
        val hasQuotedWeakWarning = infos.any { info ->
            info.severity == com.intellij.lang.annotation.HighlightSeverity.WEAK_WARNING &&
                text.substring(info.startOffset, info.endOffset) == "'foo'"
        }
        assertTrue("Expected a weak warning highlighting the quoted atom", hasQuotedWeakWarning)
    }

    fun testDisablingAnnotationsViaSettingsTurnsOff() {
        // Disable annotations
        val settings = PicatSettings.getInstance(project)
        settings.enableAnnotations = false

        myFixture.configureByText(
            "d.pi",
            """
            module d.
            
            q() :- missing().
            'foo'().
            """.trimIndent()
        )
        val infos = myFixture.doHighlighting()
        val text = myFixture.file.text
        val anyAnnot = infos.any { info ->
            info.severity == com.intellij.lang.annotation.HighlightSeverity.ERROR ||
                (info.severity == com.intellij.lang.annotation.HighlightSeverity.WEAK_WARNING &&
                    text.substring(info.startOffset, info.endOffset) == "'foo'")
        }
        assertFalse("No annotations should be produced when disabled in settings", anyAnnot)

        // Re-enable for other tests
        settings.enableAnnotations = true
    }
}
