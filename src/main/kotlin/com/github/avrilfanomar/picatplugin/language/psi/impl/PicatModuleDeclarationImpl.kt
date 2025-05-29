package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatModuleDeclaration interface.
 */
class PicatModuleDeclarationImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatModuleDeclaration {
    /**
     * Returns the name of the module.
     */
    override fun getName(): String? {
        // Find the module name element
        val moduleName = PsiTreeUtil.getChildOfType(this, PicatModuleName::class.java)

        // Return the name of the module
        return moduleName?.getName()
    }

    /**
     * Sets the name of the module.
     * Note: This is a read-only implementation, so this method does nothing.
     */
    override fun setName(name: String): PsiElement {
        // This is a read-only implementation, so we just return this
        return this
    }
}
