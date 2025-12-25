package com.github.avrilfanomar.picatplugin.stdlib

import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Tests that the embedded primitives.pi file can be loaded and parsed correctly.
 */
class PicatPrimitivesUtilTest : BasePlatformTestCase() {

    @Test
    fun testPrimitivesFileCanBeLoaded() {
        val primitivesPsi = PicatStdlibUtil.getPrimitivesPsiFile(project)
        assertNotNull("Primitives PSI file should be loaded", primitivesPsi)
        assertEquals("primitives.pi", primitivesPsi?.name)
    }

    @Test
    fun testPrimitivesFileContainsCond() {
        val primitivesPsi = PicatStdlibUtil.getPrimitivesPsiFile(project)
        assertNotNull("Primitives PSI file should be loaded", primitivesPsi)

        val heads = PsiTreeUtil.findChildrenOfType(primitivesPsi, PicatHead::class.java)
        assertTrue("Primitives should contain at least one head", heads.isNotEmpty())

        val condHeads = heads.filter { head ->
            val atom = head.atom
            val name = atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('\'', '"', '`')
            name == "cond"
        }

        assertTrue("Should find cond in primitives", condHeads.isNotEmpty())
        println("Found ${condHeads.size} cond heads in primitives")
    }

    @Test
    fun testPrimitivesFileContainsMultiplePrimitives() {
        val primitivesPsi = PicatStdlibUtil.getPrimitivesPsiFile(project)
        assertNotNull("Primitives PSI file should be loaded", primitivesPsi)

        val heads = PsiTreeUtil.findChildrenOfType(primitivesPsi, PicatHead::class.java)

        val primitiveNames = listOf("cond", "true", "fail", "false", "not", "once", "throw", "catch")
        val foundPrimitives = mutableListOf<String>()

        for (head in heads) {
            val atom = head.atom
            val name = atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('\'', '"', '`')
            if (name != null && name in primitiveNames) {
                foundPrimitives.add(name)
            }
        }

        println("Found primitives: $foundPrimitives")
        println("Total heads: ${heads.size}")
        assertTrue("Should find cond", "cond" in foundPrimitives)
    }
}
