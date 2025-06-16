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
        // Only handle structure references (predicate/function calls) with valid identifiers
        // Commenting out logic due to removal of getIdentifier, getArity from PicatStructure interface
        // and change of PicatReference constructor from PicatIdentifier to PicatAtom.
        /*
        if (element !is PicatStructure || element.getIdentifier() == null) { // getIdentifier() is no longer available
            return PsiReference.EMPTY_ARRAY
        }

        // Create a reference for the structure
        val identifier = element.getIdentifier()!! // getIdentifier() is no longer available
        // Assuming identifier was PicatAtom or similar, its type needs to be confirmed.
        // For now, PicatReference expects PicatAtom.
        // val range = TextRange(0, identifier.textLength)
        // return arrayOf(PicatReference(identifierAsPicatAtom, range, element.getArity())) // getArity() is no longer available
        */
        return PsiReference.EMPTY_ARRAY // Stubbed
    }
}
