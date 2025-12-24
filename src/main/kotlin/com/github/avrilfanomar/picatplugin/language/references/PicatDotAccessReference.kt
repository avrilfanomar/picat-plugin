package com.github.avrilfanomar.picatplugin.language.references

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
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reference for Picat dot access calls (e.g., bp.predicate(...)).
 *
 * Resolves module-qualified calls to their definitions, with special handling
 * for the "bp" module which contains C-defined built-in predicates.
 */
class PicatDotAccessReference(element: PicatDotAccess, rangeInElement: TextRange) :
    PsiPolyVariantReferenceBase<PicatDotAccess>(element, rangeInElement, false) {

    @Suppress("ReturnCount")
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val dotAccess = element
        val moduleQualifier = PicatPsiUtil.getDotAccessModuleQualifier(dotAccess)
        val predicateName = PicatPsiUtil.getDotAccessName(dotAccess)
        val arity = PicatPsiUtil.getDotAccessArity(dotAccess)

        if (moduleQualifier.isNullOrBlank() || predicateName.isNullOrBlank()) {
            return ResolveResult.EMPTY_ARRAY
        }

        // Special handling for "bp" module (C-defined built-ins)
        if (moduleQualifier == "bp") {
            // For now, we don't resolve bp predicates to definitions since they're defined in C
            // In the future, we could create a stub file with signatures
            return ResolveResult.EMPTY_ARRAY
        }

        // For other modules, try to find the module file and resolve to matching heads
        val moduleFile = PicatStdlibUtil.findStdlibModulePsiFile(
            dotAccess.project,
            moduleQualifier
        ) ?: return ResolveResult.EMPTY_ARRAY

        // Find all heads in the module file that match the predicate name and arity
        val heads = PsiTreeUtil.findChildrenOfType(moduleFile, PicatHead::class.java)
        val matches = heads.filter { head ->
            val headName = head.atomName()
            val headArity = head.arity()
            headName == predicateName && headArity == arity
        }

        return matches.map { head ->
            PsiElementResolveResult(headTarget(head)) as ResolveResult
        }.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return results.firstOrNull()?.element
    }

    @Suppress("ReturnCount")
    override fun getVariants(): Array<Any> {
        val dotAccess = element
        val moduleQualifier = PicatPsiUtil.getDotAccessModuleQualifier(dotAccess)

        if (moduleQualifier.isNullOrBlank()) {
            return emptyArray()
        }

        // Special handling for "bp" module
        if (moduleQualifier == "bp") {
            // For now, return empty variants
            // In the future, we could provide completion for known bp predicates
            return emptyArray()
        }

        // For other modules, provide completion for all predicates in the module
        val moduleFile = PicatStdlibUtil.findStdlibModulePsiFile(
            dotAccess.project,
            moduleQualifier
        ) ?: return emptyArray()

        val heads = PsiTreeUtil.findChildrenOfType(moduleFile, PicatHead::class.java)
        val nameArityPairs = heads
            .mapNotNull { head -> head.atomName()?.let { it to head.arity() } }
            .distinct()

        val variants = nameArityPairs.map { (name, arity) ->
            LookupElementBuilder
                .create(name)
                .withTypeText("/$arity", true)
        }

        return variants.toTypedArray()
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
