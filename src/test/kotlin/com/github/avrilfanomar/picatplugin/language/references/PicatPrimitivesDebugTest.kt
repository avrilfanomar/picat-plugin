package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

/**
 * Debug test to understand why primitives aren't resolving
 */
class PicatPrimitivesDebugTest : BasePlatformTestCase() {

    @Test
    fun testPrimitivesFileLoads() {
        val primitivesPsi = PicatStdlibUtil.getPrimitivesPsiFile(project)
        assertNotNull("Primitives PSI should load", primitivesPsi)
        println("Primitives PSI: ${primitivesPsi?.name}")
        println("Primitives text length: ${primitivesPsi?.text?.length}")
        
        val heads = PsiTreeUtil.findChildrenOfType(primitivesPsi, PicatHead::class.java)
        println("Number of heads in primitives: ${heads.size}")
        
        heads.take(10).forEach { head ->
            val atom = head.atom
            val name = atom.identifier?.text ?: atom.singleQuotedAtom?.text?.trim('\'', '"', '`')
            val arity = head.headArgs?.argumentList?.size ?: 0
            println("  - $name/$arity")
        }
    }

    @Test
    fun testReferenceFindsAtomOrCallNoLambda() {
        val file = myFixture.configureByText(
            "test.pi",
            """
            fibc(N) = cond((N == 0; N == 1), 1, fibc(N - 1) + fibc(N - 2)).
            """.trimIndent()
        )

        val caretOffset = file.text.indexOf("cond")
        println("Caret offset: $caretOffset")
        println("Text at offset: ${file.text.substring(caretOffset, caretOffset + 10)}")
        
        val element = file.findElementAt(caretOffset)
        println("Element at offset: $element")
        println("Element class: ${element?.javaClass}")
        println("Element parent: ${element?.parent}")
        println("Element parent class: ${element?.parent?.javaClass}")
        
        val ref = file.findReferenceAt(caretOffset)
        println("Reference: $ref")
        println("Reference class: ${ref?.javaClass}")
        
        if (ref != null) {
            val resolved = ref.resolve()
            println("Resolved: $resolved")
            println("Resolved file: ${resolved?.containingFile?.name}")
        }
    }
}
