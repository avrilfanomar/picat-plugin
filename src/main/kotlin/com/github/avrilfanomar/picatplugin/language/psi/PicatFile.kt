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
        // Use PsiTreeUtil to find all facts and function definitions in the file
        val facts = PsiTreeUtil.findChildrenOfType(this, PicatFact::class.java).toList()
        val functionDefs = PsiTreeUtil.findChildrenOfType(this, PicatFunctionDefinition::class.java).toList()
        return facts + functionDefs
    }

    /**
     * Returns all predicate definitions in the file.
     */
    fun getPredicates(): List<PicatPredicateDefinition> =
        PsiTreeUtil.findChildrenOfType(this, PicatPredicateDefinition::class.java).toList()

    /**
     * Returns all function definitions in the file.
     */
    fun getFunctions(): List<PicatFunctionDefinition> =
        PsiTreeUtil.findChildrenOfType(this, PicatFunctionDefinition::class.java).toList()

}
