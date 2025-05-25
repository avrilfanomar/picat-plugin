package com.github.avrilfanomar.picatplugin.language.psi

import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil

/**
 * Represents a Picat file in the PSI structure.
 * Provides methods for accessing file-level elements.
 */
class PicatFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PicatLanguage) {
    override fun getFileType(): FileType = PicatFileType.INSTANCE

    override fun toString(): String = "Picat File"

    /**
     * Returns all facts in the file, including function definitions.
     * This is needed because in Picat, function definitions like "factorial(0) = 1." are also facts.
     */
    fun getAllFacts(): List<PicatFact> {
        val facts = findChildrenByClass(PicatFact::class.java).toList()
        val functionDefs = findChildrenByClass(PicatFunctionDefinition::class.java).toList()
        return facts + functionDefs
    }

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

    /**
     * Returns all export statements in the file.
     */
    fun getExportStatements(): List<PicatExportStatement> =
        findChildrenByClass(PicatExportStatement::class.java).toList()

    /**
     * Returns all include statements in the file.
     */
    fun getIncludeStatements(): List<PicatIncludeStatement> =
        findChildrenByClass(PicatIncludeStatement::class.java).toList()

    /**
     * Returns all facts in the file.
     */
    fun getFacts(): List<PicatFact> =
        findChildrenByClass(PicatFact::class.java).toList()

    /**
     * Returns all rules in the file.
     */
    fun getRules(): List<PicatRule> =
        findChildrenByClass(PicatRule::class.java).toList()
}
