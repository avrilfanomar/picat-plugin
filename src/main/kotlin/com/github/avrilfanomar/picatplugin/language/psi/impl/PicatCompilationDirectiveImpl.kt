package com.github.avrilfanomar.picatplugin.language.psi.impl

import com.intellij.lang.ASTNode
import com.github.avrilfanomar.picatplugin.language.psi.PicatCompilationDirective
import com.github.avrilfanomar.picatplugin.language.psi.PicatTableMode
import com.github.avrilfanomar.picatplugin.language.psi.PicatIndexMode
// import com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes // For PRIVATE_KEYWORD check

class PicatCompilationDirectiveImpl(node: ASTNode) : PicatPsiElementImpl(node), PicatCompilationDirective {
    // TODO: Implement methods, e.g.,
    // fun isPrivate(): Boolean = findChildByType(PicatTokenTypes.PRIVATE_KEYWORD) != null
    // fun getTableMode(): PicatTableMode? = findChildByClass(PicatTableMode::class.java)
    // fun getIndexMode(): PicatIndexMode? = findChildByClass(PicatIndexMode::class.java)
}
