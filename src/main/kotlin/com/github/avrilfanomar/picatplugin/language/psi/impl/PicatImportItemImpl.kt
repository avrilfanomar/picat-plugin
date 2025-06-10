package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.language.psi.PicatModuleName
import com.github.avrilfanomar.picatplugin.language.psi.PicatRenameList
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode

class PicatImportItemImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatImportItem {
    override fun getModuleNameElement(): PicatModuleName? {
        return findChildByType(PicatTokenTypes.MODULE_NAME)
    }

    override fun getRenameList(): PicatRenameList? {
        return findChildByType(PicatTokenTypes.RENAME_LIST)
    }
}
