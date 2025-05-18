package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

/**
 * Represents a Picat file in the PSI structure.
 * Provides methods for accessing file-level elements.
 */
class PicatFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PicatLanguage) {
    override fun getFileType(): FileType = PicatFileType.INSTANCE
    
    override fun toString(): String = "Picat File"
    
    /**
     * Returns all predicate definitions in the file.
     */
    fun getPredicates(): List<PicatPredicateDefinition> = 
        findChildrenByClass(PicatPredicateDefinition::class.java).toList()
        
    /**
     * Returns all function definitions in the file.
     */
    fun getFunctions(): List<PicatFunctionDefinition> = 
        findChildrenByClass(PicatFunctionDefinition::class.java).toList()
    
    /**
     * Returns the module declaration in the file, if any.
     */
    fun getModuleDeclaration(): PicatModuleDeclaration? =
        findChildByClass(PicatModuleDeclaration::class.java)
    
    /**
     * Returns all import statements in the file.
     */
    fun getImportStatements(): List<PicatImportStatement> =
        findChildrenByClass(PicatImportStatement::class.java).toList()
}