package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.PicatIdentifier
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult

/**
 * Reference for Picat language elements.
 * Resolves references to their targets.
 */
class PicatReference(
    element: PicatIdentifier,
    textRange: TextRange,
    private val arity: Int
) : PsiPolyVariantReferenceBase<PicatIdentifier>(element, textRange) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val name = element.text
        val file = element.containingFile as? PicatFile ?: return ResolveResult.EMPTY_ARRAY

        val results = mutableListOf<ResolveResult>()

        // Look for matching function definitions
        file.getFunctions().forEach { function ->
            if (function.getName() == name && function.getArity() == arity) {
                results.add(PsiElementResolveResult(function))
            }
        }

        return results.toTypedArray()
    }

    override fun getVariants(): Array<Any> {
        val file = element.containingFile as? PicatFile ?: return emptyArray()
        val variants = mutableListOf<Any>()

        // Add rules as variants
        file.getRules().forEach { rule ->
            variants.add(rule)
        }

        // Add function definitions as variants
        file.getFunctions().forEach { function ->
            variants.add(function)
        }

        return variants.toTypedArray()
    }
}
