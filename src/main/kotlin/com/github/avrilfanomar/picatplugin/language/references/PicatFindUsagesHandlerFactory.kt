package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor

/**
 * Minimal FindUsagesHandlerFactory to ensure that usages for Picat identifiers
 * can be found in tests. It performs a simple name-based search within the
 * provided search scope and sends occurrences to the usage processor.
 *
 * This is intentionally lightweight and complements the reference-based
 * approach, acting as a fallback when references are not picked up by the
 * platform in tests.
 */
class PicatFindUsagesHandlerFactory : FindUsagesHandlerFactory() {
    override fun canFindUsages(element: PsiElement): Boolean {
        if (element.language == PicatLanguage) {
            // Basic Picat identifier pattern: starts with a lowercase letter, then letters/digits/_
            return element.text?.matches(Regex("[a-z][a-zA-Z0-9_]*")) ?: false
        }
        return false
    }

    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler {
        return object : FindUsagesHandler(element) {
            override fun processElementUsages(
                element: PsiElement,
                processor: Processor<in UsageInfo>,
                options: FindUsagesOptions
            ): Boolean {
                return com.intellij.openapi.application.ReadAction.compute<Boolean, RuntimeException> {
                    val project: Project = element.project
                    val helper = PsiSearchHelper.getInstance(project)
                    val scope = options.searchScope
                    val word = element.text
                    // Search for occurrences of the identifier in code
                    helper.processElementsWithWord({ target, _ ->
                        // Prefer reporting the leaf or the parent PSI element as the usage location
                        val usageElement: PsiElement = when (target) {
                            else -> target
                        }
                        processor.process(UsageInfo(usageElement))
                    }, scope, word, UsageSearchContext.IN_CODE, true)
                    true
                }
            }

            override fun getPrimaryElements(): Array<PsiElement> {
                return arrayOf(element)
            }

            override fun getSecondaryElements(): Array<PsiElement> {
                return emptyArray()
            }
        }
    }
}
