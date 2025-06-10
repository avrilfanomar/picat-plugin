package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatCaseArms
import com.github.avrilfanomar.picatplugin.language.psi.PicatCaseExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatCaseExpressionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCaseExpression {

    override fun getCaseKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.CASE_KEYWORD)
    }

    override fun getExpression(): PicatExpression? {
        // The parser rule for case_expression calls expressionParser.parseExpression,
        // which wraps the result in an EXPRESSION element type.
        return findChildByType(PicatTokenTypes.EXPRESSION)
    }

    override fun getOfKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.OF_KEYWORD)
    }

    override fun getArms(): PicatCaseArms? {
        return findChildByType(PicatTokenTypes.CASE_ARMS)
    }

    override fun getEndKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.END_KEYWORD)
    }
}
