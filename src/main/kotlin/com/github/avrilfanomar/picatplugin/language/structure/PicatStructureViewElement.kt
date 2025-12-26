package com.github.avrilfanomar.picatplugin.language.structure

import com.github.avrilfanomar.picatplugin.language.psi.PicatActionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatNonbacktrackablePredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

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

    override fun getAlphaSortKey(): String = getPresentableText()

    override fun getPresentation(): ItemPresentation {
        return PresentationData(
            getPresentableText(),
            null,
            null,
            null
        )
    }

    override fun getChildren(): Array<TreeElement> {
        // Rules are leaves in the structure view
        if (isRuleElement(element)) {
            return emptyArray()
        }

        // For the file root, find all top-level rules
        val children = mutableListOf<TreeElement>()
        if (element is PicatFileImpl) {
            // Collect all rule types from the file
            PsiTreeUtil.findChildrenOfType(element, PicatFunctionRule::class.java)
                .forEach { children.add(PicatStructureViewElement(it)) }
            PsiTreeUtil.findChildrenOfType(element, PicatPredicateRule::class.java)
                .forEach { children.add(PicatStructureViewElement(it)) }
            PsiTreeUtil.findChildrenOfType(element, PicatActionRule::class.java)
                .forEach { children.add(PicatStructureViewElement(it)) }
            PsiTreeUtil.findChildrenOfType(element, PicatNonbacktrackablePredicateRule::class.java)
                .forEach { children.add(PicatStructureViewElement(it)) }
            PsiTreeUtil.findChildrenOfType(element, PicatPredicateFact::class.java)
                .forEach { children.add(PicatStructureViewElement(it)) }
        }
        return children.toTypedArray()
    }

    private fun getPresentableText(): String {
        return when (element) {
            is PicatFileImpl -> element.name
            is PicatFunctionRule -> presentHead(element.head)
            is PicatPredicateRule -> presentHead(element.head)
            is PicatActionRule -> presentHead(element.head)
            is PicatNonbacktrackablePredicateRule -> presentHead(element.head)
            is PicatPredicateFact -> presentHead(element.head)
            else -> element.text?.take(MAX_TEXT_LENGTH) ?: element.toString()
        }
    }

    private fun presentHead(head: PicatHead): String {
        val name = head.atom.identifier?.text
            ?: head.atom.singleQuotedAtom?.text?.trim('"', '\'', '`')
            ?: "<head>"
        val arity = head.headArgs?.argumentList?.size ?: 0
        return "$name/$arity"
    }

    private fun isRuleElement(element: PsiElement): Boolean {
        return element is PicatFunctionRule ||
            element is PicatPredicateRule ||
            element is PicatActionRule ||
            element is PicatNonbacktrackablePredicateRule ||
            element is PicatPredicateFact
    }

    /** Constants for structure view element presentation. */
    companion object {
        private const val MAX_TEXT_LENGTH = 50
    }
}
