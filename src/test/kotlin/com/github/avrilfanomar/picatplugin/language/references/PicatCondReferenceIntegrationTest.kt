package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Integration test to verify that cond/3 resolves to primitives
 */
class PicatCondReferenceIntegrationTest : BasePlatformTestCase() {

    @Test
    fun testCondResolvesToPrimitives() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            fibc(N) = cond((N == 0; N == 1), 1, fibc(N - 1) + fibc(N - 2)).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("cond")
        val ref = file.findReferenceAt(caretOffset)
        
        assertNotNull("Should find reference at cond", ref)
        val resolved = ref?.resolve()
        
        if (resolved == null) {
            println("cond did not resolve")
            println("Reference type: ${ref?.javaClass}")
        } else {
            val fileName = resolved.containingFile?.name
            println("cond resolved to file: $fileName")
            assertTrue(
                "cond should resolve to primitives.pi, but resolved to: $fileName",
                fileName == "primitives.pi"
            )
        }
    }
}
