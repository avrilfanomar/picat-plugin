package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatStructure
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * Reference provider for Picat language.
 * Creates references for Picat elements.
 * 
 * Currently only handles structure references (predicate/function calls).
 * Support for other types of references will be added in future updates.
 */
class PicatReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return PsiReference.EMPTY_ARRAY // Stubbed
    }
}
