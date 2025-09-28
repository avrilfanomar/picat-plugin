package com.github.avrilfanomar.picatplugin.language.psi

import com.intellij.psi.PsiElement

/**
 * Grammar-Kit psiImplUtilClass entry point.
 * Delegates to PicatPsiUtil to keep logic centralized and reusable.
 */
object PicatPsiImplUtil {
    /**
     * Gets the name of a Picat atom element.
     * @param element the atom element to get the name from
     * @return the name of the atom, or null if not available
     */
    @JvmStatic
    fun getName(element: PicatAtom?): String? = PicatPsiUtil.getName(element)

    /**
     * Sets the name of a Picat atom element.
     * @param element the atom element to rename
     * @param newName the new name to set
     * @return the renamed PSI element
     */
    @JvmStatic
    fun setName(element: PicatAtom, newName: String?): PsiElement = PicatPsiUtil.setName(element, newName)

    /**
     * Gets the name identifier element of a Picat atom.
     * @param element the atom element to get the identifier from
     * @return the name identifier PSI element, or null if not available
     */
    @JvmStatic
    fun getNameIdentifier(element: PicatAtom): PsiElement? = PicatPsiUtil.getNameIdentifier(element)
}
