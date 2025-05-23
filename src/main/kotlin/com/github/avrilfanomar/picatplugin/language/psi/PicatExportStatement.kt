package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Interface for Picat export statement PSI elements.
 * An export statement in Picat exports predicates to be used by other modules.
 */
interface PicatExportStatement : PicatPsiElement {
    /**
     * Returns the list of predicate indicators in the export statement.
     */
    fun getPredicateIndicators(): List<PicatPredicateIndicator>

    /**
     * Returns the export list.
     */
    fun getExportList(): PsiElement?
}
