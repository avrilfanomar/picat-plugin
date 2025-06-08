package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatActorMember

class PicatActorMemberImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatActorMember {
    // This is a choice rule, specific implementations will handle children.
    // (e.g. PicatActionRuleImpl, PicatPredicateClauseImpl etc.)
    // If PicatActorMember itself has common properties defined in the interface,
    // they could be implemented here or in specific subtypes.
}
