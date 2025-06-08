package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionClause

open class PicatFunctionClauseImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatFunctionClause {
    // This is a choice rule, specific implementations (PicatFunctionRuleImpl, PicatFunctionFactImpl)
    // will handle specific children.
    // Common methods for PicatFunctionClause could be added here if any.
}
