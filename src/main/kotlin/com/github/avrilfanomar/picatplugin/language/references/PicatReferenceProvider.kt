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
 */
class PicatReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        // For now, only handle structure references (predicate/function calls)
        if (element !is PicatStructure) {
            return PsiReference.EMPTY_ARRAY
        }
        
        val structure = element as PicatStructure
        val identifier = structure.getIdentifier() ?: return PsiReference.EMPTY_ARRAY
        
        // Create a reference for the structure
        val range = TextRange(0, identifier.textLength)
        return arrayOf(PicatReference(identifier, range, structure.getArity()))
    }
}