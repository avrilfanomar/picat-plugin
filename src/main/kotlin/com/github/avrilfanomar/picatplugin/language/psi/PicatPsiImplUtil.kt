package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Grammar-Kit psiImplUtilClass entry point.
 * Delegates to PicatPsiUtil to keep logic centralized and reusable.
 */
object PicatPsiImplUtil {
    @JvmStatic
    fun getName(element: PicatAtom?): String? = PicatPsiUtil.getName(element)

    @JvmStatic
    fun setName(element: PicatAtom, newName: String?): PsiElement = PicatPsiUtil.setName(element, newName)

    @JvmStatic
    fun getNameIdentifier(element: PicatAtom): PsiElement? = PicatPsiUtil.getNameIdentifier(element)
}
