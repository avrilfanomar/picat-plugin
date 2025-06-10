package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement // Keep if it's directly named

/**
 * Interface for Picat module declaration PSI elements.
 * Represents: MODULE_KEYWORD module_name EOR
 */
interface PicatModuleDeclaration : PicatPsiElement, PsiNamedElement { // PicatPsiElement for base, PsiNamedElement for name
    fun getModuleKeyword(): PsiElement?
    fun getModuleNameElement(): PicatModuleName?
    fun getExportClause(): PicatExportClause? // Added
    fun getImportClause(): PicatImportClause? // Added
    fun getEorToken(): PsiElement?

    // getName() is inherited from PsiNamedElement, implementation will delegate to ModuleNameElement
}

// Need to ensure PicatExportClause and PicatImportClause interfaces exist if they are specific PSI types.
// From PicatModuleParserHelper, these are EXPORT_CLAUSE and IMPORT_CLAUSE ElementTypes.
// Assuming corresponding interfaces PicatExportClause and PicatImportClause exist or will be created.
