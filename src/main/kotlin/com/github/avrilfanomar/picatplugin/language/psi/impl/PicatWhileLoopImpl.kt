package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.PicatWhileLoop
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatWhileLoopImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatWhileLoop {

    override fun getWhileKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.WHILE_KEYWORD)
    }

    override fun getCondition(): PicatExpression? {
        return findChildByType(PicatTokenTypes.EXPRESSION)
    }

    override fun getDoKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.DO_KEYWORD)
    }

    override fun getBody(): PicatBody? {
        return findChildByType(PicatTokenTypes.BODY)
    }

    override fun getEndKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.END_KEYWORD)
    }
}
