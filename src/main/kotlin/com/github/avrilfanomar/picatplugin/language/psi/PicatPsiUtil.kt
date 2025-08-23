package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatElementFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

/**
 * Utility methods used by generated PSI implementations (Grammar-Kit psiImplUtilClass).
 * Provides getName, setName, and getNameIdentifier for Picat named elements.
 */
object PicatPsiUtil {

    @JvmStatic
    fun getName(element: PicatAtom?): String? {
        val id = element?.identifier ?: element?.singleQuotedAtom
        return id?.text
    }

    @JvmStatic
    fun setName(element: PicatAtom, newName: String?): PsiElement {
        val nameId = getNameIdentifier(element)
        val project: Project = element.project
        if (nameId != null && newName != null) {
            val currentText = nameId.text
            val desired = if (currentText.length >= 2 && currentText.first() == '\'' && currentText.last() == '\'') {
                if (newName.startsWith("'") && newName.endsWith("'")) newName else "'${'$'}newName'"
            } else newName
            val newNameLeaf = PicatElementFactory.createNameIdentifier(project, desired)
            if (nameId is LeafPsiElement && newNameLeaf is LeafPsiElement) {
                nameId.replace(newNameLeaf)
            } else {
                nameId.replace(newNameLeaf)
            }
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: PicatAtom): PsiElement? {
        return element.identifier ?: element.singleQuotedAtom
    }
}
