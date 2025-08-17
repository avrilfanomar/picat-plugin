package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.language.parser._PicatLexer
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

/**
 * Provides "Find Usages" support for Picat identifiers.
 *
 * Minimal implementation focuses on rule declarations (identifiers in rule/function heads)
 * and identifier tokens in general for indexing.
 */
class PicatFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        // Index identifier-like tokens, skip comments and strings using the parser definition's token sets
        return DefaultWordsScanner(
            FlexAdapter(_PicatLexer()),
            TokenSet.create(
                PicatTokenTypes.IDENTIFIER
            ),
            TokenSet.create(PicatTokenTypes.COMMENT, PicatTokenTypes.MULTILINE_COMMENT),
            TokenSet.create(PicatTokenTypes.STRING, PicatTokenTypes.SINGLE_QUOTED_ATOM)
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        // We consider an identifier in a rule/function head as a declaration target
        val isIdentifier = psiElement.node?.elementType == PicatTokenTypes.IDENTIFIER
        if (!isIdentifier) return false
        val parent = psiElement.parent
        val grandParent = parent?.parent
        return parent != null && grandParent is PicatHead
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String = "identifier"

    override fun getDescriptiveName(element: PsiElement): String = element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = element.text
}
