package com.github.avrilfanomar.picatplugin.language.psi

// import com.intellij.psi.PsiElement // Not strictly needed if only PicatExpression is used and it extends PicatPsiElement/PsiElement

/**
 * Interface for Picat argument list PSI elements.
 * An argument list directly contains expressions as its arguments.
 */
interface PicatArgumentList : PicatPsiElement { // Assuming PicatPsiElement is the base
    /**
     * Returns the expressions in the list, which are the arguments.
     */
    fun getArguments(): List<PicatExpression>
}
