package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase

/**
 * Reference from an import item (module name) to a Picat stdlib module file under <picat>/lib.
 * This is read-only navigation similar to Java SDK sources.
 */
class PicatImportModuleReference(element: PsiElement, rangeInElement: TextRange) :
    PsiReferenceBase<PsiElement>(element, rangeInElement, false) {

    override fun resolve(): PsiElement? {
        val project = element.project
        val fullText = element.text
        val moduleName = try {
            fullText.substring(rangeInElement.startOffset, rangeInElement.endOffset)
                .trim('`', '\'', '"')
        } catch (_: Exception) {
            fullText.trim('`', '\'', '"')
        }
        val psi = if (moduleName.isEmpty()) {
            null
        } else {
            val vf = PicatStdlibUtil.findStdlibModuleVFile(project, moduleName)
            vf?.let { PsiManager.getInstance(project).findFile(it) }
        }
        return psi
    }

    override fun getVariants(): Array<Any> = emptyArray()
}
