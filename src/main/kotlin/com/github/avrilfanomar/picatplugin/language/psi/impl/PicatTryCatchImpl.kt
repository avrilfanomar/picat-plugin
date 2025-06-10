package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatBody
import com.github.avrilfanomar.picatplugin.language.psi.PicatCatchClauses
import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes
import com.github.avrilfanomar.picatplugin.language.psi.PicatTryCatch
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class PicatTryCatchImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatTryCatch {

    override fun getTryKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.TRY_KEYWORD)
    }

    override fun getTryBody(): PicatBody? {
        // The first BODY child should be the try body.
        return findChildByType(PicatTokenTypes.BODY)
    }

    override fun getCatchKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.CATCH_KEYWORD)
    }

    override fun getCatchClauses(): PicatCatchClauses? {
        return findChildByType(PicatTokenTypes.CATCH_CLAUSES)
    }

    override fun getFinallyKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.FINALLY_KEYWORD)
    }

    override fun getFinallyBody(): PicatBody? {
        // The finally body, if it exists, will be the second BODY child
        // if a CATCH_CLAUSES block (which contains bodies) is also present.
        // Need a more robust way if CATCH_CLAUSES is optional or not present.
        // The parser structure is TRY body CATCH catch_clauses (FINALLY body_finally)? END
        // So, if FINALLY_KEYWORD exists, the BODY immediately following it is the finally body.
        val finallyKeyword = getFinallyKeyword()
        if (finallyKeyword != null) {
            var potentialBody = finallyKeyword.nextSibling
            while (potentialBody != null && potentialBody !is PicatBody) {
                potentialBody = potentialBody.nextSibling
            }
            return potentialBody as? PicatBody
        }
        return null
    }

    override fun getEndKeyword(): PsiElement? {
        return findChildByType(PicatTokenTypes.END_KEYWORD)
    }
}
