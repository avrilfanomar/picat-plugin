package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiNamedElement

/**
 * Interface for Picat identifier PSI elements.
 */
interface PicatIdentifier : PsiNamedElement {
    /**
     * Returns the name of the identifier.
     */
    override fun getName(): String?
}
