package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat export statement PSI elements.
 * An export statement in Picat exports rules to be used by other modules.
 */
interface PicatExportStatement : PicatPsiElement {

    /**
     * Returns the export list.
     */
    fun getExportList(): PsiElement?
}
