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
 * The implementation is intentionally simple:
 * - Provides a word scanner for identifiers, comments, and strings.
 * - Enables "Find Usages" primarily for identifiers that represent predicate/function names
 *   (identifiers under PicatAtom elements and generally identifiers).
 */
class PicatFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        // Use the generated lexer wrapped with FlexAdapter.
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
        // Allow finding usages for any PSI element text; our scanner narrows to identifiers.
        return true
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        // If the element appears within a head, it is likely a predicate/function.
        val inHead = element.parent is PicatHead || element.ancestorsAny { it is PicatHead }
        return if (inHead) "predicate/function" else "symbol"
    }

    override fun getDescriptiveName(element: PsiElement): String = element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = element.text
}

private inline fun PsiElement.ancestorsAny(predicate: (PsiElement) -> Boolean): Boolean {
    var curr: PsiElement? = this
    while (curr != null) {
        if (predicate(curr)) return true
        curr = curr.parent
    }
    return false
}
