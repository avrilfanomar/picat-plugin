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
        // First try to get direct children
        val directChildren = PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPredicateIndicator::class.java)
        if (directChildren.isNotEmpty()) {
            return directChildren
        }

        // If no direct children, search recursively
        val exportList = getExportList()
        if (exportList != null) {
            return PsiTreeUtil.findChildrenOfType(exportList, PicatPredicateIndicator::class.java).toList()
        }

        return emptyList()
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
