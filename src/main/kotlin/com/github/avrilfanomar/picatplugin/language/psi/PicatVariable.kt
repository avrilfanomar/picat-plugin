package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat variable PSI elements.
 */
interface PicatVariable : PsiNamedElement {
    /**
     * Returns the name of the variable.
     */
    override fun getName(): String?
}
