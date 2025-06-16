package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatPicatFileContent // Corrected Interface
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
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
        element.value is PicatPicatFileContent // Corrected Interface

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean =
        element.value is PicatFunctionRule

    override fun getSuitableClasses(): Array<Class<*>> = arrayOf(
        PicatPicatFileContent::class.java, // Corrected Interface
        PicatFunctionRule::class.java
    )
}
