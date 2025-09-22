package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
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
        } catch (e: Exception) {
            fullText.trim('`', '\'', '"')
        }
        if (moduleName.isEmpty()) return null

        val vf = PicatStdlibUtil.findStdlibModuleVFile(project, moduleName) ?: return null
        return PsiManager.getInstance(project).findFile(vf)
    }


    override fun getVariants(): Array<Any> = emptyArray()
}
