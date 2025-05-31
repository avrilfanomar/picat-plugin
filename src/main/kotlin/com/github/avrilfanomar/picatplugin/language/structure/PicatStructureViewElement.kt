package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatFile
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionDefinition
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement

/**
 * Structure view element for Picat files.
 * Represents elements in the structure view tree.
 */
class PicatStructureViewElement(private val element: PsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        if (element is NavigatablePsiElement) {
            element.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean =
        element is NavigatablePsiElement && element.canNavigate()

    override fun canNavigateToSource(): Boolean =
        element is NavigatablePsiElement && element.canNavigateToSource()

    override fun getAlphaSortKey(): String =
        when (element) {
            is PicatFunctionDefinition -> (element.getName() ?: "") + "/" + element.getArity()
            else -> element.toString()
        }

    override fun getPresentation(): ItemPresentation {
        val presentation = when (element) {
            is PicatFile -> PresentationData(
                element.name,
                "Picat File",
                null,
                null
            )

            is PicatFunctionDefinition -> {
                val name = element.getName()
                val arity = element.getArity()
                PresentationData(
                    "$name/$arity",
                    "Function",
                    null,
                    null
                )
            }

            else -> PresentationData(element.toString(), "", null, null)
        }
        return presentation
    }

    override fun getChildren(): Array<TreeElement> {
        if (element !is PicatFile) return emptyArray()

        val file = element
        val result = mutableListOf<TreeElement>()

        // Add rules
        file.getRules().forEach {
            result.add(PicatStructureViewElement(it))
        }

        // Add functions
        file.getFunctions().forEach {
            result.add(PicatStructureViewElement(it))
        }

        return result.toTypedArray()
    }
}
