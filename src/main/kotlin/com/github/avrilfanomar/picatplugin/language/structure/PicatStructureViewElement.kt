package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
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
            is PicatFunctionRule -> element.head.structure?.text ?: element.toString() // Use text as fallback
            else -> element.toString()
        }

    override fun getPresentation(): ItemPresentation {
        val presentation = when (element) {
            is PicatFunctionRule -> {
                // val name = element.head.structure?.getName() // getName removed
                // val arity = element.head.structure?.getArity() // getArity removed
                val representation = element.head.structure?.text ?: "Function" // Use text as fallback
                PresentationData(
                    representation, // Simplified representation
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
        val file = element
        val result = mutableListOf<TreeElement>()

        // For now, just adding all direct children as a placeholder.
        file.children.forEach { child ->
            // Add filtering here if necessary, e.g. only PicatFunctionRule, PicatPredicateRule instances
            if (child is PicatFunctionRule) { // Example: only show function rules
                 result.add(PicatStructureViewElement(child))
            }
            // Add other relevant types here
        }

        return result.toTypedArray()
    }
}
