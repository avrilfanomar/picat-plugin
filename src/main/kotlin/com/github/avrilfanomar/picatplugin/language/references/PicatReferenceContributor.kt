package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext

/**
 * Registers references for Picat identifiers.
 *
 * We attach a reference to IDENTIFIER tokens whose parent is a PicatAtom.
 */
class PicatReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement().withLanguage(com.github.avrilfanomar.picatplugin.language.PicatLanguage),
            object : com.intellij.psi.PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val type = element.node?.elementType
                    if (type != PicatTokenTypes.IDENTIFIER && type != PicatTokenTypes.SINGLE_QUOTED_ATOM) {
                        return PsiReference.EMPTY_ARRAY
                    }
                    val range = TextRange(0, element.textLength)
                    return arrayOf(PicatReference(element, range))
                }
            }
        )
    }
}
