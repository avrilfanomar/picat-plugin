package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

// Represents an export specification, e.g., just an atom or atom/arity.
// Parser in PicatModuleParserHelper.parseExportSpec currently just consumes an atom.
// A more complete version would handle atom/arity.
interface PicatExportSpec : PicatPsiElement {
    fun getAtom(): PsiElement? // The IDENTIFIER or QUOTED_ATOM
    // fun getArity(): PsiElement? // For atom/INTEGER style, if supported by parser later
}
