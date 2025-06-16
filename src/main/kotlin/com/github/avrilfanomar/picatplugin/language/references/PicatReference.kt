package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatProgram // Changed PicatFile to PicatProgram
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom // Assuming PicatIdentifier maps to PicatAtom or similar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement // For general PsiElement operations if needed
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult

/**
 * Reference for Picat language elements.
 * Resolves references to their targets.
 */
class PicatReference(
    element: PicatAtom, // Changed PicatIdentifier to PicatAtom
    textRange: TextRange,
    private val arity: Int
) : PsiPolyVariantReferenceBase<PicatAtom>(element, textRange) { // Changed PicatIdentifier to PicatAtom

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        // val name = element.text // element.text should be available for PicatAtom (from PsiElement)
        // val file = element.containingFile as? PicatProgram ?: return ResolveResult.EMPTY_ARRAY
        // val results = mutableListOf<ResolveResult>()

        // // Look for matching function definitions
        // // Commenting out due to missing getFunctions, getName, getArity on generated PSI
        // file.getFunctions().forEach { function ->
        //     if (function.getName() == name && function.getArity() == arity) {
        //         results.add(PsiElementResolveResult(function))
        //     }
        // }
        // return results.toTypedArray()
        return ResolveResult.EMPTY_ARRAY // Stubbed due to missing methods
    }

    override fun getVariants(): Array<Any> {
        // val file = element.containingFile as? PicatProgram ?: return emptyArray()
        // val variants = mutableListOf<Any>()

        // // Add rules as variants
        // // Commenting out due to missing getRules on generated PSI
        // file.getRules().forEach { rule ->
        //     variants.add(rule)
        // }

        // // Add function definitions as variants
        // // Commenting out due to missing getFunctions on generated PSI
        // file.getFunctions().forEach { function ->
        //     variants.add(function)
        // }
        // return variants.toTypedArray()
        return emptyArray() // Stubbed due to missing methods
    }
}
