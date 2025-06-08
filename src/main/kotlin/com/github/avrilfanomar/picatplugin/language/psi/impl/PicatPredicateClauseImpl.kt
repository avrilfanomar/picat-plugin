package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatPredicateClause

open class PicatPredicateClauseImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatPredicateClause {
    // This is a choice rule, specific implementations (PicatPredicateRuleImpl, PicatPredicateFactImpl)
    // will handle specific children.
    // Common methods for PicatPredicateClause could be added here if any.
}
