package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
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
        return if (moduleList != null) {
            PsiTreeUtil.getChildrenOfTypeAsList(moduleList, PicatModuleName::class.java)
        } else {
            emptyList()
        }
    }

    /**
     * Returns the module list.
     */
    override fun getModuleList(): PsiElement? {
        return children.find { child ->
            PsiTreeUtil.findChildOfType(child, PicatModuleName::class.java) != null
        }
    }
}
