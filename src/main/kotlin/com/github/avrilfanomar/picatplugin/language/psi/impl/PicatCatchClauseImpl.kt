package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatCatchClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // Or PicatPattern
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatCatchClauseImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCatchClause {

    override fun getPattern(): PicatExpression? {
        // Parser calls expressionParser.parsePattern()
        // This should result in a PATTERN element.
        return findChildByType(PicatTokenTypes.PATTERN)
    }

    override fun getArrowOperator(): PsiElement? {
        return findChildByType(PicatTokenTypes.ARROW_OP)
    }

    override fun getBody(): PicatBody? {
        return findChildByType(PicatTokenTypes.BODY)
    }
}
