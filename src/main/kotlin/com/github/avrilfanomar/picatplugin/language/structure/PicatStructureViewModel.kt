package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatActionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatNonbacktrackablePredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile

/**
 * Structure view model for Picat files.
 * Defines how the structure view is organized.
 */
class PicatStructureViewModel(psiFile: PsiFile) :
    StructureViewModelBase(psiFile, PicatStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean =
        element.value is PicatFileImpl

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        val value = element.value
        return value is PicatFunctionRule ||
            value is PicatPredicateRule ||
            value is PicatActionRule ||
            value is PicatNonbacktrackablePredicateRule ||
            value is PicatPredicateFact
    }

    override fun getSuitableClasses(): Array<Class<*>> = arrayOf(
        PicatFileImpl::class.java,
        PicatFunctionRule::class.java,
        PicatPredicateRule::class.java,
        PicatActionRule::class.java,
        PicatNonbacktrackablePredicateRule::class.java,
        PicatPredicateFact::class.java
    )
}
