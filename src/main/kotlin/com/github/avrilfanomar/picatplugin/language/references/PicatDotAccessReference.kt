package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.cache.PicatPsiCache
import com.github.avrilfanomar.picatplugin.language.psi.PicatDotAccess
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiUtil
import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult

/**
 * Reference for Picat dot access calls (e.g., bp.predicate(...)).
 *
 * Resolves module-qualified calls to their definitions, with special handling
 * for the "bp" module which contains C-defined built-in predicates.
 */
class PicatDotAccessReference(element: PicatDotAccess, rangeInElement: TextRange) :
    PsiPolyVariantReferenceBase<PicatDotAccess>(element, rangeInElement, false) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val dotAccess = element
        val moduleQualifier = PicatPsiUtil.getDotAccessModuleQualifier(dotAccess)?.takeIf { it.isNotBlank() }
        val predicateName = PicatPsiUtil.getDotAccessName(dotAccess)?.takeIf { it.isNotBlank() }

        // Require valid non-bp module qualifier and predicate name
        if (moduleQualifier == null || predicateName == null || moduleQualifier == "bp") {
            return ResolveResult.EMPTY_ARRAY
        }
        return resolveFromModuleFile(dotAccess, moduleQualifier, predicateName)
    }

    private fun resolveFromModuleFile(
        dotAccess: PicatDotAccess,
        moduleQualifier: String,
        predicateName: String
    ): Array<ResolveResult> {
        val moduleFile = PicatStdlibUtil.findStdlibModulePsiFile(dotAccess.project, moduleQualifier)
            ?: return ResolveResult.EMPTY_ARRAY

        val arity = PicatPsiUtil.getDotAccessArity(dotAccess)
        val cache = PicatPsiCache.getInstance(dotAccess.project)
        return cache.getFileHeads(moduleFile)
            .filter { it.atomName() == predicateName && it.arity() == arity }
            .map { PsiElementResolveResult(headTarget(it)) as ResolveResult }
            .toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return results.firstOrNull()?.element
    }

    override fun getVariants(): Array<Any> {
        val dotAccess = element
        val moduleQualifier = PicatPsiUtil.getDotAccessModuleQualifier(dotAccess)
            ?.takeIf { it.isNotBlank() && it != "bp" } ?: return emptyArray()

        return collectVariantsFromModule(dotAccess, moduleQualifier)
    }

    private fun collectVariantsFromModule(dotAccess: PicatDotAccess, moduleQualifier: String): Array<Any> {
        val moduleFile = PicatStdlibUtil.findStdlibModulePsiFile(dotAccess.project, moduleQualifier)
            ?: return emptyArray()

        val cache = PicatPsiCache.getInstance(dotAccess.project)
        return cache.getFileHeads(moduleFile)
            .mapNotNull { head -> head.atomName()?.let { it to head.arity() } }
            .distinct()
            .map { (name, arity) ->
                LookupElementBuilder.create(name).withTypeText("/$arity", true)
            }
            .toTypedArray()
    }

    private fun headTarget(h: PicatHead): PsiElement {
        return h.atom.identifier ?: h.atom
    }
}

private fun PicatHead.arity(): Int = this.headArgs?.argumentList?.size ?: 0

private fun PicatHead.atomName(): String? {
    val a = this.atom
    return a.identifier?.text ?: a.singleQuotedAtom?.text?.trim('\'', '"', '`')
}
