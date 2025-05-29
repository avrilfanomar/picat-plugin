package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatImportStatement interface.
 */
class PicatImportStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatImportStatement {
    /**
     * Returns the list of module names in the import statement.
     */
    override fun getModuleNames(): List<PicatModuleName> {
        val moduleList = getModuleList()

        if (moduleList != null) {
            val moduleSpecs = PsiTreeUtil.findChildrenOfType(moduleList, PsiElement::class.java)
                .filter { it.node.elementType == PicatTokenTypes.MODULE_SPEC }

            val moduleNames = moduleSpecs.flatMap { moduleSpec ->
                PsiTreeUtil.findChildrenOfType(moduleSpec, PicatModuleName::class.java)
            }

            return moduleNames
        }

        return emptyList()
    }

    /**
     * Returns the module list.
     */
    override fun getModuleList(): PsiElement? {
        return findChildByType(PicatTokenTypes.IMPORT_LIST)
    }
}
