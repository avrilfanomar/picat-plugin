package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatCatchClause
import com.github.avrilfanomar.picatplugin.language.psi.PicatCatchClauses
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PicatCatchClausesImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCatchClauses {

    override fun getCatchClauseList(): List<PicatCatchClause> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatCatchClause::class.java)
        // Alternative, if CATCH_CLAUSE is a token type for the rule:
        // return findChildrenByType(PicatTokenTypes.CATCH_CLAUSE)
    }
}
