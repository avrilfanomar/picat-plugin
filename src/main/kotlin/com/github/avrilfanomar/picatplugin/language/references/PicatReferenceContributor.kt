package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Reference contributor for Picat language.
 * Registers reference providers for Picat elements.
 */
class PicatReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Register reference providers for predicate calls
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withLanguage(PicatLanguage),
            PicatReferenceProvider()
        )
    }
}