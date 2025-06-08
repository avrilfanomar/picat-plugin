package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatActorDefinition
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom // For potential getNameIdentifier()
import com.github.avrilfanomar.picatplugin.language.psi.PicatActorMember // For potential getActorMembers()

class PicatActorDefinitionImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatActorDefinition {
    // TODO: Implement methods, e.g.,
    // fun getNameIdentifier(): PicatAtom? = findChildByClass(PicatAtom::class.java) // Assuming actor_name is an atom
    // fun getActorMembers(): List<PicatActorMember> = findChildrenOfType(PicatActorMember::class.java)
}
