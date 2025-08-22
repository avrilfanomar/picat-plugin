package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.parser._PicatLexer
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

/**
 * Find usages provider for Picat language.
 *
 * Narrowed to predicate/function atoms to improve relevance.
 */
class PicatFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        val idTokens = TokenSet.create(
            PicatTokenTypes.IDENTIFIER,
            PicatTokenTypes.SINGLE_QUOTED_ATOM
        )
        val commentTokens = TokenSet.create(
            PicatTokenTypes.COMMENT,
            PicatTokenTypes.MULTILINE_COMMENT
        )
        val stringTokens = TokenSet.create(
            PicatTokenTypes.STRING,
            PicatTokenTypes.SINGLE_QUOTED_ATOM
        )
        return DefaultWordsScanner(
            FlexAdapter(_PicatLexer()),
            idTokens,
            commentTokens,
            stringTokens
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        // We allow usages for Picat atoms or their leaf tokens.
        return when (psiElement) {
            is PicatAtom -> true
            else -> {
                val type = psiElement.node?.elementType
                val isAtomLeaf = type == PicatTokenTypes.IDENTIFIER ||
                    type == PicatTokenTypes.SINGLE_QUOTED_ATOM
                isAtomLeaf && psiElement.parent is PicatAtom
            }
        }
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        val isHead = element.hasAncestor<PicatHead>()
        return if (isHead) "predicate/function" else "symbol"
    }

    override fun getDescriptiveName(element: PsiElement): String = element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = element.text
}

private inline fun <reified T : PsiElement> PsiElement.hasAncestor(): Boolean {
    var curr: PsiElement? = this
    while (curr != null) {
        if (curr is T) return true
        curr = curr.parent
    }
    return false
}
