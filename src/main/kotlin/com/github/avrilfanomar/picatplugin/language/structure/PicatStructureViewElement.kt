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
        element.toString()

    override fun getPresentation(): ItemPresentation {
        return PresentationData(
            element.text ?: element.toString(),
            null,
            null,
            null
        )
    }

    override fun getChildren(): Array<TreeElement> {
        // For PicatFunctionRule, we might not want to show further children in the structure view,
        // or we might want to show specific parts of its body if relevant.
        // For a file root, you'd iterate through its top-level definitions.
        if (element is PicatFunctionRule) {
            return emptyArray() // Example: function rules are leaves in this view
        }

        // If the element is the root of the file (PicatFile), find all function rules.
        // This part needs to be adapted based on the actual root PSI element type.
        // For now, let's assume 'element' could be a PicatFile (or similar root)
        // and we want to show PicatFunctionRule children.
        val children = mutableListOf<TreeElement>()
        element.children.forEach { child ->
            if (child is PicatFunctionRule) {
                children.add(PicatStructureViewElement(child))
            }
            // Add other top-level elements you want to see in the structure view
            // e.g., if (child is PicatPredicateRule) { children.add(PicatStructureViewElement(child)) }
        }
        return children.toTypedArray()
    }
}
