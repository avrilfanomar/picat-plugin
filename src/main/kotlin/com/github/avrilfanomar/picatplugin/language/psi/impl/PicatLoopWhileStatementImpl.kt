package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatLoopWhileStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatLoopWhileStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatLoopWhileStatement {

    override fun getLoopKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.LOOP_KEYWORD)
    }

    override fun getBody(): PicatBody? {
        return findChildByType(PicatTokenTypes.BODY)
    }

    override fun getWhileKeyword(): PsiElement? {
        // This should be the WHILE_KEYWORD after the body.
        // findChildByType might find the first one if there were nested loops.
        // A more robust way might be needed if the structure is complex,
        // but for a direct sequence, this should work.
        // The parser creates LOOP_KEYWORD, then BODY, then WHILE_KEYWORD, then EXPRESSION, then EOR.
        // So, findChildByType should find the correct one.
        return findChildByType(PicatTokenTypes.WHILE_KEYWORD)
    }

    override fun getCondition(): PicatExpression? {
        return findChildByType(PicatTokenTypes.EXPRESSION)
    }

    override fun getEorToken(): PsiElement? {
        return findChildByType(PicatTokenTypes.EOR)
    }
}
