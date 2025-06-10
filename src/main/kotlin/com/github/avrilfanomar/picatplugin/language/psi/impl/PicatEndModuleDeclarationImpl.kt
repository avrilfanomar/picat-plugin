package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatEndModuleDeclaration
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatEndModuleDeclarationImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatEndModuleDeclaration {

    override fun getEndModuleKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.END_MODULE_KEYWORD)
    }

    override fun getModuleNameElement(): PicatModuleName? {
        // ModuleName is optional in end_module declaration
        return findChildByType(PicatTokenTypes.MODULE_NAME)
    }

    override fun getEorToken(): PsiElement? {
        return findChildByType(PicatTokenTypes.EOR)
    }
}
