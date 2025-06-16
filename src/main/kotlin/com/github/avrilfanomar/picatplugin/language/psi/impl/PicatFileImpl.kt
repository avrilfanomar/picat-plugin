package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.github.avrilfanomar.picatplugin.language.PicatLanguage
import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.psi.PicatPicatFileContent // Corrected interface name
import com.github.avrilfanomar.picatplugin.language.psi.PicatItem_
import com.github.avrilfanomar.picatplugin.language.psi.PicatVisitor

// This class is the root of the PSI tree for a Picat file.
// It implements PicatPicatFileContent, which is the interface generated from the `picat_file_content` rule in the BNF.
class PicatFileImpl(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, PicatLanguage),
    PicatPicatFileContent {

    override fun getFileType(): FileType {
        return PicatFileType
    }

    override fun toString(): String {
        return "Picat File"
    }

    // Implementation for PicatPicatFileContent interface (from 'picat_file_content ::= item_*')
    override fun getItem_List(): List<PicatItem_> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatItem_::class.java)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is PicatVisitor) {
            // PicatVisitor should have visitPicatFileContent(PicatPicatFileContent o)
            // as picat_file_content is a public rule defining this structure.
            visitor.visitPicatFileContent(this)
        } else {
            super.accept(visitor)
        }
    }
}
