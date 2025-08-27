package com.github.avrilfanomar.picatplugin.language.navigation

import com.github.avrilfanomar.picatplugin.language.psi.PicatActionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatNonbacktrackablePredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateRule
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateFact
import com.github.avrilfanomar.picatplugin.language.psi.impl.PicatFileImpl
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * Navbar contributor for Picat language.
 *
 * Shows Picat file and top-level predicate/function rules (by name/arity)
 * in the navigation bar.
 */
class PicatStructureAwareNavbar : StructureAwareNavBarModelExtension() {

    override val language: Language
        get() = PicatLanguage

    override fun getParent(psiElement: PsiElement?): PsiElement? {
        return if (psiElement == null || psiElement is PicatFileImpl) {
            null
        } else {
            // Prefer enclosing rule containers
            val container = psiElement.parentOfType(PicatFunctionRule::class.java)
                ?: psiElement.parentOfType(PicatPredicateRule::class.java)
                ?: psiElement.parentOfType(PicatPredicateFact::class.java)
                ?: psiElement.parentOfType(PicatActionRule::class.java)
                ?: psiElement.parentOfType(PicatNonbacktrackablePredicateRule::class.java)

            if (container == null || container === psiElement) psiElement.parent else container
        }
    }

    override fun getPresentableText(obj: Any?): String? {
        return when (obj) {
            is PicatFileImpl -> obj.name
            is PicatHead -> presentHead(obj)
            is PicatFunctionRule -> presentHead(obj.head)
            is PicatPredicateRule -> presentHead(obj.head)
            is PicatActionRule -> presentHead(obj.head)
            is PicatNonbacktrackablePredicateRule -> presentHead(obj.head)
            is PicatPredicateFact -> presentHead(obj.head)
            is PsiElement -> obj.parentOfType(PicatHead::class.java)?.let { presentHead(it) }
            else -> null
        }
    }

    override fun getIcon(`object`: Any?): Icon? {
        // Rely on global IconProvider registration
        return null
    }

    private fun presentHead(head: PicatHead): String {
        val name = head.atom.identifier?.text
            ?: head.atom.singleQuotedAtom?.text?.trim('"', '\'', '`')
            ?: "<head>"
        val arity = head.argumentList.size
        return "$name/$arity"
    }
}

private fun <T> PsiElement.parentOfType(clazz: Class<T>): T? {
    var cur: PsiElement? = this
    var found: T? = null
    while (cur != null && found == null) {
        if (clazz.isInstance(cur)) {
            @Suppress("UNCHECKED_CAST")
            found = cur as T
        } else if (cur is PicatFileImpl) {
            break
        } else {
            cur = cur.parent
        }
    }
    return found
}
