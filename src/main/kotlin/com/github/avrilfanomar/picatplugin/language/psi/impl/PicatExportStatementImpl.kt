package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.github.avrilfanomar.picatplugin.language.psi.PicatExportStatement
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateIndicator
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Implementation of the PicatExportStatement interface.
 */
class PicatExportStatementImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatExportStatement {
    /**
     * Returns the list of predicate indicators in the export statement.
     */
    override fun getPredicateIndicators(): List<PicatPredicateIndicator> {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPredicateIndicator::class.java)
    }

    /**
     * Returns the export list.
     */
    override fun getExportList(): PsiElement? {
        // Find the first child that contains predicate indicators
        return children.find { child ->
            PsiTreeUtil.findChildOfType(child, PicatPredicateIndicator::class.java) != null
        }
    }
}
