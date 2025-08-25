package com.github.avrilfanomar.picatplugin.language

import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * Provides icons for Picat PSI elements.
 *
 * Minimal implementation: returns the Picat file icon for Picat files and
 * also for top-level Picat elements where a specific icon is not yet defined.
 * This centralizes icon provisioning and makes it easy to extend later
 * (e.g., different icons for predicates, functions, modules).
 */
class PicatIconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        return when (element) {
            is PicatFileImpl -> PicatIcons.FILE
            else -> null
        }
    }
}
