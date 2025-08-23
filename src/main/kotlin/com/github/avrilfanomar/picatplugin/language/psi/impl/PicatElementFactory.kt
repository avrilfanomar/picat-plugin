package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

/**
 * Factory to create Picat PSI elements programmatically.
 * Uses a lightweight dummy file and parses fragments to extract desired nodes.
 */
object PicatElementFactory {

    fun createFile(project: Project, text: String): PsiFile {
        return PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.pi", PicatFileType, text)
    }

    fun createAtom(project: Project, name: String): PicatAtom {
        val file = createFile(project, "$name.")
        return PsiTreeUtil.findChildOfType(file, PicatAtom::class.java)
            ?: error("Failed to create PicatAtom from name: $name")
    }

    /**
     * Creates a name identifier leaf suitable to replace the current name leaf inside PicatAtom.
     * The returned element is the leaf (IDENTIFIER or SINGLE_QUOTED_ATOM) under a PicatAtom.
     */
    fun createNameIdentifier(project: Project, nameText: String): PsiElement {
        val atom = createAtom(project, nameText)
        // Prefer IDENTIFIER, else SINGLE_QUOTED_ATOM
        val id = atom.identifier ?: atom.singleQuotedAtom
        return id ?: error("Failed to create name identifier for: $nameText")
    }
}
