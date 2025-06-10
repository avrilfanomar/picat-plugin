package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatCaseArm
import com.github.avrilfanomar.picatplugin.language.psi.PicatExpression // Or PicatPattern if specific
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatCaseArmImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCaseArm {

    override fun getPattern(): PicatExpression? {
        // The parser calls expressionParser.parsePattern() for the pattern part of a case arm.
        // This likely creates a PATTERN element. PicatPattern interface might be more specific if it exists.
        // For now, assuming PATTERN is a type of PicatExpression or can be found as such.
        // Let's assume the parser creates a node of type PicatTokenTypes.PATTERN
        return findChildByType(PicatTokenTypes.PATTERN)
    }

    override fun getArrowOperator(): PsiElement? {
        return findChildByType(PicatTokenTypes.ARROW_OP)
    }

    override fun getBody(): PicatBody? {
        return findChildByType(PicatTokenTypes.BODY)
    }
}
